package com.example.dbselectlistup.view.upgrademode.related;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dbselectlistup.databinding.RelatedBinding;
import com.example.dbselectlistup.model.DBManager;
import com.example.dbselectlistup.presenter.RelativeEventContract;
import com.example.dbselectlistup.presenter.RelativeEventPresenter;
import com.example.dbselectlistup.view.upgrademode.UpgradeListItem;

import java.util.ArrayList;

public class RelatedEventListFragment extends Fragment implements RelativeEventContract.View {
    RelatedBinding binding;
    RelatedListAdapter relatedListAdapter;

    private RelativeEventContract.Presenter presenter;
    DBManager dbManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = RelatedBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        dbManager = new DBManager();
        dbManager.getSharedPreferenceValue(getContext());

        presenter = new RelativeEventPresenter(this);
        presenter.getRelatedList(dbManager.getRelatedList());

        return root;
    }

    @Override
    public void relatedList(ArrayList<UpgradeListItem> items) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        binding.relatedRecyclerView.setLayoutManager(mLayoutManager);
        relatedListAdapter = new RelatedListAdapter(getContext());
        binding.relatedRecyclerView.setAdapter(relatedListAdapter);
        relatedListAdapter.addItem(items);
    }
}
