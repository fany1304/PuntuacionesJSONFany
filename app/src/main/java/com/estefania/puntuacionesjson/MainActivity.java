package com.estefania.puntuacionesjson;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.estefania.puntuacionesjson.R.id.puntos;

public class MainActivity extends AppCompatActivity {

    Button btnVerPuntuaciones;
    Button btnCrearPuntuacion;
    TextView puntos;

    private static final String urlObtener = "http://proves.iesperemaria.com/asteroides/puntuaciones/";
    private static final String urlGrabar = "http://proves.iesperemaria.com/asteroides/puntuaciones/nueva/";
    private ProgressDialog pDialog;

    JSONManager jsonManager = new JSONManager();
    JSONObject jsonObject;
    JSONArray jsonArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnVerPuntuaciones = (Button)findViewById(R.id.btnVerPuntuaciones);
        btnCrearPuntuacion = (Button)findViewById(R.id.btnCrearPuntuacion);
        puntos = (TextView)findViewById(R.id.puntos);

        btnVerPuntuaciones.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mostrarPuntuaciones();
            }

        });
        btnCrearPuntuacion.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                crearPuntuacion();
            }

        });
    }

    private void mostrarPuntuaciones() {

        new PuntuacionesJSON().execute();

    }
    private void crearPuntuacion() {

        Integer puntos = Math.abs(new Random().nextInt(99999));
        Long fecha = System.currentTimeMillis();

        new NuevaPuntuacion().execute(puntos.toString(), "ESTEFANÍA",fecha.toString());
    }


    class PuntuacionesJSON extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Obteniendo puntuaciones...");
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setCancelable(false);
            pDialog.show();

        }
        @Override
        protected String doInBackground(String... params) {
            try {

                return jsonManager.getJsonString(urlObtener,"GET", null);

            } catch (Exception e) {

                e.printStackTrace();

            }
            return null;
        }
        @Override
        protected void onPostExecute(String jsonString) {

            StringBuilder salida = new StringBuilder();
            pDialog.dismiss();

            try {

                jsonObject = new JSONObject(jsonString);
                jsonArray = jsonObject.getJSONArray("puntuaciones");

                for (int i=0; i<jsonArray.length(); i++) {

                    JSONObject nodo = jsonArray.getJSONObject(i);
                    salida.append(nodo.getString("puntos") + " " + nodo.getString("nombre") + "\n");

                }

                puntos.setText(salida.toString());

            } catch (JSONException e) {

                Toast.makeText(getApplicationContext(), "Error accediendo al servicio", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }


    class NuevaPuntuacion extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Almacenando puntuación...");
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @SuppressWarnings("deprecation")
        @Override
        protected String doInBackground(String... params) {

            try {

                List<NameValuePair> parametros = new ArrayList<NameValuePair>();

                parametros.add(new BasicNameValuePair("puntos",params[0]));
                parametros.add(new BasicNameValuePair("nombre",params[1]));
                parametros.add(new BasicNameValuePair("fecha",params[2]));
                return jsonManager.getJsonString(urlGrabar,"POST", parametros);

            } catch (Exception e) {

                e.printStackTrace();

            }
            return null;
        }
        @Override
        protected void onPostExecute(String jsonString) {

            String salida;
            pDialog.dismiss();

            try {

                jsonObject = new JSONObject(jsonString);
                salida = jsonObject.getString("id") + " " + jsonObject.getString("puntos") + " " + jsonObject.getString("nombre");

                puntos.setText(salida);

            } catch (JSONException e) {

                Toast.makeText(getApplicationContext(), "Error accediendo al servicio", Toast.LENGTH_LONG).show();
                e.printStackTrace();

            }
        }
    }


}

