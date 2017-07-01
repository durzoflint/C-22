package aaa.c_22;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FragmentRecordClass extends Fragment {
    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_record, container, false);
        new FetchRecords().execute();
        Button addToRecord = (Button)rootView.findViewById(R.id.add);
        addToRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                final View addRecord = inflater.inflate(R.layout.addrecord, null);
                new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.MyDialogTheme2))
                        .setTitle("Add Record")
                        .setView(addRecord)
                        .setMessage("\nEnter Date, Item Name and Cost\n")
                        .setIcon(android.R.drawable.ic_menu_agenda)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which)
                            {
                                DatePicker pickDate = (DatePicker)addRecord.findViewById(R.id.pickdate);
                                String year = ""+pickDate.getYear();
                                String month = ""+(pickDate.getMonth()+1);
                                String day = ""+pickDate.getDayOfMonth();
                                EditText nameInput = (EditText)addRecord.findViewById(R.id.name);
                                String itemName = nameInput.getText().toString();
                                EditText costInput = (EditText)addRecord.findViewById(R.id.cost);
                                String itemCost = costInput.getText().toString();
                                if(month.length()==1)
                                    month="0"+month;
                                if(day.length()==1)
                                    day="0"+day;
                                String idFordate = year+month+day;
                                new AddRecord().execute(day+"/"+month+"/"+year, itemName, itemCost, idFordate);
                            }
                        })
                        .create().show();
            }
        });
        return rootView;
    }
    private class AddRecord extends AsyncTask<String,Void,Void> {
        String webPage="",baseUrl="http://srmvdpauditorium.in/aaa/c-22/",itemName;
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute(){
            progressDialog = ProgressDialog.show(getActivity(), "Please Wait!","Adding to Records!");
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(String... strings) {
            itemName=strings[1];
            URL url;
            HttpURLConnection urlConnection = null;
            try
            {
                url = new URL(handleSpaces(baseUrl+"addRecord.php?date="+strings[0]+"&name="+itemName
                        +"&cost="+strings[2]+"&idfordate="+strings[3]));
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
            if (webPage.equals("success"))
                Toast.makeText(getContext(), "Item Added to Cart Successfully", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getContext(), "Error while adding "+itemName+" to Cart", Toast.LENGTH_LONG).show();
            new FetchRecords().execute();
        }
    }
    private class FetchRecords extends AsyncTask<Void,Void,Void> {
        String webPage="",baseUrl="http://srmvdpauditorium.in/aaa/c-22/";
        @Override
        protected Void doInBackground(Void... voids) {
            URL url;
            HttpURLConnection urlConnection = null;
            try
            {
                url = new URL(baseUrl+"fetchRecords.php");
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
            LinearLayout data=(LinearLayout)rootView.findViewById(R.id.data);
            data.removeAllViews();
            while(webPage.contains("eNdEnD"))
            {
                int i=webPage.indexOf("eNdEnD");
                String date=webPage.substring(0,i);
                webPage=webPage.substring(i+6);
                i=webPage.indexOf("eNdEnD");
                String name=webPage.substring(0,i);
                webPage=webPage.substring(i+6);
                i=webPage.indexOf("eNdEnD");
                String cost=webPage.substring(0,i);
                webPage=webPage.substring(i+6);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout
                        .LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout linearLayout=new LinearLayout(getContext());
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                TextView tv1=new TextView(getContext());
                layoutParams.weight = 1;
                tv1.setLayoutParams(layoutParams);
                tv1.setGravity(Gravity.CENTER_HORIZONTAL);
                tv1.setTextColor(Color.WHITE);
                tv1.setPadding(8,8,8,8);
                tv1.setText(date);
                tv1.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.border));
                linearLayout.addView(tv1);
                TextView tv2=new TextView(getContext());
                layoutParams.weight = 1;
                tv2.setLayoutParams(layoutParams);
                tv2.setTextColor(Color.WHITE);
                tv2.setGravity(Gravity.CENTER_HORIZONTAL);
                tv2.setPadding(8,8,8,8);
                tv2.setText(name);
                tv2.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.border));
                linearLayout.addView(tv2);
                TextView tv3=new TextView(getContext());
                layoutParams.weight = 1;
                tv3.setLayoutParams(layoutParams);
                tv3.setTextColor(Color.WHITE);
                tv3.setGravity(Gravity.CENTER_HORIZONTAL);
                tv3.setPadding(8,8,8,8);
                tv3.setText(cost);
                tv3.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.border));
                linearLayout.addView(tv3);
                data.addView(linearLayout);
            }
        }
    }
    String handleSpaces(String s){
        String x="";
        for(int i=0;i<s.length();i++)
        {
            char ch= s.charAt(i);
            if(ch==' ')
                x=x+"%20";
            else
                x=x+ch;
        }
        return x;
    }
}