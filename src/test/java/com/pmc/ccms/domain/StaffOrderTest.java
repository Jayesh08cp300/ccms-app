package com.pmc.ccms.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.pmc.ccms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StaffOrderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StaffOrder.class);
        StaffOrder staffOrder1 = new StaffOrder();
        staffOrder1.setId(1L);
        StaffOrder staffOrder2 = new StaffOrder();
        staffOrder2.setId(staffOrder1.getId());
        assertThat(staffOrder1).isEqualTo(staffOrder2);
        staffOrder2.setId(2L);
        assertThat(staffOrder1).isNotEqualTo(staffOrder2);
        staffOrder1.setId(null);
        assertThat(staffOrder1).isNotEqualTo(staffOrder2);
    }
}
