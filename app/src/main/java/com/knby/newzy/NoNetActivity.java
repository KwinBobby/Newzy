package com.knby.newzy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NoNetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_net);

        Button retry=(Button)findViewById(R.id.retry);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog checkNetDialog = ProgressDialog.show(NoNetActivity.this,"Checking", "Please wait...",false,false);

                if(isOnline()==true)
                {
                    /*
                    * if the user is now online ,
                    * stop the progress dialog,
                    * stop this activity and
                     * return to mainActivity */

                    checkNetDialog.dismiss();
                    Intent returnIntent=new Intent(NoNetActivity.this,MainActivity.class);
                    startActivity(returnIntent);
                    NoNetActivity.this.finish();

                }
                else
                {   //otherwise ,dismiss the progress dialog after 2 seconds so that user can retry
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            checkNetDialog.dismiss();
                        }
                    }, 2000);
                }
            }
        });
    }

    //Function to check if the user is connected to the net
    public boolean isOnline(){
        ConnectivityManager cm=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=cm.getActiveNetworkInfo();
        boolean result=(networkInfo!=null&&networkInfo.isConnected());
        return result;
    }
}
