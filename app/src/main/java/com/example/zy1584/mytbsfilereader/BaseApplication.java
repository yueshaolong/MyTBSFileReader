package com.example.zy1584.mytbsfilereader;

import android.app.Application;
import android.util.Log;

import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        TBSUtils.initTbs(this);
    }
}
