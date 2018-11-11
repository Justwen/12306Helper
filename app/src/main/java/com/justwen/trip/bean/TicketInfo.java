package com.justwen.trip.bean;

import android.support.annotation.NonNull;

/**
 * Created by Justwen on 2018/10/21.
 */
public class TicketInfo implements Comparable<TicketInfo> {

    public static final int STATE_ORDER = 0;

    public static final int STATE_CHANGE = 1;

    public static final int STATE_ORDER_CHANGE = 3;

    public static final int STATE_RETURN = 2;

    private String mOrderId;

    private String mSeat;

    private String mDate;

    private String mTime;

    private String mDepartureStation;

    private String mDestination;

    private String mTrain;

    private String mPassenger;

    private int mState = STATE_ORDER;

    private long mTimeMillis;

    public String getSeat() {
        return mSeat;
    }

    public TicketInfo setSeat(String seat) {
        mSeat = seat;
        return this;
    }

    public String getDate() {
        return mDate;
    }

    public TicketInfo setDate(String date) {
        mDate = date;
        return this;
    }

    public String getDepartureStation() {
        return mDepartureStation;
    }

    public TicketInfo setDepartureStation(String departureStation) {
        mDepartureStation = departureStation;
        return this;
    }

    public String getDestination() {
        return mDestination;
    }

    public void setDestination(String destination) {
        mDestination = destination;
    }

    public String getTrain() {
        return mTrain;
    }

    public TicketInfo setTrain(String train) {
        mTrain = train;
        return this;
    }

    public String getPassenger() {
        return mPassenger;
    }

    public TicketInfo setPassenger(String passenger) {
        mPassenger = passenger;
        return this;
    }

    public long getTimeMillis() {
        return mTimeMillis;
    }

    public void setTimeMillis(long timeMillis) {
        mTimeMillis = timeMillis;
    }

    @Override
    public int compareTo(@NonNull TicketInfo o) {
        if (mTimeMillis > o.getTimeMillis()) {
            return 1;
        } else if (mTimeMillis < o.getTimeMillis()) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof TicketInfo && mOrderId.equals(((TicketInfo) obj).getOrderId());
    }

    public String getOrderId() {
        return mOrderId;
    }

    public TicketInfo setOrderId(String orderId) {
        mOrderId = orderId;
        return this;
    }

    public int getState() {
        return mState;
    }

    public void setState(int state) {
        mState = state;
    }

    public String getTime() {
        return mTime;
    }

    public TicketInfo setTime(String time) {
        mTime = time;
        return this;
    }
}
