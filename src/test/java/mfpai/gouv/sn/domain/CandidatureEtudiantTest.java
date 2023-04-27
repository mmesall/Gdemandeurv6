package mfpai.gouv.sn.domain;

import static org.assertj.core.api.Assertions.assertThat;

import mfpai.gouv.sn.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CandidatureEtudiantTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CandidatureEtudiant.class);
        CandidatureEtudiant candidatureEtudiant1 = new CandidatureEtudiant();
        candidatureEtudiant1.setId(1L);
        CandidatureEtudiant candidatureEtudiant2 = new CandidatureEtudiant();
        candidatureEtudiant2.setId(candidatureEtudiant1.getId());
        assertThat(candidatureEtudiant1).isEqualTo(candidatureEtudiant2);
        candidatureEtudiant2.setId(2L);
        assertThat(candidatureEtudiant1).isNotEqualTo(candidatureEtudiant2);
        candidatureEtudiant1.setId(null);
        assertThat(candidatureEtudiant1).isNotEqualTo(candidatureEtudiant2);
    }
}
