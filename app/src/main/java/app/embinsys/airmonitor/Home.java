package app.embinsys.airmonitor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Home extends AppCompatActivity
{
        ImageView home_icon;
        ImageView bell;

        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_home);
                home_icon=findViewById(R.id.home_icon);

                home_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                Intent i= new Intent(Home.this,Monitor.class);
                                startActivity(i);
                                finish();
                        }
                });
        }
}
