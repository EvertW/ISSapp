package nl.evertwoud.issapp.data.database;


import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import nl.evertwoud.issapp.data.models.Route;

@Dao
public interface RouteDao {

    @Query("SELECT * FROM routes")
    LiveData<List<Route>> getRouteList();

    @Insert
    void addRoute(Route route);

    @Delete
    void deleteRoute(Route route);

}