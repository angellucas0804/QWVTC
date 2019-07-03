package pe.gob.qw.vigilatucole.data;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "AlumnoEncuesta")
public class AlumnoEncuesta {

    @Id(autoincrement = true)
    private Long id;
    @SerializedName("alumno_id")
    @Property(nameInDb = "alumno_id")
    private Long alumno_id;
    @SerializedName("encuesta_id")
    @Property(nameInDb = "encuesta_id")
    private int encuesta_id;
    @SerializedName("fecha_encuesta")
    @Property(nameInDb = "fecha_encuesta")
    private String fecha_encuesta;
    @Generated(hash = 1321475552)
    public AlumnoEncuesta(Long id, Long alumno_id, int encuesta_id,
            String fecha_encuesta) {
        this.id = id;
        this.alumno_id = alumno_id;
        this.encuesta_id = encuesta_id;
        this.fecha_encuesta = fecha_encuesta;
    }
    @Generated(hash = 1863037062)
    public AlumnoEncuesta() {
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
