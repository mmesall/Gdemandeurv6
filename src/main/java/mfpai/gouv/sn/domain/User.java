package mfpai.gouv.sn.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.sql.Date;
import java.time.Instant;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import mfpai.gouv.sn.config.Constants;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A user.
 */
@Entity
@Table(name = "jhi_user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class User extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = true, nullable = false)
    private String login;

    @JsonIgnore
    @NotNull
    @Size(min = 60, max = 60)
    @Column(name = "password_hash", length = 60, nullable = false)
    private String password;

    @Size(max = 50)
    @Column(name = "first_name", length = 50)
    private String firstName;

    @Size(max = 50)
    @Column(name = "last_name", length = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 254)
    @Column(length = 254, unique = true)
    private String email;

    @Past(message = "La date de naissance doit etre au passé")
    @Column(name = "dateNaiss", length = 50)
    private Instant dateNaiss;

    @Size(min = 5, max = 254)
    @Column(name = "lieuNaiss", length = 50)
    private String lieuNaiss;

    @Size(min = 5, max = 50)
    @Column(name = "regionNaiss", length = 50)
    private String regionNaiss;

    // @Size(max = 50)
    // @Column(name = "typePiece", length = 50)
    // private String typePiece;

    // @Size(max = 50)
    // @Column(name = "numeroPiece", length = 50)
    // private Number numeroPiece;

    // @NotNull
    // @Column(name = "sexe", nullable = false)
    // private boolean sexe;

    // @Size(max = 50)
    // @Column(name = "regionResidence", length = 50)
    // private String regionResidence;

    // @Size(min = 5, max = 254)
    // @Column(name = "adresseResidence", length = 50)
    // private String adresseResidence;

    // @Size(min = 9, max = 14)
    // @Column(name = "telephone1", length = 9)
    // private Number telephone1;

    // @Size(min = 9, max = 14)
    // @Column(name = "telephone2", length = 9)
    // private Number telephone2;

    // @Size(min = 5, max = 254)
    // @Column(name = "nomFormation", length = 50)
    // private String nomFormation;

    // @Size(min = 60, max = 60)
    // @Column(name = "dateDebut", length = 60)
    // private Date dateDebut;

    // @Size(min = 60, max = 60)
    // @Column(name = "dateFin", length = 60)
    // private Date dateFin;

    // @Size(min = 5, max = 254)
    // @Column(name = "etabFreq", length = 50)
    // private String etabFreq;

    // @Size(min = 5, max = 254)
    // @Column(name = "niveauFormation", length = 50)
    // private String niveauFormation;

    // @Size(min = 5, max = 254)
    // @Column(name = "specialite1", length = 50)
    // private String specialite1;

    // @Size(min = 5, max = 254)
    // @Column(name = "status", length = 50)
    // private String status;

    // @Size(min = 5, max = 254)
    // @Column(name = "perspective", length = 50)
    // private String perspective;

    // @Size(min = 5, max = 254)
    // @Column(name = "structure", length = 50)
    // private String structure;

    // @Size(min = 5, max = 254)
    // @Column(name = "profession", length = 50)
    // private String profession;

    // @Size(min = 5, max = 254)
    // @Column(name = "secteur", length = 50)
    // private String secteur;

    // @Size(min = 5, max = 254)
    // @Column(name = "langue1", length = 50)
    // private String langue1;

    // @Size(min = 5, max = 254)
    // @Column(name = "langue2", length = 50)
    // private String langue2;

    // public String getTypePiece() {
    //     return this.typePiece;
    // }

    // public void setTypePiece(String typePiece) {
    //     this.typePiece = typePiece;
    // }

    // public Number getNumeroPiece() {
    //     return this.numeroPiece;
    // }

    // public void setNumeroPiece(Number numeroPiece) {
    //     this.numeroPiece = numeroPiece;
    // }

    // public boolean isSexe() {
    //     return this.sexe;
    // }

    // public boolean getSexe() {
    //     return this.sexe;
    // }

    // public void setSexe(boolean sexe) {
    //     this.sexe = sexe;
    // }

    // public String getRegionResidence() {
    //     return this.regionResidence;
    // }

    // public void setRegionResidence(String regionResidence) {
    //     this.regionResidence = regionResidence;
    // }

    // public String getAdresseResidence() {
    //     return this.adresseResidence;
    // }

    // public void setAdresseResidence(String adresseResidence) {
    //     this.adresseResidence = adresseResidence;
    // }

    // public Number getTelephone1() {
    //     return this.telephone1;
    // }

    // public void setTelephone1(Number telephone1) {
    //     this.telephone1 = telephone1;
    // }

    // public Number getTelephone2() {
    //     return this.telephone2;
    // }

    // public void setTelephone2(Number telephone2) {
    //     this.telephone2 = telephone2;
    // }

    // public String getNomFormation() {
    //     return this.nomFormation;
    // }

    // public void setNomFormation(String nomFormation) {
    //     this.nomFormation = nomFormation;
    // }

    // public Date getDateDebut() {
    //     return this.dateDebut;
    // }

    // public void setDateDebut(Date dateDebut) {
    //     this.dateDebut = dateDebut;
    // }

    // public Date getDateFin() {
    //     return this.dateFin;
    // }

    // public void setDateFin(Date dateFin) {
    //     this.dateFin = dateFin;
    // }

    // public String getEtabFreq() {
    //     return this.etabFreq;
    // }

    // public void setEtabFreq(String etabFreq) {
    //     this.etabFreq = etabFreq;
    // }

    // public String getNiveauFormation() {
    //     return this.niveauFormation;
    // }

    // public void setNiveauFormation(String niveauFormation) {
    //     this.niveauFormation = niveauFormation;
    // }

    // public String getSpecialite1() {
    //     return this.specialite1;
    // }

    // public void setSpecialite1(String specialite1) {
    //     this.specialite1 = specialite1;
    // }

    // public String getStatus() {
    //     return this.status;
    // }

    // public void setStatus(String status) {
    //     this.status = status;
    // }

    // public String getPerspective() {
    //     return this.perspective;
    // }

    // public void setPerspective(String perspective) {
    //     this.perspective = perspective;
    // }

    // public String getStructure() {
    //     return this.structure;
    // }

    // public void setStructure(String structure) {
    //     this.structure = structure;
    // }

    // public String getProfession() {
    //     return this.profession;
    // }

    // public void setProfession(String profession) {
    //     this.profession = profession;
    // }

    // public String getSecteur() {
    //     return this.secteur;
    // }

    // public void setSecteur(String secteur) {
    //     this.secteur = secteur;
    // }

    // public String getLangue1() {
    //     return this.langue1;
    // }

    // public void setLangue1(String langue1) {
    //     this.langue1 = langue1;
    // }

    // public String getLangue2() {
    //     return this.langue2;
    // }

    // public void setLangue2(String langue2) {
    //     this.langue2 = langue2;
    // }

    public boolean getActivated() {
        return this.activated;
    }

    @NotNull
    @Column(nullable = false)
    private boolean activated = false;

    @Size(min = 2, max = 10)
    @Column(name = "lang_key", length = 10)
    private String langKey;

    @Size(max = 256)
    @Column(name = "image_url", length = 256)
    private String imageUrl;

    @Size(max = 20)
    @Column(name = "activation_key", length = 20)
    @JsonIgnore
    private String activationKey;

    @Size(max = 20)
    @Column(name = "reset_key", length = 20)
    @JsonIgnore
    private String resetKey;

    @Column(name = "reset_date")
    private Instant resetDate = null;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "jhi_user_authority",
        joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") },
        inverseJoinColumns = { @JoinColumn(name = "authority_name", referencedColumnName = "name") }
    )
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 20)
    private Set<Authority> authorities = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    // Lowercase the login before saving it in database
    public void setLogin(String login) {
        this.login = StringUtils.lowerCase(login, Locale.ENGLISH);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Instant getDateNaiss() {
        return this.dateNaiss;
    }

    public void setDateNaiss(Instant dateNaiss) {
        this.dateNaiss = dateNaiss;
    }

    public String getLieuNaiss() {
        return this.lieuNaiss;
    }

    public void setLieuNaiss(String lieuNaiss) {
        this.lieuNaiss = lieuNaiss;
    }

    public String getRegionNaiss() {
        return this.regionNaiss;
    }

    public void setRegionNaiss(String regionNaiss) {
        this.regionNaiss = regionNaiss;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public String getResetKey() {
        return resetKey;
    }

    public void setResetKey(String resetKey) {
        this.resetKey = resetKey;
    }

    public Instant getResetDate() {
        return resetDate;
    }

    public void setResetDate(Instant resetDate) {
        this.resetDate = resetDate;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        return id != null && id.equals(((User) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "User{" +
            "login='" + login + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            ", dateNaiss='" + dateNaiss + "'" +
            ", lieuNaiss='" + lieuNaiss + "'" +
            ", regionNaiss='" + regionNaiss + "'" +
            ", imageUrl='" + imageUrl + '\'' +
            ", activated='" + activated + '\'' +
            ", langKey='" + langKey + '\'' +
            ", activationKey='" + activationKey + '\'' +
            "}";
    }
    // @Override
    // public String toString() {
    //     return "{" +
    //         " id='" + getId() + "'" +
    //         ", login='" + getLogin() + "'" +
    //         ", password='" + getPassword() + "'" +
    //         ", firstName='" + getFirstName() + "'" +
    //         ", lastName='" + getLastName() + "'" +
    //         ", email='" + getEmail() + "'" +
    //         ", dateNaiss='" + getDateNaiss() + "'" +
    //         ", lieuNaiss='" + getLieuNaiss() + "'" +
    //         ", regionNaiss='" + getRegionNaiss() + "'" +
    //         ", typePiece='" + getTypePiece() + "'" +
    //         ", numeroPiece='" + getNumeroPiece() + "'" +
    //         ", sexe='" + isSexe() + "'" +
    //         ", regionResidence='" + getRegionResidence() + "'" +
    //         ", adresseResidence='" + getAdresseResidence() + "'" +
    //         ", telephone1='" + getTelephone1() + "'" +
    //         ", telephone2='" + getTelephone2() + "'" +
    //         ", nomFormation='" + getNomFormation() + "'" +
    //         ", dateDebut='" + getDateDebut() + "'" +
    //         ", dateFin='" + getDateFin() + "'" +
    //         ", etabFreq='" + getEtabFreq() + "'" +
    //         ", niveauFormation='" + getNiveauFormation() + "'" +
    //         ", specialite1='" + getSpecialite1() + "'" +
    //         ", status='" + getStatus() + "'" +
    //         ", perspective='" + getPerspective() + "'" +
    //         ", structure='" + getStructure() + "'" +
    //         ", profession='" + getProfession() + "'" +
    //         ", secteur='" + getSecteur() + "'" +
    //         ", langue1='" + getLangue1() + "'" +
    //         ", langue2='" + getLangue2() + "'" +
    //         ", activated='" + isActivated() + "'" +
    //         ", langKey='" + getLangKey() + "'" +
    //         ", imageUrl='" + getImageUrl() + "'" +
    //         ", activationKey='" + getActivationKey() + "'" +
    //         ", resetKey='" + getResetKey() + "'" +
    //         ", resetDate='" + getResetDate() + "'" +
    //         ", authorities='" + getAuthorities() + "'" +
    //         "}";
    // }

}
