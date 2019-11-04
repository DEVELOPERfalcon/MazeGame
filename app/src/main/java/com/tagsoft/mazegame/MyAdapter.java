package com.tagsoft.mazegame;

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

    public MyAdapter(LayoutInflater inflater, ArrayList<ClearData> datas) {
        this.inflater = inflater;
        this.datas = datas;
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

        if(view == null){
            view = inflater.inflate(R.layout.stage, viewGroup, false);
        }

        ClearData clearData = datas.get(position);

        TextView stageNumber = view.findViewById(R.id.stage_num);
        RatingBar ratingBar = view.findViewById(R.id.rating);
        TextView clearTime = view.findViewById(R.id.clear_time);

        stageNumber.setText("stage "+clearData.getStageNumber());
        ratingBar.setRating((float)clearData.getStarsNumber());
        clearTime.setText(clearData.getMinute()+":"+clearData.getSecond());

        return view;
    }
}
