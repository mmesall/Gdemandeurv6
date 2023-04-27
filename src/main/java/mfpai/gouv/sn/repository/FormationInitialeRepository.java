package mfpai.gouv.sn.repository;

import mfpai.gouv.sn.domain.FormationInitiale;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FormationInitiale entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormationInitialeRepository extends JpaRepository<FormationInitiale, Long> {}
