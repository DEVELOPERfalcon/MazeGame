package com.tagsoft.mazegame;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.net.ProtocolException;
import java.net.URL;
import java.util.Random;

public class OptionActivity extends AppCompatActivity {

    EditText inputNickName;
    TextView nickNameSize;
    Button nickNameCheck;
    Button inputCencel;
    AlertDialog dialog;
    AlertDialog interlockDialog;
    EditText inputCodeEditText;
    Button inputCodeButton;

    TextView enteredNickName;
    Button nickNameChange;
    TextView showCodeTextView;
    Button interlockButton;
    RadioGroup joyStickLocation;
    RadioButton leftButton;
    RadioButton centerButton;
    RadioButton rightButton;
    Button recordCheckButton;

    String nickname;
    String nickname2;
    String code;

    String dbName = "Data.db";
    String tableName = "JoystickLocation";
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        getSupportActionBar().setTitle("설정");

        findView();
        setListener();
        loadingWork();

        recordCheckButton.setBackgroundColor(Color.TRANSPARENT);
   }

   public void findView(){
       enteredNickName = findViewById(R.id.tv_nickname);
       nickNameChange = findViewById(R.id.btn_nickname_change);
       interlockButton = findViewById(R.id.btn_interlocking_nickname);
       showCodeTextView = findViewById(R.id.tv_code);
       joyStickLocation = findViewById(R.id.radiogroup_joystick_location);
       leftButton = findViewById(R.id.radiobutton_joystick_location_left);
       centerButton = findViewById(R.id.radiobutton_joystick_location_center);
       rightButton = findViewById(R.id.radiobutton_joystick_location_right);
       recordCheckButton = findViewById(R.id.btn_connect_http);
   }
   public void setListener(){
       nickNameChange.setOnClickListener(nickNameChangeListener);
       interlockButton.setOnClickListener(interlockListener);
       joyStickLocation.setOnCheckedChangeListener(radioButtonChangeListener);
       recordCheckButton.setOnClickListener(recordCheckListener);
   }

   public void loadingWork(){
       new Thread(){
           @Override
           public void run() {
               db = openOrCreateDatabase(dbName, MODE_PRIVATE, null);
               db.execSQL("CREATE TABLE IF NOT EXISTS "+tableName+" ('LEFT' boolena, 'CENTER' boolean, 'RIGHT' boolean)");

               loadNickName();
               loadJoystickLocation();
               loadCode();
           }
       }.start();
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
                        enteredNickName.setText(nickname);
                        nickNameChange.setText("Change");
                    }
                });
                //Toast.makeText(this, "내장메모리 저장", Toast.LENGTH_SHORT).show();
            }else{
                //Toast.makeText(this, "파일 없음", Toast.LENGTH_SHORT).show();
                createCode();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    public void createCode(){
        new Thread(){
            @Override
            public void run() {
                Random random = new Random();
                StringBuffer buffer = new StringBuffer();
                for(int i=0;i<10;i++){
                    if(i==0) buffer.append("M");
                    else if(i==5) buffer.append("C");
                    else buffer.append(random.nextInt(10));
                }
                code = buffer.toString();
            }
        }.start();
    }

    public void loadCode(){
        try {
            File file = new File(getFilesDir(), "Code.txt");
            if(file.exists()){
                FileInputStream fis = openFileInput("Code.txt");
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader reader = new BufferedReader(isr);
                String line = reader.readLine();
                code = line;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showCodeTextView.setText("Nickname Code: "+code);
                    }
                });
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveCode(String code){
        try{
            FileOutputStream fos = this.openFileOutput("Code.txt", MODE_PRIVATE);
            PrintWriter writer = new PrintWriter(fos);
            writer.println(code);
            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

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

    /////////////////   Listener    //////////////////////

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
           if(editable.toString().getBytes().length >= 6){
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
                    String serverUrl = "http://developer3.dothome.co.kr/MAZEescape/saveNickname.php";
                    try {
                        URL url = new URL(serverUrl);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("POST");
                        connection.setDoInput(true);
                        connection.setDoOutput(true);
                        connection.setUseCaches(false);
                        //보낼 데이터
                        String query;
                        if(nickname2 != null) query = "nickname="+nickname+"&nickname2="+nickname2+"&code="+code;
                        else query = "nickname="+nickname+"&nickname2="+"null"+"&code="+code;
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
                                Toast.makeText(OptionActivity.this, buffer.toString(), Toast.LENGTH_SHORT).show();
                                if(buffer.toString().equals("사용가능한 닉네임입니다.\n저장되었습니다.\n")){
                                    saveNickName(nickname); //내장메모리 저장
                                    saveCode(code);         //내장메모리 저장
                                    showCodeTextView.setText("Nickname Code: "+code);
                                    enteredNickName.setText(nickname);
                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.toggleSoftInput(0, 0);
                                    nickNameChange.setText("Change");
                                    dialog.dismiss();
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

    View.OnClickListener cencelListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dialog.dismiss();
        }
    };

    View.OnClickListener interlockListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            LayoutInflater inflater = OptionActivity.this.getLayoutInflater();
            View v = inflater.inflate(R.layout.interlocking_nickname, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(OptionActivity.this);

            inputCodeEditText = v.findViewById(R.id.et_code);
            inputCodeButton = v.findViewById(R.id.btn_input_code);

            inputCodeButton.setOnClickListener(inputCodeListener);

            builder.setView(v);
            builder.setNegativeButton("취소", null);
            interlockDialog = builder.create();
            interlockDialog.setCancelable(false);
            interlockDialog.show();
        }
    };

    View.OnClickListener inputCodeListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            new Thread(){
                @Override
                public void run() {
                    String enteredCode = inputCodeEditText.getText().toString();
                    String serverUrl = "http://developer3.dothome.co.kr/MAZEescape/interlockingNickname.php";
                    try{
                        URL url = new URL(serverUrl);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("POST");
                        connection.setDoInput(true);
                        connection.setDoOutput(true);
                        connection.setUseCaches(false);
                        //보낼 데이터
                        String query = "code="+enteredCode;
                        OutputStream os = connection.getOutputStream();
                        OutputStreamWriter writer = new OutputStreamWriter(os);
                        writer.write(query, 0, query.length());
                        writer.flush();
                        writer.close();
                        //echo결과 받기
                        InputStream is = connection.getInputStream();
                        InputStreamReader isr = new InputStreamReader(is);
                        BufferedReader reader = new BufferedReader(isr);
                        final String line = reader.readLine();
                        if( !line.equals("존재하지 않는 코드입니다.") ) {
                            saveNickName(line);
                            saveCode(enteredCode);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    enteredNickName.setText(line);
                                    showCodeTextView.setText("Nickname Code: "+code);
                                    interlockDialog.dismiss();
                                }
                            });
                        }else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    inputCodeEditText.setText("");
                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.toggleSoftInput(0, 0);
                                    Toast.makeText(OptionActivity.this, "존재하지 않는 코드 입니다.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    };

    View.OnClickListener nickNameChangeListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(nickname != null) nickname2 = nickname;
            LayoutInflater inflater = OptionActivity.this.getLayoutInflater();
            View v = inflater.inflate(R.layout.nickname_input, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(OptionActivity.this);

            inputNickName = v.findViewById(R.id.et_nickname);
            nickNameSize = v.findViewById(R.id.tv_nickname_size);
            nickNameCheck = v.findViewById(R.id.btn_nickname_check);
            inputCencel = v.findViewById(R.id.btn_cencel);

            if(nickname != null) {
                inputNickName.setText(nickname);
                nickNameSize.setText(nickname.getBytes().length+"/20 byte");
            }
            nickNameCheck.setEnabled(false);
            inputNickName.addTextChangedListener(nickNameWatcher);
            nickNameCheck.setOnClickListener(nickNameCheckListener);
            inputCencel.setOnClickListener(cencelListener);

            builder.setView(v);
            dialog = builder.create();
            dialog.setCancelable(false);
            dialog.show();

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

    View.OnClickListener recordCheckListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://developer3.dothome.co.kr/MAZEescape/index.html"));
            startActivity(intent);
        }
    };

}
