package pe.gob.qw.vigilatucole;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;

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
import pe.gob.qw.vigilatucole.data.DaoSession;
import pe.gob.qw.vigilatucole.model.Encuesta;
import pe.gob.qw.vigilatucole.model.Pregunta;
import pe.gob.qw.vigilatucole.util.Constantes;
import pe.gob.qw.vigilatucole.util.ItemAnimation;
import pe.gob.qw.vigilatucole.util.Utils;

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
                int cantidadAlumno = (int) daoSession.getAlumnoEncuestaDao().queryBuilder().where(AlumnoEncuestaDao.Properties.Alumno_id.eq(alumno.getId())).count();
                int turnoAlumno = alumno.getInTurno();
                if (cantidadAlumno > 0) {
                    empezarActividad(EncuestaActivity.class, alumno.getId(), alumno.getLngPuntaje(),turnoAlumno);
                } else if (nuevoAlumnoEncuesta(alumno.getId(), turnoAlumno) > 0) {
                    empezarActividad(EncuestaActivity.class, alumno.getId(), alumno.getLngPuntaje(),turnoAlumno);
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

    private Long nuevoAlumnoEncuesta(Long id, int turno) {

        Date today = new Date();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        String dateToStr = format.format(today);

        AlumnoEncuesta alumnoEncuesta = new AlumnoEncuesta();
        alumnoEncuesta.setAlumno_id(id);
        alumnoEncuesta.setEncuesta_id(Constantes.ENCUESTA_ID);
        alumnoEncuesta.setFecha_encuesta(dateToStr);
        Long id_encuesta = daoSession.getAlumnoEncuestaDao().insert(alumnoEncuesta);

        Encuesta encuesta = Utils.cargarEncuestaFromJson(this, turno);

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
        alumnoRespuesta.setRespuesta(0);
        alumnoRespuesta.setDetalle(" ");

        daoSession.getAlumnoRespuestaDao().insert(alumnoRespuesta);
    }

    public void empezarActividad(Class<?> otherActivityClass, Long alumnoId, Long puntaje,int turno) {
        Intent intent = new Intent(getApplicationContext(), otherActivityClass)
                .putExtra(Constantes.PUTEXTRA_ALUMNO_ID, alumnoId)
                .putExtra(Constantes.PUTEXTRA_PUNTAJE_PERFIL, puntaje)
                .putExtra(Constantes.PUTEXTRA_ALUMNO_TURNO, turno);
        startActivity(intent);
    }

}
