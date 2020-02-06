package com.tagsoft.mazegame;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

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
                if(status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI){
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
            Intent intent = new Intent(MainActivity.this, ArcadeModeActivity.class);
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
                if(!networkConnect) networkConnectState = getString(R.string.mainactivity_network_disconnected);
                else networkConnectState = getString(R.string.mainactivity_network_connected);
                if(!nicknameExist) nicknameExistState = getString(R.string.mainactivity_nickname_not_saved);
                else nicknameExistState = getString(R.string.mainactivity_nickname_saved);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(getString(R.string.mainactivity_message1) +
                        getString(R.string.mainactivity_message2)+networkConnectState+")\n" +
                        getString(R.string.mainactivity_message3)+nicknameExistState+")\n\n" +
                        getString(R.string.mainactivity_message4));
                builder.setNeutralButton(getString(R.string.dialog_positive_button), null);
                builder.create().show();
            }

        }
    };

    View.OnClickListener optionButtonListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, OptionActivity.class);
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
