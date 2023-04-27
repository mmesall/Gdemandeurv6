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
import mfpai.gouv.sn.domain.Candidature;
import mfpai.gouv.sn.domain.enumeration.NomFiliere;
import mfpai.gouv.sn.domain.enumeration.Resultat;
import mfpai.gouv.sn.repository.CandidatureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CandidatureResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CandidatureResourceIT {

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

    private static final String ENTITY_API_URL = "/api/candidatures";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CandidatureRepository candidatureRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCandidatureMockMvc;

    private Candidature candidature;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Candidature createEntity(EntityManager em) {
        Candidature candidature = new Candidature()
            .offreFormation(DEFAULT_OFFRE_FORMATION)
            .dateDebutOffre(DEFAULT_DATE_DEBUT_OFFRE)
            .dateFinOffre(DEFAULT_DATE_FIN_OFFRE)
            .dateDepot(DEFAULT_DATE_DEPOT)
            .resultat(DEFAULT_RESULTAT);
        return candidature;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Candidature createUpdatedEntity(EntityManager em) {
        Candidature candidature = new Candidature()
            .offreFormation(UPDATED_OFFRE_FORMATION)
            .dateDebutOffre(UPDATED_DATE_DEBUT_OFFRE)
            .dateFinOffre(UPDATED_DATE_FIN_OFFRE)
            .dateDepot(UPDATED_DATE_DEPOT)
            .resultat(UPDATED_RESULTAT);
        return candidature;
    }

    @BeforeEach
    public void initTest() {
        candidature = createEntity(em);
    }

    @Test
    @Transactional
    void createCandidature() throws Exception {
        int databaseSizeBeforeCreate = candidatureRepository.findAll().size();
        // Create the Candidature
        restCandidatureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(candidature)))
            .andExpect(status().isCreated());

        // Validate the Candidature in the database
        List<Candidature> candidatureList = candidatureRepository.findAll();
        assertThat(candidatureList).hasSize(databaseSizeBeforeCreate + 1);
        Candidature testCandidature = candidatureList.get(candidatureList.size() - 1);
        assertThat(testCandidature.getOffreFormation()).isEqualTo(DEFAULT_OFFRE_FORMATION);
        assertThat(testCandidature.getDateDebutOffre()).isEqualTo(DEFAULT_DATE_DEBUT_OFFRE);
        assertThat(testCandidature.getDateFinOffre()).isEqualTo(DEFAULT_DATE_FIN_OFFRE);
        assertThat(testCandidature.getDateDepot()).isEqualTo(DEFAULT_DATE_DEPOT);
        assertThat(testCandidature.getResultat()).isEqualTo(DEFAULT_RESULTAT);
    }

    @Test
    @Transactional
    void createCandidatureWithExistingId() throws Exception {
        // Create the Candidature with an existing ID
        candidature.setId(1L);

        int databaseSizeBeforeCreate = candidatureRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCandidatureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(candidature)))
            .andExpect(status().isBadRequest());

        // Validate the Candidature in the database
        List<Candidature> candidatureList = candidatureRepository.findAll();
        assertThat(candidatureList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCandidatures() throws Exception {
        // Initialize the database
        candidatureRepository.saveAndFlush(candidature);

        // Get all the candidatureList
        restCandidatureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(candidature.getId().intValue())))
            .andExpect(jsonPath("$.[*].offreFormation").value(hasItem(DEFAULT_OFFRE_FORMATION.toString())))
            .andExpect(jsonPath("$.[*].dateDebutOffre").value(hasItem(DEFAULT_DATE_DEBUT_OFFRE.toString())))
            .andExpect(jsonPath("$.[*].dateFinOffre").value(hasItem(DEFAULT_DATE_FIN_OFFRE.toString())))
            .andExpect(jsonPath("$.[*].dateDepot").value(hasItem(DEFAULT_DATE_DEPOT.toString())))
            .andExpect(jsonPath("$.[*].resultat").value(hasItem(DEFAULT_RESULTAT.toString())));
    }

    @Test
    @Transactional
    void getCandidature() throws Exception {
        // Initialize the database
        candidatureRepository.saveAndFlush(candidature);

        // Get the candidature
        restCandidatureMockMvc
            .perform(get(ENTITY_API_URL_ID, candidature.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(candidature.getId().intValue()))
            .andExpect(jsonPath("$.offreFormation").value(DEFAULT_OFFRE_FORMATION.toString()))
            .andExpect(jsonPath("$.dateDebutOffre").value(DEFAULT_DATE_DEBUT_OFFRE.toString()))
            .andExpect(jsonPath("$.dateFinOffre").value(DEFAULT_DATE_FIN_OFFRE.toString()))
            .andExpect(jsonPath("$.dateDepot").value(DEFAULT_DATE_DEPOT.toString()))
            .andExpect(jsonPath("$.resultat").value(DEFAULT_RESULTAT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCandidature() throws Exception {
        // Get the candidature
        restCandidatureMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCandidature() throws Exception {
        // Initialize the database
        candidatureRepository.saveAndFlush(candidature);

        int databaseSizeBeforeUpdate = candidatureRepository.findAll().size();

        // Update the candidature
        Candidature updatedCandidature = candidatureRepository.findById(candidature.getId()).get();
        // Disconnect from session so that the updates on updatedCandidature are not directly saved in db
        em.detach(updatedCandidature);
        updatedCandidature
            .offreFormation(UPDATED_OFFRE_FORMATION)
            .dateDebutOffre(UPDATED_DATE_DEBUT_OFFRE)
            .dateFinOffre(UPDATED_DATE_FIN_OFFRE)
            .dateDepot(UPDATED_DATE_DEPOT)
            .resultat(UPDATED_RESULTAT);

        restCandidatureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCandidature.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCandidature))
            )
            .andExpect(status().isOk());

        // Validate the Candidature in the database
        List<Candidature> candidatureList = candidatureRepository.findAll();
        assertThat(candidatureList).hasSize(databaseSizeBeforeUpdate);
        Candidature testCandidature = candidatureList.get(candidatureList.size() - 1);
        assertThat(testCandidature.getOffreFormation()).isEqualTo(UPDATED_OFFRE_FORMATION);
        assertThat(testCandidature.getDateDebutOffre()).isEqualTo(UPDATED_DATE_DEBUT_OFFRE);
        assertThat(testCandidature.getDateFinOffre()).isEqualTo(UPDATED_DATE_FIN_OFFRE);
        assertThat(testCandidature.getDateDepot()).isEqualTo(UPDATED_DATE_DEPOT);
        assertThat(testCandidature.getResultat()).isEqualTo(UPDATED_RESULTAT);
    }

    @Test
    @Transactional
    void putNonExistingCandidature() throws Exception {
        int databaseSizeBeforeUpdate = candidatureRepository.findAll().size();
        candidature.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCandidatureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, candidature.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(candidature))
            )
            .andExpect(status().isBadRequest());

        // Validate the Candidature in the database
        List<Candidature> candidatureList = candidatureRepository.findAll();
        assertThat(candidatureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCandidature() throws Exception {
        int databaseSizeBeforeUpdate = candidatureRepository.findAll().size();
        candidature.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCandidatureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(candidature))
            )
            .andExpect(status().isBadRequest());

        // Validate the Candidature in the database
        List<Candidature> candidatureList = candidatureRepository.findAll();
        assertThat(candidatureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCandidature() throws Exception {
        int databaseSizeBeforeUpdate = candidatureRepository.findAll().size();
        candidature.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCandidatureMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(candidature)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Candidature in the database
        List<Candidature> candidatureList = candidatureRepository.findAll();
        assertThat(candidatureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCandidatureWithPatch() throws Exception {
        // Initialize the database
        candidatureRepository.saveAndFlush(candidature);

        int databaseSizeBeforeUpdate = candidatureRepository.findAll().size();

        // Update the candidature using partial update
        Candidature partialUpdatedCandidature = new Candidature();
        partialUpdatedCandidature.setId(candidature.getId());

        restCandidatureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCandidature.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCandidature))
            )
            .andExpect(status().isOk());

        // Validate the Candidature in the database
        List<Candidature> candidatureList = candidatureRepository.findAll();
        assertThat(candidatureList).hasSize(databaseSizeBeforeUpdate);
        Candidature testCandidature = candidatureList.get(candidatureList.size() - 1);
        assertThat(testCandidature.getOffreFormation()).isEqualTo(DEFAULT_OFFRE_FORMATION);
        assertThat(testCandidature.getDateDebutOffre()).isEqualTo(DEFAULT_DATE_DEBUT_OFFRE);
        assertThat(testCandidature.getDateFinOffre()).isEqualTo(DEFAULT_DATE_FIN_OFFRE);
        assertThat(testCandidature.getDateDepot()).isEqualTo(DEFAULT_DATE_DEPOT);
        assertThat(testCandidature.getResultat()).isEqualTo(DEFAULT_RESULTAT);
    }

    @Test
    @Transactional
    void fullUpdateCandidatureWithPatch() throws Exception {
        // Initialize the database
        candidatureRepository.saveAndFlush(candidature);

        int databaseSizeBeforeUpdate = candidatureRepository.findAll().size();

        // Update the candidature using partial update
        Candidature partialUpdatedCandidature = new Candidature();
        partialUpdatedCandidature.setId(candidature.getId());

        partialUpdatedCandidature
            .offreFormation(UPDATED_OFFRE_FORMATION)
            .dateDebutOffre(UPDATED_DATE_DEBUT_OFFRE)
            .dateFinOffre(UPDATED_DATE_FIN_OFFRE)
            .dateDepot(UPDATED_DATE_DEPOT)
            .resultat(UPDATED_RESULTAT);

        restCandidatureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCandidature.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCandidature))
            )
            .andExpect(status().isOk());

        // Validate the Candidature in the database
        List<Candidature> candidatureList = candidatureRepository.findAll();
        assertThat(candidatureList).hasSize(databaseSizeBeforeUpdate);
        Candidature testCandidature = candidatureList.get(candidatureList.size() - 1);
        assertThat(testCandidature.getOffreFormation()).isEqualTo(UPDATED_OFFRE_FORMATION);
        assertThat(testCandidature.getDateDebutOffre()).isEqualTo(UPDATED_DATE_DEBUT_OFFRE);
        assertThat(testCandidature.getDateFinOffre()).isEqualTo(UPDATED_DATE_FIN_OFFRE);
        assertThat(testCandidature.getDateDepot()).isEqualTo(UPDATED_DATE_DEPOT);
        assertThat(testCandidature.getResultat()).isEqualTo(UPDATED_RESULTAT);
    }

    @Test
    @Transactional
    void patchNonExistingCandidature() throws Exception {
        int databaseSizeBeforeUpdate = candidatureRepository.findAll().size();
        candidature.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCandidatureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, candidature.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(candidature))
            )
            .andExpect(status().isBadRequest());

        // Validate the Candidature in the database
        List<Candidature> candidatureList = candidatureRepository.findAll();
        assertThat(candidatureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCandidature() throws Exception {
        int databaseSizeBeforeUpdate = candidatureRepository.findAll().size();
        candidature.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCandidatureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(candidature))
            )
            .andExpect(status().isBadRequest());

        // Validate the Candidature in the database
        List<Candidature> candidatureList = candidatureRepository.findAll();
        assertThat(candidatureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCandidature() throws Exception {
        int databaseSizeBeforeUpdate = candidatureRepository.findAll().size();
        candidature.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCandidatureMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(candidature))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Candidature in the database
        List<Candidature> candidatureList = candidatureRepository.findAll();
        assertThat(candidatureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCandidature() throws Exception {
        // Initialize the database
        candidatureRepository.saveAndFlush(candidature);

        int databaseSizeBeforeDelete = candidatureRepository.findAll().size();

        // Delete the candidature
        restCandidatureMockMvc
            .perform(delete(ENTITY_API_URL_ID, candidature.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Candidature> candidatureList = candidatureRepository.findAll();
        assertThat(candidatureList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
