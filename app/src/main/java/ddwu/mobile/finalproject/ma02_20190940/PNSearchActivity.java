package ddwu.mobile.finalproject.ma02_20190940;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class PNSearchActivity extends AppCompatActivity {

    EditText nameSearch;
    ListView lvList;
    String gardenListApiAddress;
    String gardenDetailApiAddress;
    NetworkManager networkManager;

    ArrayAdapter<PlantDTO> adapter;
    List<PlantDTO> resultList;

    RegisterActivity rActivity;
    Intent intent;
    DetailDTO detail;

    String name;
    int d_check = 0;
    public final static String TAG = "NetworkAsyncTask";

    public static Activity searchActivity;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pn_search);

        searchActivity = PNSearchActivity.this;
        rActivity = (RegisterActivity) RegisterActivity.registerActivity;

        nameSearch = findViewById(R.id.nameSearch);
        lvList = findViewById(R.id.lvList);

        nameSearch.setText(getIntent().getStringExtra("searchText"));

        resultList = new ArrayList<PlantDTO>();
        adapter = new ArrayAdapter<PlantDTO>(PNSearchActivity.this,android.R.layout.simple_list_item_1, resultList);

        lvList.setAdapter(adapter);
        gardenListApiAddress = getResources().getString(R.string.gardenListUrl);
        gardenDetailApiAddress = getResources().getString(R.string.gardenDetailUrl);
        networkManager = new NetworkManager(PNSearchActivity.this);

        intent = new Intent(PNSearchActivity.this, RegisterActivity.class);
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                d_check = 1;
                final PlantDTO dto = adapter.getItem(i);
                name = dto.getName();
                String addr = gardenDetailApiAddress + "&cntntsNo=" + dto.getCono();
                new NetworkAsyncTask().execute(addr);
            }
        });
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_nameSearch:
                String p_name = nameSearch.getText().toString();
                String addr = gardenListApiAddress;
                try {
                    if (!p_name.equals("") || !p_name.equals(null)) {
                        addr += "&sText=" + URLEncoder.encode(p_name, "UTF-8");
                    }
                    Log.d(TAG, "search address: " + gardenListApiAddress);
                    new NetworkAsyncTask().execute(addr);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    class NetworkAsyncTask extends AsyncTask<String, Void, String> {

        final static String NETWORK_ERR_MSG = "Server Error!";
        ProgressDialog progressDlg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDlg = ProgressDialog.show(PNSearchActivity.this, "Wait", "Downloading...");     // ???????????? ??????????????? ??????
        }

        @Override
        protected String doInBackground(String... strings) {
            String address = strings[0];
            String result = null;
            result = networkManager.downloadData(address);
            //Log.d(TAG, result);
            if (result == null) {
                cancel(true);
                return NETWORK_ERR_MSG;
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDlg.dismiss();  // ???????????? ??????????????? ??????
            adapter.clear();        // ???????????? ???????????? ?????? ????????? ????????? ?????????

//          parser ?????? ??? OpenAPI ?????? ????????? ???????????? parsing ??????
            XmlParser parser = new XmlParser();
            if (d_check == 1) {
                //String resultDetail = parser.detail_parse(result);
                detail = parser.detail_parse(result);

                if (detail == null) {
                    Toast.makeText(PNSearchActivity.this, "????????? ????????????.", Toast.LENGTH_SHORT).show();
                } else {
                    rActivity.finish();
                    intent.putExtra("name", name);
                    intent.putExtra("resultDetail", detail);
                    startActivity(intent);
                }
            } else {
                Log.d(TAG, "post execute" + result);
                resultList = parser.parse(result);
                Log.d(TAG, "parser result" + resultList);

                if (resultList == null) {       // ????????? ????????? ???????????? ???????????? ?????? ??????
                    Toast.makeText(PNSearchActivity.this, "????????? ???????????? ???????????????.", Toast.LENGTH_SHORT).show();
                } else if (resultList.isEmpty()) {
                    Toast.makeText(PNSearchActivity.this, "?????? ????????? ????????????.", Toast.LENGTH_SHORT).show();
                } else {
                    adapter.addAll(resultList);     // ??????????????? ???????????? ?????? ???????????? parsing ?????? ArrayList ??? ??????
                }
            }
        }


        @Override
        protected void onCancelled(String msg) {
            super.onCancelled();
            progressDlg.dismiss();
            Toast.makeText(PNSearchActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    }
}
