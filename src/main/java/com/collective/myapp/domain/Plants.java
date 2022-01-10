package com.collective.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Plants.
 */
@Entity
@Table(name = "plants")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Plants implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "year", nullable = false, unique =false)
    private Integer year;

    @NotNull
    @Column(name = "pstatabb", nullable = false)
    private String pstatabb;

    @NotNull
    @Column(name = "pname", nullable = false)
    private String pname;

    @NotNull
    @Column(name = "genid", nullable = false)
    private String genid;

    @NotNull
    @Column(name = "genstat", nullable = false)
    private String genstat;

    @Column(name = "genntan")
    private Integer genntan;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Plants id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getYear() {
        return this.year;
    }

    public Plants year(Integer year) {
        this.year = year;
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getPstatabb() {
        return this.pstatabb;
    }

    public Plants pstatabb(String pstatabb) {
        this.pstatabb = pstatabb;
        return this;
    }

    public void setPstatabb(String pstatabb) {
        this.pstatabb = pstatabb;
    }

    public String getPname() {
        return this.pname;
    }

    public Plants pname(String pname) {
        this.pname = pname;
        return this;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getGenid() {
        return this.genid;
    }

    public Plants genid(String genid) {
        this.genid = genid;
        return this;
    }

    public void setGenid(String genid) {
        this.genid = genid;
    }

    public String getGenstat() {
        return this.genstat;
    }

    public Plants genstat(String genstat) {
        this.genstat = genstat;
        return this;
    }

    public void setGenstat(String genstat) {
        this.genstat = genstat;
    }

    public Integer getGenntan() {
        return this.genntan;
    }

    public Plants genntan(Integer genntan) {
        this.genntan = genntan;
        return this;
    }

    public void setGenntan(Integer genntan) {
        this.genntan = genntan;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Plants)) {
            return false;
        }
        return id != null && id.equals(((Plants) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Plants{" +
            "id=" + getId() +
            ", year=" + getYear() +
            ", pstatabb='" + getPstatabb() + "'" +
            ", pname='" + getPname() + "'" +
            ", genid='" + getGenid() + "'" +
            ", genstat='" + getGenstat() + "'" +
            ", genntan=" + getGenntan() +
            "}";
    }
}
