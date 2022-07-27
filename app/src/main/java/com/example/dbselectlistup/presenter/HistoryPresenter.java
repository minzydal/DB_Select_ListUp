package com.example.dbselectlistup.presenter;

import java.util.ArrayList;

public class HistoryPresenter implements HistoryContract.Presenter{

    HistoryContract.View view;

    public HistoryPresenter(HistoryContract.View view){
        this.view = view;
    }

    @Override
    public void getHistoryList(ArrayList<String> items) {
        view.historyList(items);
    }
}
