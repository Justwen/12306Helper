package com.justwen.trip.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;

import com.justwen.trip.adapter.TrainAdapter;
import com.justwen.trip.bean.TicketInfo;
import com.justwen.trip.common.OnSimpleCallback;
import com.justwen.trip.task.SmsLoadTask;

import java.util.List;

/**
 * Created by Justwen on 2018/10/21.
 */
public class SmsListFragment extends ListFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SmsLoadTask.execute(new OnSimpleCallback<List<TicketInfo>>() {
            @Override
            public void onResult(List<TicketInfo> ticketInfos) {
                setListAdapter(new TrainAdapter(ticketInfos));
            }
        });
    }

}
