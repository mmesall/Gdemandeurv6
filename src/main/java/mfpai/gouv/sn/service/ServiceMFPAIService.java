package mfpai.gouv.sn.service;

import java.util.List;
import java.util.Optional;
import mfpai.gouv.sn.domain.ServiceMFPAI;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ServiceMFPAI}.
 */
public interface ServiceMFPAIService {
    /**
     * Save a serviceMFPAI.
     *
     * @param serviceMFPAI the entity to save.
     * @return the persisted entity.
     */
    ServiceMFPAI save(ServiceMFPAI serviceMFPAI);

    /**
     * Partially updates a serviceMFPAI.
     *
     * @param serviceMFPAI the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ServiceMFPAI> partialUpdate(ServiceMFPAI serviceMFPAI);

    /**
     * Get all the serviceMFPAIS.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ServiceMFPAI> findAll(Pageable pageable);
    /**
     * Get all the ServiceMFPAI where Agent is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<ServiceMFPAI> findAllWhereAgentIsNull();

    /**
     * Get the "id" serviceMFPAI.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ServiceMFPAI> findOne(Long id);

    /**
     * Delete the "id" serviceMFPAI.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
