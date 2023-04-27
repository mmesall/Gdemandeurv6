package mfpai.gouv.sn.service;

import java.util.Optional;
import mfpai.gouv.sn.domain.CandidatureElev;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link CandidatureElev}.
 */
public interface CandidatureElevService {
    /**
     * Save a candidatureElev.
     *
     * @param candidatureElev the entity to save.
     * @return the persisted entity.
     */
    CandidatureElev save(CandidatureElev candidatureElev);

    /**
     * Partially updates a candidatureElev.
     *
     * @param candidatureElev the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CandidatureElev> partialUpdate(CandidatureElev candidatureElev);

    /**
     * Get all the candidatureElevs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CandidatureElev> findAll(Pageable pageable);

    /**
     * Get all the candidatureElevs with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CandidatureElev> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" candidatureElev.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CandidatureElev> findOne(Long id);

    /**
     * Delete the "id" candidatureElev.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
