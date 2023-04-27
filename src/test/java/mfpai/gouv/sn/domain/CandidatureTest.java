package mfpai.gouv.sn.domain;

import static org.assertj.core.api.Assertions.assertThat;

import mfpai.gouv.sn.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CandidatureTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Candidature.class);
        Candidature candidature1 = new Candidature();
        candidature1.setId(1L);
        Candidature candidature2 = new Candidature();
        candidature2.setId(candidature1.getId());
        assertThat(candidature1).isEqualTo(candidature2);
        candidature2.setId(2L);
        assertThat(candidature1).isNotEqualTo(candidature2);
        candidature1.setId(null);
        assertThat(candidature1).isNotEqualTo(candidature2);
    }
}
