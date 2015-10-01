package com.mobileteche.movieapp;

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
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private final Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextAuthor,mTextContent;
        public ViewHolder(View v) {
            super(v);
            mTextAuthor = (TextView)v.findViewById(R.id.list_item_author_textview);
            mTextContent = (TextView)v.findViewById(R.id.list_item_content_textview);
        }
    }

    public ReviewAdapter(Context context) {
        mContext = context;
    }



    private List<ReviewModel> reviewsList = new ArrayList<ReviewModel>();


    private View.OnClickListener onClick;


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // create a new view
        View v =  LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_reviews, viewGroup, false);

        ViewHolder vh = new ViewHolder(v);
        onClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewHolder holder = (ViewHolder) v.getTag();
                int position = holder.getPosition();
                ReviewModel tempReviewModel = reviewsList.get(position);

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(tempReviewModel.url));
                mContext.startActivity(i);
            }
        };
        return vh;
    }







    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.mTextAuthor.setText(reviewsList.get(i).author);
        viewHolder.mTextContent.setText(reviewsList.get(i).content);
        viewHolder.mTextContent.setOnClickListener(onClick);
        viewHolder.mTextAuthor.setOnClickListener(onClick);
        viewHolder.mTextAuthor.setTag(viewHolder);
        viewHolder.mTextContent.setTag(viewHolder);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }



    public boolean add(ReviewModel object) {

        reviewsList.add(object);
        return true;
    }
}
