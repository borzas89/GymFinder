package hu.zsoltborza.gymfinderhun.fragments.base;

import hu.zsoltborza.gymfinderhun.database.MarkerEntity;
import hu.zsoltborza.gymfinderhun.model.GymListItem;
import hu.zsoltborza.gymfinderhun.model.GymItemDto;


/**
 * Created by Zsolt Borza on 2018.02.16..
 */

public interface ListDetailInterface {
    public void showListDetail(GymItemDto item);
    public void showListDetailItem(GymListItem item);
    public void showMarkerDetailItem(MarkerEntity markerEntity);
}
