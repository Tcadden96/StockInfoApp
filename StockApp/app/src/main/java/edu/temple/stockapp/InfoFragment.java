package edu.temple.stockapp;

import android.app.Fragment;

/**
 * Created by tcadd on 12/6/2017.
 */
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
import android.widget.TextView;


public class InfoFragment  extends Fragment {

    private String name="";
    private String price="";
    private WebView wv;
    private View v;
    private String URL="https://finance.google.com/finance/getchart?p=5d&q=";
    public InfoFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

         v = inflater.inflate(R.layout.stock_info_fragment, container, false);

        return v;
    }
    public void setName(String name,String symbol){
        this.name=name + "(" + symbol + ")";
        TextView tv1 = (TextView)v.findViewById(R.id.StockName);
        tv1.setText(this.name);
    }
    public void setPrice(String price){
        this.price=price;
        TextView tv1 = (TextView)v.findViewById(R.id.CurrentPrice);
        tv1.setText("Last Traded Price is: " + this.price);
    }
    public void setWebView(String symbol){
        wv = (WebView) v.findViewById(R.id.webView);
        wv.setWebViewClient(new WebViewClient());
        wv.getSettings().setJavaScriptEnabled(true);

            WebSettings ws = wv.getSettings();
            ws.setJavaScriptEnabled(true);
            wv.setWebViewClient(new WebViewClient());
            String url= URL + symbol.toUpperCase();
            wv.loadUrl(url);
    }
}