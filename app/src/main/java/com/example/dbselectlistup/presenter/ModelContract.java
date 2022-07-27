package com.example.dbselectlistup.presenter;

import com.example.dbselectlistup.view.vehiclemodel.VehicleModelItem;

import java.util.ArrayList;

public interface ModelContract {
    interface View{
        void modelList(ArrayList<VehicleModelItem> items);
    }

    interface Presenter{
        void getModelList(ArrayList<VehicleModelItem> items);
    }
}
