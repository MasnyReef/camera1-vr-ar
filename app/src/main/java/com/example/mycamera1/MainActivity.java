package com.example.mycamera1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraActivity;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends CameraActivity {

    CameraBridgeViewBase cameraBridgeViewBase;
    Mat mRGBA, mRGBAT;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ustawienie fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        getPermission();

        cameraBridgeViewBase = findViewById(R.id.my_camera_view);

        cameraBridgeViewBase.setCvCameraViewListener(new CameraBridgeViewBase.CvCameraViewListener2() {
            @Override
            public void onCameraViewStarted(int width, int height) {

            }

            @Override
            public void onCameraViewStopped() {

            }

            @Override
            public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
                mRGBA = inputFrame.rgba();
//                mRGBAT = mRGBA.t();
//                Core.flip(mRGBA.t(), mRGBAT, 1);
//                Imgproc.resize(mRGBAT, mRGBAT, mRGBA.size());
//                return mRGBAT;
                return mRGBA;
            }
        });

        if(OpenCVLoader.initDebug()){
            cameraBridgeViewBase.enableView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraBridgeViewBase.enableView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraBridgeViewBase.disableView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraBridgeViewBase.disableView();
    }

    @Override
    protected List<? extends CameraBridgeViewBase> getCameraViewList() {
        return Collections.singletonList(cameraBridgeViewBase);
    }

    void getPermission(){
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},101);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0]!=PackageManager.PERMISSION_GRANTED){
            getPermission();
        }
    }
}


//public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
//
//
//    JavaCameraView javaCameraView;
//    Mat mRGBA, mRGBAT;
//    private static final int REQUEST_CAMERA_PERMISSION = 200;
//
//
//    BaseLoaderCallback baseLoaderCallback = new BaseLoaderCallback(MainActivity.this) {
//        @Override
//        public void onManagerConnected(int status) {
//            switch (status)
//            {
//                case LoaderCallbackInterface.SUCCESS:
//                {
//                    javaCameraView.enableView();
//                    break;
//                }
//                default:
//                {
//                    super.onManagerConnected(status);
//                    break;
//                }
//
//            }
//
//        }
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
//        } else {
//            javaCameraView = (JavaCameraView) findViewById(R.id.my_camera_view);
//            javaCameraView.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_BACK);
//            javaCameraView.setVisibility(CameraBridgeViewBase.VISIBLE);
//            javaCameraView.setCvCameraViewListener(MainActivity.this);
//        }
//    }
//
//
//    @Override
//    public void onCameraViewStarted(int width, int height) {
//        //mRGBA = new Mat(height, width, CvType.CV_8UC4);
//    }
//
//    @Override
//    public void onCameraViewStopped() {
//
//        //mRGBA.release();
//    }
//
//    @Override
//    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
//        mRGBA = inputFrame.rgba();
////        mRGBAT = mRGBA.t();
////        Core.flip(mRGBA.t(), mRGBAT, 1);
////        Imgproc.resize(mRGBAT, mRGBAT, mRGBA.size());
//        return mRGBAT;
//    }
//
//    @Override
//    public void onPointerCaptureChanged(boolean hasCapture) {
//
//    }
//
//    @Override
//    protected void onDestroy(){
//        super.onDestroy();
//
//        if(javaCameraView!=null){
//            javaCameraView.disableView();
//        }
//    }
//
//
//    @Override
//    protected void onPause(){
//        super.onPause();
//
//        if(javaCameraView!=null){
//            javaCameraView.disableView();
//        }
//    }
//
//    @Override
//    protected void onResume(){
//        super.onResume();
//
//        if (!OpenCVLoader.initDebug()) {
//            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, baseLoaderCallback);
//        } else {
//            baseLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
//        }
//
//    }
//}