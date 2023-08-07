package app.embinsys.airmonitor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;



public class Profile extends AppCompatActivity {
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    TextView name;
    TextView email;
    ImageView log_out;
    ImageView profile_img;
    ImageView left_arrow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


          getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        name= findViewById(R.id.name);
        email= findViewById(R.id.email);
        //  ImageView  profile_img = (ImageView) findViewById(R.id.profile_img);
        log_out= findViewById(R.id.tab_log_out);

        gso= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc= GoogleSignIn.getClient(this,gso);

        //   AccessToken accessToken = AccessToken.getCurrentAccessToken();

      /* GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        try {
                           String fullname = object.getString("name");
                           String url = object.getJSONObject("picture").getJSONObject("data").getString("url");
                            name.setText(fullname);
                            Picasso.get().load(url).into(profile);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,picture.type(large)");
        request.setParameters(parameters);
        request.executeAsync();*/

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null) {
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            //  Uri userImageUrl = acct.getPhotoUrl();
            name.setText(personName);
            email.setText(personEmail);

        }

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

    }
    void signOut() {
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {

            @Override
            public void onComplete(Task<Void> task) {
                finish();
                startActivity(new Intent(Profile.this, SignIn.class));
            }

        });

      /*  log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logOut();
                startActivity(new Intent(Profile.this, SignIn.class));
                finish();
            }
        });*/

    }
}
