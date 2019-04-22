package dim.aviv.greddit.account;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import dim.aviv.greddit.R;
import dim.aviv.greddit.RedditAPI;
import dim.aviv.greddit.URLS;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by אביב on 12/03/2019.
 */

public class LoginActivity extends AppCompatActivity{
    private static final String TAG = "LoginActivity";
    private URLS urls = new URLS();

    private ProgressBar mProgressBar;
    private EditText mUsername;
    private EditText mPassword;
    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnLogin = (Button) findViewById(R.id.btn_login);
        mPassword = (EditText) findViewById(R.id.input_password);
        mUsername = (EditText) findViewById(R.id.input_username);
        mProgressBar = (ProgressBar) findViewById(R.id.loginRequestLoadingProgressBar);
        mProgressBar.setVisibility(View.GONE);
         textView = (TextView) findViewById(R.id.link_signup);
         //String whatt = <string name="txtCredits"><a href="http://www.google.com">Google</a></string>
        textView.setMovementMethod(LinkMovementMethod.getInstance());

//        textView.setText(getString(R.string.lostpassword));
//        textView.setLinkTextColor(Color.BLUE);
//        Linkify.addLinks(textView, Linkify.WEB_URLS);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: trying to log in.");
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();

                if(!username.equals("") && !password.equals("")){
                    mProgressBar.setVisibility(View.VISIBLE);
                    login(username,password);
                }
            }
        });
    }
    private void login(final String username, String password){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urls.LOGIN_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RedditAPI redditAPI = retrofit.create(RedditAPI.class);

        HashMap<String, String> headermap = new HashMap<>();
        headermap.put("Content-Type", "application/json");

        Call<CheckLogin> call = redditAPI.signIn(headermap,username,username,password,"json");

        call.enqueue(new Callback<CheckLogin>() {
            @Override
            public void onResponse(Call<CheckLogin> call, Response<CheckLogin> response) {


                try{
                    Log.d(TAG, "onResponse: server Respone " + response.body().toString());
                    Log.d(TAG, "onResponse: server Respone " + response.toString());


                    String modhash = response.body().getJson().getData().getModhash();
                    String cookie = response.body().getJson().getData().getCookie();
                    Log.d(TAG, "onResponse: modhash " + modhash);
                    Log.d(TAG, "onResponse: cookie " + cookie);


                    if (!modhash.equals("")) {
                        setSessionParams(username,modhash,cookie);
                        mProgressBar.setVisibility(View.GONE);
                        mUsername.setText("");
                        mPassword.setText("");
                        Toast.makeText(LoginActivity.this,"Login Successfully.",Toast.LENGTH_SHORT).show();


                        finish();
                }
                }catch (NullPointerException e){
                    Log.e(TAG, "onResponse: NullPointerException" + e.getMessage() );
                }

            }

            @Override
            public void onFailure(Call<CheckLogin> call, Throwable t) {

                mProgressBar.setVisibility(View.GONE);
                Log.e(TAG, "onFailure: Something went wrong: " + t.getMessage() );
                Toast.makeText(LoginActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setSessionParams(String username,String modhash,String cookie){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        SharedPreferences.Editor editor = preferences.edit();

        Log.d(TAG, "setSessionParams: storing session variables: \n " +
                    "username: " + username + "\n" +
                    "modhash: " + modhash + "\n" +
                    "cookie: " + cookie + "\n" );

        editor.putString("@string/SessionUsername", username);
        editor.commit();
        editor.putString("@string/SessionModhash", modhash);
        editor.commit();
        editor.putString("@string/SessionCookie", cookie);
        editor.commit();
    }
}
