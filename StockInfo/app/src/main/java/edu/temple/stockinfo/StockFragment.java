package edu.temple.stockinfo;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

/**
 * Created by tcadd on 12/4/2017.
 */

public class StockFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Resources res = getResources();
        View view = inflater.inflate(R.layout.stock_fragment, container, false);

        return view;
    }

}
