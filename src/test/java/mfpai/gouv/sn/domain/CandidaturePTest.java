package mfpai.gouv.sn.domain;

import static org.assertj.core.api.Assertions.assertThat;

import mfpai.gouv.sn.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CandidaturePTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CandidatureP.class);
        CandidatureP candidatureP1 = new CandidatureP();
        candidatureP1.setId(1L);
        CandidatureP candidatureP2 = new CandidatureP();
        candidatureP2.setId(candidatureP1.getId());
        assertThat(candidatureP1).isEqualTo(candidatureP2);
        candidatureP2.setId(2L);
        assertThat(candidatureP1).isNotEqualTo(candidatureP2);
        candidatureP1.setId(null);
        assertThat(candidatureP1).isNotEqualTo(candidatureP2);
    }
}
