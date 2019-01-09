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
import nl.evertwoud.issapp.Constants;
import nl.evertwoud.issapp.R;
import nl.evertwoud.issapp.data.models.Location;
import nl.evertwoud.issapp.data.models.Route;

@EActivity(R.layout.activity_route)
public class RouteActivity extends AppCompatActivity {

    //View & extra
    @ViewById(R.id.route_mapview)
    MapView mMapView;

    @Extra
    Route mRoute;

    @AfterViews
    void init() {
        //Set back button
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //Get the start time
        long startTime = mRoute.getPoints().get(0).getTimestamp();

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(startTime*1000);

        //Set page title to formatted date time
        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy - HH:mm");
        Objects.requireNonNull(getSupportActionBar()).setTitle(df.format(cal.getTime()));

        mMapView.getMapAsync(mapboxMap -> {
            //Set map style
            mapboxMap.setStyle(getString(R.string.mapbox_style));

            List<LatLng> latLngRoute = new ArrayList<>();
            //Add all positions to the list
            for (Location pos : mRoute.getPoints()) {
                Double latitude = Double.valueOf(pos.getPosition().getLatitiude());
                Double longitude = Double.valueOf(pos.getPosition().getLongitude());
                latLngRoute.add(new LatLng(latitude, longitude));
            }

            CameraPosition position = new CameraPosition.Builder()
                    .target(latLngRoute.get(0)) // Sets the new camera position
                    .zoom(Constants.MAP_ZOOM) // Sets the zoom
                    .build(); // Creates a CameraPosition from the builder
            //Move line to final location
            mapboxMap.moveCamera(CameraUpdateFactory
                    .newCameraPosition(position));
            //Draw Poly Lines for all locations
            PolylineOptions polylineOptions = new PolylineOptions()
                    .addAll(latLngRoute)
                    .color(ContextCompat.getColor(Objects.requireNonNull(this), R.color.blue))
                    .width(Constants.MAP_LINE_WIDTH);
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

    //Navigate back on button press
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
