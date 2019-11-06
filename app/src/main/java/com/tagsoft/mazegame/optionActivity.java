package com.tagsoft.mazegame;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class optionActivity extends AppCompatActivity {

    Button nickNameCheckButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        getSupportActionBar().setTitle("설정정");

        nickNameCheckButton = findViewById(R.id.btn_connect_http);
        nickNameCheckButton.setBackgroundColor(Color.TRANSPARENT);
   }
}
