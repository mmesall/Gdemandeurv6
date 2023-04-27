package mfpai.gouv.sn.domain;

import static org.assertj.core.api.Assertions.assertThat;

import mfpai.gouv.sn.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FormationContinueTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormationContinue.class);
        FormationContinue formationContinue1 = new FormationContinue();
        formationContinue1.setId(1L);
        FormationContinue formationContinue2 = new FormationContinue();
        formationContinue2.setId(formationContinue1.getId());
        assertThat(formationContinue1).isEqualTo(formationContinue2);
        formationContinue2.setId(2L);
        assertThat(formationContinue1).isNotEqualTo(formationContinue2);
        formationContinue1.setId(null);
        assertThat(formationContinue1).isNotEqualTo(formationContinue2);
    }
}
