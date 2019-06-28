package com.prismaperu.vigilatucole;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import com.prismaperu.vigilatucole.data.AlumnoDao;
import com.prismaperu.vigilatucole.data.AlumnoEncuestaDao;
import com.prismaperu.vigilatucole.data.AlumnoRespuestaDao;
import com.prismaperu.vigilatucole.data.DaoSession;
import com.prismaperu.vigilatucole.vigilatucole.R;

import com.prismaperu.vigilatucole.api.QWService;
import com.prismaperu.vigilatucole.api.RetrofitClient;
import com.prismaperu.vigilatucole.application.App;
import com.prismaperu.vigilatucole.application.BaseActivity;
import com.prismaperu.vigilatucole.data.Alumno;
import com.prismaperu.vigilatucole.data.AlumnoEncuesta;
import com.prismaperu.vigilatucole.data.AlumnoRespuesta;
import com.prismaperu.vigilatucole.fragment.EncuestaFragment;
import com.prismaperu.vigilatucole.fragment.FinEncuestaFragment;
import com.prismaperu.vigilatucole.model.Encuesta;
import com.prismaperu.vigilatucole.util.Constantes;
import com.prismaperu.vigilatucole.util.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EncuestaActivity extends BaseActivity
        implements EncuestaFragment.OnFragmentInteractionListenerChange,
        FinEncuestaFragment.OnFragmentInteractionListenerEnviar,
        TextToSpeech.OnInitListener {

    @BindView(R.id.vp_encuesta)
    ViewPager vp_encuesta;
    @BindView(R.id.tv_puntaje_perfil)
    TextView tv_puntaje_perfil;
    @BindView(R.id.tv_puntaje_encuesta)
    TextView tv_puntaje_encuesta;
    @BindView(R.id.pb_preguntas)
    ProgressBar pb_preguntas;
    @BindView(R.id.iv_anim)
    ImageView iv_anim;
    @BindView(R.id.PuntajeBackView)
    ConstraintLayout PuntajeBackView;
    @BindView(R.id.tv_puntaje_fin_encuesta)
    TextView tv_puntaje_fin_encuesta;

    Long alumnoId;
    Long puntaje_perfil;
    int turno_alumno;

    private QWService qwService;
    private DaoSession daoSession;
    private boolean firstQuestion;
    private Encuesta encuesta;
    private TextToSpeech textToSpeech;
    private TranslateAnimation translateAnimation;
    private EncuestaPagerAdapter encuestaPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encuesta);
        ButterKnife.bind(this);
        qwService = RetrofitClient.getRetrofit().create(QWService.class);
        App app = (App) getApplication();
        daoSession = app.getDaoSession();
        Objects.requireNonNull(getSupportActionBar()).hide();
        if (getIntent().hasExtra(Constantes.PUTEXTRA_ALUMNO_ID)) {
            alumnoId = getIntent().getLongExtra(Constantes.PUTEXTRA_ALUMNO_ID, Constantes.DEFAULT_VALUE_CERO);
            puntaje_perfil = getIntent().getLongExtra(Constantes.PUTEXTRA_EDITAR_PERFIL, Constantes.DEFAULT_VALUE_CERO);
            turno_alumno = getIntent().getIntExtra(Constantes.PUTEXTRA_ALUMNO_TURNO, Constantes.DEFAULT_VALUE_CERO);
        }
        tv_puntaje_perfil.setText(String.valueOf(puntaje_perfil));
        verPagerAdapter();
        firstQuestion = true;
        encuesta = Utils.cargarEncuestaFromJson(EncuestaActivity.this, turno_alumno);
        textToSpeech = new TextToSpeech(this, this);
        translateAnimation = new TranslateAnimation(250.0F, 0.0F, 0.0F, 0.0F);
        translateAnimation.setDuration(3000);
        translateAnimation.setFillAfter(true);
        actualizarFecha();

        vp_encuesta.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                pb_preguntas.setProgress(i + 1);

                if (firstQuestion && (i + 1) == 1) {
                    firstQuestion = false;
                    hablarSpeech(encuesta.getPreguntas().get(i).getTexto());
                    iv_anim.startAnimation(translateAnimation);
                    timeStopAnimation();
                }
                if (encuesta.getPreguntas().size() == i) {
                    PuntajeBackView.setVisibility(View.VISIBLE);
                } else {
                    PuntajeBackView.setVisibility(View.GONE);
                }

            }

            @Override
            public void onPageSelected(int i) {
                if ((i + 1) != 12) {
                    hablarSpeech(encuesta.getPreguntas().get(i).getTexto());
                    iv_anim.startAnimation(translateAnimation);
                    timeStopAnimation();
                }

                if (encuesta.getPreguntas().size() == i) {
                    PuntajeBackView.setVisibility(View.VISIBLE);
                } else {
                    PuntajeBackView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

        verAlertDialog();

    }

    private void actualizarFecha() {
        AlumnoEncuesta alumnoEncuesta = daoSession.getAlumnoEncuestaDao().queryBuilder().where(AlumnoEncuestaDao.Properties.Id.eq(alumnoId)).limit(1).unique();
        alumnoEncuesta.setFecha_encuesta(Utils.dateTimeNowToString());
        daoSession.getAlumnoEncuestaDao().update(alumnoEncuesta);
    }

    public void verAlertDialog() {

        @SuppressLint("InflateParams") View view = LayoutInflater.from(this).inflate(R.layout.dialog_info, null);
        final AlertDialog.Builder dialog = new AlertDialog.Builder(Objects.requireNonNull(this));
        dialog.setView(view);
        dialog.setCancelable(false);
        AppCompatButton btn_Cerrar = view.findViewById(R.id.bt_close);
        dialog.create();
        final AlertDialog ad = dialog.show();

        btn_Cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });

    }

    private void timeStopAnimation() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                iv_anim.clearAnimation();
            }
        }, 3000);
    }

    private void hablarSpeech(String message) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null, "");
        }
    }

    private void verPagerAdapter() {
        encuestaPagerAdapter = new EncuestaPagerAdapter(getSupportFragmentManager(), Utils.cargarEncuestaFromJson(this, turno_alumno));
        vp_encuesta.setAdapter(encuestaPagerAdapter);
        vp_encuesta.setOffscreenPageLimit(1);

    }

    @Override
    public void onFragmentInteractionChange(String s) {
        tv_puntaje_encuesta.setText(s);
        tv_puntaje_fin_encuesta.setText("+ " + s);
    }


    @Override
    public void onFragmentInteractionEnviar(JSONObject jsonObject) {
        showLoading(this);
        RequestBody requestBody = null;
        try {
            requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(String.valueOf(jsonObject))).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Call<Boolean> jsonObjectCall = qwService.postEnviarEncuesta(requestBody);
        jsonObjectCall.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                if (response.body() != null) {
                    if (response.body()) {
                        limpiarAlumanoRespuesta();
                        sumarPuntaje();
                        showToastCorrecto("Felicidades, enviaste tu encuesta.");
                        startActivity(new Intent(EncuestaActivity.this, PerfilActivity.class));
                        finish();
                    } else {
                        showToastError("Ha ocurrido algo, vuelve a intentarlo.");
                    }
                }
                hideLoading();
            }

            @Override
            public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                hideLoading();
                showToastError("Error interno: " + t.getMessage());
            }
        });
    }

    private void sumarPuntaje() {
        long puntajeSumado;
        Alumno alumno = daoSession.getAlumnoDao().queryBuilder().where(AlumnoDao.Properties.Id.eq(alumnoId)).limit(1).unique();
        puntajeSumado = alumno.getLngPuntaje() + Constantes.PUNTAJE_SUMADO;
        alumno.setLngPuntaje(puntajeSumado);
        daoSession.getAlumnoDao().update(alumno);
    }

    private void limpiarAlumanoRespuesta() {


        List<AlumnoRespuesta> alumnoRespuestaList = daoSession.getAlumnoRespuestaDao().queryBuilder().where(AlumnoRespuestaDao.Properties.Alumno_id.eq(alumnoId)).list();
        for (AlumnoRespuesta alumnoRespuesta : alumnoRespuestaList) {
            alumnoRespuesta.setRespuesta(0);
            alumnoRespuesta.setDetalle(" ");
            daoSession.getAlumnoRespuestaDao().update(alumnoRespuesta);
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.isLanguageAvailable(new Locale("es", "PE"));

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Lenguaje no Soportado, configure la asistencia por voz");
            } else {
                Log.e("TTSELSE", "The Language specified is not supported!");
            }
        } else {
            Log.e("TTS", "Fallo en la Inicialización");
        }
    }

    public class EncuestaPagerAdapter extends FragmentStatePagerAdapter {

        Encuesta encuesta;

        EncuestaPagerAdapter(FragmentManager fm, Encuesta encuesta) {
            super(fm);
            this.encuesta = encuesta;
        }

        @Override
        public Fragment getItem(int i) {
            if (i < encuesta.getPreguntas().size()) {
                return EncuestaFragment.newInstance(encuesta.getPreguntas().get(i), i, alumnoId);
            } else {
                return FinEncuestaFragment.newInstance(alumnoId);
            }
        }

        @Override
        public int getCount() {
            return encuesta.getPreguntas().size() + 1;
        }
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}
