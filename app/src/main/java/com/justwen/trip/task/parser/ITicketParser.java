package com.justwen.trip.task.parser;

import com.justwen.trip.bean.TicketInfo;

import java.util.List;

public interface ITicketParser {

    TicketInfo parse(String msg);

    boolean parse(String msg, List<TicketInfo> ret);
}
