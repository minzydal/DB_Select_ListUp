package com.example.dbselectlistup.presenter;

import com.example.dbselectlistup.view.upgrademode.UpgradeListItem;

import java.util.ArrayList;

public interface RelativeEventContract {
    interface View{
        void relatedList(ArrayList<UpgradeListItem> items);
    }

    interface Presenter{
        void getRelatedList(ArrayList<UpgradeListItem> items);
    }
}
