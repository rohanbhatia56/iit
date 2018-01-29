package homeautomation.circularblue.com.iit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rohan on 26-01-2018.
 */


class Singleton {

    ArrayList<PathsInfo> paths = new ArrayList<>();
    ArrayList<Uri> uris = new ArrayList<>();
    public static  final String publicDirectory = Environment.getExternalStorageDirectory().getPath();
    private static final Singleton ourInstance = new Singleton();
    ProgressDialog dialog;

    public Storage getStorage() {
        return storage;
    }

    Storage storage;
    public StorageReference getStorageRef() {
        return mStorageRef;
    }
    public void showProgressDialog(Context context,String msg){
        if(dialog!=null){
            dialog.dismiss();
            dialog=null;
        }
        dialog = new ProgressDialog(context);
        dialog.setMessage(msg);
        dialog.setCancelable(false);
        dialog.show();
    }
    public void setProgressDialogMessage(String msg){
        if(dialog!=null) {
            if (dialog.isShowing()) {
                dialog.setMessage(msg);
            }
        }
    }
    public void dissmissProgressDialog(){
        if(dialog!=null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    private StorageReference mStorageRef;

    public void addVideoPath(String vidoepath){
        paths.add(0,new PathsInfo(vidoepath,PathType.VIDEO));
        getStorage().save();
    }
    public void setVideoPaths(ArrayList<PathsInfo> paths){
        this.paths = paths;

    }

    public void addVideoURI(Uri uri){
        uris.add(uri);
    }
    public ArrayList<PathsInfo> getVideoPaths(){
        return paths;
    }
    public ArrayList<Uri> getVideoURIs(){
        return uris;
    }
    public String getImagePath() {
        return imagePath;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    private String imagePath="";

    public String getImagePathCompressed() {
        return imagePathCompressed;
    }

    public void setImagePathCompressed(String imagePathCompressed) {
        this.imagePathCompressed = imagePathCompressed;
    }

    private String imagePathCompressed="";
    public String getVideoPath() {
        return videoPath;
    }
    private String videoPath="";

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    private String audioPath="";


    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String videoURL;
    static Singleton getInstance() {
        return ourInstance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        storage.save();
    }
    public boolean hasUsername(){
        if(!this.username.isEmpty()){
            return true;
        }
        return  false;
    }
    public void logout(){
       storage.removekeys();
        setVideoPaths(new ArrayList<PathsInfo>());
    }

    private String username="";


    private Singleton() {
        paths = new ArrayList<>();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        storage = new Storage();
    }


}
class Storage{
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences shref;
    SharedPreferences.Editor editor;
    String key = "Key";
    Context context;
    public void init(Context context){
        this.context = context;
        shref = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }

    public void save(){

        Gson gson = new Gson();
        String json = gson.toJson(Singleton.getInstance().getVideoPaths());
        editor = shref.edit();
        editor.remove(key).commit();
        editor.putString(key, json);
        if(Singleton.getInstance().hasUsername()){
            editor.remove("username").commit();
            editor.putString("username", Singleton.getInstance().getUsername());
        }
        editor.commit();
    }
    public void loadFromMemory(){
        Gson gson = new Gson();
        String response=shref.getString(key , "");
        ArrayList<PathsInfo> lstArrayList = gson.fromJson(response,
                new TypeToken<List<PathsInfo>>(){}.getType());
        if(lstArrayList!=null) {
            Singleton.getInstance().setVideoPaths(lstArrayList);
        }

        String username = shref.getString("username",null);
        if(username!=null && !username.isEmpty()){
            Singleton.getInstance().setUsername(username);
        }
    }
    public void removekeys(){
        editor = shref.edit();
        editor.clear();
        editor.commit();
    }
}
