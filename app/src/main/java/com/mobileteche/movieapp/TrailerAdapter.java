package com.mobileteche.movieapp;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Santosh.Shenoy on 21-09-2015.
 */
public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {
    private final Context mContext;
    private View.OnClickListener onClick;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView)v.findViewById(R.id.list_item_trailer_textview);
        }
    }

    public TrailerAdapter(Context context) {
        mContext = context;
    }



    private List<TrailerModel> trailetList = new ArrayList<TrailerModel>();



    View v;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // create a new view
        v =  LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_trailer, viewGroup, false);

        ViewHolder vh = new ViewHolder(v);

        onClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewHolder holder = (ViewHolder) v.getTag();
                int position = holder.getPosition();
                TrailerModel tempTrailerModel = trailetList.get(position);
                watchYoutubeVideo(tempTrailerModel.key);

            }
        };


        return vh;
    }

    public  void watchYoutubeVideo(String id){
        try{
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
             mContext.startActivity(intent);
        }catch (ActivityNotFoundException ex){
            Intent intent=new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v="+id));
            mContext.startActivity(intent);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.mTextView.setText(trailetList.get(i).name);

        viewHolder.mTextView.setOnClickListener(onClick);
        viewHolder.mTextView.setTag(viewHolder);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return trailetList.size();
    }

    public TrailerModel getTrailerModel (int position)
    {
        return trailetList.get(position);
    }

    public boolean add(TrailerModel object) {

        trailetList.add(object);
        return true;
    }
}
