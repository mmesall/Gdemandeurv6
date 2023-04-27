package mfpai.gouv.sn.domain;

import static org.assertj.core.api.Assertions.assertThat;

import mfpai.gouv.sn.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CandidatureElevTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CandidatureElev.class);
        CandidatureElev candidatureElev1 = new CandidatureElev();
        candidatureElev1.setId(1L);
        CandidatureElev candidatureElev2 = new CandidatureElev();
        candidatureElev2.setId(candidatureElev1.getId());
        assertThat(candidatureElev1).isEqualTo(candidatureElev2);
        candidatureElev2.setId(2L);
        assertThat(candidatureElev1).isNotEqualTo(candidatureElev2);
        candidatureElev1.setId(null);
        assertThat(candidatureElev1).isNotEqualTo(candidatureElev2);
    }
}
