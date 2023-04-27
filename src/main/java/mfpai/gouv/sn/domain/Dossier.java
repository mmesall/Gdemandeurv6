package mfpai.gouv.sn.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import mfpai.gouv.sn.domain.enumeration.DiplomeRequis;
import mfpai.gouv.sn.domain.enumeration.NiveauEtude;
import mfpai.gouv.sn.domain.enumeration.NomDepartement;
import mfpai.gouv.sn.domain.enumeration.NomFiliere;
import mfpai.gouv.sn.domain.enumeration.NomRegion;
import mfpai.gouv.sn.domain.enumeration.NomSerie;
import mfpai.gouv.sn.domain.enumeration.Sexe;
import mfpai.gouv.sn.domain.enumeration.TypePiece;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Dossier.
 */
@Entity
@Table(name = "dossier")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Dossier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "num_dossier", unique = true)
    private String numDossier;

    @Column(name = "date_naiss")
    private LocalDate dateNaiss;

    @NotNull
    @Column(name = "prenom", nullable = false)
    private String prenom;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "nom_utilisateur")
    private String nomUtilisateur;

    @Enumerated(EnumType.STRING)
    @Column(name = "region_naiss")
    private NomRegion regionNaiss;

    @Enumerated(EnumType.STRING)
    @Column(name = "departement_naiss")
    private NomDepartement departementNaiss;

    @Column(name = "lieu_naiss")
    private String lieuNaiss;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_piece")
    private TypePiece typePiece;

    @Column(name = "numero_piece", unique = true)
    private Long numeroPiece;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexe")
    private Sexe sexe;

    @Enumerated(EnumType.STRING)
    @Column(name = "region_residence")
    private NomRegion regionResidence;

    @Enumerated(EnumType.STRING)
    @Column(name = "dep_residence")
    private NomDepartement depResidence;

    @Column(name = "adresse_residence")
    private String adresseResidence;

    @Column(name = "telephone_1")
    private String telephone1;

    @Column(name = "telephone_2")
    private String telephone2;

    @Column(name = "email")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "niveau_formation")
    private NiveauEtude niveauFormation;

    @Enumerated(EnumType.STRING)
    @Column(name = "specialite_1")
    private NomFiliere specialite1;

    @Enumerated(EnumType.STRING)
    @Column(name = "specialite_2")
    private NomSerie specialite2;

    @Enumerated(EnumType.STRING)
    @Column(name = "diplome_requis")
    private DiplomeRequis diplomeRequis;

    @Lob
    @Column(name = "cv")
    private byte[] cv;

    @Column(name = "cv_content_type")
    private String cvContentType;

    @Lob
    @Column(name = "lettre_motivation")
    private String lettreMotivation;

    @Column(name = "profession")
    private String profession;

    @JsonIgnoreProperties(value = { "user", "diplomes", "experiences", "candidatureElevs", "dossier", "demandeur" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Eleve eleve;

    @JsonIgnoreProperties(
        value = { "user", "diplomes", "experiences", "candidatureEtudiants", "dossier", "demandeur" },
        allowSetters = true
    )
    @OneToOne
    @JoinColumn(unique = true)
    private Etudiant etudiant;

    @JsonIgnoreProperties(value = { "user", "diplomes", "experiences", "candidatureProfs", "dossier", "demandeur" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Professionnel professionnel;

    @JsonIgnoreProperties(
        value = { "user", "dossier", "eleve", "etudiant", "professionnel", "diplomes", "experiences" },
        allowSetters = true
    )
    @OneToOne(mappedBy = "dossier")
    private Demandeur demandeur;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Dossier id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumDossier() {
        return this.numDossier;
    }

    public Dossier numDossier(String numDossier) {
        this.setNumDossier(numDossier);
        return this;
    }

    public void setNumDossier(String numDossier) {
        this.numDossier = numDossier;
    }

    public LocalDate getDateNaiss() {
        return this.dateNaiss;
    }

    public Dossier dateNaiss(LocalDate dateNaiss) {
        this.setDateNaiss(dateNaiss);
        return this;
    }

    public void setDateNaiss(LocalDate dateNaiss) {
        this.dateNaiss = dateNaiss;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Dossier prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return this.nom;
    }

    public Dossier nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNomUtilisateur() {
        return this.nomUtilisateur;
    }

    public Dossier nomUtilisateur(String nomUtilisateur) {
        this.setNomUtilisateur(nomUtilisateur);
        return this;
    }

    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }

    public NomRegion getRegionNaiss() {
        return this.regionNaiss;
    }

    public Dossier regionNaiss(NomRegion regionNaiss) {
        this.setRegionNaiss(regionNaiss);
        return this;
    }

    public void setRegionNaiss(NomRegion regionNaiss) {
        this.regionNaiss = regionNaiss;
    }

    public NomDepartement getDepartementNaiss() {
        return this.departementNaiss;
    }

    public Dossier departementNaiss(NomDepartement departementNaiss) {
        this.setDepartementNaiss(departementNaiss);
        return this;
    }

    public void setDepartementNaiss(NomDepartement departementNaiss) {
        this.departementNaiss = departementNaiss;
    }

    public String getLieuNaiss() {
        return this.lieuNaiss;
    }

    public Dossier lieuNaiss(String lieuNaiss) {
        this.setLieuNaiss(lieuNaiss);
        return this;
    }

    public void setLieuNaiss(String lieuNaiss) {
        this.lieuNaiss = lieuNaiss;
    }

    public TypePiece getTypePiece() {
        return this.typePiece;
    }

    public Dossier typePiece(TypePiece typePiece) {
        this.setTypePiece(typePiece);
        return this;
    }

    public void setTypePiece(TypePiece typePiece) {
        this.typePiece = typePiece;
    }

    public Long getNumeroPiece() {
        return this.numeroPiece;
    }

    public Dossier numeroPiece(Long numeroPiece) {
        this.setNumeroPiece(numeroPiece);
        return this;
    }

    public void setNumeroPiece(Long numeroPiece) {
        this.numeroPiece = numeroPiece;
    }

    public Sexe getSexe() {
        return this.sexe;
    }

    public Dossier sexe(Sexe sexe) {
        this.setSexe(sexe);
        return this;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }

    public NomRegion getRegionResidence() {
        return this.regionResidence;
    }

    public Dossier regionResidence(NomRegion regionResidence) {
        this.setRegionResidence(regionResidence);
        return this;
    }

    public void setRegionResidence(NomRegion regionResidence) {
        this.regionResidence = regionResidence;
    }

    public NomDepartement getDepResidence() {
        return this.depResidence;
    }

    public Dossier depResidence(NomDepartement depResidence) {
        this.setDepResidence(depResidence);
        return this;
    }

    public void setDepResidence(NomDepartement depResidence) {
        this.depResidence = depResidence;
    }

    public String getAdresseResidence() {
        return this.adresseResidence;
    }

    public Dossier adresseResidence(String adresseResidence) {
        this.setAdresseResidence(adresseResidence);
        return this;
    }

    public void setAdresseResidence(String adresseResidence) {
        this.adresseResidence = adresseResidence;
    }

    public String getTelephone1() {
        return this.telephone1;
    }

    public Dossier telephone1(String telephone1) {
        this.setTelephone1(telephone1);
        return this;
    }

    public void setTelephone1(String telephone1) {
        this.telephone1 = telephone1;
    }

    public String getTelephone2() {
        return this.telephone2;
    }

    public Dossier telephone2(String telephone2) {
        this.setTelephone2(telephone2);
        return this;
    }

    public void setTelephone2(String telephone2) {
        this.telephone2 = telephone2;
    }

    public String getEmail() {
        return this.email;
    }

    public Dossier email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public NiveauEtude getNiveauFormation() {
        return this.niveauFormation;
    }

    public Dossier niveauFormation(NiveauEtude niveauFormation) {
        this.setNiveauFormation(niveauFormation);
        return this;
    }

    public void setNiveauFormation(NiveauEtude niveauFormation) {
        this.niveauFormation = niveauFormation;
    }

    public NomFiliere getSpecialite1() {
        return this.specialite1;
    }

    public Dossier specialite1(NomFiliere specialite1) {
        this.setSpecialite1(specialite1);
        return this;
    }

    public void setSpecialite1(NomFiliere specialite1) {
        this.specialite1 = specialite1;
    }

    public NomSerie getSpecialite2() {
        return this.specialite2;
    }

    public Dossier specialite2(NomSerie specialite2) {
        this.setSpecialite2(specialite2);
        return this;
    }

    public void setSpecialite2(NomSerie specialite2) {
        this.specialite2 = specialite2;
    }

    public DiplomeRequis getDiplomeRequis() {
        return this.diplomeRequis;
    }

    public Dossier diplomeRequis(DiplomeRequis diplomeRequis) {
        this.setDiplomeRequis(diplomeRequis);
        return this;
    }

    public void setDiplomeRequis(DiplomeRequis diplomeRequis) {
        this.diplomeRequis = diplomeRequis;
    }

    public byte[] getCv() {
        return this.cv;
    }

    public Dossier cv(byte[] cv) {
        this.setCv(cv);
        return this;
    }

    public void setCv(byte[] cv) {
        this.cv = cv;
    }

    public String getCvContentType() {
        return this.cvContentType;
    }

    public Dossier cvContentType(String cvContentType) {
        this.cvContentType = cvContentType;
        return this;
    }

    public void setCvContentType(String cvContentType) {
        this.cvContentType = cvContentType;
    }

    public String getLettreMotivation() {
        return this.lettreMotivation;
    }

    public Dossier lettreMotivation(String lettreMotivation) {
        this.setLettreMotivation(lettreMotivation);
        return this;
    }

    public void setLettreMotivation(String lettreMotivation) {
        this.lettreMotivation = lettreMotivation;
    }

    public String getProfession() {
        return this.profession;
    }

    public Dossier profession(String profession) {
        this.setProfession(profession);
        return this;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public Eleve getEleve() {
        return this.eleve;
    }

    public void setEleve(Eleve eleve) {
        this.eleve = eleve;
    }

    public Dossier eleve(Eleve eleve) {
        this.setEleve(eleve);
        return this;
    }

    public Etudiant getEtudiant() {
        return this.etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public Dossier etudiant(Etudiant etudiant) {
        this.setEtudiant(etudiant);
        return this;
    }

    public Professionnel getProfessionnel() {
        return this.professionnel;
    }

    public void setProfessionnel(Professionnel professionnel) {
        this.professionnel = professionnel;
    }

    public Dossier professionnel(Professionnel professionnel) {
        this.setProfessionnel(professionnel);
        return this;
    }

    public Demandeur getDemandeur() {
        return this.demandeur;
    }

    public void setDemandeur(Demandeur demandeur) {
        if (this.demandeur != null) {
            this.demandeur.setDossier(null);
        }
        if (demandeur != null) {
            demandeur.setDossier(this);
        }
        this.demandeur = demandeur;
    }

    public Dossier demandeur(Demandeur demandeur) {
        this.setDemandeur(demandeur);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dossier)) {
            return false;
        }
        return id != null && id.equals(((Dossier) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Dossier{" +
            "id=" + getId() +
            ", numDossier='" + getNumDossier() + "'" +
            ", dateNaiss='" + getDateNaiss() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", nom='" + getNom() + "'" +
            ", nomUtilisateur='" + getNomUtilisateur() + "'" +
            ", regionNaiss='" + getRegionNaiss() + "'" +
            ", departementNaiss='" + getDepartementNaiss() + "'" +
            ", lieuNaiss='" + getLieuNaiss() + "'" +
            ", typePiece='" + getTypePiece() + "'" +
            ", numeroPiece=" + getNumeroPiece() +
            ", sexe='" + getSexe() + "'" +
            ", regionResidence='" + getRegionResidence() + "'" +
            ", depResidence='" + getDepResidence() + "'" +
            ", adresseResidence='" + getAdresseResidence() + "'" +
            ", telephone1='" + getTelephone1() + "'" +
            ", telephone2='" + getTelephone2() + "'" +
            ", email='" + getEmail() + "'" +
            ", niveauFormation='" + getNiveauFormation() + "'" +
            ", specialite1='" + getSpecialite1() + "'" +
            ", specialite2='" + getSpecialite2() + "'" +
            ", diplomeRequis='" + getDiplomeRequis() + "'" +
            ", cv='" + getCv() + "'" +
            ", cvContentType='" + getCvContentType() + "'" +
            ", lettreMotivation='" + getLettreMotivation() + "'" +
            ", profession='" + getProfession() + "'" +
            "}";
    }
}
