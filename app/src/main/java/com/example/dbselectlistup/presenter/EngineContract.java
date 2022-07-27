package com.example.dbselectlistup.presenter;

import com.example.dbselectlistup.view.engine.EngineItem;

import java.util.ArrayList;

public interface EngineContract {
    interface View{
        void engineList(ArrayList<EngineItem> items);
    }

    interface Presenter{
        void getEngineList(ArrayList<EngineItem> items);
    }
}
