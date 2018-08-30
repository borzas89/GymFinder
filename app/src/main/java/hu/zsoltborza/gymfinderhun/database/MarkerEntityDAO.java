package hu.zsoltborza.gymfinderhun.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface MarkerEntityDAO {
    @Insert
    public void insert(MarkerEntity... markerEntity);

    @Update
    public void update(MarkerEntity... markerEntity);

    @Delete
    public void delete(MarkerEntity markerEntity);

    @Query("SELECT * FROM marker")
    public List<MarkerEntity> getMarkerEntitys();

    @Query("SELECT * FROM marker WHERE markerId = :markerId")
    public MarkerEntity getMarkerEntityById(long markerId);
}
