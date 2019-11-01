package com.tagsoft.mazegame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class Board extends View {

    private static final int COLS = 10, ROWS = 10;
    private static final float WALL_THICKNESS = 4;
    private Cell[][] cells = new Cell[COLS][ROWS];
    private  float cellSize, hMargin, vMargin;
    private Paint wallPaint, playerPaint, exitPaint;

    public Board(Context context, AttributeSet attrs) {
        super(context, attrs);

        wallPaint = new Paint();
        wallPaint.setColor(Color.BLACK);
        wallPaint.setStrokeWidth(WALL_THICKNESS);

        makeMaze();

    }

    private void makeMaze(){
        for(int x=0;x<COLS;x++){
            for(int y=0;y<ROWS;y++){
                cells[x][y] = new Cell(true, true, true, true);
                cells[x][y].setColumn(x);
                cells[x][y].setRow(y);
            }
        }

        cellSize = 100f;
        hMargin = cellSize/2;
        vMargin = cellSize/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.GREEN);

        canvas.translate(hMargin, vMargin);

        for(int x = 0; x<COLS; x++){
            for(int y = 0; y<ROWS; y++){
                if(cells[x][y].getTopWall()){
                    canvas.drawLine(x*cellSize, y*cellSize, (x+1)*cellSize, y*cellSize, wallPaint );
                }
                if(cells[x][y].getLeftWall()){
                    canvas.drawLine( x*cellSize, y*cellSize, x*cellSize, (y+1)*cellSize, wallPaint );
                }
                if(cells[x][y].getRightWall()){
                    canvas.drawLine(
                            (x+1)*cellSize, y*cellSize, (x+1)*cellSize, (y+1)*cellSize, wallPaint );
                }
                if(cells[x][y].getBottomWall()){
                    canvas.drawLine(
                            x*cellSize, (y+1)*cellSize, (x+1)*cellSize, (y+1)*cellSize, wallPaint );
                }
            }

            //float margin = cellSize/10;

//            canvas.drawRect(
//                    player.getColumn()*cellSize+margin,
//                    player.getRow()*cellSize+margin,
//                    (player.getColumn()+1)*cellSize-margin,
//                    (player.getRow()+1)*cellSize-margin,
//                    playerPaint);
//
//            canvas.drawRect(
//                    exit.getColumn()*cellSize+margin,
//                    exit.getRow()*cellSize+margin,
//                    (exit.getColumn()+1)*cellSize-margin,
//                    (exit.getRow()+1)*cellSize-margin,
//                    exitPaint);

        }
    }

    //    public Board(int[][] arr){
//        for(int i=0;i<10;i++){
//            for(int k=0;k<10;k++){
//                if(arr[i][k]==0) cells[i][k] = new Cell(false, false, false, false);
//
//                else if(arr[i][k]==2) cells[i][k] = new Cell(true, false, false, false);
//                else if(arr[i][k]==4) cells[i][k] = new Cell(false, true, false, false);
//                else if(arr[i][k]==6) cells[i][k] = new Cell(false, false, true, false);
//                else if(arr[i][k]==8) cells[i][k] = new Cell(false, false, false, true);
//
//                else if(arr[i][k]==24) cells[i][k] = new Cell(true, true, false, false);
//                else if(arr[i][k]==26) cells[i][k] = new Cell(true, false, true, false);
//                else if(arr[i][k]==28) cells[i][k] = new Cell(true, false, false, true);
//                else if(arr[i][k]==46) cells[i][k] = new Cell(false, true, true, false);
//                else if(arr[i][k]==48) cells[i][k] = new Cell(false, true, false, true);
//                else if(arr[i][k]==68) cells[i][k] = new Cell(false, false, true, true);
//
//                else if(arr[i][k]==246) cells[i][k] = new Cell(true, true, true, false);
//                else if(arr[i][k]==248) cells[i][k] = new Cell(true, true, false, true);
//                else if(arr[i][k]==268) cells[i][k] = new Cell(true, false, true, true);
//                else if(arr[i][k]==468) cells[i][k] = new Cell(false, true, true, true);
//
//                else if(arr[i][k]==2468) cells[i][k] = new Cell(true, true, true, true);
//
//            }
//        }
//    }

}
