package ddwu.mobile.finalproject.ma02_20190940;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DiaryUpdateActivity extends AppCompatActivity {
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

        helper = new DiaryDBHelper(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_fin:
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues row = new ContentValues();

                row.put(DiaryDBHelper.COL_TITLE, title.getText().toString());
                row.put(DiaryDBHelper.COL_DATE, date.getText().toString());
                row.put(DiaryDBHelper.COL_MEMO, memo.getText().toString());

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
}
