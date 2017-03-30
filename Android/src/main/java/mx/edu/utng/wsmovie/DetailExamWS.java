package mx.edu.utng.wsmovie;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class DetailExamWS extends AppCompatActivity implements View.OnClickListener {
    private EditText etCveMatter;
    private EditText etCveContent;
    private EditText etCveResult;
    private EditText etCveQuestion;
    private EditText etAnswer;
    private EditText etExamDepartament;
    private EditText etReactive;
    private Button btSave;
    private Button btList;

    private DetailExam detailExam = null;

    final String NAMESPACE = "http://ws.utng.edu.mx";
    final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
    static String URL = "http://172.16.12.117:8080/WSMovie/services/DetailExamWS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_exam_ws);

        initComponents();
    }

    private void initComponents() {
        etCveMatter = (EditText) findViewById(R.id.et_matter);
        etCveContent = (EditText) findViewById(R.id.et_content);
        etCveResult = (EditText) findViewById(R.id.et_result);
        etCveQuestion = (EditText) findViewById(R.id.et_question);
        etAnswer = (EditText) findViewById(R.id.et_answer);
        etExamDepartament = (EditText) findViewById(R.id.et_exam_departament);
        etReactive = (EditText) findViewById(R.id.et_reactive);
        btSave = (Button) findViewById(R.id.bt_save);
        btList = (Button) findViewById(R.id.bt_list);
        btSave.setOnClickListener(this);
        btList.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btSave.getId()) {
            try {
                if (getIntent().getExtras().getString("action").equals("update")) {
                    TaskWSUpdateDetailExam update = new TaskWSUpdateDetailExam();
                    update.execute();
                }
            } catch (Exception e) {
                TaskWSInsertDetailExam insert = new TaskWSInsertDetailExam();
                insert.execute();
            }
        }
        if (btList.getId() == v.getId()) {
            startActivity(new Intent(DetailExamWS.this, ListDetailExam.class));
        }
    }

    private class TaskWSInsertDetailExam extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            boolean result = true;
            final String METHOD_NAME = "addDetailExam";
            final String SOAP_ACTION = NAMESPACE + "/" + METHOD_NAME;

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            detailExam = new DetailExam();
            detailExam.setProperty(0, 0);
            getData();
            PropertyInfo info = new PropertyInfo();
            info.setName("detailExam");
            info.setValue(detailExam);
            info.setType(detailExam.getClass());
            request.addProperty(info);
            envelope.setOutputSoapObject(request);
            envelope.addMapping(NAMESPACE, "DetailExam", DetailExam.class);

            HttpTransportSE transportSE = new HttpTransportSE(URL);
            try {
                transportSE.call(SOAP_ACTION, envelope);
                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                String res = response.toString();
                if (!res.equals("1")) {
                    result = false;
                }
            } catch (Exception e) {
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

    private class TaskWSUpdateDetailExam extends AsyncTask<String, Integer, Boolean>{

        @Override
        protected Boolean doInBackground(String... params) {
            boolean result = true;
            final String METHOD_NAME = "editDetailExam";
            final String SOAP_ACTION = NAMESPACE + "/" + METHOD_NAME;
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            detailExam = new DetailExam();
            detailExam.setProperty(0, getIntent().getExtras().getString("value0"));
            getData();
            PropertyInfo info = new PropertyInfo();
            info.setName("detailExam");
            info.setValue(detailExam);
            info.setType(detailExam.getClass());
            request.addProperty(info);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.addMapping(NAMESPACE, "DetailExam", detailExam.getClass());

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
        detailExam.setProperty(1, etCveMatter.getText().toString());
        detailExam.setProperty(2, etCveContent.getText().toString());
        detailExam.setProperty(3, etCveResult.getText().toString());
        detailExam.setProperty(4, etCveQuestion.getText().toString());
        detailExam.setProperty(5, etAnswer.getText().toString());
        detailExam.setProperty(6, etExamDepartament.getText().toString());
        detailExam.setProperty(7, etReactive.getText().toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle backData = this.getIntent().getExtras();
        try{
            Log.i("Data ", backData.getString("value7"));

            etCveMatter.setText(backData.getString("value1"));
            etCveContent.setText(backData.getString("value2"));
            etCveResult.setText(backData.getString("value3"));
            etCveQuestion.setText(backData.getString("value4"));
            etAnswer.setText(backData.getString("value5"));
            etExamDepartament.setText(backData.getString("value6"));
            etReactive.setText(backData.getString("value7"));

        }catch(Exception e){
            Log.e("Error al recargar", e.toString());
        }
    }

    public void clearData(){
        etCveMatter.setText("");
        etCveContent.setText("");
        etCveResult.setText("");
        etCveQuestion.setText("");
        etAnswer.setText("");
        etExamDepartament.setText("");
        etReactive.setText("");
    }
}


