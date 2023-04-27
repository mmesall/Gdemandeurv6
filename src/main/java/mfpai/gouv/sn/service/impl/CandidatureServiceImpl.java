package mfpai.gouv.sn.service.impl;

import java.util.Optional;
import mfpai.gouv.sn.domain.Candidature;
import mfpai.gouv.sn.repository.CandidatureRepository;
import mfpai.gouv.sn.service.CandidatureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Candidature}.
 */
@Service
@Transactional
public class CandidatureServiceImpl implements CandidatureService {

    private final Logger log = LoggerFactory.getLogger(CandidatureServiceImpl.class);

    private final CandidatureRepository candidatureRepository;

    public CandidatureServiceImpl(CandidatureRepository candidatureRepository) {
        this.candidatureRepository = candidatureRepository;
    }

    @Override
    public Candidature save(Candidature candidature) {
        log.debug("Request to save Candidature : {}", candidature);
        return candidatureRepository.save(candidature);
    }

    @Override
    public Optional<Candidature> partialUpdate(Candidature candidature) {
        log.debug("Request to partially update Candidature : {}", candidature);

        return candidatureRepository
            .findById(candidature.getId())
            .map(existingCandidature -> {
                if (candidature.getOffreFormation() != null) {
                    existingCandidature.setOffreFormation(candidature.getOffreFormation());
                }
                if (candidature.getDateDebutOffre() != null) {
                    existingCandidature.setDateDebutOffre(candidature.getDateDebutOffre());
                }
                if (candidature.getDateFinOffre() != null) {
                    existingCandidature.setDateFinOffre(candidature.getDateFinOffre());
                }
                if (candidature.getDateDepot() != null) {
                    existingCandidature.setDateDepot(candidature.getDateDepot());
                }
                if (candidature.getResultat() != null) {
                    existingCandidature.setResultat(candidature.getResultat());
                }

                return existingCandidature;
            })
            .map(candidatureRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Candidature> findAll(Pageable pageable) {
        log.debug("Request to get all Candidatures");
        return candidatureRepository.findAll(pageable);
    }

    public Page<Candidature> findAllWithEagerRelationships(Pageable pageable) {
        return candidatureRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Candidature> findOne(Long id) {
        log.debug("Request to get Candidature : {}", id);
        return candidatureRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Candidature : {}", id);
        candidatureRepository.deleteById(id);
    }
}
