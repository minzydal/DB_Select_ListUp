package com.example.dbselectlistup.presenter;

import com.example.dbselectlistup.view.area.AreaItem;

import java.util.ArrayList;

public class AreaPresenter implements AreaContract.Presenter {
    AreaContract.View view;

    public AreaPresenter(AreaContract.View view) {
        this.view = view;
    }

    @Override
    public void getAreaList(ArrayList<AreaItem> items) {
        view.areaList(items);
    }
}
