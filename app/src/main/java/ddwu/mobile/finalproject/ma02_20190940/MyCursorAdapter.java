package ddwu.mobile.finalproject.ma02_20190940;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import java.io.InputStream;

public class MyCursorAdapter extends CursorAdapter {
    LayoutInflater inflater;
    int layout;

    public MyCursorAdapter(Context context, int layout, Cursor c) {
        super(context, c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layout = layout;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = inflater.inflate(layout, parent, false);
        ViewHolder holder = new ViewHolder();
        view.setTag(holder);
        return view;
    }

    @SuppressLint("Range")
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder holder = (ViewHolder) view.getTag();

        if (holder.plantName == null) {
            holder.plantName = view.findViewById(R.id.pl_name);
            holder.plantCover = view.findViewById(R.id.pl_cover);
        }

        holder.plantName.setText(cursor.getString(cursor.getColumnIndex(PlantDBHelper.COL_NAME)));
        holder.plantCover.setImageResource(R.drawable.ic_launcher_foreground);
    }

    static class ViewHolder {

        public ViewHolder() {
            plantName = null;
            plantCover = null;
        }

        TextView plantName;
        ImageView plantCover;
    }
}
