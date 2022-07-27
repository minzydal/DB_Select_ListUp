package com.example.dbselectlistup.view.vehiclemodel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dbselectlistup.MainActivity;
import com.example.dbselectlistup.databinding.ModellayoutBinding;
import com.example.dbselectlistup.model.DBManager;
import com.example.dbselectlistup.presenter.ModelContract;
import com.example.dbselectlistup.presenter.ModelPresenter;

import java.util.ArrayList;

public class VehicleModelFragment extends Fragment implements ModelContract.View {

    ModellayoutBinding binding;
    VehicleModelListAdapter vehicleModelListAdapter;

    private ModelContract.Presenter presenter;
    DBManager dbManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ModellayoutBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        dbManager = new DBManager();
        dbManager.getSharedPreferenceValue(getContext());

        presenter = new ModelPresenter(this);
        presenter.getModelList(dbManager.getModelList());

        ((MainActivity)MainActivity.context_main).setModelOnlyClicked();

        return root;
    }

    @Override
    public void modelList(ArrayList<VehicleModelItem> items) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        binding.modelRecyclerView.setLayoutManager(mLayoutManager);
        vehicleModelListAdapter = new VehicleModelListAdapter(getContext());
        binding.modelRecyclerView.setAdapter(vehicleModelListAdapter);
        vehicleModelListAdapter.addItem(items);
    }
}
