package com.tagsoft.mazegame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class AcadeModeActivity extends AppCompatActivity {

    GameView gameView;
    JoystickView joystick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acade_mode);

        gameView = findViewById(R.id.gameview);
        joystick = findViewById(R.id.joystick);
        joystick.setBorderColor(R.color.joystickColor);
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
                return;
            }
        });
    }

}
