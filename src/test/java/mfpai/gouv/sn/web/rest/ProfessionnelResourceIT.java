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
import mfpai.gouv.sn.domain.Professionnel;
import mfpai.gouv.sn.domain.enumeration.NomDepartement;
import mfpai.gouv.sn.domain.enumeration.NomRegion;
import mfpai.gouv.sn.domain.enumeration.Sexe;
import mfpai.gouv.sn.repository.ProfessionnelRepository;
import mfpai.gouv.sn.service.ProfessionnelService;
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
 * Integration tests for the {@link ProfessionnelResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProfessionnelResourceIT {

    private static final String DEFAULT_PROFESSION = "AAAAAAAAAA";
    private static final String UPDATED_PROFESSION = "BBBBBBBBBB";

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

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Long DEFAULT_CNI = 1L;
    private static final Long UPDATED_CNI = 2L;

    private static final String ENTITY_API_URL = "/api/professionnels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProfessionnelRepository professionnelRepository;

    @Mock
    private ProfessionnelRepository professionnelRepositoryMock;

    @Mock
    private ProfessionnelService professionnelServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProfessionnelMockMvc;

    private Professionnel professionnel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Professionnel createEntity(EntityManager em) {
        Professionnel professionnel = new Professionnel()
            .profession(DEFAULT_PROFESSION)
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM)
            .dateNaiss(DEFAULT_DATE_NAISS)
            .lieuNaiss(DEFAULT_LIEU_NAISS)
            .sexe(DEFAULT_SEXE)
            .telephone(DEFAULT_TELEPHONE)
            .adressePhysique(DEFAULT_ADRESSE_PHYSIQUE)
            .regionResidence(DEFAULT_REGION_RESIDENCE)
            .departResidence(DEFAULT_DEPART_RESIDENCE)
            .email(DEFAULT_EMAIL)
            .cni(DEFAULT_CNI);
        return professionnel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Professionnel createUpdatedEntity(EntityManager em) {
        Professionnel professionnel = new Professionnel()
            .profession(UPDATED_PROFESSION)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .dateNaiss(UPDATED_DATE_NAISS)
            .lieuNaiss(UPDATED_LIEU_NAISS)
            .sexe(UPDATED_SEXE)
            .telephone(UPDATED_TELEPHONE)
            .adressePhysique(UPDATED_ADRESSE_PHYSIQUE)
            .regionResidence(UPDATED_REGION_RESIDENCE)
            .departResidence(UPDATED_DEPART_RESIDENCE)
            .email(UPDATED_EMAIL)
            .cni(UPDATED_CNI);
        return professionnel;
    }

    @BeforeEach
    public void initTest() {
        professionnel = createEntity(em);
    }

    @Test
    @Transactional
    void createProfessionnel() throws Exception {
        int databaseSizeBeforeCreate = professionnelRepository.findAll().size();
        // Create the Professionnel
        restProfessionnelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(professionnel)))
            .andExpect(status().isCreated());

        // Validate the Professionnel in the database
        List<Professionnel> professionnelList = professionnelRepository.findAll();
        assertThat(professionnelList).hasSize(databaseSizeBeforeCreate + 1);
        Professionnel testProfessionnel = professionnelList.get(professionnelList.size() - 1);
        assertThat(testProfessionnel.getProfession()).isEqualTo(DEFAULT_PROFESSION);
        assertThat(testProfessionnel.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testProfessionnel.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testProfessionnel.getDateNaiss()).isEqualTo(DEFAULT_DATE_NAISS);
        assertThat(testProfessionnel.getLieuNaiss()).isEqualTo(DEFAULT_LIEU_NAISS);
        assertThat(testProfessionnel.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testProfessionnel.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testProfessionnel.getAdressePhysique()).isEqualTo(DEFAULT_ADRESSE_PHYSIQUE);
        assertThat(testProfessionnel.getRegionResidence()).isEqualTo(DEFAULT_REGION_RESIDENCE);
        assertThat(testProfessionnel.getDepartResidence()).isEqualTo(DEFAULT_DEPART_RESIDENCE);
        assertThat(testProfessionnel.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testProfessionnel.getCni()).isEqualTo(DEFAULT_CNI);
    }

    @Test
    @Transactional
    void createProfessionnelWithExistingId() throws Exception {
        // Create the Professionnel with an existing ID
        professionnel.setId(1L);

        int databaseSizeBeforeCreate = professionnelRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfessionnelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(professionnel)))
            .andExpect(status().isBadRequest());

        // Validate the Professionnel in the database
        List<Professionnel> professionnelList = professionnelRepository.findAll();
        assertThat(professionnelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkProfessionIsRequired() throws Exception {
        int databaseSizeBeforeTest = professionnelRepository.findAll().size();
        // set the field null
        professionnel.setProfession(null);

        // Create the Professionnel, which fails.

        restProfessionnelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(professionnel)))
            .andExpect(status().isBadRequest());

        List<Professionnel> professionnelList = professionnelRepository.findAll();
        assertThat(professionnelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = professionnelRepository.findAll().size();
        // set the field null
        professionnel.setNom(null);

        // Create the Professionnel, which fails.

        restProfessionnelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(professionnel)))
            .andExpect(status().isBadRequest());

        List<Professionnel> professionnelList = professionnelRepository.findAll();
        assertThat(professionnelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrenomIsRequired() throws Exception {
        int databaseSizeBeforeTest = professionnelRepository.findAll().size();
        // set the field null
        professionnel.setPrenom(null);

        // Create the Professionnel, which fails.

        restProfessionnelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(professionnel)))
            .andExpect(status().isBadRequest());

        List<Professionnel> professionnelList = professionnelRepository.findAll();
        assertThat(professionnelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCniIsRequired() throws Exception {
        int databaseSizeBeforeTest = professionnelRepository.findAll().size();
        // set the field null
        professionnel.setCni(null);

        // Create the Professionnel, which fails.

        restProfessionnelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(professionnel)))
            .andExpect(status().isBadRequest());

        List<Professionnel> professionnelList = professionnelRepository.findAll();
        assertThat(professionnelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProfessionnels() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get all the professionnelList
        restProfessionnelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(professionnel.getId().intValue())))
            .andExpect(jsonPath("$.[*].profession").value(hasItem(DEFAULT_PROFESSION)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].dateNaiss").value(hasItem(DEFAULT_DATE_NAISS.toString())))
            .andExpect(jsonPath("$.[*].lieuNaiss").value(hasItem(DEFAULT_LIEU_NAISS)))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE.intValue())))
            .andExpect(jsonPath("$.[*].adressePhysique").value(hasItem(DEFAULT_ADRESSE_PHYSIQUE)))
            .andExpect(jsonPath("$.[*].regionResidence").value(hasItem(DEFAULT_REGION_RESIDENCE.toString())))
            .andExpect(jsonPath("$.[*].departResidence").value(hasItem(DEFAULT_DEPART_RESIDENCE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].cni").value(hasItem(DEFAULT_CNI.intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProfessionnelsWithEagerRelationshipsIsEnabled() throws Exception {
        when(professionnelServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProfessionnelMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(professionnelServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProfessionnelsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(professionnelServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProfessionnelMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(professionnelRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProfessionnel() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        // Get the professionnel
        restProfessionnelMockMvc
            .perform(get(ENTITY_API_URL_ID, professionnel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(professionnel.getId().intValue()))
            .andExpect(jsonPath("$.profession").value(DEFAULT_PROFESSION))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.dateNaiss").value(DEFAULT_DATE_NAISS.toString()))
            .andExpect(jsonPath("$.lieuNaiss").value(DEFAULT_LIEU_NAISS))
            .andExpect(jsonPath("$.sexe").value(DEFAULT_SEXE.toString()))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE.intValue()))
            .andExpect(jsonPath("$.adressePhysique").value(DEFAULT_ADRESSE_PHYSIQUE))
            .andExpect(jsonPath("$.regionResidence").value(DEFAULT_REGION_RESIDENCE.toString()))
            .andExpect(jsonPath("$.departResidence").value(DEFAULT_DEPART_RESIDENCE.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.cni").value(DEFAULT_CNI.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingProfessionnel() throws Exception {
        // Get the professionnel
        restProfessionnelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProfessionnel() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        int databaseSizeBeforeUpdate = professionnelRepository.findAll().size();

        // Update the professionnel
        Professionnel updatedProfessionnel = professionnelRepository.findById(professionnel.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProfessionnel are not directly saved in db
        em.detach(updatedProfessionnel);
        updatedProfessionnel
            .profession(UPDATED_PROFESSION)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .dateNaiss(UPDATED_DATE_NAISS)
            .lieuNaiss(UPDATED_LIEU_NAISS)
            .sexe(UPDATED_SEXE)
            .telephone(UPDATED_TELEPHONE)
            .adressePhysique(UPDATED_ADRESSE_PHYSIQUE)
            .regionResidence(UPDATED_REGION_RESIDENCE)
            .departResidence(UPDATED_DEPART_RESIDENCE)
            .email(UPDATED_EMAIL)
            .cni(UPDATED_CNI);

        restProfessionnelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProfessionnel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProfessionnel))
            )
            .andExpect(status().isOk());

        // Validate the Professionnel in the database
        List<Professionnel> professionnelList = professionnelRepository.findAll();
        assertThat(professionnelList).hasSize(databaseSizeBeforeUpdate);
        Professionnel testProfessionnel = professionnelList.get(professionnelList.size() - 1);
        assertThat(testProfessionnel.getProfession()).isEqualTo(UPDATED_PROFESSION);
        assertThat(testProfessionnel.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testProfessionnel.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testProfessionnel.getDateNaiss()).isEqualTo(UPDATED_DATE_NAISS);
        assertThat(testProfessionnel.getLieuNaiss()).isEqualTo(UPDATED_LIEU_NAISS);
        assertThat(testProfessionnel.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testProfessionnel.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testProfessionnel.getAdressePhysique()).isEqualTo(UPDATED_ADRESSE_PHYSIQUE);
        assertThat(testProfessionnel.getRegionResidence()).isEqualTo(UPDATED_REGION_RESIDENCE);
        assertThat(testProfessionnel.getDepartResidence()).isEqualTo(UPDATED_DEPART_RESIDENCE);
        assertThat(testProfessionnel.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testProfessionnel.getCni()).isEqualTo(UPDATED_CNI);
    }

    @Test
    @Transactional
    void putNonExistingProfessionnel() throws Exception {
        int databaseSizeBeforeUpdate = professionnelRepository.findAll().size();
        professionnel.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfessionnelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, professionnel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(professionnel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Professionnel in the database
        List<Professionnel> professionnelList = professionnelRepository.findAll();
        assertThat(professionnelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProfessionnel() throws Exception {
        int databaseSizeBeforeUpdate = professionnelRepository.findAll().size();
        professionnel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionnelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(professionnel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Professionnel in the database
        List<Professionnel> professionnelList = professionnelRepository.findAll();
        assertThat(professionnelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProfessionnel() throws Exception {
        int databaseSizeBeforeUpdate = professionnelRepository.findAll().size();
        professionnel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionnelMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(professionnel)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Professionnel in the database
        List<Professionnel> professionnelList = professionnelRepository.findAll();
        assertThat(professionnelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProfessionnelWithPatch() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        int databaseSizeBeforeUpdate = professionnelRepository.findAll().size();

        // Update the professionnel using partial update
        Professionnel partialUpdatedProfessionnel = new Professionnel();
        partialUpdatedProfessionnel.setId(professionnel.getId());

        partialUpdatedProfessionnel
            .profession(UPDATED_PROFESSION)
            .nom(UPDATED_NOM)
            .sexe(UPDATED_SEXE)
            .telephone(UPDATED_TELEPHONE)
            .adressePhysique(UPDATED_ADRESSE_PHYSIQUE)
            .regionResidence(UPDATED_REGION_RESIDENCE)
            .departResidence(UPDATED_DEPART_RESIDENCE);

        restProfessionnelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfessionnel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProfessionnel))
            )
            .andExpect(status().isOk());

        // Validate the Professionnel in the database
        List<Professionnel> professionnelList = professionnelRepository.findAll();
        assertThat(professionnelList).hasSize(databaseSizeBeforeUpdate);
        Professionnel testProfessionnel = professionnelList.get(professionnelList.size() - 1);
        assertThat(testProfessionnel.getProfession()).isEqualTo(UPDATED_PROFESSION);
        assertThat(testProfessionnel.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testProfessionnel.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testProfessionnel.getDateNaiss()).isEqualTo(DEFAULT_DATE_NAISS);
        assertThat(testProfessionnel.getLieuNaiss()).isEqualTo(DEFAULT_LIEU_NAISS);
        assertThat(testProfessionnel.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testProfessionnel.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testProfessionnel.getAdressePhysique()).isEqualTo(UPDATED_ADRESSE_PHYSIQUE);
        assertThat(testProfessionnel.getRegionResidence()).isEqualTo(UPDATED_REGION_RESIDENCE);
        assertThat(testProfessionnel.getDepartResidence()).isEqualTo(UPDATED_DEPART_RESIDENCE);
        assertThat(testProfessionnel.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testProfessionnel.getCni()).isEqualTo(DEFAULT_CNI);
    }

    @Test
    @Transactional
    void fullUpdateProfessionnelWithPatch() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        int databaseSizeBeforeUpdate = professionnelRepository.findAll().size();

        // Update the professionnel using partial update
        Professionnel partialUpdatedProfessionnel = new Professionnel();
        partialUpdatedProfessionnel.setId(professionnel.getId());

        partialUpdatedProfessionnel
            .profession(UPDATED_PROFESSION)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .dateNaiss(UPDATED_DATE_NAISS)
            .lieuNaiss(UPDATED_LIEU_NAISS)
            .sexe(UPDATED_SEXE)
            .telephone(UPDATED_TELEPHONE)
            .adressePhysique(UPDATED_ADRESSE_PHYSIQUE)
            .regionResidence(UPDATED_REGION_RESIDENCE)
            .departResidence(UPDATED_DEPART_RESIDENCE)
            .email(UPDATED_EMAIL)
            .cni(UPDATED_CNI);

        restProfessionnelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfessionnel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProfessionnel))
            )
            .andExpect(status().isOk());

        // Validate the Professionnel in the database
        List<Professionnel> professionnelList = professionnelRepository.findAll();
        assertThat(professionnelList).hasSize(databaseSizeBeforeUpdate);
        Professionnel testProfessionnel = professionnelList.get(professionnelList.size() - 1);
        assertThat(testProfessionnel.getProfession()).isEqualTo(UPDATED_PROFESSION);
        assertThat(testProfessionnel.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testProfessionnel.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testProfessionnel.getDateNaiss()).isEqualTo(UPDATED_DATE_NAISS);
        assertThat(testProfessionnel.getLieuNaiss()).isEqualTo(UPDATED_LIEU_NAISS);
        assertThat(testProfessionnel.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testProfessionnel.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testProfessionnel.getAdressePhysique()).isEqualTo(UPDATED_ADRESSE_PHYSIQUE);
        assertThat(testProfessionnel.getRegionResidence()).isEqualTo(UPDATED_REGION_RESIDENCE);
        assertThat(testProfessionnel.getDepartResidence()).isEqualTo(UPDATED_DEPART_RESIDENCE);
        assertThat(testProfessionnel.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testProfessionnel.getCni()).isEqualTo(UPDATED_CNI);
    }

    @Test
    @Transactional
    void patchNonExistingProfessionnel() throws Exception {
        int databaseSizeBeforeUpdate = professionnelRepository.findAll().size();
        professionnel.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfessionnelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, professionnel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(professionnel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Professionnel in the database
        List<Professionnel> professionnelList = professionnelRepository.findAll();
        assertThat(professionnelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProfessionnel() throws Exception {
        int databaseSizeBeforeUpdate = professionnelRepository.findAll().size();
        professionnel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionnelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(professionnel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Professionnel in the database
        List<Professionnel> professionnelList = professionnelRepository.findAll();
        assertThat(professionnelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProfessionnel() throws Exception {
        int databaseSizeBeforeUpdate = professionnelRepository.findAll().size();
        professionnel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionnelMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(professionnel))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Professionnel in the database
        List<Professionnel> professionnelList = professionnelRepository.findAll();
        assertThat(professionnelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProfessionnel() throws Exception {
        // Initialize the database
        professionnelRepository.saveAndFlush(professionnel);

        int databaseSizeBeforeDelete = professionnelRepository.findAll().size();

        // Delete the professionnel
        restProfessionnelMockMvc
            .perform(delete(ENTITY_API_URL_ID, professionnel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Professionnel> professionnelList = professionnelRepository.findAll();
        assertThat(professionnelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
