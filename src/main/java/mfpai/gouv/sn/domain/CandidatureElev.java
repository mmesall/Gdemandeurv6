package mfpai.gouv.sn.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import mfpai.gouv.sn.domain.enumeration.NomFiliere;
import mfpai.gouv.sn.domain.enumeration.Resultat;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CandidatureElev.
 */
@Entity
@Table(name = "candidature_elev")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CandidatureElev implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "offre_formation")
    private NomFiliere offreFormation;

    @Column(name = "date_debut_offre")
    private LocalDate dateDebutOffre;

    @Column(name = "date_fin_offre")
    private LocalDate dateFinOffre;

    @Column(name = "date_depot")
    private LocalDate dateDepot;

    @Enumerated(EnumType.STRING)
    @Column(name = "resultat")
    private Resultat resultat;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "diplomes", "experiences", "candidatureElevs", "dossier", "demandeur" }, allowSetters = true)
    private Eleve eleve;

    @ManyToOne
    @JsonIgnoreProperties(value = { "formation", "candidatureElevs", "candidatureEtudiants" }, allowSetters = true)
    private FormationInitiale formationInitiale;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CandidatureElev id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NomFiliere getOffreFormation() {
        return this.offreFormation;
    }

    public CandidatureElev offreFormation(NomFiliere offreFormation) {
        this.setOffreFormation(offreFormation);
        return this;
    }

    public void setOffreFormation(NomFiliere offreFormation) {
        this.offreFormation = offreFormation;
    }

    public LocalDate getDateDebutOffre() {
        return this.dateDebutOffre;
    }

    public CandidatureElev dateDebutOffre(LocalDate dateDebutOffre) {
        this.setDateDebutOffre(dateDebutOffre);
        return this;
    }

    public void setDateDebutOffre(LocalDate dateDebutOffre) {
        this.dateDebutOffre = dateDebutOffre;
    }

    public LocalDate getDateFinOffre() {
        return this.dateFinOffre;
    }

    public CandidatureElev dateFinOffre(LocalDate dateFinOffre) {
        this.setDateFinOffre(dateFinOffre);
        return this;
    }

    public void setDateFinOffre(LocalDate dateFinOffre) {
        this.dateFinOffre = dateFinOffre;
    }

    public LocalDate getDateDepot() {
        return this.dateDepot;
    }

    public CandidatureElev dateDepot(LocalDate dateDepot) {
        this.setDateDepot(dateDepot);
        return this;
    }

    public void setDateDepot(LocalDate dateDepot) {
        this.dateDepot = dateDepot;
    }

    public Resultat getResultat() {
        return this.resultat;
    }

    public CandidatureElev resultat(Resultat resultat) {
        this.setResultat(resultat);
        return this;
    }

    public void setResultat(Resultat resultat) {
        this.resultat = resultat;
    }

    public Eleve getEleve() {
        return this.eleve;
    }

    public void setEleve(Eleve eleve) {
        this.eleve = eleve;
    }

    public CandidatureElev eleve(Eleve eleve) {
        this.setEleve(eleve);
        return this;
    }

    public FormationInitiale getFormationInitiale() {
        return this.formationInitiale;
    }

    public void setFormationInitiale(FormationInitiale formationInitiale) {
        this.formationInitiale = formationInitiale;
    }

    public CandidatureElev formationInitiale(FormationInitiale formationInitiale) {
        this.setFormationInitiale(formationInitiale);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CandidatureElev)) {
            return false;
        }
        return id != null && id.equals(((CandidatureElev) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CandidatureElev{" +
            "id=" + getId() +
            ", offreFormation='" + getOffreFormation() + "'" +
            ", dateDebutOffre='" + getDateDebutOffre() + "'" +
            ", dateFinOffre='" + getDateFinOffre() + "'" +
            ", dateDepot='" + getDateDepot() + "'" +
            ", resultat='" + getResultat() + "'" +
            "}";
    }
}
