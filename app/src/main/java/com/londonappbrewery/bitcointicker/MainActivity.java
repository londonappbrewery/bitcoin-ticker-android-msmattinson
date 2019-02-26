package com.londonappbrewery.bitcointicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import cz.msebera.android.httpclient.Header;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;



public class MainActivity extends AppCompatActivity {

    // Constants:
    // TODO: Create the base URL
    private final String BASE_URL = "https://apiv2.bitcoinaverage.com/indices/global/ticker/BTC";

    // Member Variables:
    TextView mPriceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPriceTextView = (TextView) findViewById(R.id.priceLabel);
        Spinner spinner = (Spinner) findViewById(R.id.currency_spinner);

        // Create an ArrayAdapter using the String array and a spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, R.layout.spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // TODO: Set an OnItemSelected listener on the spinner
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String currency = (String) parent.getItemAtPosition(position);
                Log.d("Bitcoin", "" + currency);
                String finalUrl = BASE_URL + currency;
                Log.d("Bitcoin", finalUrl);
                letsDoSomeNetworking(finalUrl);
            }

            @Override
             public void onNothingSelected(AdapterView<?> parent) {
                Log.d("Bitcoin", "Nothing selected");
             }
        });
    }

    // TODO: complete the letsDoSomeNetworking() method
    private void letsDoSomeNetworking(String url) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // called when response HTTP status is "200 OK"
                Log.d("Bitcoin", "JSON: " + response.toString());
                Double price = parseJson(response);
                mPriceTextView.setText(Double.toString(price));
//                WeatherDataModel weatherData = WeatherDataModel.fromJson(response);
//                updateUI(weatherData);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d("Bitcoin", "Request fail! Status code: " + statusCode);
                Log.d("Bitcoin", "Fail response: " + response);
                Log.e("ERROR", e.toString());
                Toast.makeText(MainActivity.this, "Request failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private double parseJson(JSONObject response) {
        try {
            double price = response.getDouble("last");
            Log.d("Bitcoin", "Bitcoin price is: " + price);
            return price;
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }

                /*public static WeatherDataModel fromJSON(JSONObject jsonObject) {
                    try {
                        WeatherDataModel weatherData = new WeatherDataModel();
                        weatherData.mCity = jsonObject.getString("name");
                        weatherData.mCondition = jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
                        weatherData.mIconName = updateWeatherIcon(weatherData.mCondition);

                        double tempResult = (jsonObject.getJSONObject("main").getDouble("temp") - 273.15) * 9/5 + 32;
                        int roundedTemp = (int) Math.rint(tempResult);
                        weatherData.mTemperature = Integer.toString(roundedTemp);

                        return weatherData;
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return null;
                    }
                }*/
    }

}
