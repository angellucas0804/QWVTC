package pe.gob.qw.vigilatucole.application;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import pe.gob.qw.vigilatucole.R;


@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showToastError(String message){
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);

        //inflate view
        @SuppressLint("InflateParams") View custom_view = getLayoutInflater().inflate(R.layout.toast_icon_text, null);
        ((TextView) custom_view.findViewById(R.id.message)).setText(message);
        ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_close);
        ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(getResources().getColor(R.color.red_600));

        toast.setView(custom_view);
        toast.show();
    }

    public void showToastCorrecto(String message){
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);

        //inflate view
        @SuppressLint("InflateParams") View custom_view = getLayoutInflater().inflate(R.layout.toast_icon_text, null);
        ((TextView) custom_view.findViewById(R.id.message)).setText(message);
        ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_done);
        ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(getResources().getColor(R.color.green_500));

        toast.setView(custom_view);
        toast.show();
    }
}
