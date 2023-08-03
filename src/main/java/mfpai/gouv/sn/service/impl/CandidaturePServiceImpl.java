package mfpai.gouv.sn.service.impl;

import java.util.Optional;
import mfpai.gouv.sn.domain.CandidatureP;
import mfpai.gouv.sn.repository.CandidaturePRepository;
import mfpai.gouv.sn.service.CandidaturePService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CandidatureP}.
 */
@Service
@Transactional
public class CandidaturePServiceImpl implements CandidaturePService {

    private final Logger log = LoggerFactory.getLogger(CandidaturePServiceImpl.class);

    private final CandidaturePRepository candidaturePRepository;

    public CandidaturePServiceImpl(CandidaturePRepository candidaturePRepository) {
        this.candidaturePRepository = candidaturePRepository;
    }

    @Override
    public CandidatureP save(CandidatureP candidatureP) {
        log.debug("Request to save CandidatureP : {}", candidatureP);
        return candidaturePRepository.save(candidatureP);
    }

    @Override
    public CandidatureP update(CandidatureP candidatureP) {
        log.debug("Request to update CandidatureP : {}", candidatureP);
        return candidaturePRepository.save(candidatureP);
    }

    @Override
    public Optional<CandidatureP> partialUpdate(CandidatureP candidatureP) {
        log.debug("Request to partially update CandidatureP : {}", candidatureP);

        return candidaturePRepository
            .findById(candidatureP.getId())
            .map(existingCandidatureP -> {
                if (candidatureP.getOffreFormation() != null) {
                    existingCandidatureP.setOffreFormation(candidatureP.getOffreFormation());
                }
                if (candidatureP.getDateDebutOffre() != null) {
                    existingCandidatureP.setDateDebutOffre(candidatureP.getDateDebutOffre());
                }
                if (candidatureP.getDateFinOffre() != null) {
                    existingCandidatureP.setDateFinOffre(candidatureP.getDateFinOffre());
                }
                if (candidatureP.getDateDepot() != null) {
                    existingCandidatureP.setDateDepot(candidatureP.getDateDepot());
                }
                if (candidatureP.getResultat() != null) {
                    existingCandidatureP.setResultat(candidatureP.getResultat());
                }

                return existingCandidatureP;
            })
            .map(candidaturePRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CandidatureP> findAll(Pageable pageable) {
        log.debug("Request to get all CandidaturePS");
        return candidaturePRepository.findAll(pageable);
    }

    public Page<CandidatureP> findAllWithEagerRelationships(Pageable pageable) {
        return candidaturePRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CandidatureP> findOne(Long id) {
        log.debug("Request to get CandidatureP : {}", id);
        return candidaturePRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CandidatureP : {}", id);
        candidaturePRepository.deleteById(id);
    }
}
