package ee.tlu.talvekool.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import ee.tlu.talvekool.domain.enumeration.Sugu;

import ee.tlu.talvekool.domain.enumeration.Oskustase;

/**
 * A Osaleja.
 */
@Entity
@Table(name = "osaleja")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "osaleja")
public class Osaleja implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 3)
    @Column(name = "nimi", nullable = false)
    private String nimi;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "sugu", nullable = false)
    private Sugu sugu;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "oskustase", nullable = false)
    private Oskustase oskustase;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNimi() {
        return nimi;
    }

    public Osaleja nimi(String nimi) {
        this.nimi = nimi;
        return this;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public Sugu getSugu() {
        return sugu;
    }

    public Osaleja sugu(Sugu sugu) {
        this.sugu = sugu;
        return this;
    }

    public void setSugu(Sugu sugu) {
        this.sugu = sugu;
    }

    public Oskustase getOskustase() {
        return oskustase;
    }

    public Osaleja oskustase(Oskustase oskustase) {
        this.oskustase = oskustase;
        return this;
    }

    public void setOskustase(Oskustase oskustase) {
        this.oskustase = oskustase;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Osaleja osaleja = (Osaleja) o;
        if (osaleja.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, osaleja.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Osaleja{" +
            "id=" + id +
            ", nimi='" + nimi + "'" +
            ", sugu='" + sugu + "'" +
            ", oskustase='" + oskustase + "'" +
            '}';
    }
}
