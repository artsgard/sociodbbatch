package com.artsgard.sociodbbatch.socio.model;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity(name = "SocioAssociatedSocio")
@Table(name = "socio_associated_socio")
@IdClass(SocioAssociatedSocioId.class)
public class SocioAssociatedSocio implements Serializable {

    private SocioAssociatedSocio() { }

    public SocioAssociatedSocio(Long socioId, Long associatedSocioId, SocioModel socio, SocioModel associatedSocio, AssociatedSocioState associatedSocioState, Timestamp associatedSocioDate) {
        this.socioId = socioId;
        this.associatedSocioId = associatedSocioId;
        this.socio = socio;
        this.associatedSocio = associatedSocio;
        this.associatedSocioState = associatedSocioState;
        this.associatedSocioDate = associatedSocioDate;
    }
    
    @Id
    @Column(name = "socio_id", nullable = true)
    private Long socioId;
    
    @Id
    @Column(name = "associated_socio_id", nullable = true)
    private Long associatedSocioId;
    
    @ManyToOne(fetch = FetchType.LAZY,
        cascade =
        {
               // CascadeType.DETACH,
              //  CascadeType.MERGE,
              //  CascadeType.REFRESH,
               // CascadeType.PERSIST,
                CascadeType.REMOVE
        })
    @JoinColumn(name = "socio_id", updatable = false, insertable = false,
            referencedColumnName = "id")
    private SocioModel socio;
    
    @ManyToOne//(fetch = FetchType.LAZY, cascade =CascadeType.REMOVE)
    @JoinColumn(name = "associated_socio_id", updatable = false, insertable = false,
            referencedColumnName = "id")
    private SocioModel associatedSocio;
    
    public enum AssociatedSocioState {
        PENDING, EXPIRED, ACCEPTED, DENIED
    }

    @Column(name = "associated_socio_state", length = 100)
    @Enumerated(EnumType.STRING)
    private AssociatedSocioState associatedSocioState;
    
    @Column(name = "associated_socio_date", nullable = true)
    private Timestamp associatedSocioDate;
}