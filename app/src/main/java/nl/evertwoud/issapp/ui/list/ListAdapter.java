package nl.evertwoud.issapp.ui.list;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import nl.evertwoud.issapp.data.models.Route;
import nl.evertwoud.issapp.ui.base.BaseRecyclerAdapter;
import nl.evertwoud.issapp.ui.base.ViewWrapper;

public class ListAdapter extends BaseRecyclerAdapter<Route, RouteListRow> {

    private Context context;

    ListAdapter(Context context) {
        this.context = context;
    }

    @Override
    protected RouteListRow onCreateItemView(ViewGroup parent, int viewType) {
        RouteListRow row = RouteListRow_.build(context);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(lp);
        return row;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewWrapper<RouteListRow> holder, int position) {
        holder.getView().bind(getData().get(position));
    }
}