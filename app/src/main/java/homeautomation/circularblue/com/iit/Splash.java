package homeautomation.circularblue.com.iit;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ContentFrameLayout;
import android.widget.Toast;

public class Splash extends AppCompatActivity {

    Singleton singleton;
    Intent intent=null;

    private int READ_EXTERNAL_STORAGE_PERMISSION_CODE = 1;
    private int WRITE_EXTERNAL_STORAGE_PERMISSION_CODE = 2;
    private int RECORD_AUDIO_PERMISSION_CODE = 3;
    private int RECORD_VIDEO_PERMISSION_CODE = 4;
    private int CAMERA_PERMISSION_CODE = 5;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        singleton =Singleton.getInstance();
        singleton.getStorage().init(this);

    }
    protected void onStart(){
        super.onStart();

        if(ContextCompat.checkSelfPermission(Splash.this, Manifest.permission.READ_EXTERNAL_STORAGE )== PackageManager.PERMISSION_GRANTED){

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    singleton.getStorage().loadFromMemory();
                    init();
                }
            }, 2000);
//            Toast.makeText(this, "you have already granted READ_EXTERNAL_STORAGE permission!", Toast.LENGTH_SHORT).show();

        }else{
            request_READ_EXTERNAL_STORAGE_Permission();
        }

//        if(ContextCompat.checkSelfPermission(Splash.this, Manifest.permission.WRITE_EXTERNAL_STORAGE )== PackageManager.PERMISSION_GRANTED){
//            Toast.makeText(this, "you have already granted WRITE_EXTERNAL_STORAGE permission!", Toast.LENGTH_SHORT).show();
//        }else{
//            request_WRITE_EXTERNAL_STORAGE_Permission();
//        }
//        if(ContextCompat.checkSelfPermission(Splash.this, Manifest.permission.RECORD_AUDIO )== PackageManager.PERMISSION_GRANTED){
//            Toast.makeText(this, "you have already granted RECORD_AUDIO permission!", Toast.LENGTH_SHORT).show();
//        }else{
//            request_RECORD_AUDIO_PERMISSION_Permission();
//        }
//        if(ContextCompat.checkSelfPermission(Splash.this, Manifest.permission.CAMERA )== PackageManager.PERMISSION_GRANTED){
//            Toast.makeText(this, "you have already granted CAMERA permission!", Toast.LENGTH_SHORT).show();
//        }else{
//            request_CAMERA_Permission();
//        }

    }
    private void request_READ_EXTERNAL_STORAGE_Permission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){

            new AlertDialog.Builder(this)

                    .setTitle("Permission needed")
                    .setMessage("read external storage Permission is needed")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(Splash.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},READ_EXTERNAL_STORAGE_PERMISSION_CODE);

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



            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},READ_EXTERNAL_STORAGE_PERMISSION_CODE);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    singleton.getStorage().loadFromMemory();
                    init();
                }
            }, 2000);
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
                            ActivityCompat.requestPermissions(Splash.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_EXTERNAL_STORAGE_PERMISSION_CODE);

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

    private void request_RECORD_AUDIO_PERMISSION_Permission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)){
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("record audio Permission is needed")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(Splash.this,new String[]{Manifest.permission.RECORD_AUDIO},RECORD_AUDIO_PERMISSION_CODE);

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
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},RECORD_AUDIO_PERMISSION_CODE);
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
                            ActivityCompat.requestPermissions(Splash.this,new String[]{Manifest.permission.CAMERA},CAMERA_PERMISSION_CODE);

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
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == READ_EXTERNAL_STORAGE_PERMISSION_CODE){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Read External storage Premssion granted", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "Read External storage Premssion not granted", Toast.LENGTH_SHORT).show();
            }
        }
        if(requestCode == WRITE_EXTERNAL_STORAGE_PERMISSION_CODE){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Write External storage Premssion granted", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "Write External storage Premssion not granted", Toast.LENGTH_SHORT).show();
            }
        }
        if(requestCode == CAMERA_PERMISSION_CODE){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "camera Premssion granted", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "camera Premssion not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void init(){
        if(singleton.hasUsername()){
            intent = new Intent(Splash.this,MainActivity.class);
        }else{
            intent = new Intent(Splash.this,LoginScreen.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
