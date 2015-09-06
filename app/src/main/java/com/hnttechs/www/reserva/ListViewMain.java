package com.hnttechs.www.reserva;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class ListViewMain extends ActionBarActivity {

    JSONObject jsonobject,restaurant;
    JSONArray jsonarray;
    ListView listview;
    ListViewAdapter adapter;
    ArrayList<HashMap<String, String>> arraylist;
    static String NAME = "name";
    static String ADDRESS = "address";
    static String PHONE = "phone";
    FileWriter jsonFileWriter;
    private static final String json_chinese = "/sdcard/Reserva/chinese.json";
    private static final String json_myanmar = "/sdcard/Reserva/myanmar.json";
    private static final String json_japan = "/sdcard/Reserva/japan.json";
    private static final String json_european = "/sdcard/Reserva/european.json";
    private static final String json_indian = "/sdcard/Reserva/indian.json";
    private static final String json_thai = "/sdcard/Reserva/thai.json";
    File file;
    private SwipeRefreshLayout swipeContainer;
    static String btn_id;
    File reservaDirectory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_main);


        reservaDirectory= new File("/sdcard/Reserva/");
        reservaDirectory.mkdirs();

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isInternetOn() == false) {
                    Toast.makeText(getBaseContext(), "No Internet Access", Toast.LENGTH_SHORT).show();
                    swipeContainer.setRefreshing(false);
                } else {
                    new DownloadJSON().execute();
                }
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        Intent intent = getIntent();
        btn_id = intent.getStringExtra("btn_id");
        if (btn_id.equals("Chinese Restaurant"))       { file = new File(json_chinese);}
        else if (btn_id.equals("Myanmar Restaurant"))  { file = new File(json_myanmar);}
        else if (btn_id.equals("Japan Restaurant"))    { file = new File(json_japan);}
        else if (btn_id.equals("European Restaurant")) { file = new File(json_european);}
        else if (btn_id.equals("Indian Restaurant"))   { file = new File(json_indian);}
        else if (btn_id.equals("Thailand Restaurant")) { file = new File(json_thai); }

        checkfile();
    }

    private void checkfile() {
        if (!file.exists() || file.length() == 0) {
            swipeContainer.post(new Runnable() {
                @Override
                public void run() {
                    swipeContainer.setRefreshing(true);
                    if (isInternetOn() == false) {
                        Toast.makeText(getBaseContext(), "No Internet Access", Toast.LENGTH_SHORT).show();
                        swipeContainer.setRefreshing(false);
                    } else {
                        new DownloadJSON().execute();
                    }
                }
            });
        } else {
                new ReadJSON().execute();
        }
    }

    private class DownloadJSON extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(swipeContainer.isRefreshing() == false ){
                swipeContainer.setRefreshing(true);
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            if (btn_id.equals("Chinese Restaurant"))       { add_downjson_to_map("https://sleepy-badlands-6645.herokuapp.com/my_chinese.txt",json_chinese); }
            else if (btn_id.equals("Myanmar Restaurant"))  { add_downjson_to_map("https://sleepy-badlands-6645.herokuapp.com/my_myanmar.txt",json_myanmar);}
            else if (btn_id.equals("Japan Restaurant"))    { add_downjson_to_map("https://sleepy-badlands-6645.herokuapp.com/my_japan.txt",json_japan);}
            else if (btn_id.equals("European Restaurant")) { add_downjson_to_map("https://sleepy-badlands-6645.herokuapp.com/my_european.txt",json_european); }
            else if (btn_id.equals("Indian Restaurant"))   { add_downjson_to_map("https://sleepy-badlands-6645.herokuapp.com/my_indian.txt",json_indian); }
            else if (btn_id.equals("Thailand Restaurant")) { add_downjson_to_map("https://sleepy-badlands-6645.herokuapp.com/my_thai.txt",json_thai); }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            listview = (ListView) findViewById(R.id.listview);
            adapter = new ListViewAdapter(ListViewMain.this, arraylist);
            listview.setAdapter(adapter);
            if (swipeContainer.isRefreshing() == true) {
                swipeContainer.setRefreshing(false);
            }
        }
    }

    public ArrayList add_downjson_to_map(String url, String jsonName) {

        arraylist = new ArrayList<HashMap<String, String>>();
        jsonobject = JSONfunctions.getJSONfromURL(url);

        try {
            jsonFileWriter = new FileWriter(jsonName);
            jsonarray = jsonobject.getJSONArray("Restaurant");

            for (int i = 0; i < jsonarray.length(); i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                restaurant = jsonarray.getJSONObject(i);

                map.put("name", restaurant.getString("name"));
                map.put("address", restaurant.getString("address"));
                map.put("phone", restaurant.getString("phone"));

                arraylist.add(map);
            }

            jsonFileWriter.write(jsonobject.toString());
            jsonFileWriter.flush();
            jsonFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return arraylist;
    }


    private class ReadJSON extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            if (btn_id.equals("Chinese Restaurant"))       { add_readjson_to_map(json_chinese); }
            else if (btn_id.equals("Myanmar Restaurant"))  { add_readjson_to_map(json_myanmar);}
            else if (btn_id.equals("Japan Restaurant"))    { add_readjson_to_map(json_japan);}
            else if (btn_id.equals("European Restaurant")) { add_readjson_to_map(json_european); }
            else if (btn_id.equals("Indian Restaurant"))   { add_readjson_to_map(json_indian); }
            else if (btn_id.equals("Thailand Restaurant")) { add_readjson_to_map(json_thai); }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            listview = (ListView) findViewById(R.id.listview);
            adapter = new ListViewAdapter(ListViewMain.this, arraylist);
            listview.setAdapter(adapter);
        }
    }

    public ArrayList add_readjson_to_map(String jsonname) {
        arraylist = new ArrayList<HashMap<String, String>>();

        try {
            InputStream jsonStream = new FileInputStream(jsonname);
            jsonobject = new JSONObject(InputStreamToString(jsonStream));
            jsonarray = jsonobject.getJSONArray("Restaurant");

            for (int i = 0; i < jsonarray.length(); i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                restaurant = jsonarray.getJSONObject(i);

                map.put("name", restaurant.getString("name"));
                map.put("address", restaurant.getString("address"));
                map.put("phone", restaurant.getString("phone"));

                arraylist.add(map);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return arraylist;
    }

    public final boolean isInternetOn() {

        ConnectivityManager connec =
                (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            return true;
        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
    }


    public static String InputStreamToString(InputStream is) {

        BufferedReader r = new BufferedReader(new InputStreamReader(is));
        StringBuilder total = new StringBuilder();
        String line;
        try {
            while ((line = r.readLine()) != null) {
                total.append(line);
            }
        } catch (IOException e) { e.printStackTrace(); }
        return total.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

}
