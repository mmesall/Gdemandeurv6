package mfpai.gouv.sn.repository;

import java.util.List;
import java.util.Optional;
import mfpai.gouv.sn.domain.Experience;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Experience entity.
 */
@Repository
public interface ExperienceRepository extends JpaRepository<Experience, Long> {
    default Optional<Experience> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Experience> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Experience> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct experience from Experience experience left join fetch experience.eleve left join fetch experience.etudiant left join fetch experience.professionnel left join fetch experience.demandeur",
        countQuery = "select count(distinct experience) from Experience experience"
    )
    Page<Experience> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct experience from Experience experience left join fetch experience.eleve left join fetch experience.etudiant left join fetch experience.professionnel left join fetch experience.demandeur"
    )
    List<Experience> findAllWithToOneRelationships();

    @Query(
        "select experience from Experience experience left join fetch experience.eleve left join fetch experience.etudiant left join fetch experience.professionnel left join fetch experience.demandeur where experience.id =:id"
    )
    Optional<Experience> findOneWithToOneRelationships(@Param("id") Long id);
}
