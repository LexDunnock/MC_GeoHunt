
package com.app.felix.geocachehuntv5;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ImageButton;
import java.util.Arrays;

/**
 * The camera activity
 */
public class CameraActivity extends AppCompatActivity {

    private static final String TAG = "CameraActivity";
    private ImageButton imgButtonTreasure;
    private TextureView textureView;

    private String cameraId;
    protected CameraDevice cameraDevice;
    protected CameraCaptureSession cameraCaptureSessions;
    protected CaptureRequest captureRequest;
    protected CaptureRequest.Builder captureRequestBuilder;
    private Size imageDimension;
    private ImageReader imageReader;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private Handler mBackgroundHandler;

    /**
     * Creating the camera with asking for permission
     * @param savedInstanceState acutal state of the camera
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_activity);

        textureView = (TextureView) findViewById(R.id.texture);
        assert textureView != null;
        textureView.setSurfaceTextureListener(textureListener);

        imgButtonTreasure = (ImageButton) findViewById(R.id.treasureButton);
        assert imgButtonTreasure != null;
        imgButtonTreasure.setOnClickListener(new View.OnClickListener() {
            /**
             * By clicking on the imagebutton the user collects a marker
             * @param v the acutal view
             */
            @Override
            public void onClick(View v) {
                imgButtonTreasure.setVisibility(View.INVISIBLE);
                PlayerClass.setPlayerScore(getApplicationContext(), 100);
                PlayerClass.addMarker(getApplicationContext(), PlayerClass.getCurrentMarker(getApplicationContext()));
                Toast.makeText(getApplicationContext(), "Added 100 Coins", Toast.LENGTH_SHORT).show();
                CameraActivity.this.finish();
            }
        });
    }

    /**
     *
     */
    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {

        /**
         * The method start if a view can be used.
         *
         * @param surface size of the area
         * @param width of the surface
         * @param height of the surface
         */
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            openCamera();
        }

        /**
         * Buffersize has changed.
         *
         * @param surface size of the area
         * @param width of the surface
         * @param height of the surface
         */
        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        }

        /**
         * The function says that the surface will be destroyed.
         *
         * @param surface actual surface
         * @return bool value
         */
        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        /**
         * This method update the surface to current view
         *
         * @param surface is updating
         */
        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    };

    /**
     *  The camera get receiving updates and with a new instance the camera device
     *  opens.
     */
    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {

        /**
         * Method called when a camera has finished opening
         *
         * @param camera gives the device
         */
        @Override
        public void onOpened(CameraDevice camera) {
            Log.e(TAG, "onOpened");
            cameraDevice = camera;
            createCameraPreview();
        }

        /**
         * disconnection of the camera, not longer available.
         *
         * @param camera
         */
        @Override
        public void onDisconnected(CameraDevice camera) {
            cameraDevice.close();
        }

        /**
         * set an error at the camera
         *
         * @param camera
         * @param error
         */
        @Override
        public void onError(CameraDevice camera, int error) {
            cameraDevice.close();
            cameraDevice = null;
        }
    };

    /**
     * This method shows the camera preview. It is creating the session.
     */
    protected void createCameraPreview() {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());
            Surface surface = new Surface(texture);
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback(){

                /**
                 * That method configures the camera
                 *
                 * @param cameraCaptureSession actual session
                 */
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    //The camera is already closed
                    if (null == cameraDevice) {
                        return;
                    }
                    // When the session is ready to start displaying the preview.
                    cameraCaptureSessions = cameraCaptureSession;
                    updatePreview();
                }

                /**
                 * new camera configuration
                 *
                 * @param cameraCaptureSession
                 */
                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(CameraActivity.this, "Configuration change", Toast.LENGTH_SHORT).show();
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     *  The function opens the camera. Checks characteristics and the surface. If the user gives
     *  the permission, the camera will start.
     */
    private void openCamera() {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        Log.e(TAG, "is camera open");
        try {
            cameraId = manager.getCameraIdList()[0];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;
            imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];
            // Add permission for camera and let user grant the permission
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(CameraActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
                return;
            }
            manager.openCamera(cameraId, stateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "openCamera X");
    }

    /**
     *   This method updating the camera preview.
     *   Checking for any errors.
     */
    protected void updatePreview() {
        if(null == cameraDevice) {
            Log.e(TAG, "updatePreview error, return");
        }
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        try {
            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(), null, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     *  This function closes the camera.
     *  If the device or imagereader isn't offline, they will close.
      */
    private void closeCamera() {
        if (null != cameraDevice) {
            cameraDevice.close();
            cameraDevice = null;
        }
        if (null != imageReader) {
            imageReader.close();
            imageReader = null;
        }
    }

    /**
     *  The function checks the permissions which the user access or denied
     *
     *  @param requestCode code of request
     *  @param permissions
     *  @param grantResults the result which are afford
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // close the app
                Toast.makeText(CameraActivity.this, "You can't use the app", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    /**
     * The activity goes on.
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
        if (textureView.isAvailable()) {
            openCamera();
        } else {
            textureView.setSurfaceTextureListener(textureListener);
        }
    }

    /**
     * The method is the end of the activity.
     */
    @Override
    protected void onPause() {
        Log.e(TAG, "onPause");
        closeCamera();
        super.onPause();
    }
}