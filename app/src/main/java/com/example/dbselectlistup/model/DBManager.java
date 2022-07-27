//데이터 처리하는 클래스
package com.example.dbselectlistup.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dbselectlistup.R;
import com.example.dbselectlistup.view.area.AreaItem;
import com.example.dbselectlistup.view.engine.EngineItem;
import com.example.dbselectlistup.view.vehiclemodel.VehicleModelItem;
import com.example.dbselectlistup.view.modelyear.ModelYearItem;
import com.example.dbselectlistup.view.upgrademode.UpgradeListItem;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DBManager extends AppCompatActivity {

    // Table Name
    private static String Table1 = "table1";
    private static String Table2 = "table2";
    private static String Table3 = "table3";
    private static String Table4 = "table4";
    private static String Table5 = "table5";

    private static String DB_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MINZY/DB_PATH";
    private static File[] DB_NAME = {};
    ArrayList<String> DB_NAMELIST;

    SQLiteDatabase db;

    ArrayList<VehicleModelItem> modelList;
    ArrayList<EngineItem> engineList;
    ArrayList<ModelYearItem> modelYearList;
    ArrayList<UpgradeListItem> upgradeList;

    VehicleModelItem vehicleModelItem;
    EngineItem engineItem;
    ModelYearItem modelYearItem;
    UpgradeListItem upgradeListItem;

    String modelYearElement;
    String[] editModelYear;
    ArrayList<String> setModelYearList;
    ArrayList<String> newModelYearList;
    ArrayList<EngineItem> setEngineList;

    String engineElement;
    String[] editEngine;

    String num;

    // Shared Preference 에서 데이터 받아올 변수
    public int getPosition = 0;
    public String getSelectedArea;
    public String getSelectedModel;
    public String getSelectedYear;
    public String getSelectedEngine;
    public String selectedString;
    public ArrayList<String> historyList;
    public String selectedEvent;
    public String eventNumber;

    public DBManager() {
        vehicleModelItem = new VehicleModelItem();
        modelList = new ArrayList();
        modelYearItem = new ModelYearItem();
        modelYearList = new ArrayList<>();
        setModelYearList = new ArrayList<>();
        newModelYearList = new ArrayList<>();
        engineItem = new EngineItem();
        engineList = new ArrayList();
        setEngineList = new ArrayList<>();
        upgradeListItem = new UpgradeListItem();
        upgradeList = new ArrayList<>();
        historyList = new ArrayList<>();

        // DB_PATH 경로에 속한 파일 이름 모두 받아오기
        File Dir = new File(DB_PATH);
        DB_NAME = Dir.listFiles();
        DB_NAMELIST = new ArrayList<>();
        for (int i = 0; i < DB_NAME.length; i++) {
            DB_NAMELIST.add(areaTypeConverter(DB_NAME[i]) + ".sqlite");
        }
    }

    public void open() throws SQLException{
        db = SQLiteDatabase.openDatabase(DB_PATH + File.separator + DB_NAMELIST.get(getPosition), null, SQLiteDatabase.OPEN_READONLY);
    }

    public void close() throws SQLException{
        db.close();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    // 경로에 파일이 있는지 테스트 확인
    public void checkDatabase() {
        File file = new File(DB_PATH + File.separator + DB_NAMELIST);
        Log.e("Check Database", "" + file.exists());
    }

    // 경로에 있는 DB 리스트 한번에 받아오기
    public ArrayList<AreaItem> getFileList() {
        File Dir = new File(DB_PATH);
        File fileNameList[] = Dir.listFiles();
        ArrayList<AreaItem> fileList = new ArrayList<>();
        for (int i = 0; i < fileNameList.length; i++) {
            fileList.add(new AreaItem(String.valueOf(areaTypeConverter(fileNameList[i]))));
        }
        return fileList;
    }

    // 차종선택 리스트 반환
    public ArrayList<VehicleModelItem> getModelList() {
        try {
            String sql = "SELECT Model FROM " + Table1 + " GROUP BY Model ORDER BY rowid ASC";
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    vehicleModelItem.setModel(cursor.getString(0));
                    modelList.add(new VehicleModelItem(vehicleModelItem.getModel()));
                }
            }
            ModelComparator modelComparator = new ModelComparator();
            Collections.sort(modelList, modelComparator);
            return modelList;
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
    }

    //연식선택 리스트 반환
    public ArrayList<String> getModelYearList() {
        try {
            String sql = "SELECT modelYear FROM " + Table1 + " WHERE model = '" + getSelectedModel + "'"
                    + " GROUP BY ModelYear ORDER BY rowid ASC";
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    modelYearItem.setModelYear(cursor.getString(0));
                    modelYearElement = modelYearItem.getModelYear();
                    editModelYear = modelYearElement.split(";");

                    for (int i = 0; i < editModelYear.length; i++) {
                        modelYearList.add(new ModelYearItem(editModelYear[i])); //ex) 2019;2020 -> 2019 / 2020으로 분할 처리
                        setModelYearList.add(modelYearTypeConverter(modelYearList.get(i))); // 분할 후 새로운 리스트에 담기
                    }

                    // 중복된 수 제거하고 새로운 리스트에 담음
                    for (int i = 0; i < setModelYearList.size(); i++) {
                        if (!newModelYearList.contains(setModelYearList.get(i))) {
                            newModelYearList.add(setModelYearList.get(i));
                        }
                    }
                    modelYearList.clear();
                    for (int i = 0; i < modelYearList.size(); i++) {
                        modelYearList.add(new ModelYearItem(newModelYearList.get(i)));
                    }
                    ModelYearComparator modelYearComparator = new ModelYearComparator();
                    Collections.sort(newModelYearList, modelYearComparator);
                }
            }
            return newModelYearList;
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
    }

    //엔진 타입 리스트 반환
    public ArrayList<EngineItem> getEngineList() {
        try {
            String sql = "SELECT Engine FROM " + Table1 + " WHERE model = '" + getSelectedModel + "' and modelYear like '%" + getSelectedYear + "%'"
                    + " GROUP BY Engine ORDER BY rowid ASC";
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    engineItem.setEngine(cursor.getString(0));
                    engineElement = engineItem.getEngine();
                    editEngine = engineElement.split(" / ");
                    for (int i = 0; i < editEngine.length; i++) {
                        engineList.add(new EngineItem(editEngine[i]));
                    }
                    EngineComparator engineComparator = new EngineComparator();
                    Collections.sort(engineList, engineComparator);
                }
            }
            for (int i = 0; i < engineList.size(); i++) {
                if (!setEngineList.contains(engineList.get(i))) { // 중복 제거
                    setEngineList.add(engineList.get(i));
                }
            }
            return setEngineList;
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
    }

    // num 컬럼 반환
    public String getNum() {
        String sql = "SELECT num FROM " + Table1 + " where model = '" + getSelectedModel + "'" +
                " AND Engine = '" + getSelectedEngine + "'" + " AND ModelYear like '%" + getSelectedYear + "%'";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                num = cursor.getString(0);
            }
        }
        return num;
    }

    public ArrayList<UpgradeListItem> getRelatedList() {
        String sql = "SELECT EGroupDesc FROM " + Table2 + " INNER JOIN " + Table1 + " ON " +
                Table3 + " = " + Table4 + " WHERE " + Table3 + " = " + "'" + getNum() + "'";
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                upgradeListItem.setEventtxt(cursor.getString(0));
                upgradeList.add(new UpgradeListItem(false, R.drawable.connector, upgradeListItem.getEventtxt(), "Service Action", "New", R.drawable.tsb));
            }
        }
        return upgradeList;
    }

    // 메이커 / 지역 선택 (Position)값 받아서 All Event List값 반환
    public ArrayList<UpgradeListItem> getAllEventList() {
        String sql = "SELECT " + Table5 + " FROM " + Table2 + " ORDER BY rowid";
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                upgradeListItem.setEventtxt(cursor.getString(0));
                upgradeList.add(new UpgradeListItem(false, R.drawable.connector, upgradeListItem.getEventtxt(), "Service Action", "New", R.drawable.tsb));
            }
        }
        return upgradeList;
    }


    // 타입 변환
    public String areaTypeConverter(File list) {
        String dbContext;

        Object obj = list;
        Gson gson = new Gson();
        dbContext = gson.toJson(obj);

        dbContext = dbContext.replace("/storage/emulated/0/MINZY/DB_PATH/", "");
        dbContext = dbContext.replace(".sqlite", "");
        dbContext = dbContext.replace("\"", "");
        dbContext = dbContext.replace(":", "");
        dbContext = dbContext.replace("fileName", "");
        dbContext = dbContext.replace("{", "");
        dbContext = dbContext.replace("}", "");
        dbContext = dbContext.replace(",", "\n");
        dbContext = dbContext.replace("[", "");
        dbContext = dbContext.replace("]", "");
        dbContext = dbContext.replace("path", "");

        return dbContext;
    }

    public String modelYearTypeConverter(ModelYearItem list) {
        String dbContext;

        Object obj = list;
        Gson gson = new Gson();
        dbContext = gson.toJson(obj);

        dbContext = dbContext.replace("\"", "");
        dbContext = dbContext.replace("{", "");
        dbContext = dbContext.replace("}", "");
        dbContext = dbContext.replace(":", "");
        dbContext = dbContext.replace("modelYear", "");
        dbContext = dbContext.replace("[", "");
        dbContext = dbContext.replace("]", "");
        dbContext = dbContext.replace(",", "\n");
        dbContext = dbContext.replace(" ", "");

        return dbContext;
    }

    public String binFileConverter(String list) {
        String dbContext;

        Object obj = list;
        Gson gson = new Gson();
        dbContext = gson.toJson(obj);

        return dbContext;
    }

    // 리스트 정렬
    class ModelYearComparator implements Comparator<String> {
        @Override
        public int compare(String item1, String item2) {
            return item1.compareToIgnoreCase(item2);
        }
    }
    class EngineComparator implements Comparator<EngineItem> {
        @Override
        public int compare(EngineItem item1, EngineItem item2) {
            return item1.getEngine().compareToIgnoreCase(item2.getEngine());
        }
    }
    class ModelComparator implements Comparator<VehicleModelItem> {
        @Override
        public int compare(VehicleModelItem item1, VehicleModelItem item2) {
            return item1.getModel().compareToIgnoreCase(item2.getModel());
        }
    }

    // SharedPreference에 저장된 모든 값 불러오기
    public void getSharedPreferenceValue(Context context){
        SharedPreferences preferences = context.getSharedPreferences("selectionList", Context.MODE_PRIVATE);
        getPosition = preferences.getInt("areaPosition", 0);
        getSelectedArea = preferences.getString("area", "");
        getSelectedModel = preferences.getString("model", "");
        getSelectedYear = preferences.getString("modelYear", "");
        getSelectedEngine = preferences.getString("engine", "");
        selectedString = preferences.getString("eventList", "");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        historyList = gson.fromJson(selectedString, type);
        selectedEvent = preferences.getString("selectedEvent", "");
        eventNumber = preferences.getString("eventNumber", "");

        open();
    }
}
