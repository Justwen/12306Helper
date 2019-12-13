package com.justwen.trip.task;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.net.Uri;

import com.justwen.trip.TripApplication;
import com.justwen.trip.bean.TicketInfo;
import com.justwen.trip.common.OnSimpleCallback;
import com.justwen.trip.task.parser.ITicketParser;
import com.justwen.trip.task.parser.TicketOrderParser;
import com.justwen.trip.task.parser.TicketRefundParser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

    private static List<ITicketParser> sITicketParsers = new ArrayList<>();

    static {
        sITicketParsers.add(new TicketOrderParser());
        sITicketParsers.add(new TicketRefundParser());
    }

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
        for (ITicketParser parser : sITicketParsers) {
            if (parser.parse(sms, ret)) {
                return;
            }
        }
    }
}
