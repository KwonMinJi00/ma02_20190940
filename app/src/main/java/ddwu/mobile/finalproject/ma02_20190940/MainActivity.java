package ddwu.mobile.finalproject.ma02_20190940;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    NetworkManager networkManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.register:
                intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.diary:
                intent = new Intent(MainActivity.this, MyPlantListActivity.class);
                startActivity(intent);
                break;
            case R.id.search:
                intent = new Intent(MainActivity.this, MapActivity.class);
                break;
        }
    }
}