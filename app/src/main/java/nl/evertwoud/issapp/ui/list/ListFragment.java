package nl.evertwoud.issapp.ui.list;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import nl.evertwoud.issapp.R;
import nl.evertwoud.issapp.ui.menu.MenuActivity;

@EFragment(R.layout.fragment_list)
public class ListFragment extends Fragment {

    //Views
    @ViewById(R.id.list_recycler)
    RecyclerView recycler;

    //Variables
    private ListAdapter listAdapter;

    //Swipe listener
    private ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            ((MenuActivity) Objects.requireNonNull(getActivity())).getViewModel().delete(listAdapter.getItem(viewHolder.getAdapterPosition()));
        }
    };

    @AfterViews
    void init() {
        //initialize the adapter & assign it to the recycler
        listAdapter = new ListAdapter(getContext());
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(listAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recycler);
        //Request the list items as live data
        ((MenuActivity) Objects.requireNonNull(getActivity()))
                .getViewModel()
                .getRoutes()
                .observe(this, routes -> listAdapter.setItems(routes));
    }

}
