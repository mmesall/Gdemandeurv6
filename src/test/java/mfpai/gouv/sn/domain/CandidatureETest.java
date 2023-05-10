package mfpai.gouv.sn.domain;

import static org.assertj.core.api.Assertions.assertThat;

import mfpai.gouv.sn.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CandidatureETest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CandidatureE.class);
        CandidatureE candidatureE1 = new CandidatureE();
        candidatureE1.setId(1L);
        CandidatureE candidatureE2 = new CandidatureE();
        candidatureE2.setId(candidatureE1.getId());
        assertThat(candidatureE1).isEqualTo(candidatureE2);
        candidatureE2.setId(2L);
        assertThat(candidatureE1).isNotEqualTo(candidatureE2);
        candidatureE1.setId(null);
        assertThat(candidatureE1).isNotEqualTo(candidatureE2);
    }
}
