package com.hengchongkeji.constantcharge.data.source;

import android.content.Context;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.google.gson.reflect.TypeToken;
import com.hengchongkeji.constantcharge.ChargeApplication;
import com.hengchongkeji.constantcharge.R;
import com.hengchongkeji.constantcharge.data.entity.Equipment;
import com.hengchongkeji.constantcharge.data.entity.Station;
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
        public void getEquipmentData(Context context, String s, IHttpRequest.OnResponseListener<Equipment> listener) {
            Random random = new Random();
            Equipment equipment = new Equipment();
            equipment.setPercent("1");
//            chargeDetailData.mCompleteTime = "5小时6分";
            equipment.setStartTime("0分");
            equipment.setElectriciryS("0w/h");
            equipment.setChargeVoltage(String.valueOf(600 + (random.nextInt(100) * ((random.nextInt(2) % 2) == 1 ? 1 : -1))));
            equipment.setChargeCurrent(String.valueOf(50 + random.nextInt(50)));
            listener.onSuccess(equipment);
        }

        @Override
        public void startEquipment(Context context, String pileId, IHttpRequest.OnResponseListener listener) {
            listener.onSuccess(null);
        }

        @Override
        public void stopEquipment(Context context, IHttpRequest.OnResponseListener listener) {
            listener.onSuccess(null);
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

        @Override
        public void getStationFeeByIDd(Context context, String stationId, IHttpRequest.OnResponseListener listener) {

        }
    }

    private class DataRemoteSource extends BaseAction implements IDataSource {
        private final String GET_LAT_LNG_NEAR_BY_URL = BASE_URL + "queryAllStation";
        private final String GET_STATION_FEE_URL = BASE_URL + "queryFee";
        private final String GET_PILE_DATA_URL = BASE_URL + "chargeInfo";
        private final String START_CHARGE_URL = BASE_URL + "charge";
        private final String STOP_CHARGE_URL = BASE_URL + "stop";

        @Override
        public void getChargeBalance(IHttpRequest.OnResponseListener<String> listener) {

        }

        @Override
        public void getLatLngNearby(Context context, final LatLng curLatLng, final IHttpRequest.OnResponseListener<List<Station>> listener) {
            getRequest(context).post(GET_LAT_LNG_NEAR_BY_URL, new String[]{}, new String[]{}, TypeToken.get(LatLngNearbyResponse.class), new IHttpRequest.OnResponseListener<LatLngNearbyResponse>() {
                @Override
                public void onSuccess(LatLngNearbyResponse o) {
                    for (Station station : o.stationInfo) {
                        LatLng latLng = new LatLng(Double.valueOf(station.getStationLat()), Double.valueOf(station.getStationLng()));
                        station.setLatLng(latLng);
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

        @Override
        public void getStationFeeByIDd(Context context, String stationId, final IHttpRequest.OnResponseListener<Station> listener) {
            getRequest(context).post(GET_STATION_FEE_URL, new String[]{"stationId"}, new String[]{stationId}, TypeToken.get(StationFeeResponse.class), new IHttpRequest.OnResponseListener<StationFeeResponse>() {
                @Override
                public void onSuccess(StationFeeResponse o) {
                    listener.onSuccess(o.getData());
                }

                @Override
                public void onFail(String errorMsg) {
                    listener.onFail(errorMsg);
                }
            });
        }

        @Override
        public void getEquipmentData(Context context, String pileId, final IHttpRequest.OnResponseListener<Equipment> listener) {
            getRequest(context).post(GET_PILE_DATA_URL, new String[]{"equipmentId"}, new String[]{pileId}, new IHttpRequest.OnResponseListener<EquipmentResponse>() {
                @Override
                public void onSuccess(EquipmentResponse o) {
                    listener.onSuccess(o.getData());
                }

                @Override
                public void onFail(String errorMsg) {
                    listener.onFail(errorMsg);
                }
            });
        }

        @Override
        public void startEquipment(Context context, String pileId, IHttpRequest.OnResponseListener listener) {
            getRequest(context).post(START_CHARGE_URL, new String[]{"equipmentId"}, new String[]{pileId}, listener);
        }

        @Override
        public void stopEquipment(Context context, IHttpRequest.OnResponseListener listener) {
            getRequest(context).post(STOP_CHARGE_URL, new String[]{}, new String[]{}, listener);
        }
    }

    private static class LatLngNearbyResponse extends BaseResponse {

        List<Station> getStationInfo() {
            return stationInfo;
        }

        public void setStationInfo(List<Station> stationInfo) {
            this.stationInfo = stationInfo;
        }

        private List<Station> stationInfo;
    }

    private static class StationFeeResponse extends BaseResponse {
        private Station data;

        public Station getData() {
            return data;
        }

        public void setData(Station data) {
            this.data = data;
        }
    }

    private static class EquipmentResponse extends BaseResponse {
        private Equipment data;

        public Equipment getData() {
            return data;
        }

        public void setData(Equipment data) {
            this.data = data;
        }
    }

}
