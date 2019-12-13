package com.justwen.trip;

import com.justwen.trip.task.parser.ITicketParser;
import com.justwen.trip.task.parser.TicketOrderParser;
import com.justwen.trip.task.parser.TicketRefundParser;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testOrderMsg() {
        String msg = "【铁路12306】订单E096400200,12月10日C2075次3车13F号,北京南站20:06开,检票口：19。请李明持购票证件进站乘车。您购买的是电子客票,无须换取纸质车票,请直接持购票证件进站检票乘车。如需报销凭证,乘车后30日内在任意车站自动售（取）设备、售票窗口均可换取。";

        TicketOrderParser parser = new TicketOrderParser();
        System.out.println(parser.parse(msg.replace("，", ",")).toString());
    }

    @Test
    public void testRefundMsg() {
        String msg = "【铁路12306】李明的1月9日C2561次2车2F号车票退票成功。流水号2E026154193001001122859303。";

        ITicketParser parser = new TicketRefundParser();
        System.out.println(parser.parse(msg).toString());
    }
}