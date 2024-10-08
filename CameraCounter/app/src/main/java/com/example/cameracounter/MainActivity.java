package com.example.cameracounter;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.objects.DetectedObject;
import com.google.mlkit.vision.objects.ObjectDetection;
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions;
import com.google.mlkit.vision.objects.ObjectDetector;

public class MainActivity extends AppCompatActivity {


    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private PreviewView previewView;
    private ExecutorService cameraExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        previewView = findViewById(R.id.cameraPreview);

        cameraExecutor = Executors.newSingleThreadExecutor();

        // Check for camera permissions before starting the camera
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            // Request camera permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        }
    }




    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (Exception e) {
                Log.e("CameraXapp", "Camera binding failed", e);
            }
        }, ContextCompat.getMainExecutor(this));
    }


    private void bindPreview(@NonNull ProcessCameraProvider cameraProvider){
        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK).build();

// Configure ImageAnalysis without tarkot aspect ration/resolution
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                //handle rotation dynamiclly insted of setTartget
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();


       imageAnalysis.setAnalyzer(cameraExecutor, imageProxy -> {
           processImageProxy(imageProxy);
       });

       preview.setSurfaceProvider(previewView.getSurfaceProvider());
       cameraProvider.bindToLifecycle(this,cameraSelector,preview,imageAnalysis);





    }

    private  void processImageProxy(ImageProxy imageProxy){

        // Image process logic


        Image mediaImage = imageProxy.getImage();
        if(mediaImage != null){
            InputImage image = InputImage.fromMediaImage(mediaImage,imageProxy.getImageInfo().getRotationDegrees());


            ObjectDetectorOptions options =
                   new ObjectDetectorOptions.Builder()
                           .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
                           .enableMultipleObjects()
                           .enableClassification()
                           .build();


            ObjectDetector objectDetector = ObjectDetection.getClient(options);

            objectDetector.process(image)
                    .addOnSuccessListener(detectedObjects -> {
                            for(DetectedObject detectedObject : detectedObjects) {
                //detected objects user bounding boxes / labels
                int trackingId = detectedObject.getTrackingId();
                List<DetectedObject.Label> labels = detectedObject.getLabels();


                //Count detected objects
                // you can track objects


            }

            }).addOnFailureListener(e -> {
                //handle later the failer
                    }).addOnCompleteListener(task -> imageProxy.close());



        }


        imageProxy.close();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,@NonNull String[] permissions,@NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);


        if(requestCode == CAMERA_PERMISSION_REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startCamera();
            }else {
                Log.e("CameraXapp","Camera permission denied");
            }
        }

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        cameraExecutor.shutdown();
    }


}