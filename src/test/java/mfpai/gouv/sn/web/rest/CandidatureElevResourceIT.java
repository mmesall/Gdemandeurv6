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
import mfpai.gouv.sn.domain.CandidatureElev;
import mfpai.gouv.sn.domain.enumeration.NomFiliere;
import mfpai.gouv.sn.domain.enumeration.Resultat;
import mfpai.gouv.sn.repository.CandidatureElevRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CandidatureElevResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CandidatureElevResourceIT {

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

    private static final String ENTITY_API_URL = "/api/candidature-elevs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CandidatureElevRepository candidatureElevRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCandidatureElevMockMvc;

    private CandidatureElev candidatureElev;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CandidatureElev createEntity(EntityManager em) {
        CandidatureElev candidatureElev = new CandidatureElev()
            .offreFormation(DEFAULT_OFFRE_FORMATION)
            .dateDebutOffre(DEFAULT_DATE_DEBUT_OFFRE)
            .dateFinOffre(DEFAULT_DATE_FIN_OFFRE)
            .dateDepot(DEFAULT_DATE_DEPOT)
            .resultat(DEFAULT_RESULTAT);
        return candidatureElev;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CandidatureElev createUpdatedEntity(EntityManager em) {
        CandidatureElev candidatureElev = new CandidatureElev()
            .offreFormation(UPDATED_OFFRE_FORMATION)
            .dateDebutOffre(UPDATED_DATE_DEBUT_OFFRE)
            .dateFinOffre(UPDATED_DATE_FIN_OFFRE)
            .dateDepot(UPDATED_DATE_DEPOT)
            .resultat(UPDATED_RESULTAT);
        return candidatureElev;
    }

    @BeforeEach
    public void initTest() {
        candidatureElev = createEntity(em);
    }

    @Test
    @Transactional
    void createCandidatureElev() throws Exception {
        int databaseSizeBeforeCreate = candidatureElevRepository.findAll().size();
        // Create the CandidatureElev
        restCandidatureElevMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(candidatureElev))
            )
            .andExpect(status().isCreated());

        // Validate the CandidatureElev in the database
        List<CandidatureElev> candidatureElevList = candidatureElevRepository.findAll();
        assertThat(candidatureElevList).hasSize(databaseSizeBeforeCreate + 1);
        CandidatureElev testCandidatureElev = candidatureElevList.get(candidatureElevList.size() - 1);
        assertThat(testCandidatureElev.getOffreFormation()).isEqualTo(DEFAULT_OFFRE_FORMATION);
        assertThat(testCandidatureElev.getDateDebutOffre()).isEqualTo(DEFAULT_DATE_DEBUT_OFFRE);
        assertThat(testCandidatureElev.getDateFinOffre()).isEqualTo(DEFAULT_DATE_FIN_OFFRE);
        assertThat(testCandidatureElev.getDateDepot()).isEqualTo(DEFAULT_DATE_DEPOT);
        assertThat(testCandidatureElev.getResultat()).isEqualTo(DEFAULT_RESULTAT);
    }

    @Test
    @Transactional
    void createCandidatureElevWithExistingId() throws Exception {
        // Create the CandidatureElev with an existing ID
        candidatureElev.setId(1L);

        int databaseSizeBeforeCreate = candidatureElevRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCandidatureElevMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(candidatureElev))
            )
            .andExpect(status().isBadRequest());

        // Validate the CandidatureElev in the database
        List<CandidatureElev> candidatureElevList = candidatureElevRepository.findAll();
        assertThat(candidatureElevList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCandidatureElevs() throws Exception {
        // Initialize the database
        candidatureElevRepository.saveAndFlush(candidatureElev);

        // Get all the candidatureElevList
        restCandidatureElevMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(candidatureElev.getId().intValue())))
            .andExpect(jsonPath("$.[*].offreFormation").value(hasItem(DEFAULT_OFFRE_FORMATION.toString())))
            .andExpect(jsonPath("$.[*].dateDebutOffre").value(hasItem(DEFAULT_DATE_DEBUT_OFFRE.toString())))
            .andExpect(jsonPath("$.[*].dateFinOffre").value(hasItem(DEFAULT_DATE_FIN_OFFRE.toString())))
            .andExpect(jsonPath("$.[*].dateDepot").value(hasItem(DEFAULT_DATE_DEPOT.toString())))
            .andExpect(jsonPath("$.[*].resultat").value(hasItem(DEFAULT_RESULTAT.toString())));
    }

    @Test
    @Transactional
    void getCandidatureElev() throws Exception {
        // Initialize the database
        candidatureElevRepository.saveAndFlush(candidatureElev);

        // Get the candidatureElev
        restCandidatureElevMockMvc
            .perform(get(ENTITY_API_URL_ID, candidatureElev.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(candidatureElev.getId().intValue()))
            .andExpect(jsonPath("$.offreFormation").value(DEFAULT_OFFRE_FORMATION.toString()))
            .andExpect(jsonPath("$.dateDebutOffre").value(DEFAULT_DATE_DEBUT_OFFRE.toString()))
            .andExpect(jsonPath("$.dateFinOffre").value(DEFAULT_DATE_FIN_OFFRE.toString()))
            .andExpect(jsonPath("$.dateDepot").value(DEFAULT_DATE_DEPOT.toString()))
            .andExpect(jsonPath("$.resultat").value(DEFAULT_RESULTAT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCandidatureElev() throws Exception {
        // Get the candidatureElev
        restCandidatureElevMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCandidatureElev() throws Exception {
        // Initialize the database
        candidatureElevRepository.saveAndFlush(candidatureElev);

        int databaseSizeBeforeUpdate = candidatureElevRepository.findAll().size();

        // Update the candidatureElev
        CandidatureElev updatedCandidatureElev = candidatureElevRepository.findById(candidatureElev.getId()).get();
        // Disconnect from session so that the updates on updatedCandidatureElev are not directly saved in db
        em.detach(updatedCandidatureElev);
        updatedCandidatureElev
            .offreFormation(UPDATED_OFFRE_FORMATION)
            .dateDebutOffre(UPDATED_DATE_DEBUT_OFFRE)
            .dateFinOffre(UPDATED_DATE_FIN_OFFRE)
            .dateDepot(UPDATED_DATE_DEPOT)
            .resultat(UPDATED_RESULTAT);

        restCandidatureElevMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCandidatureElev.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCandidatureElev))
            )
            .andExpect(status().isOk());

        // Validate the CandidatureElev in the database
        List<CandidatureElev> candidatureElevList = candidatureElevRepository.findAll();
        assertThat(candidatureElevList).hasSize(databaseSizeBeforeUpdate);
        CandidatureElev testCandidatureElev = candidatureElevList.get(candidatureElevList.size() - 1);
        assertThat(testCandidatureElev.getOffreFormation()).isEqualTo(UPDATED_OFFRE_FORMATION);
        assertThat(testCandidatureElev.getDateDebutOffre()).isEqualTo(UPDATED_DATE_DEBUT_OFFRE);
        assertThat(testCandidatureElev.getDateFinOffre()).isEqualTo(UPDATED_DATE_FIN_OFFRE);
        assertThat(testCandidatureElev.getDateDepot()).isEqualTo(UPDATED_DATE_DEPOT);
        assertThat(testCandidatureElev.getResultat()).isEqualTo(UPDATED_RESULTAT);
    }

    @Test
    @Transactional
    void putNonExistingCandidatureElev() throws Exception {
        int databaseSizeBeforeUpdate = candidatureElevRepository.findAll().size();
        candidatureElev.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCandidatureElevMockMvc
            .perform(
                put(ENTITY_API_URL_ID, candidatureElev.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(candidatureElev))
            )
            .andExpect(status().isBadRequest());

        // Validate the CandidatureElev in the database
        List<CandidatureElev> candidatureElevList = candidatureElevRepository.findAll();
        assertThat(candidatureElevList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCandidatureElev() throws Exception {
        int databaseSizeBeforeUpdate = candidatureElevRepository.findAll().size();
        candidatureElev.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCandidatureElevMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(candidatureElev))
            )
            .andExpect(status().isBadRequest());

        // Validate the CandidatureElev in the database
        List<CandidatureElev> candidatureElevList = candidatureElevRepository.findAll();
        assertThat(candidatureElevList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCandidatureElev() throws Exception {
        int databaseSizeBeforeUpdate = candidatureElevRepository.findAll().size();
        candidatureElev.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCandidatureElevMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(candidatureElev))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CandidatureElev in the database
        List<CandidatureElev> candidatureElevList = candidatureElevRepository.findAll();
        assertThat(candidatureElevList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCandidatureElevWithPatch() throws Exception {
        // Initialize the database
        candidatureElevRepository.saveAndFlush(candidatureElev);

        int databaseSizeBeforeUpdate = candidatureElevRepository.findAll().size();

        // Update the candidatureElev using partial update
        CandidatureElev partialUpdatedCandidatureElev = new CandidatureElev();
        partialUpdatedCandidatureElev.setId(candidatureElev.getId());

        partialUpdatedCandidatureElev.offreFormation(UPDATED_OFFRE_FORMATION).resultat(UPDATED_RESULTAT);

        restCandidatureElevMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCandidatureElev.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCandidatureElev))
            )
            .andExpect(status().isOk());

        // Validate the CandidatureElev in the database
        List<CandidatureElev> candidatureElevList = candidatureElevRepository.findAll();
        assertThat(candidatureElevList).hasSize(databaseSizeBeforeUpdate);
        CandidatureElev testCandidatureElev = candidatureElevList.get(candidatureElevList.size() - 1);
        assertThat(testCandidatureElev.getOffreFormation()).isEqualTo(UPDATED_OFFRE_FORMATION);
        assertThat(testCandidatureElev.getDateDebutOffre()).isEqualTo(DEFAULT_DATE_DEBUT_OFFRE);
        assertThat(testCandidatureElev.getDateFinOffre()).isEqualTo(DEFAULT_DATE_FIN_OFFRE);
        assertThat(testCandidatureElev.getDateDepot()).isEqualTo(DEFAULT_DATE_DEPOT);
        assertThat(testCandidatureElev.getResultat()).isEqualTo(UPDATED_RESULTAT);
    }

    @Test
    @Transactional
    void fullUpdateCandidatureElevWithPatch() throws Exception {
        // Initialize the database
        candidatureElevRepository.saveAndFlush(candidatureElev);

        int databaseSizeBeforeUpdate = candidatureElevRepository.findAll().size();

        // Update the candidatureElev using partial update
        CandidatureElev partialUpdatedCandidatureElev = new CandidatureElev();
        partialUpdatedCandidatureElev.setId(candidatureElev.getId());

        partialUpdatedCandidatureElev
            .offreFormation(UPDATED_OFFRE_FORMATION)
            .dateDebutOffre(UPDATED_DATE_DEBUT_OFFRE)
            .dateFinOffre(UPDATED_DATE_FIN_OFFRE)
            .dateDepot(UPDATED_DATE_DEPOT)
            .resultat(UPDATED_RESULTAT);

        restCandidatureElevMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCandidatureElev.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCandidatureElev))
            )
            .andExpect(status().isOk());

        // Validate the CandidatureElev in the database
        List<CandidatureElev> candidatureElevList = candidatureElevRepository.findAll();
        assertThat(candidatureElevList).hasSize(databaseSizeBeforeUpdate);
        CandidatureElev testCandidatureElev = candidatureElevList.get(candidatureElevList.size() - 1);
        assertThat(testCandidatureElev.getOffreFormation()).isEqualTo(UPDATED_OFFRE_FORMATION);
        assertThat(testCandidatureElev.getDateDebutOffre()).isEqualTo(UPDATED_DATE_DEBUT_OFFRE);
        assertThat(testCandidatureElev.getDateFinOffre()).isEqualTo(UPDATED_DATE_FIN_OFFRE);
        assertThat(testCandidatureElev.getDateDepot()).isEqualTo(UPDATED_DATE_DEPOT);
        assertThat(testCandidatureElev.getResultat()).isEqualTo(UPDATED_RESULTAT);
    }

    @Test
    @Transactional
    void patchNonExistingCandidatureElev() throws Exception {
        int databaseSizeBeforeUpdate = candidatureElevRepository.findAll().size();
        candidatureElev.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCandidatureElevMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, candidatureElev.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(candidatureElev))
            )
            .andExpect(status().isBadRequest());

        // Validate the CandidatureElev in the database
        List<CandidatureElev> candidatureElevList = candidatureElevRepository.findAll();
        assertThat(candidatureElevList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCandidatureElev() throws Exception {
        int databaseSizeBeforeUpdate = candidatureElevRepository.findAll().size();
        candidatureElev.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCandidatureElevMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(candidatureElev))
            )
            .andExpect(status().isBadRequest());

        // Validate the CandidatureElev in the database
        List<CandidatureElev> candidatureElevList = candidatureElevRepository.findAll();
        assertThat(candidatureElevList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCandidatureElev() throws Exception {
        int databaseSizeBeforeUpdate = candidatureElevRepository.findAll().size();
        candidatureElev.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCandidatureElevMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(candidatureElev))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CandidatureElev in the database
        List<CandidatureElev> candidatureElevList = candidatureElevRepository.findAll();
        assertThat(candidatureElevList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCandidatureElev() throws Exception {
        // Initialize the database
        candidatureElevRepository.saveAndFlush(candidatureElev);

        int databaseSizeBeforeDelete = candidatureElevRepository.findAll().size();

        // Delete the candidatureElev
        restCandidatureElevMockMvc
            .perform(delete(ENTITY_API_URL_ID, candidatureElev.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CandidatureElev> candidatureElevList = candidatureElevRepository.findAll();
        assertThat(candidatureElevList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
