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

    public int[][] stage = new int[20][22];

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
                    for(int i=0;i<20;i++){
                        for(int k=0;k<22;k++){
                            JSONArray stageData = selectedStage.getJSONArray("stage"+stageNum);
                            stage[i][k] = Integer.parseInt(stageData.getString((i*22)+k).toString());
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
        return constructorFinish;
    }

    public void setConstructorFinish(boolean constructorFinish) {
        this.constructorFinish = constructorFinish;
    }
}
