package com.tagsoft.mazegame;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class optionActivity extends AppCompatActivity {

    EditText inputNickName;
    TextView nickNameSize;
    Button nickNameCheck;
    RadioGroup joyStickLocation;
    RadioButton leftButton;
    RadioButton centerButton;
    RadioButton rightButton;
    Button recordCheckButton;
    Button nickNameChange;

    String nickname;
    String serverUrl;
    String nickname2;

    String query;

    String dbName = "Data.db";
    String tableName = "JoystickLocation";
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        getSupportActionBar().setTitle("설정정");

        inputNickName = findViewById(R.id.et_nickname);
        nickNameSize = findViewById(R.id.tv_nickname_size);
        nickNameCheck = findViewById(R.id.btn_nickname_check);
        joyStickLocation = findViewById(R.id.radiogroup_joystick_location);
        leftButton = findViewById(R.id.radiobutton_joystick_location_left);
        centerButton = findViewById(R.id.radiobutton_joystick_location_center);
        rightButton = findViewById(R.id.radiobutton_joystick_location_right);
        recordCheckButton = findViewById(R.id.btn_connect_http);
        nickNameChange = findViewById(R.id.btn_nickname_change);

        inputNickName.addTextChangedListener(nickNameWatcher);
        nickNameCheck.setOnClickListener(nickNameCheckListener);
        nickNameChange.setOnClickListener(nickNameChangeListener);
        joyStickLocation.setOnCheckedChangeListener(radioButtonChangeListener);
        recordCheckButton.setBackgroundColor(Color.TRANSPARENT);

        new Thread(){
            @Override
            public void run() {
                db = openOrCreateDatabase(dbName, MODE_PRIVATE, null);
                db.execSQL("CREATE TABLE IF NOT EXISTS "+tableName+" ('LEFT' boolena, 'CENTER' boolean, 'RIGHT' boolean)");

                loadNickName();
                loadJoystickLocation();
            }
        }.start();

   }

   TextWatcher nickNameWatcher = new TextWatcher() {
       @Override
       public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
       }

       @Override
       public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
           nickNameSize.setText(charSequence.toString().getBytes().length+"/20 byte");
       }

       @Override
       public void afterTextChanged(Editable editable) {
           if(editable.toString().getBytes().length >= 8){
               nickNameCheck.setEnabled(true);
           }else{
               nickNameCheck.setEnabled(false);
           }
           if(editable.toString().getBytes().length > 20){
               editable.delete(editable.length()-1, editable.length());
           }
       }
   };

    View.OnClickListener nickNameCheckListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            new Thread(){
                @Override
                public void run() {
                    nickname = inputNickName.getText().toString();
                    serverUrl = "http://developer3.dothome.co.kr/MAZEescape/sendPostType.php";
                    try {
                        URL url = new URL(serverUrl);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("POST");
                        connection.setDoInput(true);
                        connection.setDoOutput(true);
                        connection.setUseCaches(false);
                        //보낼 데이터
                        if(nickname2 != null) query = "nickname="+nickname+"&nickname2="+nickname2;
                        else query = "nickname="+nickname+"&nickname2="+"null";
                        OutputStream os = connection.getOutputStream();
                        OutputStreamWriter writer = new OutputStreamWriter(os);
                        writer.write(query, 0, query.length());
                        writer.flush();
                        writer.close();
                        //echo결과 받기
                        InputStream is = connection.getInputStream();
                        InputStreamReader isr = new InputStreamReader(is);
                        BufferedReader reader = new BufferedReader(isr);
                        final StringBuffer buffer = new StringBuffer();
                        String line = reader.readLine();
                        while(line != null){
                            buffer.append(line+"\n");
                            line = reader.readLine();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(optionActivity.this, buffer.toString(), Toast.LENGTH_SHORT).show();
                                if(buffer.toString().equals("사용가능한 닉네임입니다.\n저장되었습니다.\n")){
                                    saveNickName(nickname);
                                    nickNameCheck.setEnabled(false);
                                    nickNameChange.setEnabled(true);
                                    inputNickName.setEnabled(false);
                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.toggleSoftInput(0, 0);
                                }
                            }
                        });

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    };

    public void saveNickName(String nickname){
        try{
            //닉네임을 내장메모리에 저장
            FileOutputStream fos = this.openFileOutput("NickName.txt", MODE_PRIVATE);
            PrintWriter writer = new PrintWriter(fos);
            writer.println(nickname);
            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void loadNickName(){
        try {
            File file = new File(getFilesDir(), "NickName.txt");
            if(file.exists()){
                FileInputStream fis = openFileInput("NickName.txt");
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader reader = new BufferedReader(isr);
                String line = reader.readLine();
                nickname = line;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        inputNickName.setText(nickname);
                        nickNameCheck.setEnabled(false);
                        nickNameChange.setEnabled(true);
                        inputNickName.setEnabled(false);
                    }
                });
                //Toast.makeText(this, "내장메모리 저장", Toast.LENGTH_SHORT).show();
            }else{
                //Toast.makeText(this, "파일 없음", Toast.LENGTH_SHORT).show();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    View.OnClickListener nickNameChangeListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            nickname2 = inputNickName.getText().toString();
            nickNameCheck.setEnabled(true);
            nickNameChange.setEnabled(false);
            inputNickName.setEnabled(true);
        }
    };

    RadioGroup.OnCheckedChangeListener radioButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, final int checkedId) {
            new Thread(){
                @Override
                public void run() {
                    if(checkedId == R.id.radiobutton_joystick_location_left) {
                        db.execSQL("UPDATE "+tableName+" SET 'LEFT'=1, 'CENTER'=0, 'RIGHT'=0");
                    }else if(checkedId == R.id.radiobutton_joystick_location_center) {
                        db.execSQL("UPDATE "+tableName+" SET 'LEFT'=0, 'CENTER'=1, 'RIGHT'=0");
                    }else if(checkedId == R.id.radiobutton_joystick_location_right) {
                        db.execSQL("UPDATE "+tableName+" SET 'LEFT'=0, 'CENTER'=0, 'RIGHT'=1");
                    }
                }
            }.start();
        }
    };

    public void loadJoystickLocation(){
        Cursor cursor = db.rawQuery("SELECT * FROM "+tableName, null);
        if(cursor == null) return;
        if(cursor.getCount() != 0){
            while (cursor.moveToNext()){
                if(cursor.getInt(0)>0) leftButton.setChecked(true);
                else if(cursor.getInt(1)>0) centerButton.setChecked(true);
                else if(cursor.getInt(2)>0) rightButton.setChecked(true);
            }
        }else {
            db.execSQL("INSERT INTO "+tableName+" ('LEFT', 'CENTER', 'RIGHT') VALUES(0, 1, 0)");
            centerButton.setChecked(true);
        }
    }

}
