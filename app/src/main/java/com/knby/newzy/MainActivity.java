package com.knby.newzy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Article> ArticleList;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    //The JSONArray which will store the JSONObjects in 'sources'
    public JSONArray proper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //Changing the title of the activity
        setTitle("  Your feeds");
        //Checking whether the user is online
        if(isOnline()==false)
        {   //If not online ,stop this activity and go to NoNetActivity
            Intent noNetIntent=new Intent(getApplicationContext(),NoNetActivity.class);
            startActivity(noNetIntent);
            MainActivity.this.finish();
        }
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        //Setting the recyclerView to have linearLayout
        recyclerView.setLayoutManager(layoutManager);

        //Initializing the article list
        ArticleList = new ArrayList<>();

        final ProgressDialog loading = ProgressDialog.show(this,"Loading Data", "Please wait...",false,false);

        //Creating a json object request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://newsapi.org/v1/sources?language=en", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loading.dismiss();
                try {
                     proper=response.getJSONArray("sources");
                    //Traversing through the JSONArray
                    for(int i = 0; i<proper.length(); i++) {
                        Article article = new Article();
                            //Getting each object
                            JSONObject jsonObject = proper.getJSONObject(i);
                        //Getting the available sorting options
                            JSONArray jsonSortBy=jsonObject.getJSONArray("sortBysAvailable");
                        //Getting the imageURL
                            JSONObject stringJSON=jsonObject.getJSONObject("urlsToLogos");

                            article.setImageUrl(stringJSON.getString("small"));
                            article.setTitle(jsonObject.getString("name"));
                            article.setId(jsonObject.getString("id"));
                        //Setting the sorting option as the first sorting option available
                            article.setSortBy(jsonSortBy.getString(0));
                      //Adding the object to the list
                        ArticleList.add(article);
                    }

                    //Initializing the adapter
                    adapter = new CardAdapter(ArticleList,MainActivity.this);
                    //Adding adapter to recyclerView
                    recyclerView.setAdapter(adapter);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        //Adding request to the queue
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    //Function to check if the user is connected to the net
    public boolean isOnline(){
        ConnectivityManager cm=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=cm.getActiveNetworkInfo();
        boolean result=(networkInfo!=null&&networkInfo.isConnected());

        return result;
    }

}
