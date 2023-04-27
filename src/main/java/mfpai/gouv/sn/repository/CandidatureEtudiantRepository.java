package mfpai.gouv.sn.repository;

import java.util.List;
import java.util.Optional;
import mfpai.gouv.sn.domain.CandidatureEtudiant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CandidatureEtudiant entity.
 */
@Repository
public interface CandidatureEtudiantRepository extends JpaRepository<CandidatureEtudiant, Long> {
    default Optional<CandidatureEtudiant> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CandidatureEtudiant> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CandidatureEtudiant> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct candidatureEtudiant from CandidatureEtudiant candidatureEtudiant left join fetch candidatureEtudiant.etudiant left join fetch candidatureEtudiant.formationInitiale",
        countQuery = "select count(distinct candidatureEtudiant) from CandidatureEtudiant candidatureEtudiant"
    )
    Page<CandidatureEtudiant> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct candidatureEtudiant from CandidatureEtudiant candidatureEtudiant left join fetch candidatureEtudiant.etudiant left join fetch candidatureEtudiant.formationInitiale"
    )
    List<CandidatureEtudiant> findAllWithToOneRelationships();

    @Query(
        "select candidatureEtudiant from CandidatureEtudiant candidatureEtudiant left join fetch candidatureEtudiant.etudiant left join fetch candidatureEtudiant.formationInitiale where candidatureEtudiant.id =:id"
    )
    Optional<CandidatureEtudiant> findOneWithToOneRelationships(@Param("id") Long id);
}
