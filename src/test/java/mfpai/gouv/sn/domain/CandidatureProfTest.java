package mfpai.gouv.sn.domain;

import static org.assertj.core.api.Assertions.assertThat;

import mfpai.gouv.sn.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CandidatureProfTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CandidatureProf.class);
        CandidatureProf candidatureProf1 = new CandidatureProf();
        candidatureProf1.setId(1L);
        CandidatureProf candidatureProf2 = new CandidatureProf();
        candidatureProf2.setId(candidatureProf1.getId());
        assertThat(candidatureProf1).isEqualTo(candidatureProf2);
        candidatureProf2.setId(2L);
        assertThat(candidatureProf1).isNotEqualTo(candidatureProf2);
        candidatureProf1.setId(null);
        assertThat(candidatureProf1).isNotEqualTo(candidatureProf2);
    }
}
