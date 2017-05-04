package com.hengchongkeji.constantcharge.data.source;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.hengchongkeji.constantcharge.ChargeApplication;
import com.hengchongkeji.constantcharge.R;
import com.hengchongkeji.constantcharge.data.domain.ChargeDetailData;
import com.hengchongkeji.constantcharge.data.domain.CurrentVoltage;
import com.hengchongkeji.constantcharge.data.domain.MapMarkerInfo;
import com.hengchongkeji.constantcharge.data.domain.Temperature;

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
        public ChargeDetailData getChargeDetailData() {
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

            return chargeDetailData;
        }

        @Override
        public String getChargeBalance() {
            Random random = new Random();
            return String.valueOf(random.nextInt(1000));
        }

        @Override
        public List<MapMarkerInfo> getLatLngNearby(LatLng curLatLng) {
            CharSequence[] textArray = ChargeApplication.getApplicationComponent().context().getResources().getTextArray(R.array.LatLngNearby);
            List<MapMarkerInfo> makerInfos = new ArrayList<>();
            for (int i = 0; i < textArray.length; i++) {
                LatLng latLng = new LatLng(Double.valueOf(textArray[i].toString()), Double.valueOf(textArray[i + 1].toString()));
                MapMarkerInfo makerInfo = new MapMarkerInfo();
                makerInfo.latLng = latLng;
                makerInfo.address = textArray[i + 2].toString();
                makerInfo.totalPile = textArray[i + 3].toString();
                makerInfo.freePile = textArray[i + 4].toString();
                makerInfo.predictFreePileTime = textArray[i + 5].toString();
                makerInfo.distance = String.valueOf((int) DistanceUtil.getDistance(curLatLng, latLng));
                makerInfo.predictReachTime = (int) (Double.valueOf(makerInfo.distance) / 1000) + "分钟";
                i += 5;
                makerInfos.add(makerInfo);
            }
            Collections.sort(makerInfos);
            return makerInfos;
        }

        @Override
        public int[] getFoundAdImgUrl() {
            int[] ints = new int[]{R.drawable.found_ad_item_0, R.drawable.found_ad_item_1, R.drawable.found_ad_item_2};
//            CharSequence[] textArray = ChargeApplication.getApplicationComponent().context().getResources().getTextArray(R.array.AdImgUrl);
//            String[] strings = new String[textArray.length];
//            for (int i = 0; i < textArray.length; i++) {
//                strings[i] = textArray[i].toString();
//            }
            return ints;
        }

        @Override
        public int[] getIntroductionImgUrl() {
            int[] ints = new int[]{R.drawable.introduction_img_0, R.drawable.introduction_img_1, R.drawable.introduction_img_2};
            return ints;
        }
    }

    private class DataRemoteSource implements IDataSource {

        @Override
        public ChargeDetailData getChargeDetailData() {
            return new ChargeDetailData();
        }

        @Override
        public String getChargeBalance() {
            return null;
        }

        @Override
        public List<MapMarkerInfo> getLatLngNearby(LatLng curLatLng) {
            return null;
        }

        @Override
        public int[] getFoundAdImgUrl() {
            return new int[0];
        }

        @Override
        public int[] getIntroductionImgUrl() {
            return new int[0];
        }
    }
}
