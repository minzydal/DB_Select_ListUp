package com.example.dbselectlistup.presenter;

import com.example.dbselectlistup.view.vehiclemodel.VehicleModelItem;

import java.util.ArrayList;

public class ModelPresenter implements ModelContract.Presenter{

    ModelContract.View view;

    public ModelPresenter(ModelContract.View view){
        this.view = view;
    }

    @Override
    public void getModelList(ArrayList<VehicleModelItem> items) {
        view.modelList(items);
    }
}
