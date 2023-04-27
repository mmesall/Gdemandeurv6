package mfpai.gouv.sn.service.impl;

import java.util.Optional;
import mfpai.gouv.sn.domain.CandidatureElev;
import mfpai.gouv.sn.repository.CandidatureElevRepository;
import mfpai.gouv.sn.service.CandidatureElevService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CandidatureElev}.
 */
@Service
@Transactional
public class CandidatureElevServiceImpl implements CandidatureElevService {

    private final Logger log = LoggerFactory.getLogger(CandidatureElevServiceImpl.class);

    private final CandidatureElevRepository candidatureElevRepository;

    public CandidatureElevServiceImpl(CandidatureElevRepository candidatureElevRepository) {
        this.candidatureElevRepository = candidatureElevRepository;
    }

    @Override
    public CandidatureElev save(CandidatureElev candidatureElev) {
        log.debug("Request to save CandidatureElev : {}", candidatureElev);
        return candidatureElevRepository.save(candidatureElev);
    }

    @Override
    public Optional<CandidatureElev> partialUpdate(CandidatureElev candidatureElev) {
        log.debug("Request to partially update CandidatureElev : {}", candidatureElev);

        return candidatureElevRepository
            .findById(candidatureElev.getId())
            .map(existingCandidatureElev -> {
                if (candidatureElev.getOffreFormation() != null) {
                    existingCandidatureElev.setOffreFormation(candidatureElev.getOffreFormation());
                }
                if (candidatureElev.getDateDebutOffre() != null) {
                    existingCandidatureElev.setDateDebutOffre(candidatureElev.getDateDebutOffre());
                }
                if (candidatureElev.getDateFinOffre() != null) {
                    existingCandidatureElev.setDateFinOffre(candidatureElev.getDateFinOffre());
                }
                if (candidatureElev.getDateDepot() != null) {
                    existingCandidatureElev.setDateDepot(candidatureElev.getDateDepot());
                }
                if (candidatureElev.getResultat() != null) {
                    existingCandidatureElev.setResultat(candidatureElev.getResultat());
                }

                return existingCandidatureElev;
            })
            .map(candidatureElevRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CandidatureElev> findAll(Pageable pageable) {
        log.debug("Request to get all CandidatureElevs");
        return candidatureElevRepository.findAll(pageable);
    }

    public Page<CandidatureElev> findAllWithEagerRelationships(Pageable pageable) {
        return candidatureElevRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CandidatureElev> findOne(Long id) {
        log.debug("Request to get CandidatureElev : {}", id);
        return candidatureElevRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CandidatureElev : {}", id);
        candidatureElevRepository.deleteById(id);
    }
}
