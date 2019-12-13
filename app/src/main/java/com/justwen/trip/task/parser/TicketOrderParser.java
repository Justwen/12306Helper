package com.justwen.trip.task.parser;

import com.justwen.trip.bean.TicketInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Justwen
 */
public class TicketOrderParser implements ITicketParser {

    private static final String TICKET_ORDER_REGEX = "【铁路12306】订单([0-9A-Z]+),([0-9]{1,2}月[0-9]{1,2}日)(.*?)次([0-9]{1,2}车[0-9]{1,2}[A-Z])号,([\\u4E00-\\u9FA5]+)站(.*?)开,检票口：(.*?)。请([\\u4E00-\\u9FA5]+)持购票证件进站乘车";

    private static final int INDEX_ID = 1;

    private static final int INDEX_DATE = 2;

    private static final int INDEX_TRAIN = 3;

    private static final int INDEX_SEAT = 4;

    private static final int INDEX_DEPARTURE = 5;

    private static final int INDEX_TIME = 6;

    private static final int INDEX_PASSENGER = 8;

    @Override
    public TicketInfo parse(String msg) {
        Matcher matcher = Pattern.compile(TICKET_ORDER_REGEX).matcher(msg);
        if (matcher.find()) {
            String date = matcher.group(INDEX_DATE);
            String seat = matcher.group(INDEX_SEAT);
            String id = matcher.group(INDEX_ID);
            String time = matcher.group(INDEX_TIME);
            String departure = matcher.group(INDEX_DEPARTURE);
            String passenger = matcher.group(INDEX_PASSENGER);
            String train = matcher.group(INDEX_TRAIN);

            TicketInfo ticketInfo = new TicketInfo();
            ticketInfo.setDate(date)
                    .setTime(time)
                    .setDepartureStation(departure)
                    .setOrderId(id)
                    .setSeat(seat)
                    .setTrain(train)
                    .setPassenger(passenger)
                    .setState(TicketInfo.STATE_ORDER);
            return ticketInfo;
        }
        return null;
    }

    @Override
    public boolean parse(String msg, List<TicketInfo> ret) {
        TicketInfo ticketInfo = parse(msg);
        if (ticketInfo != null) {
            if (isValid(ticketInfo)) {
                if (ret.contains(ticketInfo)) {
                    ret.remove(ticketInfo);
                    ticketInfo.setState(TicketInfo.STATE_CHANGE);
                    ret.add(ticketInfo);
                } else {
                    ret.add(ticketInfo);
                }
            }
            return true;
        }
        return false;
    }

    private boolean isValid(TicketInfo ticketInfo) {
        if (ticketInfo == null) {
            return false;
        }
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
}
