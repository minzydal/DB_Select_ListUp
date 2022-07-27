package com.example.dbselectlistup.view.upgrademode.popup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.dbselectlistup.databinding.PopuplayoutBinding;
import com.example.dbselectlistup.model.DBManager;

public class FragmentDialog extends DialogFragment {

    PopuplayoutBinding binding;
    String selectedEvent;
    String mode; // Auto Mode인지 Manual Mode인지 값 받아오는 변수
    DBManager dbManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = PopuplayoutBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        clickedConfirmButton();
        setSelectedEventText();
        setTitleText();

        return root;
    }

    // 확인버튼 클릭 시 종료
    public void clickedConfirmButton() {
        binding.dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDestroyView();
            }
        });
    }

    // 선택한 event Dialog 메세지에 보여주기
    public void setSelectedEventText() {
        dbManager = new DBManager();
        dbManager.getSharedPreferenceValue(getContext());
        selectedEvent = dbManager.selectedEvent;

        binding.popuptxt.setText(selectedEvent);
    }

    // 모드 선택
    public void setTitleText() {
        Bundle bundle = getArguments();

        if (bundle != null) {
            mode = bundle.getString("mode");
        }

        if (mode != null) {
            binding.popupTitle.setText(mode);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
