package mfpai.gouv.sn.domain;

import static org.assertj.core.api.Assertions.assertThat;

import mfpai.gouv.sn.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ServiceMFPAITest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceMFPAI.class);
        ServiceMFPAI serviceMFPAI1 = new ServiceMFPAI();
        serviceMFPAI1.setId(1L);
        ServiceMFPAI serviceMFPAI2 = new ServiceMFPAI();
        serviceMFPAI2.setId(serviceMFPAI1.getId());
        assertThat(serviceMFPAI1).isEqualTo(serviceMFPAI2);
        serviceMFPAI2.setId(2L);
        assertThat(serviceMFPAI1).isNotEqualTo(serviceMFPAI2);
        serviceMFPAI1.setId(null);
        assertThat(serviceMFPAI1).isNotEqualTo(serviceMFPAI2);
    }
}
