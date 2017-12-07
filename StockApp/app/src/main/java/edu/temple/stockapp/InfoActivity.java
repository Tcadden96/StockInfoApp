package edu.temple.stockapp;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class InfoActivity extends AppCompatActivity {
    InfoFragment sf;
    FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        manager = getFragmentManager();
        sf = (InfoFragment) manager.findFragmentById((R.id.stockInfo));
        Intent i = getIntent();
        Bundle b = i.getBundleExtra("personBdl");
        String name = b.getString("name");
        String price = b.getString("price");
        String symbol = b.getString("symbol");

        sf.setName(name,symbol);
        sf.setPrice(price);
        sf.setWebView(symbol);
    }
}