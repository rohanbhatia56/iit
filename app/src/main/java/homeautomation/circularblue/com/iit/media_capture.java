package homeautomation.circularblue.com.iit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iceteck.silicompressorr.SiliCompressor;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
import static homeautomation.circularblue.com.iit.MainActivity.REQUEST_VIDEO_CAPTURE;

public class media_capture extends AppCompatActivity {
    private Uri fileUri;
    public static String path_video;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    //  public static VideocameraActivity ActivityContext =null;
    Button photo_button,video_button;
    public static int count = 0;
    int TAKE_PHOTO_CODE = 0;
    VideoView result_video;
    String path="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_capture);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle(Singleton.getInstance().getUsername());
        setSupportActionBar(myToolbar);
        init_cam();
    }
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                new AlertDialog.Builder(this)
                        .setTitle("Closing application")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Singleton.getInstance().logout();
                                Intent  intent = new Intent(media_capture.this,LoginScreen.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                            }
                        }).setNegativeButton("No", null).show();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    //Initialize camera
    public void init_cam(){

        // pics taken by the camera using this application.
        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/";
        File newdir = new File(dir);
        newdir.mkdirs();

        Button capture = (Button) findViewById(R.id.photo_button);
        capture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Here, the counter will be incremented each time, and the
                // picture taken by camera will be stored as 1.jpg,2.jpg
                // and likewise.
                //count++;
                long millis = System.currentTimeMillis();
                String file = dir+millis+".jpg";
                File newfile = new File(file);
                path = file;
                try {
                    newfile.createNewFile();
                }
                catch (IOException e) {

                }
                Uri outputFileUri = Uri.fromFile(newfile);
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
            }
        });

        Button capture_video = (Button) findViewById(R.id.video_button);
        capture_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                dispatchTakeVideoIntent(view);
                startRecording();
            }
        });
    }

    private void startRecording(){
        // create new Intentwith with Standard Intent action that can be
        // sent to have the camera application capture an video and return it.
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        // create a file to save the video
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);

        // set the image file name
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // set the video image quality to high
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

        // start the Video Capture Intent
        startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
    }


    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){

        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){


        // Check that the SDCard is mounted
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyCameraVideo");


        // Create the storage directory(MyCameraVideo) if it does not exist
        if (! mediaStorageDir.exists()){

            if (! mediaStorageDir.mkdirs()){

//                output.setText("Failed to create directory MyCameraVideo.");
                Log.d("debug","Failed to create directory MyCameraVideo.");
                Log.d("debug", "Failed to create directory MyCameraVideo.");
                return null;
            }
        }


        // Create a media file name

        // For unique file name appending current timeStamp with file name
        java.util.Date date= new java.util.Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(date.getTime());

        File mediaFile;

        if(type == MEDIA_TYPE_VIDEO) {

            // For unique video file name appending current timeStamp with file name
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + "ROH.mp4");
            path_video = mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + "ROH.mp4";

        } else {
            return null;
        }

        return mediaFile;
    }

    public void dispatchTakeVideoIntent(View v) {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
//            Log.d("CameraDemo", "Pic saved");
            Toast.makeText(media_capture.this, "Pic saved", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(media_capture.this,Image_view.class);
            intent.putExtra("path",path);
            startActivity(intent);
        }
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = data.getData();
            Singleton.getInstance().addVideoURI(videoUri);
//            Singleton.getInstance().addVideoPath(videoUri.getPath());
            Intent intent = new Intent(media_capture.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                //output.setText("Video File : " +data.getData());
                //Log.d("debug",data.getData().getPath());
                // Video captured and saved to fileUri specified in the Intent
                // Toast.makeText(this, "Video saved to: " + data.getData(), Toast.LENGTH_LONG).show();
                Log.d("debug","video save successfully");
                Log.d("debug",path_video.toString());
                Singleton.getInstance().addVideoURI(Uri.parse(path_video));
                Singleton.getInstance().setVideoURL(path_video);
//                Log.d("debug","PATH =" + data.getData().toString());

                new VideoCompressAsyncTask(this).execute(Uri.fromFile(new File(path_video)).toString(),Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath());

//                try {
//                    String filePath = SiliCompressor.with(media_capture.this).compressVideo(path_video,Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath());
//                } catch (URISyntaxException e) {
//                    e.printStackTrace();
//                }


            } else if (resultCode == RESULT_CANCELED) {

//                    output.setText("User cancelled the video capture.");

                // User cancelled the video capture
//                    Toast.makeText(this, "User cancelled the video capture.",
//                            Toast.LENGTH_LONG).show();
                Log.d("debug","User cancelled the video capture." );


            } else {

//                    output.setText("Video capture failed.");
//
//                    // Video capture failed, advise user
//                    Toast.makeText(this, "Video capture failed.",
//                            Toast.LENGTH_LONG).show();
                Log.d("debug","Video capture failed." );

            }
        }
    }
    void goHome(String compressedFilePath){
        Singleton.getInstance().addVideoPath(compressedFilePath);
        Intent intent = new Intent(media_capture.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    class VideoCompressAsyncTask extends AsyncTask<String, String, String> {

        Context mContext;

        public VideoCompressAsyncTask(Context context){
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//        imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_photo_camera_white_48px));
//        compressionMsg.setVisibility(View.VISIBLE);
//        picDescription.setVisibility(View.GONE);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Singleton.getInstance().showProgressDialog(media_capture.this,"Compressing, Please wait.........");
                }
            });
        }

        @Override
        protected String doInBackground(String... paths) {
            String filePath = null;
            try {

                filePath = SiliCompressor.with(mContext).compressVideo(Uri.parse(paths[0]), paths[1]);

            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            return  filePath;
        }
//7.67MB ->409MB
        @Override
        protected void onPostExecute(String compressedFilePath) {
            super.onPostExecute(compressedFilePath);
            File imageFile = new File(compressedFilePath);
            float length = imageFile.length() / 1024f; // Size in KB
            String value;
            if(length >= 1024)
                value = length/1024f+" MB";
            else
                value = length+" KB";
//        String text = String.format(Locale.US, "%s\nName: %s\nSize: %s",  getString(R.string.video_compression_complete), imageFile.getName(), value);
//        compressionMsg.setVisibility(View.GONE);
//        picDescription.setVisibility(View.VISIBLE);
//        picDescription.setText(text);
            Log.d("debug", "Path: "+compressedFilePath);
            upload(compressedFilePath.trim());
        }
    }
    public void upload(final String compressedFilePath){

        Uri file = Uri.fromFile(new File(compressedFilePath));
        long millis = System.currentTimeMillis();

        StorageReference riversRef = Singleton.getInstance().getStorageRef().child(Singleton.getInstance().getUsername()+"/"+millis+"_VID.mp4");

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Singleton.getInstance().dissmissProgressDialog();
                            }
                        });
                        goHome(compressedFilePath);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Singleton.getInstance().dissmissProgressDialog();
                            }
                        });

                        Toast.makeText(media_capture.this, "Upload failed", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        final double progresss = (taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount())*100.0;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Singleton.getInstance().setProgressDialogMessage("Uploading video, Please wait.........");
                                //((int)progresss) +"% Uploaded..."
                            }
                        });
                    }
                });

    }
}

