package org.app.compsat.compsatapplication;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;


public class CalendarActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener{
    private Activity context = this;
    private ArrayList<HashMap<String, String>> eventsList;
    private JSONParser jParser = new JSONParser();
    private SwipeRefreshLayout swipeRefreshLayout;

    // url to get all calandar_events
    private static final String TAG_EVENT = "event_name";
    private static final String TAG_DATE = "event_date";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_LOCATION = "location";
    private static final String TAG_HOST = "host_id";
    private ProgressDialog pDialog;

    // events JSONArray
    private JSONArray events;
    private JSONArray monthsJson;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout2);
        swipeRefreshLayout.setOnRefreshListener(this);

        new LoadAllEvents().execute();
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
    public void onRefresh() {
        new LoadAllEvents().execute();
    }

    class LoadAllEvents extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CalendarActivity.this);
            pDialog.setIcon(R.drawable.gear);
            pDialog.setMessage("Loading events. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         */
        protected String doInBackground(String... args) {

            InputStream is = null;
            try {
                is = getBaseContext().getAssets().open("db/db.properties");
            } catch (IOException e) {
                e.printStackTrace();
            }
            Scanner scanner = new Scanner(is);

            String url = "http://192.168.1.4/PEDC/index.php/Event_controller/events/format/json/";
            String monthUrl = "http://192.168.1.4/PEDC/index.php/Event_controller/subscriptionsMonths/format/json";


            // Hashmap for ListView
            eventsList = new ArrayList<HashMap<String, String>>();


            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONArray json = null;

            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();


            if(activeNetworkInfo != null && activeNetworkInfo.isConnected()){
                // getting JSON string from URL
                json = jParser.makeHttpRequest(url, "POST", params);
                monthsJson = jParser.makeHttpRequest(monthUrl, "POST", params);
                /*try {
                    monthsJson = new JSONArray("[{'month':'February'},{'month':'March'},{'month':'April'},{'month':'May'},{'month':'June'},{'month':'July'},{'month':'August'},{'month':'September'},{'month':'October'},{'month':'November'},{'month':'December'},{'month':'January'}]");
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
                //writeToFile("calendar.txt", json.toString());
                //writeToFile("months.txt", monthsJson.toString());
            }
            else{
                try {
                    json = new JSONArray(readFromFile("calendar.txt"));
                    monthsJson = new JSONArray(readFromFile("months.txt"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            try {
                // Getting Array of Contacts
                events = json;

                Log.d("JSON", events+"");


                // looping through All Events
                for (int i = 0; i < events.length(); i++) {
                    JSONObject c = events.getJSONObject(i);

                    // Storing each json item in variable
                    String event = c.getString(TAG_EVENT);
                    String date = c.getString(TAG_DATE);
                    String description = c.getString(TAG_DESCRIPTION);
                    String location = c.getString(TAG_LOCATION);
                    String host = c.getString(TAG_HOST);

                    // creating new HashMap
                    HashMap<String, String> map = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    map.put(TAG_EVENT, event);
                    map.put(TAG_DATE, date);
                    map.put(TAG_HOST, host);
                    map.put(TAG_LOCATION, location);
                    map.put(TAG_DESCRIPTION, description);

                    // adding HashList to ArrayList
                    eventsList.add(map);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
            // updating UI from Background Thread

            /**
             * After completing background task Dismiss the progress dialog
             * **/

        protected void onPostExecute(String file_url) {
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    mAdapter = new MyRecyclerAdapter(context ,monthsJson, events);
                    mRecyclerView.setAdapter(mAdapter);

                    pDialog.dismiss();
                    swipeRefreshLayout.setRefreshing(false);
                }
            });

        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    private void writeToFile(String filename, String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(filename, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private String readFromFile(String filename) {

        String ret = "";

        try {
            InputStream inputStream = openFileInput(filename);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("Calendar", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("Calendar", "Can not read file: " + e.toString());
        }

        return ret;
    }

}
