package com.tagsoft.mazegame;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class StageActivity extends AppCompatActivity {

    Intent intent;

    ScrollView verticalScroll;
    HorizontalScrollView horizontalScroll;
    Board board;
    JoystickView joystick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage);

        intent = getIntent();
        int stage = intent.getIntExtra("stage", 1);

        getSupportActionBar().setTitle("stage "+stage);

        verticalScroll = findViewById(R.id.vertical_scroll);
        verticalScroll.setSmoothScrollingEnabled(true);
        horizontalScroll = findViewById(R.id.horizontal_scroll);
        horizontalScroll.setSmoothScrollingEnabled(true);
        board = new Board(this, stage);
        horizontalScroll.addView(board);
        joystick = findViewById(R.id.joystick);
        joystick.setBackgroundColor(R.drawable.joystick_background);
        joystick.setOnMoveListener(moveListener);
    }

    JoystickView.OnMoveListener moveListener = new JoystickView.OnMoveListener() {
        @Override
        public void onMove(int angle, int strength) {
            if (strength > 80) {
                if (angle <= 45 || angle > 315) {
                    board.movePlayer(Board.Direction.RIGHT);
                } else if (angle <= 135 && angle > 45) {
                    board.movePlayer(Board.Direction.UP);
                } else if (angle <= 225 && angle > 135) {
                    board.movePlayer(Board.Direction.LEFT);
                } else if (angle <= 315 && angle > 225) {
                    board.movePlayer(Board.Direction.DOWN);
                }
            }else if(strength>0 && strength<=80){
                if (angle <= 45 || angle > 315) {
                    board.movePlayer(Board.Direction.RIGHT);
                } else if (angle <= 135 && angle > 45) {
                    board.movePlayer(Board.Direction.UP);
                } else if (angle <= 225 && angle > 135) {
                    board.movePlayer(Board.Direction.LEFT);
                } else if (angle <= 315 && angle > 225) {
                    board.movePlayer(Board.Direction.DOWN);
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            verticalScroll.scrollTo((board.player.getColumn() - 5) * 100, (board.player.getRow() - 5) * 100);
            horizontalScroll.scrollTo((board.player.getColumn() - 5) * 100, (board.player.getRow() - 5) * 100);

            return;

        }
    };

}
