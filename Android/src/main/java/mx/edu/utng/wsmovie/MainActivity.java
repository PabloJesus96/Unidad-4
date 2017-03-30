package mx.edu.utng.wsmovie;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText etName;
    private EditText etSinopsis;
    private EditText etPrice;
    private ToggleButton tbType;
    private Button btSave;
    private Button btList;

    private Movie movie = null;

    final String NAMESPACE = "http://ws.utng.edu.mx";
    final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
    static String URL = "http://172.16.12.117:8080/WSMovie/services/MovieWS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
    }

    private void initComponents() {
        etName = (EditText) findViewById(R.id.et_name);
        etSinopsis = (EditText) findViewById(R.id.et_sinopsis);
        etPrice = (EditText) findViewById(R.id.et_price);
        tbType = (ToggleButton) findViewById(R.id.tb_type);
        btSave = (Button) findViewById(R.id.bt_save);
        btList = (Button) findViewById(R.id.bt_list);
        btSave.setOnClickListener(this);
        btList.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movie_w, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.action_settings){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==btSave.getId()){
            try{
                if(getIntent().getExtras().getString("action").equals("update")){
                    TaskWSUpdate update = new TaskWSUpdate();
                    update.execute();
                }
            }catch(Exception e){
                TaskWSInsert insert = new TaskWSInsert();
                insert.execute();
            }
        }
        if(btList.getId()==v.getId()){
            startActivity(new Intent(MainActivity.this, ListMovies.class));
        }
    }

    private class TaskWSInsert extends AsyncTask<String, Integer, Boolean>{

        @Override
        protected Boolean doInBackground(String... params) {
            boolean result = true;
            final String METHOD_NAME = "addMovie";
            final String SOAP_ACTION = NAMESPACE +"/"+METHOD_NAME;

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            movie = new Movie();
            movie.setProperty(0,0);
            getData();
            PropertyInfo info = new PropertyInfo();
            info.setName("movie");
            info.setValue(movie);
            info.setType(movie.getClass());
            request.addProperty(info);
            envelope.setOutputSoapObject(request);
            envelope.addMapping(NAMESPACE,"Movie", Movie.class);

            MarshalFloat aFloat = new MarshalFloat();
            aFloat.register(envelope);

            HttpTransportSE transportSE = new HttpTransportSE(URL);
            try{
                transportSE.call(SOAP_ACTION, envelope);
                SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
                String res = response.toString();
                if(!res.equals("1")){
                    result = false;
                }
            }catch (Exception e){
                Log.e("Error ", e.getMessage());
                result = false;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result){
                Toast.makeText(getApplicationContext(), "Successful registration.", Toast.LENGTH_SHORT).show();
                clearData();
            }else{
                Toast.makeText(getApplicationContext(), "Error inserting.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class TaskWSUpdate extends AsyncTask<String, Integer, Boolean>{

        @Override
        protected Boolean doInBackground(String... params) {
            boolean result = true;
            final String METHOD_NAME = "editMovie";
            final String SOAP_ACTION = NAMESPACE + "/" + METHOD_NAME;
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            movie = new Movie();
            movie.setProperty(0, getIntent().getExtras().getString("value0"));
            getData();
            PropertyInfo info = new PropertyInfo();
            info.setName("movie");
            info.setValue(movie);
            info.setType(movie.getClass());
            request.addProperty(info);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.addMapping(NAMESPACE, "Movie", movie.getClass());
            MarshalFloat aFloat = new MarshalFloat();
            aFloat.register(envelope);
            HttpTransportSE transportSE = new HttpTransportSE(URL);
            try{
                transportSE.call(SOAP_ACTION, envelope);
                SoapPrimitive result_xml = (SoapPrimitive) envelope.getResponse();
                String res = result_xml.toString();
                if(!res.equals("1")){
                    result = false;
                }
            }catch(HttpResponseException e){
                Log.e("Error HTTP ", e.toString());
            }catch(IOException e){
                Log.e("Error IO ", e.toString());
            }catch(XmlPullParserException e){
                Log.e("Error XmlPullParse ", e.toString());
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result){
                Toast.makeText(getApplicationContext(), "Updated OK", Toast.LENGTH_SHORT).show();
                clearData();
            }else{
                Toast.makeText(getApplicationContext(), "Error updating", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getData(){
        movie.setProperty(1, etName.getText().toString());
        movie.setProperty(2, etSinopsis.getText().toString());
        movie.setProperty(3, Float.parseFloat(etPrice.getText().toString()));
        if(tbType.isChecked()){
            movie.setProperty(4,2);
        }else{
            movie.setProperty(4,1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle backData = this.getIntent().getExtras();
        try{
            Log.i("Data ", backData.getString("value4"));

            etName.setText(backData.getString("value1"));
            etSinopsis.setText(backData.getString("value2"));
            etPrice.setText(backData.getString("value3"));
            if(backData.getString("value4").equals("1")){
                tbType.setChecked(false);
            }else{
                tbType.setChecked(true);
            }
        }catch(Exception e){
            Log.e("Error al recargar", e.toString());
        }
    }

    public void clearData(){
        etName.setText("");
        etSinopsis.setText("");
        etPrice.setText("");
    }
}
