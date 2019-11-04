package com.tagsoft.mazegame;

public class ClearData {
    private int stageNumber;
    private int starsNumber;
    private String minute;
    private String second;

    public ClearData(int stageNumber, int starsNumber, String minute, String second) {
        this.stageNumber = stageNumber;
        this.starsNumber = starsNumber;
        this.minute = minute;
        this.second = second;
    }

    public int getStageNumber() {
        return stageNumber;
    }

    public void setStageNumber(int stageNumber) {
        this.stageNumber = stageNumber;
    }

    public int getStarsNumber() {
        return starsNumber;
    }

    public void setStarsNumber(int starsNumber) {
        this.starsNumber = starsNumber;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }
}
