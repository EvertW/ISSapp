package nl.evertwoud.issapp.ui.route;

import android.content.Intent;
import android.view.MenuItem;

import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import nl.evertwoud.issapp.R;
import nl.evertwoud.issapp.data.models.Location;
import nl.evertwoud.issapp.data.models.Route;

@EActivity(R.layout.activity_route)
public class RouteActivity extends AppCompatActivity {

    @ViewById(R.id.route_mapview)
    MapView mMapView;

    @Extra
    Route mRoute;

    @AfterViews
    void init() {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        long startTime = mRoute.getPoints().get(0).getTimestamp();

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(startTime*1000);

        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy - HH:mm");
        Objects.requireNonNull(getSupportActionBar()).setTitle(df.format(cal.getTime()));
        mMapView.getMapAsync(mapboxMap -> {
            mapboxMap.setStyle(getString(R.string.mapbox_style));

            List<LatLng> latLngRoute = new ArrayList<>();

            for (Location pos : mRoute.getPoints()) {
                Double latitude = Double.valueOf(pos.getPosition().getLatitiude());
                Double longitude = Double.valueOf(pos.getPosition().getLongitude());
                latLngRoute.add(new LatLng(latitude, longitude));
            }

            CameraPosition position = new CameraPosition.Builder()
                    .target(latLngRoute.get(0)) // Sets the new camera position
                    .zoom(4) // Sets the zoom
                    .build(); // Creates a CameraPosition from the builder

            mapboxMap.moveCamera(CameraUpdateFactory
                    .newCameraPosition(position));

            PolylineOptions polylineOptions = new PolylineOptions()
                    .addAll(latLngRoute)
                    .color(ContextCompat.getColor(Objects.requireNonNull(this), R.color.blue))
                    .width(4f);
            mapboxMap.addPolyline(polylineOptions);
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
}
