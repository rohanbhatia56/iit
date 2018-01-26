package homeautomation.circularblue.com.iit;

/**
 * Created by rohan on 26-01-2018.
 */

public class PlatformData {
    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int imageId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String name;

    public  PlatformData(int imageId,String name){
        this.imageId = imageId;
        this.name = name;
    }
}
