package com.justwen.trip.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Justwen on 2018/11/4.
 */
public abstract class BaseAdapter<E, T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {

    public abstract static class ViewHolderEx extends RecyclerView.ViewHolder {

        public ViewHolderEx(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private List<E> mDateList;

    public void setData(List<E> data) {
        mDateList = data;
    }

    public void append(List<E> data) {
        if (mDateList == null) {
            mDateList = new ArrayList<>();
        }
        mDateList.addAll(data);
    }

    public void appent(E data) {
        if (mDateList == null) {
            mDateList = new ArrayList<>();
        }
        mDateList.add(data);
    }

    public E getItem(int position) {
        return mDateList.get(position);
    }

    @Override
    public int getItemCount() {
        return mDateList == null ? 0 : mDateList.size();
    }
}
