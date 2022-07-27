package com.example.dbselectlistup.view.upgrademode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dbselectlistup.MainActivity;
import com.example.dbselectlistup.R;
import com.example.dbselectlistup.databinding.TransmittinglayoutBinding;
import com.example.dbselectlistup.model.DBManager;
import com.example.dbselectlistup.model.XMLParser;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class UpgradeProcess extends Fragment {
    TransmittinglayoutBinding binding;
    DBManager dbManager;

    String selectedEvent;
    ArrayList<String> eventList = new ArrayList<>();
    String mode;
    processThread processThread;

    int red, black;

    XMLParser xmlParser;

    boolean condition = true;

    private BroadcastReceiver mReceiver = null;
    int receivedData;

    long bytes;

    ArrayList<Integer> getDataList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = TransmittinglayoutBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        onBackPressed();
        setMode();
        setEventText();

        red = R.drawable.arrow_red;
        black = R.drawable.arrow;

        xmlParser = new XMLParser(getContext());

        registerReceiver();

        unmatchFile();
        xmlParser.sendData(getContext());

        return root;
    }


    //     데이터 수신
    public void registerReceiver() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "MINZY" + File.separator + xmlParser.fileName);

        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("example.test.broadcast");

        this.mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                receivedData = intent.getByteExtra("data", (byte) 0);

                if (intent.getAction().equals("example.test.broadcast")) {
                    processThread = new processThread();
                    processThread.start();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            while (condition) {
                                bytes = xmlParser.bytes;
                                try {
                                    for (int i = 0; i < bytes; i++) {
                                        setProgressMax((int) bytes);
                                        setProgress(i);
                                        setProgressPercent(i + "Bytes");
                                        i++;
                                    }
                                    setProgressPercent("100%");
                                    binding.upgradeProgress.setProgress((int) (bytes));
                                    setProcessColorRed();

                                    if (binding.upgradeProgress.getProgress() == binding.upgradeProgress.getMax()) {
                                        completeProcess();
                                        condition = false;
                                    }

                                    Thread.sleep(1000);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });

                    Log.e("buffer", "onReceive: " + receivedData);
                    getDataList.add(receivedData);
                }

                //Bin data 받아서 파일로 저장
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    for (int i = 0; i < getDataList.size(); i++) {
                        fos.write(getDataList.get(i));
                    }
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        getContext().registerReceiver(this.mReceiver, intentFilter);
    }


    public void setProgressMax(int max) {
        binding.upgradeProgress.setMax(max);
    }

    public void setProgress(int progress) {
        binding.upgradeProgress.setProgress(progress);
    }

    public void setProgressPercent(String text) {
        binding.progressPercent.setText(text);
    }

    private void onBackPressed() {
        unregisterReceiver();
        ((MainActivity) MainActivity.context_main).onBackPressedInUpgradeScreen();
    }

    private void unregisterReceiver() {
        if (mReceiver != null) {
            getActivity().unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    @Override
    public void onResume() {
        registerReceiver();
        super.onResume();
    }

    @Override
    public void onPause() {
        unregisterReceiver();
        super.onPause();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
    }


    //Auto Mode인지 Manual Mode인지 표시
    public void setMode() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mode = bundle.getString("mode");
        }
        binding.mode.setText(mode);
    }

    //무슨 event인지
    public void setEventText() {
        dbManager = new DBManager();
        dbManager.getSharedPreferenceValue(getContext());

        selectedEvent = dbManager.selectedEvent;
        if (selectedEvent != "") {
            eventList.add(selectedEvent); // 선택한 event history에 쌓임
        }
        binding.eventNameProcess.setText(selectedEvent);
    }


    public void setProcessColorRed() {
        binding.arrow.setImageResource(red);
        binding.arrow2.setImageResource(red);
        binding.arrow3.setImageResource(red);
        binding.arrow4.setImageResource(red);
        binding.arrow5.setImageResource(red);
        binding.arrow6.setImageResource(red);
    }

    public void setProcessColorBlack() {
        binding.arrow.setImageResource(black);
        binding.arrow2.setImageResource(black);
        binding.arrow3.setImageResource(black);
        binding.arrow4.setImageResource(black);
        binding.arrow5.setImageResource(black);
        binding.arrow6.setImageResource(black);
    }


    //업그레이드 완료 시
    public void completeProcess() {
        condition = false;
        binding.tablet.setVisibility(View.GONE);
        binding.vci.setVisibility(View.GONE);
        binding.arrow.setVisibility(View.GONE);
        binding.arrow2.setVisibility(View.GONE);
        binding.arrow3.setVisibility(View.GONE);
        binding.arrow4.setVisibility(View.GONE);
        binding.arrow5.setVisibility(View.GONE);
        binding.arrow6.setVisibility(View.GONE);
        binding.complete.setVisibility(View.VISIBLE);


        binding.progressPercent.setText("100%");
        binding.progressNotice.setText("성공");
        binding.progressText.setText("업그레이드가 완료 되었습니다.");
        binding.romId.setText("롬 아이디");
        binding.currentlyInVehicle.setText("현재 롬 아이디");
        binding.latestUpdate.setText("최신 업데이트");

        activateConfirmButton();
    }

    //OK 버튼 활성화
    public void activateConfirmButton() {
        binding.updateConfirmButton.setClickable(true);
        binding.updateConfirmButton.setBackgroundColor(Color.BLACK);
        binding.updateConfirmButton.setTextColor(Color.WHITE);
    }

    public void unmatchFile() {
        if (xmlParser.checkFile() == false) {
            binding.vci.setVisibility(View.GONE);
            binding.tablet.setVisibility(View.GONE);

            binding.arrow.setVisibility(View.GONE);
            binding.arrow2.setVisibility(View.GONE);
            binding.arrow3.setVisibility(View.GONE);
            binding.arrow4.setVisibility(View.GONE);
            binding.arrow5.setVisibility(View.GONE);
            binding.arrow6.setVisibility(View.GONE);

            binding.eventNameProcess.setText("It doesn't match any file.");
            binding.mode.setVisibility(View.GONE);
            binding.upgradeProgress.setVisibility(View.GONE);
            binding.progressText.setVisibility(View.GONE);
            binding.progressNotice.setVisibility(View.GONE);
            binding.progressPercent.setText("Please select the file again.");
            binding.romId.setVisibility(View.GONE);
            binding.currentlyInVehicle.setVisibility(View.GONE);
            binding.currentName.setVisibility(View.GONE);
            binding.latestUpdate.setVisibility(View.GONE);
            binding.latestVersion.setVisibility(View.GONE);
            binding.updateConfirmButton.setVisibility(View.GONE);

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //진행도 보여주기
    class processThread extends Thread {
        ImageView sendingProgress[] = {binding.arrow, binding.arrow2, binding.arrow3, binding.arrow4, binding.arrow5, binding.arrow6};

        @Override
        public void run() {
            while (condition) {
                try {
                    for (int i = 0; i < sendingProgress.length; i++) {
                        sendingProgress[i].setImageResource(red);
                        Thread.sleep(1000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
