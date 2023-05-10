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
import mfpai.gouv.sn.domain.Diplome;
import mfpai.gouv.sn.domain.enumeration.Mention;
import mfpai.gouv.sn.domain.enumeration.NiveauEtude;
import mfpai.gouv.sn.domain.enumeration.NomFiliere;
import mfpai.gouv.sn.repository.DiplomeRepository;
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
 * Integration tests for the {@link DiplomeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DiplomeResourceIT {

    private static final String DEFAULT_INTITULE = "AAAAAAAAAA";
    private static final String UPDATED_INTITULE = "BBBBBBBBBB";

    private static final NomFiliere DEFAULT_DOMAINE = NomFiliere.AGRI_ELEVAGE;
    private static final NomFiliere UPDATED_DOMAINE = NomFiliere.AGRICULTURE;

    private static final NiveauEtude DEFAULT_NIVEAU = NiveauEtude.CINQUIEME;
    private static final NiveauEtude UPDATED_NIVEAU = NiveauEtude.QUATRIEME;

    private static final Mention DEFAULT_MENTION = Mention.PASSABLE;
    private static final Mention UPDATED_MENTION = Mention.ASSEZ_BIEN;

    private static final String DEFAULT_ANNEE_OBTENTION = "AAAAAAAAAA";
    private static final String UPDATED_ANNEE_OBTENTION = "BBBBBBBBBB";

    private static final String DEFAULT_ETABLISSEMENT = "AAAAAAAAAA";
    private static final String UPDATED_ETABLISSEMENT = "BBBBBBBBBB";

    private static final byte[] DEFAULT_DOCUMENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_DOCUMENT = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_DOCUMENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_DOCUMENT_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/diplomes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DiplomeRepository diplomeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDiplomeMockMvc;

    private Diplome diplome;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Diplome createEntity(EntityManager em) {
        Diplome diplome = new Diplome()
            .intitule(DEFAULT_INTITULE)
            .domaine(DEFAULT_DOMAINE)
            .niveau(DEFAULT_NIVEAU)
            .mention(DEFAULT_MENTION)
            .anneeObtention(DEFAULT_ANNEE_OBTENTION)
            .etablissement(DEFAULT_ETABLISSEMENT)
            .document(DEFAULT_DOCUMENT)
            .documentContentType(DEFAULT_DOCUMENT_CONTENT_TYPE);
        return diplome;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Diplome createUpdatedEntity(EntityManager em) {
        Diplome diplome = new Diplome()
            .intitule(UPDATED_INTITULE)
            .domaine(UPDATED_DOMAINE)
            .niveau(UPDATED_NIVEAU)
            .mention(UPDATED_MENTION)
            .anneeObtention(UPDATED_ANNEE_OBTENTION)
            .etablissement(UPDATED_ETABLISSEMENT)
            .document(UPDATED_DOCUMENT)
            .documentContentType(UPDATED_DOCUMENT_CONTENT_TYPE);
        return diplome;
    }

    @BeforeEach
    public void initTest() {
        diplome = createEntity(em);
    }

    @Test
    @Transactional
    void createDiplome() throws Exception {
        int databaseSizeBeforeCreate = diplomeRepository.findAll().size();
        // Create the Diplome
        restDiplomeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(diplome)))
            .andExpect(status().isCreated());

        // Validate the Diplome in the database
        List<Diplome> diplomeList = diplomeRepository.findAll();
        assertThat(diplomeList).hasSize(databaseSizeBeforeCreate + 1);
        Diplome testDiplome = diplomeList.get(diplomeList.size() - 1);
        assertThat(testDiplome.getIntitule()).isEqualTo(DEFAULT_INTITULE);
        assertThat(testDiplome.getDomaine()).isEqualTo(DEFAULT_DOMAINE);
        assertThat(testDiplome.getNiveau()).isEqualTo(DEFAULT_NIVEAU);
        assertThat(testDiplome.getMention()).isEqualTo(DEFAULT_MENTION);
        assertThat(testDiplome.getAnneeObtention()).isEqualTo(DEFAULT_ANNEE_OBTENTION);
        assertThat(testDiplome.getEtablissement()).isEqualTo(DEFAULT_ETABLISSEMENT);
        assertThat(testDiplome.getDocument()).isEqualTo(DEFAULT_DOCUMENT);
        assertThat(testDiplome.getDocumentContentType()).isEqualTo(DEFAULT_DOCUMENT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createDiplomeWithExistingId() throws Exception {
        // Create the Diplome with an existing ID
        diplome.setId(1L);

        int databaseSizeBeforeCreate = diplomeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDiplomeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(diplome)))
            .andExpect(status().isBadRequest());

        // Validate the Diplome in the database
        List<Diplome> diplomeList = diplomeRepository.findAll();
        assertThat(diplomeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDomaineIsRequired() throws Exception {
        int databaseSizeBeforeTest = diplomeRepository.findAll().size();
        // set the field null
        diplome.setDomaine(null);

        // Create the Diplome, which fails.

        restDiplomeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(diplome)))
            .andExpect(status().isBadRequest());

        List<Diplome> diplomeList = diplomeRepository.findAll();
        assertThat(diplomeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDiplomes() throws Exception {
        // Initialize the database
        diplomeRepository.saveAndFlush(diplome);

        // Get all the diplomeList
        restDiplomeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(diplome.getId().intValue())))
            .andExpect(jsonPath("$.[*].intitule").value(hasItem(DEFAULT_INTITULE)))
            .andExpect(jsonPath("$.[*].domaine").value(hasItem(DEFAULT_DOMAINE.toString())))
            .andExpect(jsonPath("$.[*].niveau").value(hasItem(DEFAULT_NIVEAU.toString())))
            .andExpect(jsonPath("$.[*].mention").value(hasItem(DEFAULT_MENTION.toString())))
            .andExpect(jsonPath("$.[*].anneeObtention").value(hasItem(DEFAULT_ANNEE_OBTENTION)))
            .andExpect(jsonPath("$.[*].etablissement").value(hasItem(DEFAULT_ETABLISSEMENT)))
            .andExpect(jsonPath("$.[*].documentContentType").value(hasItem(DEFAULT_DOCUMENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].document").value(hasItem(Base64Utils.encodeToString(DEFAULT_DOCUMENT))));
    }

    @Test
    @Transactional
    void getDiplome() throws Exception {
        // Initialize the database
        diplomeRepository.saveAndFlush(diplome);

        // Get the diplome
        restDiplomeMockMvc
            .perform(get(ENTITY_API_URL_ID, diplome.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(diplome.getId().intValue()))
            .andExpect(jsonPath("$.intitule").value(DEFAULT_INTITULE))
            .andExpect(jsonPath("$.domaine").value(DEFAULT_DOMAINE.toString()))
            .andExpect(jsonPath("$.niveau").value(DEFAULT_NIVEAU.toString()))
            .andExpect(jsonPath("$.mention").value(DEFAULT_MENTION.toString()))
            .andExpect(jsonPath("$.anneeObtention").value(DEFAULT_ANNEE_OBTENTION))
            .andExpect(jsonPath("$.etablissement").value(DEFAULT_ETABLISSEMENT))
            .andExpect(jsonPath("$.documentContentType").value(DEFAULT_DOCUMENT_CONTENT_TYPE))
            .andExpect(jsonPath("$.document").value(Base64Utils.encodeToString(DEFAULT_DOCUMENT)));
    }

    @Test
    @Transactional
    void getNonExistingDiplome() throws Exception {
        // Get the diplome
        restDiplomeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDiplome() throws Exception {
        // Initialize the database
        diplomeRepository.saveAndFlush(diplome);

        int databaseSizeBeforeUpdate = diplomeRepository.findAll().size();

        // Update the diplome
        Diplome updatedDiplome = diplomeRepository.findById(diplome.getId()).get();
        // Disconnect from session so that the updates on updatedDiplome are not directly saved in db
        em.detach(updatedDiplome);
        updatedDiplome
            .intitule(UPDATED_INTITULE)
            .domaine(UPDATED_DOMAINE)
            .niveau(UPDATED_NIVEAU)
            .mention(UPDATED_MENTION)
            .anneeObtention(UPDATED_ANNEE_OBTENTION)
            .etablissement(UPDATED_ETABLISSEMENT)
            .document(UPDATED_DOCUMENT)
            .documentContentType(UPDATED_DOCUMENT_CONTENT_TYPE);

        restDiplomeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDiplome.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDiplome))
            )
            .andExpect(status().isOk());

        // Validate the Diplome in the database
        List<Diplome> diplomeList = diplomeRepository.findAll();
        assertThat(diplomeList).hasSize(databaseSizeBeforeUpdate);
        Diplome testDiplome = diplomeList.get(diplomeList.size() - 1);
        assertThat(testDiplome.getIntitule()).isEqualTo(UPDATED_INTITULE);
        assertThat(testDiplome.getDomaine()).isEqualTo(UPDATED_DOMAINE);
        assertThat(testDiplome.getNiveau()).isEqualTo(UPDATED_NIVEAU);
        assertThat(testDiplome.getMention()).isEqualTo(UPDATED_MENTION);
        assertThat(testDiplome.getAnneeObtention()).isEqualTo(UPDATED_ANNEE_OBTENTION);
        assertThat(testDiplome.getEtablissement()).isEqualTo(UPDATED_ETABLISSEMENT);
        assertThat(testDiplome.getDocument()).isEqualTo(UPDATED_DOCUMENT);
        assertThat(testDiplome.getDocumentContentType()).isEqualTo(UPDATED_DOCUMENT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingDiplome() throws Exception {
        int databaseSizeBeforeUpdate = diplomeRepository.findAll().size();
        diplome.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDiplomeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, diplome.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(diplome))
            )
            .andExpect(status().isBadRequest());

        // Validate the Diplome in the database
        List<Diplome> diplomeList = diplomeRepository.findAll();
        assertThat(diplomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDiplome() throws Exception {
        int databaseSizeBeforeUpdate = diplomeRepository.findAll().size();
        diplome.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDiplomeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(diplome))
            )
            .andExpect(status().isBadRequest());

        // Validate the Diplome in the database
        List<Diplome> diplomeList = diplomeRepository.findAll();
        assertThat(diplomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDiplome() throws Exception {
        int databaseSizeBeforeUpdate = diplomeRepository.findAll().size();
        diplome.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDiplomeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(diplome)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Diplome in the database
        List<Diplome> diplomeList = diplomeRepository.findAll();
        assertThat(diplomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDiplomeWithPatch() throws Exception {
        // Initialize the database
        diplomeRepository.saveAndFlush(diplome);

        int databaseSizeBeforeUpdate = diplomeRepository.findAll().size();

        // Update the diplome using partial update
        Diplome partialUpdatedDiplome = new Diplome();
        partialUpdatedDiplome.setId(diplome.getId());

        partialUpdatedDiplome
            .niveau(UPDATED_NIVEAU)
            .anneeObtention(UPDATED_ANNEE_OBTENTION)
            .etablissement(UPDATED_ETABLISSEMENT)
            .document(UPDATED_DOCUMENT)
            .documentContentType(UPDATED_DOCUMENT_CONTENT_TYPE);

        restDiplomeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDiplome.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDiplome))
            )
            .andExpect(status().isOk());

        // Validate the Diplome in the database
        List<Diplome> diplomeList = diplomeRepository.findAll();
        assertThat(diplomeList).hasSize(databaseSizeBeforeUpdate);
        Diplome testDiplome = diplomeList.get(diplomeList.size() - 1);
        assertThat(testDiplome.getIntitule()).isEqualTo(DEFAULT_INTITULE);
        assertThat(testDiplome.getDomaine()).isEqualTo(DEFAULT_DOMAINE);
        assertThat(testDiplome.getNiveau()).isEqualTo(UPDATED_NIVEAU);
        assertThat(testDiplome.getMention()).isEqualTo(DEFAULT_MENTION);
        assertThat(testDiplome.getAnneeObtention()).isEqualTo(UPDATED_ANNEE_OBTENTION);
        assertThat(testDiplome.getEtablissement()).isEqualTo(UPDATED_ETABLISSEMENT);
        assertThat(testDiplome.getDocument()).isEqualTo(UPDATED_DOCUMENT);
        assertThat(testDiplome.getDocumentContentType()).isEqualTo(UPDATED_DOCUMENT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateDiplomeWithPatch() throws Exception {
        // Initialize the database
        diplomeRepository.saveAndFlush(diplome);

        int databaseSizeBeforeUpdate = diplomeRepository.findAll().size();

        // Update the diplome using partial update
        Diplome partialUpdatedDiplome = new Diplome();
        partialUpdatedDiplome.setId(diplome.getId());

        partialUpdatedDiplome
            .intitule(UPDATED_INTITULE)
            .domaine(UPDATED_DOMAINE)
            .niveau(UPDATED_NIVEAU)
            .mention(UPDATED_MENTION)
            .anneeObtention(UPDATED_ANNEE_OBTENTION)
            .etablissement(UPDATED_ETABLISSEMENT)
            .document(UPDATED_DOCUMENT)
            .documentContentType(UPDATED_DOCUMENT_CONTENT_TYPE);

        restDiplomeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDiplome.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDiplome))
            )
            .andExpect(status().isOk());

        // Validate the Diplome in the database
        List<Diplome> diplomeList = diplomeRepository.findAll();
        assertThat(diplomeList).hasSize(databaseSizeBeforeUpdate);
        Diplome testDiplome = diplomeList.get(diplomeList.size() - 1);
        assertThat(testDiplome.getIntitule()).isEqualTo(UPDATED_INTITULE);
        assertThat(testDiplome.getDomaine()).isEqualTo(UPDATED_DOMAINE);
        assertThat(testDiplome.getNiveau()).isEqualTo(UPDATED_NIVEAU);
        assertThat(testDiplome.getMention()).isEqualTo(UPDATED_MENTION);
        assertThat(testDiplome.getAnneeObtention()).isEqualTo(UPDATED_ANNEE_OBTENTION);
        assertThat(testDiplome.getEtablissement()).isEqualTo(UPDATED_ETABLISSEMENT);
        assertThat(testDiplome.getDocument()).isEqualTo(UPDATED_DOCUMENT);
        assertThat(testDiplome.getDocumentContentType()).isEqualTo(UPDATED_DOCUMENT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingDiplome() throws Exception {
        int databaseSizeBeforeUpdate = diplomeRepository.findAll().size();
        diplome.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDiplomeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, diplome.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(diplome))
            )
            .andExpect(status().isBadRequest());

        // Validate the Diplome in the database
        List<Diplome> diplomeList = diplomeRepository.findAll();
        assertThat(diplomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDiplome() throws Exception {
        int databaseSizeBeforeUpdate = diplomeRepository.findAll().size();
        diplome.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDiplomeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(diplome))
            )
            .andExpect(status().isBadRequest());

        // Validate the Diplome in the database
        List<Diplome> diplomeList = diplomeRepository.findAll();
        assertThat(diplomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDiplome() throws Exception {
        int databaseSizeBeforeUpdate = diplomeRepository.findAll().size();
        diplome.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDiplomeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(diplome)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Diplome in the database
        List<Diplome> diplomeList = diplomeRepository.findAll();
        assertThat(diplomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDiplome() throws Exception {
        // Initialize the database
        diplomeRepository.saveAndFlush(diplome);

        int databaseSizeBeforeDelete = diplomeRepository.findAll().size();

        // Delete the diplome
        restDiplomeMockMvc
            .perform(delete(ENTITY_API_URL_ID, diplome.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Diplome> diplomeList = diplomeRepository.findAll();
        assertThat(diplomeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
