package aaa.c_22;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FragmentContributionsClass extends Fragment implements View.OnClickListener {
    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_contribution, container, false);
        new FetchContributions().execute();
        (rootView.findViewById(R.id.card_abhinav)).setOnClickListener(this);
        (rootView.findViewById(R.id.card_mayank)).setOnClickListener(this);
        (rootView.findViewById(R.id.card_narayanan)).setOnClickListener(this);
        (rootView.findViewById(R.id.card_kaushik)).setOnClickListener(this);
        (rootView.findViewById(R.id.card_neeraj)).setOnClickListener(this);
        return rootView;
    }
    @Override
    public void onClick(View v){
        int id=v.getId();
        String name="",textID="";
        switch (id)
        {
            case R.id.card_abhinav:
                name="Abhinav";
                textID=R.id.abhinavtext+"";
                break;
            case R.id.card_mayank:
                name="Mayank";
                textID=R.id.mayanktext+"";
                break;
            case R.id.card_narayanan:
                name="Narayanan";
                textID=R.id.narayanantext+"";
                break;
            case R.id.card_kaushik:
                name="Kaushik";
                textID=R.id.kaushiktext+"";
                break;
            case R.id.card_neeraj:
                name="Neeraj";
                textID=R.id.neerajtext+"";
                break;
        }
        new FetchPreviousContributions().execute(name,textID);
    }
    private class FetchPreviousContributions extends AsyncTask<String,Void,Void> {
        String webPage="",baseUrl="http://srmvdpauditorium.in/aaa/c-22/",name="";
        int id=0;
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute(){
            progressDialog = ProgressDialog.show(getActivity(), "Please Wait!","Fetching Data from Archive!");
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(String... strings) {
            name=strings[0];
            id=Integer.parseInt(strings[1]);
            URL url;
            HttpURLConnection urlConnection = null;
            try
            {
                url = new URL(baseUrl+"fetchPreviousContributions.php?name="+name);
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
            TextView t=(TextView)rootView.findViewById(id);
            String s=values(webPage);
            t.setText(s);
        }
    }
    private class FetchContributions extends AsyncTask<Void,Void,Void> {
        String webPage="",baseUrl="http://srmvdpauditorium.in/aaa/c-22/";
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute(){
            progressDialog = ProgressDialog.show(getActivity(), "Please Wait!","Fetching Data!");
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            URL url;
            HttpURLConnection urlConnection = null;
            try
            {
                url = new URL(baseUrl+"fetchContributions.php");
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
            try
            {
                JSONObject jason=new JSONObject(webPage);
                ((TextView)rootView.findViewById(R.id.abhinav)).setText(jason.getString("Abhinav"));
                ((TextView)rootView.findViewById(R.id.mayank)).setText(jason.getString("Mayank"));
                ((TextView)rootView.findViewById(R.id.narayanan)).setText(jason.getString("Narayanan"));
                ((TextView)rootView.findViewById(R.id.kaushik)).setText(jason.getString("Kaushik"));
                ((TextView)rootView.findViewById(R.id.neeraj)).setText(jason.getString("Neeraj"));
            }
            catch (JSONException e)
            {
                e.printStackTrace();
                Toast.makeText(getActivity(), "An error occurred while fetching data. Please try again or contact Abhinav.", Toast.LENGTH_LONG).show();
            }
        }
    }
    String values(String s){
        String ans="";
        while(s.contains(","))
        {
            int i=s.indexOf(',');
            ans=ans+s.substring(0,i)+"\n";
            s=s.substring(i+1);
        }
        return ans.trim();
    }
}