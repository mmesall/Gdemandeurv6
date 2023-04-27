package mfpai.gouv.sn.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import mfpai.gouv.sn.IntegrationTest;
import mfpai.gouv.sn.domain.Dossier;
import mfpai.gouv.sn.domain.enumeration.DiplomeRequis;
import mfpai.gouv.sn.domain.enumeration.NiveauEtude;
import mfpai.gouv.sn.domain.enumeration.NomDepartement;
import mfpai.gouv.sn.domain.enumeration.NomDepartement;
import mfpai.gouv.sn.domain.enumeration.NomFiliere;
import mfpai.gouv.sn.domain.enumeration.NomRegion;
import mfpai.gouv.sn.domain.enumeration.NomRegion;
import mfpai.gouv.sn.domain.enumeration.NomSerie;
import mfpai.gouv.sn.domain.enumeration.Sexe;
import mfpai.gouv.sn.domain.enumeration.TypePiece;
import mfpai.gouv.sn.repository.DossierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link DossierResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DossierResourceIT {

    private static final String DEFAULT_NUM_DOSSIER = "AAAAAAAAAA";
    private static final String UPDATED_NUM_DOSSIER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_NAISS = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_NAISS = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_UTILISATEUR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_UTILISATEUR = "BBBBBBBBBB";

    private static final NomRegion DEFAULT_REGION_NAISS = NomRegion.DAKAR;
    private static final NomRegion UPDATED_REGION_NAISS = NomRegion.DIOURBEL;

    private static final NomDepartement DEFAULT_DEPARTEMENT_NAISS = NomDepartement.DAKAR;
    private static final NomDepartement UPDATED_DEPARTEMENT_NAISS = NomDepartement.GUEDIAWAYE;

    private static final String DEFAULT_LIEU_NAISS = "AAAAAAAAAA";
    private static final String UPDATED_LIEU_NAISS = "BBBBBBBBBB";

    private static final TypePiece DEFAULT_TYPE_PIECE = TypePiece.CNI;
    private static final TypePiece UPDATED_TYPE_PIECE = TypePiece.PASSEPORT;

    private static final Long DEFAULT_NUMERO_PIECE = 1L;
    private static final Long UPDATED_NUMERO_PIECE = 2L;

    private static final Sexe DEFAULT_SEXE = Sexe.HOMME;
    private static final Sexe UPDATED_SEXE = Sexe.FEMME;

    private static final NomRegion DEFAULT_REGION_RESIDENCE = NomRegion.DAKAR;
    private static final NomRegion UPDATED_REGION_RESIDENCE = NomRegion.DIOURBEL;

    private static final NomDepartement DEFAULT_DEP_RESIDENCE = NomDepartement.DAKAR;
    private static final NomDepartement UPDATED_DEP_RESIDENCE = NomDepartement.GUEDIAWAYE;

    private static final String DEFAULT_ADRESSE_RESIDENCE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE_RESIDENCE = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE_1 = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE_1 = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE_2 = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE_2 = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final NiveauEtude DEFAULT_NIVEAU_FORMATION = NiveauEtude.Cinquieme;
    private static final NiveauEtude UPDATED_NIVEAU_FORMATION = NiveauEtude.Quatrieme;

    private static final NomFiliere DEFAULT_SPECIALITE_1 = NomFiliere.AGRI_ELEVAGE;
    private static final NomFiliere UPDATED_SPECIALITE_1 = NomFiliere.AGRICULTURE;

    private static final NomSerie DEFAULT_SPECIALITE_2 = NomSerie.STEG;
    private static final NomSerie UPDATED_SPECIALITE_2 = NomSerie.STIDD_M;

    private static final DiplomeRequis DEFAULT_DIPLOME_REQUIS = DiplomeRequis.ATTESTATION;
    private static final DiplomeRequis UPDATED_DIPLOME_REQUIS = DiplomeRequis.CAP;

    private static final byte[] DEFAULT_CV = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_CV = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_CV_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CV_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_LETTRE_MOTIVATION = "AAAAAAAAAA";
    private static final String UPDATED_LETTRE_MOTIVATION = "BBBBBBBBBB";

    private static final String DEFAULT_PROFESSION = "AAAAAAAAAA";
    private static final String UPDATED_PROFESSION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/dossiers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DossierRepository dossierRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDossierMockMvc;

    private Dossier dossier;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dossier createEntity(EntityManager em) {
        Dossier dossier = new Dossier()
            .numDossier(DEFAULT_NUM_DOSSIER)
            .dateNaiss(DEFAULT_DATE_NAISS)
            .prenom(DEFAULT_PRENOM)
            .nom(DEFAULT_NOM)
            .nomUtilisateur(DEFAULT_NOM_UTILISATEUR)
            .regionNaiss(DEFAULT_REGION_NAISS)
            .departementNaiss(DEFAULT_DEPARTEMENT_NAISS)
            .lieuNaiss(DEFAULT_LIEU_NAISS)
            .typePiece(DEFAULT_TYPE_PIECE)
            .numeroPiece(DEFAULT_NUMERO_PIECE)
            .sexe(DEFAULT_SEXE)
            .regionResidence(DEFAULT_REGION_RESIDENCE)
            .depResidence(DEFAULT_DEP_RESIDENCE)
            .adresseResidence(DEFAULT_ADRESSE_RESIDENCE)
            .telephone1(DEFAULT_TELEPHONE_1)
            .telephone2(DEFAULT_TELEPHONE_2)
            .email(DEFAULT_EMAIL)
            .niveauFormation(DEFAULT_NIVEAU_FORMATION)
            .specialite1(DEFAULT_SPECIALITE_1)
            .specialite2(DEFAULT_SPECIALITE_2)
            .diplomeRequis(DEFAULT_DIPLOME_REQUIS)
            .cv(DEFAULT_CV)
            .cvContentType(DEFAULT_CV_CONTENT_TYPE)
            .lettreMotivation(DEFAULT_LETTRE_MOTIVATION)
            .profession(DEFAULT_PROFESSION);
        return dossier;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dossier createUpdatedEntity(EntityManager em) {
        Dossier dossier = new Dossier()
            .numDossier(UPDATED_NUM_DOSSIER)
            .dateNaiss(UPDATED_DATE_NAISS)
            .prenom(UPDATED_PRENOM)
            .nom(UPDATED_NOM)
            .nomUtilisateur(UPDATED_NOM_UTILISATEUR)
            .regionNaiss(UPDATED_REGION_NAISS)
            .departementNaiss(UPDATED_DEPARTEMENT_NAISS)
            .lieuNaiss(UPDATED_LIEU_NAISS)
            .typePiece(UPDATED_TYPE_PIECE)
            .numeroPiece(UPDATED_NUMERO_PIECE)
            .sexe(UPDATED_SEXE)
            .regionResidence(UPDATED_REGION_RESIDENCE)
            .depResidence(UPDATED_DEP_RESIDENCE)
            .adresseResidence(UPDATED_ADRESSE_RESIDENCE)
            .telephone1(UPDATED_TELEPHONE_1)
            .telephone2(UPDATED_TELEPHONE_2)
            .email(UPDATED_EMAIL)
            .niveauFormation(UPDATED_NIVEAU_FORMATION)
            .specialite1(UPDATED_SPECIALITE_1)
            .specialite2(UPDATED_SPECIALITE_2)
            .diplomeRequis(UPDATED_DIPLOME_REQUIS)
            .cv(UPDATED_CV)
            .cvContentType(UPDATED_CV_CONTENT_TYPE)
            .lettreMotivation(UPDATED_LETTRE_MOTIVATION)
            .profession(UPDATED_PROFESSION);
        return dossier;
    }

    @BeforeEach
    public void initTest() {
        dossier = createEntity(em);
    }

    @Test
    @Transactional
    void createDossier() throws Exception {
        int databaseSizeBeforeCreate = dossierRepository.findAll().size();
        // Create the Dossier
        restDossierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dossier)))
            .andExpect(status().isCreated());

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeCreate + 1);
        Dossier testDossier = dossierList.get(dossierList.size() - 1);
        assertThat(testDossier.getNumDossier()).isEqualTo(DEFAULT_NUM_DOSSIER);
        assertThat(testDossier.getDateNaiss()).isEqualTo(DEFAULT_DATE_NAISS);
        assertThat(testDossier.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testDossier.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testDossier.getNomUtilisateur()).isEqualTo(DEFAULT_NOM_UTILISATEUR);
        assertThat(testDossier.getRegionNaiss()).isEqualTo(DEFAULT_REGION_NAISS);
        assertThat(testDossier.getDepartementNaiss()).isEqualTo(DEFAULT_DEPARTEMENT_NAISS);
        assertThat(testDossier.getLieuNaiss()).isEqualTo(DEFAULT_LIEU_NAISS);
        assertThat(testDossier.getTypePiece()).isEqualTo(DEFAULT_TYPE_PIECE);
        assertThat(testDossier.getNumeroPiece()).isEqualTo(DEFAULT_NUMERO_PIECE);
        assertThat(testDossier.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testDossier.getRegionResidence()).isEqualTo(DEFAULT_REGION_RESIDENCE);
        assertThat(testDossier.getDepResidence()).isEqualTo(DEFAULT_DEP_RESIDENCE);
        assertThat(testDossier.getAdresseResidence()).isEqualTo(DEFAULT_ADRESSE_RESIDENCE);
        assertThat(testDossier.getTelephone1()).isEqualTo(DEFAULT_TELEPHONE_1);
        assertThat(testDossier.getTelephone2()).isEqualTo(DEFAULT_TELEPHONE_2);
        assertThat(testDossier.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testDossier.getNiveauFormation()).isEqualTo(DEFAULT_NIVEAU_FORMATION);
        assertThat(testDossier.getSpecialite1()).isEqualTo(DEFAULT_SPECIALITE_1);
        assertThat(testDossier.getSpecialite2()).isEqualTo(DEFAULT_SPECIALITE_2);
        assertThat(testDossier.getDiplomeRequis()).isEqualTo(DEFAULT_DIPLOME_REQUIS);
        assertThat(testDossier.getCv()).isEqualTo(DEFAULT_CV);
        assertThat(testDossier.getCvContentType()).isEqualTo(DEFAULT_CV_CONTENT_TYPE);
        assertThat(testDossier.getLettreMotivation()).isEqualTo(DEFAULT_LETTRE_MOTIVATION);
        assertThat(testDossier.getProfession()).isEqualTo(DEFAULT_PROFESSION);
    }

    @Test
    @Transactional
    void createDossierWithExistingId() throws Exception {
        // Create the Dossier with an existing ID
        dossier.setId(1L);

        int databaseSizeBeforeCreate = dossierRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDossierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dossier)))
            .andExpect(status().isBadRequest());

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPrenomIsRequired() throws Exception {
        int databaseSizeBeforeTest = dossierRepository.findAll().size();
        // set the field null
        dossier.setPrenom(null);

        // Create the Dossier, which fails.

        restDossierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dossier)))
            .andExpect(status().isBadRequest());

        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = dossierRepository.findAll().size();
        // set the field null
        dossier.setNom(null);

        // Create the Dossier, which fails.

        restDossierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dossier)))
            .andExpect(status().isBadRequest());

        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDossiers() throws Exception {
        // Initialize the database
        dossierRepository.saveAndFlush(dossier);

        // Get all the dossierList
        restDossierMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dossier.getId().intValue())))
            .andExpect(jsonPath("$.[*].numDossier").value(hasItem(DEFAULT_NUM_DOSSIER)))
            .andExpect(jsonPath("$.[*].dateNaiss").value(hasItem(DEFAULT_DATE_NAISS.toString())))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].nomUtilisateur").value(hasItem(DEFAULT_NOM_UTILISATEUR)))
            .andExpect(jsonPath("$.[*].regionNaiss").value(hasItem(DEFAULT_REGION_NAISS.toString())))
            .andExpect(jsonPath("$.[*].departementNaiss").value(hasItem(DEFAULT_DEPARTEMENT_NAISS.toString())))
            .andExpect(jsonPath("$.[*].lieuNaiss").value(hasItem(DEFAULT_LIEU_NAISS)))
            .andExpect(jsonPath("$.[*].typePiece").value(hasItem(DEFAULT_TYPE_PIECE.toString())))
            .andExpect(jsonPath("$.[*].numeroPiece").value(hasItem(DEFAULT_NUMERO_PIECE.intValue())))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())))
            .andExpect(jsonPath("$.[*].regionResidence").value(hasItem(DEFAULT_REGION_RESIDENCE.toString())))
            .andExpect(jsonPath("$.[*].depResidence").value(hasItem(DEFAULT_DEP_RESIDENCE.toString())))
            .andExpect(jsonPath("$.[*].adresseResidence").value(hasItem(DEFAULT_ADRESSE_RESIDENCE)))
            .andExpect(jsonPath("$.[*].telephone1").value(hasItem(DEFAULT_TELEPHONE_1)))
            .andExpect(jsonPath("$.[*].telephone2").value(hasItem(DEFAULT_TELEPHONE_2)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].niveauFormation").value(hasItem(DEFAULT_NIVEAU_FORMATION.toString())))
            .andExpect(jsonPath("$.[*].specialite1").value(hasItem(DEFAULT_SPECIALITE_1.toString())))
            .andExpect(jsonPath("$.[*].specialite2").value(hasItem(DEFAULT_SPECIALITE_2.toString())))
            .andExpect(jsonPath("$.[*].diplomeRequis").value(hasItem(DEFAULT_DIPLOME_REQUIS.toString())))
            .andExpect(jsonPath("$.[*].cvContentType").value(hasItem(DEFAULT_CV_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].cv").value(hasItem(Base64Utils.encodeToString(DEFAULT_CV))))
            .andExpect(jsonPath("$.[*].lettreMotivation").value(hasItem(DEFAULT_LETTRE_MOTIVATION.toString())))
            .andExpect(jsonPath("$.[*].profession").value(hasItem(DEFAULT_PROFESSION)));
    }

    @Test
    @Transactional
    void getDossier() throws Exception {
        // Initialize the database
        dossierRepository.saveAndFlush(dossier);

        // Get the dossier
        restDossierMockMvc
            .perform(get(ENTITY_API_URL_ID, dossier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dossier.getId().intValue()))
            .andExpect(jsonPath("$.numDossier").value(DEFAULT_NUM_DOSSIER))
            .andExpect(jsonPath("$.dateNaiss").value(DEFAULT_DATE_NAISS.toString()))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.nomUtilisateur").value(DEFAULT_NOM_UTILISATEUR))
            .andExpect(jsonPath("$.regionNaiss").value(DEFAULT_REGION_NAISS.toString()))
            .andExpect(jsonPath("$.departementNaiss").value(DEFAULT_DEPARTEMENT_NAISS.toString()))
            .andExpect(jsonPath("$.lieuNaiss").value(DEFAULT_LIEU_NAISS))
            .andExpect(jsonPath("$.typePiece").value(DEFAULT_TYPE_PIECE.toString()))
            .andExpect(jsonPath("$.numeroPiece").value(DEFAULT_NUMERO_PIECE.intValue()))
            .andExpect(jsonPath("$.sexe").value(DEFAULT_SEXE.toString()))
            .andExpect(jsonPath("$.regionResidence").value(DEFAULT_REGION_RESIDENCE.toString()))
            .andExpect(jsonPath("$.depResidence").value(DEFAULT_DEP_RESIDENCE.toString()))
            .andExpect(jsonPath("$.adresseResidence").value(DEFAULT_ADRESSE_RESIDENCE))
            .andExpect(jsonPath("$.telephone1").value(DEFAULT_TELEPHONE_1))
            .andExpect(jsonPath("$.telephone2").value(DEFAULT_TELEPHONE_2))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.niveauFormation").value(DEFAULT_NIVEAU_FORMATION.toString()))
            .andExpect(jsonPath("$.specialite1").value(DEFAULT_SPECIALITE_1.toString()))
            .andExpect(jsonPath("$.specialite2").value(DEFAULT_SPECIALITE_2.toString()))
            .andExpect(jsonPath("$.diplomeRequis").value(DEFAULT_DIPLOME_REQUIS.toString()))
            .andExpect(jsonPath("$.cvContentType").value(DEFAULT_CV_CONTENT_TYPE))
            .andExpect(jsonPath("$.cv").value(Base64Utils.encodeToString(DEFAULT_CV)))
            .andExpect(jsonPath("$.lettreMotivation").value(DEFAULT_LETTRE_MOTIVATION.toString()))
            .andExpect(jsonPath("$.profession").value(DEFAULT_PROFESSION));
    }

    @Test
    @Transactional
    void getNonExistingDossier() throws Exception {
        // Get the dossier
        restDossierMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDossier() throws Exception {
        // Initialize the database
        dossierRepository.saveAndFlush(dossier);

        int databaseSizeBeforeUpdate = dossierRepository.findAll().size();

        // Update the dossier
        Dossier updatedDossier = dossierRepository.findById(dossier.getId()).get();
        // Disconnect from session so that the updates on updatedDossier are not directly saved in db
        em.detach(updatedDossier);
        updatedDossier
            .numDossier(UPDATED_NUM_DOSSIER)
            .dateNaiss(UPDATED_DATE_NAISS)
            .prenom(UPDATED_PRENOM)
            .nom(UPDATED_NOM)
            .nomUtilisateur(UPDATED_NOM_UTILISATEUR)
            .regionNaiss(UPDATED_REGION_NAISS)
            .departementNaiss(UPDATED_DEPARTEMENT_NAISS)
            .lieuNaiss(UPDATED_LIEU_NAISS)
            .typePiece(UPDATED_TYPE_PIECE)
            .numeroPiece(UPDATED_NUMERO_PIECE)
            .sexe(UPDATED_SEXE)
            .regionResidence(UPDATED_REGION_RESIDENCE)
            .depResidence(UPDATED_DEP_RESIDENCE)
            .adresseResidence(UPDATED_ADRESSE_RESIDENCE)
            .telephone1(UPDATED_TELEPHONE_1)
            .telephone2(UPDATED_TELEPHONE_2)
            .email(UPDATED_EMAIL)
            .niveauFormation(UPDATED_NIVEAU_FORMATION)
            .specialite1(UPDATED_SPECIALITE_1)
            .specialite2(UPDATED_SPECIALITE_2)
            .diplomeRequis(UPDATED_DIPLOME_REQUIS)
            .cv(UPDATED_CV)
            .cvContentType(UPDATED_CV_CONTENT_TYPE)
            .lettreMotivation(UPDATED_LETTRE_MOTIVATION)
            .profession(UPDATED_PROFESSION);

        restDossierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDossier.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDossier))
            )
            .andExpect(status().isOk());

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeUpdate);
        Dossier testDossier = dossierList.get(dossierList.size() - 1);
        assertThat(testDossier.getNumDossier()).isEqualTo(UPDATED_NUM_DOSSIER);
        assertThat(testDossier.getDateNaiss()).isEqualTo(UPDATED_DATE_NAISS);
        assertThat(testDossier.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testDossier.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testDossier.getNomUtilisateur()).isEqualTo(UPDATED_NOM_UTILISATEUR);
        assertThat(testDossier.getRegionNaiss()).isEqualTo(UPDATED_REGION_NAISS);
        assertThat(testDossier.getDepartementNaiss()).isEqualTo(UPDATED_DEPARTEMENT_NAISS);
        assertThat(testDossier.getLieuNaiss()).isEqualTo(UPDATED_LIEU_NAISS);
        assertThat(testDossier.getTypePiece()).isEqualTo(UPDATED_TYPE_PIECE);
        assertThat(testDossier.getNumeroPiece()).isEqualTo(UPDATED_NUMERO_PIECE);
        assertThat(testDossier.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testDossier.getRegionResidence()).isEqualTo(UPDATED_REGION_RESIDENCE);
        assertThat(testDossier.getDepResidence()).isEqualTo(UPDATED_DEP_RESIDENCE);
        assertThat(testDossier.getAdresseResidence()).isEqualTo(UPDATED_ADRESSE_RESIDENCE);
        assertThat(testDossier.getTelephone1()).isEqualTo(UPDATED_TELEPHONE_1);
        assertThat(testDossier.getTelephone2()).isEqualTo(UPDATED_TELEPHONE_2);
        assertThat(testDossier.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testDossier.getNiveauFormation()).isEqualTo(UPDATED_NIVEAU_FORMATION);
        assertThat(testDossier.getSpecialite1()).isEqualTo(UPDATED_SPECIALITE_1);
        assertThat(testDossier.getSpecialite2()).isEqualTo(UPDATED_SPECIALITE_2);
        assertThat(testDossier.getDiplomeRequis()).isEqualTo(UPDATED_DIPLOME_REQUIS);
        assertThat(testDossier.getCv()).isEqualTo(UPDATED_CV);
        assertThat(testDossier.getCvContentType()).isEqualTo(UPDATED_CV_CONTENT_TYPE);
        assertThat(testDossier.getLettreMotivation()).isEqualTo(UPDATED_LETTRE_MOTIVATION);
        assertThat(testDossier.getProfession()).isEqualTo(UPDATED_PROFESSION);
    }

    @Test
    @Transactional
    void putNonExistingDossier() throws Exception {
        int databaseSizeBeforeUpdate = dossierRepository.findAll().size();
        dossier.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDossierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dossier.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dossier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDossier() throws Exception {
        int databaseSizeBeforeUpdate = dossierRepository.findAll().size();
        dossier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDossierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dossier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDossier() throws Exception {
        int databaseSizeBeforeUpdate = dossierRepository.findAll().size();
        dossier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDossierMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dossier)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDossierWithPatch() throws Exception {
        // Initialize the database
        dossierRepository.saveAndFlush(dossier);

        int databaseSizeBeforeUpdate = dossierRepository.findAll().size();

        // Update the dossier using partial update
        Dossier partialUpdatedDossier = new Dossier();
        partialUpdatedDossier.setId(dossier.getId());

        partialUpdatedDossier
            .prenom(UPDATED_PRENOM)
            .nomUtilisateur(UPDATED_NOM_UTILISATEUR)
            .departementNaiss(UPDATED_DEPARTEMENT_NAISS)
            .lieuNaiss(UPDATED_LIEU_NAISS)
            .numeroPiece(UPDATED_NUMERO_PIECE)
            .adresseResidence(UPDATED_ADRESSE_RESIDENCE)
            .telephone1(UPDATED_TELEPHONE_1)
            .email(UPDATED_EMAIL)
            .specialite1(UPDATED_SPECIALITE_1)
            .specialite2(UPDATED_SPECIALITE_2)
            .diplomeRequis(UPDATED_DIPLOME_REQUIS)
            .lettreMotivation(UPDATED_LETTRE_MOTIVATION)
            .profession(UPDATED_PROFESSION);

        restDossierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDossier.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDossier))
            )
            .andExpect(status().isOk());

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeUpdate);
        Dossier testDossier = dossierList.get(dossierList.size() - 1);
        assertThat(testDossier.getNumDossier()).isEqualTo(DEFAULT_NUM_DOSSIER);
        assertThat(testDossier.getDateNaiss()).isEqualTo(DEFAULT_DATE_NAISS);
        assertThat(testDossier.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testDossier.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testDossier.getNomUtilisateur()).isEqualTo(UPDATED_NOM_UTILISATEUR);
        assertThat(testDossier.getRegionNaiss()).isEqualTo(DEFAULT_REGION_NAISS);
        assertThat(testDossier.getDepartementNaiss()).isEqualTo(UPDATED_DEPARTEMENT_NAISS);
        assertThat(testDossier.getLieuNaiss()).isEqualTo(UPDATED_LIEU_NAISS);
        assertThat(testDossier.getTypePiece()).isEqualTo(DEFAULT_TYPE_PIECE);
        assertThat(testDossier.getNumeroPiece()).isEqualTo(UPDATED_NUMERO_PIECE);
        assertThat(testDossier.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testDossier.getRegionResidence()).isEqualTo(DEFAULT_REGION_RESIDENCE);
        assertThat(testDossier.getDepResidence()).isEqualTo(DEFAULT_DEP_RESIDENCE);
        assertThat(testDossier.getAdresseResidence()).isEqualTo(UPDATED_ADRESSE_RESIDENCE);
        assertThat(testDossier.getTelephone1()).isEqualTo(UPDATED_TELEPHONE_1);
        assertThat(testDossier.getTelephone2()).isEqualTo(DEFAULT_TELEPHONE_2);
        assertThat(testDossier.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testDossier.getNiveauFormation()).isEqualTo(DEFAULT_NIVEAU_FORMATION);
        assertThat(testDossier.getSpecialite1()).isEqualTo(UPDATED_SPECIALITE_1);
        assertThat(testDossier.getSpecialite2()).isEqualTo(UPDATED_SPECIALITE_2);
        assertThat(testDossier.getDiplomeRequis()).isEqualTo(UPDATED_DIPLOME_REQUIS);
        assertThat(testDossier.getCv()).isEqualTo(DEFAULT_CV);
        assertThat(testDossier.getCvContentType()).isEqualTo(DEFAULT_CV_CONTENT_TYPE);
        assertThat(testDossier.getLettreMotivation()).isEqualTo(UPDATED_LETTRE_MOTIVATION);
        assertThat(testDossier.getProfession()).isEqualTo(UPDATED_PROFESSION);
    }

    @Test
    @Transactional
    void fullUpdateDossierWithPatch() throws Exception {
        // Initialize the database
        dossierRepository.saveAndFlush(dossier);

        int databaseSizeBeforeUpdate = dossierRepository.findAll().size();

        // Update the dossier using partial update
        Dossier partialUpdatedDossier = new Dossier();
        partialUpdatedDossier.setId(dossier.getId());

        partialUpdatedDossier
            .numDossier(UPDATED_NUM_DOSSIER)
            .dateNaiss(UPDATED_DATE_NAISS)
            .prenom(UPDATED_PRENOM)
            .nom(UPDATED_NOM)
            .nomUtilisateur(UPDATED_NOM_UTILISATEUR)
            .regionNaiss(UPDATED_REGION_NAISS)
            .departementNaiss(UPDATED_DEPARTEMENT_NAISS)
            .lieuNaiss(UPDATED_LIEU_NAISS)
            .typePiece(UPDATED_TYPE_PIECE)
            .numeroPiece(UPDATED_NUMERO_PIECE)
            .sexe(UPDATED_SEXE)
            .regionResidence(UPDATED_REGION_RESIDENCE)
            .depResidence(UPDATED_DEP_RESIDENCE)
            .adresseResidence(UPDATED_ADRESSE_RESIDENCE)
            .telephone1(UPDATED_TELEPHONE_1)
            .telephone2(UPDATED_TELEPHONE_2)
            .email(UPDATED_EMAIL)
            .niveauFormation(UPDATED_NIVEAU_FORMATION)
            .specialite1(UPDATED_SPECIALITE_1)
            .specialite2(UPDATED_SPECIALITE_2)
            .diplomeRequis(UPDATED_DIPLOME_REQUIS)
            .cv(UPDATED_CV)
            .cvContentType(UPDATED_CV_CONTENT_TYPE)
            .lettreMotivation(UPDATED_LETTRE_MOTIVATION)
            .profession(UPDATED_PROFESSION);

        restDossierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDossier.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDossier))
            )
            .andExpect(status().isOk());

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeUpdate);
        Dossier testDossier = dossierList.get(dossierList.size() - 1);
        assertThat(testDossier.getNumDossier()).isEqualTo(UPDATED_NUM_DOSSIER);
        assertThat(testDossier.getDateNaiss()).isEqualTo(UPDATED_DATE_NAISS);
        assertThat(testDossier.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testDossier.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testDossier.getNomUtilisateur()).isEqualTo(UPDATED_NOM_UTILISATEUR);
        assertThat(testDossier.getRegionNaiss()).isEqualTo(UPDATED_REGION_NAISS);
        assertThat(testDossier.getDepartementNaiss()).isEqualTo(UPDATED_DEPARTEMENT_NAISS);
        assertThat(testDossier.getLieuNaiss()).isEqualTo(UPDATED_LIEU_NAISS);
        assertThat(testDossier.getTypePiece()).isEqualTo(UPDATED_TYPE_PIECE);
        assertThat(testDossier.getNumeroPiece()).isEqualTo(UPDATED_NUMERO_PIECE);
        assertThat(testDossier.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testDossier.getRegionResidence()).isEqualTo(UPDATED_REGION_RESIDENCE);
        assertThat(testDossier.getDepResidence()).isEqualTo(UPDATED_DEP_RESIDENCE);
        assertThat(testDossier.getAdresseResidence()).isEqualTo(UPDATED_ADRESSE_RESIDENCE);
        assertThat(testDossier.getTelephone1()).isEqualTo(UPDATED_TELEPHONE_1);
        assertThat(testDossier.getTelephone2()).isEqualTo(UPDATED_TELEPHONE_2);
        assertThat(testDossier.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testDossier.getNiveauFormation()).isEqualTo(UPDATED_NIVEAU_FORMATION);
        assertThat(testDossier.getSpecialite1()).isEqualTo(UPDATED_SPECIALITE_1);
        assertThat(testDossier.getSpecialite2()).isEqualTo(UPDATED_SPECIALITE_2);
        assertThat(testDossier.getDiplomeRequis()).isEqualTo(UPDATED_DIPLOME_REQUIS);
        assertThat(testDossier.getCv()).isEqualTo(UPDATED_CV);
        assertThat(testDossier.getCvContentType()).isEqualTo(UPDATED_CV_CONTENT_TYPE);
        assertThat(testDossier.getLettreMotivation()).isEqualTo(UPDATED_LETTRE_MOTIVATION);
        assertThat(testDossier.getProfession()).isEqualTo(UPDATED_PROFESSION);
    }

    @Test
    @Transactional
    void patchNonExistingDossier() throws Exception {
        int databaseSizeBeforeUpdate = dossierRepository.findAll().size();
        dossier.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDossierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dossier.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dossier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDossier() throws Exception {
        int databaseSizeBeforeUpdate = dossierRepository.findAll().size();
        dossier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDossierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dossier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDossier() throws Exception {
        int databaseSizeBeforeUpdate = dossierRepository.findAll().size();
        dossier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDossierMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(dossier)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDossier() throws Exception {
        // Initialize the database
        dossierRepository.saveAndFlush(dossier);

        int databaseSizeBeforeDelete = dossierRepository.findAll().size();

        // Delete the dossier
        restDossierMockMvc
            .perform(delete(ENTITY_API_URL_ID, dossier.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
