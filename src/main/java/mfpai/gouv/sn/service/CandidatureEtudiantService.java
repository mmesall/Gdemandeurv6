package mfpai.gouv.sn.service;

import java.util.Optional;
import mfpai.gouv.sn.domain.CandidatureEtudiant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link CandidatureEtudiant}.
 */
public interface CandidatureEtudiantService {
    /**
     * Save a candidatureEtudiant.
     *
     * @param candidatureEtudiant the entity to save.
     * @return the persisted entity.
     */
    CandidatureEtudiant save(CandidatureEtudiant candidatureEtudiant);

    /**
     * Partially updates a candidatureEtudiant.
     *
     * @param candidatureEtudiant the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CandidatureEtudiant> partialUpdate(CandidatureEtudiant candidatureEtudiant);

    /**
     * Get all the candidatureEtudiants.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CandidatureEtudiant> findAll(Pageable pageable);

    /**
     * Get all the candidatureEtudiants with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CandidatureEtudiant> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" candidatureEtudiant.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CandidatureEtudiant> findOne(Long id);

    /**
     * Delete the "id" candidatureEtudiant.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
