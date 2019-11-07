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
        UP, DOWN, LEFT, RIGHT
    }

    public Cell player, exit, star1, star2, star3;

    private static final int COLS = 22, ROWS = 20;
    private static final float WALL_THICKNESS = 4;
    private  float cellSize, hMargin, vMargin;
    private Paint wallPaint, playerPaint, star1Paint, star2Paint, star3Paint;
    private Cell[][] cells = new Cell[ROWS][COLS];

    Canvas canvas;

    private int stage;
    private int numOfStar;

    private boolean drawStar1 = true;
    private boolean drawStar2 = true;
    private boolean drawStar3 = true;

    private int[][] stage1 = {
            {0,  8,  82,  82, 82,  82,  82,  86,  84,  82,  82, 82,  82,  82,  82, 82,  82, 82,  82,  82, 86, 4},
            {6, 46,  84,  82,  8,  82, 862,  46,  42,  86,  84, 82,  82,  86,  84, 82,  82, 82,  82,  86, 46, 4},
            {6, 46,  46,  84, 62, 842,  86,  42,  82,   6,  42, 86,  84,   6,  46, 84,  86, 84,  86,  46, 46, 4},
            {6, 46, 462,  42, 82,  86,  46,  84,  86,  46,  84, 62,  46, 462,  42, 62,  46, 46,  46,  46, 46, 4},
            {6, 42,  82,  86, 84,  62,  46,  46,  42,  62,  42, 86,  42,  86,  84, 82,  62, 46,  42,   2, 62, 4},
            {6, 84,  86,  42,  6,  84,  62,  42,  82,  82,  86, 42,  86,  42,  62, 84,  82, 62, 842,  82, 86, 4},
            {6, 46,  42,  86, 46,  42,  86, 842,   8, 862,  42, 86,  46,  84,  86, 46, 842, 82,  86,  84,  6, 4},
            {6, 42,  86,  46, 42,  86,  42,  86,  42,  82,  86, 46,  46,  46,  46, 42,  82, 86,  46,  46, 46, 4},
            {6, 84,  62,  42, 86,  46, 846,  42,  82,  86,  46, 46,  46,  46,  42, 82,  86, 46,   4,  62, 46, 4},
            {6, 42,  86,  84, 62,  46,  42,  82,  86,  46,  42, 62,  46,  42,   8, 86,  46, 46,  42,  86, 46, 4},
            {6, 84,  62,  42, 82,  62,  84,  82,  62,  42,  82, 86,  42,  86,  46, 46,  46, 42,  82,  62, 46, 4},
            {6, 42,   8,  82, 82,  86,   4,  82,  86,  84,  86,  4, 862,  42,   6, 46,  42, 82,  82, 862, 46, 4},
            {6, 84,  62,  84, 86,  46, 462,  84,  62,  46,  42,  6,  84,  86,  46, 46,  84, 82,  82,  82, 62, 4},
            {6, 46,  84,  62, 46,  46, 846,  42,  82,  62, 846, 46,  46,  42,   6, 46,  42, 82,  82,  82, 86, 4},
            {6, 46,  42,  86, 46,  42,  62,  84,  82,  86,  46, 46,  42,  86, 462, 42,   8, 86,  84,  86, 46, 4},
            {6, 46, 846,  46,  4,  82,  82,   2, 862,  42,  62, 42,  86,  42,   8, 86, 462, 42,  62,  46, 46, 4},
            {6, 46,   4,  62, 42,  86,  84,   8,  82,  86,  84, 82,  62,  84,  62, 46,  84, 86,  84,  62, 46, 4},
            {6, 46,  46, 842, 82,  62,  46,  46,  84,  62,  42, 86, 846,  46, 846, 42,  62, 46,  46, 842, 62, 4},
            {6, 46,  42,  82, 82,  82,  62,  46,  46,  84,  86, 46,  46,  46,  42, 82,  86, 46, 462,  84, 86, 4},
            {6, 42,  82,  82, 82,  82,  82,  62,  42,  62,  42, 62,  42,   2,  82, 82,  62, 42,  82,  62, 42, 0}
    };
    private int[][] stage2 = {
            {8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 86},
            {4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  6},
            {4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  6},
            {4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  6},
            {4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  6},
            {4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  6},
            {4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  6},
            {4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  6},
            {4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  6},
            {4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  6},
            {4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  6},
            {4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  6},
            {4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  6},
            {4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  6},
            {4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  6},
            {4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  6},
            {4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  6},
            {4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  6},
            {4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  6},
            {42, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2}
    };

    public Board(Context context, final int stage) {
        super(context);

        wallPaint = new Paint();
        wallPaint.setColor(Color.BLACK);
        wallPaint.setStrokeWidth(WALL_THICKNESS);

        playerPaint = new Paint();
        playerPaint.setColor(Color.RED);

        this.stage = stage;

        Thread thread = new Thread(){
            @Override
            public void run() {
                switch (stage){
                    case 1:
                        makeMaze(stage1);
                        setStars(stage);
                        break;
                    case 2:
                        makeMaze(stage2);
                        setStars(stage);
                        break;
                }
                invalidate();
            }
        };
        thread.start();

    }

    private void makeMaze(final int[][] stage){

        for(int i=0;i<ROWS;i++){
            for(int k=0;k<COLS;k++){
                if(stage[i][k]==0) cells[i][k] = new Cell(false, false, false, false);

                else if(stage[i][k]==8) cells[i][k] = new Cell(true, false, false, false);
                else if(stage[i][k]==4) cells[i][k] = new Cell(false, true, false, false);
                else if(stage[i][k]==6) cells[i][k] = new Cell(false, false, true, false);
                else if(stage[i][k]==2) cells[i][k] = new Cell(false, false, false, true);

                else if(stage[i][k]==84) cells[i][k] = new Cell(true, true, false, false);
                else if(stage[i][k]==86) cells[i][k] = new Cell(true, false, true, false);
                else if(stage[i][k]==82) cells[i][k] = new Cell(true, false, false, true);
                else if(stage[i][k]==46) cells[i][k] = new Cell(false, true, true, false);
                else if(stage[i][k]==42) cells[i][k] = new Cell(false, true, false, true);
                else if(stage[i][k]==62) cells[i][k] = new Cell(false, false, true, true);

                else if(stage[i][k]==846) cells[i][k] = new Cell(true, true, true, false);
                else if(stage[i][k]==842) cells[i][k] = new Cell(true, true, false, true);
                else if(stage[i][k]==862) cells[i][k] = new Cell(true, false, true, true);
                else if(stage[i][k]==462) cells[i][k] = new Cell(false, true, true, true);

                else if(stage[i][k]==8462) cells[i][k] = new Cell(true, true, true, true);

                cells[i][k].setRow(i);
                cells[i][k].setColumn(k);

            }
        }

        player = cells[0][0];
        exit = cells[ROWS-1][COLS-1];

    }

    private void setStars(int stage){
        switch (stage){
            case 1:
                star1 = cells[13][10];
                star2 = cells[2][5];
                star3 = cells[17][19];
                break;
        }
    }

    public void movePlayer(Direction direction){
        switch (direction){
            case UP:
                if(!player.getTopWall() && player.getRow()>0 && player.getColumn()!=(COLS-1)) {
                    player = cells[player.getRow()-1][player.getColumn()];
                }
                break;
            case DOWN:
                if(!player.getBottomWall() && player.getRow()<(ROWS-1) && player.getColumn()!=0) {
                    player = cells[player.getRow()+1][player.getColumn()];
                }
                break;
            case LEFT:
                if(!player.getLeftWall() && player.getColumn()>1) {
                    player = cells[player.getRow()][player.getColumn()-1];
                }
                break;
            case RIGHT:
                if(!player.getRightWall() && player.getColumn()<(COLS-1)) {
                    player = cells[player.getRow()][player.getColumn()+1];
                }
                break;
        }

        //checkExit();
        checkStar();
        invalidate();

    }

//    private void checkExit(){
//        if(player == exit){
//            //다이얼로그
//        }
//    }

    private void checkStar(){
        if(player == star1 && drawStar1){
            drawStar1 = false;
            numOfStar++;
        }else if(player == star2 && drawStar2){
            drawStar2 = false;
            numOfStar++;
        }else if(player == star3 && drawStar3){
            drawStar3 = false;
            numOfStar++;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        this.canvas = canvas;
        canvas.drawColor(Color.WHITE);

        cellSize = 100f;
        hMargin = cellSize/2;
        vMargin = cellSize/2;

        canvas.translate(hMargin, vMargin);

        for(int x = 0; x<ROWS; x++){
            for(int y = 0; y<COLS; y++){

                if(cells[x][y] == null) return;

                if(cells[x][y].getTopWall()){
                    canvas.drawLine(
                            y*cellSize,
                            x*cellSize,
                            (y+1)*cellSize,
                            x*cellSize,
                            wallPaint );
                }
                if(cells[x][y].getLeftWall()){
                    canvas.drawLine(
                            y*cellSize,
                            x*cellSize,
                            y*cellSize,
                            (x+1)*cellSize,
                            wallPaint );
                }
                if(cells[x][y].getRightWall()){
                    canvas.drawLine(
                            (y+1)*cellSize,
                            x*cellSize,
                            (y+1)*cellSize,
                            (x+1)*cellSize,
                            wallPaint );
                }
                if(cells[x][y].getBottomWall()){
                    canvas.drawLine(
                            y*cellSize,
                            (x+1)*cellSize,
                            (y+1)*cellSize,
                            (x+1)*cellSize,
                            wallPaint );
                }

            }
        }

        canvas.drawCircle(
                (player.getColumn()+0.5f)*cellSize,
                (player.getRow()+0.5f)*cellSize,
                cellSize/2,
                playerPaint);

        if(drawStar1) drawStar(star1);
        if(drawStar2) drawStar(star2);
        if(drawStar3) drawStar(star3);
    }

    private void drawStar(Cell star){
        float mid = cellSize / 2;
        float min = cellSize;
        float fat = min / 17;
        float half = min / 2;
        float rad = half - fat;
        mid = mid - half;

        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.starColor));
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);

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
        setMeasuredDimension(MeasureSpec.makeMeasureSpec(100*(COLS+1), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(100*(ROWS+1), MeasureSpec.EXACTLY));
    }

    public int getNumOfStar() {
        return numOfStar;
    }

    public void setNumOfStar(int numOfStar) {
        this.numOfStar = numOfStar;
    }
}
