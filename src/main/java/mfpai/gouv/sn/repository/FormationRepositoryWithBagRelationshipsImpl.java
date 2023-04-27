package mfpai.gouv.sn.repository;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import mfpai.gouv.sn.domain.Formation;
import org.hibernate.annotations.QueryHints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class FormationRepositoryWithBagRelationshipsImpl implements FormationRepositoryWithBagRelationships {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Optional<Formation> fetchBagRelationships(Optional<Formation> formation) {
        return formation.map(this::fetchEtablissements);
    }

    @Override
    public Page<Formation> fetchBagRelationships(Page<Formation> formations) {
        return new PageImpl<>(fetchBagRelationships(formations.getContent()), formations.getPageable(), formations.getTotalElements());
    }

    @Override
    public List<Formation> fetchBagRelationships(List<Formation> formations) {
        return Optional.of(formations).map(this::fetchEtablissements).get();
    }

    Formation fetchEtablissements(Formation result) {
        return entityManager
            .createQuery(
                "select formation from Formation formation left join fetch formation.etablissements where formation is :formation",
                Formation.class
            )
            .setParameter("formation", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Formation> fetchEtablissements(List<Formation> formations) {
        return entityManager
            .createQuery(
                "select distinct formation from Formation formation left join fetch formation.etablissements where formation in :formations",
                Formation.class
            )
            .setParameter("formations", formations)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
