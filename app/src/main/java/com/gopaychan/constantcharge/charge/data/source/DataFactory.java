package com.gopaychan.constantcharge.charge.data.source;

import com.gopaychan.constantcharge.charge.data.ChargeDetailData;
import com.gopaychan.constantcharge.charge.data.CurrentVoltage;
import com.gopaychan.constantcharge.charge.data.Temperature;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by gopayChan on 2017/4/20.
 */

public class DataFactory {

    private DataFactory() {
    };

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
                currentVoltage.currentVoltage = String.valueOf(600 + (random.nextInt(100) * ((random.nextInt(2)%2)==1?1:-1)));
                temperature.temperature = String.valueOf(50 + random.nextInt(50));
                currentVoltages.add(currentVoltage);
                temperatures.add(temperature);
            }
            chargeDetailData.mCurrentVoltages = currentVoltages;
            chargeDetailData.mTemperatures = temperatures;

            return chargeDetailData;
        }
    }


    private class DataRemoteSource implements IDataSource {

        @Override
        public ChargeDetailData getChargeDetailData() {
            return new ChargeDetailData();
        }
    }
}
