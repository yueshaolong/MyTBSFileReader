package com.example.zy1584.mytbsfilereader;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.ValueCallback;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private boolean isOpenInside;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        String[] list = new String[3];
        list[0]="android.permission.WRITE_EXTERNAL_STORAGE";
        list[2]="android.permission.READ_EXTERNAL_STORAGE";
        list[1]="android.permission.READ_PHONE_STATE";
        MainActivity.this.requestPermissions(list, 105);

        findViewById(R.id.btn_download_and_open_doc).setOnClickListener(this);
        findViewById(R.id.btn_local_doc).setOnClickListener(this);
        findViewById(R.id.btn_local_excel).setOnClickListener(this);
        findViewById(R.id.btn_local_txt).setOnClickListener(this);
        findViewById(R.id.btn_local_pdf).setOnClickListener(this);
        findViewById(R.id.btn_local_ppt).setOnClickListener(this);
        findViewById(R.id.btn_neihe).setOnClickListener(this);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rg_open_way);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_openFile) {
                    isOpenInside = true;
                } else if (checkedId == R.id.rb_openFileReader) {
                    isOpenInside = false;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        String filePath = getFilePath(v.getId());
        if (TextUtils.isEmpty(filePath)) return;
        if("http://debugtbs.qq.com".equals(filePath)){
            //打开调试页面
            Intent intent = new Intent(MainActivity.this, BrowserActivity.class);
            MainActivity.this.startActivity(intent);
            return;
        }
        if (isOpenInside) {
            FileDisplayActivity.show(this, filePath);
        } else {
            Log.d(TAG, "onClick: "+filePath);
            File file = new File(filePath);
            Log.d(TAG, "onClick: "+file.getAbsolutePath());
            Log.d(TAG, "onClick: "+file.length());
            TBSUtils.openFileReader(this, filePath, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String s) {
                    Log.d(TAG, "onReceiveValue: " + s);
                }
            });
        }
    }

    private String getFilePath(int id) {
        String path = null;
        switch (id) {
            case R.id.btn_download_and_open_doc:
                path = "http://www.hrssgz.gov.cn/bgxz/sydwrybgxz/201101/P020110110748901718161.doc";
                break;
            case R.id.btn_local_doc:
                // /storage/emulated/0/test.docx
                path = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/test.docx";
                break;
            case R.id.btn_local_txt:
                path = "/storage/emulated/0/test.txt";
                break;
            case R.id.btn_local_excel:
                path = "/storage/emulated/0/test.xlsx";
                break;
            case R.id.btn_local_ppt:
                path = "/storage/emulated/0/test.pptx";
                break;
            case R.id.btn_local_pdf:
                path = "/storage/emulated/0/test.pdf";
                break;
            case R.id.btn_neihe:
                path = "http://debugtbs.qq.com";
                break;
        }
        return path;
    }
}
