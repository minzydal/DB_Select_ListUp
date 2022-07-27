package com.example.dbselectlistup.model;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

public class XMLParser extends AppCompatActivity {
    final static String TAG = "XML_Parsing";

    private static String XMLPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "MINZY" + File.separator + "xml";
    private static String XMLName = "";

    public String fileName = "";

    DBManager dbManager;
    Context mContext;

    public String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "nvci" + File.separator + "data";
    public long bytes;

    //생성자에서 함수 호출
    public XMLParser(Context context) {
        this.mContext = context;
        dbManager = new DBManager();
        dbManager.getSharedPreferenceValue(context);
        XMLName = dbManager.getSelectedArea + dbManager.eventNumber + ".xml";

        checkFile();
        getDataFromFile(XMLPath + File.separator + XMLName);
    }

    //경로에 파일 있는지 확인
    public boolean checkFile() {
        File file = new File(XMLPath + File.separator + XMLName);
        Log.e(TAG, "checkFile: " + file.exists());
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public String getDataFromFile(String path) {
        File xmlFile = new File(path);
        try {
            InputStream fis = new BufferedInputStream(new FileInputStream(xmlFile));
            return parse(fis);

        } catch (Exception e) {
            Log.e(TAG, "error parsing");

            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();

            Log.e(TAG, exceptionAsString);

            return null;
        }
    }

    String parse(InputStream in) throws XmlPullParserException, IOException {
        String data = "";
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            if (parser.getEventType() != XmlPullParser.END_TAG) {
                parser.nextTag();
            }
            data = readFeed(parser);
        } finally {
            in.close();
        }
        return data;
    }

    // START_DOCUMENT 문서의 시작
    // END_DOCUMENT 문서의 끝
    // START_TAG 태그의 시작
    // END_TAG 태그의 끝 ex) </data>
    //TEXT 태그의 시작과 끝 사이 ex) <data>content</data>

    // getEventType 이벤트 유형을 반환
    // getName 태그명을 가져온다
    // getText 태그의 시작과 끝 사이의 데이터를 가져온다.
    // next 다음 이벤트를 가져온다
    private String readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "root:directory");
        int eventType = parser.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String name = null;
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    // 스트림의 시작, 리스트 생성
                    break;
                case XmlPullParser.START_TAG:
                    // 태그 식별 후 태그에 맞는 작업 수행
                    name = parser.getName();
                    if (name.equals("rom")) fileName = parser.nextText();

                    break;
                case XmlPullParser.END_TAG:
                    // 태그의 마지막 읽음. Item을 처리하는 중이면 리스트에 Message를 추가
                    name = parser.getName();
                    break;
            }
            eventType = parser.next();
        }
        return fileName;
    }

    public void sendData(Context context) {
        File file = new File(filePath + File.separator + fileName);
        Log.e(TAG, "openBinFile: " + fileName);
        FileInputStream fis = null;
        int size = 0;
        bytes = file.length();

        byte[] buffer = new byte[1024];

        if (file.exists() && file.canRead()) {
            try {
                fis = new FileInputStream(file);
                while ((size = fis.read(buffer)) != -1) {
                    Log.e(TAG, "read size : " + size);
                    for (int i = 0; i < size; i++) {
                        //broadcast 여기서 보내주기
                        Intent intent = new Intent("example.test.broadcast");
                        intent.putExtra("data", buffer[i]);
                        context.sendBroadcast(intent);
                    }
                }
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
