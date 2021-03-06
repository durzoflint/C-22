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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FragmentShoppingCartClass extends Fragment{
    ArrayList<Integer> ids = new ArrayList<>();
    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_shopping_cart, container, false);
        new FetchShoppingCart().execute();
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
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which)
                            {
                                EditText nameinput=(EditText)addToCart.findViewById(R.id.name);
                                String name=nameinput.getText().toString();
                                EditText quantityinput=(EditText)addToCart.findViewById(R.id.quantity);
                                String quantity=quantityinput.getText().toString();
                                if(name.length()>0 && quantity.length()>0)
                                    new AddToCart().execute(name,quantity);
                            }
                        })
                        .create().show();
            }
        });
        return rootView;
    }
    View.OnClickListener myOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View view)
        {
            TextView nameView=(TextView)(((ViewGroup)view).getChildAt(0));
            final String name=nameView.getText().toString();
            TextView quantityView=(TextView)(((ViewGroup)view).getChildAt(1));
            final String quantity=quantityView.getText().toString();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            final View changecolor = inflater.inflate(R.layout.changecolor, null);
            final RadioGroup gpaRadioGroup = (RadioGroup)changecolor.findViewById(R.id.gparadiogroup);
            new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.MyDialogTheme))
                    .setTitle(name)
                    .setView(changecolor)
                    .setMessage("\nQuantity : "+quantity+"\n")
                    .setIcon(android.R.drawable.ic_menu_agenda)
                    .setNegativeButton("Remove", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            new RemoveFromCart().execute(name,quantity);
                            view.setVisibility(View.GONE);
                        }
                    })
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which)
                        {
                            int selectedId=gpaRadioGroup.getCheckedRadioButtonId();
                            RadioButton gpaoption=(RadioButton)changecolor.findViewById(selectedId);
                            String color = gpaoption.getText().toString();
                            switch (color) {
                                case "Red":
                                    view.setBackgroundColor(ContextCompat.getColor(getContext(),
                                            android.R.color.holo_red_dark));
                                    break;
                                case "Green":
                                    view.setBackgroundColor(ContextCompat.getColor(getContext(),
                                            android.R.color.holo_green_dark));
                                    break;
                                default:
                                    view.setBackgroundColor(ContextCompat.getColor(getContext(),
                                            android.R.color.transparent));
                                    break;
                            }
                            new ChangeItemColorInCart().execute(name,quantity,color);
                        }
                    })
                    .create().show();
        }
    };
    private class FetchShoppingCart extends AsyncTask<Void,Void,Void> {
        String webPage="",baseUrl="http://srmvdpauditorium.in/aaa/c-22/";
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
                linearLayout.setOnClickListener(myOnClickListener);
                int id=View.generateViewId();
                ids.add(id);
                linearLayout.setId(id);
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
    private class AddToCart extends AsyncTask<String,Void,Void> {
        String webPage="",baseUrl="http://srmvdpauditorium.in/aaa/c-22/",itemName;
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute(){
            progressDialog = ProgressDialog.show(getActivity(), "Please Wait!","Adding to Cart!");
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(String... strings) {
            itemName=strings[0];
            String itemQuantity=strings[1];
            URL url;
            HttpURLConnection urlConnection = null;
            try
            {
                url = new URL(handleSpaces(baseUrl+"addItemToCart.php?name="+itemName+"&quantity="+itemQuantity));
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
            new FetchShoppingCart().execute();
        }
    }
    private class RemoveFromCart extends AsyncTask<String,Void,Void> {
        String webPage="",baseUrl="http://srmvdpauditorium.in/aaa/c-22/",itemName;
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute(){
            progressDialog = ProgressDialog.show(getActivity(), "Please Wait!","Removing form Cart!");
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(String... strings) {
            itemName=strings[0];
            String itemQuantity=strings[1];
            URL url;
            HttpURLConnection urlConnection = null;
            try
            {
                url = new URL(handleSpaces(baseUrl+"removeItemFromCart.php?name="+itemName+"&quantity="+itemQuantity));
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
                Toast.makeText(getContext(), "Item Removed from Cart Successfully", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getContext(), "Error while removing "+itemName+" from Cart", Toast.LENGTH_LONG).show();
            new FetchShoppingCart().execute();
        }
    }
    private class ChangeItemColorInCart extends AsyncTask<String,Void,Void> {
        String webPage="",baseUrl="http://srmvdpauditorium.in/aaa/c-22/",itemName;
        @Override
        protected Void doInBackground(String... strings) {
            itemName=strings[0];
            String itemQuantity=strings[1];
            URL url;
            HttpURLConnection urlConnection = null;
            try
            {
                url = new URL(handleSpaces(baseUrl+"changeColorInCart.php?name="+itemName
                        +"&quantity="+itemQuantity+"&color="+strings[2]));
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
            if (!webPage.equals("success"))
                Toast.makeText(getContext(), "Error while changing color. Please refresh again and check.",
                        Toast.LENGTH_LONG).show();
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