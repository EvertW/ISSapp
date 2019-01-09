package nl.evertwoud.issapp.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import nl.evertwoud.issapp.data.models.Route;

@Database(entities = {Route.class}, version = 1, exportSchema = false)
@TypeConverters({RouteConverter.class})
public abstract class RouteDatabase extends RoomDatabase {
    private final static String NAME_DATABASE = "route_db";
    private static RouteDatabase sInstance;

    public static RouteDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room.databaseBuilder(context, RouteDatabase.class, NAME_DATABASE).allowMainThreadQueries().build();
        }
        return sInstance;
    }

    public abstract RouteDao routeDao();
}