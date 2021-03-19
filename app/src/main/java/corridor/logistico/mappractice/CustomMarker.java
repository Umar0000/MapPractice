package corridor.logistico.mappractice;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class CustomMarker {
    public static Bitmap createCustomMarker(final Context context, String _name) {


        View markerItemView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.customemarker_bg, null);
//      CircleImageView markerImage = (CircleImageView) marker.findViewById(R.id.user_dp);
//      markerImage.setImageResource(resource);
        TextView txt_name = (TextView)markerItemView.findViewById(R.id.txt_parkng_name);
        txt_name.setText(_name);
        txt_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "msg Show", Toast.LENGTH_SHORT).show();
            }
        });
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        markerItemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        markerItemView.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        markerItemView.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        markerItemView.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(markerItemView.getMeasuredWidth(), markerItemView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        markerItemView.draw(canvas);


        return bitmap;
    }
    public static Bitmap createCustomMarker2(final Context context, String _name) {


        View markerItemView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.customemarker2, null);
//      CircleImageView markerImage = (CircleImageView) marker.findViewById(R.id.user_dp);
//      markerImage.setImageResource(resource);
        TextView txt_name = (TextView)markerItemView.findViewById(R.id.txt_parkng_name);
        txt_name.setText(_name);
        txt_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "msg Show", Toast.LENGTH_SHORT).show();
            }
        });
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        markerItemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        markerItemView.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        markerItemView.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        markerItemView.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(markerItemView.getMeasuredWidth(), markerItemView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        markerItemView.draw(canvas);


        return bitmap;
    }
}
