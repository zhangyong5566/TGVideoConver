package com.example.zhang.tgvideoconver;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhang.tgvideoconver.utils.AndroidUtilities;
import com.example.zhang.tgvideoconver.utils.VideoUtil;

import java.io.File;

public class MainActivity extends AppCompatActivity implements NotificationCenter.NotificationCenterDelegate {

    private static final int WRITE_EXTERNAL_STORAGE = 1;
    private static final int READ_EXTERNAL_STORAGE = 2;
    private TextView mAvailable;
    private TextView mAttachPath;
    private VideoEditedInfo mCompressionSettings;
    private TextView mTvCanclMsg;
    private TextView mTvRunMsg;
    private long mStarTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NotificationCenter.getInstance().addObserver(this, NotificationCenter.FilePreparingFailed);
        NotificationCenter.getInstance().addObserver(this, NotificationCenter.FilePreparingStarted);
        NotificationCenter.getInstance().addObserver(this, NotificationCenter.FileNewChunkAvailable);
        NotificationCenter.getInstance().addObserver(this, NotificationCenter.FileUploadProgressChanged);

        //检查版本是否大于M
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        WRITE_EXTERNAL_STORAGE);
            } else {
                Toast.makeText(MainActivity.this, "权限已申请", Toast.LENGTH_SHORT).show();
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        READ_EXTERNAL_STORAGE);
            } else {
                Toast.makeText(MainActivity.this, "权限已申请", Toast.LENGTH_SHORT).show();
            }
        }

        mAvailable = findViewById(R.id.tv_available);
        mAttachPath = findViewById(R.id.tv_attachPath);
        mTvRunMsg = findViewById(R.id.tv_runMsg);
        mTvCanclMsg = findViewById(R.id.tv_canclMsg);

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(MainActivity.this, Uri.fromFile(new File(getSDPath() + "/" + "test.mp4")));
        int bitrate = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE));


        findViewById(R.id.bt_ok).setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {

                mCompressionSettings = VideoUtil.createCompressionSettings(getSDPath() + "/" + "test.mp4");
                //给一个ID标识
                mCompressionSettings.id = System.currentTimeMillis();
                //原始文件大小
                long originSize = mCompressionSettings.estimatedDuration / 1000 * bitrate / 8;
                if (mCompressionSettings.estimatedSize > originSize) {   //不进行压缩
                    mAvailable.setText("当前文件不建议压缩");
                } else {
                    mStarTime = System.currentTimeMillis();
                    mCompressionSettings.attachPath = getSDPath() + "/" + "test_compress.mp4";
                    MediaController.getInstance().scheduleVideoConvert(mCompressionSettings);
                }


            }
        });

        findViewById(R.id.bt_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCompressionSettings != null) {
                    MediaController.getInstance().cancelVideoConvert(mCompressionSettings);
                    mTvCanclMsg.setText("取消压缩任务ID："+mCompressionSettings.id);
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == WRITE_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(MainActivity.this, "权限已申请", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "权限已拒绝", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == READ_EXTERNAL_STORAGE) {

            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    //判断是否勾选禁止后不再询问
                    boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissions[i]);
                    if (showRequestPermission) {
                        Toast.makeText(MainActivity.this, "权限未申请", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NotificationCenter.getInstance().removeObserver(this, NotificationCenter.FilePreparingFailed);
        NotificationCenter.getInstance().removeObserver(this, NotificationCenter.FilePreparingStarted);
        NotificationCenter.getInstance().removeObserver(this, NotificationCenter.FileNewChunkAvailable);
        NotificationCenter.getInstance().removeObserver(this, NotificationCenter.FileUploadProgressChanged);

    }

    @Override
    public void didReceivedNotification(int id, Object... args) {
        if (id == NotificationCenter.FilePreparingFailed) {
            AndroidUtilities.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    mCompressionSettings = null;
                    Toast.makeText(ApplicationLoader.applicationContext, "压缩失败", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (id == NotificationCenter.FilePreparingStarted) {
            AndroidUtilities.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    if (args.length>1) {
                        if (args[1] instanceof VideoEditedInfo){
                            VideoEditedInfo infos = (VideoEditedInfo) args[1];
                            mTvRunMsg.setText("当前压缩任务ID："+infos.id);
                        }
                    }
                    Toast.makeText(ApplicationLoader.applicationContext, "准备压缩", Toast.LENGTH_SHORT).show();
                }
            });

        } else if (id == NotificationCenter.FileNewChunkAvailable) {
            String cachePath = (String) args[0];
            long fileLenght = (long) args[1];
            long last = (long) args[2];
            AndroidUtilities.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    mAvailable.setText("压缩大小：" + fileLenght);
                    if (fileLenght == last) {
                        mCompressionSettings = null;
                        Toast.makeText(ApplicationLoader.applicationContext, "压缩完成", Toast.LENGTH_SHORT).show();
                        mAttachPath.setText("压缩后文件路径：" + cachePath+"耗时："+(System.currentTimeMillis()-mStarTime)+"毫秒");
                    }

                }
            });

        } else if (id == NotificationCenter.FileUploadProgressChanged) {

        }


    }

    public String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.getPath();
    }
}
