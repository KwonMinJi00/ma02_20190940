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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DiaryUpdateActivity extends AppCompatActivity {
    private final static int REQUEST_TAKE_THUMBNAIL = 100;
    private static final int REQUEST_TAKE_PHOTO = 200;

    private String mCurrentPhotoPath;
    int targetW;
    int targetH;

    TextView title;
    TextView date;
    TextView memo;
    ImageView img;

    DiaryDBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_update);

        title = findViewById(R.id.new_title);
        date = findViewById(R.id.new_date);
        memo = findViewById(R.id.new_memo);
        img = findViewById(R.id.new_pic);

        title.setText(getIntent().getStringExtra("title"));
        date.setText(getIntent().getStringExtra("date"));
        memo.setText(getIntent().getStringExtra("memo"));
        setPic(getIntent().getStringExtra("path"));

        helper = new DiaryDBHelper(this);
    }



    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pic2:
                dispatchTakePictureIntent();
                break;
            case R.id.update_fin:
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues row = new ContentValues();

                row.put(DiaryDBHelper.COL_TITLE, title.getText().toString());
                row.put(DiaryDBHelper.COL_DATE, date.getText().toString());
                row.put(DiaryDBHelper.COL_MEMO, memo.getText().toString());
                row.put(DiaryDBHelper.COL_PATH, mCurrentPhotoPath);

                String whereClause = "_id=?";
                String[] whereArgs = new String[] {getIntent().getStringExtra("id")};

                if (db.update(DiaryDBHelper.TABLE_NAME, row, whereClause, whereArgs) > 0)
                    Toast.makeText(this, "수정 완료", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "수정 실패", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.update_cancel:
                finish();
                break;
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;

            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(this,
                        "ddwu.mobile.finalproject.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }

    }


    private void setPic(String mCurrentPhotoPath) {
        // Get the dimensions of the View
        targetW = 350;
        targetH = 350;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        Bitmap b = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
//        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        img.setImageBitmap(bitmap);
    }


    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_THUMBNAIL && resultCode == RESULT_OK) {
            Bundle extra = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extra.get("data");
            img.setImageBitmap(imageBitmap);
        } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            setPic(mCurrentPhotoPath);
        }
    }
}
