package mfpai.gouv.sn.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import mfpai.gouv.sn.IntegrationTest;
import mfpai.gouv.sn.domain.FormationContinue;
import mfpai.gouv.sn.domain.enumeration.Admission;
import mfpai.gouv.sn.domain.enumeration.CFP;
import mfpai.gouv.sn.domain.enumeration.DiplomeObtenu;
import mfpai.gouv.sn.domain.enumeration.DiplomeRequis;
import mfpai.gouv.sn.domain.enumeration.LYCEE;
import mfpai.gouv.sn.domain.enumeration.NiveauEtude;
import mfpai.gouv.sn.domain.enumeration.NomFiliere;
import mfpai.gouv.sn.domain.enumeration.NomSerie;
import mfpai.gouv.sn.repository.FormationContinueRepository;
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
 * Integration tests for the {@link FormationContinueResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FormationContinueResourceIT {

    private static final String DEFAULT_NOM_FORMATION_C = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FORMATION_C = "BBBBBBBBBB";

    private static final String DEFAULT_DUREE = "AAAAAAAAAA";
    private static final String UPDATED_DUREE = "BBBBBBBBBB";

    private static final Admission DEFAULT_ADMISSION = Admission.CONCOURS;
    private static final Admission UPDATED_ADMISSION = Admission.PC;

    private static final DiplomeRequis DEFAULT_DIPLOME_REQUIS = DiplomeRequis.ATTESTATION;
    private static final DiplomeRequis UPDATED_DIPLOME_REQUIS = DiplomeRequis.CAP;

    private static final NiveauEtude DEFAULT_NIVEAU_ETUDE = NiveauEtude.Cinquieme;
    private static final NiveauEtude UPDATED_NIVEAU_ETUDE = NiveauEtude.Quatrieme;

    private static final NomFiliere DEFAULT_FILIERE = NomFiliere.AGRI_ELEVAGE;
    private static final NomFiliere UPDATED_FILIERE = NomFiliere.AGRICULTURE;

    private static final NomSerie DEFAULT_SERIE = NomSerie.STEG;
    private static final NomSerie UPDATED_SERIE = NomSerie.STIDD_M;

    private static final CFP DEFAULT_CFP = CFP.CEDT_G15;
    private static final CFP UPDATED_CFP = CFP.CFP_OUAKAM;

    private static final LYCEE DEFAULT_LYCEE = LYCEE.LTID_DAKAR;
    private static final LYCEE UPDATED_LYCEE = LYCEE.LTCD_DAKAR;

    private static final byte[] DEFAULT_FICHE_FORMATION = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FICHE_FORMATION = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FICHE_FORMATION_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FICHE_FORMATION_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_LIBELLE_PC = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_PC = "BBBBBBBBBB";

    private static final Double DEFAULT_MONTANT_PRISE_EN_CHARGE = 1D;
    private static final Double UPDATED_MONTANT_PRISE_EN_CHARGE = 2D;

    private static final Double DEFAULT_COUT_FORMATION = 1D;
    private static final Double UPDATED_COUT_FORMATION = 2D;

    private static final String DEFAULT_DETAIL_PC = "AAAAAAAAAA";
    private static final String UPDATED_DETAIL_PC = "BBBBBBBBBB";

    private static final DiplomeObtenu DEFAULT_NOM_DIPLOME = DiplomeObtenu.CPS;
    private static final DiplomeObtenu UPDATED_NOM_DIPLOME = DiplomeObtenu.CAP;

    private static final String DEFAULT_AUTRE_DIPLOME = "AAAAAAAAAA";
    private static final String UPDATED_AUTRE_DIPLOME = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_DEBOUCHE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_DEBOUCHE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/formation-continues";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FormationContinueRepository formationContinueRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFormationContinueMockMvc;

    private FormationContinue formationContinue;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FormationContinue createEntity(EntityManager em) {
        FormationContinue formationContinue = new FormationContinue()
            .nomFormationC(DEFAULT_NOM_FORMATION_C)
            .duree(DEFAULT_DUREE)
            .admission(DEFAULT_ADMISSION)
            .diplomeRequis(DEFAULT_DIPLOME_REQUIS)
            .niveauEtude(DEFAULT_NIVEAU_ETUDE)
            .filiere(DEFAULT_FILIERE)
            .serie(DEFAULT_SERIE)
            .cfp(DEFAULT_CFP)
            .lycee(DEFAULT_LYCEE)
            .ficheFormation(DEFAULT_FICHE_FORMATION)
            .ficheFormationContentType(DEFAULT_FICHE_FORMATION_CONTENT_TYPE)
            .libellePC(DEFAULT_LIBELLE_PC)
            .montantPriseEnCharge(DEFAULT_MONTANT_PRISE_EN_CHARGE)
            .coutFormation(DEFAULT_COUT_FORMATION)
            .detailPC(DEFAULT_DETAIL_PC)
            .nomDiplome(DEFAULT_NOM_DIPLOME)
            .autreDiplome(DEFAULT_AUTRE_DIPLOME)
            .nomDebouche(DEFAULT_NOM_DEBOUCHE);
        return formationContinue;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FormationContinue createUpdatedEntity(EntityManager em) {
        FormationContinue formationContinue = new FormationContinue()
            .nomFormationC(UPDATED_NOM_FORMATION_C)
            .duree(UPDATED_DUREE)
            .admission(UPDATED_ADMISSION)
            .diplomeRequis(UPDATED_DIPLOME_REQUIS)
            .niveauEtude(UPDATED_NIVEAU_ETUDE)
            .filiere(UPDATED_FILIERE)
            .serie(UPDATED_SERIE)
            .cfp(UPDATED_CFP)
            .lycee(UPDATED_LYCEE)
            .ficheFormation(UPDATED_FICHE_FORMATION)
            .ficheFormationContentType(UPDATED_FICHE_FORMATION_CONTENT_TYPE)
            .libellePC(UPDATED_LIBELLE_PC)
            .montantPriseEnCharge(UPDATED_MONTANT_PRISE_EN_CHARGE)
            .coutFormation(UPDATED_COUT_FORMATION)
            .detailPC(UPDATED_DETAIL_PC)
            .nomDiplome(UPDATED_NOM_DIPLOME)
            .autreDiplome(UPDATED_AUTRE_DIPLOME)
            .nomDebouche(UPDATED_NOM_DEBOUCHE);
        return formationContinue;
    }

    @BeforeEach
    public void initTest() {
        formationContinue = createEntity(em);
    }

    @Test
    @Transactional
    void createFormationContinue() throws Exception {
        int databaseSizeBeforeCreate = formationContinueRepository.findAll().size();
        // Create the FormationContinue
        restFormationContinueMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formationContinue))
            )
            .andExpect(status().isCreated());

        // Validate the FormationContinue in the database
        List<FormationContinue> formationContinueList = formationContinueRepository.findAll();
        assertThat(formationContinueList).hasSize(databaseSizeBeforeCreate + 1);
        FormationContinue testFormationContinue = formationContinueList.get(formationContinueList.size() - 1);
        assertThat(testFormationContinue.getNomFormationC()).isEqualTo(DEFAULT_NOM_FORMATION_C);
        assertThat(testFormationContinue.getDuree()).isEqualTo(DEFAULT_DUREE);
        assertThat(testFormationContinue.getAdmission()).isEqualTo(DEFAULT_ADMISSION);
        assertThat(testFormationContinue.getDiplomeRequis()).isEqualTo(DEFAULT_DIPLOME_REQUIS);
        assertThat(testFormationContinue.getNiveauEtude()).isEqualTo(DEFAULT_NIVEAU_ETUDE);
        assertThat(testFormationContinue.getFiliere()).isEqualTo(DEFAULT_FILIERE);
        assertThat(testFormationContinue.getSerie()).isEqualTo(DEFAULT_SERIE);
        assertThat(testFormationContinue.getCfp()).isEqualTo(DEFAULT_CFP);
        assertThat(testFormationContinue.getLycee()).isEqualTo(DEFAULT_LYCEE);
        assertThat(testFormationContinue.getFicheFormation()).isEqualTo(DEFAULT_FICHE_FORMATION);
        assertThat(testFormationContinue.getFicheFormationContentType()).isEqualTo(DEFAULT_FICHE_FORMATION_CONTENT_TYPE);
        assertThat(testFormationContinue.getLibellePC()).isEqualTo(DEFAULT_LIBELLE_PC);
        assertThat(testFormationContinue.getMontantPriseEnCharge()).isEqualTo(DEFAULT_MONTANT_PRISE_EN_CHARGE);
        assertThat(testFormationContinue.getCoutFormation()).isEqualTo(DEFAULT_COUT_FORMATION);
        assertThat(testFormationContinue.getDetailPC()).isEqualTo(DEFAULT_DETAIL_PC);
        assertThat(testFormationContinue.getNomDiplome()).isEqualTo(DEFAULT_NOM_DIPLOME);
        assertThat(testFormationContinue.getAutreDiplome()).isEqualTo(DEFAULT_AUTRE_DIPLOME);
        assertThat(testFormationContinue.getNomDebouche()).isEqualTo(DEFAULT_NOM_DEBOUCHE);
    }

    @Test
    @Transactional
    void createFormationContinueWithExistingId() throws Exception {
        // Create the FormationContinue with an existing ID
        formationContinue.setId(1L);

        int databaseSizeBeforeCreate = formationContinueRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormationContinueMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formationContinue))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormationContinue in the database
        List<FormationContinue> formationContinueList = formationContinueRepository.findAll();
        assertThat(formationContinueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFormationContinues() throws Exception {
        // Initialize the database
        formationContinueRepository.saveAndFlush(formationContinue);

        // Get all the formationContinueList
        restFormationContinueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formationContinue.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFormationC").value(hasItem(DEFAULT_NOM_FORMATION_C)))
            .andExpect(jsonPath("$.[*].duree").value(hasItem(DEFAULT_DUREE)))
            .andExpect(jsonPath("$.[*].admission").value(hasItem(DEFAULT_ADMISSION.toString())))
            .andExpect(jsonPath("$.[*].diplomeRequis").value(hasItem(DEFAULT_DIPLOME_REQUIS.toString())))
            .andExpect(jsonPath("$.[*].niveauEtude").value(hasItem(DEFAULT_NIVEAU_ETUDE.toString())))
            .andExpect(jsonPath("$.[*].filiere").value(hasItem(DEFAULT_FILIERE.toString())))
            .andExpect(jsonPath("$.[*].serie").value(hasItem(DEFAULT_SERIE.toString())))
            .andExpect(jsonPath("$.[*].cfp").value(hasItem(DEFAULT_CFP.toString())))
            .andExpect(jsonPath("$.[*].lycee").value(hasItem(DEFAULT_LYCEE.toString())))
            .andExpect(jsonPath("$.[*].ficheFormationContentType").value(hasItem(DEFAULT_FICHE_FORMATION_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].ficheFormation").value(hasItem(Base64Utils.encodeToString(DEFAULT_FICHE_FORMATION))))
            .andExpect(jsonPath("$.[*].libellePC").value(hasItem(DEFAULT_LIBELLE_PC)))
            .andExpect(jsonPath("$.[*].montantPriseEnCharge").value(hasItem(DEFAULT_MONTANT_PRISE_EN_CHARGE.doubleValue())))
            .andExpect(jsonPath("$.[*].coutFormation").value(hasItem(DEFAULT_COUT_FORMATION.doubleValue())))
            .andExpect(jsonPath("$.[*].detailPC").value(hasItem(DEFAULT_DETAIL_PC.toString())))
            .andExpect(jsonPath("$.[*].nomDiplome").value(hasItem(DEFAULT_NOM_DIPLOME.toString())))
            .andExpect(jsonPath("$.[*].autreDiplome").value(hasItem(DEFAULT_AUTRE_DIPLOME)))
            .andExpect(jsonPath("$.[*].nomDebouche").value(hasItem(DEFAULT_NOM_DEBOUCHE)));
    }

    @Test
    @Transactional
    void getFormationContinue() throws Exception {
        // Initialize the database
        formationContinueRepository.saveAndFlush(formationContinue);

        // Get the formationContinue
        restFormationContinueMockMvc
            .perform(get(ENTITY_API_URL_ID, formationContinue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(formationContinue.getId().intValue()))
            .andExpect(jsonPath("$.nomFormationC").value(DEFAULT_NOM_FORMATION_C))
            .andExpect(jsonPath("$.duree").value(DEFAULT_DUREE))
            .andExpect(jsonPath("$.admission").value(DEFAULT_ADMISSION.toString()))
            .andExpect(jsonPath("$.diplomeRequis").value(DEFAULT_DIPLOME_REQUIS.toString()))
            .andExpect(jsonPath("$.niveauEtude").value(DEFAULT_NIVEAU_ETUDE.toString()))
            .andExpect(jsonPath("$.filiere").value(DEFAULT_FILIERE.toString()))
            .andExpect(jsonPath("$.serie").value(DEFAULT_SERIE.toString()))
            .andExpect(jsonPath("$.cfp").value(DEFAULT_CFP.toString()))
            .andExpect(jsonPath("$.lycee").value(DEFAULT_LYCEE.toString()))
            .andExpect(jsonPath("$.ficheFormationContentType").value(DEFAULT_FICHE_FORMATION_CONTENT_TYPE))
            .andExpect(jsonPath("$.ficheFormation").value(Base64Utils.encodeToString(DEFAULT_FICHE_FORMATION)))
            .andExpect(jsonPath("$.libellePC").value(DEFAULT_LIBELLE_PC))
            .andExpect(jsonPath("$.montantPriseEnCharge").value(DEFAULT_MONTANT_PRISE_EN_CHARGE.doubleValue()))
            .andExpect(jsonPath("$.coutFormation").value(DEFAULT_COUT_FORMATION.doubleValue()))
            .andExpect(jsonPath("$.detailPC").value(DEFAULT_DETAIL_PC.toString()))
            .andExpect(jsonPath("$.nomDiplome").value(DEFAULT_NOM_DIPLOME.toString()))
            .andExpect(jsonPath("$.autreDiplome").value(DEFAULT_AUTRE_DIPLOME))
            .andExpect(jsonPath("$.nomDebouche").value(DEFAULT_NOM_DEBOUCHE));
    }

    @Test
    @Transactional
    void getNonExistingFormationContinue() throws Exception {
        // Get the formationContinue
        restFormationContinueMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFormationContinue() throws Exception {
        // Initialize the database
        formationContinueRepository.saveAndFlush(formationContinue);

        int databaseSizeBeforeUpdate = formationContinueRepository.findAll().size();

        // Update the formationContinue
        FormationContinue updatedFormationContinue = formationContinueRepository.findById(formationContinue.getId()).get();
        // Disconnect from session so that the updates on updatedFormationContinue are not directly saved in db
        em.detach(updatedFormationContinue);
        updatedFormationContinue
            .nomFormationC(UPDATED_NOM_FORMATION_C)
            .duree(UPDATED_DUREE)
            .admission(UPDATED_ADMISSION)
            .diplomeRequis(UPDATED_DIPLOME_REQUIS)
            .niveauEtude(UPDATED_NIVEAU_ETUDE)
            .filiere(UPDATED_FILIERE)
            .serie(UPDATED_SERIE)
            .cfp(UPDATED_CFP)
            .lycee(UPDATED_LYCEE)
            .ficheFormation(UPDATED_FICHE_FORMATION)
            .ficheFormationContentType(UPDATED_FICHE_FORMATION_CONTENT_TYPE)
            .libellePC(UPDATED_LIBELLE_PC)
            .montantPriseEnCharge(UPDATED_MONTANT_PRISE_EN_CHARGE)
            .coutFormation(UPDATED_COUT_FORMATION)
            .detailPC(UPDATED_DETAIL_PC)
            .nomDiplome(UPDATED_NOM_DIPLOME)
            .autreDiplome(UPDATED_AUTRE_DIPLOME)
            .nomDebouche(UPDATED_NOM_DEBOUCHE);

        restFormationContinueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFormationContinue.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFormationContinue))
            )
            .andExpect(status().isOk());

        // Validate the FormationContinue in the database
        List<FormationContinue> formationContinueList = formationContinueRepository.findAll();
        assertThat(formationContinueList).hasSize(databaseSizeBeforeUpdate);
        FormationContinue testFormationContinue = formationContinueList.get(formationContinueList.size() - 1);
        assertThat(testFormationContinue.getNomFormationC()).isEqualTo(UPDATED_NOM_FORMATION_C);
        assertThat(testFormationContinue.getDuree()).isEqualTo(UPDATED_DUREE);
        assertThat(testFormationContinue.getAdmission()).isEqualTo(UPDATED_ADMISSION);
        assertThat(testFormationContinue.getDiplomeRequis()).isEqualTo(UPDATED_DIPLOME_REQUIS);
        assertThat(testFormationContinue.getNiveauEtude()).isEqualTo(UPDATED_NIVEAU_ETUDE);
        assertThat(testFormationContinue.getFiliere()).isEqualTo(UPDATED_FILIERE);
        assertThat(testFormationContinue.getSerie()).isEqualTo(UPDATED_SERIE);
        assertThat(testFormationContinue.getCfp()).isEqualTo(UPDATED_CFP);
        assertThat(testFormationContinue.getLycee()).isEqualTo(UPDATED_LYCEE);
        assertThat(testFormationContinue.getFicheFormation()).isEqualTo(UPDATED_FICHE_FORMATION);
        assertThat(testFormationContinue.getFicheFormationContentType()).isEqualTo(UPDATED_FICHE_FORMATION_CONTENT_TYPE);
        assertThat(testFormationContinue.getLibellePC()).isEqualTo(UPDATED_LIBELLE_PC);
        assertThat(testFormationContinue.getMontantPriseEnCharge()).isEqualTo(UPDATED_MONTANT_PRISE_EN_CHARGE);
        assertThat(testFormationContinue.getCoutFormation()).isEqualTo(UPDATED_COUT_FORMATION);
        assertThat(testFormationContinue.getDetailPC()).isEqualTo(UPDATED_DETAIL_PC);
        assertThat(testFormationContinue.getNomDiplome()).isEqualTo(UPDATED_NOM_DIPLOME);
        assertThat(testFormationContinue.getAutreDiplome()).isEqualTo(UPDATED_AUTRE_DIPLOME);
        assertThat(testFormationContinue.getNomDebouche()).isEqualTo(UPDATED_NOM_DEBOUCHE);
    }

    @Test
    @Transactional
    void putNonExistingFormationContinue() throws Exception {
        int databaseSizeBeforeUpdate = formationContinueRepository.findAll().size();
        formationContinue.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormationContinueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formationContinue.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationContinue))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormationContinue in the database
        List<FormationContinue> formationContinueList = formationContinueRepository.findAll();
        assertThat(formationContinueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFormationContinue() throws Exception {
        int databaseSizeBeforeUpdate = formationContinueRepository.findAll().size();
        formationContinue.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationContinueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationContinue))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormationContinue in the database
        List<FormationContinue> formationContinueList = formationContinueRepository.findAll();
        assertThat(formationContinueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFormationContinue() throws Exception {
        int databaseSizeBeforeUpdate = formationContinueRepository.findAll().size();
        formationContinue.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationContinueMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formationContinue))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FormationContinue in the database
        List<FormationContinue> formationContinueList = formationContinueRepository.findAll();
        assertThat(formationContinueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFormationContinueWithPatch() throws Exception {
        // Initialize the database
        formationContinueRepository.saveAndFlush(formationContinue);

        int databaseSizeBeforeUpdate = formationContinueRepository.findAll().size();

        // Update the formationContinue using partial update
        FormationContinue partialUpdatedFormationContinue = new FormationContinue();
        partialUpdatedFormationContinue.setId(formationContinue.getId());

        partialUpdatedFormationContinue
            .niveauEtude(UPDATED_NIVEAU_ETUDE)
            .filiere(UPDATED_FILIERE)
            .cfp(UPDATED_CFP)
            .lycee(UPDATED_LYCEE)
            .ficheFormation(UPDATED_FICHE_FORMATION)
            .ficheFormationContentType(UPDATED_FICHE_FORMATION_CONTENT_TYPE)
            .coutFormation(UPDATED_COUT_FORMATION)
            .autreDiplome(UPDATED_AUTRE_DIPLOME);

        restFormationContinueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormationContinue.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormationContinue))
            )
            .andExpect(status().isOk());

        // Validate the FormationContinue in the database
        List<FormationContinue> formationContinueList = formationContinueRepository.findAll();
        assertThat(formationContinueList).hasSize(databaseSizeBeforeUpdate);
        FormationContinue testFormationContinue = formationContinueList.get(formationContinueList.size() - 1);
        assertThat(testFormationContinue.getNomFormationC()).isEqualTo(DEFAULT_NOM_FORMATION_C);
        assertThat(testFormationContinue.getDuree()).isEqualTo(DEFAULT_DUREE);
        assertThat(testFormationContinue.getAdmission()).isEqualTo(DEFAULT_ADMISSION);
        assertThat(testFormationContinue.getDiplomeRequis()).isEqualTo(DEFAULT_DIPLOME_REQUIS);
        assertThat(testFormationContinue.getNiveauEtude()).isEqualTo(UPDATED_NIVEAU_ETUDE);
        assertThat(testFormationContinue.getFiliere()).isEqualTo(UPDATED_FILIERE);
        assertThat(testFormationContinue.getSerie()).isEqualTo(DEFAULT_SERIE);
        assertThat(testFormationContinue.getCfp()).isEqualTo(UPDATED_CFP);
        assertThat(testFormationContinue.getLycee()).isEqualTo(UPDATED_LYCEE);
        assertThat(testFormationContinue.getFicheFormation()).isEqualTo(UPDATED_FICHE_FORMATION);
        assertThat(testFormationContinue.getFicheFormationContentType()).isEqualTo(UPDATED_FICHE_FORMATION_CONTENT_TYPE);
        assertThat(testFormationContinue.getLibellePC()).isEqualTo(DEFAULT_LIBELLE_PC);
        assertThat(testFormationContinue.getMontantPriseEnCharge()).isEqualTo(DEFAULT_MONTANT_PRISE_EN_CHARGE);
        assertThat(testFormationContinue.getCoutFormation()).isEqualTo(UPDATED_COUT_FORMATION);
        assertThat(testFormationContinue.getDetailPC()).isEqualTo(DEFAULT_DETAIL_PC);
        assertThat(testFormationContinue.getNomDiplome()).isEqualTo(DEFAULT_NOM_DIPLOME);
        assertThat(testFormationContinue.getAutreDiplome()).isEqualTo(UPDATED_AUTRE_DIPLOME);
        assertThat(testFormationContinue.getNomDebouche()).isEqualTo(DEFAULT_NOM_DEBOUCHE);
    }

    @Test
    @Transactional
    void fullUpdateFormationContinueWithPatch() throws Exception {
        // Initialize the database
        formationContinueRepository.saveAndFlush(formationContinue);

        int databaseSizeBeforeUpdate = formationContinueRepository.findAll().size();

        // Update the formationContinue using partial update
        FormationContinue partialUpdatedFormationContinue = new FormationContinue();
        partialUpdatedFormationContinue.setId(formationContinue.getId());

        partialUpdatedFormationContinue
            .nomFormationC(UPDATED_NOM_FORMATION_C)
            .duree(UPDATED_DUREE)
            .admission(UPDATED_ADMISSION)
            .diplomeRequis(UPDATED_DIPLOME_REQUIS)
            .niveauEtude(UPDATED_NIVEAU_ETUDE)
            .filiere(UPDATED_FILIERE)
            .serie(UPDATED_SERIE)
            .cfp(UPDATED_CFP)
            .lycee(UPDATED_LYCEE)
            .ficheFormation(UPDATED_FICHE_FORMATION)
            .ficheFormationContentType(UPDATED_FICHE_FORMATION_CONTENT_TYPE)
            .libellePC(UPDATED_LIBELLE_PC)
            .montantPriseEnCharge(UPDATED_MONTANT_PRISE_EN_CHARGE)
            .coutFormation(UPDATED_COUT_FORMATION)
            .detailPC(UPDATED_DETAIL_PC)
            .nomDiplome(UPDATED_NOM_DIPLOME)
            .autreDiplome(UPDATED_AUTRE_DIPLOME)
            .nomDebouche(UPDATED_NOM_DEBOUCHE);

        restFormationContinueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormationContinue.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormationContinue))
            )
            .andExpect(status().isOk());

        // Validate the FormationContinue in the database
        List<FormationContinue> formationContinueList = formationContinueRepository.findAll();
        assertThat(formationContinueList).hasSize(databaseSizeBeforeUpdate);
        FormationContinue testFormationContinue = formationContinueList.get(formationContinueList.size() - 1);
        assertThat(testFormationContinue.getNomFormationC()).isEqualTo(UPDATED_NOM_FORMATION_C);
        assertThat(testFormationContinue.getDuree()).isEqualTo(UPDATED_DUREE);
        assertThat(testFormationContinue.getAdmission()).isEqualTo(UPDATED_ADMISSION);
        assertThat(testFormationContinue.getDiplomeRequis()).isEqualTo(UPDATED_DIPLOME_REQUIS);
        assertThat(testFormationContinue.getNiveauEtude()).isEqualTo(UPDATED_NIVEAU_ETUDE);
        assertThat(testFormationContinue.getFiliere()).isEqualTo(UPDATED_FILIERE);
        assertThat(testFormationContinue.getSerie()).isEqualTo(UPDATED_SERIE);
        assertThat(testFormationContinue.getCfp()).isEqualTo(UPDATED_CFP);
        assertThat(testFormationContinue.getLycee()).isEqualTo(UPDATED_LYCEE);
        assertThat(testFormationContinue.getFicheFormation()).isEqualTo(UPDATED_FICHE_FORMATION);
        assertThat(testFormationContinue.getFicheFormationContentType()).isEqualTo(UPDATED_FICHE_FORMATION_CONTENT_TYPE);
        assertThat(testFormationContinue.getLibellePC()).isEqualTo(UPDATED_LIBELLE_PC);
        assertThat(testFormationContinue.getMontantPriseEnCharge()).isEqualTo(UPDATED_MONTANT_PRISE_EN_CHARGE);
        assertThat(testFormationContinue.getCoutFormation()).isEqualTo(UPDATED_COUT_FORMATION);
        assertThat(testFormationContinue.getDetailPC()).isEqualTo(UPDATED_DETAIL_PC);
        assertThat(testFormationContinue.getNomDiplome()).isEqualTo(UPDATED_NOM_DIPLOME);
        assertThat(testFormationContinue.getAutreDiplome()).isEqualTo(UPDATED_AUTRE_DIPLOME);
        assertThat(testFormationContinue.getNomDebouche()).isEqualTo(UPDATED_NOM_DEBOUCHE);
    }

    @Test
    @Transactional
    void patchNonExistingFormationContinue() throws Exception {
        int databaseSizeBeforeUpdate = formationContinueRepository.findAll().size();
        formationContinue.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormationContinueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, formationContinue.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formationContinue))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormationContinue in the database
        List<FormationContinue> formationContinueList = formationContinueRepository.findAll();
        assertThat(formationContinueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFormationContinue() throws Exception {
        int databaseSizeBeforeUpdate = formationContinueRepository.findAll().size();
        formationContinue.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationContinueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formationContinue))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormationContinue in the database
        List<FormationContinue> formationContinueList = formationContinueRepository.findAll();
        assertThat(formationContinueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFormationContinue() throws Exception {
        int databaseSizeBeforeUpdate = formationContinueRepository.findAll().size();
        formationContinue.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationContinueMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formationContinue))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FormationContinue in the database
        List<FormationContinue> formationContinueList = formationContinueRepository.findAll();
        assertThat(formationContinueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFormationContinue() throws Exception {
        // Initialize the database
        formationContinueRepository.saveAndFlush(formationContinue);

        int databaseSizeBeforeDelete = formationContinueRepository.findAll().size();

        // Delete the formationContinue
        restFormationContinueMockMvc
            .perform(delete(ENTITY_API_URL_ID, formationContinue.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FormationContinue> formationContinueList = formationContinueRepository.findAll();
        assertThat(formationContinueList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
