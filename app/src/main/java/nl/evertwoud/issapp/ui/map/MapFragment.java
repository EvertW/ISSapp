package nl.evertwoud.issapp.ui.map;

import android.os.Handler;

import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import nl.evertwoud.issapp.Constants;
import nl.evertwoud.issapp.R;
import nl.evertwoud.issapp.data.models.Location;
import nl.evertwoud.issapp.data.models.Route;
import nl.evertwoud.issapp.data.network.APIService;
import nl.evertwoud.issapp.ui.base.UiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@EFragment(R.layout.fragment_map)
public class MapFragment extends Fragment {

    //Views
    @ViewById(R.id.map_mapview)
    MapView mMapView;

    //Initialize variables
    private List<Location> route = new ArrayList<>();
    private List<LatLng> latLngRoute = new ArrayList<>();
    private boolean recording = false;
    private Marker prevMarker;
    private LatLng prevCoord;

    @AfterViews
    void initMap() {
        //Set the map style
        mMapView.getMapAsync(mapboxMap -> mapboxMap.setStyle(getString(R.string.mapbox_style)));
        //Create a runnable that requests the location
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                requestLocation();
                handler.postDelayed(this, Constants.RELOAD_TIME);
            }
        };
        handler.postDelayed(runnable, 0);

    }


    //Request the location of the Station
    private void requestLocation() {
        //Creates an API Service
        APIService service = APIService.retrofit.create(APIService.class);
        //Make the API call
        Call<Location> call = service.getLocation();
        call.enqueue(new Callback<Location>() {
            @Override
            public void onResponse(Call<Location> call, Response<Location> response) {
                if (mMapView != null) {
                    //Request the map view
                    mMapView.getMapAsync(mapboxMap -> {
                        //Checks if the result is not null
                        if (response.body() != null) {
                            //If recording is true then add the location to the route
                            if (recording) {
                                route.add(response.body());
                            }

                            //Get the LatLng for the location
                            Double latitude = Double.valueOf(response.body().getPosition().getLatitiude());
                            Double longitude = Double.valueOf(response.body().getPosition().getLongitude());
                            LatLng loc = new LatLng(latitude, longitude);

                            //If the route is empty move the camera to the first position
                            if (latLngRoute.isEmpty()) {
                                CameraPosition position = new CameraPosition.Builder()
                                        .target(loc) // Sets the new camera position
                                        .zoom(Constants.MAP_ZOOM) // Sets the zoom
                                        .build(); // Creates a CameraPosition from the builder

                                mapboxMap.moveCamera(CameraUpdateFactory
                                        .newCameraPosition(position));
                            }
                            //Add the item to the lat long routes
                            latLngRoute.add(loc);
                            //If the previous marker is not null remove it
                            if (prevMarker != null) {
                                mapboxMap.removeMarker(prevMarker);
                            }

                            //If the previous coord is not null draw a poly between the prev & current point
                            if (prevCoord != null) {
                                PolylineOptions polylineOptions = new PolylineOptions()
                                        .add(prevCoord, loc)
                                        .color(recording ? ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.red) : ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.blue))
                                        .width(Constants.MAP_LINE_WIDTH);
                                mapboxMap.addPolyline(polylineOptions);
                            }

                            //Draw a marker on the current pos
                            Icon icon = IconFactory.getInstance(getContext()).fromBitmap(UiUtils.getBitmapFromVectorDrawable(getContext(), R.drawable.ic_marker));
                            prevMarker = mapboxMap.addMarker(new MarkerOptions().position(loc).icon(icon));
                            //Update the prev coord
                            prevCoord = loc;
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Location> call, Throwable t) {

            }
        });
    }



    //Animates the camera to the last known location of the station
    @Click(R.id.map_center)
    void center() {
        if (mMapView != null && prevCoord != null) {
            mMapView.getMapAsync(mapboxMap -> {
                CameraPosition position = new CameraPosition.Builder()
                        .target(prevCoord)
                        .zoom(Constants.MAP_ZOOM)
                        .build();
                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
            });
        }
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
        //Animates the camera to the last known location of the station
        if (mMapView != null && prevCoord != null) {
            mMapView.getMapAsync(mapboxMap -> {
                CameraPosition position = new CameraPosition.Builder()
                        .target(prevCoord) // Sets the new camera position
                        .zoom(Constants.MAP_ZOOM) // Sets the zoom
                        .build(); // Creates a CameraPosition from the builder
                mapboxMap.moveCamera(CameraUpdateFactory
                        .newCameraPosition(position));
            });
        }
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
    public void onDestroyView() {
        super.onDestroyView();
        mMapView.onDestroy();
    }


    //Returns the recorded route
    public Route getRoute() {
        return new Route(route);
    }

    //Returns if the route is currently being recorded
    public boolean isRecording() {
        return recording;
    }

    //Stops the recording (And clears it before start recording)
    public void setRecording(boolean recording) {
        if (recording) {
            route = new ArrayList<>();
        }
        this.recording = recording;
    }
}
