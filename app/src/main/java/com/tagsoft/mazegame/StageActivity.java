package com.tagsoft.mazegame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Chronometer;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

public class StageActivity extends AppCompatActivity {

    Intent intent;

    ScrollView verticalScroll;
    HorizontalScrollView horizontalScroll;
    Chronometer timer;
    Board board;
    JoystickView joystick;

    boolean startTimer = false;
    boolean finish = false;

    long startTime;
    long endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage);

        intent = getIntent();
        int stage = intent.getIntExtra("stage", 1);

        getSupportActionBar().setTitle("stage "+stage);

        timer = findViewById(R.id.chronometer);
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
            if(finish) return;
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

            if(startTimer==false && board.player.getColumn()==1 && board.player.getRow()==0){
                timerOn();
            }

            verticalScroll.scrollTo((board.player.getColumn() - 5) * 100, (board.player.getRow() - 5) * 100);
            horizontalScroll.scrollTo((board.player.getColumn() - 5) * 100, (board.player.getRow() - 5) * 100);

            checkExit();

            return;
        }
    };

    public void timerOn(){
       startTimer = true;
       timer.setBase(SystemClock.elapsedRealtime());
       timer.start();
       startTime = SystemClock.elapsedRealtime();
    }

    private void checkExit(){
        if(board.player == board.exit){
            finish = true;
            timer.stop();
//            endTime = SystemClock.elapsedRealtime();
//            long time = endTime - startTime;
            String time = timer.getText().toString();
            //Toast.makeText(this, "시간:"+time, Toast.LENGTH_SHORT).show();
            //다이얼로그
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(time+"\n"+"별 갯수: "+board.getNumOfStar());
            builder.setPositiveButton("확인", dialogOnClickListener);
            AlertDialog dialog = builder.create();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
    }

    DialogInterface.OnClickListener dialogOnClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {

        }
    };

}
