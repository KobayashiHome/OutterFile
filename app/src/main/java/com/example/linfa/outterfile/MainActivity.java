package com.example.linfa.outterfile;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.PatternMatcher;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

        String content = mNote.getText().toString().trim();
        saveToFile("sky","skylinelin",content);
    }

    /**
     * 文件保存
     */
    private boolean saveToFile(String dirName, String fileName, String content) {

        return false;

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
