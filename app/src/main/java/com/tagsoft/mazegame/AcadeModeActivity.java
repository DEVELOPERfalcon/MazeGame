package com.tagsoft.mazegame;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.Toast;

public class AcadeModeActivity extends AppCompatActivity {

    ScrollView verticalScroll;
    HorizontalScrollView horizontalScroll;
    GameView gameView;
    JoystickView joystick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acade_mode);

        verticalScroll = findViewById(R.id.vertical_scroll);
        horizontalScroll = findViewById(R.id.horizontal_scroll);
        gameView = findViewById(R.id.gameview);
        joystick = findViewById(R.id.joystick);
        joystick.setBackgroundColor(Color.GRAY);
        joystick.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                if (strength != 0){
                    if(angle<=45 || angle>315){
                        gameView.movePlayer(GameView.Direction.RIGHT);
                    }else if(angle<=135 && angle>45){
                        gameView.movePlayer(GameView.Direction.UP);
                    }else if(angle<=225 && angle>135){
                        gameView.movePlayer(GameView.Direction.LEFT);
                    }else if(angle<=315 && angle>225){
                        gameView.movePlayer(GameView.Direction.DOWN);
                    }
                }

                verticalScroll.scrollTo((gameView.player.col-5)*100, (gameView.player.row-5)*100);
                horizontalScroll.scrollTo((gameView.player.col-5)*100, (gameView.player.row-5)*100);

                return;
            }
        });
    }

}
