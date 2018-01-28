package homeautomation.circularblue.com.iit;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splash extends AppCompatActivity {

    Singleton singleton;
    Intent intent=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        singleton =Singleton.getInstance();
        singleton.getStorage().init(this);
    }
    protected void onStart(){
        super.onStart();


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
