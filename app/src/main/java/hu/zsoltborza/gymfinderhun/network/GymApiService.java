package hu.zsoltborza.gymfinderhun.network;

import java.util.List;

import hu.zsoltborza.gymfinderhun.network.domain.Gym;
import hu.zsoltborza.gymfinderhun.network.domain.MarkerSearch;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Custom service for GymFinder-API calls.
 * Domain objects for this service is : Gym and Address.
 * Created by Zsolt Borza on 2018.07.26..
 */

public interface GymApiService {

    // Sample urls for developing
    // Search by coordinates and radius
    // https://gymfinder-hun.herokuapp.com/api/gym/neargyms?radius=1000&latitude=47.547141&longitude=19.076171
    // Fetching gyms by id
    // sample data: "gym/neargyms?radius=1000&latitude=47.547141&longitude=19.076171"
    @GET("gym/neargyms")
    Observable<List<Gym>> getGymsByRadiusAndCoordinate(@Query("radius") long radiusInMeter,
                                                 @Query("latitude") double latitude,
                                                 @Query("longitude") double longitude);

    @GET("gym/neargyms")
    Call<List<Gym>> getGymsCallsByRadiusAndCoordinate(@Query("radius") long radiusInMeter,
                                                       @Query("latitude") double latitude,
                                                       @Query("longitude") double longitude);




    @GET("search/json?type=gym&key=AIzaSyCyk1W3jHXffYh7sXdSaoFJRmcTRcyk9sg")
    Call<MarkerSearch> getGymsByLocation(@Query("location") String location, @Query("radius") int radius);

    @GET("gym/neargyms")
    Observable<List<Gym>> getReactiveGymsByRadiusAndCoordinate(@Query("radius") long radiusInMeter,
                                                               @Query("latitude") double latitude,
                                                               @Query("longitude") double longitude);


}
