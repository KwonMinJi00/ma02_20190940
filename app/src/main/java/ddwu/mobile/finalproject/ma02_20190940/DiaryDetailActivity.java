package ddwu.mobile.finalproject.ma02_20190940;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DiaryDetailActivity extends AppCompatActivity {

    TextView title;
    TextView date;
    TextView memo;
    ImageView img;

    DiaryDBHelper helper;
    Cursor cursor;

    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_detail);

        title = findViewById(R.id.d_title);
        date = findViewById(R.id.d_date);
        memo = findViewById(R.id.d_memo);
        img = findViewById(R.id.d_pic);

        helper = new DiaryDBHelper(this);
    }

    @SuppressLint("Range")
    protected void onResume() {
        super.onResume();

        SQLiteDatabase db = helper.getReadableDatabase();
        cursor = db.rawQuery("select * from " + DiaryDBHelper.TABLE_NAME + " where _id = " + getIntent().getStringExtra("id"), null);
        cursor.moveToNext();

        date.setText(cursor.getString(cursor.getColumnIndex(DiaryDBHelper.COL_DATE)));
        title.setText(cursor.getString(cursor.getColumnIndex(DiaryDBHelper.COL_TITLE)));
        memo.setText(cursor.getString(cursor.getColumnIndex(DiaryDBHelper.COL_MEMO)));
        //Bitmap bitmap = BitmapFactory.decodeFile(cursor.getString(cursor.getColumnIndex(DiaryDBHelper.COL_PATH)));
        //Log.d("detail", cursor.getString(cursor.getColumnIndex(DiaryDBHelper.COL_PATH)));
        //img.setImageBitmap(bitmap);
        path = cursor.getString(cursor.getColumnIndex(DiaryDBHelper.COL_PATH));
        setPic(path);

        helper.close();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_fin:
                finish();
                break;
            case R.id.btn_delete:
                SQLiteDatabase db = helper.getWritableDatabase();
                String whereClause = "date=?";
                String[] whereArgs = new String[]{date.getText().toString()};
                if (db.delete (DiaryDBHelper.TABLE_NAME, whereClause, whereArgs) > 0)
                    Toast.makeText(DiaryDetailActivity.this, "삭제 완료", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(DiaryDetailActivity.this, "삭제 실패", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.btn_update:
                Intent intent = new Intent(DiaryDetailActivity.this, DiaryUpdateActivity.class);
                intent.putExtra("title", title.getText().toString());
                intent.putExtra("date", date.getText().toString());
                intent.putExtra("memo", memo.getText().toString());
                intent.putExtra("pic", img.getResources().toString());
                intent.putExtra("id", getIntent().getStringExtra("id"));
                intent.putExtra("path", path);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        cursor 사용 종료
        if (cursor != null) cursor.close();
    }

    private void setPic(String mCurrentPhotoPath) {
        // Get the dimensions of the View
        int targetW = 350;
        int targetH = 350;

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

}
