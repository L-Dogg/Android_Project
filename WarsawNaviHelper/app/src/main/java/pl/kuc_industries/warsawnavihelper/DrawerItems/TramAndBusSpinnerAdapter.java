package pl.kuc_industries.warsawnavihelper.DrawerItems;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.List;

import pl.kuc_industries.warsawnavihelper.R;

/**
 * Created by mateusz on 4/25/17.
 */

public class TramAndBusSpinnerAdapter extends ArrayAdapter<String> {

    // TODO: Model for Trams and Buses + nice way to wrap lines

    private String[] sTramLines;
    private Context context;

    public TramAndBusSpinnerAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull String[] objects) {
        super(context, resource, objects);
        this.context = context;
        this.sTramLines = objects;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView label = (TextView) convertView;

        if (convertView == null) {
            convertView = new TextView(context);
            label = (TextView) convertView;
        }

        label.setText(sTramLines[position]);
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.tram_and_bus_spinner_row, parent, false);
        GridView label = (GridView) row.findViewById(R.id.gridView1);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_list_item_1,
                sTramLines);
        label.setAdapter(adapter);

        return row;
    }
}
