package ddwu.mobile.finalproject.ma02_20190940;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilderFactory;

public class RegisterActivity extends AppCompatActivity {
    static final int REQUEST_CODE = 1;

    EditText name;
    EditText wsp;
    EditText wsu;
    EditText wa;
    EditText ww;
    EditText character;
    ImageView img;

    PlantDBHelper plantDBHelper;

    public static Activity registerActivity;
    PNSearchActivity searchActivity;
    DetailDTO dto;

    private static final int CALL_GALLERY = 0;

    private String mCurrentPhotoPath = "";
    private String mPath;
    private String mFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name = findViewById(R.id.plantName);
        wsp = findViewById(R.id.edit_wsp);
        wsu = findViewById(R.id.edit_wsu);
        wa = findViewById(R.id.edit_wa);
        ww = findViewById(R.id.edit_ww);
        character = findViewById(R.id.character);
//        img = findViewById(R.id.img);

        registerActivity = RegisterActivity.this;
        searchActivity = (PNSearchActivity) PNSearchActivity.searchActivity;
        String n = "";

        n = getIntent().getStringExtra("name");
        dto = (DetailDTO) getIntent().getSerializableExtra("resultDetail");
        if ( n!= null && dto != null) {
            name.setText(n);
            character.setText(dto.getFrtlzr() + "\n" + dto.getPrpgt() + "\n" + dto.getGrwt() + "\n" + dto.getWinterLwTp());
            wsp.setText(dto.getWsp());
            wsu.setText(dto.getWsu());
            wa.setText(dto.getWa());
            ww.setText(dto.getWw());
        }

        plantDBHelper = new PlantDBHelper(this);

        mPath = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/tmp";
        new File(mPath).mkdir();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pn_search:
                Intent intent = new Intent(RegisterActivity.this, PNSearchActivity.class);
                String n = name.getText().toString();
                intent.putExtra("searchText", n);
                startActivity(intent);
                break;
//            case R.id. pic_choice:
//                Intent pic_intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(pic_intent, CALL_GALLERY);
//                break;
            case R.id.btn_register:
                searchActivity.finish();
                SQLiteDatabase db = plantDBHelper.getWritableDatabase();
                ContentValues row = new ContentValues();

                if (name.getText().toString().equals("")) {
                    Toast.makeText(RegisterActivity.this, "이름을 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                else {
                    row.put(PlantDBHelper.COL_NAME, name.getText().toString());
                    row.put(PlantDBHelper.COL_POI, character.getText().toString());
                    row.put(PlantDBHelper.COL_WSP, wsp.getText().toString());
                    row.put(PlantDBHelper.COL_WSU, wsu.getText().toString());
                    row.put(PlantDBHelper.COL_WA, wa.getText().toString());
                    row.put(PlantDBHelper.COL_WW, ww.getText().toString());

                    db.insert(PlantDBHelper.TABLE_NAME, null, row);
                    plantDBHelper.close();
                    finish();
                }
                break;
            case R.id.btn_cancle:
                finish();
                break;
        }
    }

//    protected void onResume() {
//        super.onResume();
//        setImage(uri);
//    }

}