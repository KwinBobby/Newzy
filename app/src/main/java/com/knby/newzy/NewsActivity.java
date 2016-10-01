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

public  class NewsActivity extends AppCompatActivity {

    private static String API_KEY="887d38959b0a4dcbb9e186b0da0f9252";
    private List<Article> listOfArticles;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private JSONArray jsonArticles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Changing the title of the activity
        setTitle("Articles");
        //Checking whether the user is online
        if(isOnline()==false)
        { //If not online ,stop this activity and go to NoNetActivity
            Intent noNetIntent=new Intent(getApplicationContext(),NoNetActivity.class);
            startActivity(noNetIntent);
            NewsActivity.this.finish();
        }
        setContentView(R.layout.activity_news);

        Intent intent=getIntent();
        String id=intent.getStringExtra("id");
        String sortBy=intent.getStringExtra("sortBy");
        recyclerView = (RecyclerView) findViewById(R.id.recycview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        //Setting the recyclerView to have linearLayout
        recyclerView.setLayoutManager(layoutManager);

        //Initializing the article list
        listOfArticles = new ArrayList<>();
        final ProgressDialog loading = ProgressDialog.show(this,"Loading Data", "Please wait...",false,false);

        //Creating json object request
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, "https://newsapi.org/v1/articles?source=" + id + "&sortBy="+sortBy+"&apiKey=" + API_KEY, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loading.dismiss();
                try {
                    jsonArticles=response.getJSONArray("articles");
                    //Traversing through the JSONArray
                    for(int i=0;i<jsonArticles.length();i++){
                        Article article=new Article();
                        //Getting each object
                        JSONObject jsonObject=jsonArticles.getJSONObject(i);
                        article.setTitle(jsonObject.getString("title"));
                        article.setImageUrl(jsonObject.getString("urlToImage"));
                        article.setURL(jsonObject.getString("url"));
                        //Adding the object to the list
                        listOfArticles.add(article);
                    }
                    //Initializing the adapter
                    adapter = new ArticleViewAdapter(listOfArticles,NewsActivity.this);
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
