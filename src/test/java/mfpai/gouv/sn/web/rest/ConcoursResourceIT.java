package mfpai.gouv.sn.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import mfpai.gouv.sn.IntegrationTest;
import mfpai.gouv.sn.domain.Concours;
import mfpai.gouv.sn.domain.enumeration.NiveauEtude;
import mfpai.gouv.sn.domain.enumeration.NomEtablissement;
import mfpai.gouv.sn.repository.ConcoursRepository;
import mfpai.gouv.sn.service.ConcoursService;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ConcoursResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ConcoursResourceIT {

    private static final String DEFAULT_NOM_CONCOURS = "AAAAAAAAAA";
    private static final String UPDATED_NOM_CONCOURS = "BBBBBBBBBB";

    private static final NomEtablissement DEFAULT_NOM_ETABLISSEMENT = NomEtablissement.CEDT_G15;
    private static final NomEtablissement UPDATED_NOM_ETABLISSEMENT = NomEtablissement.CFP_OUAKAM;

    private static final NiveauEtude DEFAULT_NIVEAU_ETUDE = NiveauEtude.CINQUIEME;
    private static final NiveauEtude UPDATED_NIVEAU_ETUDE = NiveauEtude.QUATRIEME;

    private static final LocalDate DEFAULT_DATE_OUVERTURE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OUVERTURE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_CLOTURE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CLOTURE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_CONCOURS = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CONCOURS = LocalDate.now(ZoneId.systemDefault());

    private static final byte[] DEFAULT_AFFICHE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_AFFICHE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_AFFICHE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_AFFICHE_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/concours";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ConcoursRepository concoursRepository;

    @Mock
    private ConcoursRepository concoursRepositoryMock;

    @Mock
    private ConcoursService concoursServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConcoursMockMvc;

    private Concours concours;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Concours createEntity(EntityManager em) {
        Concours concours = new Concours()
            .nomConcours(DEFAULT_NOM_CONCOURS)
            .nomEtablissement(DEFAULT_NOM_ETABLISSEMENT)
            .niveauEtude(DEFAULT_NIVEAU_ETUDE)
            .dateOuverture(DEFAULT_DATE_OUVERTURE)
            .dateCloture(DEFAULT_DATE_CLOTURE)
            .dateConcours(DEFAULT_DATE_CONCOURS)
            .affiche(DEFAULT_AFFICHE)
            .afficheContentType(DEFAULT_AFFICHE_CONTENT_TYPE);
        return concours;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Concours createUpdatedEntity(EntityManager em) {
        Concours concours = new Concours()
            .nomConcours(UPDATED_NOM_CONCOURS)
            .nomEtablissement(UPDATED_NOM_ETABLISSEMENT)
            .niveauEtude(UPDATED_NIVEAU_ETUDE)
            .dateOuverture(UPDATED_DATE_OUVERTURE)
            .dateCloture(UPDATED_DATE_CLOTURE)
            .dateConcours(UPDATED_DATE_CONCOURS)
            .affiche(UPDATED_AFFICHE)
            .afficheContentType(UPDATED_AFFICHE_CONTENT_TYPE);
        return concours;
    }

    @BeforeEach
    public void initTest() {
        concours = createEntity(em);
    }

    @Test
    @Transactional
    void createConcours() throws Exception {
        int databaseSizeBeforeCreate = concoursRepository.findAll().size();
        // Create the Concours
        restConcoursMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(concours)))
            .andExpect(status().isCreated());

        // Validate the Concours in the database
        List<Concours> concoursList = concoursRepository.findAll();
        assertThat(concoursList).hasSize(databaseSizeBeforeCreate + 1);
        Concours testConcours = concoursList.get(concoursList.size() - 1);
        assertThat(testConcours.getNomConcours()).isEqualTo(DEFAULT_NOM_CONCOURS);
        assertThat(testConcours.getNomEtablissement()).isEqualTo(DEFAULT_NOM_ETABLISSEMENT);
        assertThat(testConcours.getNiveauEtude()).isEqualTo(DEFAULT_NIVEAU_ETUDE);
        assertThat(testConcours.getDateOuverture()).isEqualTo(DEFAULT_DATE_OUVERTURE);
        assertThat(testConcours.getDateCloture()).isEqualTo(DEFAULT_DATE_CLOTURE);
        assertThat(testConcours.getDateConcours()).isEqualTo(DEFAULT_DATE_CONCOURS);
        assertThat(testConcours.getAffiche()).isEqualTo(DEFAULT_AFFICHE);
        assertThat(testConcours.getAfficheContentType()).isEqualTo(DEFAULT_AFFICHE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createConcoursWithExistingId() throws Exception {
        // Create the Concours with an existing ID
        concours.setId(1L);

        int databaseSizeBeforeCreate = concoursRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConcoursMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(concours)))
            .andExpect(status().isBadRequest());

        // Validate the Concours in the database
        List<Concours> concoursList = concoursRepository.findAll();
        assertThat(concoursList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllConcours() throws Exception {
        // Initialize the database
        concoursRepository.saveAndFlush(concours);

        // Get all the concoursList
        restConcoursMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(concours.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomConcours").value(hasItem(DEFAULT_NOM_CONCOURS)))
            .andExpect(jsonPath("$.[*].nomEtablissement").value(hasItem(DEFAULT_NOM_ETABLISSEMENT.toString())))
            .andExpect(jsonPath("$.[*].niveauEtude").value(hasItem(DEFAULT_NIVEAU_ETUDE.toString())))
            .andExpect(jsonPath("$.[*].dateOuverture").value(hasItem(DEFAULT_DATE_OUVERTURE.toString())))
            .andExpect(jsonPath("$.[*].dateCloture").value(hasItem(DEFAULT_DATE_CLOTURE.toString())))
            .andExpect(jsonPath("$.[*].dateConcours").value(hasItem(DEFAULT_DATE_CONCOURS.toString())))
            .andExpect(jsonPath("$.[*].afficheContentType").value(hasItem(DEFAULT_AFFICHE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].affiche").value(hasItem(Base64Utils.encodeToString(DEFAULT_AFFICHE))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllConcoursWithEagerRelationshipsIsEnabled() throws Exception {
        when(concoursServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restConcoursMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(concoursServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllConcoursWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(concoursServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restConcoursMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(concoursRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getConcours() throws Exception {
        // Initialize the database
        concoursRepository.saveAndFlush(concours);

        // Get the concours
        restConcoursMockMvc
            .perform(get(ENTITY_API_URL_ID, concours.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(concours.getId().intValue()))
            .andExpect(jsonPath("$.nomConcours").value(DEFAULT_NOM_CONCOURS))
            .andExpect(jsonPath("$.nomEtablissement").value(DEFAULT_NOM_ETABLISSEMENT.toString()))
            .andExpect(jsonPath("$.niveauEtude").value(DEFAULT_NIVEAU_ETUDE.toString()))
            .andExpect(jsonPath("$.dateOuverture").value(DEFAULT_DATE_OUVERTURE.toString()))
            .andExpect(jsonPath("$.dateCloture").value(DEFAULT_DATE_CLOTURE.toString()))
            .andExpect(jsonPath("$.dateConcours").value(DEFAULT_DATE_CONCOURS.toString()))
            .andExpect(jsonPath("$.afficheContentType").value(DEFAULT_AFFICHE_CONTENT_TYPE))
            .andExpect(jsonPath("$.affiche").value(Base64Utils.encodeToString(DEFAULT_AFFICHE)));
    }

    @Test
    @Transactional
    void getNonExistingConcours() throws Exception {
        // Get the concours
        restConcoursMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingConcours() throws Exception {
        // Initialize the database
        concoursRepository.saveAndFlush(concours);

        int databaseSizeBeforeUpdate = concoursRepository.findAll().size();

        // Update the concours
        Concours updatedConcours = concoursRepository.findById(concours.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedConcours are not directly saved in db
        em.detach(updatedConcours);
        updatedConcours
            .nomConcours(UPDATED_NOM_CONCOURS)
            .nomEtablissement(UPDATED_NOM_ETABLISSEMENT)
            .niveauEtude(UPDATED_NIVEAU_ETUDE)
            .dateOuverture(UPDATED_DATE_OUVERTURE)
            .dateCloture(UPDATED_DATE_CLOTURE)
            .dateConcours(UPDATED_DATE_CONCOURS)
            .affiche(UPDATED_AFFICHE)
            .afficheContentType(UPDATED_AFFICHE_CONTENT_TYPE);

        restConcoursMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedConcours.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedConcours))
            )
            .andExpect(status().isOk());

        // Validate the Concours in the database
        List<Concours> concoursList = concoursRepository.findAll();
        assertThat(concoursList).hasSize(databaseSizeBeforeUpdate);
        Concours testConcours = concoursList.get(concoursList.size() - 1);
        assertThat(testConcours.getNomConcours()).isEqualTo(UPDATED_NOM_CONCOURS);
        assertThat(testConcours.getNomEtablissement()).isEqualTo(UPDATED_NOM_ETABLISSEMENT);
        assertThat(testConcours.getNiveauEtude()).isEqualTo(UPDATED_NIVEAU_ETUDE);
        assertThat(testConcours.getDateOuverture()).isEqualTo(UPDATED_DATE_OUVERTURE);
        assertThat(testConcours.getDateCloture()).isEqualTo(UPDATED_DATE_CLOTURE);
        assertThat(testConcours.getDateConcours()).isEqualTo(UPDATED_DATE_CONCOURS);
        assertThat(testConcours.getAffiche()).isEqualTo(UPDATED_AFFICHE);
        assertThat(testConcours.getAfficheContentType()).isEqualTo(UPDATED_AFFICHE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingConcours() throws Exception {
        int databaseSizeBeforeUpdate = concoursRepository.findAll().size();
        concours.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConcoursMockMvc
            .perform(
                put(ENTITY_API_URL_ID, concours.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(concours))
            )
            .andExpect(status().isBadRequest());

        // Validate the Concours in the database
        List<Concours> concoursList = concoursRepository.findAll();
        assertThat(concoursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConcours() throws Exception {
        int databaseSizeBeforeUpdate = concoursRepository.findAll().size();
        concours.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConcoursMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(concours))
            )
            .andExpect(status().isBadRequest());

        // Validate the Concours in the database
        List<Concours> concoursList = concoursRepository.findAll();
        assertThat(concoursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConcours() throws Exception {
        int databaseSizeBeforeUpdate = concoursRepository.findAll().size();
        concours.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConcoursMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(concours)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Concours in the database
        List<Concours> concoursList = concoursRepository.findAll();
        assertThat(concoursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConcoursWithPatch() throws Exception {
        // Initialize the database
        concoursRepository.saveAndFlush(concours);

        int databaseSizeBeforeUpdate = concoursRepository.findAll().size();

        // Update the concours using partial update
        Concours partialUpdatedConcours = new Concours();
        partialUpdatedConcours.setId(concours.getId());

        partialUpdatedConcours.nomEtablissement(UPDATED_NOM_ETABLISSEMENT);

        restConcoursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConcours.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConcours))
            )
            .andExpect(status().isOk());

        // Validate the Concours in the database
        List<Concours> concoursList = concoursRepository.findAll();
        assertThat(concoursList).hasSize(databaseSizeBeforeUpdate);
        Concours testConcours = concoursList.get(concoursList.size() - 1);
        assertThat(testConcours.getNomConcours()).isEqualTo(DEFAULT_NOM_CONCOURS);
        assertThat(testConcours.getNomEtablissement()).isEqualTo(UPDATED_NOM_ETABLISSEMENT);
        assertThat(testConcours.getNiveauEtude()).isEqualTo(DEFAULT_NIVEAU_ETUDE);
        assertThat(testConcours.getDateOuverture()).isEqualTo(DEFAULT_DATE_OUVERTURE);
        assertThat(testConcours.getDateCloture()).isEqualTo(DEFAULT_DATE_CLOTURE);
        assertThat(testConcours.getDateConcours()).isEqualTo(DEFAULT_DATE_CONCOURS);
        assertThat(testConcours.getAffiche()).isEqualTo(DEFAULT_AFFICHE);
        assertThat(testConcours.getAfficheContentType()).isEqualTo(DEFAULT_AFFICHE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateConcoursWithPatch() throws Exception {
        // Initialize the database
        concoursRepository.saveAndFlush(concours);

        int databaseSizeBeforeUpdate = concoursRepository.findAll().size();

        // Update the concours using partial update
        Concours partialUpdatedConcours = new Concours();
        partialUpdatedConcours.setId(concours.getId());

        partialUpdatedConcours
            .nomConcours(UPDATED_NOM_CONCOURS)
            .nomEtablissement(UPDATED_NOM_ETABLISSEMENT)
            .niveauEtude(UPDATED_NIVEAU_ETUDE)
            .dateOuverture(UPDATED_DATE_OUVERTURE)
            .dateCloture(UPDATED_DATE_CLOTURE)
            .dateConcours(UPDATED_DATE_CONCOURS)
            .affiche(UPDATED_AFFICHE)
            .afficheContentType(UPDATED_AFFICHE_CONTENT_TYPE);

        restConcoursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConcours.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConcours))
            )
            .andExpect(status().isOk());

        // Validate the Concours in the database
        List<Concours> concoursList = concoursRepository.findAll();
        assertThat(concoursList).hasSize(databaseSizeBeforeUpdate);
        Concours testConcours = concoursList.get(concoursList.size() - 1);
        assertThat(testConcours.getNomConcours()).isEqualTo(UPDATED_NOM_CONCOURS);
        assertThat(testConcours.getNomEtablissement()).isEqualTo(UPDATED_NOM_ETABLISSEMENT);
        assertThat(testConcours.getNiveauEtude()).isEqualTo(UPDATED_NIVEAU_ETUDE);
        assertThat(testConcours.getDateOuverture()).isEqualTo(UPDATED_DATE_OUVERTURE);
        assertThat(testConcours.getDateCloture()).isEqualTo(UPDATED_DATE_CLOTURE);
        assertThat(testConcours.getDateConcours()).isEqualTo(UPDATED_DATE_CONCOURS);
        assertThat(testConcours.getAffiche()).isEqualTo(UPDATED_AFFICHE);
        assertThat(testConcours.getAfficheContentType()).isEqualTo(UPDATED_AFFICHE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingConcours() throws Exception {
        int databaseSizeBeforeUpdate = concoursRepository.findAll().size();
        concours.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConcoursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, concours.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(concours))
            )
            .andExpect(status().isBadRequest());

        // Validate the Concours in the database
        List<Concours> concoursList = concoursRepository.findAll();
        assertThat(concoursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConcours() throws Exception {
        int databaseSizeBeforeUpdate = concoursRepository.findAll().size();
        concours.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConcoursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(concours))
            )
            .andExpect(status().isBadRequest());

        // Validate the Concours in the database
        List<Concours> concoursList = concoursRepository.findAll();
        assertThat(concoursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConcours() throws Exception {
        int databaseSizeBeforeUpdate = concoursRepository.findAll().size();
        concours.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConcoursMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(concours)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Concours in the database
        List<Concours> concoursList = concoursRepository.findAll();
        assertThat(concoursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConcours() throws Exception {
        // Initialize the database
        concoursRepository.saveAndFlush(concours);

        int databaseSizeBeforeDelete = concoursRepository.findAll().size();

        // Delete the concours
        restConcoursMockMvc
            .perform(delete(ENTITY_API_URL_ID, concours.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Concours> concoursList = concoursRepository.findAll();
        assertThat(concoursList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
