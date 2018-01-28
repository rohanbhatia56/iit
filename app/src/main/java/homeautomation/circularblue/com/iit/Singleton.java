package homeautomation.circularblue.com.iit;

import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by rohan on 26-01-2018.
 */


class Singleton {

    ArrayList<PathsInfo> paths = new ArrayList<>();
    ArrayList<Uri> uris = new ArrayList<>();
    public static  final String publicDirectory = Environment.getExternalStorageDirectory().getPath();
    private static final Singleton ourInstance = new Singleton();

    public void addVideoPath(String vidoepath){
        paths.add(0,new PathsInfo(vidoepath,PathType.VIDEO));
    }
//    public void addVideoPath(String vidoepath,String imagePath){
//        paths.add(0,new PathsInfo(vidoepath,imagePath,PathType.IMAGE));
//    }

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

    public Uri getVideoURI()
    {
        return this.videoURI;
    }


    public void setVideoURI(Uri videoURI) {
        this.videoURI = videoURI;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String videoURL;
    public Uri videoURI;
    static Singleton getInstance() {
        return ourInstance;
    }

    private Singleton() {
        paths = new ArrayList<>();
    }

}
