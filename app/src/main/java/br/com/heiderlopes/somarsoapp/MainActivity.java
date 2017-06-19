package br.com.heiderlopes.somarsoapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class MainActivity extends AppCompatActivity {


    String METHOD_NAME = "calcular";
    String SOAP_ACTION = "";

    String NAMESPACE = "http://heiderlopes.com.br/";
    String SOAP_URL = "http://192.168.2.20:8080/CalculadoraWSService/CalculadoraWS";

    SoapObject request;
    SoapPrimitive calcular;

    ProgressDialog pdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CalcularAsync calcularAsync = new CalcularAsync();
        calcularAsync.execute();
    }

    private class CalcularAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("num1", 1);
            request.addProperty("num2", 1);
            request.addProperty("op", "+");

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            //envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransport = new HttpTransportSE(SOAP_URL);
            try {
                httpTransport.call(SOAP_ACTION, envelope);
                calcular = (SoapPrimitive) envelope.getResponse();
            } catch (Exception e) {
                e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pdialog.dismiss();
            Toast.makeText(getApplicationContext(), "Resultado: " + calcular.toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog = new ProgressDialog(MainActivity.this);
            pdialog.setMessage("Converting...");
            pdialog.show();
        }
    }
}
