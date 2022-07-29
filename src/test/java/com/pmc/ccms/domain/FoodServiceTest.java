package com.pmc.ccms.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.pmc.ccms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FoodServiceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FoodService.class);
        FoodService foodService1 = new FoodService();
        foodService1.setId(1L);
        FoodService foodService2 = new FoodService();
        foodService2.setId(foodService1.getId());
        assertThat(foodService1).isEqualTo(foodService2);
        foodService2.setId(2L);
        assertThat(foodService1).isNotEqualTo(foodService2);
        foodService1.setId(null);
        assertThat(foodService1).isNotEqualTo(foodService2);
    }
}
