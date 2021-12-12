package ddwu.mobile.finalproject.ma02_20190940;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DiaryActivity extends AppCompatActivity {
    ListView diaryList;
    DiaryDBHelper helper;
    SimpleCursorAdapter adapter;
    String name;
    long id;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_list);
        diaryList = findViewById(R.id.diaryList);
        helper = new DiaryDBHelper(this);

        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null,
                new String[] {DiaryDBHelper.COL_DATE}, new int[] {android.R.id.text1},
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        diaryList.setAdapter(adapter);

        TextView title = findViewById(R.id.diary_title);
        name = getIntent().getStringExtra("name");
        title.setText(name + "'s diary");

        diaryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("Range")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(DiaryActivity.this, DiaryDetailActivity.class);
//                SQLiteDatabase db = helper.getReadableDatabase();
//                cursor = db.rawQuery("select * from " + DiaryDBHelper.TABLE_NAME + " where _id = " + l, null);
//                cursor.moveToNext();

                intent.putExtra("id", String.valueOf(l));
//                intent.putExtra("date", cursor.getString(cursor.getColumnIndex(DiaryDBHelper.COL_DATE)));
//                intent.putExtra("title", cursor.getString(cursor.getColumnIndex(DiaryDBHelper.COL_TITLE)));
//                intent.putExtra("memo", cursor.getString(cursor.getColumnIndex(DiaryDBHelper.COL_MEMO)));
                startActivity(intent);
            }
        });
    }

    protected void onResume() {
        super.onResume();
        SQLiteDatabase db = helper.getReadableDatabase();

        cursor = db.rawQuery("select * from " + DiaryDBHelper.TABLE_NAME + " where name = " + "'" + name + "'", null);

        adapter.changeCursor(cursor);
        helper.close();
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.add_diary:
                Intent intent = new Intent(DiaryActivity.this, DiaryAddActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("id", id);
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

}
