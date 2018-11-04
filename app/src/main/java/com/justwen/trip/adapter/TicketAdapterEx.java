package com.justwen.trip.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.justwen.trip.bean.TicketInfo;

/**
 * Created by Justwen on 2018/11/4.
 */
public class TicketAdapterEx extends BaseAdapter<TicketInfo, TicketAdapterEx.TicketViewHolder> {

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {

    }

    public static class TicketViewHolder extends BaseAdapter.ViewHolderEx {

        TextView trainView;

        TextView seatView;

        public TicketViewHolder(View itemView) {
            super(itemView);
        }
    }
}
