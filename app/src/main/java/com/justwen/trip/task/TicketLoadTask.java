package com.justwen.trip.task;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.net.Uri;

import com.justwen.trip.TripApplication;
import com.justwen.trip.bean.TicketInfo;
import com.justwen.trip.common.OnSimpleCallback;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Justwen on 2018/10/21.
 */
public class TicketLoadTask {

    private static final String TICKET_ORDER_REGEX = "订单(.*?),(.*?)您已购(.*?)日(.*?)次(.*?),(.*[\\u4E00-\\u9FA5])(.*[0-9])开";

    private static final String TICKET_CHANGE_REGEX = "订单(.*?),(.*?)您已签(.*?)日(.*?)次(.*?),(.*[\\u4E00-\\u9FA5])(.*[0-9])开";

    private static final String TICKET_RETURN_REGEX = "【铁路客服】(.*?)的(.*?)日(.*?)次(.*?)车票退票成功";

    private static final int INDEX_ORDER_ID = 1;

    private static final int INDEX_NAME = 2;

    private static final int INDEX_DATE = 3;

    private static final int INDEX_TRAIN = 4;

    private static final int INDEX_SEAT = 5;

    private static final int INDEX_FROM = 6;

    private static final int INDEX_TIME = 7;


    @SuppressLint("CheckResult")
    public static void execute(final OnSimpleCallback<List<TicketInfo>> callback) {
        Observable.create(new ObservableOnSubscribe<List<TicketInfo>>() {

            @Override
            public void subscribe(ObservableEmitter<List<TicketInfo>> e) throws Exception {
                e.onNext(sort(loadTripInfo()));
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<TicketInfo>>() {
                    @Override
                    public void accept(List<TicketInfo> ticketInfos) throws Exception {
                        callback.onResult(ticketInfos);
                    }
                });

    }

    private static List<TicketInfo> sort(List<TicketInfo> list) {
        Collections.sort(list, new Comparator<TicketInfo>() {
            @Override
            public int compare(TicketInfo o1, TicketInfo o2) {
                return o1.compareTo(o2);
            }
        });
        return list;
    }

    private static List<TicketInfo> loadTripInfo() {
        List<TicketInfo> result = new ArrayList<>();
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -1);
            try (Cursor cursor = TripApplication.getContext().getContentResolver().query(Uri.parse("content://sms/"),
                    new String[]{"address", "body", "date"},
                    "date > ? and address=?",
                    new String[]{String.valueOf(calendar.getTime().getTime()), "12306"}, "date asc")) {
                if (cursor != null) {
                    String body;
                    while (cursor.moveToNext()) {
                        body = cursor.getString(cursor.getColumnIndex("body"));
                        body = body.replaceAll("，", ",");
                        parse(result, body);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static void parse(List<TicketInfo> ret, String sms) {

        TicketInfo ticketInfo = parseOrder(sms);
        if (ticketInfo != null && !ret.contains(ticketInfo)) {
            ret.add(ticketInfo);
            return;
        }

        ticketInfo = parseChange(sms);
        if (ticketInfo != null) {
            int index = ret.indexOf(ticketInfo);
            if (index >= 0) {
                ret.get(index).setState(TicketInfo.STATE_CHANGE);
            }
            ret.add(ticketInfo);
            return;
        }

        ticketInfo = parseReturn(sms);
        if (ticketInfo != null) {
            for (TicketInfo info : ret) {
                if (info.getPassenger().equals(ticketInfo.getPassenger())
                        && info.getTrain().equals(ticketInfo.getTrain())
                        && info.getDate().equals(ticketInfo.getDate())) {
                    info.setState(TicketInfo.STATE_RETURN);
                    return;
                }
            }
        }
    }


    private static TicketInfo parseOrder(String sms) {
        Matcher matcher = Pattern.compile(TICKET_ORDER_REGEX).matcher(sms);
        if (matcher.find()) {
            TicketInfo ticketInfo = new TicketInfo();
            ticketInfo.setTrain(matcher.group(INDEX_TRAIN))
                    .setSeat(matcher.group(INDEX_SEAT))
                    .setPassenger(matcher.group(INDEX_NAME))
                    .setDepartureStation(matcher.group(INDEX_FROM))
                    .setDate(matcher.group(INDEX_DATE) + "日")
                    .setTime(matcher.group(INDEX_TIME))
                    .setOrderId(matcher.group(INDEX_ORDER_ID));
            if (isValid(ticketInfo)) {
                return ticketInfo;
            }
        }
        return null;
    }


    private static boolean isValid(TicketInfo ticketInfo) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM月dd日HH:mm", Locale.getDefault());
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            if (Calendar.DECEMBER == month && ticketInfo.getDate().contains("1月") && !ticketInfo.getDate().contains("11月")) {
                year++;
            }
            String dateStr = year + ticketInfo.getDate() + ticketInfo.getTime();
            Date date = dateFormat.parse(dateStr);
            if (date.getTime() >= System.currentTimeMillis()) {
                ticketInfo.setTimeMillis(date.getTime());
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static TicketInfo parseChange(String sms) {
        Matcher matcher = Pattern.compile(TICKET_CHANGE_REGEX).matcher(sms);
        if (matcher.find()) {
            TicketInfo ticketInfo = new TicketInfo();
            ticketInfo.setTrain(matcher.group(INDEX_TRAIN))
                    .setSeat(matcher.group(INDEX_SEAT))
                    .setPassenger(matcher.group(INDEX_NAME))
                    .setDepartureStation(matcher.group(INDEX_FROM))
                    .setDate(matcher.group(INDEX_DATE) + "日")
                    .setTime(matcher.group(INDEX_TIME))
                    .setOrderId(matcher.group(INDEX_ORDER_ID))
                    .setState(TicketInfo.STATE_ORDER_CHANGE);
            if (isValid(ticketInfo)) {
                return ticketInfo;
            }
        }
        return null;
    }

    private static TicketInfo parseReturn(String sms) {
        Matcher matcher = Pattern.compile(TICKET_RETURN_REGEX).matcher(sms);
        if (matcher.find()) {
            TicketInfo ticketInfo = new TicketInfo();
            ticketInfo.setPassenger(matcher.group(1))
                    .setTrain(matcher.group(3))
                    .setDate(matcher.group(2) + "日")
                    .setSeat(matcher.group(4))
                    .setState(TicketInfo.STATE_RETURN);
            return ticketInfo;
        }
        return null;
    }
}
