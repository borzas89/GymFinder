package hu.zsoltborza.gymfinderhun.network.cache;

import java.util.List;
import java.util.concurrent.TimeUnit;

import hu.zsoltborza.gymfinderhun.network.domain.Gym;
import io.reactivex.Observable;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.LifeCache;

/**
 * Created by Zsolt Borza on 2018.08.21..
 */
public interface CacheProviders
{
    @LifeCache(duration = 5, timeUnit = TimeUnit.MINUTES)
    Observable<List<Gym>> getCachedGymsByRadiusAndCoordinates(Observable<List<Gym>> observableGymList,
                                                              DynamicKey query, EvictDynamicKey update);
}