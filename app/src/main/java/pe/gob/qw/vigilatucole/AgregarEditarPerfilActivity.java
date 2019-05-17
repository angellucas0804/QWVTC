package pe.gob.qw.vigilatucole;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ToggleButton;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pe.gob.qw.vigilatucole.application.App;
import pe.gob.qw.vigilatucole.application.BaseActivity;
import pe.gob.qw.vigilatucole.data.Alumno;
import pe.gob.qw.vigilatucole.data.AlumnoDao;
import pe.gob.qw.vigilatucole.data.DaoSession;

public class AgregarEditarPerfilActivity extends BaseActivity {

    @BindView(R.id.et_codigo_modular)
    EditText et_codigo_modular;
    @BindView(R.id.et_nombre_colegio)
    EditText et_nombre_colegio;
    @BindView(R.id.et_nombres_alumno)
    EditText et_nombres_alumno;
    @BindView(R.id.et_apellidos_alumno)
    EditText et_apellidos_alumno;
    @BindView(R.id.tb_municipio)
    ToggleButton tb_municipio;
    @BindView(R.id.sp_sexo)
    Spinner sp_sexo;
    @BindView(R.id.sp_nivel)
    Spinner sp_nivel;
    @BindView(R.id.sp_grado)
    Spinner sp_grado;
    @BindView(R.id.sp_turno)
    Spinner sp_turno;

    private DaoSession daoSession;
    List<Alumno> alumnosList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_editar_perfil);
        ButterKnife.bind(this);
        App app = (App) getApplication();
        daoSession = app.getDaoSession();
        if (getIntent().hasExtra("KEY_EDITAR_PERFIL")){
            int alumnoId = Integer.parseInt(getIntent().getStringExtra("KEY_EDITAR_PERFIL"));
            alumnosList = daoSession.getAlumnoDao().queryBuilder().where(AlumnoDao.Properties.Id.eq(alumnoId)).list();
            et_codigo_modular.setText(alumnosList.get(0).getChCodModular());
            et_nombre_colegio.setText(alumnosList.get(0).getNvColegio());
            et_nombres_alumno.setText(alumnosList.get(0).getNvNombres());
            et_apellidos_alumno.setText(alumnosList.get(0).getNvApePatMat());
            sp_nivel.setSelection(alumnosList.get(0).getInNivel());
            sp_grado.setSelection(alumnosList.get(0).getInGrado());
            sp_turno.setSelection(alumnosList.get(0).getInTurno());
            sp_sexo.setSelection(alumnosList.get(0).getInSexo());
            tb_municipio.setChecked(alumnosList.get(0).getBiMunicipio());
        }
    }

    @OnClick(R.id.btn_editar_agregar)
    public void editarAgregarPerfil() {
        if (validarCampos()) {
            if (getIntent().hasExtra("KEY_EDITAR_PERFIL")){
                editarAlumno();
            }else {
                nuevoAlumno();
            }
            showToastCorrecto("Se guardo correctamente");
            onBackPressed();
        }
    }

    private void editarAlumno() {
        Alumno alumno = alumnosList.get(0);
        ponerDatos(alumno);
        daoSession.getAlumnoDao().update(alumno);
    }

    private void nuevoAlumno(){
        AlumnoDao alumnoDao = daoSession.getAlumnoDao();
        Alumno alumno = new Alumno();
        ponerDatos(alumno);
        alumnoDao.save(alumno);
    }

    private void ponerDatos(Alumno alumno){
        alumno.setChCodModular(et_codigo_modular.getText().toString().trim());
        alumno.setNvColegio(et_nombre_colegio.getText().toString().trim());
        alumno.setNvNombres(et_nombres_alumno.getText().toString().trim());
        alumno.setNvApePatMat(et_apellidos_alumno.getText().toString().trim());
        alumno.setBiMunicipio(tb_municipio.isChecked());
        alumno.setInSexo(sp_sexo.getSelectedItemPosition());
        alumno.setInNivel(sp_nivel.getSelectedItemPosition());
        alumno.setInGrado(sp_grado.getSelectedItemPosition());
        alumno.setInTurno(sp_turno.getSelectedItemPosition());
    }

    private boolean validarCampos() {
        if (et_codigo_modular.getText().toString().trim().isEmpty()) {
            showToastError("Ingrese el Código Modular");
            return false;
        } else if (et_nombre_colegio.getText().toString().trim().isEmpty()) {
            showToastError("Ingrese el Nombre del Colegio");
            return false;
        } else if (et_nombres_alumno.getText().toString().trim().isEmpty()) {
            showToastError("Ingrese sus Nombres");
            return false;
        } else if (et_apellidos_alumno.getText().toString().trim().isEmpty()) {
            showToastError("Ingrese sus Apellidos");
            return false;
        } else if (sp_sexo.getSelectedItemPosition() == 0) {
            showToastError("Eliga su Género");
            return false;
        } else if (sp_nivel.getSelectedItemPosition() == 0) {
            showToastError("Eliga su Nivel");
            return false;
        } else if (sp_grado.getSelectedItemPosition() == 0) {
            showToastError("Eliga su Grado");
            return false;
        } else if (sp_turno.getSelectedItemPosition() == 0) {
            showToastError("Eliga su Turno");
            return false;
        } else {
            return true;
        }
    }
}
