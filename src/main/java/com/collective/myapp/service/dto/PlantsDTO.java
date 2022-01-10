package com.collective.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.collective.myapp.domain.Plants} entity.
 */
public class PlantsDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer year;

    @NotNull
    private String pstatabb;

    @NotNull
    private String pname;

    @NotNull
    private String genid;

    @NotNull
    private String genstat;

    private Integer genntan;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getPstatabb() {
        return pstatabb;
    }

    public void setPstatabb(String pstatabb) {
        this.pstatabb = pstatabb;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getGenid() {
        return genid;
    }

    public void setGenid(String genid) {
        this.genid = genid;
    }

    public String getGenstat() {
        return genstat;
    }

    public void setGenstat(String genstat) {
        this.genstat = genstat;
    }

    public Integer getGenntan() {
        return genntan;
    }

    public void setGenntan(Integer genntan) {
        this.genntan = genntan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlantsDTO)) {
            return false;
        }

        PlantsDTO plantsDTO = (PlantsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, plantsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlantsDTO{" +
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
