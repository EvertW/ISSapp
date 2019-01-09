package nl.evertwoud.issapp.data.database;

import android.content.Context;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.lifecycle.LiveData;
import nl.evertwoud.issapp.data.models.Route;

public class RouteRepo {
    private Executor mExecutor = Executors.newSingleThreadExecutor();
    private LiveData<List<Route>> routes;
    private RouteDao routeRepo;

    public RouteRepo(Context context) {
        RouteDatabase mAppDatabase = RouteDatabase.getInstance(context);
        routeRepo = mAppDatabase.routeDao();
        routes = routeRepo.getRouteList();
    }

    public LiveData<List<Route>> getRoutes() {
        return routes;
    }

    public void insert(final Route bucketListItem) {
        mExecutor.execute(() -> routeRepo.addRoute(bucketListItem));
    }

    public void delete(final Route bucketListItem) {
        mExecutor.execute(() -> routeRepo.deleteRoute(bucketListItem));
    }
}