package com.example.zy1584.mytbsfilereader;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


import androidx.appcompat.app.AppCompatActivity;

import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsReaderView;
import com.tencent.smtt.sdk.ValueCallback;

import java.io.File;

public class FileDisplayActivity extends AppCompatActivity implements TbsReaderView.ReaderCallback {

    private static final String TAG = "FileDisplayActivity";
    private TbsReaderView mTbsReaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_display);

        mTbsReaderView = new TbsReaderView(this, this);
        RelativeLayout mRelativeLayout = findViewById(R.id.activity_file_display);
        mRelativeLayout.addView(mTbsReaderView,new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        Intent intent = getIntent();
        if (intent != null) {
            String path = intent.getStringExtra("path");
            if (!TextUtils.isEmpty(path)) {
                //增加下面一句解决没有TbsReaderTemp文件夹存在导致加载文件失败
                String bsReaderTemp = "/storage/emulated/0/TbsReaderTemp";
                File bsReaderTempFile = new File(bsReaderTemp);

                if (!bsReaderTempFile.exists()) {
                    Log.d(TAG, "准备创建/storage/emulated/0/TbsReaderTemp！！");
                    boolean mkdir = bsReaderTempFile.mkdir();
                    if (!mkdir) {
                        Log.e(TAG, "创建/storage/emulated/0/TbsReaderTemp失败！！！！！");
                    }
                }

                boolean bool = mTbsReaderView.preOpen(getFileType(path), false);
                if (bool) {
                    Bundle bundle = new Bundle();
                    bundle.putString("filePath", path);
                    bundle.putString("tempPath", bsReaderTemp);
                    mTbsReaderView.openFile(bundle);
                }
            }
        }
    }

    public static void show(Context context, String path) {
        Intent intent = new Intent(context, FileDisplayActivity.class);
        intent.putExtra("path", path);
        context.startActivity(intent);
    }

    /***
     * 获取文件类型
     *
     * @param paramString
     * @return
     */
    private String getFileType(String paramString) {
        String str = "";

        if (TextUtils.isEmpty(paramString)) {
            Log.d(TAG, "paramString---->null");
            return str;
        }
        Log.d(TAG, "paramString:" + paramString);
        int i = paramString.lastIndexOf('.');
        if (i <= -1) {
            Log.d(TAG, "i <= -1");
            return str;
        }


        str = paramString.substring(i + 1);
        Log.d(TAG, "paramString.substring(i + 1)------>" + str);
        return str;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTbsReaderView != null) {
            mTbsReaderView.onStop();
        }
    }

    @Override
    public void onCallBackAction(Integer integer, Object o, Object o1) {

    }
}
