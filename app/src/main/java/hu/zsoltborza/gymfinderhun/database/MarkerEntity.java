package hu.zsoltborza.gymfinderhun.database;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "marker")
public class MarkerEntity {

    @NonNull
    @PrimaryKey
    private long markerId;
    private String title;
    private String information;
    private String address;
    private double latitude;
    private double longitude;
    private boolean isFavourite;

    public MarkerEntity() {

    }

    @NonNull
    public long getMarkerId() {
        return markerId;
    }

    public void setMarkerId(@NonNull long markerId) {
        this.markerId = markerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

}
