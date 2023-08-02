package app.embinsys.airmonitor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class SignIn extends AppCompatActivity {
    ImageView tab_google;
    ImageView tab_facebook;
    ImageView tab_sign_in;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
   // CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        tab_google = findViewById(R.id.tab_google);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);
        tab_sign_in=findViewById(R.id.tab_sign_in);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            navigateToSecondActivity();
        }


      /*  //   tab_facebook = findViewById(R.id.tab_facebook);
        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if(accessToken!=null && accessToken.isExpired()==false){
            startActivity(new Intent(SignIn.this, Monitor.class));
            finish();
        }

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        startActivity(new Intent(SignIn.this, Monitor.class));
                        finish();
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "facebook:onCancel");  // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
        tab_facebook = findViewById(R.id.tab_facebook);


        tab_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(SignIn.this, Arrays.asList("public_profile"));

            }
        });
*/

        tab_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // signIn();
                Intent signInIntent = gsc.getSignInIntent();
                startActivityForResult(signInIntent, 1000);
            }
        });

        tab_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void signIn()
    {
        finish();
        Intent i = new Intent(SignIn.this, Monitor.class);
        startActivity(i);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                navigateToSecondActivity();
            } catch (ApiException e) {
               Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();

            }

        }
    }

    private void navigateToSecondActivity() {
        finish();
        Intent i = new Intent(SignIn.this, Monitor.class);
        startActivity(i);
    }


}
