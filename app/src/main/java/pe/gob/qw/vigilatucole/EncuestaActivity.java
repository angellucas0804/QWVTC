package pe.gob.qw.vigilatucole;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import pe.gob.qw.vigilatucole.api.QWService;
import pe.gob.qw.vigilatucole.api.RetrofitClient;
import pe.gob.qw.vigilatucole.application.BaseActivity;
import pe.gob.qw.vigilatucole.fragment.EncuestaFragment;
import pe.gob.qw.vigilatucole.fragment.FinEncuestaFragment;
import pe.gob.qw.vigilatucole.model.Encuesta;
import pe.gob.qw.vigilatucole.model.Pregunta;
import pe.gob.qw.vigilatucole.model.Respuesta;
import pe.gob.qw.vigilatucole.util.Constantes;
import pe.gob.qw.vigilatucole.util.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EncuestaActivity extends BaseActivity
        implements EncuestaFragment.OnFragmentInteractionListenerChange,
        FinEncuestaFragment.OnFragmentInteractionListenerEnviar {

    @BindView(R.id.vp_encuesta)
    ViewPager vp_encuesta;

    @BindView(R.id.tv_puntaje_perfil)
    TextView tv_puntaje_perfil;

    @BindView(R.id.tv_puntaje_encuesta)
    TextView tv_puntaje_encuesta;

    Long alumnoId;
    Long puntaje_perfil;
    int turno_alumno;

    private QWService qwService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encuesta);
        ButterKnife.bind(this);
        qwService = RetrofitClient.getRetrofit().create(QWService.class);
        if (getIntent().hasExtra(Constantes.PUTEXTRA_ALUMNO_ID)) {
            alumnoId = getIntent().getLongExtra(Constantes.PUTEXTRA_ALUMNO_ID, Constantes.DEFAULT_VALUE_CERO);
            puntaje_perfil = getIntent().getLongExtra(Constantes.PUTEXTRA_EDITAR_PERFIL, Constantes.DEFAULT_VALUE_CERO);
            turno_alumno = getIntent().getIntExtra(Constantes.PUTEXTRA_ALUMNO_TURNO, Constantes.DEFAULT_VALUE_CERO);
        }
        tv_puntaje_perfil.setText(String.valueOf(puntaje_perfil));
        verPagerAdapter();

    }

    private void verPagerAdapter() {
        EncuestaPagerAdapter encuestaPagerAdapter = new EncuestaPagerAdapter(getSupportFragmentManager(), Utils.cargarEncuestaFromJson(this, turno_alumno));
        vp_encuesta.setAdapter(encuestaPagerAdapter);
    }

    @Override
    public void onFragmentInteractionChange(String s) {
        tv_puntaje_encuesta.setText(s);
    }

    @Override
    public void onFragmentInteractionEnviar(JSONObject jsonObject) {

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
                        showToastCorrecto("Felicidades, enviaste tu encuesta.");
                    } else {
                        showToastError("Ha ocurrido algo, vuelve a intentarlo.");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                showToastError("Error interno: " + t.getMessage());
            }
        });
    }


    public class EncuestaPagerAdapter extends FragmentPagerAdapter {

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
}
