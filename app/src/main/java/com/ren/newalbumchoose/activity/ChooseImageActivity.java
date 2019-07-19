package com.ren.newalbumchoose.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ren.newalbumchoose.R;
import com.ren.newalbumchoose.adapter.ChooseImageMyAdapter;
import com.ren.newalbumchoose.adapter.PopDirListAdapter;
import com.ren.newalbumchoose.bean.ImageBean;
import com.ren.newalbumchoose.bean.ImageFolder;
import com.ren.newalbumchoose.utils.PhotoUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ChooseImageActivity extends AppCompatActivity {
    private GridView choose_image_gridview;
    private TextView choose_image_title_text;
    private FrameLayout choose_image_title_frame;
    private Map<String, ImageFolder> mFolderMap;
    private ChooseImageMyAdapter myAdapter;
//    private ChoosePicAdapter myAdapter;
    private List<ImageBean> mPhotoLists = new ArrayList<>();
    private final static String ALL_PHOTO = "所有图片";
    private final static String ALL_VIDEO = "所有视频";
    private final static String ALL_PHOTO_AND_VIDEO = "所有图片和视频";

    private PopupWindow mListImageDirPopupWindow;
    private List<ImageFolder> ImageFolders;

    private int SHOW_MODE;
    private boolean isSingleChoose = false;//是否单选。默认为false
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_image);
        initIntentData();
        findViewById();
        getPhotosTask.execute();
    }

    private void initIntentData() {
        SHOW_MODE = getIntent().getIntExtra("showVideo",0);
        isSingleChoose = getIntent().getBooleanExtra("singleChoose",false);
    }

    private void findViewById() {
        choose_image_gridview = (GridView) findViewById(R.id.choose_image_gridview);
        choose_image_gridview.setFocusable(true);
        choose_image_title_text = (TextView) findViewById(R.id.choose_image_title_text);
        choose_image_title_frame = (FrameLayout) findViewById(R.id.choose_image_title_frame);
        btn = (Button) findViewById(R.id.btn);

        choose_image_title_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isSingleChoose){
                    myAdapter.oldImageView = null;
                    myAdapter.oldImageViewYes = null;
                    for(int i = 0;i < mPhotoLists.size();i++){
                        mPhotoLists.get(i).setChecked(false);
                    }
                }

                if(mListImageDirPopupWindow != null && mListImageDirPopupWindow.isShowing()){
                    mListImageDirPopupWindow.dismiss();
                }else{
                    myAdapter.notifyDataSetChanged();
                    showFolderPopopWindow();
                }
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> picPath = myAdapter.getImagePath();
                for(int i = 0;i < picPath.size();i++){
                    Log.i("xxx","path   " + picPath.get(i));
                }
            }
        });
    }

    /**
     * 获取照片的异步任务
     */
    private AsyncTask getPhotosTask = new AsyncTask() {
        @Override
        protected void onPreExecute() {
//            mProgressDialog = ProgressDialog.show(PhotoPickerActivity.this, null, "loading...");
        }

        @Override
        protected Object doInBackground(Object[] params) {
            if(SHOW_MODE == 0){//显示全部
                mFolderMap = PhotoUtil.getPhotosAndVideos(ChooseImageActivity.this.getApplicationContext());
            }else if(SHOW_MODE == 1){//仅图片
                mFolderMap = PhotoUtil.getPhotos(ChooseImageActivity.this.getApplicationContext());
            }else if(SHOW_MODE == 2){//仅视频
                mFolderMap = PhotoUtil.getVideos(ChooseImageActivity.this.getApplicationContext());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            getPhotosSuccess();
        }
    };

    /**
     * 加载照片成功
     */
    private void getPhotosSuccess() {
        if(SHOW_MODE == 0){//图片和视频
            choose_image_title_text.setText(ALL_PHOTO_AND_VIDEO);
            mPhotoLists.addAll(mFolderMap.get(ALL_PHOTO_AND_VIDEO).getPhotoList());
        }else if(SHOW_MODE == 1){//仅图片
            choose_image_title_text.setText(ALL_PHOTO);
            mPhotoLists.addAll(mFolderMap.get(ALL_PHOTO).getPhotoList());
        }else if(SHOW_MODE == 2){//仅视频
            choose_image_title_text.setText(ALL_VIDEO);
            mPhotoLists.addAll(mFolderMap.get(ALL_VIDEO).getPhotoList());
        }


        myAdapter = new ChooseImageMyAdapter(this.getApplicationContext(), mPhotoLists,isSingleChoose);
        choose_image_gridview.setAdapter(myAdapter);

        Set<String> keys = mFolderMap.keySet();
        final List<ImageFolder> folders = new ArrayList<>();
        for (String key : keys) {
            if (ALL_PHOTO.equals(key) || ALL_PHOTO_AND_VIDEO.equals(key) || ALL_VIDEO.equals(key)) {
                ImageFolder folder = mFolderMap.get(key);
                folder.setIsSelected(true);
                folders.add(0, folder);
            } else {
                folders.add(mFolderMap.get(key));
            }
        }
        ImageFolders = folders;

        choose_image_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ImageBean imageBean = myAdapter.getItem(position);
                if(imageBean == null) {
                    return;
                }
                String path = imageBean.getPath();
                Toast.makeText(ChooseImageActivity.this,path,Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 显示相册文件夹popupWindow
     */
    private void showFolderPopopWindow() {

        View popupWindowView = getLayoutInflater().inflate(R.layout.activity_choose_image_list_pop_dir, null);
        mListImageDirPopupWindow = new PopupWindow(popupWindowView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, false);
        mListImageDirPopupWindow.showAsDropDown(choose_image_title_frame);
        ListView dirList = (ListView) popupWindowView.findViewById(R.id.list_pop_dir);

        final PopDirListAdapter adapter = new PopDirListAdapter(this,ImageFolders);
        dirList.setAdapter(adapter);

        mListImageDirPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });

        dirList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                ImageFolder folder = ImageFolders.get(position);
                adapter.notifyDataSetChanged();
                mPhotoLists.clear();
                mPhotoLists.addAll(folder.getPhotoList());

                choose_image_gridview.setAdapter(myAdapter);
                choose_image_title_text.setText(folder.getName());
                mListImageDirPopupWindow.dismiss();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        mFolderMap.clear();
        mPhotoLists.clear();
    }
}
