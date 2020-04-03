
public class DefaultParameters {

    private final double defK = 1240.9;

    private Parameter[] parameters = new Parameter[48];



    public DefaultParameters() {

        parameters[0] = new Parameter(P_ID.BATTERY_VOLTAGE,   1, 2.2);
        parameters[1] = new Parameter(P_ID.BATTERY_CURRENT,   2, 0);
        parameters[2] = new Parameter(P_ID.BCR_VOLTAGE,       3, 0);
        parameters[3] = new Parameter(P_ID.BCR_CURRENT,       4, 0);
        parameters[4] = new Parameter(P_ID.X_VOLTAGE,         5, 0);
        parameters[5] = new Parameter(P_ID.X_POS_CURRENT,     6, 0);
        parameters[6] = new Parameter(P_ID.X_NEG_CURRENT,     7, 0);

        parameters[7] = new Parameter(P_ID.Y_VOLTAGE,         8, 1.2);
        parameters[8] = new Parameter(P_ID.Y_POS_CURRENT,     9, 0);
        parameters[9] = new Parameter(P_ID.Y_NEG_CURRENT,     10, 0);

        parameters[10] = new Parameter(P_ID.Z_VOLTAGE,         11, 0);
        parameters[11]  = new Parameter(P_ID.Z_POS_CURRENT,    12, 0);
        parameters[12] = new Parameter(P_ID.Z_NEG_CURRENT,     13, 0);
        parameters[13] = new Parameter(P_ID.CURRENT_3V3,       14, 0);
        parameters[14] = new Parameter(P_ID.CURRENT_5V,        15, 0);
        parameters[15] = new Parameter(P_ID.LUP_3V3,           16, 0);
        parameters[16] = new Parameter(P_ID.LUP_5V,            17, 0);
        parameters[17] = new Parameter(P_ID.MCU_TEMP,          18, 0);
        parameters[18] = new Parameter(P_ID.BAT_TEMP_SENS1,    19, 0);
        parameters[19] = new Parameter(P_ID.BAT_TEMP_SENS2,    20, 0);
        parameters[20] = new Parameter(P_ID.BAT_TEMP_SENS3,    21, 0);
        parameters[21] = new Parameter(P_ID.BAT_TEMP_SENS4,    22, 0);
        parameters[22] = new Parameter(P_ID.INPUTS_COND,       23, 0);
        parameters[23] = new Parameter(P_ID.OUTPUTS_COND,      24, 0);
        parameters[24] = new Parameter(P_ID.POWER_ON_CYCLES,   25, 0);
        parameters[25] = new Parameter(P_ID.BAT_UNDER_VOLTAGE, 26, 0);
        parameters[26] = new Parameter(P_ID.BAT_SHORT_CIRCUIT, 27, 0);
        parameters[27] = new Parameter(P_ID.BAT_OVER_TEMP,     28, 0);
        parameters[28] = new Parameter(P_ID.MAX_TEMP1,         29, 0);
        parameters[29] = new Parameter(P_ID.MAX_TEMP2,         30, 0);
        parameters[30] = new Parameter(P_ID.MAX_TEMP3,         31, 0);
        parameters[31] = new Parameter(P_ID.MAX_TEMP4,         32, 0);
        parameters[32] = new Parameter(P_ID.MIN_TEMP1,         33, 0);
        parameters[33] = new Parameter(P_ID.MIN_TEMP2,         34, 0);
        parameters[34] = new Parameter(P_ID.MIN_TEMP3,         35, 0);
        parameters[35] = new Parameter(P_ID.MIN_TEMP4,         36, 0);
        parameters[36] = new Parameter(P_ID.TEMP_SENS5,        37, 0);
        parameters[37] = new Parameter(P_ID.TEMP_SENS6,        38, 0);
        parameters[38] = new Parameter(P_ID.TEMP_SENS7,        39, 0);
        parameters[39] = new Parameter(P_ID.TEMP_SENS8,        40, 0);
        parameters[40] = new Parameter(P_ID.SW_VERSION,        41, 0);
        parameters[41] = new Parameter(P_ID.BAT_FAST_CH,       42, 0);
        parameters[42] = new Parameter(P_ID.V_OUT_1_DEF,       43, 0);
        parameters[43] = new Parameter(P_ID.V_OUT_3_DEF,       44, 0);
        parameters[44] = new Parameter(P_ID.V_OUT_4_DEF,       45, 0);
        parameters[45] = new Parameter(P_ID.V_OUT_5_DEF,       46, 0);
        parameters[46] = new Parameter(P_ID.V_OUT_6_DEF,       47, 0);
        parameters[47] = new Parameter(P_ID.CHARGE_CYCLES,     48, 0);

    }

    public double getValById(P_ID id){
        for (Parameter p : this.parameters){
            if (p.getId() == id)
                return p.getDoubleValue();
        }
        return 0;
    }

    public String getStringValById(P_ID id){
        for (Parameter p : this.parameters){
            if (p.getId() == id)
                return String.valueOf(p.getDoubleValue());
        }
        return null;
    }

    public void setValById(P_ID id, double newVal){
        for (Parameter p : this.parameters){
            if (p.getId() == id)
                p.setDoubleValue(newVal);
        }
    }

    public void setValById(P_ID id, int newVal){
        for (Parameter p : this.parameters){
            if (p.getId() == id)
                p.setAdcValue(newVal);
        }
    }

    public Parameter[] getParameters() {
        return parameters;
    }

    public void setParameters(Parameter[] parameters) {
        this.parameters = parameters;
    }
}
