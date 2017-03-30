package mx.edu.utng.wsmovie;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by UTNG on 30/03/2017.
 */

public class ListDetailExam  extends ListActivity {

    final String NAMESPACE = "http://ws.utng.edu.mx";
    final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
            SoapEnvelope.VER11);

    private ArrayList<DetailExam> detailExams = new ArrayList<DetailExam>();
    private int idSelected;
    private int positionSelected;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TaskWSQuery query = new TaskWSQuery();
        query.execute();
        registerForContextMenu(getListView());
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_update:
                DetailExam detailExam = detailExams.get(positionSelected);
                Bundle bundleDetailExam = new Bundle();
                for(int i = 0; i < detailExam.getPropertyCount(); i++){
                    bundleDetailExam.putString("value" + i, detailExam.getProperty(i).toString());
                }
                bundleDetailExam.putString("action", "update");
                Intent intent = new Intent(ListDetailExam.this, DetailExamWS.class);
                intent.putExtras(bundleDetailExam);
                startActivity(intent);
                return  true;

            case R.id.item_delete:
                TaskWSDelete delete = new TaskWSDelete();
                delete.execute();
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(getApplicationContext());
        menuInflater.inflate(R.menu.menu_back, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_back:
                startActivity(new Intent(ListDetailExam.this,DetailExamWS.class));
                break;
            default:
                break;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    private class TaskWSQuery extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            boolean result = true;
            final String METHOD_NAME = "getDetailExams";
            final String SOAP_ACTION = NAMESPACE+"/"+METHOD_NAME;
            detailExams.clear();
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            envelope.setOutputSoapObject(request);
            HttpTransportSE transportSE = new HttpTransportSE(DetailExamWS.URL);
            try{
                transportSE.call(SOAP_ACTION, envelope);
                Vector<SoapObject> response = (Vector<SoapObject>) envelope.getResponse();

                if(response!=null){
                    for(SoapObject soapObject : response){
                        DetailExam detailExam = new DetailExam();
                        detailExam.setProperty(0, Integer.parseInt(soapObject.getProperty("id").toString()));
                        detailExam.setProperty(1, soapObject.getProperty("cveMatter").toString());
                        detailExam.setProperty(2, soapObject.getProperty("cveContent").toString());
                        detailExam.setProperty(3, soapObject.getProperty("cveResult").toString());
                        detailExam.setProperty(4, soapObject.getProperty("cveQuestion").toString());
                        detailExam.setProperty(5, soapObject.getProperty("answer").toString());
                        detailExam.setProperty(5, soapObject.getProperty("examDepartament").toString());
                        detailExam.setProperty(4, soapObject.getProperty("reactive").toString());
                        detailExams.add(detailExam);
                    }
                }
            }catch (XmlPullParserException e){
                Log.e("Error XMLPullParser ", e.toString());
                result = false;
            }catch (HttpResponseException e){
                Log.e("Error HTTP ", e.toString());
                result = false;
            }catch(IOException e){
                Log.e("Error IO ", e.toString());
                result = false;
            }catch (ClassCastException e){
                try{
                    SoapObject object = (SoapObject)envelope.getResponse();
                    DetailExam detailExam = new DetailExam();
                    detailExam.setProperty(0, Integer.parseInt(object.getProperty("id").toString()));
                    detailExam.setProperty(1, object.getProperty("cveMatter").toString());
                    detailExam.setProperty(2, object.getProperty("cveContent").toString());
                    detailExam.setProperty(3, object.getProperty("cveResult").toString());
                    detailExam.setProperty(4, object.getProperty("cveQuestion").toString());
                    detailExam.setProperty(5, object.getProperty("answer").toString());
                    detailExam.setProperty(6, object.getProperty("examDepartament").toString());
                    detailExam.setProperty(7, object.getProperty("reactive").toString());
                    detailExams.add(detailExam);
                }catch (SoapFault e1){
                    Log.e("Error SoapFault", e.toString());
                    result = false;
                }
            }
            return result;
        }

        protected void onPostExecute(Boolean result) {

            if (result) {
                final String[] data = new String[detailExams.size()];
                for (int i = 0; i < detailExams.size(); i++) {
                    data[i] = detailExams.get(i).getProperty(0) + " - "
                            + detailExams.get(i).getProperty(1);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        ListDetailExam.this,
                        android.R.layout.simple_list_item_1, data);
                setListAdapter(adapter);
            } else {
                Toast.makeText(getApplicationContext(), "No data found",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

        menu.setHeaderTitle(getListView().getAdapter().getItem(info.position)
                .toString());
        idSelected = (Integer) detailExams.get(info.position).getProperty(0);
        positionSelected = info.position;

        inflater.inflate(R.menu.menu_contextual, menu);

    }



    private class TaskWSDelete extends AsyncTask<String, Integer, Boolean> {

        protected Boolean doInBackground(String... params) {

            boolean result = true;

            final String METHOD_NAME = "removeDetailExam";
            final String SOAP_ACTION = NAMESPACE + "/" + METHOD_NAME;

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("id", idSelected);

            envelope.setOutputSoapObject(request);
            HttpTransportSE transportSE = new HttpTransportSE(DetailExamWS.URL);
            try {
                transportSE.call(SOAP_ACTION, envelope);
                SoapPrimitive result_xml = (SoapPrimitive) envelope
                        .getResponse();
                String res = result_xml.toString();

                if (!res.equals("0")){
                    result = true;
                }

            } catch (Exception e) {
                Log.e("Error", e.toString());
                result = false;
            }

            return result;
        }

        protected void onPostExecute(Boolean result) {

            if (result) {
                Toast.makeText(getApplicationContext(),
                        "Deleted", Toast.LENGTH_SHORT).show();
                TaskWSQuery query = new TaskWSQuery();
                query.execute();
            } else {
                Toast.makeText(getApplicationContext(), "Error deleting",
                        Toast.LENGTH_SHORT).show();

            }
        }
    }

}
