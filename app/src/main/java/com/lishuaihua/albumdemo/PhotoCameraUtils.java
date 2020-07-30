package com.lishuaihua.albumdemo;

import android.app.Activity;
import android.os.Environment;

import androidx.annotation.NonNull;

import com.lishuaihua.album.Action;
import com.lishuaihua.album.Album;
import com.lishuaihua.album.AlbumFile;
import com.lishuaihua.album.api.widget.Widget;
import com.lishuaihua.durban.Controller;
import com.lishuaihua.durban.Durban;

import java.io.File;
import java.util.ArrayList;


public class PhotoCameraUtils {
    public static void cameraImage(final Activity context, final int type) {
        Album.camera(context) // 相机功能。
                .image() // 拍照。
                .filePath("") // 文件保存路径，非必须。
                .requestCode(AppConstants.CAMERA_IMAGE)
                .onResult(new Action<String>() {
                    @Override
                    public void onAction(int requestCode, @NonNull String result) {
                        durbanList.clear();
                        durbanList.add(result);
                        String cropDirectory = Utils.getAppRootPath(context).getAbsolutePath();
                        File filePhoto;
                        if (FileUtils.sdCardExist()) {
                            filePhoto = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), FileUtils.ROOT_STORAGE_PATH);
                        } else {
                            filePhoto = new File(Environment.getRootDirectory().getAbsoluteFile(), FileUtils.ROOT_STORAGE_PATH);
                        }
                        if (filePhoto != null && !filePhoto.exists()) {
                            filePhoto.mkdirs();
                        }
                        if (FileUtils.sdCardExist()) {
                            cropDirectory = filePhoto.getAbsolutePath();
                        } else {
                            cropDirectory = filePhoto.getAbsolutePath();
                        }
                        Durban.with(context)
                                // 裁剪界面的标题。
                                .title("剪切图片")
                                // 图片路径list或者数组。
                                .inputImagePaths(durbanList)
                                // 图片输出文件夹路径。
                                .outputDirectory(cropDirectory)
                                // 裁剪图片输出的最大宽高。
                                .maxWidthHeight(type == 1 ? 500 : ((type == 2 || type == 3) ? 1200 : 500), type == 1 ? 500 : ((type == 4) ? 480 : (type == 2 || type == 3) ? 900 : 500))
                                // 裁剪时的宽高比。
                                .aspectRatio(type == 1 ? 1 : ((type == 4) ? 5 : (type == 2 || type == 3) ? 4 : 1), type == 1 ? 1 : ((type == 4) ? 2 : (type == 2 || type == 3) ? 3 : 1))
                                // 图片压缩格式：JPEG、PNG。
                                .compressFormat(Durban.COMPRESS_JPEG)
                                // 图片压缩质量，请参考：Bitmap#compress(Bitmap.CompressFormat, int, OutputStream)
                                .compressQuality(90)
                                // 裁剪时的手势支持：ROTATE, SCALE, ALL, NONE.
                                .gesture(Durban.GESTURE_SCALE)
                                .controller(
                                        Controller.newBuilder()
                                                .enable(false) // 是否开启控制面板。
                                                .rotation(true) // 是否有旋转按钮。
                                                .rotationTitle(true) // 旋转控制按钮上面的标题。
                                                .scale(true) // 是否有缩放按钮。
                                                .scaleTitle(true) // 缩放控制按钮上面的标题。
                                                .build()) // 创建控制面板配置。
                                .requestCode(type == 1 ? AppConstants.LOCAL_IMAGE : (type == 2 || type == 4 ? AppConstants.LOCAL_IMAGE_FONT : (type == 3 ? AppConstants.LOCAL_IMAGE_BACKGROUNT : AppConstants.LOCAL_IMAGE)))
                                .start();
                    }
                })
                .onCancel(new Action<String>() {
                    @Override
                    public void onAction(int requestCode, @NonNull String result) {
                    }
                })
                .start();
    }

    /**
     * Select picture, from album.
     */
    private static ArrayList<String> durbanList = new ArrayList<>();

    /**
     * @param context
     * @param isLocalImage 是否是加载本地照片库
     */
    public static void selectImage(final Activity context, final boolean isLocalImage, final int type) {
        Album.image(context)
                .singleChoice()
                .requestCode(AppConstants.PHOTO_IMAGE)
                .camera(true)
                .columnCount(2)
                .widget(
                        Widget.newDarkBuilder(context)
                                .title("选择图片")
                                .build()
                )
                .onResult(new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(int requestCode, @NonNull final ArrayList<AlbumFile> result) {
                        if (result != null && result.size() > 0) {
                            if (isLocalImage) {
                                durbanList.clear();
                                for (AlbumFile albumFile : result) {
                                    durbanList.add(albumFile.getPath());
                                }
                                String cropDirectory = Utils.getAppRootPath(context).getAbsolutePath();
                                File filePhoto;
                                if (FileUtils.sdCardExist()) {
                                    filePhoto = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), FileUtils.ROOT_STORAGE_PATH);
                                } else {
                                    filePhoto = new File(Environment.getRootDirectory().getAbsoluteFile(), FileUtils.ROOT_STORAGE_PATH);
                                }
                                if (filePhoto != null && !filePhoto.exists()) {
                                    filePhoto.mkdirs();
                                }
                                if (FileUtils.sdCardExist()) {
                                    cropDirectory = filePhoto.getAbsolutePath();
                                } else {
                                    cropDirectory = filePhoto.getAbsolutePath();
                                }
                                Durban.with(context)
                                        .statusBarColor(context.getResources().getColor(com.lishuaihua.album.R.color.ColorPrimary))
                                        .toolBarColor(context.getResources().getColor(com.lishuaihua.album.R.color.ColorPrimaryDark))
//                                        .navigationBarColor(context.getResources().getColor(com.yanzhenjie.album.R.color.FontLight))
                                        // 裁剪界面的标题。
                                        .title("剪切图片")
                                        // 图片路径list或者数组。
                                        .inputImagePaths(durbanList)
                                        // 图片输出文件夹路径。
                                        .outputDirectory(cropDirectory)
                                        // 裁剪图片输出的最大宽高。
                                        .maxWidthHeight(type == 1 ? 500 : ((type == 2 || type == 3) ? 1200 : 500), type == 1 ? 500 : ((type == 4) ? 480 : (type == 2 || type == 3) ? 900 : 500))
                                        // 裁剪时的宽高比。
                                        .aspectRatio(type == 1 ? 1 : ((type == 4) ? 5 : (type == 2 || type == 3) ? 4 : 1), type == 1 ? 1 : ((type == 4) ? 2 : (type == 2 || type == 3) ? 3 : 1))
                                        // 图片压缩格式：JPEG、PNG。
                                        .compressFormat(Durban.COMPRESS_JPEG)
                                        // 图片压缩质量，请参考：Bitmap#compress(Bitmap.CompressFormat, int, OutputStream)
                                        .compressQuality(90)
                                        // 裁剪时的手势支持：ROTATE, SCALE, ALL, NONE.
                                        .gesture(Durban.GESTURE_SCALE)
                                        .controller(
                                                Controller.newBuilder()
                                                        .enable(false) // 是否开启控制面板。
                                                        .rotation(true) // 是否有旋转按钮。
                                                        .rotationTitle(true) // 旋转控制按钮上面的标题。
                                                        .scale(true) // 是否有缩放按钮。
                                                        .scaleTitle(true) // 缩放控制按钮上面的标题。
                                                        .build()) // 创建控制面板配置。
                                        .requestCode(type == 1 ? AppConstants.LOCAL_IMAGE : (type == 2 || type == 4 ? AppConstants.LOCAL_IMAGE_FONT : (type == 3 ? AppConstants.LOCAL_IMAGE_BACKGROUNT : AppConstants.LOCAL_IMAGE)))
                                        .start();
                            }
                        }
                    }
                })
                .onCancel(new Action<String>() {
                    @Override
                    public void onAction(int requestCode, @NonNull String result) {
                    }
                })
                .start();
    }
}
