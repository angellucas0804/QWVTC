package pe.gob.qw.vigilatucole.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import pe.gob.qw.vigilatucole.R;
import pe.gob.qw.vigilatucole.application.App;
import pe.gob.qw.vigilatucole.data.Alumno;
import pe.gob.qw.vigilatucole.data.AlumnoDao;
import pe.gob.qw.vigilatucole.data.AlumnoEncuesta;
import pe.gob.qw.vigilatucole.data.AlumnoEncuestaDao;
import pe.gob.qw.vigilatucole.data.AlumnoRespuesta;
import pe.gob.qw.vigilatucole.data.AlumnoRespuestaDao;
import pe.gob.qw.vigilatucole.data.DaoSession;
import pe.gob.qw.vigilatucole.model.Pregunta;
import pe.gob.qw.vigilatucole.model.Respuesta;

public class EncuestaFragment extends Fragment {

    @BindView(R.id.tv_pregunta)
    TextView tv_pregunta;
    @BindView(R.id.tv_numero_pregunta)
    TextView tv_numero_pregunta;
    @BindView(R.id.rb_respuesta_si)
    RadioButton rb_respuesta_si;
    @BindView(R.id.rb_respuesta_no)
    RadioButton rb_respuesta_no;
    @BindView(R.id.iv_portada)
    ImageView iv_portada;
    @BindView(R.id.rg_respuestas)
    RadioGroup rg_respuestas;

    private static final String POSITION = "position";
    private static final String NUMERO_PREGUNTA = "numero_preguntas";
    private static final String TEXTO_PREGUNTA = "texto_pregunta";
    private static final String IMAGEN_PORTADA = "imagen_portada";

    private static final String IMAGEN_RESPUESTA = "imagen_respuesta";
    private static final String TEXTO_RESPUESTA = "texto_respuesta";

    private static final String ALUMNO_ID = "alumno_id";

    DaoSession daoSession;

    private OnFragmentInteractionListener mListener;


    private int position;
    private String texto_pregunta;
    private int numero_pregunta;
    private String portada;
    private ArrayList<String> imagen_respuesta;
    private ArrayList<String> texto_respuesta;
    private Long alumnoId;

    private AlumnoRespuesta alumnoRespuesta;


    public EncuestaFragment() {
        // Required empty public constructor
    }

    public static EncuestaFragment newInstance(Pregunta pregunta, int num, Long alumnoId) {
        EncuestaFragment fragment = new EncuestaFragment();
        Bundle args = new Bundle();
        args.putInt(POSITION, num);
        args.putInt(NUMERO_PREGUNTA, pregunta.getOrden());
        args.putString(TEXTO_PREGUNTA, pregunta.getTexto());
        args.putString(IMAGEN_PORTADA, pregunta.getPortada());

        ArrayList<String> texto_respuestas = new ArrayList<>(pregunta.getRespuestas().size());
        ArrayList<String> imagen_respuestas = new ArrayList<>(pregunta.getRespuestas().size());
        for (Respuesta item : pregunta.getRespuestas()) {
            texto_respuestas.add(item.getTexto());
            imagen_respuestas.add(item.getImagen());
        }

        args.putStringArrayList(IMAGEN_RESPUESTA, imagen_respuestas);
        args.putStringArrayList(TEXTO_RESPUESTA, texto_respuestas);

        args.putLong(ALUMNO_ID, alumnoId);


        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt(POSITION);
            texto_pregunta = getArguments().getString(TEXTO_PREGUNTA);
            portada = getArguments().getString(IMAGEN_PORTADA);
            imagen_respuesta = getArguments().getStringArrayList(IMAGEN_RESPUESTA);
            texto_respuesta = getArguments().getStringArrayList(TEXTO_RESPUESTA);
            alumnoId = getArguments().getLong(ALUMNO_ID);
            numero_pregunta = getArguments().getInt(NUMERO_PREGUNTA);
        }

        App app = (App) getActivity().getApplication();
        daoSession = app.getDaoSession();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_encuesta, container, false);
        ButterKnife.bind(this, view);
        tv_pregunta.setText(texto_pregunta);
        tv_numero_pregunta.setText(String.valueOf(numero_pregunta));
        rb_respuesta_si.setText(texto_respuesta.get(0));
        rb_respuesta_no.setText(texto_respuesta.get(1));
        rb_respuesta_si.setCompoundDrawables(null, stringToDrawableRButton(imagen_respuesta.get(0)), null, null);
        rb_respuesta_no.setCompoundDrawables(null, stringToDrawableRButton(imagen_respuesta.get(1)), null, null);
        iv_portada.setImageResource(stringToDrawableImageView(portada));


        cargarRespuestas();


        rg_respuestas.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_respuesta_si:
                        guardarRespuesta("1", " ");
                        break;
                    case R.id.rb_respuesta_no:
                        guardarRespuesta("2", " ");
                        break;
                }
                //calcularPuntaje();
            }
        });

        return view;
    }

    private void calcularPuntaje() {
        List<Alumno> alumnoList = daoSession.getAlumnoDao().queryBuilder().where(AlumnoDao.Properties.Id.eq(alumnoId)).list();
        Long puntajeActual = alumnoList.get(0).getLngPuntaje();
        Long puntajeAumentado = puntajeActual + 10;
        Alumno alumno = alumnoList.get(0);
        alumno.setLngPuntaje(puntajeAumentado);
        daoSession.getAlumnoDao().update(alumno);
    }

    private void guardarRespuesta(String respuesta, String detalle) {
        AlumnoRespuesta alumnoRespuesta1 = alumnoRespuesta;
        alumnoRespuesta1.setRespuesta(respuesta);
        alumnoRespuesta1.setDetalle(detalle);
        daoSession.getAlumnoRespuestaDao().update(alumnoRespuesta1);
    }


    private void cargarRespuestas() {

        alumnoRespuesta = daoSession.getAlumnoRespuestaDao().queryBuilder().where(AlumnoRespuestaDao.Properties.Alumno_id.eq(alumnoId),
                AlumnoRespuestaDao.Properties.Pregunta_id.eq(numero_pregunta)).limit(1).unique();
        switch (alumnoRespuesta.getRespuesta()) {
            case "1":
                rb_respuesta_si.setChecked(true);
                break;
            case "2":
                rb_respuesta_no.setChecked(true);
                break;
        }
    }

    private Drawable stringToDrawableRButton(String s) {
        int imageId = getResources().getIdentifier(s, "drawable", getActivity().getPackageName());
        Drawable imagedrawable = getResources().getDrawable(imageId);
        imagedrawable.setBounds(0, 0, 196, 196);
        return imagedrawable;
    }

    private Integer stringToDrawableImageView(String s) {
        return getResources().getIdentifier(s, "drawable", getActivity().getPackageName());
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
