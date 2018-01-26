package homeautomation.circularblue.com.iit;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;

import java.io.File;
import java.io.IOException;


enum Status
{
    NONE, RECORDING, RECODED;
}
class CurrentStatus{
    Status currentStatus = Status.NONE;
}
public class Record_Audio extends AppCompatActivity {

    LinearLayout action_panel_linearlayout;
    Button yes_button,no_button;
    TextView textView;
    CurrentStatus status;
    String path="";
    Singleton singleton;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;
    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record__audio);
        init();
    }
    void init(){

        singleton = Singleton.getInstance();
        action_panel_linearlayout = (LinearLayout) findViewById(R.id.action_panel_linear_layout);
        yes_button = (Button)  findViewById(R.id.yes_button);
        no_button = (Button) findViewById(R.id.no_button);
        textView =(TextView) findViewById(R.id.text_view);
        status= new CurrentStatus();

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (status.currentStatus){
                    case NONE:

                            if (mPlayer !=null) {
                                if(mPlayer.isPlaying()) {
                                    stopPlaying();
                                    textView.setText("CLICK HERE TO RECORD AUDIO");
                                }
                            } else {
                                status.currentStatus = Status.RECORDING;
                                textView.setText("RECORDING STARTED PLEASE SPEAK. CLICK HERE TO STOP RECORDING ");
                                startRecording();
                            }
                        break;
                    case RECODED:
                        status.currentStatus = Status.NONE;
                        textView.setText("CURRENTLY PLAYING AUDIO. CLICK TO STOP PLAYING");
                        startPlaying();
                        break;
                    case RECORDING:
                        status.currentStatus = Status.RECODED;
                        textView.setText("RECORDING FINISHED. CLICK HERE TO PLAY");
                        stopRecording();
                        break;
                };
            }
        });

        yes_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singleton.setAudioPath(path);
                new GlueFilesTask().execute();
//                Intent intent = new Intent(Record_Audio.this, MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                finish(); // call this to finish the current activity
            }
        });

        no_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
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
         path = folder.getAbsolutePath() + "/data/sound";
        mPlayer = new MediaPlayer();
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Toast.makeText(Record_Audio.this, "playing completed", Toast.LENGTH_SHORT).show();
                textView.setText("CLICK HERE TO RECORD AUDIO");
                mPlayer=null;
                mRecorder=null;
            }
        });
        try {
            mPlayer.setDataSource(path);
            mPlayer.prepare();
            mPlayer.start();
            Toast.makeText(this, "Started Playing", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.d("debug", "prepare() failed");
            Toast.makeText(this, "prepare() failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopPlaying(){
        mPlayer.release();
        mPlayer = null;
        Toast.makeText(this, "Playing stopped", Toast.LENGTH_SHORT).show();
    }

    private void startRecording() {
        File folder = Environment.getExternalStorageDirectory();
         path = folder.getAbsolutePath() + "/data/sound";

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setOutputFile(path);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.d("debug", "prepare() failed");
            Toast.makeText(this, "prepare() failed", Toast.LENGTH_SHORT).show();
        }
        mRecorder.start();
        Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        Toast.makeText(this, "Recording stopped", Toast.LENGTH_SHORT).show();
    }
    public class GlueFilesTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;
        FFmpegFrameRecorder recorder;
        private String videoPath;
        Double frameRate=30.0;
        long startTime;
        protected Void doInBackground(Void... arg0) {

            File folder = Environment.getExternalStorageDirectory();
            String path = folder.getAbsolutePath() + "/DCIM/Camera";
            Log.d("debug",path);
            // ArrayList<String> paths = (ArrayList<String>) getListOfFiles(path, "jpg");
            long millis = System.currentTimeMillis();

            videoPath = path + "/" + "test_sham_"+millis+".mp4";

            try {
                //audio grabber
//                FrameGrabber grabber2 = new FFmpegFrameGrabber(folder.getAbsolutePath()+"/Samsung/Music/Over_the_horizon.mp3");
                FrameGrabber grabber2 = new FFmpegFrameGrabber(singleton.getAudioPath());
                //video grabber

                FrameGrabber grabber1 = new FFmpegFrameGrabber(singleton.getImagePath());
                grabber1.start();
                grabber2.start();

                String s = path + "/" + "test_sham_"+millis+".mp4";
                Log.d("debug","S = " + s);
                recorder = new FFmpegFrameRecorder(s,  grabber1.getImageWidth(), grabber1.getImageHeight(),2);

                //recorder.setVideoCodec(5);
                recorder.setVideoCodec(avcodec.AV_CODEC_ID_MPEG4);
                // recorder.setVideoCodec(avcodec.AV_CODEC_ID_MP4ALS);

               // recorder.setFormat("3gp");
                recorder.setFormat("mp4");
                recorder.setFrameRate(frameRate);
                recorder.setSampleRate(grabber2.getSampleRate());
                recorder.setVideoBitrate(30);
                startTime = System.currentTimeMillis();
                recorder.start();

                Frame frame1, frame2 = null;

                while ((frame1 = grabber1.grabFrame()) != null ||

                        (frame2 = grabber2.grabFrame()) != null) {

                    recorder.record(frame1);

                    recorder.record(frame2);

                }

                recorder.stop();

                grabber1.stop();

                grabber2.stop();

//                System.out.println("Total Time:- " + recorder.getTimestamp());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Record_Audio.this, "video saved", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.d("debug","Total Time:- " + recorder.getTimestamp());
//                singleton.setFinalVideoPath(s);
                singleton.addVideoPath(s);
                Intent intent = new Intent(Record_Audio.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        protected void onPostExecute(Long result){
            dialog = new ProgressDialog(Record_Audio.this);
            dialog.setMessage("Genrating video, Please wait.........");
            dialog.setCancelable(false);
            dialog.show();
        }



        protected void onPreExecute(){
            dialog = new ProgressDialog(Record_Audio.this);
            dialog.setMessage("Genrating video, Please wait.........");
            dialog.setCancelable(false);
            dialog.show();
        };
    }

}

