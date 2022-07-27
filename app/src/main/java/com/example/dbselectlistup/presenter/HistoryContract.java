package com.example.dbselectlistup.presenter;

import java.util.ArrayList;

public interface HistoryContract {
    interface View{
        void historyList(ArrayList<String> items);
    }

    interface Presenter{
        void getHistoryList(ArrayList<String> items);
    }
}
