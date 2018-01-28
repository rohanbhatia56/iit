package homeautomation.circularblue.com.iit;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iceteck.silicompressorr.SiliCompressor;

import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Locale;


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
               // new GlueFilesTask().execute();
                startImageComression(singleton.getImagePath());
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
    void goHome(String compressedFilePath){
        Singleton.getInstance().addVideoPath(compressedFilePath);
        Intent intent = new Intent(Record_Audio.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
//        finish();

    }
    void startVideoCompression(String path){
        new VideoCompressAsyncTask(this).execute(Uri.fromFile(new File(path)).toString(),Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath());
    }
    void startImageComression(String path){
        new ImageCompressionAsyncTask(this).execute(Uri.fromFile(new File(path)).toString(),Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath()) ;
    }
    void startGlueTask(){
        new GlueFilesTask().execute();
    }

    public class GlueFilesTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;
        FFmpegFrameRecorder recorder;
        private String videoPath;
        Double frameRate=30.0;
        long startTime;
        String s;
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

                s = path + "/" + "test_sham_"+millis+".mp4";
                Log.d("debug","S = " + s);
                recorder = new FFmpegFrameRecorder(s,  grabber1.getImageWidth(), grabber1.getImageHeight(),2);

                //recorder.setVideoCodec(5);
              //  recorder.setVideoCodec(avcodec.AV_CODEC_ID_AMR_NB);
//                grabber.setVideoCodec(avcodec.AV_CODEC_ID_H264);


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

     //           singleton.addVideoPath(s,singleton.getImagePath());
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //startFileCompression(s);
            goHome(s);
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
            goHome(compressedFilePath.trim());
        }
    }
    class ImageCompressionAsyncTask extends AsyncTask<String, Void, String>{

        Context mContext;

        public ImageCompressionAsyncTask(Context context){
            mContext = context;
        }

        @Override
        protected String doInBackground(String... params) {

            String filePath = SiliCompressor.with(mContext).compress(params[0], new File(params[1]));
            return filePath;


            /*
            Bitmap compressBitMap = null;
            try {
                compressBitMap = SiliCompressor.with(mContext).getCompressBitmap(params[0], true);
                return compressBitMap;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return compressBitMap;
            */
        }

        @Override
        protected void onPostExecute(String s) {
            /*
            if (null != s){
                imageView.setImageBitmap(s);
                int compressHieght = s.getHeight();
                int compressWidth = s.getWidth();
                float length = s.getByteCount() / 1024f; // Size in KB;
                String text = String.format("Name: %s\nSize: %fKB\nWidth: %d\nHeight: %d", "ff", length, compressWidth, compressHieght);
                picDescription.setVisibility(View.VISIBLE);
                picDescription.setText(text);
            }
            */

            File imageFile = new File(s);
            Uri compressUri = Uri.fromFile(imageFile);

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), compressUri);
//                imageView.setImageBitmap(bitmap);

                String name = imageFile.getName();
                float length = imageFile.length() / 1024f; // Size in KB
                int compressWidth = bitmap.getWidth();
                int compressHieght = bitmap.getHeight();
                String text = String.format(Locale.US, "Name: %s\nSize: %fKB\nWidth: %d\nHeight: %d", name, length, compressWidth, compressHieght);
//                picDescription.setVisibility(View.VISIBLE);
//                picDescription.setText(text);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            singleton.setImagePath(s);
            startGlueTask();
        }
    }


}

