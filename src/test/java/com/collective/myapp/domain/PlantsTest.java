package com.collective.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.collective.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlantsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Plants.class);
        Plants plants1 = new Plants();
        plants1.setId(1L);
        Plants plants2 = new Plants();
        plants2.setId(plants1.getId());
        assertThat(plants1).isEqualTo(plants2);
        plants2.setId(2L);
        assertThat(plants1).isNotEqualTo(plants2);
        plants1.setId(null);
        assertThat(plants1).isNotEqualTo(plants2);
    }
}
