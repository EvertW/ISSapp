package nl.evertwoud.issapp.ui.map;

import android.os.Bundle;

import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import androidx.fragment.app.Fragment;
import nl.evertwoud.issapp.R;

@EFragment(R.layout.fragment_map)
public class MapFragment extends Fragment {

    @ViewById(R.id.map_mapview)
    MapView mMapView;

    @AfterViews
    void initMap() {
        mMapView.getMapAsync(mapboxMap ->
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(41.885, -87.679))
                        .title("Chicago")
                        .snippet("Illinois")));
    }
}
