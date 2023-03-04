package com.acepero13.research.profilesimilarity.utils;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

/**
 * A Description
 *
 * @author Alvaro Cepero
 */
class CalculationUtilsTest {
    @Test void roundValueBiggerThanZero(){
        var result = CalculationUtils.roundedTwoDecimals(100.3333333);
        assertThat(result, equalTo(100.33));
    }

    @Test void roundValueSmallerThanZero(){
        var result = CalculationUtils.roundedTwoDecimals(0.666666);
        assertThat(result, equalTo(0.67));
    }

}
