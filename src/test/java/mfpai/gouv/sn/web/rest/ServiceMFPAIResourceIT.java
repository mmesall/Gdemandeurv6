package mfpai.gouv.sn.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import mfpai.gouv.sn.IntegrationTest;
import mfpai.gouv.sn.domain.ServiceMFPAI;
import mfpai.gouv.sn.repository.ServiceMFPAIRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ServiceMFPAIResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ServiceMFPAIResourceIT {

    private static final byte[] DEFAULT_IMAGE_SERVICE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE_SERVICE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_SERVICE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_SERVICE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_NOM_SERVICE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_SERVICE = "BBBBBBBBBB";

    private static final String DEFAULT_CHEF_SERVICE = "AAAAAAAAAA";
    private static final String UPDATED_CHEF_SERVICE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/service-mfpais";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ServiceMFPAIRepository serviceMFPAIRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restServiceMFPAIMockMvc;

    private ServiceMFPAI serviceMFPAI;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceMFPAI createEntity(EntityManager em) {
        ServiceMFPAI serviceMFPAI = new ServiceMFPAI()
            .imageService(DEFAULT_IMAGE_SERVICE)
            .imageServiceContentType(DEFAULT_IMAGE_SERVICE_CONTENT_TYPE)
            .nomService(DEFAULT_NOM_SERVICE)
            .chefService(DEFAULT_CHEF_SERVICE)
            .description(DEFAULT_DESCRIPTION);
        return serviceMFPAI;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceMFPAI createUpdatedEntity(EntityManager em) {
        ServiceMFPAI serviceMFPAI = new ServiceMFPAI()
            .imageService(UPDATED_IMAGE_SERVICE)
            .imageServiceContentType(UPDATED_IMAGE_SERVICE_CONTENT_TYPE)
            .nomService(UPDATED_NOM_SERVICE)
            .chefService(UPDATED_CHEF_SERVICE)
            .description(UPDATED_DESCRIPTION);
        return serviceMFPAI;
    }

    @BeforeEach
    public void initTest() {
        serviceMFPAI = createEntity(em);
    }

    @Test
    @Transactional
    void createServiceMFPAI() throws Exception {
        int databaseSizeBeforeCreate = serviceMFPAIRepository.findAll().size();
        // Create the ServiceMFPAI
        restServiceMFPAIMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(serviceMFPAI)))
            .andExpect(status().isCreated());

        // Validate the ServiceMFPAI in the database
        List<ServiceMFPAI> serviceMFPAIList = serviceMFPAIRepository.findAll();
        assertThat(serviceMFPAIList).hasSize(databaseSizeBeforeCreate + 1);
        ServiceMFPAI testServiceMFPAI = serviceMFPAIList.get(serviceMFPAIList.size() - 1);
        assertThat(testServiceMFPAI.getImageService()).isEqualTo(DEFAULT_IMAGE_SERVICE);
        assertThat(testServiceMFPAI.getImageServiceContentType()).isEqualTo(DEFAULT_IMAGE_SERVICE_CONTENT_TYPE);
        assertThat(testServiceMFPAI.getNomService()).isEqualTo(DEFAULT_NOM_SERVICE);
        assertThat(testServiceMFPAI.getChefService()).isEqualTo(DEFAULT_CHEF_SERVICE);
        assertThat(testServiceMFPAI.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createServiceMFPAIWithExistingId() throws Exception {
        // Create the ServiceMFPAI with an existing ID
        serviceMFPAI.setId(1L);

        int databaseSizeBeforeCreate = serviceMFPAIRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceMFPAIMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(serviceMFPAI)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceMFPAI in the database
        List<ServiceMFPAI> serviceMFPAIList = serviceMFPAIRepository.findAll();
        assertThat(serviceMFPAIList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkChefServiceIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceMFPAIRepository.findAll().size();
        // set the field null
        serviceMFPAI.setChefService(null);

        // Create the ServiceMFPAI, which fails.

        restServiceMFPAIMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(serviceMFPAI)))
            .andExpect(status().isBadRequest());

        List<ServiceMFPAI> serviceMFPAIList = serviceMFPAIRepository.findAll();
        assertThat(serviceMFPAIList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllServiceMFPAIS() throws Exception {
        // Initialize the database
        serviceMFPAIRepository.saveAndFlush(serviceMFPAI);

        // Get all the serviceMFPAIList
        restServiceMFPAIMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceMFPAI.getId().intValue())))
            .andExpect(jsonPath("$.[*].imageServiceContentType").value(hasItem(DEFAULT_IMAGE_SERVICE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imageService").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE_SERVICE))))
            .andExpect(jsonPath("$.[*].nomService").value(hasItem(DEFAULT_NOM_SERVICE)))
            .andExpect(jsonPath("$.[*].chefService").value(hasItem(DEFAULT_CHEF_SERVICE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    void getServiceMFPAI() throws Exception {
        // Initialize the database
        serviceMFPAIRepository.saveAndFlush(serviceMFPAI);

        // Get the serviceMFPAI
        restServiceMFPAIMockMvc
            .perform(get(ENTITY_API_URL_ID, serviceMFPAI.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(serviceMFPAI.getId().intValue()))
            .andExpect(jsonPath("$.imageServiceContentType").value(DEFAULT_IMAGE_SERVICE_CONTENT_TYPE))
            .andExpect(jsonPath("$.imageService").value(Base64Utils.encodeToString(DEFAULT_IMAGE_SERVICE)))
            .andExpect(jsonPath("$.nomService").value(DEFAULT_NOM_SERVICE))
            .andExpect(jsonPath("$.chefService").value(DEFAULT_CHEF_SERVICE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingServiceMFPAI() throws Exception {
        // Get the serviceMFPAI
        restServiceMFPAIMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewServiceMFPAI() throws Exception {
        // Initialize the database
        serviceMFPAIRepository.saveAndFlush(serviceMFPAI);

        int databaseSizeBeforeUpdate = serviceMFPAIRepository.findAll().size();

        // Update the serviceMFPAI
        ServiceMFPAI updatedServiceMFPAI = serviceMFPAIRepository.findById(serviceMFPAI.getId()).get();
        // Disconnect from session so that the updates on updatedServiceMFPAI are not directly saved in db
        em.detach(updatedServiceMFPAI);
        updatedServiceMFPAI
            .imageService(UPDATED_IMAGE_SERVICE)
            .imageServiceContentType(UPDATED_IMAGE_SERVICE_CONTENT_TYPE)
            .nomService(UPDATED_NOM_SERVICE)
            .chefService(UPDATED_CHEF_SERVICE)
            .description(UPDATED_DESCRIPTION);

        restServiceMFPAIMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedServiceMFPAI.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedServiceMFPAI))
            )
            .andExpect(status().isOk());

        // Validate the ServiceMFPAI in the database
        List<ServiceMFPAI> serviceMFPAIList = serviceMFPAIRepository.findAll();
        assertThat(serviceMFPAIList).hasSize(databaseSizeBeforeUpdate);
        ServiceMFPAI testServiceMFPAI = serviceMFPAIList.get(serviceMFPAIList.size() - 1);
        assertThat(testServiceMFPAI.getImageService()).isEqualTo(UPDATED_IMAGE_SERVICE);
        assertThat(testServiceMFPAI.getImageServiceContentType()).isEqualTo(UPDATED_IMAGE_SERVICE_CONTENT_TYPE);
        assertThat(testServiceMFPAI.getNomService()).isEqualTo(UPDATED_NOM_SERVICE);
        assertThat(testServiceMFPAI.getChefService()).isEqualTo(UPDATED_CHEF_SERVICE);
        assertThat(testServiceMFPAI.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingServiceMFPAI() throws Exception {
        int databaseSizeBeforeUpdate = serviceMFPAIRepository.findAll().size();
        serviceMFPAI.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceMFPAIMockMvc
            .perform(
                put(ENTITY_API_URL_ID, serviceMFPAI.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(serviceMFPAI))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceMFPAI in the database
        List<ServiceMFPAI> serviceMFPAIList = serviceMFPAIRepository.findAll();
        assertThat(serviceMFPAIList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchServiceMFPAI() throws Exception {
        int databaseSizeBeforeUpdate = serviceMFPAIRepository.findAll().size();
        serviceMFPAI.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceMFPAIMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(serviceMFPAI))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceMFPAI in the database
        List<ServiceMFPAI> serviceMFPAIList = serviceMFPAIRepository.findAll();
        assertThat(serviceMFPAIList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamServiceMFPAI() throws Exception {
        int databaseSizeBeforeUpdate = serviceMFPAIRepository.findAll().size();
        serviceMFPAI.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceMFPAIMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(serviceMFPAI)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceMFPAI in the database
        List<ServiceMFPAI> serviceMFPAIList = serviceMFPAIRepository.findAll();
        assertThat(serviceMFPAIList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateServiceMFPAIWithPatch() throws Exception {
        // Initialize the database
        serviceMFPAIRepository.saveAndFlush(serviceMFPAI);

        int databaseSizeBeforeUpdate = serviceMFPAIRepository.findAll().size();

        // Update the serviceMFPAI using partial update
        ServiceMFPAI partialUpdatedServiceMFPAI = new ServiceMFPAI();
        partialUpdatedServiceMFPAI.setId(serviceMFPAI.getId());

        partialUpdatedServiceMFPAI
            .imageService(UPDATED_IMAGE_SERVICE)
            .imageServiceContentType(UPDATED_IMAGE_SERVICE_CONTENT_TYPE)
            .nomService(UPDATED_NOM_SERVICE)
            .chefService(UPDATED_CHEF_SERVICE);

        restServiceMFPAIMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceMFPAI.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedServiceMFPAI))
            )
            .andExpect(status().isOk());

        // Validate the ServiceMFPAI in the database
        List<ServiceMFPAI> serviceMFPAIList = serviceMFPAIRepository.findAll();
        assertThat(serviceMFPAIList).hasSize(databaseSizeBeforeUpdate);
        ServiceMFPAI testServiceMFPAI = serviceMFPAIList.get(serviceMFPAIList.size() - 1);
        assertThat(testServiceMFPAI.getImageService()).isEqualTo(UPDATED_IMAGE_SERVICE);
        assertThat(testServiceMFPAI.getImageServiceContentType()).isEqualTo(UPDATED_IMAGE_SERVICE_CONTENT_TYPE);
        assertThat(testServiceMFPAI.getNomService()).isEqualTo(UPDATED_NOM_SERVICE);
        assertThat(testServiceMFPAI.getChefService()).isEqualTo(UPDATED_CHEF_SERVICE);
        assertThat(testServiceMFPAI.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateServiceMFPAIWithPatch() throws Exception {
        // Initialize the database
        serviceMFPAIRepository.saveAndFlush(serviceMFPAI);

        int databaseSizeBeforeUpdate = serviceMFPAIRepository.findAll().size();

        // Update the serviceMFPAI using partial update
        ServiceMFPAI partialUpdatedServiceMFPAI = new ServiceMFPAI();
        partialUpdatedServiceMFPAI.setId(serviceMFPAI.getId());

        partialUpdatedServiceMFPAI
            .imageService(UPDATED_IMAGE_SERVICE)
            .imageServiceContentType(UPDATED_IMAGE_SERVICE_CONTENT_TYPE)
            .nomService(UPDATED_NOM_SERVICE)
            .chefService(UPDATED_CHEF_SERVICE)
            .description(UPDATED_DESCRIPTION);

        restServiceMFPAIMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceMFPAI.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedServiceMFPAI))
            )
            .andExpect(status().isOk());

        // Validate the ServiceMFPAI in the database
        List<ServiceMFPAI> serviceMFPAIList = serviceMFPAIRepository.findAll();
        assertThat(serviceMFPAIList).hasSize(databaseSizeBeforeUpdate);
        ServiceMFPAI testServiceMFPAI = serviceMFPAIList.get(serviceMFPAIList.size() - 1);
        assertThat(testServiceMFPAI.getImageService()).isEqualTo(UPDATED_IMAGE_SERVICE);
        assertThat(testServiceMFPAI.getImageServiceContentType()).isEqualTo(UPDATED_IMAGE_SERVICE_CONTENT_TYPE);
        assertThat(testServiceMFPAI.getNomService()).isEqualTo(UPDATED_NOM_SERVICE);
        assertThat(testServiceMFPAI.getChefService()).isEqualTo(UPDATED_CHEF_SERVICE);
        assertThat(testServiceMFPAI.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingServiceMFPAI() throws Exception {
        int databaseSizeBeforeUpdate = serviceMFPAIRepository.findAll().size();
        serviceMFPAI.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceMFPAIMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, serviceMFPAI.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(serviceMFPAI))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceMFPAI in the database
        List<ServiceMFPAI> serviceMFPAIList = serviceMFPAIRepository.findAll();
        assertThat(serviceMFPAIList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchServiceMFPAI() throws Exception {
        int databaseSizeBeforeUpdate = serviceMFPAIRepository.findAll().size();
        serviceMFPAI.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceMFPAIMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(serviceMFPAI))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceMFPAI in the database
        List<ServiceMFPAI> serviceMFPAIList = serviceMFPAIRepository.findAll();
        assertThat(serviceMFPAIList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamServiceMFPAI() throws Exception {
        int databaseSizeBeforeUpdate = serviceMFPAIRepository.findAll().size();
        serviceMFPAI.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceMFPAIMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(serviceMFPAI))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceMFPAI in the database
        List<ServiceMFPAI> serviceMFPAIList = serviceMFPAIRepository.findAll();
        assertThat(serviceMFPAIList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteServiceMFPAI() throws Exception {
        // Initialize the database
        serviceMFPAIRepository.saveAndFlush(serviceMFPAI);

        int databaseSizeBeforeDelete = serviceMFPAIRepository.findAll().size();

        // Delete the serviceMFPAI
        restServiceMFPAIMockMvc
            .perform(delete(ENTITY_API_URL_ID, serviceMFPAI.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ServiceMFPAI> serviceMFPAIList = serviceMFPAIRepository.findAll();
        assertThat(serviceMFPAIList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
