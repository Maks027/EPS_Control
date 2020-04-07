import java.util.HashMap;
import java.util.Map;

public class DefaultParameters {

    private final double defK = 1240.9;

    private Map<P_ID, Parameter> parameterMap;

    public DefaultParameters() {

        parameterMap = new HashMap<>();

        parameterMap.put(P_ID.BATTERY_VOLTAGE,  new Parameter(P_ID.BATTERY_VOLTAGE,   1, 2.2, 3.3));
        parameterMap.put(P_ID.BATTERY_CURRENT,  new Parameter(P_ID.BATTERY_CURRENT,   2, 0,   3.3));
        parameterMap.put(P_ID.BCR_VOLTAGE,      new Parameter(P_ID.BCR_VOLTAGE,       3, 0,   3.3));
        parameterMap.put(P_ID.BCR_CURRENT,      new Parameter(P_ID.BCR_CURRENT,       4, 0,   3.3));
        parameterMap.put(P_ID.X_VOLTAGE,        new Parameter(P_ID.X_VOLTAGE,         5, 0,   3.3));
        parameterMap.put(P_ID.X_POS_CURRENT,    new Parameter(P_ID.X_POS_CURRENT,     6, 0,   3.3));
        parameterMap.put(P_ID.X_NEG_CURRENT,    new Parameter(P_ID.X_NEG_CURRENT,     7, 0,   3.3));
        parameterMap.put(P_ID.Y_VOLTAGE,        new Parameter(P_ID.Y_VOLTAGE,         8, 1.2,   3.3));
        parameterMap.put(P_ID.Y_POS_CURRENT,    new Parameter(P_ID.Y_POS_CURRENT,     9, 0,   3.3));
        parameterMap.put(P_ID.Y_NEG_CURRENT,    new Parameter(P_ID.Y_NEG_CURRENT,     10, 0,   3.3));
        parameterMap.put(P_ID.Z_VOLTAGE,        new Parameter(P_ID.Z_VOLTAGE,         11, 0,   3.3));
        parameterMap.put(P_ID.Z_POS_CURRENT,    new Parameter(P_ID.Z_POS_CURRENT,    12, 0,   3.3));
        parameterMap.put(P_ID.Z_NEG_CURRENT,    new Parameter(P_ID.Z_NEG_CURRENT,     13, 0,   3.3));
        parameterMap.put(P_ID.CURRENT_3V3,      new Parameter(P_ID.CURRENT_3V3,       14, 0,   3.3));
        parameterMap.put(P_ID.CURRENT_5V,       new Parameter(P_ID.CURRENT_5V,        15, 0,   3.3));
        parameterMap.put(P_ID.LUP_3V3,          new Parameter(P_ID.LUP_3V3,           16, 0));
        parameterMap.put(P_ID.LUP_5V,           new Parameter(P_ID.LUP_5V,            17, 0));
        parameterMap.put(P_ID.MCU_TEMP,         new Parameter(P_ID.MCU_TEMP,          18, 0));
        parameterMap.put(P_ID.BAT_TEMP_SENS1,   new Parameter(P_ID.BAT_TEMP_SENS1,    19, 0));
        parameterMap.put(P_ID.BAT_TEMP_SENS2,   new Parameter(P_ID.BAT_TEMP_SENS2,    20, 0));
        parameterMap.put(P_ID.BAT_TEMP_SENS3,   new Parameter(P_ID.BAT_TEMP_SENS3,    21, 0));
        parameterMap.put(P_ID.BAT_TEMP_SENS4,   new Parameter(P_ID.BAT_TEMP_SENS4,    22, 0));
        parameterMap.put(P_ID.INPUTS_COND,      new Parameter(P_ID.INPUTS_COND,       23, 0));
        parameterMap.put(P_ID.OUTPUTS_COND,     new Parameter(P_ID.OUTPUTS_COND,      24, 0));
        parameterMap.put(P_ID.POWER_ON_CYCLES,  new Parameter(P_ID.POWER_ON_CYCLES,   25, 0));
        parameterMap.put(P_ID.BAT_UNDER_VOLTAGE,new Parameter(P_ID.BAT_UNDER_VOLTAGE, 26, 0));
        parameterMap.put(P_ID.BAT_SHORT_CIRCUIT,new Parameter(P_ID.BAT_SHORT_CIRCUIT, 27, 0));
        parameterMap.put(P_ID.BAT_OVER_TEMP,    new Parameter(P_ID.BAT_OVER_TEMP,     28, 0));
        parameterMap.put(P_ID.MAX_TEMP1,        new Parameter(P_ID.MAX_TEMP1,         29, 0));
        parameterMap.put(P_ID.MAX_TEMP2,        new Parameter(P_ID.MAX_TEMP2,         30, 0));
        parameterMap.put(P_ID.MAX_TEMP3,        new Parameter(P_ID.MAX_TEMP3,         31, 0));
        parameterMap.put(P_ID.MAX_TEMP4,        new Parameter(P_ID.MAX_TEMP4,         32, 0));
        parameterMap.put(P_ID.MIN_TEMP1,        new Parameter(P_ID.MIN_TEMP1,         33, 0));
        parameterMap.put(P_ID.MIN_TEMP2,        new Parameter(P_ID.MIN_TEMP2,         34, 0));
        parameterMap.put(P_ID.MIN_TEMP3,        new Parameter(P_ID.MIN_TEMP3,         35, 0));
        parameterMap.put(P_ID.MIN_TEMP4,        new Parameter(P_ID.MIN_TEMP4,         36, 0));
        parameterMap.put(P_ID.TEMP_SENS5,       new Parameter(P_ID.TEMP_SENS5,        37, 0));
        parameterMap.put(P_ID.TEMP_SENS6,       new Parameter(P_ID.TEMP_SENS6,        38, 0));
        parameterMap.put(P_ID.TEMP_SENS7,       new Parameter(P_ID.TEMP_SENS7,        39, 0));
        parameterMap.put(P_ID.TEMP_SENS8,       new Parameter(P_ID.TEMP_SENS8,        40, 0));
        parameterMap.put(P_ID.SW_VERSION,       new Parameter(P_ID.SW_VERSION,        41, 0));
        parameterMap.put(P_ID.BAT_FAST_CH,      new Parameter(P_ID.BAT_FAST_CH,       42, 0));
        parameterMap.put(P_ID.V_OUT_1_DEF,      new Parameter(P_ID.V_OUT_1_DEF,       43, 0));
        parameterMap.put(P_ID.V_OUT_3_DEF,      new Parameter(P_ID.V_OUT_3_DEF,       44, 0));
        parameterMap.put(P_ID.V_OUT_4_DEF,      new Parameter(P_ID.V_OUT_4_DEF,       45, 0));
        parameterMap.put(P_ID.V_OUT_5_DEF,      new Parameter(P_ID.V_OUT_5_DEF,       46, 0));
        parameterMap.put(P_ID.V_OUT_6_DEF,      new Parameter(P_ID.V_OUT_6_DEF,       47, 0));
        parameterMap.put(P_ID.CHARGE_CYCLES,    new Parameter(P_ID.CHARGE_CYCLES,     48, 0));

    }


    public Map<P_ID, Parameter> getParameterMap() {
        return parameterMap;
    }

    public void setParameterMap(Map<P_ID, Parameter> parameterMap) {
        this.parameterMap = parameterMap;
    }

    public static void main(String[] args) {
        DefaultParameters defaultParameters = new DefaultParameters();

        Map<P_ID, Parameter> map = defaultParameters.getParameterMap();

        map.forEach((k, v) -> System.out.println("Parameter: " + k.name() + "; Address = " + v.getAddress() + "; Value = " + v.getDoubleValue()));

    }
}
