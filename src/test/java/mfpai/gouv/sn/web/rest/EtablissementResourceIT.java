package mfpai.gouv.sn.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import mfpai.gouv.sn.IntegrationTest;
import mfpai.gouv.sn.domain.Etablissement;
import mfpai.gouv.sn.domain.Formation;
import mfpai.gouv.sn.domain.enumeration.CFP;
import mfpai.gouv.sn.domain.enumeration.LYCEE;
import mfpai.gouv.sn.domain.enumeration.NomDepartement;
import mfpai.gouv.sn.domain.enumeration.NomEtablissement;
import mfpai.gouv.sn.domain.enumeration.NomFiliere;
import mfpai.gouv.sn.domain.enumeration.NomRegion;
import mfpai.gouv.sn.domain.enumeration.NomSerie;
import mfpai.gouv.sn.domain.enumeration.StatutEtab;
import mfpai.gouv.sn.domain.enumeration.TypeEtablissement;
import mfpai.gouv.sn.repository.EtablissementRepository;
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
 * Integration tests for the {@link EtablissementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EtablissementResourceIT {

    private static final NomEtablissement DEFAULT_NOM_ETABLISSEMENT = NomEtablissement.CEDT_G15;
    private static final NomEtablissement UPDATED_NOM_ETABLISSEMENT = NomEtablissement.CFP_OUAKAM;

    private static final byte[] DEFAULT_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PHOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PHOTO_CONTENT_TYPE = "image/png";

    private static final NomRegion DEFAULT_REGION = NomRegion.DAKAR;
    private static final NomRegion UPDATED_REGION = NomRegion.DIOURBEL;

    private static final NomDepartement DEFAULT_DEPARTEMENT = NomDepartement.DAKAR;
    private static final NomDepartement UPDATED_DEPARTEMENT = NomDepartement.GUEDIAWAYE;

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Long DEFAULT_TELEPHONE = 1L;
    private static final Long UPDATED_TELEPHONE = 2L;

    private static final TypeEtablissement DEFAULT_TYPE_ETABLISSEMENT = TypeEtablissement.CFP;
    private static final TypeEtablissement UPDATED_TYPE_ETABLISSEMENT = TypeEtablissement.LYCEE_TECH;

    private static final StatutEtab DEFAULT_STATUT = StatutEtab.PRIVE;
    private static final StatutEtab UPDATED_STATUT = StatutEtab.PUBLIC;

    private static final String DEFAULT_AUTRE_REGION = "AAAAAAAAAA";
    private static final String UPDATED_AUTRE_REGION = "BBBBBBBBBB";

    private static final String DEFAULT_AUTRE_DEPARTEMENT = "AAAAAAAAAA";
    private static final String UPDATED_AUTRE_DEPARTEMENT = "BBBBBBBBBB";

    private static final CFP DEFAULT_CFP = CFP.CEDT_G15;
    private static final CFP UPDATED_CFP = CFP.CFP_OUAKAM;

    private static final LYCEE DEFAULT_LYCEE = LYCEE.LTID_DAKAR;
    private static final LYCEE UPDATED_LYCEE = LYCEE.LTCD_DAKAR;

    private static final NomFiliere DEFAULT_FILIERE = NomFiliere.AGRI_ELEVAGE;
    private static final NomFiliere UPDATED_FILIERE = NomFiliere.AGRICULTURE;

    private static final NomSerie DEFAULT_SERIE = NomSerie.STEG;
    private static final NomSerie UPDATED_SERIE = NomSerie.STIDD_M;

    private static final String DEFAULT_AUTRE_FILIERE = "AAAAAAAAAA";
    private static final String UPDATED_AUTRE_FILIERE = "BBBBBBBBBB";

    private static final String DEFAULT_AUTRE_SERIE = "AAAAAAAAAA";
    private static final String UPDATED_AUTRE_SERIE = "BBBBBBBBBB";

    private static final String DEFAULT_AUTRE_NOM_ETABLISSEMENT = "AAAAAAAAAA";
    private static final String UPDATED_AUTRE_NOM_ETABLISSEMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/etablissements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EtablissementRepository etablissementRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEtablissementMockMvc;

    private Etablissement etablissement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Etablissement createEntity(EntityManager em) {
        Etablissement etablissement = new Etablissement()
            .nomEtablissement(DEFAULT_NOM_ETABLISSEMENT)
            .photo(DEFAULT_PHOTO)
            .photoContentType(DEFAULT_PHOTO_CONTENT_TYPE)
            .region(DEFAULT_REGION)
            .departement(DEFAULT_DEPARTEMENT)
            .email(DEFAULT_EMAIL)
            .telephone(DEFAULT_TELEPHONE)
            .typeEtablissement(DEFAULT_TYPE_ETABLISSEMENT)
            .statut(DEFAULT_STATUT)
            .autreRegion(DEFAULT_AUTRE_REGION)
            .autreDepartement(DEFAULT_AUTRE_DEPARTEMENT)
            .cfp(DEFAULT_CFP)
            .lycee(DEFAULT_LYCEE)
            .filiere(DEFAULT_FILIERE)
            .serie(DEFAULT_SERIE)
            .autreFiliere(DEFAULT_AUTRE_FILIERE)
            .autreSerie(DEFAULT_AUTRE_SERIE)
            .autreNomEtablissement(DEFAULT_AUTRE_NOM_ETABLISSEMENT);
        // Add required entity
        Formation formation;
        if (TestUtil.findAll(em, Formation.class).isEmpty()) {
            formation = FormationResourceIT.createEntity(em);
            em.persist(formation);
            em.flush();
        } else {
            formation = TestUtil.findAll(em, Formation.class).get(0);
        }
        etablissement.getFormations().add(formation);
        return etablissement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Etablissement createUpdatedEntity(EntityManager em) {
        Etablissement etablissement = new Etablissement()
            .nomEtablissement(UPDATED_NOM_ETABLISSEMENT)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .region(UPDATED_REGION)
            .departement(UPDATED_DEPARTEMENT)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .typeEtablissement(UPDATED_TYPE_ETABLISSEMENT)
            .statut(UPDATED_STATUT)
            .autreRegion(UPDATED_AUTRE_REGION)
            .autreDepartement(UPDATED_AUTRE_DEPARTEMENT)
            .cfp(UPDATED_CFP)
            .lycee(UPDATED_LYCEE)
            .filiere(UPDATED_FILIERE)
            .serie(UPDATED_SERIE)
            .autreFiliere(UPDATED_AUTRE_FILIERE)
            .autreSerie(UPDATED_AUTRE_SERIE)
            .autreNomEtablissement(UPDATED_AUTRE_NOM_ETABLISSEMENT);
        // Add required entity
        Formation formation;
        if (TestUtil.findAll(em, Formation.class).isEmpty()) {
            formation = FormationResourceIT.createUpdatedEntity(em);
            em.persist(formation);
            em.flush();
        } else {
            formation = TestUtil.findAll(em, Formation.class).get(0);
        }
        etablissement.getFormations().add(formation);
        return etablissement;
    }

    @BeforeEach
    public void initTest() {
        etablissement = createEntity(em);
    }

    @Test
    @Transactional
    void createEtablissement() throws Exception {
        int databaseSizeBeforeCreate = etablissementRepository.findAll().size();
        // Create the Etablissement
        restEtablissementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(etablissement)))
            .andExpect(status().isCreated());

        // Validate the Etablissement in the database
        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeCreate + 1);
        Etablissement testEtablissement = etablissementList.get(etablissementList.size() - 1);
        assertThat(testEtablissement.getNomEtablissement()).isEqualTo(DEFAULT_NOM_ETABLISSEMENT);
        assertThat(testEtablissement.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testEtablissement.getPhotoContentType()).isEqualTo(DEFAULT_PHOTO_CONTENT_TYPE);
        assertThat(testEtablissement.getRegion()).isEqualTo(DEFAULT_REGION);
        assertThat(testEtablissement.getDepartement()).isEqualTo(DEFAULT_DEPARTEMENT);
        assertThat(testEtablissement.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testEtablissement.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testEtablissement.getTypeEtablissement()).isEqualTo(DEFAULT_TYPE_ETABLISSEMENT);
        assertThat(testEtablissement.getStatut()).isEqualTo(DEFAULT_STATUT);
        assertThat(testEtablissement.getAutreRegion()).isEqualTo(DEFAULT_AUTRE_REGION);
        assertThat(testEtablissement.getAutreDepartement()).isEqualTo(DEFAULT_AUTRE_DEPARTEMENT);
        assertThat(testEtablissement.getCfp()).isEqualTo(DEFAULT_CFP);
        assertThat(testEtablissement.getLycee()).isEqualTo(DEFAULT_LYCEE);
        assertThat(testEtablissement.getFiliere()).isEqualTo(DEFAULT_FILIERE);
        assertThat(testEtablissement.getSerie()).isEqualTo(DEFAULT_SERIE);
        assertThat(testEtablissement.getAutreFiliere()).isEqualTo(DEFAULT_AUTRE_FILIERE);
        assertThat(testEtablissement.getAutreSerie()).isEqualTo(DEFAULT_AUTRE_SERIE);
        assertThat(testEtablissement.getAutreNomEtablissement()).isEqualTo(DEFAULT_AUTRE_NOM_ETABLISSEMENT);
    }

    @Test
    @Transactional
    void createEtablissementWithExistingId() throws Exception {
        // Create the Etablissement with an existing ID
        etablissement.setId(1L);

        int databaseSizeBeforeCreate = etablissementRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEtablissementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(etablissement)))
            .andExpect(status().isBadRequest());

        // Validate the Etablissement in the database
        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRegionIsRequired() throws Exception {
        int databaseSizeBeforeTest = etablissementRepository.findAll().size();
        // set the field null
        etablissement.setRegion(null);

        // Create the Etablissement, which fails.

        restEtablissementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(etablissement)))
            .andExpect(status().isBadRequest());

        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDepartementIsRequired() throws Exception {
        int databaseSizeBeforeTest = etablissementRepository.findAll().size();
        // set the field null
        etablissement.setDepartement(null);

        // Create the Etablissement, which fails.

        restEtablissementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(etablissement)))
            .andExpect(status().isBadRequest());

        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatutIsRequired() throws Exception {
        int databaseSizeBeforeTest = etablissementRepository.findAll().size();
        // set the field null
        etablissement.setStatut(null);

        // Create the Etablissement, which fails.

        restEtablissementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(etablissement)))
            .andExpect(status().isBadRequest());

        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEtablissements() throws Exception {
        // Initialize the database
        etablissementRepository.saveAndFlush(etablissement);

        // Get all the etablissementList
        restEtablissementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(etablissement.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomEtablissement").value(hasItem(DEFAULT_NOM_ETABLISSEMENT.toString())))
            .andExpect(jsonPath("$.[*].photoContentType").value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO))))
            .andExpect(jsonPath("$.[*].region").value(hasItem(DEFAULT_REGION.toString())))
            .andExpect(jsonPath("$.[*].departement").value(hasItem(DEFAULT_DEPARTEMENT.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE.intValue())))
            .andExpect(jsonPath("$.[*].typeEtablissement").value(hasItem(DEFAULT_TYPE_ETABLISSEMENT.toString())))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())))
            .andExpect(jsonPath("$.[*].autreRegion").value(hasItem(DEFAULT_AUTRE_REGION)))
            .andExpect(jsonPath("$.[*].autreDepartement").value(hasItem(DEFAULT_AUTRE_DEPARTEMENT)))
            .andExpect(jsonPath("$.[*].cfp").value(hasItem(DEFAULT_CFP.toString())))
            .andExpect(jsonPath("$.[*].lycee").value(hasItem(DEFAULT_LYCEE.toString())))
            .andExpect(jsonPath("$.[*].filiere").value(hasItem(DEFAULT_FILIERE.toString())))
            .andExpect(jsonPath("$.[*].serie").value(hasItem(DEFAULT_SERIE.toString())))
            .andExpect(jsonPath("$.[*].autreFiliere").value(hasItem(DEFAULT_AUTRE_FILIERE)))
            .andExpect(jsonPath("$.[*].autreSerie").value(hasItem(DEFAULT_AUTRE_SERIE)))
            .andExpect(jsonPath("$.[*].autreNomEtablissement").value(hasItem(DEFAULT_AUTRE_NOM_ETABLISSEMENT)));
    }

    @Test
    @Transactional
    void getEtablissement() throws Exception {
        // Initialize the database
        etablissementRepository.saveAndFlush(etablissement);

        // Get the etablissement
        restEtablissementMockMvc
            .perform(get(ENTITY_API_URL_ID, etablissement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(etablissement.getId().intValue()))
            .andExpect(jsonPath("$.nomEtablissement").value(DEFAULT_NOM_ETABLISSEMENT.toString()))
            .andExpect(jsonPath("$.photoContentType").value(DEFAULT_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.photo").value(Base64Utils.encodeToString(DEFAULT_PHOTO)))
            .andExpect(jsonPath("$.region").value(DEFAULT_REGION.toString()))
            .andExpect(jsonPath("$.departement").value(DEFAULT_DEPARTEMENT.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE.intValue()))
            .andExpect(jsonPath("$.typeEtablissement").value(DEFAULT_TYPE_ETABLISSEMENT.toString()))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT.toString()))
            .andExpect(jsonPath("$.autreRegion").value(DEFAULT_AUTRE_REGION))
            .andExpect(jsonPath("$.autreDepartement").value(DEFAULT_AUTRE_DEPARTEMENT))
            .andExpect(jsonPath("$.cfp").value(DEFAULT_CFP.toString()))
            .andExpect(jsonPath("$.lycee").value(DEFAULT_LYCEE.toString()))
            .andExpect(jsonPath("$.filiere").value(DEFAULT_FILIERE.toString()))
            .andExpect(jsonPath("$.serie").value(DEFAULT_SERIE.toString()))
            .andExpect(jsonPath("$.autreFiliere").value(DEFAULT_AUTRE_FILIERE))
            .andExpect(jsonPath("$.autreSerie").value(DEFAULT_AUTRE_SERIE))
            .andExpect(jsonPath("$.autreNomEtablissement").value(DEFAULT_AUTRE_NOM_ETABLISSEMENT));
    }

    @Test
    @Transactional
    void getNonExistingEtablissement() throws Exception {
        // Get the etablissement
        restEtablissementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEtablissement() throws Exception {
        // Initialize the database
        etablissementRepository.saveAndFlush(etablissement);

        int databaseSizeBeforeUpdate = etablissementRepository.findAll().size();

        // Update the etablissement
        Etablissement updatedEtablissement = etablissementRepository.findById(etablissement.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEtablissement are not directly saved in db
        em.detach(updatedEtablissement);
        updatedEtablissement
            .nomEtablissement(UPDATED_NOM_ETABLISSEMENT)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .region(UPDATED_REGION)
            .departement(UPDATED_DEPARTEMENT)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .typeEtablissement(UPDATED_TYPE_ETABLISSEMENT)
            .statut(UPDATED_STATUT)
            .autreRegion(UPDATED_AUTRE_REGION)
            .autreDepartement(UPDATED_AUTRE_DEPARTEMENT)
            .cfp(UPDATED_CFP)
            .lycee(UPDATED_LYCEE)
            .filiere(UPDATED_FILIERE)
            .serie(UPDATED_SERIE)
            .autreFiliere(UPDATED_AUTRE_FILIERE)
            .autreSerie(UPDATED_AUTRE_SERIE)
            .autreNomEtablissement(UPDATED_AUTRE_NOM_ETABLISSEMENT);

        restEtablissementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEtablissement.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEtablissement))
            )
            .andExpect(status().isOk());

        // Validate the Etablissement in the database
        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeUpdate);
        Etablissement testEtablissement = etablissementList.get(etablissementList.size() - 1);
        assertThat(testEtablissement.getNomEtablissement()).isEqualTo(UPDATED_NOM_ETABLISSEMENT);
        assertThat(testEtablissement.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testEtablissement.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
        assertThat(testEtablissement.getRegion()).isEqualTo(UPDATED_REGION);
        assertThat(testEtablissement.getDepartement()).isEqualTo(UPDATED_DEPARTEMENT);
        assertThat(testEtablissement.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEtablissement.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testEtablissement.getTypeEtablissement()).isEqualTo(UPDATED_TYPE_ETABLISSEMENT);
        assertThat(testEtablissement.getStatut()).isEqualTo(UPDATED_STATUT);
        assertThat(testEtablissement.getAutreRegion()).isEqualTo(UPDATED_AUTRE_REGION);
        assertThat(testEtablissement.getAutreDepartement()).isEqualTo(UPDATED_AUTRE_DEPARTEMENT);
        assertThat(testEtablissement.getCfp()).isEqualTo(UPDATED_CFP);
        assertThat(testEtablissement.getLycee()).isEqualTo(UPDATED_LYCEE);
        assertThat(testEtablissement.getFiliere()).isEqualTo(UPDATED_FILIERE);
        assertThat(testEtablissement.getSerie()).isEqualTo(UPDATED_SERIE);
        assertThat(testEtablissement.getAutreFiliere()).isEqualTo(UPDATED_AUTRE_FILIERE);
        assertThat(testEtablissement.getAutreSerie()).isEqualTo(UPDATED_AUTRE_SERIE);
        assertThat(testEtablissement.getAutreNomEtablissement()).isEqualTo(UPDATED_AUTRE_NOM_ETABLISSEMENT);
    }

    @Test
    @Transactional
    void putNonExistingEtablissement() throws Exception {
        int databaseSizeBeforeUpdate = etablissementRepository.findAll().size();
        etablissement.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEtablissementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, etablissement.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(etablissement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etablissement in the database
        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEtablissement() throws Exception {
        int databaseSizeBeforeUpdate = etablissementRepository.findAll().size();
        etablissement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtablissementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(etablissement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etablissement in the database
        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEtablissement() throws Exception {
        int databaseSizeBeforeUpdate = etablissementRepository.findAll().size();
        etablissement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtablissementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(etablissement)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Etablissement in the database
        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEtablissementWithPatch() throws Exception {
        // Initialize the database
        etablissementRepository.saveAndFlush(etablissement);

        int databaseSizeBeforeUpdate = etablissementRepository.findAll().size();

        // Update the etablissement using partial update
        Etablissement partialUpdatedEtablissement = new Etablissement();
        partialUpdatedEtablissement.setId(etablissement.getId());

        partialUpdatedEtablissement
            .nomEtablissement(UPDATED_NOM_ETABLISSEMENT)
            .departement(UPDATED_DEPARTEMENT)
            .email(UPDATED_EMAIL)
            .typeEtablissement(UPDATED_TYPE_ETABLISSEMENT)
            .autreRegion(UPDATED_AUTRE_REGION)
            .autreDepartement(UPDATED_AUTRE_DEPARTEMENT)
            .cfp(UPDATED_CFP)
            .filiere(UPDATED_FILIERE)
            .autreFiliere(UPDATED_AUTRE_FILIERE)
            .autreSerie(UPDATED_AUTRE_SERIE);

        restEtablissementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEtablissement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEtablissement))
            )
            .andExpect(status().isOk());

        // Validate the Etablissement in the database
        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeUpdate);
        Etablissement testEtablissement = etablissementList.get(etablissementList.size() - 1);
        assertThat(testEtablissement.getNomEtablissement()).isEqualTo(UPDATED_NOM_ETABLISSEMENT);
        assertThat(testEtablissement.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testEtablissement.getPhotoContentType()).isEqualTo(DEFAULT_PHOTO_CONTENT_TYPE);
        assertThat(testEtablissement.getRegion()).isEqualTo(DEFAULT_REGION);
        assertThat(testEtablissement.getDepartement()).isEqualTo(UPDATED_DEPARTEMENT);
        assertThat(testEtablissement.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEtablissement.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testEtablissement.getTypeEtablissement()).isEqualTo(UPDATED_TYPE_ETABLISSEMENT);
        assertThat(testEtablissement.getStatut()).isEqualTo(DEFAULT_STATUT);
        assertThat(testEtablissement.getAutreRegion()).isEqualTo(UPDATED_AUTRE_REGION);
        assertThat(testEtablissement.getAutreDepartement()).isEqualTo(UPDATED_AUTRE_DEPARTEMENT);
        assertThat(testEtablissement.getCfp()).isEqualTo(UPDATED_CFP);
        assertThat(testEtablissement.getLycee()).isEqualTo(DEFAULT_LYCEE);
        assertThat(testEtablissement.getFiliere()).isEqualTo(UPDATED_FILIERE);
        assertThat(testEtablissement.getSerie()).isEqualTo(DEFAULT_SERIE);
        assertThat(testEtablissement.getAutreFiliere()).isEqualTo(UPDATED_AUTRE_FILIERE);
        assertThat(testEtablissement.getAutreSerie()).isEqualTo(UPDATED_AUTRE_SERIE);
        assertThat(testEtablissement.getAutreNomEtablissement()).isEqualTo(DEFAULT_AUTRE_NOM_ETABLISSEMENT);
    }

    @Test
    @Transactional
    void fullUpdateEtablissementWithPatch() throws Exception {
        // Initialize the database
        etablissementRepository.saveAndFlush(etablissement);

        int databaseSizeBeforeUpdate = etablissementRepository.findAll().size();

        // Update the etablissement using partial update
        Etablissement partialUpdatedEtablissement = new Etablissement();
        partialUpdatedEtablissement.setId(etablissement.getId());

        partialUpdatedEtablissement
            .nomEtablissement(UPDATED_NOM_ETABLISSEMENT)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .region(UPDATED_REGION)
            .departement(UPDATED_DEPARTEMENT)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .typeEtablissement(UPDATED_TYPE_ETABLISSEMENT)
            .statut(UPDATED_STATUT)
            .autreRegion(UPDATED_AUTRE_REGION)
            .autreDepartement(UPDATED_AUTRE_DEPARTEMENT)
            .cfp(UPDATED_CFP)
            .lycee(UPDATED_LYCEE)
            .filiere(UPDATED_FILIERE)
            .serie(UPDATED_SERIE)
            .autreFiliere(UPDATED_AUTRE_FILIERE)
            .autreSerie(UPDATED_AUTRE_SERIE)
            .autreNomEtablissement(UPDATED_AUTRE_NOM_ETABLISSEMENT);

        restEtablissementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEtablissement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEtablissement))
            )
            .andExpect(status().isOk());

        // Validate the Etablissement in the database
        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeUpdate);
        Etablissement testEtablissement = etablissementList.get(etablissementList.size() - 1);
        assertThat(testEtablissement.getNomEtablissement()).isEqualTo(UPDATED_NOM_ETABLISSEMENT);
        assertThat(testEtablissement.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testEtablissement.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
        assertThat(testEtablissement.getRegion()).isEqualTo(UPDATED_REGION);
        assertThat(testEtablissement.getDepartement()).isEqualTo(UPDATED_DEPARTEMENT);
        assertThat(testEtablissement.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEtablissement.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testEtablissement.getTypeEtablissement()).isEqualTo(UPDATED_TYPE_ETABLISSEMENT);
        assertThat(testEtablissement.getStatut()).isEqualTo(UPDATED_STATUT);
        assertThat(testEtablissement.getAutreRegion()).isEqualTo(UPDATED_AUTRE_REGION);
        assertThat(testEtablissement.getAutreDepartement()).isEqualTo(UPDATED_AUTRE_DEPARTEMENT);
        assertThat(testEtablissement.getCfp()).isEqualTo(UPDATED_CFP);
        assertThat(testEtablissement.getLycee()).isEqualTo(UPDATED_LYCEE);
        assertThat(testEtablissement.getFiliere()).isEqualTo(UPDATED_FILIERE);
        assertThat(testEtablissement.getSerie()).isEqualTo(UPDATED_SERIE);
        assertThat(testEtablissement.getAutreFiliere()).isEqualTo(UPDATED_AUTRE_FILIERE);
        assertThat(testEtablissement.getAutreSerie()).isEqualTo(UPDATED_AUTRE_SERIE);
        assertThat(testEtablissement.getAutreNomEtablissement()).isEqualTo(UPDATED_AUTRE_NOM_ETABLISSEMENT);
    }

    @Test
    @Transactional
    void patchNonExistingEtablissement() throws Exception {
        int databaseSizeBeforeUpdate = etablissementRepository.findAll().size();
        etablissement.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEtablissementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, etablissement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(etablissement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etablissement in the database
        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEtablissement() throws Exception {
        int databaseSizeBeforeUpdate = etablissementRepository.findAll().size();
        etablissement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtablissementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(etablissement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etablissement in the database
        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEtablissement() throws Exception {
        int databaseSizeBeforeUpdate = etablissementRepository.findAll().size();
        etablissement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtablissementMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(etablissement))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Etablissement in the database
        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEtablissement() throws Exception {
        // Initialize the database
        etablissementRepository.saveAndFlush(etablissement);

        int databaseSizeBeforeDelete = etablissementRepository.findAll().size();

        // Delete the etablissement
        restEtablissementMockMvc
            .perform(delete(ENTITY_API_URL_ID, etablissement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
