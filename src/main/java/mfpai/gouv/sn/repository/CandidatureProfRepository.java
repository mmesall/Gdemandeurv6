package mfpai.gouv.sn.repository;

import java.util.List;
import java.util.Optional;
import mfpai.gouv.sn.domain.CandidatureProf;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CandidatureProf entity.
 */
@Repository
public interface CandidatureProfRepository extends JpaRepository<CandidatureProf, Long> {
    default Optional<CandidatureProf> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CandidatureProf> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CandidatureProf> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct candidatureProf from CandidatureProf candidatureProf left join fetch candidatureProf.professionnel left join fetch candidatureProf.formationContinue",
        countQuery = "select count(distinct candidatureProf) from CandidatureProf candidatureProf"
    )
    Page<CandidatureProf> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct candidatureProf from CandidatureProf candidatureProf left join fetch candidatureProf.professionnel left join fetch candidatureProf.formationContinue"
    )
    List<CandidatureProf> findAllWithToOneRelationships();

    @Query(
        "select candidatureProf from CandidatureProf candidatureProf left join fetch candidatureProf.professionnel left join fetch candidatureProf.formationContinue where candidatureProf.id =:id"
    )
    Optional<CandidatureProf> findOneWithToOneRelationships(@Param("id") Long id);
}
