package com.tagsoft.mazegame;

public class Cell {

    private boolean topWall;
    private boolean leftWall;
    private boolean rightWall;
    private boolean bottomWall;

    private int column;
    private int row;

    public Cell(){
        this(false, false, false, false);
    }
    public Cell(boolean topWall, boolean leftWall, boolean rightWall, boolean bottomWall){
        this.topWall = topWall;
        this.leftWall = leftWall;
        this.rightWall = rightWall;
        this.bottomWall = bottomWall;
    }

    public void setTopWall(boolean wall){
        topWall = wall;
    }
    public void setLeftWall(boolean wall){
        leftWall = wall;
    }
    public void setRightWall(boolean wall){
        rightWall = wall;
    }
    public void setBottomWall(boolean wall) { bottomWall = wall; }
    public void setColumn(int column) { this.column = column; }
    public void setRow(int row) { this.row = row; }

    public boolean getTopWall(){
        return topWall;
    }
    public boolean getLeftWall(){
        return leftWall;
    }
    public boolean getRightWall(){
        return rightWall;
    }
    public boolean getBottomWall(){
        return bottomWall;
    }
    public int getColumn() { return column; }
    public int getRow() { return row; }

}
