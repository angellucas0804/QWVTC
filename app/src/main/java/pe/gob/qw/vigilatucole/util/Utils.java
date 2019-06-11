package pe.gob.qw.vigilatucole.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import pe.gob.qw.vigilatucole.model.Encuesta;
import pe.gob.qw.vigilatucole.model.Pregunta;
import pe.gob.qw.vigilatucole.model.Respuesta;

public class Utils {

    public static Encuesta cargarEncuestaFromJson(Context context, int turno) {
        Encuesta encuesta = new Encuesta();
        try {
            JSONObject jsonEncuesta = new JSONObject(convertirFileToJson(context));
            List<Pregunta> preguntaList = new ArrayList<>();
            encuesta.setNombre(jsonEncuesta.getString("nombre"));
            JSONArray arrayPreguntas = jsonEncuesta.getJSONArray("preguntas");
            for (int i = 0; i < arrayPreguntas.length(); i++) {
                switch (turno){
                    case 1:
                        if (!arrayPreguntas.getJSONObject(i).getString("id").equals("turno_tarde")){
                            List<Respuesta> respuestaList = new ArrayList<>();
                            Pregunta pregunta = new Pregunta();
                            pregunta.setOrden(arrayPreguntas.getJSONObject(i).getInt("orden"));
                            pregunta.setPortada(arrayPreguntas.getJSONObject(i).getString("portada"));
                            pregunta.setTexto(arrayPreguntas.getJSONObject(i).getString("texto"));
                            JSONArray arrayRespuestas = arrayPreguntas.getJSONObject(i).getJSONArray("respuestas");
                            for (int a = 0; a < arrayRespuestas.length(); a++) {
                                Respuesta respuesta = new Respuesta();
                                respuesta.setImagen(arrayRespuestas.getJSONObject(a).getString("imagen"));
                                respuesta.setTexto(arrayRespuestas.getJSONObject(a).getString("texto"));
                                respuesta.setValor(arrayRespuestas.getJSONObject(a).getInt("valor"));
                                respuestaList.add(respuesta);
                                pregunta.setRespuestas(respuestaList);
                            }
                            preguntaList.add(pregunta);
                            encuesta.setPreguntas(preguntaList);
                        }
                        break;
                    case 2:
                        if (!arrayPreguntas.getJSONObject(i).getString("id").equals("turno_mañana")){
                            List<Respuesta> respuestaList = new ArrayList<>();
                            Pregunta pregunta = new Pregunta();
                            pregunta.setOrden(arrayPreguntas.getJSONObject(i).getInt("orden"));
                            pregunta.setPortada(arrayPreguntas.getJSONObject(i).getString("portada"));
                            pregunta.setTexto(arrayPreguntas.getJSONObject(i).getString("texto"));
                            JSONArray arrayRespuestas = arrayPreguntas.getJSONObject(i).getJSONArray("respuestas");
                            for (int a = 0; a < arrayRespuestas.length(); a++) {
                                Respuesta respuesta = new Respuesta();
                                respuesta.setImagen(arrayRespuestas.getJSONObject(a).getString("imagen"));
                                respuesta.setTexto(arrayRespuestas.getJSONObject(a).getString("texto"));
                                respuesta.setValor(arrayRespuestas.getJSONObject(a).getInt("valor"));
                                respuestaList.add(respuesta);
                                pregunta.setRespuestas(respuestaList);
                            }
                            preguntaList.add(pregunta);
                            encuesta.setPreguntas(preguntaList);
                        }
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return encuesta;
    }

    private static String convertirFileToJson(Context context) {
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

    public static boolean esValidoInput(EditText et) {
        if (et.getText().toString().trim().isEmpty()) {
            et.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean esValidoSpinner(Spinner spinner) {
        if (spinner.getSelectedItemPosition() == 0) {
            spinner.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean minSiete(EditText editText){
        return editText.getText().toString().length() == 7;
    }

    public static String dateTimeNowToString() {
        Date today = new Date();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(today);
    }
}
