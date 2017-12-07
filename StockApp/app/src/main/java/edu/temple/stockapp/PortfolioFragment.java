package edu.temple.stockapp;

/**
 * Created by tcadd on 12/6/2017.
 */

import android.app.Fragment;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.database.sqlite.SQLiteDatabase;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleCursorAdapter;

public class PortfolioFragment  extends Fragment implements AdapterView.OnItemClickListener {

    GridView gridView;
    Communicator communicator;
    String[] Stocks;
    StockDBHelper mDbHelper;
    public PortfolioFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Stocks = new String[1];
        Stocks[0] = "Add a Stock to begin";
        //  populateListView(mDbHelper);
        View v = inflater.inflate(R.layout.portfolio_fragment, container, false);
        gridView = (GridView) v.findViewById(R.id.PortfolioGrid);
        CustomListAdapter myAdapter = new CustomListAdapter(getActivity(), Stocks);
        gridView.setOnItemClickListener(this);
        gridView.setAdapter(myAdapter);

        return v;
    }

            @Override
            public void onItemClick(AdapterView<?> GridView, View view, int i, long l) {
                communicator.respond(i);
            }


    public interface  Communicator{
        public void respond(int index);
    }
    public void setCommuicator(Communicator comm){
        this.communicator = comm;
    }


    public void populateListView(StockDBHelper dbHelper) {
//dbHelper.delete(dbHelper.getReadableDatabase());
     //   dbHelper.delete(dbHelper.getWritableDatabase());
       // SQLiteDatabase db = dbHelper.getReadableDatabase();
      //  Cursor cursor = db.query(StockDBContract.StockEntry.TABLE_NAME, new String[]{"_id", StockDBContract.StockEntry.COLUMN_NAME_COMPANY}, null, null, null, null, null);

        //SimpleCursorAdapter adapter = new SimpleCursorAdapter(getContext(), R.layout.portfolio_fragment, cursor, new String[]{StockDBContract.StockEntry.COLUMN_NAME_COMPANY}, new int[]{R.id.List_Item_Name}, 0);
       String[] names=dbHelper.getNames();
        if(names!=null){
            Stocks=names;
        }
        CustomListAdapter itemAdapter = new CustomListAdapter(getContext(),Stocks);
        ((GridView)gridView.findViewById(R.id.PortfolioGrid)).setAdapter(itemAdapter);

    }

    public void UpdateDate(){

    }

}
