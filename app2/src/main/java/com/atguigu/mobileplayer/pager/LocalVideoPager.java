package com.atguigu.mobileplayer.pager;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.mobileplayer.R;
import com.atguigu.mobileplayer.activity.SystemVideoPlayerActivity;
import com.atguigu.mobileplayer.adapter.LocalVideoAdapter;
import com.atguigu.mobileplayer.domain.MediaItem;
import com.atguigu.mobileplayer.fragment.BaseFragment;

import java.util.ArrayList;

/**
 * 作者：杨光福 on 2017/5/19 11:47
 * QQ：541433511
 * 微信：yangguangfu520
 * 作用：
 */

public class LocalVideoPager extends BaseFragment {
//    private TextView textView;
    private ListView lv;
    private TextView tv_nodata;
    private ArrayList<MediaItem> mediaItems;
    private LocalVideoAdapter adapter;

    //重写视图
    @Override
    public View initView() {
//        textView = new TextView(context);
//        textView.setTextSize(30);
//        textView.setGravity(Gravity.CENTER);
//        textView.setTextColor(Color.RED);
        View view = View.inflate(context, R.layout.fragment_local_video_pager, null);
        lv = (ListView) view.findViewById(R.id.lv);
        tv_nodata = (TextView) view.findViewById(R.id.tv_nodata);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             //   MediaItem mediaItem = mediaItems.get(position);

                MediaItem item = adapter.getItem(position);
                Toast.makeText(context, ""+item.toString(), Toast.LENGTH_SHORT).show();
                //把系统的播放器调起来
                Intent intent = new Intent(context,SystemVideoPlayerActivity.class);
                intent.setDataAndType(Uri.parse(item.getData()),"video/*");
                startActivity(intent);

            }
        });
        return view;
    }

    @Override
    public void initData() {
        super.initData();
//        textView.setText("本地视频的内容");
        getDate();
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(mediaItems != null && mediaItems.size() > 0) {
                tv_nodata.setVisibility(View.GONE);
                adapter = new LocalVideoAdapter(context,mediaItems);
                lv.setAdapter(adapter);
            }else {
                tv_nodata.setVisibility(View.VISIBLE);
            }
        }
    };
    private void getDate(){
        new Thread(){
            public void run(){
                mediaItems = new ArrayList<MediaItem>();
                ContentResolver resolver = context.getContentResolver();
                Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                String[] objs = {
                        MediaStore.Video.Media.DISPLAY_NAME,//视频在sdcard上的名称
                        MediaStore.Video.Media.DURATION,//视频时长
                        MediaStore.Video.Media.SIZE,//视频文件的大小
                        MediaStore.Video.Media.DATA//视频播放地址
                };
                Cursor cursor = resolver.query(uri,objs,null,null,null);
                if(cursor != null) {
                    while(cursor.moveToNext()) {
                        String name = cursor.getString(cursor.getColumnIndex( MediaStore.Video.Media.DISPLAY_NAME));
                        long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                        long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                        String data = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));

                        mediaItems.add(new MediaItem(name,duration,size,data));
                        handler.sendEmptyMessage(0);
                    }
                    cursor.close();
                }
            }
        }.start();
    }
}
