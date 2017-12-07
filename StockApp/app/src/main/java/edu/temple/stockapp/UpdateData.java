package edu.temple.stockapp;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class UpdateData extends IntentService {
    private String stocksymbol;
    private StockDBHelper helper;
    private SQLiteDatabase db;
    private int id;

    public UpdateData() {
        this(UpdateData.class.getName());
    }

    public UpdateData(String name) {
        super(name);


    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Boolean running = true;
        while (running) {
            showToast("Updated Info");
            UpdateDatabase();
            SystemClock.sleep(60000);

        }
    }

    protected void showToast(final String msg) {
        //gets the main thread
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                // run this code in the main thread
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void UpdateDatabase() {
        helper = new StockDBHelper(this);
        db = helper.getWritableDatabase();

        for (int i = 1; i < helper.getSymbols().size()+1; i++) {
            stocksymbol = helper.getSymbols().get(i-1);
            id=i;
            Thread t = new Thread() {
                @Override
                public void run() {
                    URL stockQuoteUrl;
                    try {

                        //stockQuoteUrl = new URL("http://finance.yahoo.com/webservice/v1/symbols/" + stockSymbol + "/quote?format=json");
                        stockQuoteUrl = new URL("http://dev.markitondemand.com/MODApis/Api/v2/Quote/json/?symbol=" + stocksymbol);

                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(
                                        stockQuoteUrl.openStream()));

                        String response = "", tmpResponse;

                        tmpResponse = reader.readLine();
                        while (tmpResponse != null) {
                            response = response + tmpResponse;
                            tmpResponse = reader.readLine();
                        }

                        JSONObject stockObject = new JSONObject(response);
                        Message msg = Message.obtain();
                        msg.obj = stockObject;
                        stockResponseHandler.sendMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            };
            t.start();
        }
    }
        Handler stockResponseHandler = new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {

                JSONObject responseObject = (JSONObject) msg.obj;

                try {
                    Stock stock = new Stock(responseObject);

                    saveData(stock.getSymbol(), stock.getName(), stock.getPrice());
                } catch (Exception e) {
                    e.printStackTrace();
                }


                return false;
            }
        });
    private void saveData(String symbol, String company, double price) {

        // Gets the data repository in write mode


        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(StockDBContract.StockEntry._ID,id);
        values.put(StockDBContract.StockEntry.COLUMN_NAME_SYMBOL, symbol);
        values.put(StockDBContract.StockEntry.COLUMN_NAME_COMPANY, company);
        values.put(StockDBContract.StockEntry.COLUMN_NAME_PRICE, price);

        db.replace("entry",null,values);

    }


    }





