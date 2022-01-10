package com.collective.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.collective.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlantsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlantsDTO.class);
        PlantsDTO plantsDTO1 = new PlantsDTO();
        plantsDTO1.setId(1L);
        PlantsDTO plantsDTO2 = new PlantsDTO();
        assertThat(plantsDTO1).isNotEqualTo(plantsDTO2);
        plantsDTO2.setId(plantsDTO1.getId());
        assertThat(plantsDTO1).isEqualTo(plantsDTO2);
        plantsDTO2.setId(2L);
        assertThat(plantsDTO1).isNotEqualTo(plantsDTO2);
        plantsDTO1.setId(null);
        assertThat(plantsDTO1).isNotEqualTo(plantsDTO2);
    }
}
