package homeautomation.circularblue.com.iit;

import android.graphics.Path;

/**
 * Created by rohan on 28-01-2018.
 */

public class PathsInfo {

    public PathsInfo(String videoPath,PathType type) {
        this.videoPath = videoPath;
        this.imagePath = imagePath;
        this.type = type;
    }
    public String videoPath;
    public String imagePath;
    public PathType type;
}
