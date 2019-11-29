package com.tagsoft.mazegame;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class FreeModeActivity extends AppCompatActivity {

    GridView gridView;
    MyAdapter adapter;
    ArrayList<ClearData> datas = new ArrayList<>();
    int stage;
    int numOfStar;
    int totalStageNum = 51;
    int totalStarNum = 0;

    String query;
    String nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_mode);

        getSupportActionBar().setTitle("Stage Select");

        gridView = findViewById(R.id.gridview);

        for(int i=0;i<totalStageNum;i++){
            datas.add(new ClearData(i+1));
        }

        loadingWork();

        adapter = new MyAdapter(getLayoutInflater(), datas, totalStarNum);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                stage = position+1;
                if(stage ==51 && adapter.isStage51Lock()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(FreeModeActivity.this);
                    builder.setTitle("알림");
                    builder.setMessage("stage51은 special stage로서\nstage1부터 stage50까지\n모든 별을 모아야 플레이 가능합니다.\n\n현재 별 갯수: "+totalStarNum);
                    builder.setPositiveButton("OK", null);
                    builder.create().show();
                }else{
                    Intent intent = new Intent(FreeModeActivity.this, StageActivity.class);
                    intent.putExtra("stage", stage);
                    startActivityForResult(intent, 20);
                }
            }
        });
    }

    public void loadingWork(){
        Thread thread = new Thread(){
            @Override
            public void run() {
                loadNickName(); //내장메모리의 텍스트파일에서 닉네임 가져오기
                loadStageStarInfomation();
                loadStageTimeInfomation();
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    public void loadNickName(){
        try {
            File file = new File(getFilesDir(), "NickName.txt");
            if(file.exists()){
                FileInputStream fis = openFileInput("NickName.txt");
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader reader = new BufferedReader(isr);
                String line = reader.readLine();
                nickname = line;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadStageStarInfomation(){
        String serverUrl = "http://developer3.dothome.co.kr/MAZEescape/loadStar.php";
        try{
            URL url = new URL(serverUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setUseCaches(false);

            query = "nickname="+nickname;
            OutputStream os = connection.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(os);
            writer.write(query, 0, query.length());
            writer.flush();
            writer.close();

            //echo결과 받기(작업 확인용)
            InputStream is = connection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);
            final StringBuffer buffer = new StringBuffer();
            String line = reader.readLine();
            while(line != null){
                buffer.append(line+"\n");
                line = reader.readLine();
            }
            String echo = buffer.toString();
            if(echo.equals("loading fail\n")){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(FreeModeActivity.this, "loading fail", Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                String[] array1 = echo.split("\\n");
                for(int i=0; i<array1.length; i++){
                    String[] array2 = array1[i].split("=");
                    if(array2[0].equals("stage"+(i+1))){
                        int starNum = Integer.parseInt(array2[1]);
                        if(i != array1.length-1) totalStarNum += starNum;
                        datas.get(i).setStarsNumber(starNum);
                    }
                }
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadStageTimeInfomation(){
        String serverUrl = "http://developer3.dothome.co.kr/MAZEescape/loadTime.php";
        try{
            URL url = new URL(serverUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            query = "nickname="+nickname;
            OutputStream os = connection.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(os);
            writer.write(query, 0, query.length());
            writer.flush();
            writer.close();

            //echo결과 받기(작업 확인용)
            InputStream is = connection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);
            final StringBuffer buffer = new StringBuffer();
            String line = reader.readLine();
            while(line != null){
                buffer.append(line+"\n");
                line = reader.readLine();
            }
            final String echo = buffer.toString();
            if(echo.equals("loading fail\n")){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(FreeModeActivity.this, "loading fail", Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                String[] array1 = echo.split("\\n");
                for(int i=0; i<array1.length; i++){
                    String[] array2 = array1[i].split("=");
                    if(array2[0].equals("stage"+(i+1))){
                        String[] hms = array2[1].split(":"); //hms -> hour, minute, second
                        datas.get(i).setHour(hms[0]);
                        datas.get(i).setMinute(hms[1]);
                        datas.get(i).setSecond(hms[2]);
                    }
                }
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 20:
                if(resultCode == RESULT_OK){
                    //데이터 넘겨받기
                    numOfStar = data.getIntExtra("numOfStar", 0);
                    String timeRecord = data.getStringExtra("time");
                    String[] timeRecordArray = timeRecord.split(":");
                    //기록 비교
                    int hour = Integer.parseInt(datas.get(stage-1).getHour());      //기존 기록(시간)
                    int minute = Integer.parseInt(datas.get(stage-1).getMinute());      //기존 기록(분)
                    int second = Integer.parseInt(datas.get(stage-1).getSecond());      //기존 기록(초)
                    if(datas.get(stage-1).getStarsNumber() < numOfStar) {
                        new Thread(){
                            @Override
                            public void run() {
                                totalStarNum += (numOfStar - datas.get(stage-1).getStarsNumber());
                                adapter.setTotalStarNum(totalStarNum);
                                if(totalStarNum == 150) adapter.setStage51Lock(false);
                                datas.get(stage-1).setStarsNumber(numOfStar);
                                saveStarToMYSQL();      //MYSQL저장
                            }
                        }.start();
                    }
                    if(Integer.parseInt(timeRecordArray[0]) < hour){
                        datas.get(stage-1).setHour(timeRecordArray[0]);
                        datas.get(stage-1).setMinute(timeRecordArray[1]);
                        datas.get(stage-1).setSecond(timeRecordArray[2]);
                        //데이터 저장
                        new Thread(){
                            @Override
                            public void run() {
                                saveTimeToMYSQL();      //MYSQL저장
                            }
                        }.start();
                    }else if(Integer.parseInt(timeRecordArray[0]) == hour && Integer.parseInt(timeRecordArray[1]) < minute){
                        datas.get(stage-1).setHour(timeRecordArray[0]);
                        datas.get(stage-1).setMinute(timeRecordArray[1]);
                        datas.get(stage-1).setSecond(timeRecordArray[2]);
                        //데이터 저장
                        new Thread(){
                            @Override
                            public void run() {
                                saveTimeToMYSQL();      //MYSQL저장
                            }
                        }.start();
                    }else if(Integer.parseInt(timeRecordArray[0]) == hour && Integer.parseInt(timeRecordArray[1]) == minute && Integer.parseInt(timeRecordArray[2]) < second){
                        datas.get(stage-1).setHour(timeRecordArray[0]);
                        datas.get(stage-1).setMinute(timeRecordArray[1]);
                        datas.get(stage-1).setSecond(timeRecordArray[2]);
                        //데이터 저장
                        new Thread(){
                            @Override
                            public void run() {
                                saveTimeToMYSQL();      //MYSQL저장
                            }
                        }.start();
                    }
                    adapter.notifyDataSetChanged();     //화면 갱신
                }
                break;
        }
    }

    public void saveTimeToMYSQL(){
        String serverUrl = "http://developer3.dothome.co.kr/MAZEescape/saveTimeData.php";
        try{
            URL url = new URL(serverUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            String time = datas.get(stage-1).getHour()+":"+datas.get(stage-1).getMinute()+":"+datas.get(stage-1).getSecond();
            query = "nickname="+nickname+"&stage="+stage+"&time="+time;
            OutputStream os = connection.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(os);
            writer.write(query, 0, query.length());
            writer.flush();
            writer.close();

            //echo결과 받기(작업 확인용)
            InputStream is = connection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);
            final StringBuffer buffer = new StringBuffer();
            String line = reader.readLine();
            buffer.append(line);
            if( buffer.toString().equals("upload fail") ) {
                saveTimeToMYSQL();
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveStarToMYSQL(){
        String serverUrl = "http://developer3.dothome.co.kr/MAZEescape/saveStarData.php";
        try{
            URL url = new URL(serverUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            numOfStar = datas.get(stage-1).getStarsNumber();
            query = "nickname="+nickname+"&stage="+stage+"&star="+numOfStar;
            OutputStream os = connection.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(os);
            writer.write(query, 0, query.length());
            writer.flush();
            writer.close();

            //echo결과 받기(작업 확인용)
            InputStream is = connection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);
            final StringBuffer buffer = new StringBuffer();
            String line = reader.readLine();
            buffer.append(line);
            if( buffer.toString().equals("upload fail") ) {
                saveStarToMYSQL();
            }
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(FreeModeActivity.this, buffer.toString(), Toast.LENGTH_SHORT).show();
//                }
//            });
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
