package com.prismaperu.vigilatucole;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.prismaperu.vigilatucole.vigilatucole.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.iv_logo)
    ImageView iv_logo;
    @BindView(R.id.tv_titulo)
    TextView tv_titulo;
    @BindView(R.id.tv_subtitulo)
    TextView tv_subtitulo;
    @BindView(R.id.btn_inicio)
    Button btn_inicio;
    @BindView(R.id.civ_logo_2)
    CircleImageView civ_logo_2;

    Animation uptodown, downtoup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        uptodown = AnimationUtils.loadAnimation(this, R.anim.uptodown);
        downtoup = AnimationUtils.loadAnimation(this, R.anim.downtoup);

        animacionMain();
    }

    public void animacionMain() {
        iv_logo.setAnimation(uptodown);
        tv_titulo.setAnimation(uptodown);
        tv_subtitulo.setAnimation(downtoup);
        btn_inicio.setAnimation(downtoup);
        civ_logo_2.setAnimation(uptodown);
    }

    @OnClick(R.id.btn_inicio)
    public void irPerfil() {
        Intent intent = new Intent(MainActivity.this, PerfilActivity.class);
        startActivity(intent);
    }
}
