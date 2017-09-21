package lab.zlren.sell.util;

import lab.zlren.sell.common.enums.CodeEnum;

/**
 * Created by zlren on 17/9/19.
 */
public class EnumUtil {

    public static <T extends CodeEnum> T getByCode(Integer code, Class<T> enumClass) {
        for (T each : enumClass.getEnumConstants()) {
            if (code.equals(each.getCode())) {
                return each;
            }
        }
        return null;
    }
}
