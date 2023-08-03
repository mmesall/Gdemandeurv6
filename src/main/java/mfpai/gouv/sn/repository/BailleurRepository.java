package mfpai.gouv.sn.repository;

import mfpai.gouv.sn.domain.Bailleur;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Bailleur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BailleurRepository extends JpaRepository<Bailleur, Long> {}
