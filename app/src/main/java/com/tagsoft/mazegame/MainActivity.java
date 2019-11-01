package com.tagsoft.mazegame;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageView title;
    Button acadeModeButton;
    Button freeModeButton;
    Button optionButton;
    Button finishButton;

    int acadeModeStage;

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

        //파일 입출력으로 스테이지 정보 가져오기기

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
            Intent intent = new Intent(MainActivity.this, FreeModeActivity.class);
            startActivity(intent);
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
