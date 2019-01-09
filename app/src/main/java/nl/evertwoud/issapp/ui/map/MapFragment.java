package nl.evertwoud.issapp.ui.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
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
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import nl.evertwoud.issapp.R;
import nl.evertwoud.issapp.data.models.Location;
import nl.evertwoud.issapp.data.models.Route;
import nl.evertwoud.issapp.data.network.APIService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@EFragment(R.layout.fragment_map)
public class MapFragment extends Fragment {

    private final int RELOAD_TIME = 1500;
    @ViewById(R.id.map_mapview)
    MapView mMapView;
    private List<Location> route = new ArrayList<>();
    private List<LatLng> latLngRoute = new ArrayList<>();
    private boolean recording = false;
    private Marker prevMarker;
    private LatLng prevCoord;

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    @AfterViews
    void initMap() {
        mMapView.getMapAsync(mapboxMap -> mapboxMap.setStyle(getString(R.string.mapbox_style)));
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                requestLocation();
                handler.postDelayed(this, RELOAD_TIME);
            }
        };
        handler.postDelayed(runnable, 0);

    }

    void requestLocation() {
        APIService service = APIService.retrofit.create(APIService.class);
        Call<Location> call = service.getLocation();

        call.enqueue(new Callback<Location>() {
            @Override
            public void onResponse(Call<Location> call, Response<Location> response) {
                if (mMapView != null) {
                    mMapView.getMapAsync(mapboxMap -> {
                        if (response.body() != null) {
                            if (recording) {
                                route.add(response.body());
                            }

                            Double latitude = Double.valueOf(response.body().getPosition().getLatitiude());
                            Double longitude = Double.valueOf(response.body().getPosition().getLongitude());

                            LatLng loc = new LatLng(latitude, longitude);

                            if (latLngRoute.isEmpty()) {
                                CameraPosition position = new CameraPosition.Builder()
                                        .target(loc) // Sets the new camera position
                                        .zoom(3) // Sets the zoom
                                        .build(); // Creates a CameraPosition from the builder

                                mapboxMap.moveCamera(CameraUpdateFactory
                                        .newCameraPosition(position));
                            }
                            latLngRoute.add(loc);


                            if (prevMarker != null) {
                                mapboxMap.removeMarker(prevMarker);
                            }

                            if (prevCoord != null) {
                                PolylineOptions polylineOptions = new PolylineOptions()
                                        .add(prevCoord, loc)
                                        .color(recording ? ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.red) : ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.blue))
                                        .width(4f);
                                mapboxMap.addPolyline(polylineOptions);
                            }


                            Icon icon = IconFactory.getInstance(getContext()).fromBitmap(getBitmapFromVectorDrawable(getContext(), R.drawable.ic_marker));
                            prevMarker = mapboxMap.addMarker(new MarkerOptions().position(loc).icon(icon));
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
    public void onDestroyView() {
        super.onDestroyView();
        mMapView.onDestroy();
    }

    public Route getRoute() {
        return new Route(route);
    }

    public boolean isRecording() {
        return recording;
    }

    public void setRecording(boolean recording) {
        if (recording) {
            route = new ArrayList<>();
        }
        this.recording = recording;
    }
}
