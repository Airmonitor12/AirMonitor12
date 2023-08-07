package app.embinsys.airmonitor;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
       // getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_splash);
           new CountDownTimer(3000, 1000)
        {

            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                Intent i = new Intent(Splash.this, SignIn.class);
                startActivity(i);
                finish();
            }
        }.start();
    }
}