package homeautomation.circularblue.com.iit;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;

import static homeautomation.circularblue.com.iit.MainActivity.REQUEST_VIDEO_CAPTURE;

public class media_capture extends AppCompatActivity {

    Button photo_button,video_button;
    public static int count = 0;
    int TAKE_PHOTO_CODE = 0;
    VideoView result_video;
    String path="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_capture);
        init_cam();
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
                String file = dir+count+".jpg";
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
                dispatchTakeVideoIntent(view);
            }
        });
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
    }
}
