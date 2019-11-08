package com.tagsoft.mazegame;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

public class FreeModeActivity extends AppCompatActivity {

    GridView gridView;
    MyAdapter adapter;
    ArrayList<ClearData> datas = new ArrayList<>();
    int stage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_mode);

        getSupportActionBar().setTitle("Stage Select");

        gridView = findViewById(R.id.gridview);

        for(int i=0;i<100;i++){
            datas.add(new ClearData(i+1, 0, "00", "00"));
        }

        adapter = new MyAdapter(getLayoutInflater(), datas);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                stage = position+1;
                Intent intent = new Intent(FreeModeActivity.this, StageActivity.class);
                intent.putExtra("stage", stage);
                startActivityForResult(intent, 20);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 20:
                if(resultCode == RESULT_OK){
                    int numOfStar = data.getIntExtra("numOfStar", 0);
                    String time = data.getStringExtra("time");
                    String[] timeArray = time.split(":");
                    datas.get(stage-1).setStarsNumber(numOfStar);
                    datas.get(stage-1).setMinute(timeArray[0]);
                    datas.get(stage-1).setSecond(timeArray[1]);
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }
}
