package homeautomation.circularblue.com.iit;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.AppCompatButton;
//import android.widget.Button;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String videoPath;
    Double frameRate;
    long startTime;
    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    static final int REQUEST_VIDEO_CAPTURE = 1;
    //    private static String mFileName = "sound_file";
    int TAKE_PHOTO_CODE = 0;
    public static int count = 0;
    TextView novideos_textview;
    private int WRITE_EXTERNAL_STORAGE_PERMISSION_CODE = 2;
    private int CAMERA_PERMISSION_CODE = 5;

    private RecordButton mRecordButton = null;
    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;
    private PlayButton   mPlayButton = null;
    private VideoView videoView;
    private RecyclerView recyclerView;
    ProgressDialog dialog;
    Button add_button;
    Singleton singleton;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<PathsInfo> mDataset;
    private int READ_EXTERNAL_STORAGE_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle(Singleton.getInstance().getUsername());
        setSupportActionBar(myToolbar);




        singleton = Singleton.getInstance();
        add_button = (Button) findViewById(R.id.add_button);
        videoView = (VideoView) findViewById(R.id.videoView);
        novideos_textview = (TextView) findViewById(R.id.novideos_textView);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        int size= singleton.getVideoPaths().size();
     //   Toast.makeText(this, "URIS = " + size , Toast.LENGTH_SHORT).show();
        if(size==0){
            recyclerView.setVisibility(View.GONE);
            novideos_textview.setVisibility(View.VISIBLE);
        }else {
            for(int i=0;i<singleton.getVideoPaths().size();i++){
                Log.d("debug","["+i+"] "+singleton.getVideoPaths().get(i));
            }
            recyclerView.setVisibility(View.VISIBLE);
            novideos_textview.setVisibility(View.GONE);
            mDataset = singleton.getVideoPaths();
            mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mAdapter = new MainAdapter(mDataset);
            mRecyclerView.setAdapter(mAdapter);
        }


        add_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,media_capture.class);
                startActivity(intent);
            }
        });
        frameRate = 30.0;
        init_cam();
      //  Toast.makeText(this, "str", Toast.LENGTH_SHORT).show();
//        new DownloadFilesTask().execute();
    }
    protected void onStart(){
        super.onStart();
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE )== PackageManager.PERMISSION_GRANTED){
           // Toast.makeText(this, "you have already granted WRITE_EXTERNAL_STORAGE permission!", Toast.LENGTH_SHORT).show();
        }else{
            request_WRITE_EXTERNAL_STORAGE_Permission();
        }
    }
    private void request_CAMERA_Permission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)){
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("request camera Permission is needed")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA},CAMERA_PERMISSION_CODE);

                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();
        }else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},CAMERA_PERMISSION_CODE);
            Intent intent = new Intent(MainActivity.this,media_capture.class);
            startActivity(intent);

        }
    }

    private void request_WRITE_EXTERNAL_STORAGE_Permission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("write external storage Permission is needed")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_EXTERNAL_STORAGE_PERMISSION_CODE);

                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();
        }else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_EXTERNAL_STORAGE_PERMISSION_CODE);
        }
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
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Singleton.getInstance().logout();
                                Intent  intent = new Intent(MainActivity.this,LoginScreen.class);
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
    void init_player(){}


    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();
    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        File folder = Environment.getExternalStorageDirectory();
        String path = folder.getAbsolutePath() + "/data/sound";
        mPlayer = new MediaPlayer();

        try {
            mPlayer.setDataSource(path);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    private void startRecording() {
        File folder = Environment.getExternalStorageDirectory();
        String path = folder.getAbsolutePath() + "/data/sound";

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(path);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }
    class RecordButton extends  AppCompatButton {
        boolean mStartRecording = true;

        View.OnClickListener clicker = new View.OnClickListener() {
            public void onClick(View v) {
                onRecord(mStartRecording);
                if (mStartRecording) {
                    setText("Stop recording");
                } else {
                    setText("Start recording");
                }
                mStartRecording = !mStartRecording;
            }
        };

        public RecordButton(Context ctx) {
            super(ctx);
            setText("Start recording");
            setOnClickListener(clicker);
        }
    }

    class PlayButton extends AppCompatButton {
        boolean mStartPlaying = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    setText("Stop playing");
                } else {
                    setText("Start playing");
                }
                mStartPlaying = !mStartPlaying;
            }
        };

        public PlayButton(Context ctx) {
            super(ctx);
            setText("Start playing");
            setOnClickListener(clicker);
        }
    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
            Log.d("CameraDemo", "Pic saved");
        }
//        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
//            Uri videoUri = intent.getData();
//            mVideoView.setVideoURI(videoUri);
//        }
    }

    public void init_cam(){
        // setContentView(R.layout.main);
        // Here, we are making a folder named picFolder to store
        // pics taken by the camera using this application.
        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/";
        File newdir = new File(dir);
        newdir.mkdirs();
    }
    protected void onStop(){
        super.onStop();
  //    //  Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();
    //    SendLoagcatMail();
    }

    protected void onDestroy(){
        super.onDestroy();
     //   Toast.makeText(this, "DEST", Toast.LENGTH_SHORT).show();
    }

}





