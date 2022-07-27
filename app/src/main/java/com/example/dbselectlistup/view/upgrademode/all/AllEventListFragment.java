package com.example.dbselectlistup.view.upgrademode.all;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dbselectlistup.databinding.AllBinding;
import com.example.dbselectlistup.model.DBManager;
import com.example.dbselectlistup.presenter.AllEventContract;
import com.example.dbselectlistup.presenter.AllEventPresenter;
import com.example.dbselectlistup.view.upgrademode.UpgradeListItem;

import java.util.ArrayList;

public class AllEventListFragment extends Fragment implements AllEventContract.View {
    AllBinding binding;
    AllEventListAdapter allEventListAdapter;

    private AllEventContract.Presenter presenter;
    DBManager dbManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = AllBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        dbManager = new DBManager();
        dbManager.getSharedPreferenceValue(getContext());

        presenter = new AllEventPresenter(this);
        presenter.getAllEventList(dbManager.getAllEventList());

        return root;
    }

    @Override
    public void allEventList(ArrayList<UpgradeListItem> items) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        binding.allEventRecyclerView.setLayoutManager(mLayoutManager);
        allEventListAdapter = new AllEventListAdapter(getContext());
        allEventListAdapter.initializeEventList();

        binding.allEventRecyclerView.setAdapter(allEventListAdapter);
        allEventListAdapter.addItem(items);
    }
}
