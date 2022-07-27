package com.example.dbselectlistup.presenter;

import com.example.dbselectlistup.view.upgrademode.UpgradeListItem;

import java.util.ArrayList;

public interface AllEventContract {
    interface View{
        void allEventList(ArrayList<UpgradeListItem> items);
    }

    interface Presenter{
        void getAllEventList(ArrayList<UpgradeListItem> items);
    }
}
