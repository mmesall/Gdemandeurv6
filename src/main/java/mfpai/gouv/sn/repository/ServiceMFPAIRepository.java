package mfpai.gouv.sn.repository;

import mfpai.gouv.sn.domain.ServiceMFPAI;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ServiceMFPAI entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServiceMFPAIRepository extends JpaRepository<ServiceMFPAI, Long> {}
