package com.tagsoft.mazegame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class Board extends View {

    public enum Direction{
        UP, DOWN, LEFT, RIGHT
    }

    public Cell player, exit;

    private static final int COLS = 20, ROWS = 20;
    private static final float WALL_THICKNESS = 4;
    private  float cellSize, hMargin, vMargin;
    private Paint wallPaint, playerPaint, exitPaint;
    private Cell[][] cells = new Cell[COLS][ROWS];

    int stage;

    private int[][] stage1 = {
            {8, 82, 82, 82, 82, 82, 86, 84, 82, 82, 82, 82, 82, 82, 82, 82, 82, 82, 82, 86},
            {46, 84, 82, 8, 82, 862, 46, 42, 86, 84, 82, 82, 86, 84, 82, 82, 82, 82, 86, 46},
            {46, 46, 84, 62, 842, 86, 42, 82, 6, 42, 86, 84, 6, 46, 84, 86, 84, 86, 46, 46},
            {46, 462, 42, 82, 86, 46, 84, 86, 46, 84, 62, 46, 462, 42, 62, 46, 46, 46, 46, 46},
            {42, 82, 86, 84, 62, 46, 46, 42, 62, 42, 86, 42, 86, 84, 82, 62, 46, 42, 2, 62},
            {84, 86, 42, 6, 84, 62, 42, 82, 82, 86, 42, 86, 42, 62, 84, 82, 62, 842, 82, 86},
            {46, 42, 86, 46, 42, 86, 842, 8, 862, 42, 86, 46, 84, 86, 46, 842, 82, 86, 84, 6},
            {42, 86, 46, 42, 86, 42, 86, 42, 82, 86, 46, 46, 46, 46, 42, 82, 86, 46, 46, 46},
            {84, 62, 42, 86, 46, 846, 42, 82, 86, 46, 46, 46, 46, 42, 82, 86, 46, 4, 62, 46},
            {42, 86, 84, 62, 46, 42, 82, 86, 46, 42, 62, 46, 42, 8, 86, 46, 46, 42, 86, 46},
            {84, 62, 42, 82, 62, 84, 82, 62, 42, 82, 86, 42, 86, 46, 46, 46, 42, 82, 62, 46},
            {42, 8, 82, 82, 86, 4, 82, 86, 84, 86, 4, 862, 42, 6, 46, 42, 82, 82, 862, 46},
            {84, 62, 84, 86, 46, 462, 84, 62, 46, 42, 6, 84, 86, 46, 46, 84, 82, 82, 82, 62},
            {46, 84, 62, 46, 46, 846, 42, 82, 62, 846, 46, 46, 42, 6, 46, 42, 82, 82, 82, 86},
            {46, 42, 86, 46, 42, 62, 84, 82, 86, 46, 46, 42, 86, 462, 42, 8, 86, 84, 86, 46},
            {46, 846, 46, 4, 82, 82, 2, 862, 42, 62, 42, 86, 42, 8, 86, 462, 42, 62, 46, 46},
            {46, 4, 62, 42, 86, 84, 8, 82, 86, 84, 82, 62, 84, 62, 46, 84, 86, 84, 62, 46},
            {46, 46, 842, 82, 62, 46, 46, 84, 62, 42, 86, 846, 46, 846, 42, 62, 46, 46, 842, 62},
            {46, 42, 82, 82, 82, 62, 46, 46, 84, 86, 46, 46, 46, 42, 82, 86, 46, 462, 84, 86},
            {42, 82, 82, 82, 82, 82, 62, 42, 62, 42, 62, 42, 2, 82, 82, 62, 42, 82, 62, 42}
    };

    public Board(Context context, final int stage) {
        super(context);

        wallPaint = new Paint();
        wallPaint.setColor(Color.BLACK);
        wallPaint.setStrokeWidth(WALL_THICKNESS);

        playerPaint = new Paint();
        playerPaint.setColor(Color.RED);

        exitPaint = new Paint();
        exitPaint.setColor(Color.BLUE);

        this.stage = stage;

        Thread thread = new Thread(){
            @Override
            public void run() {
                switch (stage){
                    case 1:
                        makeMaze(stage1);
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
        exit = cells[COLS-1][ROWS-1];

    }

    public void movePlayer(Direction direction){
        switch (direction){
            case UP:
                if(!player.getTopWall()) {
                    player = cells[player.getColumn()][player.getRow()-1];
                }
                break;
            case DOWN:
                if(!player.getBottomWall()) {
                    player = cells[player.getColumn()][player.getRow()+1];
                }
                break;
            case LEFT:
                if(!player.getLeftWall()) {
                    player = cells[player.getColumn()-1][player.getRow()];
                }
                break;
            case RIGHT:
                if(!player.getRightWall()) {
                    player = cells[player.getColumn()+1][player.getRow()];
                }
                break;
        }

        checkExit();
        invalidate();

    }

    private void checkExit(){
        if(player == exit){
            //다이얼로그
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);

        cellSize = 100f;
        hMargin = cellSize/2;
        vMargin = cellSize/2;

        canvas.translate(hMargin, vMargin);

        for(int x = 0; x<ROWS; x++){
            for(int y = 0; y<COLS; y++){

                //if(cells[x][y] == null) return;

                if(cells[x][y].getTopWall()){
                    canvas.drawLine(
                            x*cellSize,
                            y*cellSize,
                            (x+1)*cellSize,
                            y*cellSize,
                            wallPaint );
                }
                if(cells[x][y].getLeftWall()){
                    canvas.drawLine(
                            x*cellSize,
                            y*cellSize,
                            x*cellSize,
                            (y+1)*cellSize,
                            wallPaint );
                }
                if(cells[x][y].getRightWall()){
                    canvas.drawLine(
                            (x+1)*cellSize,
                            y*cellSize,
                            (x+1)*cellSize,
                            (y+1)*cellSize,
                            wallPaint );
                }
                if(cells[x][y].getBottomWall()){
                    canvas.drawLine(
                            x*cellSize,
                            (y+1)*cellSize,
                            (x+1)*cellSize,
                            (y+1)*cellSize,
                            wallPaint );
                }

            }
        }

        canvas.drawCircle(
                (player.getColumn()+0.5f)*cellSize,
                (player.getRow()+0.5f)*cellSize,
                cellSize/2,
                playerPaint);

        canvas.drawCircle(
                (exit.getColumn()+0.5f)*cellSize,
                (exit.getRow()+0.5f)*cellSize,
                cellSize/2,
                exitPaint);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.makeMeasureSpec(100*(COLS+1), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(100*(ROWS+1), MeasureSpec.EXACTLY));
    }

}
