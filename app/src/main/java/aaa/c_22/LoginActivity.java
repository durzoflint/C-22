package aaa.c_22;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    boolean savelogin;
    String baseUrl="http://srmvdpauditorium.in/aaa/c-22/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final EditText nameInout = (EditText)findViewById(R.id.name);
        final EditText passwordInout = (EditText)findViewById(R.id.password);
        final CheckBox rememberpasswordbox=(CheckBox)findViewById(R.id.checkBox);
        SharedPreferences loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        final SharedPreferences.Editor loginPrefsEditor = loginPreferences.edit();
        savelogin = loginPreferences.getBoolean("savelogin", false);
        if(savelogin)
        {
            nameInout.setText(loginPreferences.getString("id",""));
            passwordInout.setText(loginPreferences.getString("password",""));
            rememberpasswordbox.setChecked(true);
        }
        Button login=(Button)findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = nameInout.getText().toString();
                String password =  passwordInout.getText().toString();
                if (rememberpasswordbox.isChecked()) {
                    loginPrefsEditor.putBoolean("saveLogin", true);
                    loginPrefsEditor.putString("id", id);
                    loginPrefsEditor.putString("password", password);
                    loginPrefsEditor.apply();
                } else {
                    loginPrefsEditor.clear();
                    loginPrefsEditor.commit();
                }
                new Login().execute(id,password);
            }
        });
    }
    private class Login extends AsyncTask<String,Void,Void> {
        String webPage="",name="";
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute(){
            progressDialog = ProgressDialog.show(LoginActivity.this, "Please Wait!","Logging In!");
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(String... strings){
            name=strings[0];
            URL url;
            HttpURLConnection urlConnection = null;
            try
            {
                url = new URL(baseUrl+"login.php?username="+name+"&password="+strings[1]);
                urlConnection = (HttpURLConnection) url.openConnection();
                BufferedReader br=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String data;
                while ((data=br.readLine()) != null)
                    webPage=webPage+data;
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            finally
            {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            if(webPage.equals("login successful"))
            {
                Toast.makeText(LoginActivity.this, "Login Success!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(LoginActivity.this,HomeActivity.class);
                i.putExtra("Name",name);
                startActivity(i);
            }
            else
                Toast.makeText(LoginActivity.this, "Username or Password incorrect.", Toast.LENGTH_LONG).show();
        }
    }
}