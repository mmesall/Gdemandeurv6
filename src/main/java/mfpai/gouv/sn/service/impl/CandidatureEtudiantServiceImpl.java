package mfpai.gouv.sn.service.impl;

import java.util.Optional;
import mfpai.gouv.sn.domain.CandidatureEtudiant;
import mfpai.gouv.sn.repository.CandidatureEtudiantRepository;
import mfpai.gouv.sn.service.CandidatureEtudiantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CandidatureEtudiant}.
 */
@Service
@Transactional
public class CandidatureEtudiantServiceImpl implements CandidatureEtudiantService {

    private final Logger log = LoggerFactory.getLogger(CandidatureEtudiantServiceImpl.class);

    private final CandidatureEtudiantRepository candidatureEtudiantRepository;

    public CandidatureEtudiantServiceImpl(CandidatureEtudiantRepository candidatureEtudiantRepository) {
        this.candidatureEtudiantRepository = candidatureEtudiantRepository;
    }

    @Override
    public CandidatureEtudiant save(CandidatureEtudiant candidatureEtudiant) {
        log.debug("Request to save CandidatureEtudiant : {}", candidatureEtudiant);
        return candidatureEtudiantRepository.save(candidatureEtudiant);
    }

    @Override
    public Optional<CandidatureEtudiant> partialUpdate(CandidatureEtudiant candidatureEtudiant) {
        log.debug("Request to partially update CandidatureEtudiant : {}", candidatureEtudiant);

        return candidatureEtudiantRepository
            .findById(candidatureEtudiant.getId())
            .map(existingCandidatureEtudiant -> {
                if (candidatureEtudiant.getOffreFormation() != null) {
                    existingCandidatureEtudiant.setOffreFormation(candidatureEtudiant.getOffreFormation());
                }
                if (candidatureEtudiant.getDateDebutOffre() != null) {
                    existingCandidatureEtudiant.setDateDebutOffre(candidatureEtudiant.getDateDebutOffre());
                }
                if (candidatureEtudiant.getDateFinOffre() != null) {
                    existingCandidatureEtudiant.setDateFinOffre(candidatureEtudiant.getDateFinOffre());
                }
                if (candidatureEtudiant.getDateDepot() != null) {
                    existingCandidatureEtudiant.setDateDepot(candidatureEtudiant.getDateDepot());
                }
                if (candidatureEtudiant.getResultat() != null) {
                    existingCandidatureEtudiant.setResultat(candidatureEtudiant.getResultat());
                }

                return existingCandidatureEtudiant;
            })
            .map(candidatureEtudiantRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CandidatureEtudiant> findAll(Pageable pageable) {
        log.debug("Request to get all CandidatureEtudiants");
        return candidatureEtudiantRepository.findAll(pageable);
    }

    public Page<CandidatureEtudiant> findAllWithEagerRelationships(Pageable pageable) {
        return candidatureEtudiantRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CandidatureEtudiant> findOne(Long id) {
        log.debug("Request to get CandidatureEtudiant : {}", id);
        return candidatureEtudiantRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CandidatureEtudiant : {}", id);
        candidatureEtudiantRepository.deleteById(id);
    }
}
