package com.example.dbselectlistup.view.upgrademode.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dbselectlistup.databinding.HistoryBinding;
import com.example.dbselectlistup.model.DBManager;
import com.example.dbselectlistup.presenter.HistoryContract;
import com.example.dbselectlistup.presenter.HistoryPresenter;
import java.util.ArrayList;
import java.util.Collections;

public class UpgradeHistoryFragment extends Fragment implements HistoryContract.View {
    HistoryBinding binding;
    HistoryListAdapter historyListAdapter;

    private HistoryContract.Presenter presenter;
    DBManager dbManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = HistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        dbManager = new DBManager();
        dbManager.getSharedPreferenceValue(getContext());

        presenter = new HistoryPresenter(this);
        presenter.getHistoryList(dbManager.historyList);

        return root;
    }

    @Override
    public void historyList(ArrayList<String> items) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        binding.historyRecyclerView.setLayoutManager(mLayoutManager);
        historyListAdapter = new HistoryListAdapter(getContext());
        Collections.reverse(items); //가장 최근에 추가된 값이 리스트의 가장 맨 위로 오도록 뒤집기
        for (int i = 0; i < items.size(); i++) {
            historyListAdapter.addItem(new HistoryItem(items.get(i)));
        }
        binding.historyRecyclerView.setAdapter(historyListAdapter);
    }
}
