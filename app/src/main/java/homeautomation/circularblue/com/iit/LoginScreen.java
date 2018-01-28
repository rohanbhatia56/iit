package homeautomation.circularblue.com.iit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class LoginScreen extends AppCompatActivity {

    Button button;
    EditText editText;
    TextView textureView;
    Singleton singleton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        init();
    }
    private void init(){
        button = (Button) findViewById(R.id.button);
        editText = (EditText) findViewById(R.id.editText);
        textureView = (TextView) findViewById(R.id.textView);
        singleton =Singleton.getInstance();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singleton.setUsername(editText.getText().toString().trim());
                Intent intent = new Intent(LoginScreen.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
}
