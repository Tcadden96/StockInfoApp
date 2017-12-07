package edu.temple.stockapp;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.content.Intent;
import org.json.JSONObject;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;


public class MainActivity extends AppCompatActivity implements PortfolioFragment.Communicator {
    InfoFragment sf;
    FragmentManager manager;
    PortfolioFragment portfolio;
    EditText stockSymbol;
    SQLiteDatabase db;
    StockDBHelper mDbHelper;
    PortfolioFragment.Communicator comm;
    int orientation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = getFragmentManager();
        orientation = getResources().getConfiguration().orientation;
        if (orientation == 2) {
            sf = (InfoFragment) manager.findFragmentById((R.id.stockInfo));
        }



        portfolio = (PortfolioFragment) manager.findFragmentById(R.id.PortfolioFragment);
        portfolio.setCommuicator(this);
        stockSymbol = (EditText) findViewById(R.id.StockSymbol);

        mDbHelper = new StockDBHelper(this);
        portfolio.populateListView(mDbHelper);

        Intent i= new Intent(this, UpdateData.class);

        i.putExtra("intent","name");
        this.startService(i);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String stocksymbol = stockSymbol.getText().toString();
                Toast toast = Toast.makeText(MainActivity.this, stocksymbol, Toast.LENGTH_SHORT);
                toast.show();
                if (!mDbHelper.getSymbols().contains(stocksymbol.toUpperCase())) {

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
                } else {
                    toast = Toast.makeText(MainActivity.this, stocksymbol + " is already in your portfolio", Toast.LENGTH_LONG);
                    toast.show();
                }
            }


        });
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
        db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(StockDBContract.StockEntry.COLUMN_NAME_SYMBOL, symbol);
        values.put(StockDBContract.StockEntry.COLUMN_NAME_COMPANY, company);
        values.put(StockDBContract.StockEntry.COLUMN_NAME_PRICE, price);

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                StockDBContract.StockEntry.TABLE_NAME,
                null,
                values);

        if (newRowId > 0) {
            Log.d("Stock data saved ", newRowId + " - " + company);
            portfolio.populateListView(mDbHelper);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void respond(int index) {
        String name;
        String price;
        String symbol;
        name = mDbHelper.getNames()[index];
        symbol = mDbHelper.getSymbols().get(index);
        price = mDbHelper.getPrice()[index];
        if (orientation==2) {
            sf = (InfoFragment) manager.findFragmentById(R.id.stockInfo);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.remove(portfolio);
            transaction.addToBackStack(null);
            transaction.commit();
            sf.setName(name,symbol);
            sf.setPrice(price);
            sf.setWebView(symbol);
        } else {
            Intent i = new Intent(MainActivity.this, InfoActivity.class);
            Bundle b = new Bundle();
            b.putString("name", name);
            b.putString("price", price);
            b.putString("symbol", symbol);
            i.putExtra("personBdl", b);

            startActivity(i);
        }
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        //Toast.makeText(this, "Stopping service", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, UpdateData.class);
        stopService(intent);
    }
}
