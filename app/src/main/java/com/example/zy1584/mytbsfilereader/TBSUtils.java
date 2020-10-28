package com.example.zy1584.mytbsfilereader;

import android.content.Context;
import android.os.Handler;
import android.view.View;

import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;
import com.tencent.smtt.sdk.ValueCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Logger;

public class TBSUtils {
    public static final String TAG = "TBSUtils";
    public static final Logger logger = Logger.getLogger(TAG);
    public static boolean isInitFinished;//此标记不是很准确，因为有的手机上发现onViewInitFinished不会回调
    public static void initTbs(Context context) {
//        QbSdk.setDownloadWithoutWifi(true);
        QbSdk.initX5Environment(context,new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
                logger.info("onCoreInitFinished: ");
            }

            @Override
            public void onViewInitFinished(boolean b) {
                //回调接口初始化完成接口回调，onViewInitFinished
                // 参数中为 true 代表 x5 内核初始化完成，
                //false 为系统内核初始化完成。此参数可以传 Null，无影响。
                isInitFinished = b;
                logger.info("onViewInitFinished: b="+b);
            }
        });
        QbSdk.setTbsListener(new TbsListener() {
            @Override
            public void onDownloadFinish(int i) {
                //tbs内核下载完成回调
                logger.info("onDownloadFinish: i="+i);
            }

            @Override
            public void onInstallFinish(int i) {
                //内核安装完成回调，
                logger.info("onInstallFinish: i="+i);
            }

            @Override
            public void onDownloadProgress(int i) {
                //下载进度监听
                logger.info("onDownloadProgress: i="+i);
            }
        });
    }

    public static void openFileReader(Context context, String pathName, ValueCallback<String> valueCallback) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("local", "true");//“true”表示是进入文件查看器，如果不设置或设置为“false”，则进入 miniqb 浏览器模式。不是必须设置项。
        params.put("style", "0");//“0”表示文件查看器使用默认的 UI 样式。“1”表示文件查看器使用微信的 UI 样式。不设置此 key或设置错误值，则为默认 UI 样式。
//        params.put("topBarBgColor", "#4D7CFE");//定制文件查看器的顶部栏背景色。格式为“#xxxxxx”，例“#2CFC47”;不设置此 key 或设置错误值，则为默认 UI 样式。
        JSONObject Object = new JSONObject();
        try {
            Object.put("pkgName",context.getApplicationContext().getPackageName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.put("menuData",Object.toString());
//        QbSdk.getMiniQBVersion(context);
        int i = QbSdk.openFileReader(context, pathName, params, valueCallback);
//        1：用 QQ 浏览器打开
//        2：用 MiniQB 打开
//        3：调起阅读器弹框
//        -1：filePath 为空 打开失败
        logger.info("打开文件结果：i="+i);
    }

    public static void hideView(final View pb, String s) {
        logger.info("onReceiveValue: "+s);//TbsReaderDialogClosed,open QB,open success
        if (pb == null) return;
        if("fileReaderClosed".equals(s)){
            pb.setVisibility(View.GONE);
        }
        if("TbsReaderDialogClosed".equals(s)){
            pb.setVisibility(View.GONE);
        }
        if("open QB".equals(s)){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    pb.setVisibility(View.GONE);
                }
            }, 1000);
        }
    }

}
