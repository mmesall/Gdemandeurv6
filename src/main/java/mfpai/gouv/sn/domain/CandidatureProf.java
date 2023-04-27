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
 * A CandidatureProf.
 */
@Entity
@Table(name = "candidature_prof")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CandidatureProf implements Serializable {

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
    @JsonIgnoreProperties(value = { "user", "diplomes", "experiences", "candidatureProfs", "dossier", "demandeur" }, allowSetters = true)
    private Professionnel professionnel;

    @ManyToOne
    @JsonIgnoreProperties(value = { "formation", "candidatureProfs" }, allowSetters = true)
    private FormationContinue formationContinue;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CandidatureProf id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NomFiliere getOffreFormation() {
        return this.offreFormation;
    }

    public CandidatureProf offreFormation(NomFiliere offreFormation) {
        this.setOffreFormation(offreFormation);
        return this;
    }

    public void setOffreFormation(NomFiliere offreFormation) {
        this.offreFormation = offreFormation;
    }

    public LocalDate getDateDebutOffre() {
        return this.dateDebutOffre;
    }

    public CandidatureProf dateDebutOffre(LocalDate dateDebutOffre) {
        this.setDateDebutOffre(dateDebutOffre);
        return this;
    }

    public void setDateDebutOffre(LocalDate dateDebutOffre) {
        this.dateDebutOffre = dateDebutOffre;
    }

    public LocalDate getDateFinOffre() {
        return this.dateFinOffre;
    }

    public CandidatureProf dateFinOffre(LocalDate dateFinOffre) {
        this.setDateFinOffre(dateFinOffre);
        return this;
    }

    public void setDateFinOffre(LocalDate dateFinOffre) {
        this.dateFinOffre = dateFinOffre;
    }

    public LocalDate getDateDepot() {
        return this.dateDepot;
    }

    public CandidatureProf dateDepot(LocalDate dateDepot) {
        this.setDateDepot(dateDepot);
        return this;
    }

    public void setDateDepot(LocalDate dateDepot) {
        this.dateDepot = dateDepot;
    }

    public Resultat getResultat() {
        return this.resultat;
    }

    public CandidatureProf resultat(Resultat resultat) {
        this.setResultat(resultat);
        return this;
    }

    public void setResultat(Resultat resultat) {
        this.resultat = resultat;
    }

    public Professionnel getProfessionnel() {
        return this.professionnel;
    }

    public void setProfessionnel(Professionnel professionnel) {
        this.professionnel = professionnel;
    }

    public CandidatureProf professionnel(Professionnel professionnel) {
        this.setProfessionnel(professionnel);
        return this;
    }

    public FormationContinue getFormationContinue() {
        return this.formationContinue;
    }

    public void setFormationContinue(FormationContinue formationContinue) {
        this.formationContinue = formationContinue;
    }

    public CandidatureProf formationContinue(FormationContinue formationContinue) {
        this.setFormationContinue(formationContinue);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CandidatureProf)) {
            return false;
        }
        return id != null && id.equals(((CandidatureProf) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CandidatureProf{" +
            "id=" + getId() +
            ", offreFormation='" + getOffreFormation() + "'" +
            ", dateDebutOffre='" + getDateDebutOffre() + "'" +
            ", dateFinOffre='" + getDateFinOffre() + "'" +
            ", dateDepot='" + getDateDepot() + "'" +
            ", resultat='" + getResultat() + "'" +
            "}";
    }
}
