package com.hengchongkeji.constantcharge.data.source;

import android.content.Context;
import android.text.TextUtils;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.google.gson.reflect.TypeToken;
import com.hengchongkeji.constantcharge.ChargeApplication;
import com.hengchongkeji.constantcharge.R;
import com.hengchongkeji.constantcharge.data.entity.ChargeDetailData;
import com.hengchongkeji.constantcharge.data.entity.CurrentVoltage;
import com.hengchongkeji.constantcharge.data.entity.Equipment;
import com.hengchongkeji.constantcharge.data.entity.Station;
import com.hengchongkeji.constantcharge.data.entity.Temperature;
import com.hengchongkeji.constantcharge.http.BaseAction;
import com.hengchongkeji.constantcharge.http.BaseResponse;
import com.hengchongkeji.constantcharge.http.IHttpRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by gopayChan on 2017/4/20.
 */

public class DataFactory {

    private DataFactory() {
    }

    private static DataFactory mInstance = new DataFactory();

    public static DataFactory getInstance() {
        return mInstance;
    }

    public IDataSource getDataSource(boolean fromLocal) {
        return fromLocal ? new DataLocalSource() : new DataRemoteSource();
    }


    private class DataLocalSource implements IDataSource {

        @Override
        public void getChargeDetailData(Context context, IHttpRequest.OnResponseListener<ChargeDetailData> listener) {
            Random random = new Random();
            ChargeDetailData chargeDetailData = new ChargeDetailData();
            chargeDetailData.mPercent = String.valueOf(random.nextInt(100));
            chargeDetailData.mCompleteTime = "5小时6分";
            chargeDetailData.mChargeTime = "8小时15分";
            chargeDetailData.mChargeMoney = "¥250元";
            chargeDetailData.mChargeCount = "200.4kw/h";
            chargeDetailData.mMaxTemperature = String.valueOf(100 + random.nextInt(10));
            chargeDetailData.mMinTemperature = "50";
            chargeDetailData.mMaxVoltages = String.valueOf(700 + random.nextInt(10));
            chargeDetailData.mMinVoltages = "500";
            List<CurrentVoltage> currentVoltages = new ArrayList<>();
            List<Temperature> temperatures = new ArrayList<>();
            for (int i = 0; i < 24; i++) {
                CurrentVoltage currentVoltage = new CurrentVoltage();
                Temperature temperature = new Temperature();
                currentVoltage.hours = String.valueOf(i + 1) + "h";
                temperature.hour = String.valueOf(i + 1) + "h";
                currentVoltage.currentVoltage = String.valueOf(600 + (random.nextInt(100) * ((random.nextInt(2) % 2) == 1 ? 1 : -1)));
                temperature.temperature = String.valueOf(50 + random.nextInt(50));
                currentVoltages.add(currentVoltage);
                temperatures.add(temperature);
            }
            chargeDetailData.mCurrentVoltages = currentVoltages;
            chargeDetailData.mTemperatures = temperatures;
            listener.onSuccess(chargeDetailData);
        }

        @Override
        public void getChargeBalance(IHttpRequest.OnResponseListener<String> listener) {
            Random random = new Random();
            listener.onSuccess(String.valueOf(random.nextInt(1000)));
        }

        @Override
        public void getLatLngNearby(Context context, LatLng curLatLng, IHttpRequest.OnResponseListener<List<Station>> listener) {
            CharSequence[] textArray = ChargeApplication.getApplicationComponent().context().getResources().getTextArray(R.array.LatLngNearby);
            List<Station> makerInfos = new ArrayList<>();
            for (int i = 0; i < textArray.length; i++) {
                LatLng latLng = new LatLng(Double.valueOf(textArray[i].toString()), Double.valueOf(textArray[i + 1].toString()));
                Station makerInfo = new Station();
                makerInfo.setLatLng(latLng);
                makerInfo.setAddress(textArray[i + 2].toString());
                makerInfo.setTotalPile(textArray[i + 3].toString());
                makerInfo.setFreePile(textArray[i + 4].toString());
                makerInfo.setPredictFreePileTime(textArray[i + 5].toString());
                makerInfo.setDistance(String.valueOf((int) DistanceUtil.getDistance(curLatLng, latLng)));
                makerInfo.setPredictReachTime((int) (Double.valueOf(makerInfo.getDistance()) / 1000) + "分钟");
                i += 5;
                makerInfos.add(makerInfo);
            }
            Collections.sort(makerInfos);
            listener.onSuccess(makerInfos);
        }

        @Override
        public void getFoundAdImgUrl(IHttpRequest.OnResponseListener<int[]> listener) {
            int[] ints = new int[]{R.drawable.found_ad_item_0, R.drawable.found_ad_item_1, R.drawable.found_ad_item_2};
//            CharSequence[] textArray = ChargeApplication.getApplicationComponent().context().getResources().getTextArray(R.array.AdImgUrl);
//            String[] strings = new String[textArray.length];
//            for (int i = 0; i < textArray.length; i++) {
//                strings[i] = textArray[i].toString();
//            }
            listener.onSuccess(ints);
        }

        @Override
        public void getIntroductionImgUrl(IHttpRequest.OnResponseListener<int[]> listener) {
            int[] ints = new int[]{R.drawable.introduction_img_0, R.drawable.introduction_img_1, R.drawable.introduction_img_2};
            listener.onSuccess(ints);
        }
    }

    private class DataRemoteSource extends BaseAction implements IDataSource {
        private final String GET_LAT_LNG_NEAR_BY = BASE_URL + "queryAllStation";

        @Override
        public void getChargeDetailData(Context context, IHttpRequest.OnResponseListener<ChargeDetailData> listener) {

        }

        @Override
        public void getChargeBalance(IHttpRequest.OnResponseListener<String> listener) {

        }

        @Override
        public void getLatLngNearby(Context context, final LatLng curLatLng, final IHttpRequest.OnResponseListener<List<Station>> listener) {
            getRequest(context).post(GET_LAT_LNG_NEAR_BY, new String[]{}, new String[]{}, TypeToken.get(LatLngNearbyResponse.class), new IHttpRequest.OnResponseListener<LatLngNearbyResponse>() {
                @Override
                public void onSuccess(LatLngNearbyResponse o) {
                    for (Station station : o.stationInfo) {
                        LatLng latLng = new LatLng(Double.valueOf(station.getStationLat()), Double.valueOf(station.getStationLng()));
                        station.setLatLng(latLng);
                        int i = 0;
                        for (Equipment equipment : station.getList()) {
                            if (TextUtils.equals("1", equipment.getStatus())) {
                                i += 1;
                            }
                        }
                        station.setFreePile(String.valueOf(i));
                        station.setTotalPile(String.valueOf(station.getList().size()));
                        station.setDistance(String.valueOf((int) DistanceUtil.getDistance(curLatLng, latLng)));
                        station.setPredictReachTime((int) (Double.valueOf(station.getDistance()) / 1000) + "分钟");
                    }
                    Collections.sort(o.getStationInfo());
                    listener.onSuccess(o.getStationInfo());
                }

                @Override
                public void onFail(String errorMsg) {
                    listener.onFail(errorMsg);
                }
            });
        }

        @Override
        public void getFoundAdImgUrl(IHttpRequest.OnResponseListener<int[]> listener) {

        }

        @Override
        public void getIntroductionImgUrl(IHttpRequest.OnResponseListener<int[]> listener) {

        }
    }

    static class LatLngNearbyResponse extends BaseResponse {

        public List<Station> getStationInfo() {
            return stationInfo;
        }

        public void setStationInfo(List<Station> stationInfo) {
            this.stationInfo = stationInfo;
        }

        private List<Station> stationInfo;
    }
}
