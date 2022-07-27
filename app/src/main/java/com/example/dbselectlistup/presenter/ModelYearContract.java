package com.example.dbselectlistup.presenter;

import java.util.ArrayList;

public interface ModelYearContract {
    interface View{
        void modelYearList(ArrayList<String> items);
    }

    interface Presenter{
        void getModelYearList(ArrayList<String> items);
    }
}
