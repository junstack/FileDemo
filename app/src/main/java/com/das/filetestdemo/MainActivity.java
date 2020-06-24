package com.das.filetestdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.das.filetestdemo.utils.AssetsUtils;
import com.das.filetestdemo.utils.ConstantUtils;
import com.das.filetestdemo.utils.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static com.das.filetestdemo.utils.ConstantUtils.bitmapToBase64;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private AssetManager assetManager;
    private MediaPlayer mediaPlayer;
    private static final String TAG = "main";
    /**
     * 获取存取text的缓存文件夹
     */
    private String fileTextPath;
    /**
     * 获取存取图片的缓存文件夹
     */
    private String fileImagePath;
    private TextView tv_file;
    private ImageView iv_bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assetManager = getAssets();
        mediaPlayer = new MediaPlayer();

        Button btn_write_text = findViewById(R.id.btn_write_text);
        Button btn_write_bitmap = findViewById(R.id.btn_write_bitmap);
        Button btn_write_base64 = findViewById(R.id.btn_write_base64);
        Button btn_read_txt = findViewById(R.id.btn_read_txt);
        Button btn_read_bitmap = findViewById(R.id.btn_read_bitmap);
        Button btn_clean = findViewById(R.id.btn_clean);
        Button btn_ass = findViewById(R.id.btn_ass);
        tv_file = findViewById(R.id.tv_file);
        iv_bitmap = findViewById(R.id.iv_bitmap);

        btn_write_text.setOnClickListener(this);
        btn_write_bitmap.setOnClickListener(this);
        btn_write_base64.setOnClickListener(this);
        btn_read_txt.setOnClickListener(this);
        btn_read_bitmap.setOnClickListener(this);
        btn_ass.setOnClickListener(this);
        btn_clean.setOnClickListener(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }

    @Override
    public void onClick(View v) {
        Bitmap bitmap;
        switch (v.getId()) {
            case R.id.btn_write_text:
                //向指定文件写入字符串
                String text = "测试测试";
                fileTextPath = FileUtils.getCacheDir(this, "test").getAbsolutePath() + "/text";
                if (FileUtils.writeText(new File(fileTextPath), text)) {
                    Toast.makeText(MainActivity.this, "写入成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "写入失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_write_bitmap:
                //向指定文件写入图片
                fileImagePath = FileUtils.getCacheDir(MainActivity.this, "test").getAbsolutePath() + "/launcher.png";
                bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);//用于根据给定的资源ID从指定的资源文件中解析、创建Bitmap对象。
                if (FileUtils.writeBitmap(new File(fileImagePath), bitmap)) {
                    Toast.makeText(MainActivity.this, "写入成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "写入失败", Toast.LENGTH_SHORT).show();
                }
                bitmap.recycle();
                break;
            case R.id.btn_write_base64:
                //向指定文件写入Base64
                fileImagePath = FileUtils.getCacheDir(MainActivity.this, "test").getAbsolutePath() + "/launcher.png";
                bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);//用于根据给定的资源ID从指定的资源文件中解析、创建Bitmap对象。
                String base64String = ConstantUtils.bitmapToBase64(bitmap);
                if (FileUtils.writeBase64String(new File(fileImagePath), base64String)) {
                    Toast.makeText(MainActivity.this, "写入成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "写入失败", Toast.LENGTH_SHORT).show();
                }
                bitmap.recycle();
                break;
            case R.id.btn_read_txt:
                fileTextPath = FileUtils.getCacheDir(this, "test").getAbsolutePath() + "/text";
                tv_file.setText(FileUtils.readText(new File(fileTextPath)));
                break;
            case R.id.btn_read_bitmap:
                fileImagePath = FileUtils.getCacheDir(MainActivity.this, "test").getAbsolutePath() + "/launcher.png";
                bitmap = FileUtils.readBitmap(new File(fileImagePath));
                iv_bitmap.setImageBitmap(bitmap);
                break;
            case R.id.btn_ass:
                //读取assets文件数据
//                String str = AssetsUtils.getAssetsFile(this,"test.txt");
//                tv_file.setText(str);
                //读取assets文件数据
//                String str = AssetsUtils.getResourceRaw(this,R.raw.test);
//                tv_file.setText(str);
                //读取与res同目录assets的文件(音频文件)
                AssetFileDescriptor fileDescriptor = AssetsUtils.getAssetsVoice(this, "alarm.mp3");
                mediaPlayer.reset();
                try {
                    mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
                            fileDescriptor.getStartOffset(), fileDescriptor.getLength());
                    mediaPlayer.prepare();
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_clean:
                //清除缓存文件夹内容
                FileUtils.clearCache(this);
                tv_file.setText("");
                iv_bitmap.setImageBitmap(null);
                break;
        }
    }
}
