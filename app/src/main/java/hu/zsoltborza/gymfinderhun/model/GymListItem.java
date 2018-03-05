package hu.zsoltborza.gymfinderhun.model;

/**
 * Created by Zsolt Borza on 2018.01.30..
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GymListItem implements Parcelable{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("address1")
    @Expose
    private String address1;
    @SerializedName("address2")
    @Expose
    private String address2;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;

    protected GymListItem(Parcel in) {
        id = in.readString();
        title = in.readString();
        address1 = in.readString();
        address2 = in.readString();
        address = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        info = in.readString();
    }

    public GymListItem(){

    }



    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    @SerializedName("info")
    @Expose
    private String info;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(address1);
        parcel.writeString(address2);
        parcel.writeString(address);
        parcel.writeString(latitude);
        parcel.writeString(longitude);
        parcel.writeString(info);
    }

    public static final Creator<GymListItem> CREATOR = new Creator<GymListItem>() {
        @Override
        public GymListItem createFromParcel(Parcel in) {
            return new GymListItem(in);
        }

        @Override
        public GymListItem[] newArray(int size) {
            return new GymListItem[size];
        }
    };
}
