package mfpai.gouv.sn.service;

import java.util.Optional;
import mfpai.gouv.sn.domain.CandidatureE;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link CandidatureE}.
 */
public interface CandidatureEService {
    /**
     * Save a candidatureE.
     *
     * @param candidatureE the entity to save.
     * @return the persisted entity.
     */
    CandidatureE save(CandidatureE candidatureE);

    /**
     * Partially updates a candidatureE.
     *
     * @param candidatureE the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CandidatureE> partialUpdate(CandidatureE candidatureE);

    /**
     * Get all the candidatureES.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CandidatureE> findAll(Pageable pageable);

    /**
     * Get all the candidatureES with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CandidatureE> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" candidatureE.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CandidatureE> findOne(Long id);

    /**
     * Delete the "id" candidatureE.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
