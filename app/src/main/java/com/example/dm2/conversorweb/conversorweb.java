package com.example.dm2.conversorweb;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

public class conversorweb extends AppCompatActivity {

    private Button boton;
    private EditText num;
    private TextView resul;
    private Spinner spinner1,spinner2;
    private String contSpinn1;
    private String contSpinn2;
    private String resDouble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversorweb);

        resul =(TextView)findViewById(R.id.resultado);
        boton =(Button)findViewById(R.id.btnconv);
        num =(EditText)findViewById(R.id.numero);
        spinner1=(Spinner)findViewById(R.id.spinner1);
        spinner2=(Spinner)findViewById(R.id.spinner2);


        String[] datos=new String[]{"Kilometers","Yards","Centimeters","Meters","Miles"};

        ArrayAdapter<String> adaptador=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,datos);
        spinner1.setAdapter(adaptador);
        spinner2.setAdapter(adaptador);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                contSpinn1 = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                contSpinn2 = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cantidad= num.getText().toString();
                AsyncPost tarea=new AsyncPost();
                tarea.execute(cantidad);
            }
        });
    }

    private class AsyncPost extends AsyncTask<String,Void,Void>
    {

        @Override
        protected Void doInBackground(String... params) {


            try {
                HttpURLConnection conn;
                URL url=new URL("http://www.webservicex.net/length.asmx/ChangeLengthUnit");
                String param="LengthValue="+ URLEncoder.encode(params[0],"UTF-8")+"&fromLengthUnit="+URLEncoder.encode(contSpinn1,"UTF-8")
                        +"&toLengthUnit="+URLEncoder.encode(contSpinn2,"UTF-8");
                conn= (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                PrintWriter out=new PrintWriter((conn.getOutputStream()));
                out.print(param);
                out.close();
                String result="";
                Scanner inStream=new Scanner(conn.getInputStream());
                while(inStream.hasNextLine())
                {
                    result=inStream.nextLine();
                    resDouble = result.replace("</double>","");
                    String pru=resDouble.substring(result.indexOf('>')+1);
                    resDouble=pru;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(Void result)
        {
            resul.setText(resDouble+" "+spinner2.getSelectedItem());
        }
    }

}