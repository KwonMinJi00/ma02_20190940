package ddwu.mobile.finalproject.ma02_20190940;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MyPlantListActivity extends AppCompatActivity {
    ListView plantList = null;
    PlantDBHelper helper;
    Cursor cursor;
    MyCursorAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_plant_list);
        plantList = (ListView) findViewById(R.id.plantList);

        helper = new PlantDBHelper(this);
        adapter = new MyCursorAdapter(this, R.layout.plant_list_view, null);
        plantList.setAdapter(adapter);

        plantList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("Range")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                final PopupMenu popupMenu = new PopupMenu(getApplicationContext(),view);
                getMenuInflater().inflate(R.menu.popup,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.menu1){
                            SQLiteDatabase db = helper.getReadableDatabase();
                            cursor = db.rawQuery("select p_name from my_plant_table where _id = " + id, null);
                            cursor.moveToNext();
                            String p_name = cursor.getString(cursor.getColumnIndex(PlantDBHelper.COL_NAME));

                            Intent intent = new Intent(MyPlantListActivity.this, DiaryActivity.class);
                            intent.putExtra("name", p_name);
                            startActivity(intent);
                        }else if (menuItem.getItemId() == R.id.menu2){
                            SQLiteDatabase db = helper.getWritableDatabase();
                            String whereClause = "_id=?";
                            String[] whereArgs = new String[]{String.valueOf(id)};
                            if (db.delete (PlantDBHelper.TABLE_NAME, whereClause, whereArgs) > 0)
                                Toast.makeText(MyPlantListActivity.this, "삭제 완료", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(MyPlantListActivity.this, "삭제 실패", Toast.LENGTH_SHORT).show();
                        }
                        onResume();
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        plantList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @SuppressLint("Range")
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                SQLiteDatabase db = helper.getReadableDatabase();
                cursor = db.rawQuery("select * from my_plant_table where _id = " + l, null);
                cursor.moveToNext();
                String detail = "이름:  " + cursor.getString(cursor.getColumnIndex(PlantDBHelper.COL_NAME));
                detail += "\n\n" + cursor.getString(cursor.getColumnIndex(PlantDBHelper.COL_POI));
                detail += "\n\n물주기 봄: " + cursor.getString(cursor.getColumnIndex(PlantDBHelper.COL_WSP));
                detail += "\n\n물주기 여름: " + cursor.getString(cursor.getColumnIndex(PlantDBHelper.COL_WSU));
                detail += "\n\n물주기 가을: " + cursor.getString(cursor.getColumnIndex(PlantDBHelper.COL_WA));
                detail += "\n\n물주기 겨울: " + cursor.getString(cursor.getColumnIndex(PlantDBHelper.COL_WW));

                AlertDialog.Builder oDialog = new AlertDialog.Builder(MyPlantListActivity.this, R.style.MyAlertDialogStyle);

                Typeface tf = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    tf = getResources().getFont(R.font.gmarketsans_ttf_medium);
                }

                TextView title = new TextView(MyPlantListActivity.this);
                title.setText("상세 정보");
                title.setWidth(0);
                title.setHeight(150);
                title.setGravity(Gravity.CENTER);
                title.setTextColor(Color.WHITE);
                title.setTextSize(20);
                title.setTypeface(tf);
                title.setBackgroundColor(Color.rgb(63, 81, 181));

                oDialog.setCustomTitle(title)
                        .setMessage(detail)
                        .setPositiveButton("확인", null)
                        .show();
                return true;
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
//        DB에서 데이터를 읽어와 Adapter에 설정
        SQLiteDatabase db = helper.getReadableDatabase();
        cursor = db.rawQuery("select * from " + PlantDBHelper.TABLE_NAME, null);
        adapter.changeCursor(cursor);
        helper.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        cursor 사용 종료
        if (cursor != null) cursor.close();
    }

}
