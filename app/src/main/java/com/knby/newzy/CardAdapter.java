package com.knby.newzy;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {


    private ImageLoader imageLoader;
    private Context context;
    Intent intent;
    List<Article> articleFeed;

    public CardAdapter(List<Article> articleFeed, Context context){
        super();
        //Getting all the feeds
        this.articleFeed = articleFeed;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflating the layout for the feeds
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card, parent, false);
        ViewHolder holder=new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Article currentArticle=articleFeed.get(position);
        //Initialising the imageLoader
        imageLoader = MySingleton.getInstance(context).getImageLoader();
        imageLoader.get(currentArticle.getImageUrl(), ImageLoader.getImageListener(holder.logo, R.drawable.loading,R.drawable.error));

        holder.logo.setImageUrl(currentArticle.getImageUrl(), imageLoader);
        holder.title.setText(currentArticle.getTitle());
        //On clicking the view
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //Going to NewActivity to show the news for the particular feed
                intent=new Intent(context,NewsActivity.class);
                intent.putExtra("id",currentArticle.getId());
                intent.putExtra("sortBy",currentArticle.getSortBy());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount()
    {
        return articleFeed.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        //Defining viewHolder
         TextView title;
         NetworkImageView logo;
        public ViewHolder(View itemView) {
            super(itemView);
            //finding the required views by id
            title=(TextView)itemView.findViewById(R.id.title);
            logo=(NetworkImageView)itemView.findViewById(R.id.logo);
        }
    }
}
