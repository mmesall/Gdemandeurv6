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
import mfpai.gouv.sn.domain.CandidatureE;
import mfpai.gouv.sn.domain.enumeration.NomFiliere;
import mfpai.gouv.sn.domain.enumeration.Resultat;
import mfpai.gouv.sn.repository.CandidatureERepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CandidatureEResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CandidatureEResourceIT {

    private static final NomFiliere DEFAULT_OFFRE_FORMATION = NomFiliere.AGRI_ELEVAGE;
    private static final NomFiliere UPDATED_OFFRE_FORMATION = NomFiliere.AGRICULTURE;

    private static final LocalDate DEFAULT_DATE_DEBUT_OFFRE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT_OFFRE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN_OFFRE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN_OFFRE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_DEPOT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEPOT = LocalDate.now(ZoneId.systemDefault());

    private static final Resultat DEFAULT_RESULTAT = Resultat.SOUMIS;
    private static final Resultat UPDATED_RESULTAT = Resultat.ACCORD;

    private static final String ENTITY_API_URL = "/api/candidature-es";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CandidatureERepository candidatureERepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCandidatureEMockMvc;

    private CandidatureE candidatureE;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CandidatureE createEntity(EntityManager em) {
        CandidatureE candidatureE = new CandidatureE()
            .offreFormation(DEFAULT_OFFRE_FORMATION)
            .dateDebutOffre(DEFAULT_DATE_DEBUT_OFFRE)
            .dateFinOffre(DEFAULT_DATE_FIN_OFFRE)
            .dateDepot(DEFAULT_DATE_DEPOT)
            .resultat(DEFAULT_RESULTAT);
        return candidatureE;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CandidatureE createUpdatedEntity(EntityManager em) {
        CandidatureE candidatureE = new CandidatureE()
            .offreFormation(UPDATED_OFFRE_FORMATION)
            .dateDebutOffre(UPDATED_DATE_DEBUT_OFFRE)
            .dateFinOffre(UPDATED_DATE_FIN_OFFRE)
            .dateDepot(UPDATED_DATE_DEPOT)
            .resultat(UPDATED_RESULTAT);
        return candidatureE;
    }

    @BeforeEach
    public void initTest() {
        candidatureE = createEntity(em);
    }

    @Test
    @Transactional
    void createCandidatureE() throws Exception {
        int databaseSizeBeforeCreate = candidatureERepository.findAll().size();
        // Create the CandidatureE
        restCandidatureEMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(candidatureE)))
            .andExpect(status().isCreated());

        // Validate the CandidatureE in the database
        List<CandidatureE> candidatureEList = candidatureERepository.findAll();
        assertThat(candidatureEList).hasSize(databaseSizeBeforeCreate + 1);
        CandidatureE testCandidatureE = candidatureEList.get(candidatureEList.size() - 1);
        assertThat(testCandidatureE.getOffreFormation()).isEqualTo(DEFAULT_OFFRE_FORMATION);
        assertThat(testCandidatureE.getDateDebutOffre()).isEqualTo(DEFAULT_DATE_DEBUT_OFFRE);
        assertThat(testCandidatureE.getDateFinOffre()).isEqualTo(DEFAULT_DATE_FIN_OFFRE);
        assertThat(testCandidatureE.getDateDepot()).isEqualTo(DEFAULT_DATE_DEPOT);
        assertThat(testCandidatureE.getResultat()).isEqualTo(DEFAULT_RESULTAT);
    }

    @Test
    @Transactional
    void createCandidatureEWithExistingId() throws Exception {
        // Create the CandidatureE with an existing ID
        candidatureE.setId(1L);

        int databaseSizeBeforeCreate = candidatureERepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCandidatureEMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(candidatureE)))
            .andExpect(status().isBadRequest());

        // Validate the CandidatureE in the database
        List<CandidatureE> candidatureEList = candidatureERepository.findAll();
        assertThat(candidatureEList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCandidatureES() throws Exception {
        // Initialize the database
        candidatureERepository.saveAndFlush(candidatureE);

        // Get all the candidatureEList
        restCandidatureEMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(candidatureE.getId().intValue())))
            .andExpect(jsonPath("$.[*].offreFormation").value(hasItem(DEFAULT_OFFRE_FORMATION.toString())))
            .andExpect(jsonPath("$.[*].dateDebutOffre").value(hasItem(DEFAULT_DATE_DEBUT_OFFRE.toString())))
            .andExpect(jsonPath("$.[*].dateFinOffre").value(hasItem(DEFAULT_DATE_FIN_OFFRE.toString())))
            .andExpect(jsonPath("$.[*].dateDepot").value(hasItem(DEFAULT_DATE_DEPOT.toString())))
            .andExpect(jsonPath("$.[*].resultat").value(hasItem(DEFAULT_RESULTAT.toString())));
    }

    @Test
    @Transactional
    void getCandidatureE() throws Exception {
        // Initialize the database
        candidatureERepository.saveAndFlush(candidatureE);

        // Get the candidatureE
        restCandidatureEMockMvc
            .perform(get(ENTITY_API_URL_ID, candidatureE.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(candidatureE.getId().intValue()))
            .andExpect(jsonPath("$.offreFormation").value(DEFAULT_OFFRE_FORMATION.toString()))
            .andExpect(jsonPath("$.dateDebutOffre").value(DEFAULT_DATE_DEBUT_OFFRE.toString()))
            .andExpect(jsonPath("$.dateFinOffre").value(DEFAULT_DATE_FIN_OFFRE.toString()))
            .andExpect(jsonPath("$.dateDepot").value(DEFAULT_DATE_DEPOT.toString()))
            .andExpect(jsonPath("$.resultat").value(DEFAULT_RESULTAT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCandidatureE() throws Exception {
        // Get the candidatureE
        restCandidatureEMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCandidatureE() throws Exception {
        // Initialize the database
        candidatureERepository.saveAndFlush(candidatureE);

        int databaseSizeBeforeUpdate = candidatureERepository.findAll().size();

        // Update the candidatureE
        CandidatureE updatedCandidatureE = candidatureERepository.findById(candidatureE.getId()).get();
        // Disconnect from session so that the updates on updatedCandidatureE are not directly saved in db
        em.detach(updatedCandidatureE);
        updatedCandidatureE
            .offreFormation(UPDATED_OFFRE_FORMATION)
            .dateDebutOffre(UPDATED_DATE_DEBUT_OFFRE)
            .dateFinOffre(UPDATED_DATE_FIN_OFFRE)
            .dateDepot(UPDATED_DATE_DEPOT)
            .resultat(UPDATED_RESULTAT);

        restCandidatureEMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCandidatureE.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCandidatureE))
            )
            .andExpect(status().isOk());

        // Validate the CandidatureE in the database
        List<CandidatureE> candidatureEList = candidatureERepository.findAll();
        assertThat(candidatureEList).hasSize(databaseSizeBeforeUpdate);
        CandidatureE testCandidatureE = candidatureEList.get(candidatureEList.size() - 1);
        assertThat(testCandidatureE.getOffreFormation()).isEqualTo(UPDATED_OFFRE_FORMATION);
        assertThat(testCandidatureE.getDateDebutOffre()).isEqualTo(UPDATED_DATE_DEBUT_OFFRE);
        assertThat(testCandidatureE.getDateFinOffre()).isEqualTo(UPDATED_DATE_FIN_OFFRE);
        assertThat(testCandidatureE.getDateDepot()).isEqualTo(UPDATED_DATE_DEPOT);
        assertThat(testCandidatureE.getResultat()).isEqualTo(UPDATED_RESULTAT);
    }

    @Test
    @Transactional
    void putNonExistingCandidatureE() throws Exception {
        int databaseSizeBeforeUpdate = candidatureERepository.findAll().size();
        candidatureE.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCandidatureEMockMvc
            .perform(
                put(ENTITY_API_URL_ID, candidatureE.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(candidatureE))
            )
            .andExpect(status().isBadRequest());

        // Validate the CandidatureE in the database
        List<CandidatureE> candidatureEList = candidatureERepository.findAll();
        assertThat(candidatureEList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCandidatureE() throws Exception {
        int databaseSizeBeforeUpdate = candidatureERepository.findAll().size();
        candidatureE.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCandidatureEMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(candidatureE))
            )
            .andExpect(status().isBadRequest());

        // Validate the CandidatureE in the database
        List<CandidatureE> candidatureEList = candidatureERepository.findAll();
        assertThat(candidatureEList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCandidatureE() throws Exception {
        int databaseSizeBeforeUpdate = candidatureERepository.findAll().size();
        candidatureE.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCandidatureEMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(candidatureE)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CandidatureE in the database
        List<CandidatureE> candidatureEList = candidatureERepository.findAll();
        assertThat(candidatureEList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCandidatureEWithPatch() throws Exception {
        // Initialize the database
        candidatureERepository.saveAndFlush(candidatureE);

        int databaseSizeBeforeUpdate = candidatureERepository.findAll().size();

        // Update the candidatureE using partial update
        CandidatureE partialUpdatedCandidatureE = new CandidatureE();
        partialUpdatedCandidatureE.setId(candidatureE.getId());

        partialUpdatedCandidatureE.offreFormation(UPDATED_OFFRE_FORMATION).dateDebutOffre(UPDATED_DATE_DEBUT_OFFRE);

        restCandidatureEMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCandidatureE.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCandidatureE))
            )
            .andExpect(status().isOk());

        // Validate the CandidatureE in the database
        List<CandidatureE> candidatureEList = candidatureERepository.findAll();
        assertThat(candidatureEList).hasSize(databaseSizeBeforeUpdate);
        CandidatureE testCandidatureE = candidatureEList.get(candidatureEList.size() - 1);
        assertThat(testCandidatureE.getOffreFormation()).isEqualTo(UPDATED_OFFRE_FORMATION);
        assertThat(testCandidatureE.getDateDebutOffre()).isEqualTo(UPDATED_DATE_DEBUT_OFFRE);
        assertThat(testCandidatureE.getDateFinOffre()).isEqualTo(DEFAULT_DATE_FIN_OFFRE);
        assertThat(testCandidatureE.getDateDepot()).isEqualTo(DEFAULT_DATE_DEPOT);
        assertThat(testCandidatureE.getResultat()).isEqualTo(DEFAULT_RESULTAT);
    }

    @Test
    @Transactional
    void fullUpdateCandidatureEWithPatch() throws Exception {
        // Initialize the database
        candidatureERepository.saveAndFlush(candidatureE);

        int databaseSizeBeforeUpdate = candidatureERepository.findAll().size();

        // Update the candidatureE using partial update
        CandidatureE partialUpdatedCandidatureE = new CandidatureE();
        partialUpdatedCandidatureE.setId(candidatureE.getId());

        partialUpdatedCandidatureE
            .offreFormation(UPDATED_OFFRE_FORMATION)
            .dateDebutOffre(UPDATED_DATE_DEBUT_OFFRE)
            .dateFinOffre(UPDATED_DATE_FIN_OFFRE)
            .dateDepot(UPDATED_DATE_DEPOT)
            .resultat(UPDATED_RESULTAT);

        restCandidatureEMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCandidatureE.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCandidatureE))
            )
            .andExpect(status().isOk());

        // Validate the CandidatureE in the database
        List<CandidatureE> candidatureEList = candidatureERepository.findAll();
        assertThat(candidatureEList).hasSize(databaseSizeBeforeUpdate);
        CandidatureE testCandidatureE = candidatureEList.get(candidatureEList.size() - 1);
        assertThat(testCandidatureE.getOffreFormation()).isEqualTo(UPDATED_OFFRE_FORMATION);
        assertThat(testCandidatureE.getDateDebutOffre()).isEqualTo(UPDATED_DATE_DEBUT_OFFRE);
        assertThat(testCandidatureE.getDateFinOffre()).isEqualTo(UPDATED_DATE_FIN_OFFRE);
        assertThat(testCandidatureE.getDateDepot()).isEqualTo(UPDATED_DATE_DEPOT);
        assertThat(testCandidatureE.getResultat()).isEqualTo(UPDATED_RESULTAT);
    }

    @Test
    @Transactional
    void patchNonExistingCandidatureE() throws Exception {
        int databaseSizeBeforeUpdate = candidatureERepository.findAll().size();
        candidatureE.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCandidatureEMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, candidatureE.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(candidatureE))
            )
            .andExpect(status().isBadRequest());

        // Validate the CandidatureE in the database
        List<CandidatureE> candidatureEList = candidatureERepository.findAll();
        assertThat(candidatureEList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCandidatureE() throws Exception {
        int databaseSizeBeforeUpdate = candidatureERepository.findAll().size();
        candidatureE.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCandidatureEMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(candidatureE))
            )
            .andExpect(status().isBadRequest());

        // Validate the CandidatureE in the database
        List<CandidatureE> candidatureEList = candidatureERepository.findAll();
        assertThat(candidatureEList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCandidatureE() throws Exception {
        int databaseSizeBeforeUpdate = candidatureERepository.findAll().size();
        candidatureE.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCandidatureEMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(candidatureE))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CandidatureE in the database
        List<CandidatureE> candidatureEList = candidatureERepository.findAll();
        assertThat(candidatureEList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCandidatureE() throws Exception {
        // Initialize the database
        candidatureERepository.saveAndFlush(candidatureE);

        int databaseSizeBeforeDelete = candidatureERepository.findAll().size();

        // Delete the candidatureE
        restCandidatureEMockMvc
            .perform(delete(ENTITY_API_URL_ID, candidatureE.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CandidatureE> candidatureEList = candidatureERepository.findAll();
        assertThat(candidatureEList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
