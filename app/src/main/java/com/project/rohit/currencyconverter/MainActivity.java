package com.project.rohit.currencyconverter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    //TODO: Create Constants
    //Base URL of the currency converter
    final String BASE_URL = "http://apilayer.net/api/live?access_key=";

    //API key from the website
    final String API_KEY = "e11c1e5fb349103cde16081ff9fe17fd";

    //TODO: Member Variables
    EditText mEditText;
    ImageView mLogo;
    TextView mResult;
    Spinner mSpinnerToConvertTo;
    Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditText = findViewById(R.id.editText2);
        mLogo = findViewById(R.id.logoImage);
        mResult = findViewById(R.id.result);
        mButton = findViewById(R.id.button_show);

        mSpinnerToConvertTo = findViewById(R.id.spinnerConvertTo);
        // Create an ArrayAdapter using the string array and a default spinner layout
        final ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.currency_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mSpinnerToConvertTo.setAdapter(adapter2);


        mSpinnerToConvertTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("CurrencyConverter", "" + parent.getItemAtPosition(position));
                Log.d("CurrencyConverter", "Position(2) is: " + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                Log.d("CurrencyConverter", "Nothing is selected");

            }

        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String finalUrl = BASE_URL + API_KEY + "&currencies=" + mSpinnerToConvertTo.getSelectedItem() + "&source=USD";
                Log.d("Currency", "final url is" + finalUrl);
                letsDoSomeNetworking(finalUrl);
                }

                private void letsDoSomeNetworking(String url) {

                AsyncHttpClient client = new AsyncHttpClient();
                client.get(url, new JsonHttpResponseHandler(){

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        // called when response HTTP status is "200 OK"
                        Log.d("CurrencyConverter", "JSON: " + response.toString());
                        try {
                            String price = response.getString("quotes");
                            mResult.setText(price);

                        } catch(JSONException e ) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        Log.d("CurrencyConverter", "Request fail! Status code: " + statusCode);
                        Log.d("CurrencyConverter", "Fail response: " + response);
                        Log.e("ERROR", e.toString());
                        Toast.makeText(MainActivity.this, "Request Failed", Toast.LENGTH_SHORT).show();
                    }

                });
                }
        });



    }


}


