package mfpai.gouv.sn.service;

import java.util.Optional;
import mfpai.gouv.sn.domain.CandidatureProf;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link CandidatureProf}.
 */
public interface CandidatureProfService {
    /**
     * Save a candidatureProf.
     *
     * @param candidatureProf the entity to save.
     * @return the persisted entity.
     */
    CandidatureProf save(CandidatureProf candidatureProf);

    /**
     * Partially updates a candidatureProf.
     *
     * @param candidatureProf the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CandidatureProf> partialUpdate(CandidatureProf candidatureProf);

    /**
     * Get all the candidatureProfs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CandidatureProf> findAll(Pageable pageable);

    /**
     * Get all the candidatureProfs with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CandidatureProf> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" candidatureProf.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CandidatureProf> findOne(Long id);

    /**
     * Delete the "id" candidatureProf.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
