package com.hengchongkeji.constantcharge.main.home.map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.hengchongkeji.constantcharge.ChargeApplication;
import com.hengchongkeji.constantcharge.R;
import com.hengchongkeji.constantcharge.ViewHolder;
import com.hengchongkeji.constantcharge.base.BaseFragment;
import com.hengchongkeji.constantcharge.charge.ChargeDetailActivity;
import com.hengchongkeji.constantcharge.data.entity.Station;
import com.hengchongkeji.constantcharge.data.source.DataFactory;
import com.hengchongkeji.constantcharge.executor.ThreadExecutor;
import com.hengchongkeji.constantcharge.http.IHttpRequest;
import com.hengchongkeji.constantcharge.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;

import static com.hengchongkeji.constantcharge.charge.ChargeDetailActivity.TO_CHARGE_DETAIL_ACTIVITY_ARGS;

/**
 * Created by gopayChan on 2017/4/26.
 */

public class ChargeListFragment extends BaseFragment {

    @Bind(R.id.chargeListLvId)
    ListView mListview;
    @Inject
    ThreadExecutor mThreadExecutor;
    private List<Station> mMapMarkerInfos;
    private final static int LOAD_DATA_SUCCESS = 1;
    private BaseAdapter mAdapter;

    public static ChargeListFragment getInstance() {
        ChargeListFragment fragment = new ChargeListFragment();
        fragment.mMapMarkerInfos = new ArrayList<>();
        return fragment;
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOAD_DATA_SUCCESS:
                    List<Station> infos = (List<Station>) msg.obj;
                    mMapMarkerInfos.clear();
                    if (infos != null)
                        mMapMarkerInfos.addAll(infos);
                    mAdapter.notifyDataSetChanged();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            MainActivity activity = (MainActivity) context;
            activity.registerOnLocationChangeListener(new MainActivity.OnLocationChangeListener() {
                @Override
                public void onChange(BDLocation location) {
                    resetData();
                }

                @Override
                public void onFail(String failStr) {
                    Message msg = Message.obtain();
                    msg.obj = null;
                    msg.what = LOAD_DATA_SUCCESS;
                    mHandler.sendMessage(msg);
                }
            });
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mThreadExecutor = ChargeApplication.getApplicationComponent().threadExecutor();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_charge_list;
    }

    @Override
    protected void postOnCreateView() {
        super.postOnCreateView();
        mAdapter = new ChargeListAdapter();
        mListview.setAdapter(mAdapter);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            resetData();
        }
    }

    private void resetData() {
        mThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                BDLocation location = ChargeApplication.getInstance().getCurLocation();
                if (location != null) {
                    DataFactory.getInstance().getDataSource(false).getLatLngNearby(getActivity(), new LatLng(location.getLatitude(), location.getLongitude()), new IHttpRequest.OnResponseListener<List<Station>>() {
                        @Override
                        public void onSuccess(List<Station> mapMarkerInfos) {
                            Message msg = Message.obtain();
                            msg.obj = mapMarkerInfos;
                            msg.what = LOAD_DATA_SUCCESS;
                            mHandler.sendMessage(msg);
                        }

                        @Override
                        public void onFail(String errorMsg) {

                        }
                    });

                }
            }
        });
    }

    private class ChargeListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mMapMarkerInfos == null ? 0 : mMapMarkerInfos.size();
        }

        @Override
        public Station getItem(int position) {
            return mMapMarkerInfos == null ? null : mMapMarkerInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.listview_item_charge_list, parent, false);

            }
            TextView addressTv = ViewHolder.get(convertView, R.id.listItemChargeListAddressTvId);
            TextView totalPileTv = ViewHolder.get(convertView, R.id.listItemChargeListTotalTvId);
            TextView freePileTv = ViewHolder.get(convertView, R.id.listItemChargeListFreeTvId);
            TextView distanceTv = ViewHolder.get(convertView, R.id.listItemChargeListDistanceTvId);
            TextView navigationTv = ViewHolder.get(convertView, R.id.listItemChargeListNaviTvId);
            final Station info = getItem(position);
            addressTv.setText(info.getAddress());
            totalPileTv.setText(getString(R.string.charge_map_popup_total_pile).replace("{}", info.getTotalPile()));
            freePileTv.setText(getString(R.string.charge_map_popup_free_pile).replace("{}", info.getFreePile()));
            if (info.getDistance().length() > 3) {
                distanceTv.setText(String.format("%.1f", Double.valueOf(info.getDistance()) / 1000) + "km");
            } else {
                distanceTv.setText(info.getDistance() + "m");
            }
            navigationTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Activity activity = getActivity();
                    if (activity != null) {
                        ((MainActivity) activity).mMainPresenter.routePlanToNavi(info.getLatLng(), info.getAddress());
                    }
                }
            });

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ChargeDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(TO_CHARGE_DETAIL_ACTIVITY_ARGS, info);
                    startActivity(intent);
                }
            });
            return convertView;
        }
    }
}
