package mfpai.gouv.sn.service;

import java.util.Optional;
import mfpai.gouv.sn.domain.FormationContinue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link FormationContinue}.
 */
public interface FormationContinueService {
    /**
     * Save a formationContinue.
     *
     * @param formationContinue the entity to save.
     * @return the persisted entity.
     */
    FormationContinue save(FormationContinue formationContinue);

    /**
     * Partially updates a formationContinue.
     *
     * @param formationContinue the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FormationContinue> partialUpdate(FormationContinue formationContinue);

    /**
     * Get all the formationContinues.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FormationContinue> findAll(Pageable pageable);

    /**
     * Get the "id" formationContinue.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FormationContinue> findOne(Long id);

    /**
     * Delete the "id" formationContinue.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
