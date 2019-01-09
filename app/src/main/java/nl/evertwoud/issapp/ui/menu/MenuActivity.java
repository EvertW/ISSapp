package nl.evertwoud.issapp.ui.menu;

import android.widget.FrameLayout;
import android.widget.Toast;

import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import nl.evertwoud.issapp.R;
import nl.evertwoud.issapp.data.models.Route;
import nl.evertwoud.issapp.ui.list.ListFragment_;
import nl.evertwoud.issapp.ui.map.MapFragment;
import nl.evertwoud.issapp.ui.map.MapFragment_;

@EActivity(R.layout.activity_main)
public class MenuActivity extends AppCompatActivity {

    @ViewById(R.id.menu_navigation)
    SpaceNavigationView mSpaceNavigationView;

    @ViewById(R.id.menu_fragment_container)
    FrameLayout mFragmentContainer;

    MapFragment mMapFragment;
    MenuViewModel viewModel;

    int currentFragmentIndexSelected = 0;

    //Handles the navigation
    private void handleNavigation(int pItemIndex) {
        currentFragmentIndexSelected = pItemIndex;
        //Navigate to item check
        switch (pItemIndex) {
            case 0:
                goToFragment(mMapFragment);
                Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.map_title));
                break;
            case 1:
                Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.map_routes));
                goToFragment(ListFragment_.builder().build());
                break;
        }
    }

    //Base go to fragment method
    void goToFragment(Fragment pFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.menu_fragment_container, pFragment, "");
        transaction.commit();
    }


    @AfterViews
    void setup() {
        //Set toolbar title
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.map_title));
        //Build view model
        viewModel = new MenuViewModel(this);

        //Set bottom menu
        mSpaceNavigationView.showIconOnly();
        mSpaceNavigationView.addSpaceItem(new SpaceItem(getString(R.string.menu_map), R.drawable.ic_map));
        mSpaceNavigationView.addSpaceItem(new SpaceItem(getString(R.string.menu_list), R.drawable.ic_list));
        mSpaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                //Check if the MapFragment is selected
                if (currentFragmentIndexSelected != 0) {
                    Toast.makeText(MenuActivity.this, getString(R.string.record_button_error), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mMapFragment != null) {
                    //Start/stop recording
                    if (!mMapFragment.isRecording()) {
                       startRecording();
                    } else {
                        stopRecording();
                    }
                }
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                //Stop map fragment click on tab switch
                if (mMapFragment != null) {
                    if (mMapFragment.isRecording()) {
                        stopRecording();
                    }
                }
                handleNavigation(itemIndex);
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                //Stop map fragment click on tab switch
                if (mMapFragment != null) {
                    if (mMapFragment.isRecording()) {
                        stopRecording();
                    }
                }
                handleNavigation(itemIndex);
            }
        });
        //Start the initial MapFragment
        mMapFragment = MapFragment_.builder().build();
        goToFragment(mMapFragment);
    }

    //Starts the recording
    void startRecording(){
        mSpaceNavigationView.changeCenterButtonIcon(R.drawable.ic_stop);
        mMapFragment.setRecording(true);
    }

    //Stops the recording
    void stopRecording(){
        mSpaceNavigationView.changeCenterButtonIcon(R.drawable.ic_record);
        mMapFragment.setRecording(false);
        Route route = mMapFragment.getRoute();
        //If there are no points don't create it
        if (!route.getPoints().isEmpty()) {
            viewModel.insert(mMapFragment.getRoute());
        }
    }

    //Returns the view model
    public MenuViewModel getViewModel() {
        return viewModel;
    }
}
