package pe.gob.qw.vigilatucole;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

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
import pe.gob.qw.vigilatucole.application.App;
import pe.gob.qw.vigilatucole.application.BaseActivity;
import pe.gob.qw.vigilatucole.data.DaoSession;
import pe.gob.qw.vigilatucole.fragment.EncuestaFragment;
import pe.gob.qw.vigilatucole.fragment.FinEncuestaFragment;
import pe.gob.qw.vigilatucole.model.Encuesta;
import pe.gob.qw.vigilatucole.model.Pregunta;
import pe.gob.qw.vigilatucole.model.Respuesta;

public class EncuestaActivity
        extends BaseActivity
        implements EncuestaFragment.OnFragmentInteractionListener,
        FinEncuestaFragment.OnFragmentInteractionListener {

    @BindView(R.id.vp_encuesta)
    ViewPager vp_encuesta;

    private DaoSession daoSession;
    Long alumnoId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encuesta);
        ButterKnife.bind(this);
        App app = (App) getApplication();
        daoSession = app.getDaoSession();
        if (getIntent().hasExtra("ID_ALUMNO")) {
            alumnoId = getIntent().getLongExtra("ID_ALUMNO", 0);
        }
        EncuestaPagerAdapter encuestaPagerAdapter = new EncuestaPagerAdapter(getSupportFragmentManager(), cargarEncuesta());
        vp_encuesta.setAdapter(encuestaPagerAdapter);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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

    private String obteneEncuestaFromJson(Context context) {
        String json;
        try {
            InputStream is = context.getAssets().open("encuesta1.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    private Encuesta cargarEncuesta() {
        Encuesta encuesta = new Encuesta();
        try {
            JSONObject jsonEncuesta = new JSONObject(obteneEncuestaFromJson(getApplication()));
            List<Pregunta> preguntaList = new ArrayList<>();
            List<Respuesta> respuestaList = new ArrayList<>();
            encuesta.setNombre(jsonEncuesta.getString("nombre"));
            JSONArray arrayPreguntas = jsonEncuesta.getJSONArray("preguntas");
            for (int i = 0; i < arrayPreguntas.length(); i++) {
                Pregunta pregunta = new Pregunta();
                pregunta.setOrden(arrayPreguntas.getJSONObject(i).getInt("orden"));
                pregunta.setPortada(arrayPreguntas.getJSONObject(i).getString("portada"));
                pregunta.setTexto(arrayPreguntas.getJSONObject(i).getString("texto"));
                preguntaList.add(pregunta);
                encuesta.setPreguntas(preguntaList);
                JSONArray arrayRespuestas = arrayPreguntas.getJSONObject(i).getJSONArray("respuestas");
                for (int a = 0; a < arrayRespuestas.length(); a++) {
                    Respuesta respuesta = new Respuesta();
                    respuesta.setImagen(arrayRespuestas.getJSONObject(a).getString("imagen"));
                    respuesta.setTexto(arrayRespuestas.getJSONObject(a).getString("texto"));
                    respuesta.setValor(arrayRespuestas.getJSONObject(a).getInt("valor"));
                    respuestaList.add(respuesta);
                    pregunta.setRespuestas(respuestaList);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return encuesta;
    }
}
