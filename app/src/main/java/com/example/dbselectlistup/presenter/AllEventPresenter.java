package com.example.dbselectlistup.presenter;

import com.example.dbselectlistup.view.upgrademode.UpgradeListItem;

import java.util.ArrayList;

public class AllEventPresenter implements AllEventContract.Presenter{

    AllEventContract.View view;

    public AllEventPresenter(AllEventContract.View view){
        this.view = view;
    }


    @Override
    public void getAllEventList(ArrayList<UpgradeListItem> items) {
        view.allEventList(items);
    }
}
