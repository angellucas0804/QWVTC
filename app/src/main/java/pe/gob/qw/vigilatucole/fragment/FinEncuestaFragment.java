package pe.gob.qw.vigilatucole.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.greenrobot.greendao.query.DeleteQuery;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pe.gob.qw.vigilatucole.PerfilActivity;
import pe.gob.qw.vigilatucole.R;
import pe.gob.qw.vigilatucole.api.QWService;
import pe.gob.qw.vigilatucole.api.RetrofitClient;
import pe.gob.qw.vigilatucole.application.App;
import pe.gob.qw.vigilatucole.data.Alumno;
import pe.gob.qw.vigilatucole.data.AlumnoEncuesta;
import pe.gob.qw.vigilatucole.data.AlumnoEncuestaDao;
import pe.gob.qw.vigilatucole.data.AlumnoRespuesta;
import pe.gob.qw.vigilatucole.data.DaoSession;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FinEncuestaFragment extends Fragment {

    private static final String ALUMNO_ID = "alumnoId";

    private Long alumnoId;
    DaoSession daoSession;

    private QWService mQWService;

    private OnFragmentInteractionListener mListener;

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

        App app = (App) getActivity().getApplication();
        daoSession = app.getDaoSession();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fin_encuesta, container, false);
        ButterKnife.bind(this, view);
        mQWService = RetrofitClient.getRetrofit().create(QWService.class);
        return view;
    }

    @OnClick(R.id.btn_enviar_encuesta)
    public void terminarEncuesta() {
        /*limpiarTablas();
        JSONObject jsonObject = new JSONObject();
        Call<List<Alumno>> alumnoCall = mQWService.postEnviarEncuesta(jsonObject);
        alumnoCall.enqueue(new Callback<List<Alumno>>() {
            @Override
            public void onResponse(@NonNull Call<List<Alumno>> call, @NonNull Response<List<Alumno>> response) {

            }

            @Override
            public void onFailure(@NonNull Call<List<Alumno>> call, @NonNull Throwable t) {

            }
        });*/
        Intent intent = new Intent(getContext(), PerfilActivity.class);
        startActivity(intent);
    }

    private void limpiarTablas() {
        DeleteQuery<AlumnoRespuesta> alumnoRespuestaDeleteQuery = daoSession.getAlumnoRespuestaDao().queryBuilder().where(AlumnoEncuestaDao.Properties.Alumno_id.eq(alumnoId)).buildDelete();
        DeleteQuery<AlumnoEncuesta> alumnoEncuestaDeleteQuery = daoSession.getAlumnoEncuestaDao().queryBuilder().where(AlumnoEncuestaDao.Properties.Alumno_id.eq(alumnoId)).buildDelete();
        alumnoRespuestaDeleteQuery.executeDeleteWithoutDetachingEntities();
        alumnoEncuestaDeleteQuery.executeDeleteWithoutDetachingEntities();
        daoSession.clear();
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
