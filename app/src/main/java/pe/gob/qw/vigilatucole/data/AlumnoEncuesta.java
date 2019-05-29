package pe.gob.qw.vigilatucole.data;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "AlumnoEncuesta")
public class AlumnoEncuesta {

    @SerializedName("alumno_id")
    @Property(nameInDb = "alumno_id")
    private Long alumno_id;
    @SerializedName("encuesta_id")
    @Property(nameInDb = "encuesta_id")
    private int encuesta_id;
    @SerializedName("fecha_encuesta")
    @Property(nameInDb = "fecha_encuesta")
    private String fecha_encuesta;
    @Generated(hash = 360717551)
    public AlumnoEncuesta(Long alumno_id, int encuesta_id, String fecha_encuesta) {
        this.alumno_id = alumno_id;
        this.encuesta_id = encuesta_id;
        this.fecha_encuesta = fecha_encuesta;
    }
    @Generated(hash = 1863037062)
    public AlumnoEncuesta() {
    }
    public Long getAlumno_id() {
        return this.alumno_id;
    }
    public void setAlumno_id(Long alumno_id) {
        this.alumno_id = alumno_id;
    }
    public int getEncuesta_id() {
        return this.encuesta_id;
    }
    public void setEncuesta_id(int encuesta_id) {
        this.encuesta_id = encuesta_id;
    }
    public String getFecha_encuesta() {
        return this.fecha_encuesta;
    }
    public void setFecha_encuesta(String fecha_encuesta) {
        this.fecha_encuesta = fecha_encuesta;
    }



}
