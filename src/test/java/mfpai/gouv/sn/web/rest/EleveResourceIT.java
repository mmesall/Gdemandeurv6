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
import mfpai.gouv.sn.domain.Eleve;
import mfpai.gouv.sn.domain.enumeration.NiveauEtude;
import mfpai.gouv.sn.domain.enumeration.NomDepartement;
import mfpai.gouv.sn.domain.enumeration.NomRegion;
import mfpai.gouv.sn.domain.enumeration.Sexe;
import mfpai.gouv.sn.repository.EleveRepository;
import mfpai.gouv.sn.service.EleveService;
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
 * Integration tests for the {@link EleveResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EleveResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_NAISS = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_NAISS = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_LIEU_NAISS = "AAAAAAAAAA";
    private static final String UPDATED_LIEU_NAISS = "BBBBBBBBBB";

    private static final Sexe DEFAULT_SEXE = Sexe.HOMME;
    private static final Sexe UPDATED_SEXE = Sexe.FEMME;

    private static final Long DEFAULT_TELEPHONE = 1L;
    private static final Long UPDATED_TELEPHONE = 2L;

    private static final String DEFAULT_ADRESSE_PHYSIQUE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE_PHYSIQUE = "BBBBBBBBBB";

    private static final NomRegion DEFAULT_REGION_RESIDENCE = NomRegion.DAKAR;
    private static final NomRegion UPDATED_REGION_RESIDENCE = NomRegion.DIOURBEL;

    private static final NomDepartement DEFAULT_DEPART_RESIDENCE = NomDepartement.DAKAR;
    private static final NomDepartement UPDATED_DEPART_RESIDENCE = NomDepartement.GUEDIAWAYE;

    private static final NiveauEtude DEFAULT_NIVEAU_ETUDE = NiveauEtude.CINQUIEME;
    private static final NiveauEtude UPDATED_NIVEAU_ETUDE = NiveauEtude.QUATRIEME;

    private static final Long DEFAULT_CNI = 1L;
    private static final Long UPDATED_CNI = 2L;

    private static final String ENTITY_API_URL = "/api/eleves";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EleveRepository eleveRepository;

    @Mock
    private EleveRepository eleveRepositoryMock;

    @Mock
    private EleveService eleveServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEleveMockMvc;

    private Eleve eleve;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Eleve createEntity(EntityManager em) {
        Eleve eleve = new Eleve()
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM)
            .dateNaiss(DEFAULT_DATE_NAISS)
            .lieuNaiss(DEFAULT_LIEU_NAISS)
            .sexe(DEFAULT_SEXE)
            .telephone(DEFAULT_TELEPHONE)
            .adressePhysique(DEFAULT_ADRESSE_PHYSIQUE)
            .regionResidence(DEFAULT_REGION_RESIDENCE)
            .departResidence(DEFAULT_DEPART_RESIDENCE)
            .niveauEtude(DEFAULT_NIVEAU_ETUDE)
            .cni(DEFAULT_CNI);
        return eleve;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Eleve createUpdatedEntity(EntityManager em) {
        Eleve eleve = new Eleve()
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .dateNaiss(UPDATED_DATE_NAISS)
            .lieuNaiss(UPDATED_LIEU_NAISS)
            .sexe(UPDATED_SEXE)
            .telephone(UPDATED_TELEPHONE)
            .adressePhysique(UPDATED_ADRESSE_PHYSIQUE)
            .regionResidence(UPDATED_REGION_RESIDENCE)
            .departResidence(UPDATED_DEPART_RESIDENCE)
            .niveauEtude(UPDATED_NIVEAU_ETUDE)
            .cni(UPDATED_CNI);
        return eleve;
    }

    @BeforeEach
    public void initTest() {
        eleve = createEntity(em);
    }

    @Test
    @Transactional
    void createEleve() throws Exception {
        int databaseSizeBeforeCreate = eleveRepository.findAll().size();
        // Create the Eleve
        restEleveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eleve)))
            .andExpect(status().isCreated());

        // Validate the Eleve in the database
        List<Eleve> eleveList = eleveRepository.findAll();
        assertThat(eleveList).hasSize(databaseSizeBeforeCreate + 1);
        Eleve testEleve = eleveList.get(eleveList.size() - 1);
        assertThat(testEleve.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testEleve.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testEleve.getDateNaiss()).isEqualTo(DEFAULT_DATE_NAISS);
        assertThat(testEleve.getLieuNaiss()).isEqualTo(DEFAULT_LIEU_NAISS);
        assertThat(testEleve.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testEleve.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testEleve.getAdressePhysique()).isEqualTo(DEFAULT_ADRESSE_PHYSIQUE);
        assertThat(testEleve.getRegionResidence()).isEqualTo(DEFAULT_REGION_RESIDENCE);
        assertThat(testEleve.getDepartResidence()).isEqualTo(DEFAULT_DEPART_RESIDENCE);
        assertThat(testEleve.getNiveauEtude()).isEqualTo(DEFAULT_NIVEAU_ETUDE);
        assertThat(testEleve.getCni()).isEqualTo(DEFAULT_CNI);
    }

    @Test
    @Transactional
    void createEleveWithExistingId() throws Exception {
        // Create the Eleve with an existing ID
        eleve.setId(1L);

        int databaseSizeBeforeCreate = eleveRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEleveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eleve)))
            .andExpect(status().isBadRequest());

        // Validate the Eleve in the database
        List<Eleve> eleveList = eleveRepository.findAll();
        assertThat(eleveList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = eleveRepository.findAll().size();
        // set the field null
        eleve.setNom(null);

        // Create the Eleve, which fails.

        restEleveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eleve)))
            .andExpect(status().isBadRequest());

        List<Eleve> eleveList = eleveRepository.findAll();
        assertThat(eleveList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrenomIsRequired() throws Exception {
        int databaseSizeBeforeTest = eleveRepository.findAll().size();
        // set the field null
        eleve.setPrenom(null);

        // Create the Eleve, which fails.

        restEleveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eleve)))
            .andExpect(status().isBadRequest());

        List<Eleve> eleveList = eleveRepository.findAll();
        assertThat(eleveList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNiveauEtudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = eleveRepository.findAll().size();
        // set the field null
        eleve.setNiveauEtude(null);

        // Create the Eleve, which fails.

        restEleveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eleve)))
            .andExpect(status().isBadRequest());

        List<Eleve> eleveList = eleveRepository.findAll();
        assertThat(eleveList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEleves() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get all the eleveList
        restEleveMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eleve.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].dateNaiss").value(hasItem(DEFAULT_DATE_NAISS.toString())))
            .andExpect(jsonPath("$.[*].lieuNaiss").value(hasItem(DEFAULT_LIEU_NAISS)))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE.intValue())))
            .andExpect(jsonPath("$.[*].adressePhysique").value(hasItem(DEFAULT_ADRESSE_PHYSIQUE)))
            .andExpect(jsonPath("$.[*].regionResidence").value(hasItem(DEFAULT_REGION_RESIDENCE.toString())))
            .andExpect(jsonPath("$.[*].departResidence").value(hasItem(DEFAULT_DEPART_RESIDENCE.toString())))
            .andExpect(jsonPath("$.[*].niveauEtude").value(hasItem(DEFAULT_NIVEAU_ETUDE.toString())))
            .andExpect(jsonPath("$.[*].cni").value(hasItem(DEFAULT_CNI.intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllElevesWithEagerRelationshipsIsEnabled() throws Exception {
        when(eleveServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEleveMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(eleveServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllElevesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(eleveServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEleveMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(eleveRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getEleve() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        // Get the eleve
        restEleveMockMvc
            .perform(get(ENTITY_API_URL_ID, eleve.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eleve.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.dateNaiss").value(DEFAULT_DATE_NAISS.toString()))
            .andExpect(jsonPath("$.lieuNaiss").value(DEFAULT_LIEU_NAISS))
            .andExpect(jsonPath("$.sexe").value(DEFAULT_SEXE.toString()))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE.intValue()))
            .andExpect(jsonPath("$.adressePhysique").value(DEFAULT_ADRESSE_PHYSIQUE))
            .andExpect(jsonPath("$.regionResidence").value(DEFAULT_REGION_RESIDENCE.toString()))
            .andExpect(jsonPath("$.departResidence").value(DEFAULT_DEPART_RESIDENCE.toString()))
            .andExpect(jsonPath("$.niveauEtude").value(DEFAULT_NIVEAU_ETUDE.toString()))
            .andExpect(jsonPath("$.cni").value(DEFAULT_CNI.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingEleve() throws Exception {
        // Get the eleve
        restEleveMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEleve() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        int databaseSizeBeforeUpdate = eleveRepository.findAll().size();

        // Update the eleve
        Eleve updatedEleve = eleveRepository.findById(eleve.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEleve are not directly saved in db
        em.detach(updatedEleve);
        updatedEleve
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .dateNaiss(UPDATED_DATE_NAISS)
            .lieuNaiss(UPDATED_LIEU_NAISS)
            .sexe(UPDATED_SEXE)
            .telephone(UPDATED_TELEPHONE)
            .adressePhysique(UPDATED_ADRESSE_PHYSIQUE)
            .regionResidence(UPDATED_REGION_RESIDENCE)
            .departResidence(UPDATED_DEPART_RESIDENCE)
            .niveauEtude(UPDATED_NIVEAU_ETUDE)
            .cni(UPDATED_CNI);

        restEleveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEleve.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEleve))
            )
            .andExpect(status().isOk());

        // Validate the Eleve in the database
        List<Eleve> eleveList = eleveRepository.findAll();
        assertThat(eleveList).hasSize(databaseSizeBeforeUpdate);
        Eleve testEleve = eleveList.get(eleveList.size() - 1);
        assertThat(testEleve.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testEleve.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testEleve.getDateNaiss()).isEqualTo(UPDATED_DATE_NAISS);
        assertThat(testEleve.getLieuNaiss()).isEqualTo(UPDATED_LIEU_NAISS);
        assertThat(testEleve.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testEleve.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testEleve.getAdressePhysique()).isEqualTo(UPDATED_ADRESSE_PHYSIQUE);
        assertThat(testEleve.getRegionResidence()).isEqualTo(UPDATED_REGION_RESIDENCE);
        assertThat(testEleve.getDepartResidence()).isEqualTo(UPDATED_DEPART_RESIDENCE);
        assertThat(testEleve.getNiveauEtude()).isEqualTo(UPDATED_NIVEAU_ETUDE);
        assertThat(testEleve.getCni()).isEqualTo(UPDATED_CNI);
    }

    @Test
    @Transactional
    void putNonExistingEleve() throws Exception {
        int databaseSizeBeforeUpdate = eleveRepository.findAll().size();
        eleve.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEleveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eleve.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eleve))
            )
            .andExpect(status().isBadRequest());

        // Validate the Eleve in the database
        List<Eleve> eleveList = eleveRepository.findAll();
        assertThat(eleveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEleve() throws Exception {
        int databaseSizeBeforeUpdate = eleveRepository.findAll().size();
        eleve.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEleveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eleve))
            )
            .andExpect(status().isBadRequest());

        // Validate the Eleve in the database
        List<Eleve> eleveList = eleveRepository.findAll();
        assertThat(eleveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEleve() throws Exception {
        int databaseSizeBeforeUpdate = eleveRepository.findAll().size();
        eleve.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEleveMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eleve)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Eleve in the database
        List<Eleve> eleveList = eleveRepository.findAll();
        assertThat(eleveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEleveWithPatch() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        int databaseSizeBeforeUpdate = eleveRepository.findAll().size();

        // Update the eleve using partial update
        Eleve partialUpdatedEleve = new Eleve();
        partialUpdatedEleve.setId(eleve.getId());

        partialUpdatedEleve
            .nom(UPDATED_NOM)
            .dateNaiss(UPDATED_DATE_NAISS)
            .telephone(UPDATED_TELEPHONE)
            .adressePhysique(UPDATED_ADRESSE_PHYSIQUE)
            .regionResidence(UPDATED_REGION_RESIDENCE)
            .niveauEtude(UPDATED_NIVEAU_ETUDE)
            .cni(UPDATED_CNI);

        restEleveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEleve.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEleve))
            )
            .andExpect(status().isOk());

        // Validate the Eleve in the database
        List<Eleve> eleveList = eleveRepository.findAll();
        assertThat(eleveList).hasSize(databaseSizeBeforeUpdate);
        Eleve testEleve = eleveList.get(eleveList.size() - 1);
        assertThat(testEleve.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testEleve.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testEleve.getDateNaiss()).isEqualTo(UPDATED_DATE_NAISS);
        assertThat(testEleve.getLieuNaiss()).isEqualTo(DEFAULT_LIEU_NAISS);
        assertThat(testEleve.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testEleve.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testEleve.getAdressePhysique()).isEqualTo(UPDATED_ADRESSE_PHYSIQUE);
        assertThat(testEleve.getRegionResidence()).isEqualTo(UPDATED_REGION_RESIDENCE);
        assertThat(testEleve.getDepartResidence()).isEqualTo(DEFAULT_DEPART_RESIDENCE);
        assertThat(testEleve.getNiveauEtude()).isEqualTo(UPDATED_NIVEAU_ETUDE);
        assertThat(testEleve.getCni()).isEqualTo(UPDATED_CNI);
    }

    @Test
    @Transactional
    void fullUpdateEleveWithPatch() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        int databaseSizeBeforeUpdate = eleveRepository.findAll().size();

        // Update the eleve using partial update
        Eleve partialUpdatedEleve = new Eleve();
        partialUpdatedEleve.setId(eleve.getId());

        partialUpdatedEleve
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .dateNaiss(UPDATED_DATE_NAISS)
            .lieuNaiss(UPDATED_LIEU_NAISS)
            .sexe(UPDATED_SEXE)
            .telephone(UPDATED_TELEPHONE)
            .adressePhysique(UPDATED_ADRESSE_PHYSIQUE)
            .regionResidence(UPDATED_REGION_RESIDENCE)
            .departResidence(UPDATED_DEPART_RESIDENCE)
            .niveauEtude(UPDATED_NIVEAU_ETUDE)
            .cni(UPDATED_CNI);

        restEleveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEleve.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEleve))
            )
            .andExpect(status().isOk());

        // Validate the Eleve in the database
        List<Eleve> eleveList = eleveRepository.findAll();
        assertThat(eleveList).hasSize(databaseSizeBeforeUpdate);
        Eleve testEleve = eleveList.get(eleveList.size() - 1);
        assertThat(testEleve.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testEleve.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testEleve.getDateNaiss()).isEqualTo(UPDATED_DATE_NAISS);
        assertThat(testEleve.getLieuNaiss()).isEqualTo(UPDATED_LIEU_NAISS);
        assertThat(testEleve.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testEleve.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testEleve.getAdressePhysique()).isEqualTo(UPDATED_ADRESSE_PHYSIQUE);
        assertThat(testEleve.getRegionResidence()).isEqualTo(UPDATED_REGION_RESIDENCE);
        assertThat(testEleve.getDepartResidence()).isEqualTo(UPDATED_DEPART_RESIDENCE);
        assertThat(testEleve.getNiveauEtude()).isEqualTo(UPDATED_NIVEAU_ETUDE);
        assertThat(testEleve.getCni()).isEqualTo(UPDATED_CNI);
    }

    @Test
    @Transactional
    void patchNonExistingEleve() throws Exception {
        int databaseSizeBeforeUpdate = eleveRepository.findAll().size();
        eleve.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEleveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eleve.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eleve))
            )
            .andExpect(status().isBadRequest());

        // Validate the Eleve in the database
        List<Eleve> eleveList = eleveRepository.findAll();
        assertThat(eleveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEleve() throws Exception {
        int databaseSizeBeforeUpdate = eleveRepository.findAll().size();
        eleve.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEleveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eleve))
            )
            .andExpect(status().isBadRequest());

        // Validate the Eleve in the database
        List<Eleve> eleveList = eleveRepository.findAll();
        assertThat(eleveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEleve() throws Exception {
        int databaseSizeBeforeUpdate = eleveRepository.findAll().size();
        eleve.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEleveMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(eleve)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Eleve in the database
        List<Eleve> eleveList = eleveRepository.findAll();
        assertThat(eleveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEleve() throws Exception {
        // Initialize the database
        eleveRepository.saveAndFlush(eleve);

        int databaseSizeBeforeDelete = eleveRepository.findAll().size();

        // Delete the eleve
        restEleveMockMvc
            .perform(delete(ENTITY_API_URL_ID, eleve.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Eleve> eleveList = eleveRepository.findAll();
        assertThat(eleveList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
