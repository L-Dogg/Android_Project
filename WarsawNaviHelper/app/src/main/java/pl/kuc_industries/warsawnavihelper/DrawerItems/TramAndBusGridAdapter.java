package pl.kuc_industries.warsawnavihelper.DrawerItems;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import pl.kuc_industries.warsawnavihelper.Models.TramAndBusLine;
import pl.kuc_industries.warsawnavihelper.R;

/**
 * Created by mateusz on 4/25/17.
 */

public class TramAndBusGridAdapter extends RecyclerView.Adapter<TramAndBusGridAdapter.TramAndBusViewHolder> {

    private static final String TAG = "TramAndBusGridAdapter";

    private List<TramAndBusLine> tramAndBusLineList;
    private Context context;

    public TramAndBusGridAdapter(Context context, List<TramAndBusLine> tramAndBusLineList) {
        this.context = context;
        this.tramAndBusLineList = tramAndBusLineList;
    }

    @Override
    public TramAndBusViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        boolean isAttachedToRoot = false;

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.tram_and_bus_grid_item, parent, isAttachedToRoot);
        TramAndBusViewHolder viewHolder = new TramAndBusViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TramAndBusViewHolder holder, int position) {
        holder.bindTramAndBusLine(tramAndBusLineList.get(position));
    }

    @Override
    public int getItemCount() {
        return tramAndBusLineList.size();
    }

    public class TramAndBusViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private int visibleBgColor;
        private int invisibleBgColor;

        private TramAndBusLine tramAndBusLine;
        private TextView mTramAndBusLineNameTextView;

        public TramAndBusViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTramAndBusLineNameTextView = (TextView) itemView.findViewById(R.id.tram_and_bus_grid_item);

            visibleBgColor = ContextCompat.getColor(context, R.color.tram_bus_line_visible_bg_color);
            invisibleBgColor = ContextCompat.getColor(context, R.color.tram_bus_line_invisible_bg_color);
        }

        public void bindTramAndBusLine(TramAndBusLine tramAndBusLine) {
            this.tramAndBusLine = tramAndBusLine;
            boolean visibility = tramAndBusLine.isVisible();

            mTramAndBusLineNameTextView.setBackgroundColor(visibility ? visibleBgColor : invisibleBgColor);
            mTramAndBusLineNameTextView.setText(tramAndBusLine.getLineName());
        }

        @Override
        public void onClick(View v) {
            boolean newVisibility = !tramAndBusLine.isVisible();
            int visibleBgColor = ContextCompat.getColor(context, R.color.tram_bus_line_visible_bg_color);
            int invisibleBgColor = ContextCompat.getColor(context, R.color.tram_bus_line_invisible_bg_color);

            tramAndBusLine.setVisible(newVisibility);
            mTramAndBusLineNameTextView.setBackgroundColor(newVisibility ? visibleBgColor : invisibleBgColor);
        }

    }
}
