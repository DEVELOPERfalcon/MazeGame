package com.tagsoft.mazegame;

public class ClearData {
    private int stageNumber;
    private int starsNumber;
    private String hour;
    private String minute;
    private String second;

    public ClearData(int stageNumber) {
        this(stageNumber, 0, "23", "59", "59");
    }

    public ClearData(int stageNumber, int starsNumber, String hour, String minute, String second) {
        this.stageNumber = stageNumber;
        this.starsNumber = starsNumber;
        this.hour = hour;
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

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
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
