package mfpai.gouv.sn.service.impl;

import java.util.Optional;
import mfpai.gouv.sn.domain.CandidatureProf;
import mfpai.gouv.sn.repository.CandidatureProfRepository;
import mfpai.gouv.sn.service.CandidatureProfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CandidatureProf}.
 */
@Service
@Transactional
public class CandidatureProfServiceImpl implements CandidatureProfService {

    private final Logger log = LoggerFactory.getLogger(CandidatureProfServiceImpl.class);

    private final CandidatureProfRepository candidatureProfRepository;

    public CandidatureProfServiceImpl(CandidatureProfRepository candidatureProfRepository) {
        this.candidatureProfRepository = candidatureProfRepository;
    }

    @Override
    public CandidatureProf save(CandidatureProf candidatureProf) {
        log.debug("Request to save CandidatureProf : {}", candidatureProf);
        return candidatureProfRepository.save(candidatureProf);
    }

    @Override
    public Optional<CandidatureProf> partialUpdate(CandidatureProf candidatureProf) {
        log.debug("Request to partially update CandidatureProf : {}", candidatureProf);

        return candidatureProfRepository
            .findById(candidatureProf.getId())
            .map(existingCandidatureProf -> {
                if (candidatureProf.getOffreFormation() != null) {
                    existingCandidatureProf.setOffreFormation(candidatureProf.getOffreFormation());
                }
                if (candidatureProf.getDateDebutOffre() != null) {
                    existingCandidatureProf.setDateDebutOffre(candidatureProf.getDateDebutOffre());
                }
                if (candidatureProf.getDateFinOffre() != null) {
                    existingCandidatureProf.setDateFinOffre(candidatureProf.getDateFinOffre());
                }
                if (candidatureProf.getDateDepot() != null) {
                    existingCandidatureProf.setDateDepot(candidatureProf.getDateDepot());
                }
                if (candidatureProf.getResultat() != null) {
                    existingCandidatureProf.setResultat(candidatureProf.getResultat());
                }

                return existingCandidatureProf;
            })
            .map(candidatureProfRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CandidatureProf> findAll(Pageable pageable) {
        log.debug("Request to get all CandidatureProfs");
        return candidatureProfRepository.findAll(pageable);
    }

    public Page<CandidatureProf> findAllWithEagerRelationships(Pageable pageable) {
        return candidatureProfRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CandidatureProf> findOne(Long id) {
        log.debug("Request to get CandidatureProf : {}", id);
        return candidatureProfRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CandidatureProf : {}", id);
        candidatureProfRepository.deleteById(id);
    }
}
