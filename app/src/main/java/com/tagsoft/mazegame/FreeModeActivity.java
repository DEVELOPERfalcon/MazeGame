package com.tagsoft.mazegame;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

    String dbName = "Data.db";
    String timeTable = "stage";
    String starTable = "star";
    SQLiteDatabase db;

    String serverUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_mode);

        getSupportActionBar().setTitle("Stage Select");

        gridView = findViewById(R.id.gridview);

        for(int i=0;i<100;i++){
            datas.add(new ClearData(i+1));
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

        new Thread(){
            @Override
            public void run() {
                db = openOrCreateDatabase(dbName, MODE_PRIVATE, null);
                StringBuffer buffer = new StringBuffer();
                StringBuffer buffer2 = new StringBuffer();
                for(int i=0;i<100;i++){
                    if(i != 0) {
                        buffer.append(", ");
                        buffer2.append(", ");
                    }
                    buffer.append("'STAGE"+(i+1)+"' String");
                    buffer2.append("'STAGE"+(i+1)+"' int");
                }
                db.execSQL("CREATE TABLE IF NOT EXISTS "+timeTable+" ("+buffer.toString()+")");
                db.execSQL("CREATE TABLE IF NOT EXISTS "+starTable+" ("+buffer2.toString()+")");

                loadStageStarInfomation();
                loadStageTimeInfomation();
            }
        }.start();
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
                    new Thread(){
                        @Override
                        public void run() {
                            db.execSQL("UPDATE "+starTable+" SET 'STAGE"+stage+"'="+datas.get(stage-1).getStarsNumber());
                            db.execSQL("UPDATE "+timeTable+" SET 'STAGE"+stage+"'='"+datas.get(stage-1).getMinute()+":"+datas.get(stage-1).getSecond()+"'");
                        }
                    }.start();
                }
                break;
        }
    }

    public void loadStageStarInfomation(){
        Cursor cursor = db.rawQuery("SELECT * FROM "+starTable, null);
        if(cursor == null) return;
        if(cursor.getCount() != 0){
            if(cursor.moveToNext()){
                for(int i=0; i<100; i++){
                    datas.get(i).setStarsNumber(cursor.getInt(i));
                }
            }
        }else {
            StringBuffer buffer = new StringBuffer();
            for(int i=0; i<100; i++){
                if(i != 0) buffer.append(", ");
                buffer.append("0");

                datas.get(i).setStarsNumber(0);
            }
            db.execSQL("INSERT INTO "+starTable+" VALUES("+buffer.toString()+")");
        }
    }

    public void loadStageTimeInfomation(){
        Cursor cursor = db.rawQuery("SELECT * FROM "+timeTable, null);
        if(cursor == null) return;
        if(cursor.getCount() != 0){
            if(cursor.moveToNext()){
                for(int i=0; i<100; i++){
                    String[] time = cursor.getString(i).split(":");
                    datas.get(i).setMinute(time[0]);
                    datas.get(i).setSecond(time[1]);
                }
            }
        }else {
            StringBuffer buffer = new StringBuffer();
            for(int i=0; i<100; i++){
                if(i != 0) buffer.append(", ");
                buffer.append("'00:00'");

                datas.get(i).setMinute("00");
                datas.get(i).setSecond("00");
            }
            db.execSQL("INSERT INTO "+timeTable+" VALUES("+buffer.toString()+")");
        }
    }
}
