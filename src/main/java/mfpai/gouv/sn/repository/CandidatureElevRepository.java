package mfpai.gouv.sn.repository;

import java.util.List;
import java.util.Optional;
import mfpai.gouv.sn.domain.CandidatureElev;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CandidatureElev entity.
 */
@Repository
public interface CandidatureElevRepository extends JpaRepository<CandidatureElev, Long> {
    default Optional<CandidatureElev> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CandidatureElev> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CandidatureElev> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct candidatureElev from CandidatureElev candidatureElev left join fetch candidatureElev.eleve left join fetch candidatureElev.formationInitiale",
        countQuery = "select count(distinct candidatureElev) from CandidatureElev candidatureElev"
    )
    Page<CandidatureElev> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct candidatureElev from CandidatureElev candidatureElev left join fetch candidatureElev.eleve left join fetch candidatureElev.formationInitiale"
    )
    List<CandidatureElev> findAllWithToOneRelationships();

    @Query(
        "select candidatureElev from CandidatureElev candidatureElev left join fetch candidatureElev.eleve left join fetch candidatureElev.formationInitiale where candidatureElev.id =:id"
    )
    Optional<CandidatureElev> findOneWithToOneRelationships(@Param("id") Long id);
}
