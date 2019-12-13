package com.justwen.trip.task.parser;

import com.justwen.trip.bean.TicketInfo;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Justwen
 */
public class TicketRefundParser implements ITicketParser {

    private static final String TICKET_ORDER_REGEX = "【铁路12306】([\\u4E00-\\u9FA5]+)的([0-9]{1,2}月[0-9]{1,2}日)(.*?)次([0-9]{1,2}车[0-9]{1,2}[A-Z])号车票退票成功";

    private static final int INDEX_PASSENGER = 1;

    private static final int INDEX_DATE = 2;

    private static final int INDEX_TRAIN = 3;

    @Override
    public TicketInfo parse(String msg) {
        Matcher matcher = Pattern.compile(TICKET_ORDER_REGEX).matcher(msg);
        if (matcher.find()) {
            String date = matcher.group(INDEX_DATE);
            String passenger = matcher.group(INDEX_PASSENGER);
            String train = matcher.group(INDEX_TRAIN);

            TicketInfo ticketInfo = new TicketInfo();
            ticketInfo.setDate(date)
                    .setTrain(train)
                    .setPassenger(passenger)
                    .setState(TicketInfo.STATE_REFUND);
            return ticketInfo;
        }
        return null;
    }

    @Override
    public boolean parse(String msg, List<TicketInfo> ret) {
        TicketInfo ticketInfo = parse(msg);
        if (ticketInfo != null) {
            for (TicketInfo info : ret) {
                if (info.getDate().equals(ticketInfo.getDate()) && info.getPassenger().equals(ticketInfo.getPassenger())
                        && info.getTrain().equals(ticketInfo.getTrain())) {
                    info.setState(TicketInfo.STATE_REFUND);
                }
            }
            return true;
        }
        return false;
    }
}
