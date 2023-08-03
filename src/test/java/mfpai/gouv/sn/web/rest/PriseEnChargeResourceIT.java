package mfpai.gouv.sn.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import mfpai.gouv.sn.IntegrationTest;
import mfpai.gouv.sn.domain.PriseEnCharge;
import mfpai.gouv.sn.repository.PriseEnChargeRepository;
import mfpai.gouv.sn.service.PriseEnChargeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PriseEnChargeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PriseEnChargeResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final Double DEFAULT_MONTANT_PC = 1D;
    private static final Double UPDATED_MONTANT_PC = 2D;

    private static final String ENTITY_API_URL = "/api/prise-en-charges";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PriseEnChargeRepository priseEnChargeRepository;

    @Mock
    private PriseEnChargeRepository priseEnChargeRepositoryMock;

    @Mock
    private PriseEnChargeService priseEnChargeServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPriseEnChargeMockMvc;

    private PriseEnCharge priseEnCharge;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PriseEnCharge createEntity(EntityManager em) {
        PriseEnCharge priseEnCharge = new PriseEnCharge().libelle(DEFAULT_LIBELLE).montantPC(DEFAULT_MONTANT_PC);
        return priseEnCharge;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PriseEnCharge createUpdatedEntity(EntityManager em) {
        PriseEnCharge priseEnCharge = new PriseEnCharge().libelle(UPDATED_LIBELLE).montantPC(UPDATED_MONTANT_PC);
        return priseEnCharge;
    }

    @BeforeEach
    public void initTest() {
        priseEnCharge = createEntity(em);
    }

    @Test
    @Transactional
    void createPriseEnCharge() throws Exception {
        int databaseSizeBeforeCreate = priseEnChargeRepository.findAll().size();
        // Create the PriseEnCharge
        restPriseEnChargeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(priseEnCharge)))
            .andExpect(status().isCreated());

        // Validate the PriseEnCharge in the database
        List<PriseEnCharge> priseEnChargeList = priseEnChargeRepository.findAll();
        assertThat(priseEnChargeList).hasSize(databaseSizeBeforeCreate + 1);
        PriseEnCharge testPriseEnCharge = priseEnChargeList.get(priseEnChargeList.size() - 1);
        assertThat(testPriseEnCharge.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testPriseEnCharge.getMontantPC()).isEqualTo(DEFAULT_MONTANT_PC);
    }

    @Test
    @Transactional
    void createPriseEnChargeWithExistingId() throws Exception {
        // Create the PriseEnCharge with an existing ID
        priseEnCharge.setId(1L);

        int databaseSizeBeforeCreate = priseEnChargeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPriseEnChargeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(priseEnCharge)))
            .andExpect(status().isBadRequest());

        // Validate the PriseEnCharge in the database
        List<PriseEnCharge> priseEnChargeList = priseEnChargeRepository.findAll();
        assertThat(priseEnChargeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPriseEnCharges() throws Exception {
        // Initialize the database
        priseEnChargeRepository.saveAndFlush(priseEnCharge);

        // Get all the priseEnChargeList
        restPriseEnChargeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(priseEnCharge.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].montantPC").value(hasItem(DEFAULT_MONTANT_PC.doubleValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPriseEnChargesWithEagerRelationshipsIsEnabled() throws Exception {
        when(priseEnChargeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPriseEnChargeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(priseEnChargeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPriseEnChargesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(priseEnChargeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPriseEnChargeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(priseEnChargeRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPriseEnCharge() throws Exception {
        // Initialize the database
        priseEnChargeRepository.saveAndFlush(priseEnCharge);

        // Get the priseEnCharge
        restPriseEnChargeMockMvc
            .perform(get(ENTITY_API_URL_ID, priseEnCharge.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(priseEnCharge.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.montantPC").value(DEFAULT_MONTANT_PC.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingPriseEnCharge() throws Exception {
        // Get the priseEnCharge
        restPriseEnChargeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPriseEnCharge() throws Exception {
        // Initialize the database
        priseEnChargeRepository.saveAndFlush(priseEnCharge);

        int databaseSizeBeforeUpdate = priseEnChargeRepository.findAll().size();

        // Update the priseEnCharge
        PriseEnCharge updatedPriseEnCharge = priseEnChargeRepository.findById(priseEnCharge.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPriseEnCharge are not directly saved in db
        em.detach(updatedPriseEnCharge);
        updatedPriseEnCharge.libelle(UPDATED_LIBELLE).montantPC(UPDATED_MONTANT_PC);

        restPriseEnChargeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPriseEnCharge.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPriseEnCharge))
            )
            .andExpect(status().isOk());

        // Validate the PriseEnCharge in the database
        List<PriseEnCharge> priseEnChargeList = priseEnChargeRepository.findAll();
        assertThat(priseEnChargeList).hasSize(databaseSizeBeforeUpdate);
        PriseEnCharge testPriseEnCharge = priseEnChargeList.get(priseEnChargeList.size() - 1);
        assertThat(testPriseEnCharge.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testPriseEnCharge.getMontantPC()).isEqualTo(UPDATED_MONTANT_PC);
    }

    @Test
    @Transactional
    void putNonExistingPriseEnCharge() throws Exception {
        int databaseSizeBeforeUpdate = priseEnChargeRepository.findAll().size();
        priseEnCharge.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPriseEnChargeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, priseEnCharge.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(priseEnCharge))
            )
            .andExpect(status().isBadRequest());

        // Validate the PriseEnCharge in the database
        List<PriseEnCharge> priseEnChargeList = priseEnChargeRepository.findAll();
        assertThat(priseEnChargeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPriseEnCharge() throws Exception {
        int databaseSizeBeforeUpdate = priseEnChargeRepository.findAll().size();
        priseEnCharge.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPriseEnChargeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(priseEnCharge))
            )
            .andExpect(status().isBadRequest());

        // Validate the PriseEnCharge in the database
        List<PriseEnCharge> priseEnChargeList = priseEnChargeRepository.findAll();
        assertThat(priseEnChargeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPriseEnCharge() throws Exception {
        int databaseSizeBeforeUpdate = priseEnChargeRepository.findAll().size();
        priseEnCharge.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPriseEnChargeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(priseEnCharge)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PriseEnCharge in the database
        List<PriseEnCharge> priseEnChargeList = priseEnChargeRepository.findAll();
        assertThat(priseEnChargeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePriseEnChargeWithPatch() throws Exception {
        // Initialize the database
        priseEnChargeRepository.saveAndFlush(priseEnCharge);

        int databaseSizeBeforeUpdate = priseEnChargeRepository.findAll().size();

        // Update the priseEnCharge using partial update
        PriseEnCharge partialUpdatedPriseEnCharge = new PriseEnCharge();
        partialUpdatedPriseEnCharge.setId(priseEnCharge.getId());

        partialUpdatedPriseEnCharge.montantPC(UPDATED_MONTANT_PC);

        restPriseEnChargeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPriseEnCharge.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPriseEnCharge))
            )
            .andExpect(status().isOk());

        // Validate the PriseEnCharge in the database
        List<PriseEnCharge> priseEnChargeList = priseEnChargeRepository.findAll();
        assertThat(priseEnChargeList).hasSize(databaseSizeBeforeUpdate);
        PriseEnCharge testPriseEnCharge = priseEnChargeList.get(priseEnChargeList.size() - 1);
        assertThat(testPriseEnCharge.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testPriseEnCharge.getMontantPC()).isEqualTo(UPDATED_MONTANT_PC);
    }

    @Test
    @Transactional
    void fullUpdatePriseEnChargeWithPatch() throws Exception {
        // Initialize the database
        priseEnChargeRepository.saveAndFlush(priseEnCharge);

        int databaseSizeBeforeUpdate = priseEnChargeRepository.findAll().size();

        // Update the priseEnCharge using partial update
        PriseEnCharge partialUpdatedPriseEnCharge = new PriseEnCharge();
        partialUpdatedPriseEnCharge.setId(priseEnCharge.getId());

        partialUpdatedPriseEnCharge.libelle(UPDATED_LIBELLE).montantPC(UPDATED_MONTANT_PC);

        restPriseEnChargeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPriseEnCharge.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPriseEnCharge))
            )
            .andExpect(status().isOk());

        // Validate the PriseEnCharge in the database
        List<PriseEnCharge> priseEnChargeList = priseEnChargeRepository.findAll();
        assertThat(priseEnChargeList).hasSize(databaseSizeBeforeUpdate);
        PriseEnCharge testPriseEnCharge = priseEnChargeList.get(priseEnChargeList.size() - 1);
        assertThat(testPriseEnCharge.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testPriseEnCharge.getMontantPC()).isEqualTo(UPDATED_MONTANT_PC);
    }

    @Test
    @Transactional
    void patchNonExistingPriseEnCharge() throws Exception {
        int databaseSizeBeforeUpdate = priseEnChargeRepository.findAll().size();
        priseEnCharge.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPriseEnChargeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, priseEnCharge.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(priseEnCharge))
            )
            .andExpect(status().isBadRequest());

        // Validate the PriseEnCharge in the database
        List<PriseEnCharge> priseEnChargeList = priseEnChargeRepository.findAll();
        assertThat(priseEnChargeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPriseEnCharge() throws Exception {
        int databaseSizeBeforeUpdate = priseEnChargeRepository.findAll().size();
        priseEnCharge.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPriseEnChargeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(priseEnCharge))
            )
            .andExpect(status().isBadRequest());

        // Validate the PriseEnCharge in the database
        List<PriseEnCharge> priseEnChargeList = priseEnChargeRepository.findAll();
        assertThat(priseEnChargeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPriseEnCharge() throws Exception {
        int databaseSizeBeforeUpdate = priseEnChargeRepository.findAll().size();
        priseEnCharge.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPriseEnChargeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(priseEnCharge))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PriseEnCharge in the database
        List<PriseEnCharge> priseEnChargeList = priseEnChargeRepository.findAll();
        assertThat(priseEnChargeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePriseEnCharge() throws Exception {
        // Initialize the database
        priseEnChargeRepository.saveAndFlush(priseEnCharge);

        int databaseSizeBeforeDelete = priseEnChargeRepository.findAll().size();

        // Delete the priseEnCharge
        restPriseEnChargeMockMvc
            .perform(delete(ENTITY_API_URL_ID, priseEnCharge.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PriseEnCharge> priseEnChargeList = priseEnChargeRepository.findAll();
        assertThat(priseEnChargeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
