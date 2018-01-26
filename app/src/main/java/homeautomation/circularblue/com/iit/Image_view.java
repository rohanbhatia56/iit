package homeautomation.circularblue.com.iit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Image_view extends AppCompatActivity {

    Button yes_button,no_button;
    Singleton singleton;
    String path="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        init();
        loadImage();

    }
    void init(){

        singleton =Singleton.getInstance();
        yes_button=(Button)  findViewById(R.id.yes_button);
        no_button = (Button) findViewById(R.id.no_button);

        yes_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singleton.setImagePath(path);
                Intent intent = new Intent(Image_view.this,Record_Audio.class);
                startActivity(intent);
            }
        });

        no_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    void loadImage(){
        path=getIntent().getExtras().getString("path");
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        ImageView myImageView = (ImageView)findViewById(R.id.image_view);
        myImageView.setImageBitmap(bitmap);
    }

}
