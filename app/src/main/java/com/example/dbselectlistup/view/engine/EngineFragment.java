package com.example.dbselectlistup.view.engine;

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
import com.example.dbselectlistup.databinding.EnginelayoutBinding;
import com.example.dbselectlistup.model.DBManager;
import com.example.dbselectlistup.presenter.EngineContract;
import com.example.dbselectlistup.presenter.EnginePresenter;

import java.util.ArrayList;


public class EngineFragment extends Fragment implements EngineContract.View {

    EnginelayoutBinding binding;
    EngineListAdapter engineListAdapter;

    private EngineContract.Presenter presenter;
    DBManager dbManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = EnginelayoutBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        dbManager = new DBManager();
        dbManager.getSharedPreferenceValue(getContext());

        presenter = new EnginePresenter(this);
        presenter.getEngineList(dbManager.getEngineList());

        ((MainActivity) MainActivity.context_main).setEngineOnlyClicked();

        return root;
    }


    @Override
    public void engineList(ArrayList<EngineItem> items) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        binding.engineRecyclerView.setLayoutManager(mLayoutManager);

        engineListAdapter = new EngineListAdapter(getContext());
        binding.engineRecyclerView.setAdapter(engineListAdapter);
        engineListAdapter.addItem(items);
    }
}
