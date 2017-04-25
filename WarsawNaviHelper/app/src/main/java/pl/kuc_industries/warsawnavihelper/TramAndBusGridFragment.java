package pl.kuc_industries.warsawnavihelper;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import java.util.ArrayList;
import java.util.List;

import pl.kuc_industries.warsawnavihelper.R;

/**
 * Created by mateusz on 4/25/17.
 */

public class TramAndBusGridFragment extends AppCompatDialogFragment {

    private static final int GRID_SPAN_COUNT_PORTRAIT = 6;
    private static final int GRID_SPAN_COUNT_LANDSCAPE = 12;

    private GridLayoutManager gridLayoutManager;
    private Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        boolean isAttachedToRoot = false;
        return inflater.inflate(R.layout.tram_and_bus_grid_fragment, container, isAttachedToRoot);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        boolean isOrientationLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        gridLayoutManager = new GridLayoutManager(context, isOrientationLandscape ? GRID_SPAN_COUNT_LANDSCAPE : GRID_SPAN_COUNT_PORTRAIT);

        GridLayoutManager.
        List<TramAndBusLine> tramAndBusLines = getTramAndBusLineList();
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.tram_and_bus_recycler_view);
        TramAndBusGridAdapter tramAndBusGridAdapter = new TramAndBusGridAdapter(context, tramAndBusLines);

        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(tramAndBusGridAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    private List<TramAndBusLine> getTramAndBusLineList() {
        List<TramAndBusLine> tramAndBusLineList = new ArrayList<>();
        String[] tramAndBusLineNames = getResources().getStringArray(R.array.tram_lines);

        for (String tramAndBusLineName : tramAndBusLineNames) {
            tramAndBusLineList.add(new TramAndBusLine(tramAndBusLineName));
        }
        return tramAndBusLineList;
    }
}
