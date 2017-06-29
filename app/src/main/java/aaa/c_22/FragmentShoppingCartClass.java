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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FragmentShoppingCartClass extends Fragment {
    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_shopping_cart, container, false);

        new FetchShoppingCart().execute();
        //TODO set ids to each linear layout and implement onClick() method

        //TODO give option to mark as red or green or remove when clicked on an item

        Button edit=(Button)rootView.findViewById(R.id.editcart);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                final View addToCart = inflater.inflate(R.layout.addtocart, null);
                new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.MyDialogTheme))
                        .setTitle("Add Item to Cart")
                        .setView(addToCart)
                        .setMessage("\nEnter name and quantity of Item to be added.\n")
                        .setIcon(android.R.drawable.ic_menu_agenda)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                EditText nameinput=(EditText)addToCart.findViewById(R.id.name);
                                String name=nameinput.getText().toString();
                                EditText quantityinput=(EditText)addToCart.findViewById(R.id.quantity);
                                String quantity=quantityinput.getText().toString();
                                if(name.length()>0 && quantity.length()>0)
                                {
                                    //TODO add item in database and fetch again to display in fragment
                                }
                            }
                        })
                        .create().show();
            }
        });
        return rootView;
    }
    private class FetchShoppingCart extends AsyncTask<Void,Void,Void> {
        String webPage="",baseUrl="http://srmvdpauditorium.in/aaa/c-22/";
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute(){
            progressDialog = ProgressDialog.show(getActivity(), "Please Wait!","Fetching Cart!");
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            URL url;
            HttpURLConnection urlConnection = null;
            try
            {
                url = new URL(baseUrl+"fetchShoppingCart.php");
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
            LinearLayout data=(LinearLayout)rootView.findViewById(R.id.data);
            data.removeAllViews();
            while(webPage.contains("eNdEnD"))
            {
                int i=webPage.indexOf("eNdEnD");
                String name=webPage.substring(0,i);
                webPage=webPage.substring(i+6);
                i=webPage.indexOf("eNdEnD");
                String quantity=webPage.substring(0,i);
                webPage=webPage.substring(i+6);
                i=webPage.indexOf("eNdEnD");
                String color=webPage.substring(0,i);
                webPage=webPage.substring(i+6);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout
                        .LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout linearLayout=new LinearLayout(getContext());
                if(color.equals("Red"))
                    linearLayout.setBackgroundColor(ContextCompat.getColor(getContext(),android.R.color.holo_red_dark));
                else if(color.equals("Green"))
                    linearLayout.setBackgroundColor(ContextCompat.getColor(getContext(),android.R.color.holo_green_dark));
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                TextView tv1=new TextView(getContext());
                layoutParams.weight = 1;
                tv1.setLayoutParams(layoutParams);
                tv1.setGravity(Gravity.CENTER_HORIZONTAL);
                tv1.setTextColor(Color.WHITE);
                tv1.setPadding(8,8,8,8);
                tv1.setText(name);
                tv1.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.border));
                linearLayout.addView(tv1);
                TextView tv2=new TextView(getContext());
                layoutParams.weight = 1;
                tv2.setLayoutParams(layoutParams);
                tv2.setTextColor(Color.WHITE);
                tv2.setGravity(Gravity.CENTER_HORIZONTAL);
                tv2.setPadding(8,8,8,8);
                tv2.setText(quantity);
                tv2.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.border));
                linearLayout.addView(tv2);
                data.addView(linearLayout);
            }
        }
    }
}