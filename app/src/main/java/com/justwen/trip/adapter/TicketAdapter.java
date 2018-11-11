package com.justwen.trip.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.justwen.trip.R;
import com.justwen.trip.bean.TicketInfo;

import java.util.List;

/**
 * Created by Justwen on 2018/10/21.
 */
public class TicketAdapter extends BaseAdapter {

    private List<TicketInfo> mDataList;

    public TicketAdapter(List<TicketInfo> dataList) {
        mDataList = dataList;
    }

    @Override
    public int getCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Deprecated
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_train, parent, false);
        }
        TextView nameView = convertView.findViewById(R.id.tv_name);
        TextView dateView = convertView.findViewById(R.id.tv_date);
        TextView trainView = convertView.findViewById(R.id.tv_train);
        TextView fromView = convertView.findViewById(R.id.tv_from);
        TextView seatView = convertView.findViewById(R.id.tv_seat);
        TextView stateView = convertView.findViewById(R.id.tv_state);

        TicketInfo ticketInfo = (TicketInfo) getItem(position);

        nameView.setText(String.format("乘客：%s", ticketInfo.getPassenger()));
        dateView.setText(String.format("乘车时间：%s %s", ticketInfo.getDate(), ticketInfo.getTime()));
        trainView.setText(String.format("车次：%s", ticketInfo.getTrain()));
        fromView.setText(String.format("始发地：%s", ticketInfo.getDepartureStation()));
        seatView.setText(String.format("座位：%s", ticketInfo.getSeat()));
        if (ticketInfo.getState() == TicketInfo.STATE_CHANGE) {
            stateView.setText("车票状态：已改签");
            stateView.setTextColor(parent.getContext().getColor(android.R.color.holo_blue_dark));
        } else if (ticketInfo.getState() == TicketInfo.STATE_RETURN) {
            stateView.setText("车票状态：已退票");
            stateView.setTextColor(parent.getContext().getColor(android.R.color.holo_red_dark));
        } else if (ticketInfo.getState() == TicketInfo.STATE_ORDER_CHANGE) {
            stateView.setText("车票状态：改签票");
            stateView.setTextColor(parent.getContext().getColor(android.R.color.holo_blue_dark));
        } else {
            stateView.setText("");
        }
        return convertView;
    }
}
