package com.example.dbselectlistup.presenter;

import com.example.dbselectlistup.view.upgrademode.UpgradeListItem;

import java.util.ArrayList;

public class RelativeEventPresenter implements RelativeEventContract.Presenter{

    RelativeEventContract.View view;

    public RelativeEventPresenter(RelativeEventContract.View view){
        this.view = view;
    }

    @Override
    public void getRelatedList(ArrayList<UpgradeListItem> items) {
        view.relatedList(items);
    }
}
