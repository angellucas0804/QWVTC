package pe.gob.qw.vigilatucole;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pe.gob.qw.vigilatucole.adapter.PerfilAdapter;
import pe.gob.qw.vigilatucole.application.App;
import pe.gob.qw.vigilatucole.application.BaseActivity;
import pe.gob.qw.vigilatucole.data.Alumno;
import pe.gob.qw.vigilatucole.data.AlumnoEncuesta;
import pe.gob.qw.vigilatucole.data.AlumnoEncuestaDao;
import pe.gob.qw.vigilatucole.data.AlumnoRespuesta;
import pe.gob.qw.vigilatucole.data.AlumnoRespuestaDao;
import pe.gob.qw.vigilatucole.data.DaoSession;
import pe.gob.qw.vigilatucole.model.Encuesta;
import pe.gob.qw.vigilatucole.model.Pregunta;
import pe.gob.qw.vigilatucole.model.Respuesta;
import pe.gob.qw.vigilatucole.util.ItemAnimation;

public class PerfilActivity extends BaseActivity {

    @BindView(R.id.rv_perfil)
    RecyclerView rv_perfil;

    private DaoSession daoSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        ButterKnife.bind(this);
        App app = (App) getApplication();
        daoSession = app.getDaoSession();
        verPerfilList();
    }

    @OnClick(R.id.fab_agregar_perfil)
    public void agregar_perfil() {
        Intent intent = new Intent(PerfilActivity.this, AgregarEditarPerfilActivity.class);
        startActivity(intent);
    }

    private void verPerfilList() {
        PerfilAdapter adapter = new PerfilAdapter(this, daoSession.getAlumnoDao().loadAll(), ItemAnimation.BOTTOM_UP);
        adapter.setlistener(new PerfilAdapter.Encuestalistener() {
            @Override
            public void setItme(Alumno alumno) {
                int count = (int) daoSession.getAlumnoEncuestaDao().queryBuilder().where(AlumnoEncuestaDao.Properties.Alumno_id.eq(alumno.getId())).count();

                if (count > 0) {
                    Intent intent = new Intent(PerfilActivity.this, EncuestaActivity.class)
                            .putExtra("ID_ALUMNO", alumno.getId());
                    startActivity(intent);
                } else if (nuevoAlumnoEncuesta(alumno.getId()) > 0) {
                    Intent intent = new Intent(PerfilActivity.this, EncuestaActivity.class)
                            .putExtra("ID_ALUMNO", alumno.getId());
                    startActivity(intent);
                }


            }
        });
        rv_perfil.setHasFixedSize(true);
        rv_perfil.setLayoutManager(new LinearLayoutManager(this));
        rv_perfil.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        verPerfilList();
    }

    private Long nuevoAlumnoEncuesta(Long id) {
        Date today = new Date();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        String dateToStr = format.format(today);

        AlumnoEncuesta alumnoEncuesta = new AlumnoEncuesta();

        alumnoEncuesta.setAlumno_id(id);
        alumnoEncuesta.setEncuesta_id(1);
        alumnoEncuesta.setFecha_encuesta(dateToStr);
        Long id_encuesta = daoSession.getAlumnoEncuestaDao().insert(alumnoEncuesta);

        Encuesta encuesta = cargarEncuesta();


        for (Pregunta pregunta : encuesta.getPreguntas()) {
            nuevoAlumnoRespuesta(id_encuesta, id, pregunta.getOrden());
        }

        return id_encuesta;

    }

    private void nuevoAlumnoRespuesta(Long id_encuesta, Long id, int orden) {

        AlumnoRespuesta alumnoRespuesta = new AlumnoRespuesta();
        alumnoRespuesta.setAlumno_id(id);
        alumnoRespuesta.setEncuesta_id(id_encuesta);
        alumnoRespuesta.setPregunta_id(orden);
        alumnoRespuesta.setRespuesta("0");
        alumnoRespuesta.setDetalle(" ");

        daoSession.getAlumnoRespuestaDao().insert(alumnoRespuesta);
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
