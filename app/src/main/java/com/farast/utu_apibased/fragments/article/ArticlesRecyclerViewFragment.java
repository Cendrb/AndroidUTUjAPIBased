package com.farast.utu_apibased.fragments.article;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.farast.utu_apibased.Bullshit;
import com.farast.utu_apibased.R;
import com.farast.utu_apibased.listeners.OnListFragmentInteractionListener;
import com.farast.utuapi.data.Article;
import com.farast.utuapi.data.DataLoader;
import com.farast.utuapi.util.DateUtil;

import java.util.List;

/**
 * Created by cendr_000 on 05.08.2016.
 */

public class ArticlesRecyclerViewFragment extends RecyclerView.Adapter<ArticlesRecyclerViewFragment.ViewHolder> {
    private final List<Article> mValues;
    private final OnListFragmentInteractionListener<Article> mListener;

    public ArticlesRecyclerViewFragment(OnListFragmentInteractionListener<Article> listener, Context context) {
        mValues = Bullshit.dataLoader.getArticlesList();
        mListener = listener;

        final Handler handler = new Handler(context.getMainLooper());

        Bullshit.dataLoader.getNotifier().setArticlesListener(new DataLoader.OnDataSetListener() {
            @Override
            public void onDataSetChanged() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mValues.clear();
                        mValues.addAll(Bullshit.dataLoader.getArticlesList());
                        notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @Override
    public ArticlesRecyclerViewFragment.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_event, parent, false);
        return new ArticlesRecyclerViewFragment.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ArticlesRecyclerViewFragment.ViewHolder holder, int position) {
        Article item = mValues.get(position);
        holder.mItem = item;
        holder.mTitleView.setText(item.getTitle());
        if(item.isPublished())
        holder.mDateView.setText(DateUtil.CZ_SHORT_DATE_FORMAT.format(item.getPublishedOn()));
        else
            holder.mDateView.setText(R.string.not_published);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView;
        public final TextView mDateView;
        public Article mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.event_title);
            mDateView = (TextView) view.findViewById(R.id.event_description);
        }
    }
}
