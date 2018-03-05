package hu.zsoltborza.gymfinderhun.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Zsolt Borza on 2018.01.30..
 */

public class GymData {

    @SerializedName("gym_list")
    @Expose
    private List<GymListItem> gymList = null;

    public List<GymListItem> getGymList() {
        return gymList;
    }

    public void setGymList(List<GymListItem> gymList) {
        this.gymList = gymList;
    }
}
