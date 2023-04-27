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
import mfpai.gouv.sn.domain.CandidatureProf;
import mfpai.gouv.sn.domain.enumeration.NomFiliere;
import mfpai.gouv.sn.domain.enumeration.Resultat;
import mfpai.gouv.sn.repository.CandidatureProfRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CandidatureProfResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CandidatureProfResourceIT {

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

    private static final String ENTITY_API_URL = "/api/candidature-profs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CandidatureProfRepository candidatureProfRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCandidatureProfMockMvc;

    private CandidatureProf candidatureProf;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CandidatureProf createEntity(EntityManager em) {
        CandidatureProf candidatureProf = new CandidatureProf()
            .offreFormation(DEFAULT_OFFRE_FORMATION)
            .dateDebutOffre(DEFAULT_DATE_DEBUT_OFFRE)
            .dateFinOffre(DEFAULT_DATE_FIN_OFFRE)
            .dateDepot(DEFAULT_DATE_DEPOT)
            .resultat(DEFAULT_RESULTAT);
        return candidatureProf;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CandidatureProf createUpdatedEntity(EntityManager em) {
        CandidatureProf candidatureProf = new CandidatureProf()
            .offreFormation(UPDATED_OFFRE_FORMATION)
            .dateDebutOffre(UPDATED_DATE_DEBUT_OFFRE)
            .dateFinOffre(UPDATED_DATE_FIN_OFFRE)
            .dateDepot(UPDATED_DATE_DEPOT)
            .resultat(UPDATED_RESULTAT);
        return candidatureProf;
    }

    @BeforeEach
    public void initTest() {
        candidatureProf = createEntity(em);
    }

    @Test
    @Transactional
    void createCandidatureProf() throws Exception {
        int databaseSizeBeforeCreate = candidatureProfRepository.findAll().size();
        // Create the CandidatureProf
        restCandidatureProfMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(candidatureProf))
            )
            .andExpect(status().isCreated());

        // Validate the CandidatureProf in the database
        List<CandidatureProf> candidatureProfList = candidatureProfRepository.findAll();
        assertThat(candidatureProfList).hasSize(databaseSizeBeforeCreate + 1);
        CandidatureProf testCandidatureProf = candidatureProfList.get(candidatureProfList.size() - 1);
        assertThat(testCandidatureProf.getOffreFormation()).isEqualTo(DEFAULT_OFFRE_FORMATION);
        assertThat(testCandidatureProf.getDateDebutOffre()).isEqualTo(DEFAULT_DATE_DEBUT_OFFRE);
        assertThat(testCandidatureProf.getDateFinOffre()).isEqualTo(DEFAULT_DATE_FIN_OFFRE);
        assertThat(testCandidatureProf.getDateDepot()).isEqualTo(DEFAULT_DATE_DEPOT);
        assertThat(testCandidatureProf.getResultat()).isEqualTo(DEFAULT_RESULTAT);
    }

    @Test
    @Transactional
    void createCandidatureProfWithExistingId() throws Exception {
        // Create the CandidatureProf with an existing ID
        candidatureProf.setId(1L);

        int databaseSizeBeforeCreate = candidatureProfRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCandidatureProfMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(candidatureProf))
            )
            .andExpect(status().isBadRequest());

        // Validate the CandidatureProf in the database
        List<CandidatureProf> candidatureProfList = candidatureProfRepository.findAll();
        assertThat(candidatureProfList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCandidatureProfs() throws Exception {
        // Initialize the database
        candidatureProfRepository.saveAndFlush(candidatureProf);

        // Get all the candidatureProfList
        restCandidatureProfMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(candidatureProf.getId().intValue())))
            .andExpect(jsonPath("$.[*].offreFormation").value(hasItem(DEFAULT_OFFRE_FORMATION.toString())))
            .andExpect(jsonPath("$.[*].dateDebutOffre").value(hasItem(DEFAULT_DATE_DEBUT_OFFRE.toString())))
            .andExpect(jsonPath("$.[*].dateFinOffre").value(hasItem(DEFAULT_DATE_FIN_OFFRE.toString())))
            .andExpect(jsonPath("$.[*].dateDepot").value(hasItem(DEFAULT_DATE_DEPOT.toString())))
            .andExpect(jsonPath("$.[*].resultat").value(hasItem(DEFAULT_RESULTAT.toString())));
    }

    @Test
    @Transactional
    void getCandidatureProf() throws Exception {
        // Initialize the database
        candidatureProfRepository.saveAndFlush(candidatureProf);

        // Get the candidatureProf
        restCandidatureProfMockMvc
            .perform(get(ENTITY_API_URL_ID, candidatureProf.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(candidatureProf.getId().intValue()))
            .andExpect(jsonPath("$.offreFormation").value(DEFAULT_OFFRE_FORMATION.toString()))
            .andExpect(jsonPath("$.dateDebutOffre").value(DEFAULT_DATE_DEBUT_OFFRE.toString()))
            .andExpect(jsonPath("$.dateFinOffre").value(DEFAULT_DATE_FIN_OFFRE.toString()))
            .andExpect(jsonPath("$.dateDepot").value(DEFAULT_DATE_DEPOT.toString()))
            .andExpect(jsonPath("$.resultat").value(DEFAULT_RESULTAT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCandidatureProf() throws Exception {
        // Get the candidatureProf
        restCandidatureProfMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCandidatureProf() throws Exception {
        // Initialize the database
        candidatureProfRepository.saveAndFlush(candidatureProf);

        int databaseSizeBeforeUpdate = candidatureProfRepository.findAll().size();

        // Update the candidatureProf
        CandidatureProf updatedCandidatureProf = candidatureProfRepository.findById(candidatureProf.getId()).get();
        // Disconnect from session so that the updates on updatedCandidatureProf are not directly saved in db
        em.detach(updatedCandidatureProf);
        updatedCandidatureProf
            .offreFormation(UPDATED_OFFRE_FORMATION)
            .dateDebutOffre(UPDATED_DATE_DEBUT_OFFRE)
            .dateFinOffre(UPDATED_DATE_FIN_OFFRE)
            .dateDepot(UPDATED_DATE_DEPOT)
            .resultat(UPDATED_RESULTAT);

        restCandidatureProfMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCandidatureProf.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCandidatureProf))
            )
            .andExpect(status().isOk());

        // Validate the CandidatureProf in the database
        List<CandidatureProf> candidatureProfList = candidatureProfRepository.findAll();
        assertThat(candidatureProfList).hasSize(databaseSizeBeforeUpdate);
        CandidatureProf testCandidatureProf = candidatureProfList.get(candidatureProfList.size() - 1);
        assertThat(testCandidatureProf.getOffreFormation()).isEqualTo(UPDATED_OFFRE_FORMATION);
        assertThat(testCandidatureProf.getDateDebutOffre()).isEqualTo(UPDATED_DATE_DEBUT_OFFRE);
        assertThat(testCandidatureProf.getDateFinOffre()).isEqualTo(UPDATED_DATE_FIN_OFFRE);
        assertThat(testCandidatureProf.getDateDepot()).isEqualTo(UPDATED_DATE_DEPOT);
        assertThat(testCandidatureProf.getResultat()).isEqualTo(UPDATED_RESULTAT);
    }

    @Test
    @Transactional
    void putNonExistingCandidatureProf() throws Exception {
        int databaseSizeBeforeUpdate = candidatureProfRepository.findAll().size();
        candidatureProf.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCandidatureProfMockMvc
            .perform(
                put(ENTITY_API_URL_ID, candidatureProf.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(candidatureProf))
            )
            .andExpect(status().isBadRequest());

        // Validate the CandidatureProf in the database
        List<CandidatureProf> candidatureProfList = candidatureProfRepository.findAll();
        assertThat(candidatureProfList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCandidatureProf() throws Exception {
        int databaseSizeBeforeUpdate = candidatureProfRepository.findAll().size();
        candidatureProf.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCandidatureProfMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(candidatureProf))
            )
            .andExpect(status().isBadRequest());

        // Validate the CandidatureProf in the database
        List<CandidatureProf> candidatureProfList = candidatureProfRepository.findAll();
        assertThat(candidatureProfList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCandidatureProf() throws Exception {
        int databaseSizeBeforeUpdate = candidatureProfRepository.findAll().size();
        candidatureProf.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCandidatureProfMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(candidatureProf))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CandidatureProf in the database
        List<CandidatureProf> candidatureProfList = candidatureProfRepository.findAll();
        assertThat(candidatureProfList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCandidatureProfWithPatch() throws Exception {
        // Initialize the database
        candidatureProfRepository.saveAndFlush(candidatureProf);

        int databaseSizeBeforeUpdate = candidatureProfRepository.findAll().size();

        // Update the candidatureProf using partial update
        CandidatureProf partialUpdatedCandidatureProf = new CandidatureProf();
        partialUpdatedCandidatureProf.setId(candidatureProf.getId());

        partialUpdatedCandidatureProf
            .offreFormation(UPDATED_OFFRE_FORMATION)
            .dateDebutOffre(UPDATED_DATE_DEBUT_OFFRE)
            .dateFinOffre(UPDATED_DATE_FIN_OFFRE)
            .resultat(UPDATED_RESULTAT);

        restCandidatureProfMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCandidatureProf.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCandidatureProf))
            )
            .andExpect(status().isOk());

        // Validate the CandidatureProf in the database
        List<CandidatureProf> candidatureProfList = candidatureProfRepository.findAll();
        assertThat(candidatureProfList).hasSize(databaseSizeBeforeUpdate);
        CandidatureProf testCandidatureProf = candidatureProfList.get(candidatureProfList.size() - 1);
        assertThat(testCandidatureProf.getOffreFormation()).isEqualTo(UPDATED_OFFRE_FORMATION);
        assertThat(testCandidatureProf.getDateDebutOffre()).isEqualTo(UPDATED_DATE_DEBUT_OFFRE);
        assertThat(testCandidatureProf.getDateFinOffre()).isEqualTo(UPDATED_DATE_FIN_OFFRE);
        assertThat(testCandidatureProf.getDateDepot()).isEqualTo(DEFAULT_DATE_DEPOT);
        assertThat(testCandidatureProf.getResultat()).isEqualTo(UPDATED_RESULTAT);
    }

    @Test
    @Transactional
    void fullUpdateCandidatureProfWithPatch() throws Exception {
        // Initialize the database
        candidatureProfRepository.saveAndFlush(candidatureProf);

        int databaseSizeBeforeUpdate = candidatureProfRepository.findAll().size();

        // Update the candidatureProf using partial update
        CandidatureProf partialUpdatedCandidatureProf = new CandidatureProf();
        partialUpdatedCandidatureProf.setId(candidatureProf.getId());

        partialUpdatedCandidatureProf
            .offreFormation(UPDATED_OFFRE_FORMATION)
            .dateDebutOffre(UPDATED_DATE_DEBUT_OFFRE)
            .dateFinOffre(UPDATED_DATE_FIN_OFFRE)
            .dateDepot(UPDATED_DATE_DEPOT)
            .resultat(UPDATED_RESULTAT);

        restCandidatureProfMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCandidatureProf.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCandidatureProf))
            )
            .andExpect(status().isOk());

        // Validate the CandidatureProf in the database
        List<CandidatureProf> candidatureProfList = candidatureProfRepository.findAll();
        assertThat(candidatureProfList).hasSize(databaseSizeBeforeUpdate);
        CandidatureProf testCandidatureProf = candidatureProfList.get(candidatureProfList.size() - 1);
        assertThat(testCandidatureProf.getOffreFormation()).isEqualTo(UPDATED_OFFRE_FORMATION);
        assertThat(testCandidatureProf.getDateDebutOffre()).isEqualTo(UPDATED_DATE_DEBUT_OFFRE);
        assertThat(testCandidatureProf.getDateFinOffre()).isEqualTo(UPDATED_DATE_FIN_OFFRE);
        assertThat(testCandidatureProf.getDateDepot()).isEqualTo(UPDATED_DATE_DEPOT);
        assertThat(testCandidatureProf.getResultat()).isEqualTo(UPDATED_RESULTAT);
    }

    @Test
    @Transactional
    void patchNonExistingCandidatureProf() throws Exception {
        int databaseSizeBeforeUpdate = candidatureProfRepository.findAll().size();
        candidatureProf.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCandidatureProfMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, candidatureProf.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(candidatureProf))
            )
            .andExpect(status().isBadRequest());

        // Validate the CandidatureProf in the database
        List<CandidatureProf> candidatureProfList = candidatureProfRepository.findAll();
        assertThat(candidatureProfList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCandidatureProf() throws Exception {
        int databaseSizeBeforeUpdate = candidatureProfRepository.findAll().size();
        candidatureProf.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCandidatureProfMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(candidatureProf))
            )
            .andExpect(status().isBadRequest());

        // Validate the CandidatureProf in the database
        List<CandidatureProf> candidatureProfList = candidatureProfRepository.findAll();
        assertThat(candidatureProfList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCandidatureProf() throws Exception {
        int databaseSizeBeforeUpdate = candidatureProfRepository.findAll().size();
        candidatureProf.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCandidatureProfMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(candidatureProf))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CandidatureProf in the database
        List<CandidatureProf> candidatureProfList = candidatureProfRepository.findAll();
        assertThat(candidatureProfList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCandidatureProf() throws Exception {
        // Initialize the database
        candidatureProfRepository.saveAndFlush(candidatureProf);

        int databaseSizeBeforeDelete = candidatureProfRepository.findAll().size();

        // Delete the candidatureProf
        restCandidatureProfMockMvc
            .perform(delete(ENTITY_API_URL_ID, candidatureProf.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CandidatureProf> candidatureProfList = candidatureProfRepository.findAll();
        assertThat(candidatureProfList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
