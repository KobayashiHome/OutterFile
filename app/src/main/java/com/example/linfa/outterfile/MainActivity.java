package com.example.linfa.outterfile;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @date 2018/4/3
 * @author skylinelin
 */

public class MainActivity extends AppCompatActivity {

    private EditText mNote;
    private Button mRead,mSave;
    private TextView mTvRead;

    private static final int REQ_CODE_FILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        checkFilePermission();
    }

    /**
     * 文件保存
     */
    private boolean saveToFile(String dirName, String fileName, String content) {

       if (!checkEnvironment()){
           //如果sd卡检查失败
           return false;
       }
        //获取存储根目录
       String exter = Environment.getExternalStorageDirectory().getAbsolutePath();
       File dir = new File(exter + "/" + dirName + "/");
       if (!dir.exists()){
           //如果文件夹不存在,就创建一下
           dir.mkdir();
       }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(dir,fileName));
            fos.write(content.getBytes());
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;

    }

    /**
     * 检查是否挂起
     */
    private boolean checkEnvironment() {
        //挂载没有成功
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 动态获取文件存储权限，Android6.0以后需要动态获取权限
     */
    private void checkFilePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            //没有获得权限
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQ_CODE_FILE);
        }
    }

    private void initViews() {

        mNote = findViewById(R.id.ed_Note);
        mRead = findViewById(R.id.bt_Read);
        mSave = findViewById(R.id.bt_Save);
        mTvRead = findViewById(R.id.tv_Read);

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = mNote.getText().toString().trim();
                boolean succ = saveToFile("sky", "skylinelin.txt", content);

                if (succ){
                    Toast.makeText(MainActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this,"保存失败",Toast.LENGTH_SHORT).show();
                }
            }
        });

        mRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = readFile("sky", "skylinelin.txt");
                mTvRead.setText(content);
            }
        });
    }

    /**
     * 问价读取
     */
    private String readFile(String dirName,String fileName) {

        if (!checkEnvironment()){
            return null;
        }

        //获取存储根目录
        String exter = Environment.getExternalStorageDirectory().getAbsolutePath();
        File dir = new File(exter + "/" + dirName + "/");
        if (!dir.exists()){
            //如果文件夹不存在,就创建一下
            return null;
        }

        FileInputStream fls = null;
        BufferedReader reader = null;

        try {
            fls = new FileInputStream(new File(dir,fileName));
            reader = new BufferedReader(new InputStreamReader(fls));

            StringBuilder builder = new StringBuilder();
            String line;
            if ((line = reader.readLine()) != null){
                builder.append(line);
            }
            return builder.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (fls != null){
                    fls.close();
                }
                if (reader !=null){
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQ_CODE_FILE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this,"文件权限获取成功",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this,"文件权限获取失败",Toast.LENGTH_SHORT).show();
        }
    }
}
