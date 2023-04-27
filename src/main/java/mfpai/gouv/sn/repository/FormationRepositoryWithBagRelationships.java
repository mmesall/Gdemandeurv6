package mfpai.gouv.sn.repository;

import java.util.List;
import java.util.Optional;
import mfpai.gouv.sn.domain.Formation;
import org.springframework.data.domain.Page;

public interface FormationRepositoryWithBagRelationships {
    Optional<Formation> fetchBagRelationships(Optional<Formation> formation);

    List<Formation> fetchBagRelationships(List<Formation> formations);

    Page<Formation> fetchBagRelationships(Page<Formation> formations);
}
