package homeautomation.circularblue.com.iit;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle(Singleton.getInstance().getUsername());
        setSupportActionBar(myToolbar);
        init();
        loadImage();
    }
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                new AlertDialog.Builder(this)
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Singleton.getInstance().logout();
                                Intent  intent = new Intent(Image_view.this,LoginScreen.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                            }
                        }).setNegativeButton("No", null).show();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
    void init(){

        singleton =Singleton.getInstance();
        yes_button=(Button)  findViewById(R.id.yes_button);
        no_button = (Button) findViewById(R.id.no_button);

        yes_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
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
      //  Bitmap bitmap = BitmapFactory.decodeFile(path);
        ImageView myImageView = (ImageView)findViewById(R.id.image_view);
        Bitmap myBitmap  = BitmapFactory.decodeFile(path);
        Bitmap bitmap = ExifUtil.rotateBitmap(path, myBitmap);

        myImageView.setImageBitmap(bitmap);
    }



}
