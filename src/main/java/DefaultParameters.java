import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class DefaultParameters {

    private final double defK = 1240.9;

    private List<Parameter> parameterList;

    private Parameter batteryVoltage    = new Parameter("Battery Voltage",   1, 0, defK);
    private Parameter batteryCurrent    = new Parameter("Battery Current",   2, 0, defK);

    private Parameter BCRVoltage        = new Parameter("BCR Voltage",       3, 0, defK);
    private Parameter BCRCurrent        = new Parameter("BCR Current",       4, 0, defK);

    private Parameter XVoltage          = new Parameter("X Voltage",         5, 0, defK);
    private Parameter XCurrentPos       = new Parameter("X Current+",        6, 0, defK);
    private Parameter XCurrentNeg       = new Parameter("X Current-",        7, 0, defK);

    private Parameter YVoltage          = new Parameter("Y Voltage",         8, 0, defK);
    private Parameter YCurrentPos       = new Parameter("Y Current+",        9, 0, defK);
    private Parameter YCurrentNeg       = new Parameter("Y Current-",        10, 0, defK);

    private Parameter ZVoltage          = new Parameter("Z Voltage",         11, 0, defK);
    private Parameter ZCurrentPos       = new Parameter("Z Current+",        12, 0, defK);
    private Parameter ZCurrentNeg       = new Parameter("Z Current-",        13, 0, defK);

    private Parameter current3V3        = new Parameter("3.3V Current",      14, 0, defK);
    private Parameter current5V         = new Parameter("5V Current",        15, 0, defK);

    public DefaultParameters() {
        parameterList = new LinkedList<Parameter>();

        parameterList.add(batteryVoltage);
        parameterList.add(batteryCurrent);
        parameterList.add(BCRVoltage);
        parameterList.add(BCRCurrent);
        parameterList.add(XVoltage);
        parameterList.add(XCurrentPos);
        parameterList.add(XCurrentNeg);
        parameterList.add(YVoltage);
        parameterList.add(YCurrentPos);
        parameterList.add(YCurrentNeg);
        parameterList.add(ZVoltage);
        parameterList.add(ZCurrentPos);
        parameterList.add(ZCurrentNeg);
        parameterList.add(current3V3);
        parameterList.add(current5V);

    }

    public void modifyVal(int id,double newVal , List<Parameter> list){
        for(Parameter p: list){
            if (p.getId() == id) {
                p.setDoubleValue(newVal);
                p.setAdcValue(p.floatToADC(newVal, p.getK()));
            }
        }
    }

    public void modifyVal(String name ,double newVal , List<Parameter> list){
        for(Parameter p: list){
            if (p.getName().equals(name)) {
                p.setDoubleValue(newVal);
                p.setAdcValue(p.floatToADC(newVal, p.getK()));
            }
        }
    }

    public List<Parameter> getParameterList() {
        return parameterList;
    }

    public void setParameterList(List<Parameter> parameterList) {
        this.parameterList = parameterList;
    }
}
