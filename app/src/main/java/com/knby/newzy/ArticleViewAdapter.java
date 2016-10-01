package com.knby.newzy;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

public class ArticleViewAdapter extends RecyclerView.Adapter<ArticleViewAdapter.ViewHolder> {

    private ImageLoader imageLoader;
    private Context context;
    List<Article> articleFeed;

    public ArticleViewAdapter(List<Article> articleFeed, Context context){
        super();
        //Getting all the articles
        this.articleFeed = articleFeed;
        this.context = context;
    }

    @Override
    public ArticleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflating the article_view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.article_view, parent, false);
        ViewHolder holder=new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ArticleViewAdapter.ViewHolder holder, int position) {

        final Article currentArticle=articleFeed.get(position);
        //Initialising the imageLoader
        imageLoader = MySingleton.getInstance(context).getImageLoader();
        imageLoader.get(currentArticle.getImageUrl(), ImageLoader.getImageListener(holder.image,R.drawable.loading,R.drawable.error));

        holder.image.setImageUrl(currentArticle.getImageUrl(), imageLoader);
        holder.title.setText(currentArticle.getTitle());
        //On clicking the view
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Setting up the custom chrome tabs
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                //Setting the startAnimation
                builder.setStartAnimations(context,android.R.anim.fade_in,android.R.anim.fade_out);
                //Setting the exitAnimation
                builder.setExitAnimations(context,android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                //Setting the toolBarColor
                builder.setToolbarColor(Color.parseColor("#00BCD4"));
                //Show the title
                builder.setShowTitle(true);
                CustomTabsIntent customTabsIntent = builder.build();
                //Launching the URL
                customTabsIntent.launchUrl((Activity)context,Uri.parse(currentArticle.getURL()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return articleFeed.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        //Defining viewHolder
        TextView title;
        NetworkImageView image;
        public ViewHolder(View itemView) {
            //finding the required views by id
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.heading);
            image=(NetworkImageView)itemView.findViewById(R.id.image);
        }

    }
}
