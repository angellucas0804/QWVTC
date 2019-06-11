package com.prismaperu.vigilatucole;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.prismaperu.vigilatucole.data.AlumnoDao;
import com.prismaperu.vigilatucole.data.DaoSession;
import com.prismaperu.vigilatucole.vigilatucole.R;

import com.prismaperu.vigilatucole.application.App;
import com.prismaperu.vigilatucole.application.BaseActivity;
import com.prismaperu.vigilatucole.data.Alumno;
import com.prismaperu.vigilatucole.util.Constantes;
import com.prismaperu.vigilatucole.util.Utils;

public class AgregarEditarPerfilActivity extends BaseActivity {

    @BindView(R.id.et_codigo_modular)
    EditText et_codigo_modular;
    @BindView(R.id.et_nombre_colegio)
    EditText et_nombre_colegio;
    @BindView(R.id.et_nombres_alumno)
    EditText et_nombres_alumno;
    @BindView(R.id.et_apellidos_alumno)
    EditText et_apellidos_alumno;
    @BindView(R.id.rb_perfil_si)
    RadioButton rb_perfil_si;
    @BindView(R.id.rb_perfil_no)
    RadioButton rb_perfil_no;
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
    private boolean editarPerfil = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_editar_perfil);
        ButterKnife.bind(this);
        App app = (App) getApplication();
        daoSession = app.getDaoSession();
        setTitle("Datos del alumno");
        if (getIntent().hasExtra(Constantes.PUTEXTRA_ALUMNO_ID)) {
            editarPerfil = true;
            long alumnoId = getIntent().getLongExtra(Constantes.PUTEXTRA_ALUMNO_ID, Constantes.DEFAULT_VALUE_CERO);
            alumnosList = daoSession.getAlumnoDao().queryBuilder().where(AlumnoDao.Properties.Id.eq(alumnoId)).list();
            et_codigo_modular.setText(alumnosList.get(0).getChCodModular());
            et_nombre_colegio.setText(alumnosList.get(0).getNvColegio());
            et_nombres_alumno.setText(alumnosList.get(0).getNvNombres());
            et_apellidos_alumno.setText(alumnosList.get(0).getNvApePatMat());
            sp_nivel.setSelection(alumnosList.get(0).getInNivel());
            sp_grado.setSelection(alumnosList.get(0).getInGrado());
            sp_turno.setSelection(alumnosList.get(0).getInTurno());
            sp_sexo.setSelection(alumnosList.get(0).getInSexo());
            rb_perfil_si.setChecked(alumnosList.get(0).getBiMunicipio());
            rb_perfil_no.setChecked(!alumnosList.get(0).getBiMunicipio());
        }
        sp_nivel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1: //PRIMARIA
                        List<String> gradosPrimaria = Arrays.asList(getResources().getStringArray(R.array.gradoPrimaria));
                        ArrayAdapter<String> spinnerArrayAdapterP = new ArrayAdapter<>(AgregarEditarPerfilActivity.this, android.R.layout.simple_spinner_item, gradosPrimaria);
                        spinnerArrayAdapterP.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        sp_grado.setAdapter(spinnerArrayAdapterP);
                        if (editarPerfil) {
                            sp_grado.setSelection(alumnosList.get(0).getInGrado());
                        }
                        break;
                    case 2: //SECUNDARIA
                        List<String> gradosSecundaria = Arrays.asList(getResources().getStringArray(R.array.gradoSecundaria));
                        ArrayAdapter<String> spinnerArrayAdapterS = new ArrayAdapter<>(AgregarEditarPerfilActivity.this, android.R.layout.simple_spinner_item, gradosSecundaria);
                        spinnerArrayAdapterS.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        sp_grado.setAdapter(spinnerArrayAdapterS);
                        if (editarPerfil) {
                            sp_grado.setSelection(alumnosList.get(0).getInGrado());
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @OnClick(R.id.btn_editar_agregar)
    public void editarAgregarPerfil() {
        if (validarCampos()) {
            if (getIntent().hasExtra(Constantes.PUTEXTRA_EDITAR_PERFIL)) {
                editarAlumno();
            } else {
                nuevoAlumno();
            }
            showToastCorrecto("Se guardo correctamente");
            editarPerfil = false;
            onBackPressed();
        }
    }

    private void editarAlumno() {
        Alumno alumno = alumnosList.get(0);
        ponerDatos(alumno);
        daoSession.getAlumnoDao().update(alumno);
    }

    private void nuevoAlumno() {
        Alumno alumno = new Alumno();
        ponerDatos(alumno);
        alumno.setLngPuntaje(0L);
        daoSession.getAlumnoDao().save(alumno);
    }

    private void ponerDatos(Alumno alumno) {

        alumno.setChCodModular(et_codigo_modular.getText().toString().trim());
        alumno.setNvColegio(et_nombre_colegio.getText().toString().trim());
        alumno.setNvNombres(et_nombres_alumno.getText().toString().trim());
        alumno.setNvApePatMat(et_apellidos_alumno.getText().toString().trim());
        alumno.setBiMunicipio(rb_perfil_si.isChecked());
        alumno.setInSexo(sp_sexo.getSelectedItemPosition());
        alumno.setInNivel(sp_nivel.getSelectedItemPosition());
        alumno.setInGrado(sp_grado.getSelectedItemPosition());
        alumno.setInTurno(sp_turno.getSelectedItemPosition());
    }

    private boolean validarCampos() {
        if (!Utils.esValidoInput(et_codigo_modular)) {
            showToastError("Ingrese el Código Modular");
            return false;
        } else if (!Utils.esValidoInput(et_nombre_colegio)) {
            showToastError("Ingrese el Nombre del Colegio");
            return false;
        } else if (!Utils.esValidoInput(et_nombres_alumno)) {
            showToastError("Ingrese sus Nombres");
            return false;
        } else if (!Utils.esValidoInput(et_apellidos_alumno)) {
            showToastError("Ingrese sus Apellidos");
            return false;
        } else if (!Utils.esValidoSpinner(sp_sexo)) {
            showToastError("Eliga su Género");
            return false;
        } else if (!Utils.esValidoSpinner(sp_nivel)) {
            showToastError("Eliga su Nivel");
            return false;
        } else if (!Utils.esValidoSpinner(sp_grado)) {
            showToastError("Eliga su Grado");
            return false;
        } else if (!Utils.esValidoSpinner(sp_turno)) {
            showToastError("Eliga su Turno");
            return false;
        } else if (!Utils.minSiete(et_codigo_modular)) {
            showToastError("El Código Modular debe tener 7 dígitos.");
            return false;
        } else {
            return true;
        }
    }
}
