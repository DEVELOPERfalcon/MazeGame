package com.tagsoft.mazegame;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {

    LayoutInflater inflater;
    ArrayList<ClearData> datas = new ArrayList<>();

    boolean stage51Lock = true;
    int totalStarNum;

    public MyAdapter(LayoutInflater inflater, ArrayList<ClearData> datas, int totalStarNum) {
        this.inflater = inflater;
        this.datas = datas;
        this.totalStarNum = totalStarNum;
        if(totalStarNum == 150) stage51Lock = false;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if(view == null || position != 50 || !stage51Lock){
            view = inflater.inflate(R.layout.stage, viewGroup, false);
        }
        if(position == 50 && stage51Lock) {
            view = inflater.inflate(R.layout.stage51, viewGroup, false);
            return view;
        }
        ClearData clearData = datas.get(position);

        TextView stageNumber = view.findViewById(R.id.stage_num);
        RatingBar ratingBar = view.findViewById(R.id.rating);
        TextView clearTime = view.findViewById(R.id.clear_time);

        stageNumber.setText("stage "+clearData.getStageNumber());
        ratingBar.setRating((float)clearData.getStarsNumber());
        clearTime.setText(clearData.getHour()+":"+clearData.getMinute()+":"+clearData.getSecond());

        return view;
    }

    public void setStage51Lock(boolean stage51Lock) {
        this.stage51Lock = stage51Lock;
    }
    public void setTotalStarNum(int totalStarNum){
        this.totalStarNum = totalStarNum;
    }

    public boolean isStage51Lock() {
        return stage51Lock;
    }
    public int getTotalStarNum() {
        return totalStarNum;
    }
}
