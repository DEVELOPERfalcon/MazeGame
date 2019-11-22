package com.tagsoft.mazegame;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;

import ua.org.tenletters.widget.DiagonalScrollView;

public class ArcadeModeActivity extends AppCompatActivity {

    DiagonalScrollView diagonalScrollView;
    GameView gameView;
    JoystickView joystick;
    RelativeLayout moveLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arcade_mode);

        diagonalScrollView = findViewById(R.id.diagonal_scroller);
        gameView = findViewById(R.id.gameview);
        joystick = findViewById(R.id.joystick);
        joystick.setBackgroundColor(Color.GRAY);
        joystick.setOnMoveListener(moveListener);
        moveLayout = findViewById(R.id.layout_move);

        loadJoystickLocation();
    }

    JoystickView.OnMoveListener moveListener = new JoystickView.OnMoveListener() {
        @Override
        public void onMove(int angle, int strength) {
            if (strength > 0) {
                if (angle <= 45 || angle > 315) {
                    gameView.movePlayer(GameView.Direction.RIGHT);
                } else if (angle <= 135 && angle > 45) {
                    gameView.movePlayer(GameView.Direction.UP);
                } else if (angle <= 225 && angle > 135) {
                    gameView.movePlayer(GameView.Direction.LEFT);
                } else if (angle <= 315 && angle > 225) {
                    gameView.movePlayer(GameView.Direction.DOWN);
                }
            }

            workingAfterMove();

            return;

        }
    };

    public void workingAfterMove(){
        diagonalScrollView.scrollTo((gameView.player.col - 5) * 100, (gameView.player.row - 5) * 100);
    }

    public void clickUp(View view) {    //업키 클릭시 1칸 위로 이동
        gameView.movePlayer(GameView.Direction.UP);
        workingAfterMove();
    }

    public void clickDown(View view) {    //다운키 클릭시 1칸 아래로 이동
        gameView.movePlayer(GameView.Direction.DOWN);
        workingAfterMove();
    }

    public void clickLeft(View view) {    //레프트키 클릭시 1칸 왼쪽으로 이동
        gameView.movePlayer(GameView.Direction.LEFT);
        workingAfterMove();
    }

    public void clickRight(View view) {    //라이트키 클릭시 1칸 오른쪽으로 이동
        gameView.movePlayer(GameView.Direction.RIGHT);
        workingAfterMove();
    }

    public void loadJoystickLocation(){
        try{
            SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/com.tagsoft.mazegame/databases/Data.db", null, SQLiteDatabase.OPEN_READONLY);

            Cursor cursor = db.rawQuery("SELECT * FROM JoystickLocation", null);
            if(cursor == null) return;
            if(cursor.getCount() != 0){
                while (cursor.moveToNext()){
                    if(cursor.getInt(0)>0) moveLayout.setGravity(Gravity.LEFT);
                    else if(cursor.getInt(1)>0) moveLayout.setGravity(Gravity.CENTER);
                    else if(cursor.getInt(2)>0) moveLayout.setGravity(Gravity.RIGHT);
                }
            }else{
                moveLayout.setGravity(Gravity.CENTER);
            }
        }catch (Exception e){moveLayout.setGravity(Gravity.CENTER);}

    }
}
