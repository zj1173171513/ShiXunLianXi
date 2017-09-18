package com.jiyun.asus.shixunlianxi.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS on 2017/9/7.
 */

public class Bean implements Parcelable {
    private List<PositionBean> position;

    public List<PositionBean> getPosition() {
        return position;
    }

    public void setPosition(List<PositionBean> position) {
        this.position = position;
    }

    public static class PositionBean implements Parcelable {
        /**
         * city : 北京平谷
         * latitude : 40.13
         * longitude : 117.1
         */

        private String city;
        private String latitude;
        private String longitude;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.city);
            dest.writeString(this.latitude);
            dest.writeString(this.longitude);
        }

        public PositionBean() {
        }

        protected PositionBean(Parcel in) {
            this.city = in.readString();
            this.latitude = in.readString();
            this.longitude = in.readString();
        }

        public static final Creator<PositionBean> CREATOR = new Creator<PositionBean>() {
            @Override
            public PositionBean createFromParcel(Parcel source) {
                return new PositionBean(source);
            }

            @Override
            public PositionBean[] newArray(int size) {
                return new PositionBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.position);
    }

    public Bean() {
    }

    protected Bean(Parcel in) {
        this.position = new ArrayList<PositionBean>();
        in.readList(this.position, PositionBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<Bean> CREATOR = new Parcelable.Creator<Bean>() {
        @Override
        public Bean createFromParcel(Parcel source) {
            return new Bean(source);
        }

        @Override
        public Bean[] newArray(int size) {
            return new Bean[size];
        }
    };
}
