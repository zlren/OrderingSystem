package lab.zlren.sell.util;

/**
 * Created by zlren on 17/9/10.
 */
public class MathUtil {

    private static final Double MONEY_RANGE = 0.01;

    public static Boolean equal(Double d1, Double d2) {
        return Math.abs(d1 - d2) < MONEY_RANGE;
    }
}
