package com.example.nutritionapp;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView mtfat, mfats, msodium, mpotassium, mfiber, msugar, mnfacts, mfood;

    String MyFood;

    private RequestQueue rqueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        rqueue = Volley.newRequestQueue(this);
        mtfat = findViewById(R.id.mtfat);
        mfats = findViewById(R.id.mfats);
        msodium = findViewById(R.id.msodium);
        mpotassium = findViewById(R.id.mpotassium);
        mfiber = findViewById(R.id.mfiber);
        msugar = findViewById(R.id.msugar);
        mnfacts = findViewById(R.id.mnfacts);
        mfood = findViewById(R.id.mfood);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.recherche, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Write the name of the food");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                MyFood = query;
                afficher();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    public void afficher() {
        String url = "https://api.api-ninjas.com/v1/nutrition?query=" + MyFood;

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            //JSONArray items = response.getJSONObject(0);
                            JSONObject item = (JSONObject) response.get(0);
                            if (item.length() > 0) {

                                mfood.setText(item.getString("name"));
                                mtfat.setText("Total fat: " + item.getString("fat_total_g") + " g");
                                mfats.setText("Saturated fat: " + item.getString("fat_saturated_g") + " g");
                                msodium.setText("Sodium: " + item.getString("sodium_mg") + " mg");
                                mpotassium.setText("Potassium: " + item.getString("potassium_mg") + " mg");
                                mfiber.setText("Fiber: " + item.getString("fiber_g") + " g");
                                msugar.setText("Sugar: " + item.getString("sugar_g") + " g");
                                Log.d("ApiResponse", "Nutrition data fetched successfully");
                            } else {
                                Log.e("ApiResponse", "No data found for the item");
                            }
                        } catch (JSONException e) {
                            Log.e("ApiError", "JSON  error: " + e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ApiError", "Error: " + error.getMessage());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("X-Api-Key", "E4LNTkliWca2XzcSJGw/og==WP6qKXW53LRByDLJ");
                return headers;
            }
        };

        rqueue.add(jsonObjectRequest);

    }


}