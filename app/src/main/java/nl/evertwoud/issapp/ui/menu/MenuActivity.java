package nl.evertwoud.issapp.ui.menu;

import android.widget.FrameLayout;

import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import nl.evertwoud.issapp.R;
import nl.evertwoud.issapp.ui.list.ListFragment_;
import nl.evertwoud.issapp.ui.map.MapFragment_;

@EActivity(R.layout.activity_main)
public class MenuActivity extends AppCompatActivity {

    @ViewById(R.id.menu_navigation)
    SpaceNavigationView mSpaceNavigationView;

    @ViewById(R.id.menu_fragment_container)
    FrameLayout mFragmentContainer;

    private void captureLocation() {
        //Capture location
    }

    private void handleNavigation(int pItemIndex) {
        switch (pItemIndex) {
            case 0:
                goToFragment(MapFragment_.builder().build());
                break;
            case 1:
                goToFragment(ListFragment_.builder().build());
                break;
        }
    }

    void goToFragment(Fragment pFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.menu_fragment_container, pFragment, "");
        transaction.commit();
    }


    @AfterViews
    void setUpMenu() {
        mSpaceNavigationView.showIconOnly();
        mSpaceNavigationView.addSpaceItem(new SpaceItem(getString(R.string.menu_map), R.drawable.ic_map));
        mSpaceNavigationView.addSpaceItem(new SpaceItem(getString(R.string.menu_list), R.drawable.ic_list));
        mSpaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                captureLocation();
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                handleNavigation(itemIndex);
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                handleNavigation(itemIndex);
            }
        });

        goToFragment(MapFragment_.builder().build());
    }
}
