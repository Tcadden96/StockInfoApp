package edu.temple.stockapp;

import android.content.res.Resources;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
/**
 * Created by tcadd on 12/5/2017.
 */

public class StockSearch extends AsyncTask<Void, Void, String> {

    Resources res;
    private Exception exception;
    private String symbol;
    private String filename = "Portfolio";
    Context fileContext;
    public StockSearch(String symbol, Context fileContext){
        this.symbol=symbol;
        this.fileContext = fileContext;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected String doInBackground(Void... urls) {

        try {
            URL url = new URL("http://dev.markitondemand.com/MODApis/Api/v2/Quote/json/?symbol=<" + symbol + ">");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                return stringBuilder.toString();
            }
            finally{
                urlConnection.disconnect();
            }
        }
        catch(Exception e) {
            return null;
        }
    }

    protected void onPostExecute(String response) {
        if(response == null) {
            response = "THERE WAS AN ERROR";
        }
        try {
            JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
            String requestID = object.getString("Name");
            Double lastprice = object.getDouble("LastPrice");
            FileOutputStream outputStream;
            String finalString =requestID + "," + lastprice.toString() +":";
            try {
                outputStream = fileContext.getApplicationContext().openFileOutput(filename, Context.MODE_PRIVATE);
                outputStream.write(finalString.getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {

        }



    }
}
