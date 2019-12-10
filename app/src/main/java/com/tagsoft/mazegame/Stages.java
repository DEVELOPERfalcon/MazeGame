package com.tagsoft.mazegame;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Stages {

    private boolean constructorFinish = false;

    public int[][] stage = new int[Board.ROWS][Board.COLS];     //미로 모양 정보 받을 배열
    public int[][] stage2 = new int[Board.ROWS][Board.COLS2];   //stage51 전용 배열

    public Stages(Context context, final int stageNum) {
        final AssetManager assetManager = context.getAssets();
        new Thread(){
            @Override
            public void run() {
                try {
                    InputStream is = assetManager.open("jsons/Stage.json");
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader reader = new BufferedReader(isr);
                    StringBuffer buffer = new StringBuffer();
                    String line = reader.readLine();
                    while (line != null){
                        buffer.append(line+"\n");
                        line = reader.readLine();
                    }
                    String jsonData = buffer.toString();
                    JSONArray stageArray = new JSONArray(jsonData);
                    JSONObject selectedStage = stageArray.getJSONObject(stageNum-1);
                    if(stageNum==51){               //stage51이면
                        for(int i=0;i<Board.ROWS;i++){
                            for(int k=0;k<Board.COLS2;k++){
                                JSONArray stageData = selectedStage.getJSONArray("stage"+stageNum);
                                stage2[i][k] = Integer.parseInt(stageData.getString((i*Board.COLS2)+k).toString());
                            }
                        }
                    }else{                          //stage51이 아니면
                        for(int i=0;i<Board.ROWS;i++){
                            for(int k=0;k<Board.COLS;k++){
                                JSONArray stageData = selectedStage.getJSONArray("stage"+stageNum);
                                stage[i][k] = Integer.parseInt(stageData.getString((i*Board.COLS)+k).toString());
                            }
                        }
                    }

                    constructorFinish = true;
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public boolean isConstructorFinish() {
        return constructorFinish;                               //생성자 작업 종료 여부 얻기
    }

    public void setConstructorFinish(boolean constructorFinish) {
        this.constructorFinish = constructorFinish;             //생성자 작업 종료 여부 설정
    }
}
