package hu.zsoltborza.gymfinderhun.database;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {MarkerEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MarkerEntityDAO getMarkerEntityDAO();
}
