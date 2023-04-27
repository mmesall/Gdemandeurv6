package mfpai.gouv.sn.domain;

import static org.assertj.core.api.Assertions.assertThat;

import mfpai.gouv.sn.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FormationInitialeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormationInitiale.class);
        FormationInitiale formationInitiale1 = new FormationInitiale();
        formationInitiale1.setId(1L);
        FormationInitiale formationInitiale2 = new FormationInitiale();
        formationInitiale2.setId(formationInitiale1.getId());
        assertThat(formationInitiale1).isEqualTo(formationInitiale2);
        formationInitiale2.setId(2L);
        assertThat(formationInitiale1).isNotEqualTo(formationInitiale2);
        formationInitiale1.setId(null);
        assertThat(formationInitiale1).isNotEqualTo(formationInitiale2);
    }
}
