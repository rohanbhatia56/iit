package homeautomation.circularblue.com.iit;

import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by rohan on 26-01-2018.
 */

class Singleton {

    ArrayList<String> paths = new ArrayList<>();
    ArrayList<Uri> uris = new ArrayList<>();
    private static final Singleton ourInstance = new Singleton();

    public void addVideoPath(String path){
        paths.add(path);
    }
    public void addVideoURI(Uri uri){
        uris.add(uri);
    }
    public ArrayList<String> getVideoPaths(){
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

    public Uri videoURI;
    static Singleton getInstance() {
        return ourInstance;
    }

    private Singleton() {
        paths = new ArrayList<>();
    }

}
