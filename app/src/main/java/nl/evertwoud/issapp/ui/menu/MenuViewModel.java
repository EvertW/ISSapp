package nl.evertwoud.issapp.ui.menu;

import android.content.Context;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import nl.evertwoud.issapp.data.database.RouteRepo;
import nl.evertwoud.issapp.data.models.Route;

public class MenuViewModel extends ViewModel {

    private RouteRepo repo;
    private LiveData<List<Route>> items;

    public MenuViewModel(Context context) {
        repo = new RouteRepo(context);
        items = repo.getRoutes();
    }

    public LiveData<List<Route>> getRoutes() {
        return items;
    }

    public void insert(Route route) {
        repo.insert(route);
    }

    public void delete(Route route) {
        repo.delete(route);
    }
}