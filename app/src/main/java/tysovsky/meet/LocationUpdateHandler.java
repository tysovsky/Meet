package tysovsky.meet;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tysovsky on 4/4/16.
 */
public class LocationUpdateHandler extends BroadcastReceiver {
    public static Activity currentActivity;
    public static final Object CURRENTACTIVITYLOCK = new Object();
    private LocationManager locationManager = null;
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
    private String location_provider = "";
    String url = "http://52.27.83.251/update";

    @Override
    public void onReceive(final Context context, Intent intent) {
        updateLocation(context, true);
    }


    public void updateLocation(final Context context, final boolean active){
        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        location_provider = locationManager.getBestProvider(criteria, false);
        if(!active){
            locationManager.removeUpdates(locationListener);
        }else{
            locationManager.requestLocationUpdates(location_provider, 5000, 10, locationListener);
        }
        final Location location = locationManager.getLastKnownLocation(location_provider);


        if(location != null){
            new HttpHandler(){

                @Override
                public HttpUriRequest getHttpRequestMethod() {
                    HttpPost httpPost = new HttpPost(url);

                    List<NameValuePair> nameValuePairs = new ArrayList<>();
                    nameValuePairs.add(new BasicNameValuePair("name", "John Doe"));
                    nameValuePairs.add(new BasicNameValuePair("username", "jdoe"));
                    nameValuePairs.add(new BasicNameValuePair("latitude", location.getLatitude()+""));
                    nameValuePairs.add(new BasicNameValuePair("longitude", location.getLongitude() + ""));
                    nameValuePairs.add(new BasicNameValuePair("active", active ? "true" : "false"));

                    try {
                        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    return httpPost;
                }

                @Override
                public void onResponse(String result) {
                    //Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
                    try {
                        ArrayList<PeopleNearMe> peopleNearMe = new ArrayList<PeopleNearMe>();
                        JSONArray jsonArray = new JSONArray(result);
                        for(int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            peopleNearMe.add(new PeopleNearMe(jsonObject.getString("name"), jsonObject.getString("username")));

                        }

                        if(currentActivity != null && currentActivity instanceof MainActivity){
                            ((MainActivity)currentActivity).updatePeopleNearMe(peopleNearMe);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }.execute();
        }
    }

}
