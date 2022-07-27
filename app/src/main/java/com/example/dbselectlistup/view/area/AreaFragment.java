//지역선택 프래그먼트
package com.example.dbselectlistup.view.area;

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
import com.example.dbselectlistup.databinding.ArealayoutBinding;
import com.example.dbselectlistup.model.DBManager;
import com.example.dbselectlistup.presenter.AreaContract;
import com.example.dbselectlistup.presenter.AreaPresenter;

import java.util.ArrayList;

public class AreaFragment extends Fragment implements AreaContract.View {
    ArealayoutBinding binding;
    AreaListAdapter areaListAdapter;

    private AreaContract.Presenter presenter;
    DBManager dbManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ArealayoutBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        dbManager = new DBManager();
        presenter = new AreaPresenter(this);
        presenter.getAreaList(dbManager.getFileList());

        ((MainActivity) MainActivity.context_main).deactivateConfirmButton();
        ((MainActivity) MainActivity.context_main).setAreaOnlyClicked();

        return root;
    }

    @Override
    public void areaList(ArrayList<AreaItem> items) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        binding.areaRecyclerView.setLayoutManager(mLayoutManager);
        areaListAdapter = new AreaListAdapter(getContext());
        binding.areaRecyclerView.setAdapter(areaListAdapter);
        areaListAdapter.addItem(items);
    }
}
