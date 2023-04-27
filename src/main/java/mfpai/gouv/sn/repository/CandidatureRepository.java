package mfpai.gouv.sn.repository;

import java.util.List;
import java.util.Optional;
import mfpai.gouv.sn.domain.Candidature;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Candidature entity.
 */
@Repository
public interface CandidatureRepository extends JpaRepository<Candidature, Long> {
    default Optional<Candidature> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Candidature> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Candidature> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct candidature from Candidature candidature left join fetch candidature.formationInitiale left join fetch candidature.formationContinue",
        countQuery = "select count(distinct candidature) from Candidature candidature"
    )
    Page<Candidature> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct candidature from Candidature candidature left join fetch candidature.formationInitiale left join fetch candidature.formationContinue"
    )
    List<Candidature> findAllWithToOneRelationships();

    @Query(
        "select candidature from Candidature candidature left join fetch candidature.formationInitiale left join fetch candidature.formationContinue where candidature.id =:id"
    )
    Optional<Candidature> findOneWithToOneRelationships(@Param("id") Long id);
}
