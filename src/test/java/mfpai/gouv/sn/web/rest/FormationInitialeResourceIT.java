package mfpai.gouv.sn.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import mfpai.gouv.sn.IntegrationTest;
import mfpai.gouv.sn.domain.FormationInitiale;
import mfpai.gouv.sn.domain.enumeration.Admission;
import mfpai.gouv.sn.domain.enumeration.CFP;
import mfpai.gouv.sn.domain.enumeration.DiplomeObtenu;
import mfpai.gouv.sn.domain.enumeration.DiplomeRequis;
import mfpai.gouv.sn.domain.enumeration.LYCEE;
import mfpai.gouv.sn.domain.enumeration.NiveauEtude;
import mfpai.gouv.sn.domain.enumeration.NomFiliere;
import mfpai.gouv.sn.domain.enumeration.NomSerie;
import mfpai.gouv.sn.repository.FormationInitialeRepository;
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
 * Integration tests for the {@link FormationInitialeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FormationInitialeResourceIT {

    private static final String DEFAULT_NOM_FORMATION_I = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FORMATION_I = "BBBBBBBBBB";

    private static final String DEFAULT_DUREE = "AAAAAAAAAA";
    private static final String UPDATED_DUREE = "BBBBBBBBBB";

    private static final Admission DEFAULT_ADMISSION = Admission.CONCOURS;
    private static final Admission UPDATED_ADMISSION = Admission.PC;

    private static final DiplomeRequis DEFAULT_DIPLOME_REQUIS = DiplomeRequis.ATTESTATION;
    private static final DiplomeRequis UPDATED_DIPLOME_REQUIS = DiplomeRequis.CAP;

    private static final NiveauEtude DEFAULT_NIVEAU_ETUDE = NiveauEtude.CINQUIEME;
    private static final NiveauEtude UPDATED_NIVEAU_ETUDE = NiveauEtude.QUATRIEME;

    private static final byte[] DEFAULT_FICHE_FORMATION = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FICHE_FORMATION = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FICHE_FORMATION_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FICHE_FORMATION_CONTENT_TYPE = "image/png";

    private static final NomFiliere DEFAULT_FILIERE = NomFiliere.AGRI_ELEVAGE;
    private static final NomFiliere UPDATED_FILIERE = NomFiliere.AGRICULTURE;

    private static final NomSerie DEFAULT_SERIE = NomSerie.STEG;
    private static final NomSerie UPDATED_SERIE = NomSerie.STIDD_M;

    private static final CFP DEFAULT_CFP = CFP.CEDT_G15;
    private static final CFP UPDATED_CFP = CFP.CFP_OUAKAM;

    private static final LYCEE DEFAULT_LYCEE = LYCEE.LTID_DAKAR;
    private static final LYCEE UPDATED_LYCEE = LYCEE.LTCD_DAKAR;

    private static final String DEFAULT_NOM_CONCOURS = "AAAAAAAAAA";
    private static final String UPDATED_NOM_CONCOURS = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_OUVERTURE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OUVERTURE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_CLOTURE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CLOTURE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_CONCOURS = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CONCOURS = LocalDate.now(ZoneId.systemDefault());

    private static final DiplomeObtenu DEFAULT_NOM_DIPLOME = DiplomeObtenu.CPS;
    private static final DiplomeObtenu UPDATED_NOM_DIPLOME = DiplomeObtenu.CAP;

    private static final String DEFAULT_NOM_DEBOUCHE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_DEBOUCHE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/formation-initiales";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FormationInitialeRepository formationInitialeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFormationInitialeMockMvc;

    private FormationInitiale formationInitiale;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FormationInitiale createEntity(EntityManager em) {
        FormationInitiale formationInitiale = new FormationInitiale()
            .nomFormationI(DEFAULT_NOM_FORMATION_I)
            .duree(DEFAULT_DUREE)
            .admission(DEFAULT_ADMISSION)
            .diplomeRequis(DEFAULT_DIPLOME_REQUIS)
            .niveauEtude(DEFAULT_NIVEAU_ETUDE)
            .ficheFormation(DEFAULT_FICHE_FORMATION)
            .ficheFormationContentType(DEFAULT_FICHE_FORMATION_CONTENT_TYPE)
            .filiere(DEFAULT_FILIERE)
            .serie(DEFAULT_SERIE)
            .cfp(DEFAULT_CFP)
            .lycee(DEFAULT_LYCEE)
            .nomConcours(DEFAULT_NOM_CONCOURS)
            .dateOuverture(DEFAULT_DATE_OUVERTURE)
            .dateCloture(DEFAULT_DATE_CLOTURE)
            .dateConcours(DEFAULT_DATE_CONCOURS)
            .nomDiplome(DEFAULT_NOM_DIPLOME)
            .nomDebouche(DEFAULT_NOM_DEBOUCHE);
        return formationInitiale;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FormationInitiale createUpdatedEntity(EntityManager em) {
        FormationInitiale formationInitiale = new FormationInitiale()
            .nomFormationI(UPDATED_NOM_FORMATION_I)
            .duree(UPDATED_DUREE)
            .admission(UPDATED_ADMISSION)
            .diplomeRequis(UPDATED_DIPLOME_REQUIS)
            .niveauEtude(UPDATED_NIVEAU_ETUDE)
            .ficheFormation(UPDATED_FICHE_FORMATION)
            .ficheFormationContentType(UPDATED_FICHE_FORMATION_CONTENT_TYPE)
            .filiere(UPDATED_FILIERE)
            .serie(UPDATED_SERIE)
            .cfp(UPDATED_CFP)
            .lycee(UPDATED_LYCEE)
            .nomConcours(UPDATED_NOM_CONCOURS)
            .dateOuverture(UPDATED_DATE_OUVERTURE)
            .dateCloture(UPDATED_DATE_CLOTURE)
            .dateConcours(UPDATED_DATE_CONCOURS)
            .nomDiplome(UPDATED_NOM_DIPLOME)
            .nomDebouche(UPDATED_NOM_DEBOUCHE);
        return formationInitiale;
    }

    @BeforeEach
    public void initTest() {
        formationInitiale = createEntity(em);
    }

    @Test
    @Transactional
    void createFormationInitiale() throws Exception {
        int databaseSizeBeforeCreate = formationInitialeRepository.findAll().size();
        // Create the FormationInitiale
        restFormationInitialeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formationInitiale))
            )
            .andExpect(status().isCreated());

        // Validate the FormationInitiale in the database
        List<FormationInitiale> formationInitialeList = formationInitialeRepository.findAll();
        assertThat(formationInitialeList).hasSize(databaseSizeBeforeCreate + 1);
        FormationInitiale testFormationInitiale = formationInitialeList.get(formationInitialeList.size() - 1);
        assertThat(testFormationInitiale.getNomFormationI()).isEqualTo(DEFAULT_NOM_FORMATION_I);
        assertThat(testFormationInitiale.getDuree()).isEqualTo(DEFAULT_DUREE);
        assertThat(testFormationInitiale.getAdmission()).isEqualTo(DEFAULT_ADMISSION);
        assertThat(testFormationInitiale.getDiplomeRequis()).isEqualTo(DEFAULT_DIPLOME_REQUIS);
        assertThat(testFormationInitiale.getNiveauEtude()).isEqualTo(DEFAULT_NIVEAU_ETUDE);
        assertThat(testFormationInitiale.getFicheFormation()).isEqualTo(DEFAULT_FICHE_FORMATION);
        assertThat(testFormationInitiale.getFicheFormationContentType()).isEqualTo(DEFAULT_FICHE_FORMATION_CONTENT_TYPE);
        assertThat(testFormationInitiale.getFiliere()).isEqualTo(DEFAULT_FILIERE);
        assertThat(testFormationInitiale.getSerie()).isEqualTo(DEFAULT_SERIE);
        assertThat(testFormationInitiale.getCfp()).isEqualTo(DEFAULT_CFP);
        assertThat(testFormationInitiale.getLycee()).isEqualTo(DEFAULT_LYCEE);
        assertThat(testFormationInitiale.getNomConcours()).isEqualTo(DEFAULT_NOM_CONCOURS);
        assertThat(testFormationInitiale.getDateOuverture()).isEqualTo(DEFAULT_DATE_OUVERTURE);
        assertThat(testFormationInitiale.getDateCloture()).isEqualTo(DEFAULT_DATE_CLOTURE);
        assertThat(testFormationInitiale.getDateConcours()).isEqualTo(DEFAULT_DATE_CONCOURS);
        assertThat(testFormationInitiale.getNomDiplome()).isEqualTo(DEFAULT_NOM_DIPLOME);
        assertThat(testFormationInitiale.getNomDebouche()).isEqualTo(DEFAULT_NOM_DEBOUCHE);
    }

    @Test
    @Transactional
    void createFormationInitialeWithExistingId() throws Exception {
        // Create the FormationInitiale with an existing ID
        formationInitiale.setId(1L);

        int databaseSizeBeforeCreate = formationInitialeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormationInitialeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formationInitiale))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormationInitiale in the database
        List<FormationInitiale> formationInitialeList = formationInitialeRepository.findAll();
        assertThat(formationInitialeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFormationInitiales() throws Exception {
        // Initialize the database
        formationInitialeRepository.saveAndFlush(formationInitiale);

        // Get all the formationInitialeList
        restFormationInitialeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formationInitiale.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFormationI").value(hasItem(DEFAULT_NOM_FORMATION_I)))
            .andExpect(jsonPath("$.[*].duree").value(hasItem(DEFAULT_DUREE)))
            .andExpect(jsonPath("$.[*].admission").value(hasItem(DEFAULT_ADMISSION.toString())))
            .andExpect(jsonPath("$.[*].diplomeRequis").value(hasItem(DEFAULT_DIPLOME_REQUIS.toString())))
            .andExpect(jsonPath("$.[*].niveauEtude").value(hasItem(DEFAULT_NIVEAU_ETUDE.toString())))
            .andExpect(jsonPath("$.[*].ficheFormationContentType").value(hasItem(DEFAULT_FICHE_FORMATION_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].ficheFormation").value(hasItem(Base64Utils.encodeToString(DEFAULT_FICHE_FORMATION))))
            .andExpect(jsonPath("$.[*].filiere").value(hasItem(DEFAULT_FILIERE.toString())))
            .andExpect(jsonPath("$.[*].serie").value(hasItem(DEFAULT_SERIE.toString())))
            .andExpect(jsonPath("$.[*].cfp").value(hasItem(DEFAULT_CFP.toString())))
            .andExpect(jsonPath("$.[*].lycee").value(hasItem(DEFAULT_LYCEE.toString())))
            .andExpect(jsonPath("$.[*].nomConcours").value(hasItem(DEFAULT_NOM_CONCOURS)))
            .andExpect(jsonPath("$.[*].dateOuverture").value(hasItem(DEFAULT_DATE_OUVERTURE.toString())))
            .andExpect(jsonPath("$.[*].dateCloture").value(hasItem(DEFAULT_DATE_CLOTURE.toString())))
            .andExpect(jsonPath("$.[*].dateConcours").value(hasItem(DEFAULT_DATE_CONCOURS.toString())))
            .andExpect(jsonPath("$.[*].nomDiplome").value(hasItem(DEFAULT_NOM_DIPLOME.toString())))
            .andExpect(jsonPath("$.[*].nomDebouche").value(hasItem(DEFAULT_NOM_DEBOUCHE)));
    }

    @Test
    @Transactional
    void getFormationInitiale() throws Exception {
        // Initialize the database
        formationInitialeRepository.saveAndFlush(formationInitiale);

        // Get the formationInitiale
        restFormationInitialeMockMvc
            .perform(get(ENTITY_API_URL_ID, formationInitiale.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(formationInitiale.getId().intValue()))
            .andExpect(jsonPath("$.nomFormationI").value(DEFAULT_NOM_FORMATION_I))
            .andExpect(jsonPath("$.duree").value(DEFAULT_DUREE))
            .andExpect(jsonPath("$.admission").value(DEFAULT_ADMISSION.toString()))
            .andExpect(jsonPath("$.diplomeRequis").value(DEFAULT_DIPLOME_REQUIS.toString()))
            .andExpect(jsonPath("$.niveauEtude").value(DEFAULT_NIVEAU_ETUDE.toString()))
            .andExpect(jsonPath("$.ficheFormationContentType").value(DEFAULT_FICHE_FORMATION_CONTENT_TYPE))
            .andExpect(jsonPath("$.ficheFormation").value(Base64Utils.encodeToString(DEFAULT_FICHE_FORMATION)))
            .andExpect(jsonPath("$.filiere").value(DEFAULT_FILIERE.toString()))
            .andExpect(jsonPath("$.serie").value(DEFAULT_SERIE.toString()))
            .andExpect(jsonPath("$.cfp").value(DEFAULT_CFP.toString()))
            .andExpect(jsonPath("$.lycee").value(DEFAULT_LYCEE.toString()))
            .andExpect(jsonPath("$.nomConcours").value(DEFAULT_NOM_CONCOURS))
            .andExpect(jsonPath("$.dateOuverture").value(DEFAULT_DATE_OUVERTURE.toString()))
            .andExpect(jsonPath("$.dateCloture").value(DEFAULT_DATE_CLOTURE.toString()))
            .andExpect(jsonPath("$.dateConcours").value(DEFAULT_DATE_CONCOURS.toString()))
            .andExpect(jsonPath("$.nomDiplome").value(DEFAULT_NOM_DIPLOME.toString()))
            .andExpect(jsonPath("$.nomDebouche").value(DEFAULT_NOM_DEBOUCHE));
    }

    @Test
    @Transactional
    void getNonExistingFormationInitiale() throws Exception {
        // Get the formationInitiale
        restFormationInitialeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFormationInitiale() throws Exception {
        // Initialize the database
        formationInitialeRepository.saveAndFlush(formationInitiale);

        int databaseSizeBeforeUpdate = formationInitialeRepository.findAll().size();

        // Update the formationInitiale
        FormationInitiale updatedFormationInitiale = formationInitialeRepository.findById(formationInitiale.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFormationInitiale are not directly saved in db
        em.detach(updatedFormationInitiale);
        updatedFormationInitiale
            .nomFormationI(UPDATED_NOM_FORMATION_I)
            .duree(UPDATED_DUREE)
            .admission(UPDATED_ADMISSION)
            .diplomeRequis(UPDATED_DIPLOME_REQUIS)
            .niveauEtude(UPDATED_NIVEAU_ETUDE)
            .ficheFormation(UPDATED_FICHE_FORMATION)
            .ficheFormationContentType(UPDATED_FICHE_FORMATION_CONTENT_TYPE)
            .filiere(UPDATED_FILIERE)
            .serie(UPDATED_SERIE)
            .cfp(UPDATED_CFP)
            .lycee(UPDATED_LYCEE)
            .nomConcours(UPDATED_NOM_CONCOURS)
            .dateOuverture(UPDATED_DATE_OUVERTURE)
            .dateCloture(UPDATED_DATE_CLOTURE)
            .dateConcours(UPDATED_DATE_CONCOURS)
            .nomDiplome(UPDATED_NOM_DIPLOME)
            .nomDebouche(UPDATED_NOM_DEBOUCHE);

        restFormationInitialeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFormationInitiale.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFormationInitiale))
            )
            .andExpect(status().isOk());

        // Validate the FormationInitiale in the database
        List<FormationInitiale> formationInitialeList = formationInitialeRepository.findAll();
        assertThat(formationInitialeList).hasSize(databaseSizeBeforeUpdate);
        FormationInitiale testFormationInitiale = formationInitialeList.get(formationInitialeList.size() - 1);
        assertThat(testFormationInitiale.getNomFormationI()).isEqualTo(UPDATED_NOM_FORMATION_I);
        assertThat(testFormationInitiale.getDuree()).isEqualTo(UPDATED_DUREE);
        assertThat(testFormationInitiale.getAdmission()).isEqualTo(UPDATED_ADMISSION);
        assertThat(testFormationInitiale.getDiplomeRequis()).isEqualTo(UPDATED_DIPLOME_REQUIS);
        assertThat(testFormationInitiale.getNiveauEtude()).isEqualTo(UPDATED_NIVEAU_ETUDE);
        assertThat(testFormationInitiale.getFicheFormation()).isEqualTo(UPDATED_FICHE_FORMATION);
        assertThat(testFormationInitiale.getFicheFormationContentType()).isEqualTo(UPDATED_FICHE_FORMATION_CONTENT_TYPE);
        assertThat(testFormationInitiale.getFiliere()).isEqualTo(UPDATED_FILIERE);
        assertThat(testFormationInitiale.getSerie()).isEqualTo(UPDATED_SERIE);
        assertThat(testFormationInitiale.getCfp()).isEqualTo(UPDATED_CFP);
        assertThat(testFormationInitiale.getLycee()).isEqualTo(UPDATED_LYCEE);
        assertThat(testFormationInitiale.getNomConcours()).isEqualTo(UPDATED_NOM_CONCOURS);
        assertThat(testFormationInitiale.getDateOuverture()).isEqualTo(UPDATED_DATE_OUVERTURE);
        assertThat(testFormationInitiale.getDateCloture()).isEqualTo(UPDATED_DATE_CLOTURE);
        assertThat(testFormationInitiale.getDateConcours()).isEqualTo(UPDATED_DATE_CONCOURS);
        assertThat(testFormationInitiale.getNomDiplome()).isEqualTo(UPDATED_NOM_DIPLOME);
        assertThat(testFormationInitiale.getNomDebouche()).isEqualTo(UPDATED_NOM_DEBOUCHE);
    }

    @Test
    @Transactional
    void putNonExistingFormationInitiale() throws Exception {
        int databaseSizeBeforeUpdate = formationInitialeRepository.findAll().size();
        formationInitiale.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormationInitialeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formationInitiale.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationInitiale))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormationInitiale in the database
        List<FormationInitiale> formationInitialeList = formationInitialeRepository.findAll();
        assertThat(formationInitialeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFormationInitiale() throws Exception {
        int databaseSizeBeforeUpdate = formationInitialeRepository.findAll().size();
        formationInitiale.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationInitialeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationInitiale))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormationInitiale in the database
        List<FormationInitiale> formationInitialeList = formationInitialeRepository.findAll();
        assertThat(formationInitialeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFormationInitiale() throws Exception {
        int databaseSizeBeforeUpdate = formationInitialeRepository.findAll().size();
        formationInitiale.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationInitialeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formationInitiale))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FormationInitiale in the database
        List<FormationInitiale> formationInitialeList = formationInitialeRepository.findAll();
        assertThat(formationInitialeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFormationInitialeWithPatch() throws Exception {
        // Initialize the database
        formationInitialeRepository.saveAndFlush(formationInitiale);

        int databaseSizeBeforeUpdate = formationInitialeRepository.findAll().size();

        // Update the formationInitiale using partial update
        FormationInitiale partialUpdatedFormationInitiale = new FormationInitiale();
        partialUpdatedFormationInitiale.setId(formationInitiale.getId());

        partialUpdatedFormationInitiale
            .diplomeRequis(UPDATED_DIPLOME_REQUIS)
            .ficheFormation(UPDATED_FICHE_FORMATION)
            .ficheFormationContentType(UPDATED_FICHE_FORMATION_CONTENT_TYPE)
            .filiere(UPDATED_FILIERE)
            .cfp(UPDATED_CFP)
            .dateCloture(UPDATED_DATE_CLOTURE)
            .dateConcours(UPDATED_DATE_CONCOURS)
            .nomDebouche(UPDATED_NOM_DEBOUCHE);

        restFormationInitialeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormationInitiale.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormationInitiale))
            )
            .andExpect(status().isOk());

        // Validate the FormationInitiale in the database
        List<FormationInitiale> formationInitialeList = formationInitialeRepository.findAll();
        assertThat(formationInitialeList).hasSize(databaseSizeBeforeUpdate);
        FormationInitiale testFormationInitiale = formationInitialeList.get(formationInitialeList.size() - 1);
        assertThat(testFormationInitiale.getNomFormationI()).isEqualTo(DEFAULT_NOM_FORMATION_I);
        assertThat(testFormationInitiale.getDuree()).isEqualTo(DEFAULT_DUREE);
        assertThat(testFormationInitiale.getAdmission()).isEqualTo(DEFAULT_ADMISSION);
        assertThat(testFormationInitiale.getDiplomeRequis()).isEqualTo(UPDATED_DIPLOME_REQUIS);
        assertThat(testFormationInitiale.getNiveauEtude()).isEqualTo(DEFAULT_NIVEAU_ETUDE);
        assertThat(testFormationInitiale.getFicheFormation()).isEqualTo(UPDATED_FICHE_FORMATION);
        assertThat(testFormationInitiale.getFicheFormationContentType()).isEqualTo(UPDATED_FICHE_FORMATION_CONTENT_TYPE);
        assertThat(testFormationInitiale.getFiliere()).isEqualTo(UPDATED_FILIERE);
        assertThat(testFormationInitiale.getSerie()).isEqualTo(DEFAULT_SERIE);
        assertThat(testFormationInitiale.getCfp()).isEqualTo(UPDATED_CFP);
        assertThat(testFormationInitiale.getLycee()).isEqualTo(DEFAULT_LYCEE);
        assertThat(testFormationInitiale.getNomConcours()).isEqualTo(DEFAULT_NOM_CONCOURS);
        assertThat(testFormationInitiale.getDateOuverture()).isEqualTo(DEFAULT_DATE_OUVERTURE);
        assertThat(testFormationInitiale.getDateCloture()).isEqualTo(UPDATED_DATE_CLOTURE);
        assertThat(testFormationInitiale.getDateConcours()).isEqualTo(UPDATED_DATE_CONCOURS);
        assertThat(testFormationInitiale.getNomDiplome()).isEqualTo(DEFAULT_NOM_DIPLOME);
        assertThat(testFormationInitiale.getNomDebouche()).isEqualTo(UPDATED_NOM_DEBOUCHE);
    }

    @Test
    @Transactional
    void fullUpdateFormationInitialeWithPatch() throws Exception {
        // Initialize the database
        formationInitialeRepository.saveAndFlush(formationInitiale);

        int databaseSizeBeforeUpdate = formationInitialeRepository.findAll().size();

        // Update the formationInitiale using partial update
        FormationInitiale partialUpdatedFormationInitiale = new FormationInitiale();
        partialUpdatedFormationInitiale.setId(formationInitiale.getId());

        partialUpdatedFormationInitiale
            .nomFormationI(UPDATED_NOM_FORMATION_I)
            .duree(UPDATED_DUREE)
            .admission(UPDATED_ADMISSION)
            .diplomeRequis(UPDATED_DIPLOME_REQUIS)
            .niveauEtude(UPDATED_NIVEAU_ETUDE)
            .ficheFormation(UPDATED_FICHE_FORMATION)
            .ficheFormationContentType(UPDATED_FICHE_FORMATION_CONTENT_TYPE)
            .filiere(UPDATED_FILIERE)
            .serie(UPDATED_SERIE)
            .cfp(UPDATED_CFP)
            .lycee(UPDATED_LYCEE)
            .nomConcours(UPDATED_NOM_CONCOURS)
            .dateOuverture(UPDATED_DATE_OUVERTURE)
            .dateCloture(UPDATED_DATE_CLOTURE)
            .dateConcours(UPDATED_DATE_CONCOURS)
            .nomDiplome(UPDATED_NOM_DIPLOME)
            .nomDebouche(UPDATED_NOM_DEBOUCHE);

        restFormationInitialeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormationInitiale.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormationInitiale))
            )
            .andExpect(status().isOk());

        // Validate the FormationInitiale in the database
        List<FormationInitiale> formationInitialeList = formationInitialeRepository.findAll();
        assertThat(formationInitialeList).hasSize(databaseSizeBeforeUpdate);
        FormationInitiale testFormationInitiale = formationInitialeList.get(formationInitialeList.size() - 1);
        assertThat(testFormationInitiale.getNomFormationI()).isEqualTo(UPDATED_NOM_FORMATION_I);
        assertThat(testFormationInitiale.getDuree()).isEqualTo(UPDATED_DUREE);
        assertThat(testFormationInitiale.getAdmission()).isEqualTo(UPDATED_ADMISSION);
        assertThat(testFormationInitiale.getDiplomeRequis()).isEqualTo(UPDATED_DIPLOME_REQUIS);
        assertThat(testFormationInitiale.getNiveauEtude()).isEqualTo(UPDATED_NIVEAU_ETUDE);
        assertThat(testFormationInitiale.getFicheFormation()).isEqualTo(UPDATED_FICHE_FORMATION);
        assertThat(testFormationInitiale.getFicheFormationContentType()).isEqualTo(UPDATED_FICHE_FORMATION_CONTENT_TYPE);
        assertThat(testFormationInitiale.getFiliere()).isEqualTo(UPDATED_FILIERE);
        assertThat(testFormationInitiale.getSerie()).isEqualTo(UPDATED_SERIE);
        assertThat(testFormationInitiale.getCfp()).isEqualTo(UPDATED_CFP);
        assertThat(testFormationInitiale.getLycee()).isEqualTo(UPDATED_LYCEE);
        assertThat(testFormationInitiale.getNomConcours()).isEqualTo(UPDATED_NOM_CONCOURS);
        assertThat(testFormationInitiale.getDateOuverture()).isEqualTo(UPDATED_DATE_OUVERTURE);
        assertThat(testFormationInitiale.getDateCloture()).isEqualTo(UPDATED_DATE_CLOTURE);
        assertThat(testFormationInitiale.getDateConcours()).isEqualTo(UPDATED_DATE_CONCOURS);
        assertThat(testFormationInitiale.getNomDiplome()).isEqualTo(UPDATED_NOM_DIPLOME);
        assertThat(testFormationInitiale.getNomDebouche()).isEqualTo(UPDATED_NOM_DEBOUCHE);
    }

    @Test
    @Transactional
    void patchNonExistingFormationInitiale() throws Exception {
        int databaseSizeBeforeUpdate = formationInitialeRepository.findAll().size();
        formationInitiale.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormationInitialeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, formationInitiale.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formationInitiale))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormationInitiale in the database
        List<FormationInitiale> formationInitialeList = formationInitialeRepository.findAll();
        assertThat(formationInitialeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFormationInitiale() throws Exception {
        int databaseSizeBeforeUpdate = formationInitialeRepository.findAll().size();
        formationInitiale.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationInitialeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formationInitiale))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormationInitiale in the database
        List<FormationInitiale> formationInitialeList = formationInitialeRepository.findAll();
        assertThat(formationInitialeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFormationInitiale() throws Exception {
        int databaseSizeBeforeUpdate = formationInitialeRepository.findAll().size();
        formationInitiale.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationInitialeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formationInitiale))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FormationInitiale in the database
        List<FormationInitiale> formationInitialeList = formationInitialeRepository.findAll();
        assertThat(formationInitialeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFormationInitiale() throws Exception {
        // Initialize the database
        formationInitialeRepository.saveAndFlush(formationInitiale);

        int databaseSizeBeforeDelete = formationInitialeRepository.findAll().size();

        // Delete the formationInitiale
        restFormationInitialeMockMvc
            .perform(delete(ENTITY_API_URL_ID, formationInitiale.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FormationInitiale> formationInitialeList = formationInitialeRepository.findAll();
        assertThat(formationInitialeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
