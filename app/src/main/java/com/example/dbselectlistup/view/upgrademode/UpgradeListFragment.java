package com.example.dbselectlistup.view.upgrademode;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.dbselectlistup.MainActivity;
import com.example.dbselectlistup.R;
import com.example.dbselectlistup.databinding.UpgrademodeBinding;
import com.example.dbselectlistup.model.DBManager;
import com.example.dbselectlistup.view.upgrademode.all.AllEventListFragment;
import com.example.dbselectlistup.view.upgrademode.history.UpgradeHistoryFragment;
import com.example.dbselectlistup.view.upgrademode.related.RelatedEventListFragment;
import com.google.gson.Gson;

import java.util.ArrayList;

public class UpgradeListFragment extends Fragment {

    UpgrademodeBinding binding;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    AllEventListFragment allEventListFragment;
    UpgradeHistoryFragment upgradeHistoryFragment;
    RelatedEventListFragment relatedEventListFragment;
    UpgradeProcess upgradeProcess;

    String selectedString;

    ArrayList<String> eventList = new ArrayList();
    String selectedEvent;

    DBManager dbManager = new DBManager();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = UpgrademodeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        dbManager.getSharedPreferenceValue(getContext());
        selectedString = dbManager.getSelectedArea + " / " + dbManager.getSelectedModel + " / " + dbManager.getSelectedYear + " / " + dbManager.getSelectedEngine;
        ((MainActivity) MainActivity.context_main).setSelectText(selectedString);

        SharedPreferences preferences = getContext().getSharedPreferences("selectionList", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        editor.putString("selectedEvent", "");  // 아무 것도 선택하지 않았을 때 초기화
        editor.commit();

        allEventListFragment = new AllEventListFragment();
        upgradeHistoryFragment = new UpgradeHistoryFragment();
        relatedEventListFragment = new RelatedEventListFragment();
        upgradeProcess = new UpgradeProcess();

        setFrag(FragName.RELATED);

        // 탭 버튼 클릭 시 배경 설정
        binding.relatedEventList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFrag(FragName.RELATED);
                binding.allEventList.setTextColor(Color.BLACK);
                binding.upgradeHistory.setTextColor(Color.BLACK);
                binding.relatedEventList.setTextColor(Color.parseColor("#A40303"));

                binding.relatedEventList.setBackgroundResource(R.drawable.button_line);
                binding.allEventList.setBackgroundResource(R.drawable.notselectedline);
                binding.upgradeHistory.setBackgroundResource(R.drawable.notselectedline);
            }
        });
        binding.allEventList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFrag(FragName.ALL);
                binding.relatedEventList.setTextColor(Color.BLACK);
                binding.upgradeHistory.setTextColor(Color.BLACK);
                binding.allEventList.setTextColor(Color.parseColor("#A40303"));
                binding.relatedEventList.setBackgroundResource(R.drawable.notselectedline);
                binding.allEventList.setBackgroundResource(R.drawable.button_line);
                binding.upgradeHistory.setBackgroundResource(R.drawable.notselectedline);
            }
        });
        binding.upgradeHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFrag(FragName.HISTORY);
                binding.relatedEventList.setTextColor(Color.BLACK);
                binding.allEventList.setTextColor(Color.BLACK);
                binding.upgradeHistory.setTextColor(Color.parseColor("#A40303"));
                binding.relatedEventList.setBackgroundResource(R.drawable.notselectedline);
                binding.allEventList.setBackgroundResource(R.drawable.notselectedline);
                binding.upgradeHistory.setBackgroundResource(R.drawable.button_line);
            }
        });


        Bundle bundle = new Bundle();
        binding.automode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putString("mode", "AUTO MODE");
                upgradeProcess.setArguments(bundle);

                DBManager dbManager = new DBManager();
                dbManager.getSharedPreferenceValue(getContext());

                selectedEvent = dbManager.selectedEvent;
                if (selectedEvent != "") {
                    eventList.add(selectedEvent); // 선택한 event history에 쌓임
                    setFrag(FragName.UPGRADE_SCREEN);
                }else{
                    Toast.makeText(getContext(), "Please select the event list", Toast.LENGTH_SHORT).show();
                }

                String eventJson = gson.toJson(eventList);
                editor.putString("eventList", eventJson);
                editor.commit();
            }
        });

        binding.manualmode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bundle.putString("mode", "MANUAL MODE");
                upgradeProcess.setArguments(bundle);

                DBManager dbManager = new DBManager();
                dbManager.getSharedPreferenceValue(getContext());
                selectedEvent = dbManager.selectedEvent;
                if (selectedEvent != "") {
                    eventList.add(selectedEvent);
                    setFrag(FragName.UPGRADE_SCREEN);
                }else{
                    Toast.makeText(getContext(), "Please select the event list", Toast.LENGTH_SHORT).show();
                }

                String eventJson = gson.toJson(eventList);
                editor.putString("eventList", eventJson);
                editor.commit();
            }
        });
        ((MainActivity) MainActivity.context_main).showBackButton();
        ((MainActivity) MainActivity.context_main).onBackPressed();

        return root;
    }

    // Feedback
    // 프레그먼트 교체 -> Enum 이용해서 어떤 프래그먼트로 교체되는지 알 수 있게 하기
    enum FragName{
        RELATED, ALL, HISTORY, UPGRADE_SCREEN;
    }

    // 프레그먼트 교체
    public void setFrag(FragName fragName) {
        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        switch (fragName) {
            case RELATED:
                fragmentTransaction.replace(R.id.eventListFrag, relatedEventListFragment);
                fragmentTransaction.commit();
                break;

            case ALL:
                fragmentTransaction.replace(R.id.eventListFrag, allEventListFragment);
                fragmentTransaction.commit();
                break;

            case HISTORY:
                fragmentTransaction.replace(R.id.eventListFrag, upgradeHistoryFragment);
                fragmentTransaction.commit();
                break;

            case UPGRADE_SCREEN:
                fragmentTransaction.replace(R.id.fragmentLayout2, upgradeProcess);
                fragmentTransaction.commit();
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
