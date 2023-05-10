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
import mfpai.gouv.sn.domain.CandidatureP;
import mfpai.gouv.sn.domain.enumeration.NomFiliere;
import mfpai.gouv.sn.domain.enumeration.Resultat;
import mfpai.gouv.sn.repository.CandidaturePRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CandidaturePResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CandidaturePResourceIT {

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

    private static final String ENTITY_API_URL = "/api/candidature-ps";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CandidaturePRepository candidaturePRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCandidaturePMockMvc;

    private CandidatureP candidatureP;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CandidatureP createEntity(EntityManager em) {
        CandidatureP candidatureP = new CandidatureP()
            .offreFormation(DEFAULT_OFFRE_FORMATION)
            .dateDebutOffre(DEFAULT_DATE_DEBUT_OFFRE)
            .dateFinOffre(DEFAULT_DATE_FIN_OFFRE)
            .dateDepot(DEFAULT_DATE_DEPOT)
            .resultat(DEFAULT_RESULTAT);
        return candidatureP;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CandidatureP createUpdatedEntity(EntityManager em) {
        CandidatureP candidatureP = new CandidatureP()
            .offreFormation(UPDATED_OFFRE_FORMATION)
            .dateDebutOffre(UPDATED_DATE_DEBUT_OFFRE)
            .dateFinOffre(UPDATED_DATE_FIN_OFFRE)
            .dateDepot(UPDATED_DATE_DEPOT)
            .resultat(UPDATED_RESULTAT);
        return candidatureP;
    }

    @BeforeEach
    public void initTest() {
        candidatureP = createEntity(em);
    }

    @Test
    @Transactional
    void createCandidatureP() throws Exception {
        int databaseSizeBeforeCreate = candidaturePRepository.findAll().size();
        // Create the CandidatureP
        restCandidaturePMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(candidatureP)))
            .andExpect(status().isCreated());

        // Validate the CandidatureP in the database
        List<CandidatureP> candidaturePList = candidaturePRepository.findAll();
        assertThat(candidaturePList).hasSize(databaseSizeBeforeCreate + 1);
        CandidatureP testCandidatureP = candidaturePList.get(candidaturePList.size() - 1);
        assertThat(testCandidatureP.getOffreFormation()).isEqualTo(DEFAULT_OFFRE_FORMATION);
        assertThat(testCandidatureP.getDateDebutOffre()).isEqualTo(DEFAULT_DATE_DEBUT_OFFRE);
        assertThat(testCandidatureP.getDateFinOffre()).isEqualTo(DEFAULT_DATE_FIN_OFFRE);
        assertThat(testCandidatureP.getDateDepot()).isEqualTo(DEFAULT_DATE_DEPOT);
        assertThat(testCandidatureP.getResultat()).isEqualTo(DEFAULT_RESULTAT);
    }

    @Test
    @Transactional
    void createCandidaturePWithExistingId() throws Exception {
        // Create the CandidatureP with an existing ID
        candidatureP.setId(1L);

        int databaseSizeBeforeCreate = candidaturePRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCandidaturePMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(candidatureP)))
            .andExpect(status().isBadRequest());

        // Validate the CandidatureP in the database
        List<CandidatureP> candidaturePList = candidaturePRepository.findAll();
        assertThat(candidaturePList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCandidaturePS() throws Exception {
        // Initialize the database
        candidaturePRepository.saveAndFlush(candidatureP);

        // Get all the candidaturePList
        restCandidaturePMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(candidatureP.getId().intValue())))
            .andExpect(jsonPath("$.[*].offreFormation").value(hasItem(DEFAULT_OFFRE_FORMATION.toString())))
            .andExpect(jsonPath("$.[*].dateDebutOffre").value(hasItem(DEFAULT_DATE_DEBUT_OFFRE.toString())))
            .andExpect(jsonPath("$.[*].dateFinOffre").value(hasItem(DEFAULT_DATE_FIN_OFFRE.toString())))
            .andExpect(jsonPath("$.[*].dateDepot").value(hasItem(DEFAULT_DATE_DEPOT.toString())))
            .andExpect(jsonPath("$.[*].resultat").value(hasItem(DEFAULT_RESULTAT.toString())));
    }

    @Test
    @Transactional
    void getCandidatureP() throws Exception {
        // Initialize the database
        candidaturePRepository.saveAndFlush(candidatureP);

        // Get the candidatureP
        restCandidaturePMockMvc
            .perform(get(ENTITY_API_URL_ID, candidatureP.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(candidatureP.getId().intValue()))
            .andExpect(jsonPath("$.offreFormation").value(DEFAULT_OFFRE_FORMATION.toString()))
            .andExpect(jsonPath("$.dateDebutOffre").value(DEFAULT_DATE_DEBUT_OFFRE.toString()))
            .andExpect(jsonPath("$.dateFinOffre").value(DEFAULT_DATE_FIN_OFFRE.toString()))
            .andExpect(jsonPath("$.dateDepot").value(DEFAULT_DATE_DEPOT.toString()))
            .andExpect(jsonPath("$.resultat").value(DEFAULT_RESULTAT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCandidatureP() throws Exception {
        // Get the candidatureP
        restCandidaturePMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCandidatureP() throws Exception {
        // Initialize the database
        candidaturePRepository.saveAndFlush(candidatureP);

        int databaseSizeBeforeUpdate = candidaturePRepository.findAll().size();

        // Update the candidatureP
        CandidatureP updatedCandidatureP = candidaturePRepository.findById(candidatureP.getId()).get();
        // Disconnect from session so that the updates on updatedCandidatureP are not directly saved in db
        em.detach(updatedCandidatureP);
        updatedCandidatureP
            .offreFormation(UPDATED_OFFRE_FORMATION)
            .dateDebutOffre(UPDATED_DATE_DEBUT_OFFRE)
            .dateFinOffre(UPDATED_DATE_FIN_OFFRE)
            .dateDepot(UPDATED_DATE_DEPOT)
            .resultat(UPDATED_RESULTAT);

        restCandidaturePMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCandidatureP.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCandidatureP))
            )
            .andExpect(status().isOk());

        // Validate the CandidatureP in the database
        List<CandidatureP> candidaturePList = candidaturePRepository.findAll();
        assertThat(candidaturePList).hasSize(databaseSizeBeforeUpdate);
        CandidatureP testCandidatureP = candidaturePList.get(candidaturePList.size() - 1);
        assertThat(testCandidatureP.getOffreFormation()).isEqualTo(UPDATED_OFFRE_FORMATION);
        assertThat(testCandidatureP.getDateDebutOffre()).isEqualTo(UPDATED_DATE_DEBUT_OFFRE);
        assertThat(testCandidatureP.getDateFinOffre()).isEqualTo(UPDATED_DATE_FIN_OFFRE);
        assertThat(testCandidatureP.getDateDepot()).isEqualTo(UPDATED_DATE_DEPOT);
        assertThat(testCandidatureP.getResultat()).isEqualTo(UPDATED_RESULTAT);
    }

    @Test
    @Transactional
    void putNonExistingCandidatureP() throws Exception {
        int databaseSizeBeforeUpdate = candidaturePRepository.findAll().size();
        candidatureP.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCandidaturePMockMvc
            .perform(
                put(ENTITY_API_URL_ID, candidatureP.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(candidatureP))
            )
            .andExpect(status().isBadRequest());

        // Validate the CandidatureP in the database
        List<CandidatureP> candidaturePList = candidaturePRepository.findAll();
        assertThat(candidaturePList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCandidatureP() throws Exception {
        int databaseSizeBeforeUpdate = candidaturePRepository.findAll().size();
        candidatureP.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCandidaturePMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(candidatureP))
            )
            .andExpect(status().isBadRequest());

        // Validate the CandidatureP in the database
        List<CandidatureP> candidaturePList = candidaturePRepository.findAll();
        assertThat(candidaturePList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCandidatureP() throws Exception {
        int databaseSizeBeforeUpdate = candidaturePRepository.findAll().size();
        candidatureP.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCandidaturePMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(candidatureP)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CandidatureP in the database
        List<CandidatureP> candidaturePList = candidaturePRepository.findAll();
        assertThat(candidaturePList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCandidaturePWithPatch() throws Exception {
        // Initialize the database
        candidaturePRepository.saveAndFlush(candidatureP);

        int databaseSizeBeforeUpdate = candidaturePRepository.findAll().size();

        // Update the candidatureP using partial update
        CandidatureP partialUpdatedCandidatureP = new CandidatureP();
        partialUpdatedCandidatureP.setId(candidatureP.getId());

        partialUpdatedCandidatureP.offreFormation(UPDATED_OFFRE_FORMATION).dateDepot(UPDATED_DATE_DEPOT).resultat(UPDATED_RESULTAT);

        restCandidaturePMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCandidatureP.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCandidatureP))
            )
            .andExpect(status().isOk());

        // Validate the CandidatureP in the database
        List<CandidatureP> candidaturePList = candidaturePRepository.findAll();
        assertThat(candidaturePList).hasSize(databaseSizeBeforeUpdate);
        CandidatureP testCandidatureP = candidaturePList.get(candidaturePList.size() - 1);
        assertThat(testCandidatureP.getOffreFormation()).isEqualTo(UPDATED_OFFRE_FORMATION);
        assertThat(testCandidatureP.getDateDebutOffre()).isEqualTo(DEFAULT_DATE_DEBUT_OFFRE);
        assertThat(testCandidatureP.getDateFinOffre()).isEqualTo(DEFAULT_DATE_FIN_OFFRE);
        assertThat(testCandidatureP.getDateDepot()).isEqualTo(UPDATED_DATE_DEPOT);
        assertThat(testCandidatureP.getResultat()).isEqualTo(UPDATED_RESULTAT);
    }

    @Test
    @Transactional
    void fullUpdateCandidaturePWithPatch() throws Exception {
        // Initialize the database
        candidaturePRepository.saveAndFlush(candidatureP);

        int databaseSizeBeforeUpdate = candidaturePRepository.findAll().size();

        // Update the candidatureP using partial update
        CandidatureP partialUpdatedCandidatureP = new CandidatureP();
        partialUpdatedCandidatureP.setId(candidatureP.getId());

        partialUpdatedCandidatureP
            .offreFormation(UPDATED_OFFRE_FORMATION)
            .dateDebutOffre(UPDATED_DATE_DEBUT_OFFRE)
            .dateFinOffre(UPDATED_DATE_FIN_OFFRE)
            .dateDepot(UPDATED_DATE_DEPOT)
            .resultat(UPDATED_RESULTAT);

        restCandidaturePMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCandidatureP.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCandidatureP))
            )
            .andExpect(status().isOk());

        // Validate the CandidatureP in the database
        List<CandidatureP> candidaturePList = candidaturePRepository.findAll();
        assertThat(candidaturePList).hasSize(databaseSizeBeforeUpdate);
        CandidatureP testCandidatureP = candidaturePList.get(candidaturePList.size() - 1);
        assertThat(testCandidatureP.getOffreFormation()).isEqualTo(UPDATED_OFFRE_FORMATION);
        assertThat(testCandidatureP.getDateDebutOffre()).isEqualTo(UPDATED_DATE_DEBUT_OFFRE);
        assertThat(testCandidatureP.getDateFinOffre()).isEqualTo(UPDATED_DATE_FIN_OFFRE);
        assertThat(testCandidatureP.getDateDepot()).isEqualTo(UPDATED_DATE_DEPOT);
        assertThat(testCandidatureP.getResultat()).isEqualTo(UPDATED_RESULTAT);
    }

    @Test
    @Transactional
    void patchNonExistingCandidatureP() throws Exception {
        int databaseSizeBeforeUpdate = candidaturePRepository.findAll().size();
        candidatureP.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCandidaturePMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, candidatureP.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(candidatureP))
            )
            .andExpect(status().isBadRequest());

        // Validate the CandidatureP in the database
        List<CandidatureP> candidaturePList = candidaturePRepository.findAll();
        assertThat(candidaturePList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCandidatureP() throws Exception {
        int databaseSizeBeforeUpdate = candidaturePRepository.findAll().size();
        candidatureP.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCandidaturePMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(candidatureP))
            )
            .andExpect(status().isBadRequest());

        // Validate the CandidatureP in the database
        List<CandidatureP> candidaturePList = candidaturePRepository.findAll();
        assertThat(candidaturePList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCandidatureP() throws Exception {
        int databaseSizeBeforeUpdate = candidaturePRepository.findAll().size();
        candidatureP.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCandidaturePMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(candidatureP))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CandidatureP in the database
        List<CandidatureP> candidaturePList = candidaturePRepository.findAll();
        assertThat(candidaturePList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCandidatureP() throws Exception {
        // Initialize the database
        candidaturePRepository.saveAndFlush(candidatureP);

        int databaseSizeBeforeDelete = candidaturePRepository.findAll().size();

        // Delete the candidatureP
        restCandidaturePMockMvc
            .perform(delete(ENTITY_API_URL_ID, candidatureP.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CandidatureP> candidaturePList = candidaturePRepository.findAll();
        assertThat(candidaturePList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
