package com.tagsoft.mazegame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    ImageView title;
    Button acadeModeButton;
    Button freeModeButton;
    Button optionButton;
    Button finishButton;

    boolean networkConnect;
    boolean nicknameExist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = findViewById(R.id.titleimg);
        acadeModeButton = findViewById(R.id.academodebutton);
        freeModeButton = findViewById(R.id.freemodebutton);
        optionButton = findViewById(R.id.optionbutton);
        finishButton = findViewById(R.id.finishbutton);

        acadeModeButton.setBackgroundColor(Color.TRANSPARENT);
        freeModeButton.setBackgroundColor(Color.TRANSPARENT);
        optionButton.setBackgroundColor(Color.TRANSPARENT);
        finishButton.setBackgroundColor(Color.TRANSPARENT);

        acadeModeButton.setOnClickListener(acadeModeButtonListener);
        freeModeButton.setOnClickListener(freeModeButtonListener);
        optionButton.setOnClickListener(optionButtonListener);
        finishButton.setOnClickListener(finishButtonListener);

    }

    @Override
    protected void onResume() {
        super.onResume();

        //인터넷 연결 확인
        checkNetworkConnectivity();

        //파일 입출력으로 닉네임 정보 가져오기
        loadNickName();
    }

    public void checkNetworkConnectivity(){
        new Thread(){
            @Override
            public void run() {
                int status = NetworkStatus.getConnectivityStatus(getApplicationContext());
                if(status == NetworkStatus.TYPE_MOBILE){
                    networkConnect = true;
                }else if (status == NetworkStatus.TYPE_WIFI){
                    networkConnect = true;
                }else {
                    networkConnect = false;
                }
            }
        }.start();

    }

    public void loadNickName(){
        new Thread(){
            @Override
            public void run() {
                try {
                    File file = new File(getFilesDir(), "NickName.txt");
                    if(file.exists()){
                        FileInputStream fis = openFileInput("NickName.txt");
                        InputStreamReader isr = new InputStreamReader(fis);
                        BufferedReader reader = new BufferedReader(isr);
                        String line = reader.readLine();
                        if(line.toString().getBytes().length<6) nicknameExist=false;
                        else nicknameExist = true;
                    }else nicknameExist=false;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    View.OnClickListener acadeModeButtonListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, AcadeModeActivity.class);
            startActivity(intent);
        }
    };

    View.OnClickListener freeModeButtonListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if(networkConnect && nicknameExist){
                Intent intent = new Intent(MainActivity.this, FreeModeActivity.class);
                startActivity(intent);
            }else{
                String networkConnectState;
                String nicknameExistState;
                if(!networkConnect) networkConnectState = "연결 안됨";
                else networkConnectState = "연결됨";
                if(!nicknameExist) nicknameExistState = "저장 안됨";
                else nicknameExistState = "저장됨";
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Free Mode는 클리어시간을 업로드하기 위해\n\n" +
                        "1. 인터넷 연결("+networkConnectState+")\n" +
                        "2. 옵션(설정)에서 닉네임 저장("+nicknameExistState+")\n\n" +
                        "이 필요합니다.");
                builder.setNeutralButton("확인", null);
                builder.create().show();
            }

        }
    };

    View.OnClickListener optionButtonListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, optionActivity.class);
            startActivity(intent);
        }
    };

    View.OnClickListener finishButtonListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            finish();
        }
    };

}
