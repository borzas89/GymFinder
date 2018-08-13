package hu.zsoltborza.gymfinderhun.converters;

import hu.zsoltborza.gymfinderhun.model.GymListItem;
import hu.zsoltborza.gymfinderhun.network.domain.Address;
import hu.zsoltborza.gymfinderhun.network.domain.Gym;

/**
 * Created by Borzas on 2018. 08. 13..
 */

public class GymToGymItemConverter {

    public static final String TAG = "GymToGymItemConverter";

    public static Gym convertGymToGymListItem(GymListItem gymListItem){
        Gym convertedGym = new Gym();
        convertedGym.setId(Integer.valueOf(gymListItem.getId()));

        Address address = new Address();
        address.setAddress1(gymListItem.getAddress1());
        address.setAddress2(gymListItem.getAddress2());
        address.setLatitude(Double.valueOf(gymListItem.getLatitude()));
        address.setLongitude(Double.valueOf(gymListItem.getLongitude()));
        convertedGym.setAddress(address);
        convertedGym.setInformation(gymListItem.getInfo());
        convertedGym.setTitle(gymListItem.getTitle());

        return convertedGym;
    }
}
