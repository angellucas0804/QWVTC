package pe.gob.qw.vigilatucole.api;

import org.json.JSONObject;

import java.util.List;

import pe.gob.qw.vigilatucole.data.Alumno;
import pe.gob.qw.vigilatucole.model.Encuesta;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface QWService {

    @POST("AlumnoRespuestas")
    Call<List<Alumno>> postEnviarEncuesta(@Body JSONObject encuesta);
}
