package de.rjst.ps;

import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class TestConstant {

    public static final String BLANK_STRING = " ";
    public static final String ANY_STRING = "anyString";
    public static final String OTHER_STRING = "otherString";
    public static final Long MAX_LONG = Long.MAX_VALUE;

    public static final BigDecimal MAX_PRICE = new BigDecimal(10000L);
    public static final BigDecimal LOW_PRICE = BigDecimal.ZERO;
    public static final BigDecimal HIGH_PRICE = new BigDecimal(99999L);

}
