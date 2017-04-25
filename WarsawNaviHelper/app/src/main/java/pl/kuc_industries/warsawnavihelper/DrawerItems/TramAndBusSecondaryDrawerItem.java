package pl.kuc_industries.warsawnavihelper.DrawerItems;

import android.content.Context;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import pl.kuc_industries.warsawnavihelper.R;
import com.mikepenz.materialdrawer.model.BaseDrawerItem;

import java.util.List;


public class TramAndBusSecondaryDrawerItem extends BaseDrawerItem<TramAndBusSecondaryDrawerItem, TramAndBusSecondaryDrawerItem.ViewHolder> {

    public int getType() {
        return R.id.material_drawer_spinner_item_secondary;
    }

    @Override
    @LayoutRes
    public int getLayoutRes() {
        return R.layout.material_drawer_spinner_item_secondary;
    }

    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    @Override
    public void bindView(ViewHolder viewHolder, List payloads) {
        super.bindView(viewHolder, payloads);

        //set the identifier from the drawerItem here. It can be used to run tests
        viewHolder.itemView.setId(hashCode());

        //call the onPostBindView method to trigger post bind view actions (like the listener to modify the item if required)
        onPostBindView(this, viewHolder.itemView);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        protected ImageView icon;
        private Spinner spinner;

        private ViewHolder(View view) {
            super(view);
            this.view = view;
            this.spinner = (Spinner) view.findViewById(R.id.spinner);
            this.icon = (ImageView) view.findViewById(R.id.material_drawer_icon);

            spinner.setAdapter(new TramAndBusSpinnerAdapter(view.getContext(),
                    R.layout.tram_and_bus_spinner_row, view.getResources().getStringArray(R.array.tram_lines)));
        }
    }
}
