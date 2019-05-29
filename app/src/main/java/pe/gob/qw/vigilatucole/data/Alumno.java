package pe.gob.qw.vigilatucole.data;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import java.io.Serializable;
import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "Alumno")
public class Alumno  {

    @Id(autoincrement = true)
    private Long id;
    @SerializedName("chCodModular")
    @Property(nameInDb = "chCodModular")
    private String chCodModular;
    @SerializedName("nvColegio")
    @Property(nameInDb = "nvColegio")
    private String nvColegio;
    @SerializedName("nvNombres")
    @Property(nameInDb = "nvNombres")
    private String nvNombres;
    @SerializedName("nvApePatMat")
    @Property(nameInDb = "nvApePatMat")
    private String nvApePatMat;
    @SerializedName("inSexo")
    @Property(nameInDb = "inSexo")
    private int inSexo;
    @SerializedName("biMunicipio")
    @Property(nameInDb = "biMunicipio")
    private boolean biMunicipio;
    @SerializedName("inGrado")
    @Property(nameInDb = "inGrado")
    private int inGrado;
    @SerializedName("inNivel")
    @Property(nameInDb = "inNivel")
    private int inNivel;
    @SerializedName("inTurno")
    @Property(nameInDb = "inTurno")
    private int inTurno;
    @SerializedName("lngPuntaje")
    @Property(nameInDb = "lngPuntaje")
    private Long lngPuntaje;
    @Generated(hash = 1492985648)
    public Alumno(Long id, String chCodModular, String nvColegio, String nvNombres,
            String nvApePatMat, int inSexo, boolean biMunicipio, int inGrado,
            int inNivel, int inTurno, Long lngPuntaje) {
        this.id = id;
        this.chCodModular = chCodModular;
        this.nvColegio = nvColegio;
        this.nvNombres = nvNombres;
        this.nvApePatMat = nvApePatMat;
        this.inSexo = inSexo;
        this.biMunicipio = biMunicipio;
        this.inGrado = inGrado;
        this.inNivel = inNivel;
        this.inTurno = inTurno;
        this.lngPuntaje = lngPuntaje;
    }
    @Generated(hash = 721709040)
    public Alumno() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getChCodModular() {
        return this.chCodModular;
    }
    public void setChCodModular(String chCodModular) {
        this.chCodModular = chCodModular;
    }
    public String getNvColegio() {
        return this.nvColegio;
    }
    public void setNvColegio(String nvColegio) {
        this.nvColegio = nvColegio;
    }
    public String getNvNombres() {
        return this.nvNombres;
    }
    public void setNvNombres(String nvNombres) {
        this.nvNombres = nvNombres;
    }
    public String getNvApePatMat() {
        return this.nvApePatMat;
    }
    public void setNvApePatMat(String nvApePatMat) {
        this.nvApePatMat = nvApePatMat;
    }
    public int getInSexo() {
        return this.inSexo;
    }
    public void setInSexo(int inSexo) {
        this.inSexo = inSexo;
    }
    public boolean getBiMunicipio() {
        return this.biMunicipio;
    }
    public void setBiMunicipio(boolean biMunicipio) {
        this.biMunicipio = biMunicipio;
    }
    public int getInGrado() {
        return this.inGrado;
    }
    public void setInGrado(int inGrado) {
        this.inGrado = inGrado;
    }
    public int getInNivel() {
        return this.inNivel;
    }
    public void setInNivel(int inNivel) {
        this.inNivel = inNivel;
    }
    public int getInTurno() {
        return this.inTurno;
    }
    public void setInTurno(int inTurno) {
        this.inTurno = inTurno;
    }
    public Long getLngPuntaje() {
        return this.lngPuntaje;
    }
    public void setLngPuntaje(Long lngPuntaje) {
        this.lngPuntaje = lngPuntaje;
    }


}
