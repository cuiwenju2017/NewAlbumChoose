package com.ren.newalbumchoose.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.ren.newalbumchoose.R;
import com.ren.newalbumchoose.bean.ImageBean;

import java.util.ArrayList;
import java.util.List;


/**
 * 用于适配每个相册GridView的adapter
 * Created by win10 on 2017/6/19.
 */

public class ChooseImageMyAdapter extends BaseAdapter {
    private Context context;
    private List<ImageBean> mPhotoLists;
    private boolean isSingChoose;//是否单选
    private List<String> imagePathList = new ArrayList<>();//用于存放选中的图片的路径的list

    public ImageView oldImageView;
    public ImageView oldImageViewYes;
    public  int oldPostion;

    public ChooseImageMyAdapter(Context context,List<ImageBean> mPhotoLists, boolean isSingChoose){
        this.context = context;
        this.mPhotoLists = mPhotoLists;
        this.isSingChoose = isSingChoose;
    }

    @Override
    public int getCount() {
        return mPhotoLists.size();
    }

    @Override
    public ImageBean getItem(int position) {
        if(mPhotoLists == null || mPhotoLists.size() == 0){
            return null;
        }
        return mPhotoLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        Log.i("vvvv","positon   " + position);

        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_choose_image_grid_item,parent,false);
            holder.choose_item_image = (ImageView) convertView.findViewById(R.id.choose_item_image);
            holder.choose_item_imagePlayer = (ImageView) convertView.findViewById(R.id.choose_item_imagePlayer);
            holder.choose_item_select = (ImageView) convertView.findViewById(R.id.choose_item_select);
            holder.rl_root_grid_item = (RelativeLayout) convertView.findViewById(R.id.rl_root_grid_item);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(context).load(mPhotoLists.get(position).getPath()).into(holder.choose_item_image);

        if(mPhotoLists.get(position).isVideo()){
            holder.choose_item_imagePlayer.setVisibility(View.VISIBLE);
        }else{
            holder.choose_item_imagePlayer.setVisibility(View.GONE);
        }

        if(mPhotoLists.get(position).isChecked()){
            holder.choose_item_select.setImageResource(R.mipmap.choose_image_yes);
            holder.choose_item_image.setColorFilter(Color.parseColor("#77000000"));
        }else{
            holder.choose_item_select.setImageResource(R.mipmap.choose_image_no);
            holder.choose_item_image.setColorFilter(null);
        }

        holder.choose_item_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("cccc","position " + position);

                String imagePath = mPhotoLists.get(position).getPath();
                if(isSingChoose){//图片单选
                    notifyDataSetChanged();
                    if(oldImageViewYes != null && oldImageView != null){
                        mPhotoLists.get(oldPostion).setChecked(false);
                    }
                    imagePathList.clear();
                    imagePathList.add(imagePath);

                    mPhotoLists.get(position).setChecked(true);
                    oldImageView = holder.choose_item_image;
                    oldImageViewYes = holder.choose_item_select;
                    oldPostion = position;
                }else{//图片多选
                    notifyDataSetChanged();
                    if(imagePathList.contains(imagePath)){
                        holder.choose_item_select.setImageResource(R.mipmap.choose_image_no);
                        holder.choose_item_image.setColorFilter(null);
                        imagePathList.remove(imagePath);
                        mPhotoLists.get(position).setChecked(false);
                    }else{
                        mPhotoLists.get(position).setChecked(true);
                        imagePathList.add(imagePath);
                        holder.choose_item_select.setImageResource(R.mipmap.choose_image_yes);
                        holder.choose_item_image.setColorFilter(Color.parseColor("#77000000"));
                        oldPostion = position;
                    }
                }
            }
        });

        return convertView;
    }

    class ViewHolder{
        ImageView choose_item_image,choose_item_imagePlayer;
        ImageView choose_item_select;
        RelativeLayout rl_root_grid_item;
    }

    public List<String> getImagePath() {
        List<String> imagePaths = new ArrayList<>();
        imagePaths.addAll(imagePathList);
        return imagePaths;
    }
}
