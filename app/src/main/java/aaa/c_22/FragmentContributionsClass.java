package aaa.c_22;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FragmentContributionsClass extends Fragment implements View.OnClickListener {
    View rootView;
    String username=HomeActivity.username;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_contributions, container, false);
        new FetchContributions().execute();
        (rootView.findViewById(R.id.card_abhinav)).setOnClickListener(this);
        (rootView.findViewById(R.id.card_mayank)).setOnClickListener(this);
        (rootView.findViewById(R.id.card_narayanan)).setOnClickListener(this);
        (rootView.findViewById(R.id.card_kaushik)).setOnClickListener(this);
        (rootView.findViewById(R.id.card_neeraj)).setOnClickListener(this);
        Button edit=(Button)rootView.findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                final View inputbox = inflater.inflate(R.layout.edittext, null);
                new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.MyDialogTheme))
                        .setTitle("Contribution or Take Away")
                        .setView(inputbox)
                        .setMessage("\nEnter a positive number to contribute or negative to take away.\n")
                        .setIcon(android.R.drawable.ic_menu_agenda)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                EditText input=(EditText)inputbox.findViewById(R.id.input);
                                String ans=input.getText().toString();
                                if(ans.length()>0)
                                {
                                    int a=Integer.parseInt(ans);
                                    if(a!=0)
                                    {
                                        new UpdateContributions().execute(a+"");
                                        new FetchContributions().execute();
                                    }
                                }
                            }
                        })
                        .create().show();
            }
        });
        return rootView;
    }
    private class UpdateContributions extends AsyncTask<String,Void,Void> {
        String webPage="",baseUrl="http://srmvdpauditorium.in/aaa/c-22/",name=username;
        int id=0;
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute(){
            progressDialog = ProgressDialog.show(getActivity(), "Please Wait!","Updating Values!");
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection = null;
            try
            {
                url = new URL(baseUrl+"updateContributions.php?name="+name+"&amount="+strings[0]);
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
            if(webPage.equals("success"))
                Toast.makeText(getContext(), "Update Successful!", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getContext(), "An error occurred while updating! Try again or call Abhinav", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onClick(View v){
        int id=v.getId(),textID=0;
        String name="";
        switch (id)
        {
            case R.id.card_abhinav:
                name="Abhinav";
                textID=R.id.abhinavtext;
                break;
            case R.id.card_mayank:
                name="Mayank";
                textID=R.id.mayanktext;
                break;
            case R.id.card_narayanan:
                name="Narayanan";
                textID=R.id.narayanantext;
                break;
            case R.id.card_kaushik:
                name="Kaushik";
                textID=R.id.kaushiktext;
                break;
            case R.id.card_neeraj:
                name="Neeraj";
                textID=R.id.neerajtext;
                break;
        }
        TextView t=(TextView)rootView.findViewById(textID);
        if(t.getVisibility() == View.VISIBLE)
            t.setVisibility(View.GONE);
        else
            new FetchPreviousContributions().execute(name,textID+"");
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
            t.setVisibility(View.VISIBLE);
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