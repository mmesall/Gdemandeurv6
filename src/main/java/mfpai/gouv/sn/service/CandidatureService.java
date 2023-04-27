package mfpai.gouv.sn.service;

import java.util.Optional;
import mfpai.gouv.sn.domain.Candidature;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Candidature}.
 */
public interface CandidatureService {
    /**
     * Save a candidature.
     *
     * @param candidature the entity to save.
     * @return the persisted entity.
     */
    Candidature save(Candidature candidature);

    /**
     * Partially updates a candidature.
     *
     * @param candidature the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Candidature> partialUpdate(Candidature candidature);

    /**
     * Get all the candidatures.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Candidature> findAll(Pageable pageable);

    /**
     * Get all the candidatures with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Candidature> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" candidature.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Candidature> findOne(Long id);

    /**
     * Delete the "id" candidature.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
