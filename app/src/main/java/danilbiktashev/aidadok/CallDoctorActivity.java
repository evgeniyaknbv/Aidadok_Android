package danilbiktashev.aidadok;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CallDoctorActivity extends AppCompatActivity {

    CallDoctorAdapter boxAdapter;
    Map<String, String> districts = new HashMap<>();
    ListView callDoctorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_doctor);

        addDistricts();
        callDoctorList = (ListView) findViewById(R.id.callDoctorList);
        callDoctorList.setOnItemClickListener(new ItemList());

        ArrayList<String> namesDistrict = (new ArrayList<String>(districts.values()));
        boxAdapter = new CallDoctorAdapter(this, namesDistrict);
        // настраиваем список
        callDoctorList.setAdapter(boxAdapter);

        checkForCall();
    }

    void checkForCall() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CALL_PHONE},123);
        }
    }

    class ItemList implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TextView name = (TextView) view.findViewById(R.id.title);
            for (Map.Entry<String,String> allDistricts : districts.entrySet()) {
                if (allDistricts.getValue().equals((String) name.getText())) {
                    String phoneNumber = allDistricts.getKey();
                    makeCall(phoneNumber,CallDoctorActivity.this);
                }
            }
        }
    }

    void addDistricts() {
        districts.put("84952764666","ВАО Москвы");
        districts.put("84991446279","ЗАО Москвы");
        districts.put("84992100303","Зеленоград");
        districts.put("84956204233","Неизвестный округ");
        districts.put("84999770100","САО Москвы");
        districts.put("84954118484","СВАО Москвы");
        districts.put("84959495252","СЗАО Москвы");
        districts.put("84959156570","ЦАО Москвы");
        districts.put("84953840640","ЮАО Москвы");
        districts.put("84956989120","ЮВАО Москвы");
        districts.put("84957129999","ЮЗАО Москвы");
    }

    public static void makeCall(String phone, Activity activity) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phone));


        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.CALL_PHONE},123);
        }
        else
        {
            activity.startActivity(callIntent);
        }
    }

    public void backButtonAction (View view) {
        finish();
    }

}
