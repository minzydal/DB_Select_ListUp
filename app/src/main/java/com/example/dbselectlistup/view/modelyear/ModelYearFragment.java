package com.example.dbselectlistup.view.modelyear;

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
import com.example.dbselectlistup.databinding.ModelyearlayoutBinding;
import com.example.dbselectlistup.model.DBManager;
import com.example.dbselectlistup.presenter.ModelYearContract;
import com.example.dbselectlistup.presenter.ModelYearPresenter;

import java.util.ArrayList;

public class ModelYearFragment extends Fragment implements ModelYearContract.View {

    ModelyearlayoutBinding binding;
    ModelYearListAdapter modelYearListAdapter;

    private ModelYearContract.Presenter presenter;
    DBManager dbManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ModelyearlayoutBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        dbManager = new DBManager();
        dbManager.getSharedPreferenceValue(getContext());

        presenter = new ModelYearPresenter(this);
        presenter.getModelYearList(dbManager.getModelYearList());

        ((MainActivity) MainActivity.context_main).setYearOnlyClicked();

        return root;
    }

    // 수정한 부분
    @Override
    public void modelYearList(ArrayList<String> items) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        binding.modelYearRecyclerView.setLayoutManager(mLayoutManager);
        modelYearListAdapter = new ModelYearListAdapter(getContext());

        for (int i = 0; i < items.size(); i++) {
            modelYearListAdapter.addItem(new ModelYearItem(items.get(i)));
        }

        binding.modelYearRecyclerView.setAdapter(modelYearListAdapter);
    }
}
