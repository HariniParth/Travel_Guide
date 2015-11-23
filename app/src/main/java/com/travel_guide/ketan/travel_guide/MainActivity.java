package com.travel_guide.ketan.travel_guide;


import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.media.ImageWriter;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.SearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class MainActivity extends ActionBarActivity {

    TextView textview;
    private Toolbar mToolbar;
    //This is array adapter. Contents in this structure are displayed on the screen. As per the talk with dr puder
    //we need to change this to display images.
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<String> myDataset = new ArrayList();
    private ArrayList<String> myDescription = new ArrayList();
    private  String[] myImageset;
    private ArrayList<Bitmap> bitmap = new ArrayList<Bitmap>();
    private ArrayAdapter<String> mForcastAdapter;
    private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();
    // private  FetchWeatherTask fetchWeatherTask = new FetchWeatherTask();
    //for filtering reference
    String reference ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        textview = (TextView) findViewById(R.id.showQuery);
        //Initialising forecast adapter

        mRecyclerView = (RecyclerView) findViewById(R.id.location);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        FetchWeatherTask fetchWeatherTask = new FetchWeatherTask();
        try {
            myDataset = fetchWeatherTask.execute("san francisco").get();
            String data="";int index=0;String id="";
            myImageset =new String[myDataset.size()];
            for (int i=0;i<myDataset.size();i++)
            {
                data = myDataset.get(i);
                index=data.indexOf("$$$");
                myDescription.add(data.substring(0,index));
                index=index+3;
                reference = data.substring(index);
                id = reference;
                myImageset[i] = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+id+"&key=AIzaSyDJwZEahDJ2W0zi89IFfkXMyKCS7h7KsUo";
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //  System.out.println("this is the status of fetchweather Task " + fetchWeatherTask.getStatus());
        LoadImage load = new LoadImage();
        try {
            bitmap = load.execute(myImageset).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        mAdapter = new ItemAdapter(myDescription,bitmap);
        mRecyclerView.setAdapter(mAdapter);
        //Calling the FetchWeatherTask,for this is the name of method we using to get places
    }
    //This method take the value from places API using Async task.
    public class FetchWeatherTask extends AsyncTask<String, Void, ArrayList<String>> {
        /**
         * Take the String representing the complete forecast in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         * <p/>
         * Fortunately parsing is easy:  constructor takes the JSON string and converts it
         * into an Object hierarchy for us.
         */
        //Basically this method is parsing the json data
        private ArrayList<String> getWeatherDataFromJson(String forecastJsonStr, int numDays)
                throws JSONException {
            // These are the names of the JSON objects that need to be extracted.
            // final String OWM_LIST = "name";
            final String OWM_NAME = "name";
            final String OWM_TEMPERATURE = "temp";
            // final String OWM_MAX = "max";
            // final String OWM_MIN = "min";
            final String OWM_ADDRESS = "formatted_address";
            final String PHOTO_REFERENCE = "photo_reference";
            String description="";
            String imageUrl = "";
            String reference = "";
            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            JSONArray weatherArray = forecastJson.getJSONArray("results");
            JSONArray photoArray = null;
            // OWM returns daily forecasts based upon the local time of the city that is being
            // asked for, which means that we need to know the GMT offset to translate this data
            // properly.
            // Since this data is also sent in-order and the first day is always the
            // current day, we're going to take advantage of that to get a nice
            // normalized UTC date for all of our weather.
            ArrayList<String> resultStrs = new ArrayList<String>();
            //  Log.v(LOG_TAG, "Json List: " + photoArray);
            for (int i = 0; i < weatherArray.length(); i++) {
                // For now, using the format "Day, description, hi/low"
                String name;
                //String description;
                String highAndLow;
                // Get the JSON object representing the day
                JSONObject dayForecast = weatherArray.getJSONObject(i);
                try{
                    photoArray = dayForecast.getJSONArray("photos");
                    //System.out.println("This is not a null array");
                    //Log.v(LOG_TAG, "photo Array " + photoArray);
                }catch (JSONException e) {
                    System.out.println("This is a null array");
                    // Log.v(LOG_TAG, "photo Array " + photoArray);
                    // throw new RuntimeException(e);
                }
                imageUrl = dayForecast.getString("icon");
                description = dayForecast.getString(OWM_ADDRESS);
                name = dayForecast.getString(OWM_NAME);
                if(photoArray.length()>0){
                    for(int j=0;j<photoArray.length();j++) {
                        JSONObject photos = photoArray.getJSONObject(j);
                        reference = photos.getString(PHOTO_REFERENCE);
                    }
                }else{
                    reference =  dayForecast.getString("icon");
                }
                // Log.v(LOG_TAG, "Inside the function " +description+" - "+name+""+imageUrl);
                // The date/time is returned as a long.  We need to convert that
                // into something human-readable, since most people won't read "1400356800" as
                // "this saturday".
                // long dateTime;
                // Cheating to convert this to UTC time, which is what we want anyhow
                // description is in a child array called "weather", which is 1 element long.
                //JSONObject weatherObject = forecastJson.getJSONArray(OWM_DESCRIPTION).getJSONObject(0);
                //description = weatherObject.getString(OWM_DESCRIPTION);
                // Log.v(LOG_TAG, "Inside the function " +description);
                // Log.v(LOG_TAG, "Inside the function " +description);
                // Temperatures are in a child object called "temp".  Try not to name variables
                // "temp" when working with temperature.  It confuses everybody.
                //JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
                // double high = temperatureObject.getDouble(OWM_MAX);
                // double low = temperatureObject.getDouble(OWM_MIN);
                //  highAndLow = formatHighLows(high, low);
                resultStrs.add( name+"$$$"+reference) ;
            }
            for (String s : resultStrs) {
                //  Log.v(LOG_TAG, "Forecast entry: " + reference);
            }
            return resultStrs;
        }
        //This is the method called in async task
        @Override
        protected ArrayList<String> doInBackground(String... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            String type = "natural_feature&establishment";
            String key = "AIzaSyDJwZEahDJ2W0zi89IFfkXMyKCS7h7KsUo";
            String parameter = params[0]+"attractions";
            int numDays = 7;
            try {
                // Construct the URL for the Google Places query
                // Possible parameters are available at
                // https://developers.google.com/places/web-service/search?hl=en
                final String FORECAST_BASE_URL = "https://maps.googleapis.com/maps/api/place/textsearch/json?";
                final String QUERY_PARAM = "query";
                final String TYPE_PARAM = "types";
                final String KEY = "key";
                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM,parameter)
                        .appendQueryParameter(TYPE_PARAM, type)
                        .appendQueryParameter(KEY, key)
                        .build();
                //I have hard coded the url for now. We will use the above parameter later
                // URL url = new URL("https://maps.googleapis.com/maps/api/place/textsearch/json?query=san+francisco+tourist+spots&types=establishment&natural_feature&key=AIzaSyCiZDY-xzYlpJwFb1O_z78gvuLErcXCIWA");
                // URL url = new URL("https://maps.googleapis.com/maps/api/place/textsearch/json?query=san+francisco+tourist+spots&types=establishment&natural_feature&key=AIzaSyDv3yQLMXvDWXneaG506lIP9kEOPip7PUU");
                URL url = new URL(builtUri.toString());
                Log.v(LOG_TAG, "Built URI " + builtUri.toString());
                // Create the request to Google Palces, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    forecastJsonStr = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    forecastJsonStr = null;
                }
                forecastJsonStr = buffer.toString();
                Log.v(LOG_TAG, "Forecast JSON string:" + forecastJsonStr);
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                forecastJsonStr = null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            try {
                return getWeatherDataFromJson(forecastJsonStr,numDays);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);
        }
        // protected void onPostExecute(ArrayList<String> result) {
        //if(result!=null){
        //    myDataset.clear();
        //    myImageset = new String[result.length];
        //    int i=0;String reference = "";
        //    int j=0;
        //     for(String dayForcastString : result) {
        //         myDataset.add(dayForcastString);
        //         // Log.e("myDataSet", myDataset.get(i));
        //          //System.out.println("day forcast is" + dayForcastString.indexOf("$$$"));
        //          i=dayForcastString.indexOf("$$$");
        //          i=i+3;
        //          reference = dayForcastString.substring(i);
        //System.out.println("this is reference "+reference);
        //int size = reference.length;
        //           String id = reference;
        // System.out.println("This is id "+id);
        //         myImageset[j] = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=+"+id+"&key=AIzaSyDv3yQLMXvDWXneaG506lIP9kEOPip7PUU";
        // Log.e("myImageSet",myImageset[i]);
        //         j++;
        // mAdapter.notifyItemInserted(0);
        //}
        // System.out.println("Post execute finished");
//            }
        // }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //Creating search option at action bar and take the query entered
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        if (null != searchView) {
            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
        }
        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                // this is your adapter that will be filtered
                return true;
            }
            public boolean onQueryTextSubmit(String query) {
                //Here u can get the value "query" which is entered in the search box.
                // textview.setText(query);
                FetchWeatherTask fetchWeatherTask = new FetchWeatherTask();
                String value = "";
                String[] quote = query.split(" ");
                for(int i=0;i<quote.length;i++) {
                    value += quote[i] + "+";
                }
                //After converting the search bar query in appropriate format we call the asyncTask again which request the data
                // fetchWeatherTask.execute(value);
                //from onCreate
                myDataset.clear();myDescription.clear();
                try {
                    myDataset = fetchWeatherTask.execute(value).get();
                    String data="";int index=0;String id="";
                    myImageset =new String[myDataset.size()];
                    for (int i=0;i<myDataset.size();i++)
                    {
                        data = myDataset.get(i);
                        index=data.indexOf("$$$");
                        myDescription.add(data.substring(0,index));
                        index=index+3;
                        reference = data.substring(index);
                        id = reference;
                        myImageset[i] = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+id+"&key=AIzaSyDJwZEahDJ2W0zi89IFfkXMyKCS7h7KsUo";
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                //  System.out.println("this is the status of fetchweather Task " + fetchWeatherTask.getStatus());
                LoadImage load = new LoadImage();
                bitmap.clear();
                try {
                    bitmap = load.execute(myImageset).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                mAdapter = new ItemAdapter(myDescription,bitmap);
                mRecyclerView.setAdapter(mAdapter);
//                if(fetchWeatherTask.getStatus() == AsyncTask.Status.FINISHED) {
//                    LoadImage load = new LoadImage();
//                    try {
//                        bitmap = load.execute(myImageset).get();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    } catch (ExecutionException e) {
//                        e.printStackTrace();
//                    }
//                    System.out.println("This mydataset"+myDataset.size());
//                    mAdapter = new ItemAdapter(myDataset,bitmap);
//                }
                opensearch();
                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);
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
        }else if(id == R.id.action_search){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //This method was created for testing purpose only
    private void opensearch(){
        Toast.makeText(this,"Search Button presses",Toast.LENGTH_SHORT).show();
        //Intent search = new Intent(this,SearchPlaces.class);
        //startActivity(search);
    }
    private class LoadImage extends AsyncTask<String[], String, ArrayList<Bitmap>> {
        protected ArrayList<Bitmap> doInBackground(String[]... args) {
            for (String[] url:args) {
                for(String tmp: url){
                    System.out.println(tmp);
                    try {
                        //String U = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=+"+tmp+"&key=AIzaSyDv3yQLMXvDWXneaG506lIP9kEOPip7PUU";
                        bitmap.add(BitmapFactory.decodeStream((InputStream) new URL(tmp).getContent()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return bitmap;
        }
        @Override
        protected void onPostExecute(ArrayList<Bitmap> bitmaps) {
            System.out.println("size of bitmap is" + bitmap.size());
            super.onPostExecute(bitmaps);
            // mAdapter.notifyItemInserted(0);
        }
    }
}