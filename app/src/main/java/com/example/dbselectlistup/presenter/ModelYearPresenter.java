package com.example.dbselectlistup.presenter;

import java.util.ArrayList;

public class ModelYearPresenter implements ModelYearContract.Presenter{

    ModelYearContract.View view;

    public ModelYearPresenter(ModelYearContract.View view){
        this.view = view;
    }

    @Override
    public void getModelYearList(ArrayList<String> items) {
        view.modelYearList(items);
    }
}
