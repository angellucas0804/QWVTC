package pe.gob.qw.vigilatucole.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.OnClick;
import pe.gob.qw.vigilatucole.R;
import pe.gob.qw.vigilatucole.application.App;

import pe.gob.qw.vigilatucole.data.Alumno;
import pe.gob.qw.vigilatucole.data.AlumnoEncuesta;
import pe.gob.qw.vigilatucole.data.AlumnoRespuesta;
import pe.gob.qw.vigilatucole.util.Constantes;
import pe.gob.qw.vigilatucole.data.AlumnoDao;
import pe.gob.qw.vigilatucole.data.AlumnoEncuestaDao;
import pe.gob.qw.vigilatucole.data.AlumnoRespuestaDao;
import pe.gob.qw.vigilatucole.data.DaoSession;



public class FinEncuestaFragment extends Fragment {

    private static final String ALUMNO_ID = "alumnoId";

    private Long alumnoId;
    DaoSession daoSession;

    private OnFragmentInteractionListenerEnviar mListenerEnviar;

    public FinEncuestaFragment() {
        // Required empty public constructor
    }

    public static FinEncuestaFragment newInstance(Long alumnoId) {
        FinEncuestaFragment fragment = new FinEncuestaFragment();
        Bundle args = new Bundle();
        args.putLong(ALUMNO_ID, alumnoId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            alumnoId = getArguments().getLong(ALUMNO_ID);
        }
        App app = (App) Objects.requireNonNull(getActivity()).getApplication();
        daoSession = app.getDaoSession();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fin_encuesta, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.btn_enviar_encuesta)
    public void terminarEncuesta() {

        List<AlumnoRespuesta> alumnoRespuestaList = daoSession.getAlumnoRespuestaDao().queryBuilder().where(AlumnoRespuestaDao.Properties.Alumno_id.eq(alumnoId)).list();
        Alumno alumno = daoSession.getAlumnoDao().queryBuilder().where(AlumnoDao.Properties.Id.eq(alumnoId)).limit(1).unique();
        AlumnoEncuesta alumnoEncuesta = daoSession.getAlumnoEncuestaDao().queryBuilder().where(AlumnoEncuestaDao.Properties.Alumno_id.eq(alumnoId)).limit(1).unique();

        JSONObject jsonResponse = new JSONObject();
        try {
            jsonResponse.put("nombres", alumno.getNvNombres());
            jsonResponse.put("apellidos", alumno.getNvApePatMat());
            jsonResponse.put("sexo", alumno.getInSexo());
            jsonResponse.put("modular", alumno.getChCodModular());
            jsonResponse.put("colegio", alumno.getNvColegio());
            jsonResponse.put("turno", alumno.getInTurno());
            jsonResponse.put("nivel", alumno.getInNivel());
            jsonResponse.put("grado", alumno.getInGrado());
            jsonResponse.put("miembroDelMunicipio", alumno.getBiMunicipio());
            jsonResponse.put("encuestaId", Constantes.ENCUESTA_ID);
            jsonResponse.put("datFecha", alumnoEncuesta.getFecha_encuesta());

            JSONArray arrayRespuestas = new JSONArray();
            for (AlumnoRespuesta item : alumnoRespuestaList) {
                if (item.getRespuesta() == 0) {
                    showToastError("Debe responder la pregunta " + item.getPregunta_id());
                    return;
                }
                JSONObject jsonRespuestas = new JSONObject();
                jsonRespuestas.put("pregunta", item.getPregunta_id());
                jsonRespuestas.put("respuesta", item.getRespuesta());
                jsonRespuestas.put("detalle", item.getDetalle());
                arrayRespuestas.put(jsonRespuestas);
            }

            jsonResponse.put("respuestas", arrayRespuestas);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        enviarEncuesta(jsonResponse);
    }

    private void showToastError(String message) {
        Toast toast = new Toast(getContext());
        toast.setDuration(Toast.LENGTH_LONG);
        @SuppressLint("InflateParams") View custom_view = getLayoutInflater().inflate(R.layout.toast_icon_text, null);
        ((TextView) custom_view.findViewById(R.id.message)).setText(message);
        ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_close);
        ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(getResources().getColor(R.color.red_600));
        toast.setView(custom_view);
        toast.show();
    }


    public void enviarEncuesta(JSONObject jsonObject) {
        if (mListenerEnviar != null) {
            mListenerEnviar.onFragmentInteractionEnviar(jsonObject);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListenerEnviar) {
            mListenerEnviar = (OnFragmentInteractionListenerEnviar) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListenerEnviar = null;
    }

    public interface OnFragmentInteractionListenerEnviar {
        void onFragmentInteractionEnviar(JSONObject jsonObject);
    }
}
