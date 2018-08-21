package hu.zsoltborza.gymfinderhun.network.cache;

import java.util.List;
import hu.zsoltborza.gymfinderhun.network.domain.Gym;
import io.reactivex.Observable;

/**
 * Created by Zsolt Borza on 2018.08.21..
 */
public interface CacheProviders
{
    Observable<List<Gym>> getCachedGymsByRadiusAndCoordinates(Observable<List<Gym>> observableGymList);
}