package com.tagsoft.mazegame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class Board extends View {

    public enum Direction{
        UP, DOWN, LEFT, RIGHT   //가독성을 위해 숫자 대신 사용
    }

    public Cell player, exit, star1, star2, star3;  //플레이어 위치, 출구 위치, 미로에 흩어진 별들의 위치 정보를 가질 객체들

    private Stages stages;      //미로의 모양 정보를 가짐
    private Stars stars;        //별의 위치 정보를 가짐

    public static final int COLS = 22, ROWS = 20;  //미로 만들 칸 갯수 -> 가로:22, 세로:20
    public static final int COLS2 = 62;         //stage51 전용 column 갯수
    private  final float WALL_THICKNESS = 4;  //벽 두깨
    private  float cellSize, hMargin, vMargin;      //한칸의 크기, 미로의 좌우 여유공간, 미로의 상하 여유공간
    private Paint wallPaint, playerPaint, star1Paint, star2Paint, star3Paint;   //Board에 그릴 것들
    private Cell[][] cells = new Cell[ROWS][COLS];      //미로의 모양 정보를 가질 배열
    private Cell[][] cells2 = new Cell[ROWS][COLS2];    //stage51 전용 배열

    Canvas canvas;  //미로를 그릴 도화지 역할

    private int stage;  //만들 스테이지 번호
    private int numOfStar;  //미로에서 찾아낸 별의 갯수

    private boolean drawStar1 = true;   //별1을 그릴지 여부
    private boolean drawStar2 = true;   //별2을 그릴지 여부
    private boolean drawStar3 = true;   //별3을 그릴지 여부

    public Board(Context context, final int stage) {    //생성자
        super(context);

        wallPaint = new Paint();                //그려낼 벽 객체 생성
        wallPaint.setColor(Color.BLACK);        //벽 색깔
        wallPaint.setStrokeWidth(WALL_THICKNESS);   //벽 두깨 설정

        playerPaint = new Paint();              //플레이어가 움직일 케릭터 객체 생성
        playerPaint.setColor(Color.RED);        //플레이어가 움직일 캐릭터 색깔

        this.stage = stage;                     //그려낼 스테이지 번호 받기
        stages = new Stages(context, stage);    //그려낼 미로 모양 정보 가진 객체 생성
        stars = new Stars(stage);               //그려낼 별들의 위치 정보를 가진 객체 생성

        Thread thread = new Thread(){
            @Override
            public void run() {
                while (!stages.isConstructorFinish()){}     //Main Thread가 stages 생성자 작업 끝낼 때까지 대기
                if(stage == 51) makeMaze2(stages.stage2);   //stage51 전용 미로 설계
                else makeMaze(stages.stage);                 //미로 설계
                setStars(stars);                //별들의 위치 지정
                invalidate();                   //강제로 onDraw() 시키기(화면 갱신)
            }
        };
        thread.start();

    }

    private void makeMaze2(final int[][] stage){        //stage51 전용 미로 설계
        if(this.stage == 51){
            for(int i=0;i<ROWS;i++){
                for(int k=0;k<COLS2;k++){
                    if(stage[i][k]==0) cells2[i][k] = new Cell(false, false, false, false);      //상하좌우에 벽이 없을면

                    else if(stage[i][k]==8) cells2[i][k] = new Cell(true, false, false, false);  //위에만 벽이면
                    else if(stage[i][k]==4) cells2[i][k] = new Cell(false, true, false, false);  //왼쪽만 벽이면
                    else if(stage[i][k]==6) cells2[i][k] = new Cell(false, false, true, false);  //오른쪽만 벽이면
                    else if(stage[i][k]==2) cells2[i][k] = new Cell(false, false, false, true);  //아래만 벽이면

                    else if(stage[i][k]==84) cells2[i][k] = new Cell(true, true, false, false);  //위, 왼쪽만 벽이면
                    else if(stage[i][k]==86) cells2[i][k] = new Cell(true, false, true, false);  //위, 오른쪽만 벽이면
                    else if(stage[i][k]==82) cells2[i][k] = new Cell(true, false, false, true);  //위, 아래만 벽이면
                    else if(stage[i][k]==46) cells2[i][k] = new Cell(false, true, true, false);  //왼쪽, 오른쪽만 벽이면
                    else if(stage[i][k]==42) cells2[i][k] = new Cell(false, true, false, true);  //왼쪽, 아래만 벽이면
                    else if(stage[i][k]==62) cells2[i][k] = new Cell(false, false, true, true);  //오른쪽, 아래만 벽이면

                    else if(stage[i][k]==846) cells2[i][k] = new Cell(true, true, true, false);  //위, 왼쪽, 오른쪽만 벽이면
                    else if(stage[i][k]==842) cells2[i][k] = new Cell(true, true, false, true);  //위, 왼쪽, 아래만 벽이면
                    else if(stage[i][k]==862) cells2[i][k] = new Cell(true, false, true, true);  //위, 오른쪽, 아래만 벽이면
                    else if(stage[i][k]==462) cells2[i][k] = new Cell(false, true, true, true);  //왼쪽, 오른쪽, 아래만 벽이면

                    else if(stage[i][k]==8462) cells2[i][k] = new Cell(true, true, true, true);  //상하좌우 모두 벽이면

                    cells2[i][k].setRow(i);      //해당 칸의 행 위치 정보 설정
                    cells2[i][k].setColumn(k);   //해당 칸의 열 위치 정보 설정
                }
            }

            player = cells2[0][0];           //플레이어 캐릭터 위치 설정
            exit = cells2[ROWS-1][COLS2-1];   //출구 위치 설정

        }
    }
    public void movePlayer2(Direction direction){        //stage51 전용 캐릭터 이동
        switch (direction){
            case UP:
                if(!player.getTopWall() && player.getRow()>0 && player.getColumn()!=(COLS2-1)) {     //플레이어 위쪽에 벽이 없고, 미로를 벗어나지 않으면
                    player = cells2[player.getRow()-1][player.getColumn()];      //캐릭터 위치 위로 1칸 변경
                }
                break;
            case DOWN:
                if(!player.getBottomWall() && player.getRow()<(ROWS-1) && player.getColumn()!=0) {     //플레이어 아래쪽에 벽이 없고, 미로를 벗어나지 않으면
                    player = cells2[player.getRow()+1][player.getColumn()];      //캐릭터 위치 아래로 1칸 변경
                }
                break;
            case LEFT:
                if(!player.getLeftWall() && player.getColumn()>1) {     //플레이어 왼쪽에 벽이 없고, 미로를 벗어나지 않으면
                    player = cells2[player.getRow()][player.getColumn()-1];      //캐릭터 위치 왼쪽으로 1칸 변경
                }
                break;
            case RIGHT:
                if(!player.getRightWall() && player.getColumn()<(COLS2-1)) {     //플레이어 오른쪽에 벽이 없고, 미로를 벗어나지 않으면
                    player = cells2[player.getRow()][player.getColumn()+1];      //캐릭터 위치 오른쪽으로 1칸 변경
                }
                break;
        }

        checkStar();        //별 찾았는지 확인
        invalidate();       //화면 갱신
    }

    private void makeMaze(final int[][] stage){     //미로 설계

        for(int i=0;i<ROWS;i++){
            for(int k=0;k<COLS;k++){
                if(stage[i][k]==0) cells[i][k] = new Cell(false, false, false, false);      //상하좌우에 벽이 없을면

                else if(stage[i][k]==8) cells[i][k] = new Cell(true, false, false, false);  //위에만 벽이면
                else if(stage[i][k]==4) cells[i][k] = new Cell(false, true, false, false);  //왼쪽만 벽이면
                else if(stage[i][k]==6) cells[i][k] = new Cell(false, false, true, false);  //오른쪽만 벽이면
                else if(stage[i][k]==2) cells[i][k] = new Cell(false, false, false, true);  //아래만 벽이면

                else if(stage[i][k]==84) cells[i][k] = new Cell(true, true, false, false);  //위, 왼쪽만 벽이면
                else if(stage[i][k]==86) cells[i][k] = new Cell(true, false, true, false);  //위, 오른쪽만 벽이면
                else if(stage[i][k]==82) cells[i][k] = new Cell(true, false, false, true);  //위, 아래만 벽이면
                else if(stage[i][k]==46) cells[i][k] = new Cell(false, true, true, false);  //왼쪽, 오른쪽만 벽이면
                else if(stage[i][k]==42) cells[i][k] = new Cell(false, true, false, true);  //왼쪽, 아래만 벽이면
                else if(stage[i][k]==62) cells[i][k] = new Cell(false, false, true, true);  //오른쪽, 아래만 벽이면

                else if(stage[i][k]==846) cells[i][k] = new Cell(true, true, true, false);  //위, 왼쪽, 오른쪽만 벽이면
                else if(stage[i][k]==842) cells[i][k] = new Cell(true, true, false, true);  //위, 왼쪽, 아래만 벽이면
                else if(stage[i][k]==862) cells[i][k] = new Cell(true, false, true, true);  //위, 오른쪽, 아래만 벽이면
                else if(stage[i][k]==462) cells[i][k] = new Cell(false, true, true, true);  //왼쪽, 오른쪽, 아래만 벽이면

                else if(stage[i][k]==8462) cells[i][k] = new Cell(true, true, true, true);  //상하좌우 모두 벽이면

                cells[i][k].setRow(i);      //해당 칸의 행 위치 정보 설정
                cells[i][k].setColumn(k);   //해당 칸의 열 위치 정보 설정

            }
        }

        player = cells[0][0];           //플레이어 캐릭터 위치 설정
        exit = cells[ROWS-1][COLS-1];   //출구 위치 설정

    }

    private void setStars(Stars stars){                 //별들의 위치 지정
        if(stage == 51){
            star1 = cells2[stars.star1Row][stars.star1Col];  //별1의 위치 지정
            star2 = cells2[stars.star2Row][stars.star2Col];  //별2의 위치 지정
            star3 = cells2[stars.star3Row][stars.star3Col];  //별3의 위치 지정
        }else{
            star1 = cells[stars.star1Row][stars.star1Col];  //별1의 위치 지정
            star2 = cells[stars.star2Row][stars.star2Col];  //별2의 위치 지정
            star3 = cells[stars.star3Row][stars.star3Col];  //별3의 위치 지정
        }
    }

    public void movePlayer(Direction direction){        //캐릭터 움직이기
        switch (direction){
            case UP:
                if(!player.getTopWall() && player.getRow()>0 && player.getColumn()!=(COLS-1)) {     //플레이어 위쪽에 벽이 없고, 미로를 벗어나지 않으면
                    player = cells[player.getRow()-1][player.getColumn()];      //캐릭터 위치 위로 1칸 변경
                }
                break;
            case DOWN:
                if(!player.getBottomWall() && player.getRow()<(ROWS-1) && player.getColumn()!=0) {     //플레이어 아래쪽에 벽이 없고, 미로를 벗어나지 않으면
                    player = cells[player.getRow()+1][player.getColumn()];      //캐릭터 위치 아래로 1칸 변경
                }
                break;
            case LEFT:
                if(!player.getLeftWall() && player.getColumn()>1) {     //플레이어 왼쪽에 벽이 없고, 미로를 벗어나지 않으면
                    player = cells[player.getRow()][player.getColumn()-1];      //캐릭터 위치 왼쪽으로 1칸 변경
                }
                break;
            case RIGHT:
                if(!player.getRightWall() && player.getColumn()<(COLS-1)) {     //플레이어 오른쪽에 벽이 없고, 미로를 벗어나지 않으면
                    player = cells[player.getRow()][player.getColumn()+1];      //캐릭터 위치 오른쪽으로 1칸 변경
                }
                break;
        }

        checkStar();        //별 찾았는지 확인
        invalidate();       //화면 갱신

    }

    private void checkStar(){   //별 찾았는지 확인
        if(player == star1 && drawStar1){   //별1을 찾았으면
            drawStar1 = false;               //별1 그릴지 여부를 false로
            numOfStar++;                     //찾은 별 갯수 증가
        }else if(player == star2 && drawStar2){   //별2을 찾았으면
            drawStar2 = false;               //별2 그릴지 여부를 false로
            numOfStar++;                     //찾은 별 갯수 증가
        }else if(player == star3 && drawStar3){   //별3을 찾았으면
            drawStar3 = false;               //별3 그릴지 여부를 false로
            numOfStar++;                     //찾은 별 갯수 증가
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {      //Board 그리기
        this.canvas = canvas;                   //도화지 준비
        canvas.drawColor(Color.WHITE);          //도화지 기본 배경색

        cellSize = 100f;            //한칸의 크기
        hMargin = cellSize/2;       //미로의 좌우 여유공간
        vMargin = cellSize/2;       //미로의 상하 여유공간

        canvas.translate(hMargin, vMargin);     //여유공간만큼 띄워서 원점 설정

        if(stage==51){                      //stage51이면
            for(int x = 0; x<ROWS; x++){
                for(int y = 0; y<COLS2; y++){

                    if(cells2[x][y] == null) return;     //미로정보를 제대로 못 가져왔으면 그리지 않음

                    if(cells2[x][y].getTopWall()){         //해당 칸에 위쪽에 벽이 있어야 하면
                        canvas.drawLine(                  //선 그리기
                                y*cellSize,         //그릴 선의 시작 X좌표
                                x*cellSize,         //그릴 선의 시작 Y좌표
                                (y+1)*cellSize,     //그릴 선의 종료 X좌표
                                x*cellSize,         //그릴 선의 종료 Y좌표
                                wallPaint );              //그려낼 객체(벽)
                    }
                    if(cells2[x][y].getLeftWall()){        //해당 칸에 왼쪽에 벽이 있어야 하면
                        canvas.drawLine(                   //선 그리기
                                y*cellSize,         //그릴 선의 시작 X좌표
                                x*cellSize,         //그릴 선의 시작 Y좌표
                                y*cellSize,         //그릴 선의 종료 X좌표
                                (x+1)*cellSize,     //그릴 선의 종료 Y좌표
                                wallPaint );              //그려낼 객체(벽)
                    }
                    if(cells2[x][y].getRightWall()){       //해당 칸에 오른쪽에 벽이 있어야 하면
                        canvas.drawLine(                   //선 그리기
                                (y+1)*cellSize,     //그릴 선의 시작 X좌표
                                x*cellSize,         //그릴 선의 시작 Y좌표
                                (y+1)*cellSize,     //그릴 선의 종료 X좌표
                                (x+1)*cellSize,     //그릴 선의 종료 Y좌표
                                wallPaint );              //그려낼 객체(벽)
                    }
                    if(cells2[x][y].getBottomWall()){       //해당 칸에 아래쪽에 벽이 있어야 하면
                        canvas.drawLine(                   //선 그리기
                                y*cellSize,         //그릴 선의 시작 X좌표
                                (x+1)*cellSize,     //그릴 선의 시작 Y좌표
                                (y+1)*cellSize,     //그릴 선의 종료 X좌표
                                (x+1)*cellSize,     //그릴 선의 종료 Y좌표
                                wallPaint );              //그려낼 객체(벽)
                    }

                }
            }
        }else{                              //stage51이 아니면
            for(int x = 0; x<ROWS; x++){
                for(int y = 0; y<COLS; y++){

                    if(cells[x][y] == null) return;     //미로정보를 제대로 못 가져왔으면 그리지 않음

                    if(cells[x][y].getTopWall()){         //해당 칸에 위쪽에 벽이 있어야 하면
                        canvas.drawLine(                  //선 그리기
                                y*cellSize,         //그릴 선의 시작 X좌표
                                x*cellSize,         //그릴 선의 시작 Y좌표
                                (y+1)*cellSize,     //그릴 선의 종료 X좌표
                                x*cellSize,         //그릴 선의 종료 Y좌표
                                wallPaint );              //그려낼 객체(벽)
                    }
                    if(cells[x][y].getLeftWall()){        //해당 칸에 왼쪽에 벽이 있어야 하면
                        canvas.drawLine(                   //선 그리기
                                y*cellSize,         //그릴 선의 시작 X좌표
                                x*cellSize,         //그릴 선의 시작 Y좌표
                                y*cellSize,         //그릴 선의 종료 X좌표
                                (x+1)*cellSize,     //그릴 선의 종료 Y좌표
                                wallPaint );              //그려낼 객체(벽)
                    }
                    if(cells[x][y].getRightWall()){       //해당 칸에 오른쪽에 벽이 있어야 하면
                        canvas.drawLine(                   //선 그리기
                                (y+1)*cellSize,     //그릴 선의 시작 X좌표
                                x*cellSize,         //그릴 선의 시작 Y좌표
                                (y+1)*cellSize,     //그릴 선의 종료 X좌표
                                (x+1)*cellSize,     //그릴 선의 종료 Y좌표
                                wallPaint );              //그려낼 객체(벽)
                    }
                    if(cells[x][y].getBottomWall()){       //해당 칸에 아래쪽에 벽이 있어야 하면
                        canvas.drawLine(                   //선 그리기
                                y*cellSize,         //그릴 선의 시작 X좌표
                                (x+1)*cellSize,     //그릴 선의 시작 Y좌표
                                (y+1)*cellSize,     //그릴 선의 종료 X좌표
                                (x+1)*cellSize,     //그릴 선의 종료 Y좌표
                                wallPaint );              //그려낼 객체(벽)
                    }

                }
            }
        }



        canvas.drawCircle(                              //원 그리기
                (player.getColumn()+0.5f)*cellSize, //원의 중심의 X좌표
                (player.getRow()+0.5f)*cellSize,    //원의 중심의 Y좌표
                cellSize/2,                      //반지름
                playerPaint);                           //그려낼 객체(케릭터)

        if(drawStar1) drawStar(star1);                  //별1 그리기
        if(drawStar2) drawStar(star2);                  //별2 그리기
        if(drawStar3) drawStar(star3);                  //별3 그리기
    }

    private void drawStar(Cell star){                  //별 그리기
        float mid = cellSize / 2;
        float min = cellSize;
        float fat = min / 17;
        float half = min / 2;
        float rad = half - fat;
        mid = mid - half;

        Paint paint = new Paint();                  //그려낼 객체
        paint.setColor(getResources().getColor(R.color.starColor)); //색깔 설정
        paint.setAntiAlias(true);               //주변 배경과 잘 어울리도록 하기
        paint.setStyle(Paint.Style.STROKE);     //가장자리만 그리도록 설정

        Path path = new Path();

        if(star == star1) star1Paint = paint;
        if(star == star2) star2Paint = paint;
        if(star == star3) star3Paint = paint;

        paint.setStrokeWidth(fat);
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawCircle((star.getColumn()*cellSize)+mid + half, (star.getRow()*cellSize)+half, rad, paint);

        path.reset();

        paint.setStyle(Paint.Style.FILL);


        // top left
        path.moveTo((star.getColumn()*cellSize)+mid + half * 0.5f, (star.getRow()*cellSize)+half * 0.84f);
        // top right
        path.lineTo((star.getColumn()*cellSize)+mid + half * 1.5f, (star.getRow()*cellSize)+half * 0.84f);
        // bottom left
        path.lineTo((star.getColumn()*cellSize)+mid + half * 0.68f, (star.getRow()*cellSize)+half * 1.45f);
        // top tip
        path.lineTo((star.getColumn()*cellSize)+mid + half * 1.0f, (star.getRow()*cellSize)+half * 0.5f);
        // bottom right
        path.lineTo((star.getColumn()*cellSize)+mid + half * 1.32f, (star.getRow()*cellSize)+half * 1.45f);
        // top left
        path.lineTo((star.getColumn()*cellSize)+mid + half * 0.5f, (star.getRow()*cellSize)+half * 0.84f);

        path.close();
        canvas.drawPath(path, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(stage == 51){
            setMeasuredDimension(MeasureSpec.makeMeasureSpec(100*(COLS2+1), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(100*(ROWS+1), MeasureSpec.EXACTLY));
        }else{
            setMeasuredDimension(MeasureSpec.makeMeasureSpec(100*(COLS+1), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(100*(ROWS+1), MeasureSpec.EXACTLY));
        }

    }

    public int getNumOfStar() {
        return numOfStar;
    }       //별 갯수 얻기

    public void setNumOfStar(int numOfStar) {
        this.numOfStar = numOfStar;
    }   //별 갯수 설정
}
