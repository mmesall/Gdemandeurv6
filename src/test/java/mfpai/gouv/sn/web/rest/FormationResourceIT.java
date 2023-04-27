package mfpai.gouv.sn.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import mfpai.gouv.sn.IntegrationTest;
import mfpai.gouv.sn.domain.Etablissement;
import mfpai.gouv.sn.domain.Formation;
import mfpai.gouv.sn.domain.enumeration.Admission;
import mfpai.gouv.sn.domain.enumeration.DiplomeRequis;
import mfpai.gouv.sn.domain.enumeration.TypeFormation;
import mfpai.gouv.sn.repository.FormationRepository;
import mfpai.gouv.sn.service.FormationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link FormationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FormationResourceIT {

    private static final String DEFAULT_NOM_FORMATION = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FORMATION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE_FORMATION = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE_FORMATION = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_FORMATION_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_FORMATION_CONTENT_TYPE = "image/png";

    private static final TypeFormation DEFAULT_TYPE_FORMATION = TypeFormation.INITIALE;
    private static final TypeFormation UPDATED_TYPE_FORMATION = TypeFormation.CONTINUE;

    private static final String DEFAULT_DUREE = "AAAAAAAAAA";
    private static final String UPDATED_DUREE = "BBBBBBBBBB";

    private static final Admission DEFAULT_ADMISSION = Admission.CONCOURS;
    private static final Admission UPDATED_ADMISSION = Admission.PC;

    private static final DiplomeRequis DEFAULT_DIPLOME_REQUIS = DiplomeRequis.ATTESTATION;
    private static final DiplomeRequis UPDATED_DIPLOME_REQUIS = DiplomeRequis.CAP;

    private static final byte[] DEFAULT_FICHE_FORMATION = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FICHE_FORMATION = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FICHE_FORMATION_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FICHE_FORMATION_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/formations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FormationRepository formationRepository;

    @Mock
    private FormationRepository formationRepositoryMock;

    @Mock
    private FormationService formationServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFormationMockMvc;

    private Formation formation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Formation createEntity(EntityManager em) {
        Formation formation = new Formation()
            .nomFormation(DEFAULT_NOM_FORMATION)
            .imageFormation(DEFAULT_IMAGE_FORMATION)
            .imageFormationContentType(DEFAULT_IMAGE_FORMATION_CONTENT_TYPE)
            .typeFormation(DEFAULT_TYPE_FORMATION)
            .duree(DEFAULT_DUREE)
            .admission(DEFAULT_ADMISSION)
            .diplomeRequis(DEFAULT_DIPLOME_REQUIS)
            .ficheFormation(DEFAULT_FICHE_FORMATION)
            .ficheFormationContentType(DEFAULT_FICHE_FORMATION_CONTENT_TYPE);
        // Add required entity
        Etablissement etablissement;
        if (TestUtil.findAll(em, Etablissement.class).isEmpty()) {
            etablissement = EtablissementResourceIT.createEntity(em);
            em.persist(etablissement);
            em.flush();
        } else {
            etablissement = TestUtil.findAll(em, Etablissement.class).get(0);
        }
        formation.getEtablissements().add(etablissement);
        return formation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Formation createUpdatedEntity(EntityManager em) {
        Formation formation = new Formation()
            .nomFormation(UPDATED_NOM_FORMATION)
            .imageFormation(UPDATED_IMAGE_FORMATION)
            .imageFormationContentType(UPDATED_IMAGE_FORMATION_CONTENT_TYPE)
            .typeFormation(UPDATED_TYPE_FORMATION)
            .duree(UPDATED_DUREE)
            .admission(UPDATED_ADMISSION)
            .diplomeRequis(UPDATED_DIPLOME_REQUIS)
            .ficheFormation(UPDATED_FICHE_FORMATION)
            .ficheFormationContentType(UPDATED_FICHE_FORMATION_CONTENT_TYPE);
        // Add required entity
        Etablissement etablissement;
        if (TestUtil.findAll(em, Etablissement.class).isEmpty()) {
            etablissement = EtablissementResourceIT.createUpdatedEntity(em);
            em.persist(etablissement);
            em.flush();
        } else {
            etablissement = TestUtil.findAll(em, Etablissement.class).get(0);
        }
        formation.getEtablissements().add(etablissement);
        return formation;
    }

    @BeforeEach
    public void initTest() {
        formation = createEntity(em);
    }

    @Test
    @Transactional
    void createFormation() throws Exception {
        int databaseSizeBeforeCreate = formationRepository.findAll().size();
        // Create the Formation
        restFormationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formation)))
            .andExpect(status().isCreated());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeCreate + 1);
        Formation testFormation = formationList.get(formationList.size() - 1);
        assertThat(testFormation.getNomFormation()).isEqualTo(DEFAULT_NOM_FORMATION);
        assertThat(testFormation.getImageFormation()).isEqualTo(DEFAULT_IMAGE_FORMATION);
        assertThat(testFormation.getImageFormationContentType()).isEqualTo(DEFAULT_IMAGE_FORMATION_CONTENT_TYPE);
        assertThat(testFormation.getTypeFormation()).isEqualTo(DEFAULT_TYPE_FORMATION);
        assertThat(testFormation.getDuree()).isEqualTo(DEFAULT_DUREE);
        assertThat(testFormation.getAdmission()).isEqualTo(DEFAULT_ADMISSION);
        assertThat(testFormation.getDiplomeRequis()).isEqualTo(DEFAULT_DIPLOME_REQUIS);
        assertThat(testFormation.getFicheFormation()).isEqualTo(DEFAULT_FICHE_FORMATION);
        assertThat(testFormation.getFicheFormationContentType()).isEqualTo(DEFAULT_FICHE_FORMATION_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createFormationWithExistingId() throws Exception {
        // Create the Formation with an existing ID
        formation.setId(1L);

        int databaseSizeBeforeCreate = formationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formation)))
            .andExpect(status().isBadRequest());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFormations() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        // Get all the formationList
        restFormationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formation.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFormation").value(hasItem(DEFAULT_NOM_FORMATION)))
            .andExpect(jsonPath("$.[*].imageFormationContentType").value(hasItem(DEFAULT_IMAGE_FORMATION_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imageFormation").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE_FORMATION))))
            .andExpect(jsonPath("$.[*].typeFormation").value(hasItem(DEFAULT_TYPE_FORMATION.toString())))
            .andExpect(jsonPath("$.[*].duree").value(hasItem(DEFAULT_DUREE)))
            .andExpect(jsonPath("$.[*].admission").value(hasItem(DEFAULT_ADMISSION.toString())))
            .andExpect(jsonPath("$.[*].diplomeRequis").value(hasItem(DEFAULT_DIPLOME_REQUIS.toString())))
            .andExpect(jsonPath("$.[*].ficheFormationContentType").value(hasItem(DEFAULT_FICHE_FORMATION_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].ficheFormation").value(hasItem(Base64Utils.encodeToString(DEFAULT_FICHE_FORMATION))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFormationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(formationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFormationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(formationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFormationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(formationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFormationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(formationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getFormation() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        // Get the formation
        restFormationMockMvc
            .perform(get(ENTITY_API_URL_ID, formation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(formation.getId().intValue()))
            .andExpect(jsonPath("$.nomFormation").value(DEFAULT_NOM_FORMATION))
            .andExpect(jsonPath("$.imageFormationContentType").value(DEFAULT_IMAGE_FORMATION_CONTENT_TYPE))
            .andExpect(jsonPath("$.imageFormation").value(Base64Utils.encodeToString(DEFAULT_IMAGE_FORMATION)))
            .andExpect(jsonPath("$.typeFormation").value(DEFAULT_TYPE_FORMATION.toString()))
            .andExpect(jsonPath("$.duree").value(DEFAULT_DUREE))
            .andExpect(jsonPath("$.admission").value(DEFAULT_ADMISSION.toString()))
            .andExpect(jsonPath("$.diplomeRequis").value(DEFAULT_DIPLOME_REQUIS.toString()))
            .andExpect(jsonPath("$.ficheFormationContentType").value(DEFAULT_FICHE_FORMATION_CONTENT_TYPE))
            .andExpect(jsonPath("$.ficheFormation").value(Base64Utils.encodeToString(DEFAULT_FICHE_FORMATION)));
    }

    @Test
    @Transactional
    void getNonExistingFormation() throws Exception {
        // Get the formation
        restFormationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFormation() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        int databaseSizeBeforeUpdate = formationRepository.findAll().size();

        // Update the formation
        Formation updatedFormation = formationRepository.findById(formation.getId()).get();
        // Disconnect from session so that the updates on updatedFormation are not directly saved in db
        em.detach(updatedFormation);
        updatedFormation
            .nomFormation(UPDATED_NOM_FORMATION)
            .imageFormation(UPDATED_IMAGE_FORMATION)
            .imageFormationContentType(UPDATED_IMAGE_FORMATION_CONTENT_TYPE)
            .typeFormation(UPDATED_TYPE_FORMATION)
            .duree(UPDATED_DUREE)
            .admission(UPDATED_ADMISSION)
            .diplomeRequis(UPDATED_DIPLOME_REQUIS)
            .ficheFormation(UPDATED_FICHE_FORMATION)
            .ficheFormationContentType(UPDATED_FICHE_FORMATION_CONTENT_TYPE);

        restFormationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFormation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFormation))
            )
            .andExpect(status().isOk());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
        Formation testFormation = formationList.get(formationList.size() - 1);
        assertThat(testFormation.getNomFormation()).isEqualTo(UPDATED_NOM_FORMATION);
        assertThat(testFormation.getImageFormation()).isEqualTo(UPDATED_IMAGE_FORMATION);
        assertThat(testFormation.getImageFormationContentType()).isEqualTo(UPDATED_IMAGE_FORMATION_CONTENT_TYPE);
        assertThat(testFormation.getTypeFormation()).isEqualTo(UPDATED_TYPE_FORMATION);
        assertThat(testFormation.getDuree()).isEqualTo(UPDATED_DUREE);
        assertThat(testFormation.getAdmission()).isEqualTo(UPDATED_ADMISSION);
        assertThat(testFormation.getDiplomeRequis()).isEqualTo(UPDATED_DIPLOME_REQUIS);
        assertThat(testFormation.getFicheFormation()).isEqualTo(UPDATED_FICHE_FORMATION);
        assertThat(testFormation.getFicheFormationContentType()).isEqualTo(UPDATED_FICHE_FORMATION_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingFormation() throws Exception {
        int databaseSizeBeforeUpdate = formationRepository.findAll().size();
        formation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFormation() throws Exception {
        int databaseSizeBeforeUpdate = formationRepository.findAll().size();
        formation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFormation() throws Exception {
        int databaseSizeBeforeUpdate = formationRepository.findAll().size();
        formation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formation)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFormationWithPatch() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        int databaseSizeBeforeUpdate = formationRepository.findAll().size();

        // Update the formation using partial update
        Formation partialUpdatedFormation = new Formation();
        partialUpdatedFormation.setId(formation.getId());

        partialUpdatedFormation.admission(UPDATED_ADMISSION);

        restFormationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormation))
            )
            .andExpect(status().isOk());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
        Formation testFormation = formationList.get(formationList.size() - 1);
        assertThat(testFormation.getNomFormation()).isEqualTo(DEFAULT_NOM_FORMATION);
        assertThat(testFormation.getImageFormation()).isEqualTo(DEFAULT_IMAGE_FORMATION);
        assertThat(testFormation.getImageFormationContentType()).isEqualTo(DEFAULT_IMAGE_FORMATION_CONTENT_TYPE);
        assertThat(testFormation.getTypeFormation()).isEqualTo(DEFAULT_TYPE_FORMATION);
        assertThat(testFormation.getDuree()).isEqualTo(DEFAULT_DUREE);
        assertThat(testFormation.getAdmission()).isEqualTo(UPDATED_ADMISSION);
        assertThat(testFormation.getDiplomeRequis()).isEqualTo(DEFAULT_DIPLOME_REQUIS);
        assertThat(testFormation.getFicheFormation()).isEqualTo(DEFAULT_FICHE_FORMATION);
        assertThat(testFormation.getFicheFormationContentType()).isEqualTo(DEFAULT_FICHE_FORMATION_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateFormationWithPatch() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        int databaseSizeBeforeUpdate = formationRepository.findAll().size();

        // Update the formation using partial update
        Formation partialUpdatedFormation = new Formation();
        partialUpdatedFormation.setId(formation.getId());

        partialUpdatedFormation
            .nomFormation(UPDATED_NOM_FORMATION)
            .imageFormation(UPDATED_IMAGE_FORMATION)
            .imageFormationContentType(UPDATED_IMAGE_FORMATION_CONTENT_TYPE)
            .typeFormation(UPDATED_TYPE_FORMATION)
            .duree(UPDATED_DUREE)
            .admission(UPDATED_ADMISSION)
            .diplomeRequis(UPDATED_DIPLOME_REQUIS)
            .ficheFormation(UPDATED_FICHE_FORMATION)
            .ficheFormationContentType(UPDATED_FICHE_FORMATION_CONTENT_TYPE);

        restFormationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormation))
            )
            .andExpect(status().isOk());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
        Formation testFormation = formationList.get(formationList.size() - 1);
        assertThat(testFormation.getNomFormation()).isEqualTo(UPDATED_NOM_FORMATION);
        assertThat(testFormation.getImageFormation()).isEqualTo(UPDATED_IMAGE_FORMATION);
        assertThat(testFormation.getImageFormationContentType()).isEqualTo(UPDATED_IMAGE_FORMATION_CONTENT_TYPE);
        assertThat(testFormation.getTypeFormation()).isEqualTo(UPDATED_TYPE_FORMATION);
        assertThat(testFormation.getDuree()).isEqualTo(UPDATED_DUREE);
        assertThat(testFormation.getAdmission()).isEqualTo(UPDATED_ADMISSION);
        assertThat(testFormation.getDiplomeRequis()).isEqualTo(UPDATED_DIPLOME_REQUIS);
        assertThat(testFormation.getFicheFormation()).isEqualTo(UPDATED_FICHE_FORMATION);
        assertThat(testFormation.getFicheFormationContentType()).isEqualTo(UPDATED_FICHE_FORMATION_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingFormation() throws Exception {
        int databaseSizeBeforeUpdate = formationRepository.findAll().size();
        formation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, formation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFormation() throws Exception {
        int databaseSizeBeforeUpdate = formationRepository.findAll().size();
        formation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFormation() throws Exception {
        int databaseSizeBeforeUpdate = formationRepository.findAll().size();
        formation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(formation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFormation() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        int databaseSizeBeforeDelete = formationRepository.findAll().size();

        // Delete the formation
        restFormationMockMvc
            .perform(delete(ENTITY_API_URL_ID, formation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
