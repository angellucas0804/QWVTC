package pe.gob.qw.vigilatucole.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pe.gob.qw.vigilatucole.R;
import pe.gob.qw.vigilatucole.util.Constantes;

import pe.gob.qw.vigilatucole.data.AlumnoRespuestaDao;
import pe.gob.qw.vigilatucole.data.DaoSession;

import pe.gob.qw.vigilatucole.application.App;
import pe.gob.qw.vigilatucole.data.AlumnoRespuesta;
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

    private OnFragmentInteractionListenerChange mListenerChange;


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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_encuesta, container, false);
        ButterKnife.bind(this, view);
        tv_numero_pregunta.setText(String.valueOf(numero_pregunta));
        rb_respuesta_si.setText(texto_respuesta.get(0));
        rb_respuesta_no.setText(texto_respuesta.get(1));

        if (numero_pregunta == 9) {
            SpannableStringBuilder longDescription = new SpannableStringBuilder();
            longDescription.append("¿Hay alimentos que entrega Qali Warma que ");
            int start = longDescription.length();
            longDescription.append("NO LES GUSTA ");
            longDescription.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), start, longDescription.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            longDescription.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start, longDescription.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            longDescription.setSpan(new AbsoluteSizeSpan(32), start, longDescription.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            longDescription.append("?");
            tv_pregunta.setText(longDescription);
        } else {
            tv_pregunta.setText(texto_pregunta);
        }

        rb_respuesta_si.setCompoundDrawables(null, stringToDrawableRButton(imagen_respuesta.get(0)), null, null);
        rb_respuesta_no.setCompoundDrawables(null, stringToDrawableRButton(imagen_respuesta.get(1)), null, null);
        iv_portada.setImageResource(stringToDrawableImageView(portada));
        cargarRespuestas();
        calcularPuntaje();

        rg_respuestas.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_respuesta_si:
                        if (numero_pregunta == 9) {
                            showCustomDialog();
                        } else {
                            guardarRespuesta(Constantes.RESPUESTA_SI, " ");
                        }
                        break;
                    case R.id.rb_respuesta_no:
                        guardarRespuesta(Constantes.RESPUESTA_NO, " ");
                        break;
                }
                calcularPuntaje();
            }
        });

        return view;
    }

    private void calcularPuntaje() {
        List<AlumnoRespuesta> alumnoRespuestaList = daoSession.getAlumnoRespuestaDao()
                .queryBuilder()
                .where(AlumnoRespuestaDao.Properties.Alumno_id.eq(alumnoId))
                .list();

        int puntaje = 0;
        for (AlumnoRespuesta item : alumnoRespuestaList) {
            if (item.getRespuesta() != 0) {
                puntaje += Constantes.PUNTAJE_PREGUNTA;
            }
        }
        puntajeEncuesta(String.valueOf(puntaje));
    }

    public void puntajeEncuesta(String puntaje) {
        onChangePoints(puntaje);
    }

    private void guardarRespuesta(int respuesta, String detalle) {
        AlumnoRespuesta alumnoRespuesta1 = alumnoRespuesta;
        alumnoRespuesta1.setRespuesta(respuesta);
        alumnoRespuesta1.setDetalle(detalle);
        daoSession.getAlumnoRespuestaDao().update(alumnoRespuesta1);
        sonidoRespuesta();
    }


    private void sonidoRespuesta() {
        MediaPlayer mediaPlayer = MediaPlayer.create(getContext(), R.raw.soundclick3);
        mediaPlayer.start();
    }


    private void cargarRespuestas() {
        alumnoRespuesta = daoSession.getAlumnoRespuestaDao().queryBuilder().where(AlumnoRespuestaDao.Properties.Alumno_id.eq(alumnoId),
                AlumnoRespuestaDao.Properties.Pregunta_id.eq(numero_pregunta)).limit(1).unique();
        switch (alumnoRespuesta.getRespuesta()) {
            case Constantes.RESPUESTA_SI:
                setCheckedRB(rb_respuesta_si);
                break;
            case Constantes.RESPUESTA_NO:
                setCheckedRB(rb_respuesta_no);
                break;
        }
    }

    private void showCustomDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_detail);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final EditText et_dialog = dialog.findViewById(R.id.et_dialog);

        (dialog.findViewById(R.id.btn_guardar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String detalle = et_dialog.getText().toString().trim();
                if (detalle.isEmpty()) {
                    Toast.makeText(getContext(), "Debe escribir que alimentos no les gusta.", Toast.LENGTH_SHORT).show();
                } else {
                    guardarRespuesta(Constantes.RESPUESTA_SI, detalle);
                    dialog.dismiss();
                }
            }
        });

        (dialog.findViewById(R.id.btn_cancelar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCheckedRB(rb_respuesta_no);
                guardarRespuesta(Constantes.RESPUESTA_NO, " ");
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void setCheckedRB(RadioButton rb_respuesta) {
        rb_respuesta.setChecked(true);
    }

    private Drawable stringToDrawableRButton(String s) {
        int imageId = getResources().getIdentifier(s, "drawable", getActivity().getPackageName());
        Drawable imagedrawable = getResources().getDrawable(imageId);
        imagedrawable.setBounds(0, 0, Constantes.TAMANIO_IMAGE_RADIO_BUTTON_X, Constantes.TAMANIO_IMAGE_RADIO_BUTTON_Y);
        return imagedrawable;
    }

    private Integer stringToDrawableImageView(String s) {
        return getResources().getIdentifier(s, "drawable", getActivity().getPackageName());
    }

    public void onChangePoints(String s) {
        if (mListenerChange != null) {
            mListenerChange.onFragmentInteractionChange(s);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListenerChange) {
            mListenerChange = (OnFragmentInteractionListenerChange) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListenerChange = null;
    }

    public interface OnFragmentInteractionListenerChange {
        void onFragmentInteractionChange(String s);
    }
}
