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
import mfpai.gouv.sn.domain.CandidatureEtudiant;
import mfpai.gouv.sn.domain.enumeration.NomFiliere;
import mfpai.gouv.sn.domain.enumeration.Resultat;
import mfpai.gouv.sn.repository.CandidatureEtudiantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CandidatureEtudiantResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CandidatureEtudiantResourceIT {

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

    private static final String ENTITY_API_URL = "/api/candidature-etudiants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CandidatureEtudiantRepository candidatureEtudiantRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCandidatureEtudiantMockMvc;

    private CandidatureEtudiant candidatureEtudiant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CandidatureEtudiant createEntity(EntityManager em) {
        CandidatureEtudiant candidatureEtudiant = new CandidatureEtudiant()
            .offreFormation(DEFAULT_OFFRE_FORMATION)
            .dateDebutOffre(DEFAULT_DATE_DEBUT_OFFRE)
            .dateFinOffre(DEFAULT_DATE_FIN_OFFRE)
            .dateDepot(DEFAULT_DATE_DEPOT)
            .resultat(DEFAULT_RESULTAT);
        return candidatureEtudiant;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CandidatureEtudiant createUpdatedEntity(EntityManager em) {
        CandidatureEtudiant candidatureEtudiant = new CandidatureEtudiant()
            .offreFormation(UPDATED_OFFRE_FORMATION)
            .dateDebutOffre(UPDATED_DATE_DEBUT_OFFRE)
            .dateFinOffre(UPDATED_DATE_FIN_OFFRE)
            .dateDepot(UPDATED_DATE_DEPOT)
            .resultat(UPDATED_RESULTAT);
        return candidatureEtudiant;
    }

    @BeforeEach
    public void initTest() {
        candidatureEtudiant = createEntity(em);
    }

    @Test
    @Transactional
    void createCandidatureEtudiant() throws Exception {
        int databaseSizeBeforeCreate = candidatureEtudiantRepository.findAll().size();
        // Create the CandidatureEtudiant
        restCandidatureEtudiantMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(candidatureEtudiant))
            )
            .andExpect(status().isCreated());

        // Validate the CandidatureEtudiant in the database
        List<CandidatureEtudiant> candidatureEtudiantList = candidatureEtudiantRepository.findAll();
        assertThat(candidatureEtudiantList).hasSize(databaseSizeBeforeCreate + 1);
        CandidatureEtudiant testCandidatureEtudiant = candidatureEtudiantList.get(candidatureEtudiantList.size() - 1);
        assertThat(testCandidatureEtudiant.getOffreFormation()).isEqualTo(DEFAULT_OFFRE_FORMATION);
        assertThat(testCandidatureEtudiant.getDateDebutOffre()).isEqualTo(DEFAULT_DATE_DEBUT_OFFRE);
        assertThat(testCandidatureEtudiant.getDateFinOffre()).isEqualTo(DEFAULT_DATE_FIN_OFFRE);
        assertThat(testCandidatureEtudiant.getDateDepot()).isEqualTo(DEFAULT_DATE_DEPOT);
        assertThat(testCandidatureEtudiant.getResultat()).isEqualTo(DEFAULT_RESULTAT);
    }

    @Test
    @Transactional
    void createCandidatureEtudiantWithExistingId() throws Exception {
        // Create the CandidatureEtudiant with an existing ID
        candidatureEtudiant.setId(1L);

        int databaseSizeBeforeCreate = candidatureEtudiantRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCandidatureEtudiantMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(candidatureEtudiant))
            )
            .andExpect(status().isBadRequest());

        // Validate the CandidatureEtudiant in the database
        List<CandidatureEtudiant> candidatureEtudiantList = candidatureEtudiantRepository.findAll();
        assertThat(candidatureEtudiantList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCandidatureEtudiants() throws Exception {
        // Initialize the database
        candidatureEtudiantRepository.saveAndFlush(candidatureEtudiant);

        // Get all the candidatureEtudiantList
        restCandidatureEtudiantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(candidatureEtudiant.getId().intValue())))
            .andExpect(jsonPath("$.[*].offreFormation").value(hasItem(DEFAULT_OFFRE_FORMATION.toString())))
            .andExpect(jsonPath("$.[*].dateDebutOffre").value(hasItem(DEFAULT_DATE_DEBUT_OFFRE.toString())))
            .andExpect(jsonPath("$.[*].dateFinOffre").value(hasItem(DEFAULT_DATE_FIN_OFFRE.toString())))
            .andExpect(jsonPath("$.[*].dateDepot").value(hasItem(DEFAULT_DATE_DEPOT.toString())))
            .andExpect(jsonPath("$.[*].resultat").value(hasItem(DEFAULT_RESULTAT.toString())));
    }

    @Test
    @Transactional
    void getCandidatureEtudiant() throws Exception {
        // Initialize the database
        candidatureEtudiantRepository.saveAndFlush(candidatureEtudiant);

        // Get the candidatureEtudiant
        restCandidatureEtudiantMockMvc
            .perform(get(ENTITY_API_URL_ID, candidatureEtudiant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(candidatureEtudiant.getId().intValue()))
            .andExpect(jsonPath("$.offreFormation").value(DEFAULT_OFFRE_FORMATION.toString()))
            .andExpect(jsonPath("$.dateDebutOffre").value(DEFAULT_DATE_DEBUT_OFFRE.toString()))
            .andExpect(jsonPath("$.dateFinOffre").value(DEFAULT_DATE_FIN_OFFRE.toString()))
            .andExpect(jsonPath("$.dateDepot").value(DEFAULT_DATE_DEPOT.toString()))
            .andExpect(jsonPath("$.resultat").value(DEFAULT_RESULTAT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCandidatureEtudiant() throws Exception {
        // Get the candidatureEtudiant
        restCandidatureEtudiantMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCandidatureEtudiant() throws Exception {
        // Initialize the database
        candidatureEtudiantRepository.saveAndFlush(candidatureEtudiant);

        int databaseSizeBeforeUpdate = candidatureEtudiantRepository.findAll().size();

        // Update the candidatureEtudiant
        CandidatureEtudiant updatedCandidatureEtudiant = candidatureEtudiantRepository.findById(candidatureEtudiant.getId()).get();
        // Disconnect from session so that the updates on updatedCandidatureEtudiant are not directly saved in db
        em.detach(updatedCandidatureEtudiant);
        updatedCandidatureEtudiant
            .offreFormation(UPDATED_OFFRE_FORMATION)
            .dateDebutOffre(UPDATED_DATE_DEBUT_OFFRE)
            .dateFinOffre(UPDATED_DATE_FIN_OFFRE)
            .dateDepot(UPDATED_DATE_DEPOT)
            .resultat(UPDATED_RESULTAT);

        restCandidatureEtudiantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCandidatureEtudiant.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCandidatureEtudiant))
            )
            .andExpect(status().isOk());

        // Validate the CandidatureEtudiant in the database
        List<CandidatureEtudiant> candidatureEtudiantList = candidatureEtudiantRepository.findAll();
        assertThat(candidatureEtudiantList).hasSize(databaseSizeBeforeUpdate);
        CandidatureEtudiant testCandidatureEtudiant = candidatureEtudiantList.get(candidatureEtudiantList.size() - 1);
        assertThat(testCandidatureEtudiant.getOffreFormation()).isEqualTo(UPDATED_OFFRE_FORMATION);
        assertThat(testCandidatureEtudiant.getDateDebutOffre()).isEqualTo(UPDATED_DATE_DEBUT_OFFRE);
        assertThat(testCandidatureEtudiant.getDateFinOffre()).isEqualTo(UPDATED_DATE_FIN_OFFRE);
        assertThat(testCandidatureEtudiant.getDateDepot()).isEqualTo(UPDATED_DATE_DEPOT);
        assertThat(testCandidatureEtudiant.getResultat()).isEqualTo(UPDATED_RESULTAT);
    }

    @Test
    @Transactional
    void putNonExistingCandidatureEtudiant() throws Exception {
        int databaseSizeBeforeUpdate = candidatureEtudiantRepository.findAll().size();
        candidatureEtudiant.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCandidatureEtudiantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, candidatureEtudiant.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(candidatureEtudiant))
            )
            .andExpect(status().isBadRequest());

        // Validate the CandidatureEtudiant in the database
        List<CandidatureEtudiant> candidatureEtudiantList = candidatureEtudiantRepository.findAll();
        assertThat(candidatureEtudiantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCandidatureEtudiant() throws Exception {
        int databaseSizeBeforeUpdate = candidatureEtudiantRepository.findAll().size();
        candidatureEtudiant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCandidatureEtudiantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(candidatureEtudiant))
            )
            .andExpect(status().isBadRequest());

        // Validate the CandidatureEtudiant in the database
        List<CandidatureEtudiant> candidatureEtudiantList = candidatureEtudiantRepository.findAll();
        assertThat(candidatureEtudiantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCandidatureEtudiant() throws Exception {
        int databaseSizeBeforeUpdate = candidatureEtudiantRepository.findAll().size();
        candidatureEtudiant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCandidatureEtudiantMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(candidatureEtudiant))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CandidatureEtudiant in the database
        List<CandidatureEtudiant> candidatureEtudiantList = candidatureEtudiantRepository.findAll();
        assertThat(candidatureEtudiantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCandidatureEtudiantWithPatch() throws Exception {
        // Initialize the database
        candidatureEtudiantRepository.saveAndFlush(candidatureEtudiant);

        int databaseSizeBeforeUpdate = candidatureEtudiantRepository.findAll().size();

        // Update the candidatureEtudiant using partial update
        CandidatureEtudiant partialUpdatedCandidatureEtudiant = new CandidatureEtudiant();
        partialUpdatedCandidatureEtudiant.setId(candidatureEtudiant.getId());

        partialUpdatedCandidatureEtudiant
            .offreFormation(UPDATED_OFFRE_FORMATION)
            .dateDebutOffre(UPDATED_DATE_DEBUT_OFFRE)
            .dateFinOffre(UPDATED_DATE_FIN_OFFRE)
            .dateDepot(UPDATED_DATE_DEPOT)
            .resultat(UPDATED_RESULTAT);

        restCandidatureEtudiantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCandidatureEtudiant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCandidatureEtudiant))
            )
            .andExpect(status().isOk());

        // Validate the CandidatureEtudiant in the database
        List<CandidatureEtudiant> candidatureEtudiantList = candidatureEtudiantRepository.findAll();
        assertThat(candidatureEtudiantList).hasSize(databaseSizeBeforeUpdate);
        CandidatureEtudiant testCandidatureEtudiant = candidatureEtudiantList.get(candidatureEtudiantList.size() - 1);
        assertThat(testCandidatureEtudiant.getOffreFormation()).isEqualTo(UPDATED_OFFRE_FORMATION);
        assertThat(testCandidatureEtudiant.getDateDebutOffre()).isEqualTo(UPDATED_DATE_DEBUT_OFFRE);
        assertThat(testCandidatureEtudiant.getDateFinOffre()).isEqualTo(UPDATED_DATE_FIN_OFFRE);
        assertThat(testCandidatureEtudiant.getDateDepot()).isEqualTo(UPDATED_DATE_DEPOT);
        assertThat(testCandidatureEtudiant.getResultat()).isEqualTo(UPDATED_RESULTAT);
    }

    @Test
    @Transactional
    void fullUpdateCandidatureEtudiantWithPatch() throws Exception {
        // Initialize the database
        candidatureEtudiantRepository.saveAndFlush(candidatureEtudiant);

        int databaseSizeBeforeUpdate = candidatureEtudiantRepository.findAll().size();

        // Update the candidatureEtudiant using partial update
        CandidatureEtudiant partialUpdatedCandidatureEtudiant = new CandidatureEtudiant();
        partialUpdatedCandidatureEtudiant.setId(candidatureEtudiant.getId());

        partialUpdatedCandidatureEtudiant
            .offreFormation(UPDATED_OFFRE_FORMATION)
            .dateDebutOffre(UPDATED_DATE_DEBUT_OFFRE)
            .dateFinOffre(UPDATED_DATE_FIN_OFFRE)
            .dateDepot(UPDATED_DATE_DEPOT)
            .resultat(UPDATED_RESULTAT);

        restCandidatureEtudiantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCandidatureEtudiant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCandidatureEtudiant))
            )
            .andExpect(status().isOk());

        // Validate the CandidatureEtudiant in the database
        List<CandidatureEtudiant> candidatureEtudiantList = candidatureEtudiantRepository.findAll();
        assertThat(candidatureEtudiantList).hasSize(databaseSizeBeforeUpdate);
        CandidatureEtudiant testCandidatureEtudiant = candidatureEtudiantList.get(candidatureEtudiantList.size() - 1);
        assertThat(testCandidatureEtudiant.getOffreFormation()).isEqualTo(UPDATED_OFFRE_FORMATION);
        assertThat(testCandidatureEtudiant.getDateDebutOffre()).isEqualTo(UPDATED_DATE_DEBUT_OFFRE);
        assertThat(testCandidatureEtudiant.getDateFinOffre()).isEqualTo(UPDATED_DATE_FIN_OFFRE);
        assertThat(testCandidatureEtudiant.getDateDepot()).isEqualTo(UPDATED_DATE_DEPOT);
        assertThat(testCandidatureEtudiant.getResultat()).isEqualTo(UPDATED_RESULTAT);
    }

    @Test
    @Transactional
    void patchNonExistingCandidatureEtudiant() throws Exception {
        int databaseSizeBeforeUpdate = candidatureEtudiantRepository.findAll().size();
        candidatureEtudiant.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCandidatureEtudiantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, candidatureEtudiant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(candidatureEtudiant))
            )
            .andExpect(status().isBadRequest());

        // Validate the CandidatureEtudiant in the database
        List<CandidatureEtudiant> candidatureEtudiantList = candidatureEtudiantRepository.findAll();
        assertThat(candidatureEtudiantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCandidatureEtudiant() throws Exception {
        int databaseSizeBeforeUpdate = candidatureEtudiantRepository.findAll().size();
        candidatureEtudiant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCandidatureEtudiantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(candidatureEtudiant))
            )
            .andExpect(status().isBadRequest());

        // Validate the CandidatureEtudiant in the database
        List<CandidatureEtudiant> candidatureEtudiantList = candidatureEtudiantRepository.findAll();
        assertThat(candidatureEtudiantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCandidatureEtudiant() throws Exception {
        int databaseSizeBeforeUpdate = candidatureEtudiantRepository.findAll().size();
        candidatureEtudiant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCandidatureEtudiantMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(candidatureEtudiant))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CandidatureEtudiant in the database
        List<CandidatureEtudiant> candidatureEtudiantList = candidatureEtudiantRepository.findAll();
        assertThat(candidatureEtudiantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCandidatureEtudiant() throws Exception {
        // Initialize the database
        candidatureEtudiantRepository.saveAndFlush(candidatureEtudiant);

        int databaseSizeBeforeDelete = candidatureEtudiantRepository.findAll().size();

        // Delete the candidatureEtudiant
        restCandidatureEtudiantMockMvc
            .perform(delete(ENTITY_API_URL_ID, candidatureEtudiant.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CandidatureEtudiant> candidatureEtudiantList = candidatureEtudiantRepository.findAll();
        assertThat(candidatureEtudiantList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
