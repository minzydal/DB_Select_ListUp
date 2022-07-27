package com.example.dbselectlistup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.example.dbselectlistup.databinding.ActivityMainBinding;
import com.example.dbselectlistup.view.area.AreaFragment;
import com.example.dbselectlistup.view.engine.EngineFragment;
import com.example.dbselectlistup.view.vehiclemodel.VehicleModelFragment;
import com.example.dbselectlistup.view.modelyear.ModelYearFragment;
import com.example.dbselectlistup.view.upgrademode.UpgradeListFragment;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    //fragment setting
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    //fragment 선언
    AreaFragment areaFrag;
    VehicleModelFragment modelFrag;
    ModelYearFragment modelYearFrag;
    EngineFragment engineFrag;
    UpgradeListFragment upgradeFrag;

    public static Context context_main;

    String beforeText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context_main = this;

        // 외부 저장소 권한 설정
        // 권한ID 가져오기
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        // 권한 열려있는지 확인
        if (permission == PackageManager.PERMISSION_DENIED || permission2 == PackageManager.PERMISSION_DENIED) {
            // 마쉬멜로우 이상버전부터 권한 묻기
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // 권한 체크(READ_PHONE_STATE의 requestCode를 1000으로 세팅
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);
            }
            return;
        }

        // fragment 선언
        areaFrag = new AreaFragment();
        modelFrag = new VehicleModelFragment();
        modelYearFrag = new ModelYearFragment();
        engineFrag = new EngineFragment();
        upgradeFrag = new UpgradeListFragment();
        setFrag(FragName.AREA); // Default fragment

        deactivateConfirmButton();

        // 지역 선택 버튼
        binding.area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.area.setText("지역");
                binding.model.setText("차종");
                binding.modelYear.setText("연식");
                binding.engine.setText("엔진 타입");
                deactivateConfirmButton();

                setButtonNotClicked();
                setButtonAreaClicked();
                setFrag(FragName.AREA);
            }
        });

        // 차종 선택 버튼
        binding.model.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.model.setText("차종");
                binding.modelYear.setText("연식");
                binding.engine.setText("엔진 타입");
                deactivateConfirmButton();

                setButtonNotClicked();
                setButtonModelClicked();
                setFrag(FragName.MODEL);
            }
        });

        // 연식 선택 버튼
        binding.modelYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.modelYear.setText("연식");
                binding.engine.setText("엔진 타입");
                deactivateConfirmButton();

                setButtonNotClicked();
                setButtonModelYearClicked();
                setFrag(FragName.MODELYEAR);
            }
        });

        // 엔진 타입 선택 버튼
        binding.engine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.engine.setText("엔진 타입");

                setButtonNotClicked();
                setButtonEngineClicked();
                setFrag(FragName.ENGINE);
            }
        });
        binding.backBtn.setVisibility(View.GONE); // 뒤로가기 버튼 홈화면에서 숨기기

        // 엔진 버튼 텍스트 변경 감지 시 (엔진까지 선택 완료했을 시에) 확인 버튼 활성화
        binding.engine.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) { // 입력하기 전에
                beforeText = charSequence.toString(); // 변경되기 전 문자열
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) { // 입력 란에 변화가 있을 시
                if (!charSequence.toString().equals(beforeText)) {
                    binding.confirmButton.setClickable(true);
                    binding.confirmButton.setBackgroundColor(Color.parseColor("#2F385E"));
                    binding.confirmButton.setTextColor(Color.WHITE);
                    binding.confirmButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            setFrag(FragName.UPGRADE);
                        }
                    });
                }
            }
            @Override
            public void afterTextChanged(Editable editable) { // 입력이 끝났을 때 조치
            }
        });
    }

    // 권한 체크 이후로직
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grandResults) {
        // READ_PHONE_STATE의 권한 체크 결과를 불러온다
        super.onRequestPermissionsResult(requestCode, permissions, grandResults);
        if (requestCode == 1000) {
            boolean check_result = true;
            // 모든 퍼미션을 허용했는지 체크
            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            } // 권한 체크에 동의를 하지 않으면 안드로이드 종료
            if (check_result == true) {
            } else {
                finish();
            }
        }
    }

    // Feedback
    // 프레그먼트 교체 -> Enum 이용해서 어떤 프래그먼트로 교체되는지 알 수 있게 하기
    enum FragName{
        AREA, MODEL, MODELYEAR, ENGINE, UPGRADE
    }

    public void setFrag(FragName fragName) {

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        switch (fragName) {
            case AREA:
                fragmentTransaction.remove(upgradeFrag);
                fragmentTransaction.replace(R.id.fragmentLayout, areaFrag);
                fragmentTransaction.commit();
                break;

            case MODEL:
                fragmentTransaction.replace(R.id.fragmentLayout, modelFrag);
                fragmentTransaction.commit();
                break;

            case MODELYEAR:
                fragmentTransaction.replace(R.id.fragmentLayout, modelYearFrag);
                fragmentTransaction.commit();
                break;

            case ENGINE:
                fragmentTransaction.replace(R.id.fragmentLayout, engineFrag);
                fragmentTransaction.commit();
                break;

            case UPGRADE:
                fragmentTransaction.replace(R.id.fragmentLayout2, upgradeFrag);
                binding.area.setVisibility(View.GONE);
                binding.model.setVisibility(View.GONE);
                binding.modelYear.setVisibility(View.GONE);
                binding.engine.setVisibility(View.GONE);
                binding.confirmButton.setVisibility(View.GONE);
                binding.searchEditText.setVisibility(View.GONE);
                binding.topLayout.setVisibility(View.GONE);
                binding.selectModelBtn.setVisibility(View.GONE);
                binding.searchBtn.setVisibility(View.GONE);
                binding.scanBtn.setVisibility(View.GONE);
                fragmentTransaction.commit();
                break;
        }
    }

    // 확인 버튼 비활성화
    public void deactivateConfirmButton() {
        binding.confirmButton.setClickable(false);
        binding.confirmButton.setBackgroundColor(Color.parseColor("#C5C5C5"));
        binding.confirmButton.setTextColor(Color.WHITE);
    }

    // 버튼 선택 시 설정 (현재 프래그먼트가 무엇인지 알려줌)
    public void setButtonAreaClicked() {
        binding.area.setBackgroundColor(Color.parseColor("#2F385E"));
        binding.area.setTextColor(Color.WHITE);
    }

    public void setButtonModelClicked() {
        binding.model.setBackgroundColor(Color.parseColor("#2F385E"));
        binding.model.setTextColor(Color.WHITE);
    }

    public void setButtonModelYearClicked() {
        binding.modelYear.setBackgroundColor(Color.parseColor("#2F385E"));
        binding.modelYear.setTextColor(Color.WHITE);
    }

    public void setButtonEngineClicked() {
        binding.engine.setBackgroundColor(Color.parseColor("#2F385E"));
        binding.engine.setTextColor(Color.WHITE);
    }

    // 지역 버튼 미선택 시 설정
    public void setButtonNotClicked() {
        binding.area.setBackgroundColor(Color.parseColor("#DEDEDE"));
        binding.area.setTextColor(Color.parseColor("#898888"));

        binding.model.setBackgroundColor(Color.parseColor("#DEDEDE"));
        binding.model.setTextColor(Color.parseColor("#898888"));

        binding.modelYear.setBackgroundColor(Color.parseColor("#DEDEDE"));
        binding.modelYear.setTextColor(Color.parseColor("#898888"));

        binding.engine.setBackgroundColor(Color.parseColor("#DEDEDE"));
        binding.engine.setTextColor(Color.parseColor("#898888"));
    }

    // 버튼 text. 리스트 선택 시 변경됨
    public void setAreaText(String text) { binding.area.setText(text); }
    public void setModelText(String text) {
        binding.model.setText(text);
    }
    public void setModelYearText(String text) {
        binding.modelYear.setText(text);
    }
    public void setEngineText(String text) {
        binding.engine.setText(text);
    }

    // 선택한 차종 리스트 보여주는 textview
    public void setSelectText(String text) {
        binding.selectText.setText(text);
    }

    public void showBackButton() {
        binding.backBtn.setVisibility(View.VISIBLE);
    }

    // BackButton Event
    public void onBackPressedInUpgradeScreen(){
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFrag(FragName.UPGRADE);
            }
        });
    }

    // 뒤로 가기 버튼 수행 시
    public void onBackPressed() {
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.backBtn.setVisibility(View.GONE);
                setSelectText("차종선택");
                setFrag(FragName.AREA);
                setButtonNotClicked();
                setButtonAreaClicked();
                setAreaText("지역");
                setModelText("차종");
                setModelYearText("연식");
                setEngineText("엔진 타입");
                fragmentTransaction.attach(areaFrag);
                binding.area.setVisibility(View.VISIBLE);
                binding.model.setVisibility(View.VISIBLE);
                binding.modelYear.setVisibility(View.VISIBLE);
                binding.engine.setVisibility(View.VISIBLE);
                binding.confirmButton.setVisibility(View.VISIBLE);
                binding.searchEditText.setVisibility(View.VISIBLE);
                binding.topLayout.setVisibility(View.VISIBLE);
                binding.selectModelBtn.setVisibility(View.VISIBLE);
                binding.searchBtn.setVisibility(View.VISIBLE);
                binding.scanBtn.setVisibility(View.VISIBLE);
                deactivateConfirmButton();
            }
        });
    }

    public void setAreaOnlyClicked() {
        binding.model.setClickable(false);
        binding.modelYear.setClickable(false);
        binding.engine.setClickable(false);
    }

    public void setModelOnlyClicked() {
        binding.modelYear.setClickable(false);
    }

    public void setYearOnlyClicked() {
        binding.model.setClickable(true);
    }

    public void setEngineOnlyClicked() {
        binding.modelYear.setClickable(true);
    }

    //화면 회전 시 앱 초기화 방지
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}