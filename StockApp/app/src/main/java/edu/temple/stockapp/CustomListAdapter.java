package edu.temple.stockapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by tcadd on 12/6/2017.
 */

public class CustomListAdapter extends BaseAdapter {
    private String[] Stocks;
    private LayoutInflater thisInflater;
    private Context context;
    private String[] isEmpty = new String[1];

    CustomListAdapter(Context con, String[] Stocks) {
        this.thisInflater = LayoutInflater.from(con);
        this.context = con;
        this.Stocks=Stocks;

    }

    @Override
    public int getCount(){
        return Stocks.length;
    }

    @Override
    public Object getItem(int position){
        return position;

    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if (convertView == null) {
            convertView = thisInflater.inflate( R.layout.list_item, parent, false );
            TextView textHeading = (TextView) convertView.findViewById(R.id.List_Item_Name);
            textHeading.setText(Stocks[position] );
        }
        return convertView;
    }

}