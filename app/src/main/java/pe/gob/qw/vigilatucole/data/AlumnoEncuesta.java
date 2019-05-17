package pe.gob.qw.vigilatucole.data;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "AlumnoEncuesta")
public class AlumnoEncuesta {

    @Id(autoincrement = true)
    private Long id;
    @SerializedName("alumnoId")
    @Property(nameInDb = "alumnoId")
    private int alumnoId;
    @SerializedName("inIdAlumnoEncuesta")
    @Property(nameInDb = "inIdAlumnoEncuesta")
    private int inIdAlumnoEncuesta;
    @SerializedName("inIdPregunta")
    @Property(nameInDb = "inIdPregunta")
    private int inIdPregunta;
    @SerializedName("inRespuestaOpcion")
    @Property(nameInDb = "inRespuestaOpcion")
    private int inRespuestaOpcion;
    @SerializedName("nvDetalleRespuesta")
    @Property(nameInDb = "nvDetalleRespuesta")
    private String nvDetalleRespuesta;
    @Generated(hash = 773329838)
    public AlumnoEncuesta(Long id, int alumnoId, int inIdAlumnoEncuesta,
            int inIdPregunta, int inRespuestaOpcion, String nvDetalleRespuesta) {
        this.id = id;
        this.alumnoId = alumnoId;
        this.inIdAlumnoEncuesta = inIdAlumnoEncuesta;
        this.inIdPregunta = inIdPregunta;
        this.inRespuestaOpcion = inRespuestaOpcion;
        this.nvDetalleRespuesta = nvDetalleRespuesta;
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
    public int getAlumnoId() {
        return this.alumnoId;
    }
    public void setAlumnoId(int alumnoId) {
        this.alumnoId = alumnoId;
    }
    public int getInIdAlumnoEncuesta() {
        return this.inIdAlumnoEncuesta;
    }
    public void setInIdAlumnoEncuesta(int inIdAlumnoEncuesta) {
        this.inIdAlumnoEncuesta = inIdAlumnoEncuesta;
    }
    public int getInIdPregunta() {
        return this.inIdPregunta;
    }
    public void setInIdPregunta(int inIdPregunta) {
        this.inIdPregunta = inIdPregunta;
    }
    public int getInRespuestaOpcion() {
        return this.inRespuestaOpcion;
    }
    public void setInRespuestaOpcion(int inRespuestaOpcion) {
        this.inRespuestaOpcion = inRespuestaOpcion;
    }
    public String getNvDetalleRespuesta() {
        return this.nvDetalleRespuesta;
    }
    public void setNvDetalleRespuesta(String nvDetalleRespuesta) {
        this.nvDetalleRespuesta = nvDetalleRespuesta;
    }
}
