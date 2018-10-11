package com.codelabs.anphoqua.sharedpreference;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    Button writeButton, readButton, clearButton;
    TextView favoriteAnimalTextView;
    EditText favoriteAnimalEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        favoriteAnimalEditText=findViewById(R.id.FavoriteAnimalEditTextID);
        favoriteAnimalTextView=findViewById(R.id.FavoriteAnimalTextViewID);
        writeButton=findViewById(R.id.WriteButtonID);
        readButton=findViewById(R.id.ReadButtonID);
        clearButton=findViewById(R.id.ClearButtonID);

        writeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = favoriteAnimalEditText.getText().toString();

                //kiem tra xem version la truoc hay sau M
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//bang M
                    //kiem tra da xin quyen va duoc chap nhan chua
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        // if nay la cap roi
                        writeFA();
                    } else { //else la chua cap quyen
                        //hien dialog de xin quyen, goi onRequestPermissionResult
                        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                    }
                }
                else //bang truoc M
                {
                    writeFA();
                }
            }
        });

        readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readFA();
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearFA();
            }
        });
    }

    public void writeFA(){
        String fa=favoriteAnimalEditText.getText().toString().trim();

        //Share Preferences
        /*
        SharedPreferences write=getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor=write.edit();
        editor.putString("favorite_animal", fa);
        editor.commit();
        */


        //Internal storage
        /*
        try {
            FileOutputStream outputStream=openFileOutput("favorite-animal.txt",MODE_PRIVATE);
            outputStream.write(fa.getBytes());
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

        //External storage
        try {
            File link=Environment.getExternalStorageDirectory();
            File file=new File(link, "favorite-animal-2.txt");
            FileOutputStream outputStream=new FileOutputStream(file);
            outputStream.write(fa.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void readFA(){

        /*
        SharedPreferences read=getPreferences(MODE_PRIVATE);
        String fa=read.getString("favorite_animal","This is your favorite animal.");
        if (fa.equalsIgnoreCase("")){
            favoriteAnimalTextView.setText("Hmmm... You don't like any animal.");
        } else favoriteAnimalTextView.setText(fa);
        */

        //Internal storage
        /*
        try {
            FileInputStream inputStream=openFileInput("favorite-animal.txt");
            byte[] buffer=new byte[inputStream.available()];
            inputStream.read(buffer);
            String fa=new String(buffer);
            inputStream.close();
            favoriteAnimalTextView.setText(fa);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */


        //External storage
        try {
            File link=Environment.getExternalStorageDirectory();
            File file=new File(link,"favorite-animal-2.txt");
            FileInputStream inputStream=new FileInputStream(file);
            byte[] buffer=new byte[inputStream.available()];
            inputStream.read(buffer);
            String fa=new String(buffer);
            inputStream.close();
            if (fa.equalsIgnoreCase("")){
                favoriteAnimalTextView.setText("Hmmm... You don't like any animal.");
            } else favoriteAnimalTextView.setText(fa);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void clearFA() {
        favoriteAnimalTextView.setText("...");
        favoriteAnimalEditText.setText("");

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "xin duoc quyen roi", Toast.LENGTH_SHORT).show();
            writeFA();
        }

    }
}
