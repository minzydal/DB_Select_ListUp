package com.example.dbselectlistup.presenter;

import com.example.dbselectlistup.view.area.AreaItem;

import java.util.ArrayList;

public interface AreaContract {
    interface View{
        void areaList(ArrayList<AreaItem> items);
    }

    interface Presenter{
        void getAreaList(ArrayList<AreaItem> items);
    }
}
