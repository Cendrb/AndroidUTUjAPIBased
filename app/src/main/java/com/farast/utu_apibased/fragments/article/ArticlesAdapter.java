package com.farast.utu_apibased.fragments.article;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.farast.utu_apibased.Bullshit;
import com.farast.utu_apibased.R;
import com.farast.utu_apibased.UtuLineGenericViewHolder;
import com.farast.utu_apibased.listeners.OnListFragmentInteractionListener;
import com.farast.utuapi.data.Article;
import com.farast.utuapi.data.DataLoader;

import java.util.List;

/**
 * Created by cendr_000 on 05.08.2016.
 */

public class ArticlesAdapter extends RecyclerView.Adapter<UtuLineGenericViewHolder> {
    private final List<Article> mValues;
    private final OnListFragmentInteractionListener<Article> mListener;
    private Context mContext;

    public ArticlesAdapter(OnListFragmentInteractionListener<Article> listener, Context context) {
        mValues = Bullshit.dataLoader.getArticlesList();
        mListener = listener;
        mContext = context;

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
    public UtuLineGenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.utu_line_generic, parent, false);
        return new UtuLineGenericViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final UtuLineGenericViewHolder holder, int position) {
        final Article item = mValues.get(position);
        holder.mTitleView.setText(item.getTitle());
        holder.mAvatarParent.setVisibility(View.GONE);
        if (item.isPublished())
            holder.mLeftBottomView.setText(Bullshit.prettyDate(item.getPublishedOn()));
        else
            holder.mLeftBottomView.setText(R.string.not_published);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }
}
