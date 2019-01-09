package nl.evertwoud.issapp.ui.list;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import nl.evertwoud.issapp.R;
import nl.evertwoud.issapp.data.models.Route;
import nl.evertwoud.issapp.ui.route.RouteActivity;
import nl.evertwoud.issapp.ui.route.RouteActivity_;


@EViewGroup(R.layout.view_route_list)
public class RouteListRow extends FrameLayout {

    @ViewById(R.id.date)
    TextView date;

    @ViewById(R.id.times)
    TextView times;

    Route mRoute;

    public RouteListRow(@NonNull Context context) {
        super(context);
    }

    public RouteListRow(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RouteListRow(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    void bind(Route item) {
        mRoute = item;
        if (item.getPoints() != null && !item.getPoints().isEmpty()) {
            long startTime = item.getPoints().get(0).getTimestamp();

            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(startTime*1000);

            SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
            SimpleDateFormat tf = new SimpleDateFormat("hh:mm");
            date.setText(df.format(cal.getTime()));
            times.setText(tf.format(cal.getTime()));
        }
    }

    @Click(R.id.route)
    void onItemClick(){
        if (mRoute!=null){
            RouteActivity_.intent(getContext()).mRoute(mRoute).start();
        }
    }


}