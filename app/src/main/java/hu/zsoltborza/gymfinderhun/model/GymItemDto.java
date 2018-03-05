package hu.zsoltborza.gymfinderhun.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Zsolt Borza on 2018.02.22..
 *
 *
 * Pojo class for online and offline searchable lists.
 */

public class GymItemDto implements Parcelable {

    private Long id;
    private  String title;
    private String address;
    private double rating;
    private String info;
    private boolean isOpen;
    private double latitude;
    private double longitude;
    private String photoReference;
    private String distance;
    private boolean isFromOnline;


    public GymItemDto(){

    }

    public GymItemDto(Long id, String title, String address, double rating, String info, boolean isOpen, double latitude, double longitude, String photoReference, String distance,boolean isFromOnline) {
        this.id = id;
        this.title = title;
        this.address = address;
        this.rating = rating;
        this.info = info;
        this.isOpen = isOpen;
        this.latitude = latitude;
        this.longitude = longitude;
        this.photoReference = photoReference;
        this.distance = distance;
        this.isFromOnline = isFromOnline;
    }

    protected GymItemDto(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        title = in.readString();
        address = in.readString();
        rating = in.readDouble();
        info = in.readString();
        isOpen = in.readByte() != 0;
        latitude = in.readDouble();
        longitude = in.readDouble();
        photoReference = in.readString();
        distance = in.readString();
        isFromOnline = in.readByte() != 0;
    }

    public static final Creator<GymItemDto> CREATOR = new Creator<GymItemDto>() {
        @Override
        public GymItemDto createFromParcel(Parcel in) {
            return new GymItemDto(in);
        }

        @Override
        public GymItemDto[] newArray(int size) {
            return new GymItemDto[size];
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getPhotoReference() {
        return photoReference;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public boolean isFromOnline() {
        return isFromOnline;
    }

    public void setFromOnline(boolean fromOnline) {
        isFromOnline = fromOnline;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(id);
        }
        parcel.writeString(title);
        parcel.writeString(address);
        parcel.writeDouble(rating);
        parcel.writeString(info);
        parcel.writeByte((byte) (isOpen ? 1 : 0));
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeString(photoReference);
        parcel.writeString(distance);
        parcel.writeByte((byte) (isFromOnline ? 1 : 0));
    }
}
