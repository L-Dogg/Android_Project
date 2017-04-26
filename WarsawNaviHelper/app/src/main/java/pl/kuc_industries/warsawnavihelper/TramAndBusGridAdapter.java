package pl.kuc_industries.warsawnavihelper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pl.kuc_industries.warsawnavihelper.R;

/**
 * Created by mateusz on 4/25/17.
 */

public class TramAndBusGridAdapter extends RecyclerView.Adapter<TramAndBusGridAdapter.TramAndBusViewHolder> {

    private List<TramAndBusLine> tramAndBusLineList;
    private Context context;

    public TramAndBusGridAdapter(Context context) {
        this.context = context;
        this.tramAndBusLineList = getTramAndBusLineList();
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
        holder.mTramAndBusNameTextView.setText(tramAndBusLineList.get(position).getLineName());
    }

    @Override
    public int getItemCount() {
        return tramAndBusLineList.size();
    }

    public class TramAndBusViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mTramAndBusNameTextView;

        public TramAndBusViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTramAndBusNameTextView = (TextView) itemView.findViewById(R.id.tram_and_bus_grid_item);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), "Position = " + getLayoutPosition(), Toast.LENGTH_SHORT);
        }
    }

    private List<TramAndBusLine> getTramAndBusLineList() {
        List<TramAndBusLine> tramAndBusLineList = new ArrayList<>();
        String[] tramAndBusLineNames = context.getResources().getStringArray(R.array.tram_lines);

        for (String tramAndBusLineName : tramAndBusLineNames) {
            tramAndBusLineList.add(new TramAndBusLine(tramAndBusLineName));
        }
        return tramAndBusLineList;
    }
}
