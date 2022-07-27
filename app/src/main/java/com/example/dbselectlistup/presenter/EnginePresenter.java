package com.example.dbselectlistup.presenter;

import com.example.dbselectlistup.view.engine.EngineItem;

import java.util.ArrayList;

public class EnginePresenter implements EngineContract.Presenter{

    EngineContract.View view;

    public EnginePresenter(EngineContract.View view){
        this.view = view;
    }

    @Override
    public void getEngineList(ArrayList<EngineItem> items) {
        view.engineList(items);
    }
}
