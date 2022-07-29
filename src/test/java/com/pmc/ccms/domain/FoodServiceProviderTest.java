package com.pmc.ccms.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.pmc.ccms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FoodServiceProviderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FoodServiceProvider.class);
        FoodServiceProvider foodServiceProvider1 = new FoodServiceProvider();
        foodServiceProvider1.setId(1L);
        FoodServiceProvider foodServiceProvider2 = new FoodServiceProvider();
        foodServiceProvider2.setId(foodServiceProvider1.getId());
        assertThat(foodServiceProvider1).isEqualTo(foodServiceProvider2);
        foodServiceProvider2.setId(2L);
        assertThat(foodServiceProvider1).isNotEqualTo(foodServiceProvider2);
        foodServiceProvider1.setId(null);
        assertThat(foodServiceProvider1).isNotEqualTo(foodServiceProvider2);
    }
}
