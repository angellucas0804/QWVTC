package pe.gob.qw.vigilatucole.data;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "AlumnoRespuesta")
public class AlumnoRespuesta {

    @Id(autoincrement = true)
    private Long id;
    @SerializedName("alumno_id")
    @Property(nameInDb = "alumno_id")
    private Long alumno_id;
    @SerializedName("encuesta_id")
    @Property(nameInDb = "encuesta_id")
    private Long encuesta_id;
    @SerializedName("pregunta_id")
    @Property(nameInDb = "pregunta_id")
    private int pregunta_id;
    @SerializedName("respuesta")
    @Property(nameInDb = "respuesta")
    private int respuesta;
    @SerializedName("detalle")
    @Property(nameInDb = "detalle")
    private String detalle;
    @Generated(hash = 1864167690)
    public AlumnoRespuesta(Long id, Long alumno_id, Long encuesta_id,
            int pregunta_id, int respuesta, String detalle) {
        this.id = id;
        this.alumno_id = alumno_id;
        this.encuesta_id = encuesta_id;
        this.pregunta_id = pregunta_id;
        this.respuesta = respuesta;
        this.detalle = detalle;
    }
    @Generated(hash = 371485993)
    public AlumnoRespuesta() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getAlumno_id() {
        return this.alumno_id;
    }
    public void setAlumno_id(Long alumno_id) {
        this.alumno_id = alumno_id;
    }
    public Long getEncuesta_id() {
        return this.encuesta_id;
    }
    public void setEncuesta_id(Long encuesta_id) {
        this.encuesta_id = encuesta_id;
    }
    public int getPregunta_id() {
        return this.pregunta_id;
    }
    public void setPregunta_id(int pregunta_id) {
        this.pregunta_id = pregunta_id;
    }
    public int getRespuesta() {
        return this.respuesta;
    }
    public void setRespuesta(int respuesta) {
        this.respuesta = respuesta;
    }
    public String getDetalle() {
        return this.detalle;
    }
    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

}
