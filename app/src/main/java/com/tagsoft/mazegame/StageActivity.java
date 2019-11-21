package com.tagsoft.mazegame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import ua.org.tenletters.widget.DiagonalScrollView;

public class StageActivity extends AppCompatActivity {

    Intent intent;
    int numOfStar;
    String time;

    DiagonalScrollView diagonalScrollView;
    Chronometer timer;
    Board board;
    JoystickView joystick;
    RelativeLayout moveLayout;

    boolean startTimer = false;
    boolean finish = false;

    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage);

        intent = getIntent();
        int stage = intent.getIntExtra("stage", 1);

        getSupportActionBar().setTitle("stage "+stage);

        timer = findViewById(R.id.chronometer);
        diagonalScrollView = findViewById(R.id.diagonal_scroller);
        board = new Board(this, stage);
        diagonalScrollView.addView(board);
        joystick = findViewById(R.id.joystick);
        joystick.setBackgroundColor(R.drawable.joystick_background);
        joystick.setOnMoveListener(moveListener);
        moveLayout = findViewById(R.id.layout_move);

        loadJoystickLocation();

    }

    JoystickView.OnMoveListener moveListener = new JoystickView.OnMoveListener() {  //조이스틱의 움직임을 주시하는 리스너
        @Override
        public void onMove(int angle, int strength) {   //angle: 각도, strength: 강도(중심에서 움직인 거리)
            if(finish) return;  //게임이 끝나면 못 움직이게
            if (strength > 0) { //움직였으면
                if (angle <= 45 || angle > 315) {   //오른쪽으로 인식할 각도
                    board.movePlayer(Board.Direction.RIGHT);    //오른쪽으로 이동
                } else if (angle <= 135 && angle > 45) {  //위로 인식할 각도
                    board.movePlayer(Board.Direction.UP);    //위로 이동
                } else if (angle <= 225 && angle > 135) {  //왼쪽으로 인식할 각도
                    board.movePlayer(Board.Direction.LEFT);    //왼쪽으로 이동
                } else if (angle <= 315 && angle > 225) {  //아래로 인식할 각도
                    board.movePlayer(Board.Direction.DOWN);    //아래로 이동
                }
            }

            workingAfterMove();

            return;     //리스너 종료
        }
    };

    public void workingAfterMove(){ //이동 후 해야 할 작업
        if(!startTimer && board.player.getColumn()==1 && board.player.getRow()==0){   //플레이어가 처음 이동을 시작하면
            timerOn();  //타이머 시작
        }
        diagonalScrollView.scrollTo((board.player.getColumn() - 5) * 100, (board.player.getRow() - 5) * 100);   //스크롤 초점 이동
        checkExit();       //골인 지점 도착 확인
    }

    public void timerOn(){  //타이머 시작
        if(!startTimer){
            startTimer = true;   //타이머 시작 여부
            timer.setBase(SystemClock.elapsedRealtime());    //시간 초기화
            timer.start();   //카운트 업 시작
        }
    }

    private void checkExit(){       //골인 지점 도착 확인
        if(board.player == board.exit){     //플레이어 위치와 골인 위치가 같으면
            finish = true;      //끝났는지 여부
            timer.stop();       //타이머 종료
            time = timer.getText().toString();  //타이머에게 시간 얻어오기
            numOfStar = board.getNumOfStar();   //별 갯수 얻어오기

            AlertDialog.Builder builder = new AlertDialog.Builder(this);    //다이얼로그 만드는 빌더 생성

            LayoutInflater inflater = this.getLayoutInflater(); //객체화 시켜주는 인플레이터 얻어오기
            View v = inflater.inflate(R.layout.dialog_activity_stage, null);    //다이얼로그에 보여줄 레이아웃 객체화
            RatingBar star1 = v.findViewById(R.id.star1);   //레이아웃의 첫번째 별점 얻어오기
            RatingBar star2 = v.findViewById(R.id.star2);   //레이아웃의 두번째 별점 얻어오기
            RatingBar star3 = v.findViewById(R.id.star3);   //레이아웃의 세번째 별점 얻어오기
            TextView tvTime = v.findViewById(R.id.tv_clear_time);   //레이아웃의 시간표시해주는 텍스트뷰 얻어오기
            Button confirmButton = v.findViewById(R.id.btn_confirm);    //레이아웃의 확인 버튼 얻어오기
            confirmButton.setOnClickListener(listener); //레이아웃의 확인버튼 리스너 설정
            if(numOfStar==1) {  //아이템 1개 먹었을 경우
                star1.setRating(1); //첫번째 별점에 별점주기
            }else if(numOfStar==2) {  //아이템 2개 먹었을 경우
                star1.setRating(1); //첫번째 별점에 별점주기
                star2.setRating(1); //두번째 별점에 별점주기
            }else if(numOfStar==3){  //아이템 3개 먹었을 경우
                star1.setRating(1); //첫번째 별점에 별점주기
                star2.setRating(1); //두번째 별점에 별점주기
                star3.setRating(1); //세번째 별점에 별점주기
            }
            tvTime.setText("클리어 타임: "+time);    //텍스트뷰에 클리어 타임 넣기
            builder.setView(v); //빌더에 레이아웃 세팅

            dialog = builder.create();  //디이얼로그 생성
            dialog.setCancelable(false);    //뒤로가기로 다이얼로그 사라지게 못하기
            dialog.show();  //다이얼로그 보여주기
        }
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {    //다이얼로그 확인 버튼 클릭을 듣는 리스너
            intent.putExtra("numOfStar", numOfStar);    //별점 전달 부탁
            intent.putExtra("time", time);      //클리어타임 전달 부탁
            StageActivity.this.setResult(RESULT_OK, intent);    //액티비티 결과 정상이라고 전달
            finish();   //액티비티 종료
        }
    };

    public void clickUp(View view) {    //업키 클릭시 1칸 위로 이동
        board.movePlayer(Board.Direction.UP);
        workingAfterMove();
    }

    public void clickDown(View view) {    //다운키 클릭시 1칸 아래로 이동
        board.movePlayer(Board.Direction.DOWN);
        workingAfterMove();
    }

    public void clickLeft(View view) {    //레프트키 클릭시 1칸 왼쪽으로 이동
        board.movePlayer(Board.Direction.LEFT);
        workingAfterMove();
    }

    public void clickRight(View view) {    //라이트키 클릭시 1칸 오른쪽으로 이동
        board.movePlayer(Board.Direction.RIGHT);
        workingAfterMove();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(dialog != null) dialog.dismiss();
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
