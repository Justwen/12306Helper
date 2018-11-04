package com.justwen.trip.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

/**
 * Created by Justwen on 2018/11/4.
 */
public class BaseListFragment extends BaseFragment {

    private RecyclerView mRecyclerView;

    private View mEmptyView;

    private View mProgressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final Context context = getContext();
        FrameLayout root = new FrameLayout(context);
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        if (mProgressBar == null) {
            mProgressBar = createProgressBarDefault(context);
        } else {
            removeViewParent(mProgressBar);
        }

        if (mEmptyView != null) {
            removeViewParent(mEmptyView);
        } else {
            mEmptyView = new View(context);
        }

        mRecyclerView = new RecyclerView(context);
        mRecyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        root.addView(mProgressBar);
        root.addView(mEmptyView);
        root.addView(mRecyclerView);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        updateViewVisibility();
        super.onViewCreated(view, savedInstanceState);
    }

    private void removeViewParent(View view) {
        ViewParent parent = view.getParent();
        if (parent != null) {
            ((ViewGroup) parent).removeView(view);
        }
    }

    private View createProgressBarDefault(Context context) {
        FrameLayout container = new FrameLayout(context);
        container.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        ProgressBar progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleLarge);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;

        container.addView(progressBar, lp);

        return container;

    }

    private void updateViewVisibility() {
        RecyclerView.Adapter adapter = getAdapter();
        if (adapter == null) {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
        } else if (adapter.getItemCount() == 0) {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
        }

    }

    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
    }

    public void setProgressBar(View progressBar) {
        mProgressBar = progressBar;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    @Nullable
    public RecyclerView.Adapter getAdapter() {
        return mRecyclerView.getAdapter();
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        mRecyclerView.setAdapter(adapter);
        updateViewVisibility();
    }
}
