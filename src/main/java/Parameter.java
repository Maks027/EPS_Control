public class Parameter {
    private int address;
    private int adcValue;
    private double k = 1;
    private double doubleValue;
    P_ID id;

    public Parameter(){}

    public Parameter(P_ID id, int address, double value) {
        this.address = address;
        this.doubleValue = value;
        this.id = id;
        k = 1;
    }

    public Parameter(P_ID id, int address, double value, double ADCRef) {
        this.address = address;
        this.doubleValue = value;
        this.id = id;

        this.k = 4095 / ADCRef;
        this.adcValue = (int)Math.round(this.k * this.doubleValue);
    }

    public double getDoubleValue() {
        return doubleValue;
    }

    public void setDoubleValue(double doubleValue) {
        this.doubleValue = doubleValue;
        reCalcAdcVal();
    }

    public void setDoubleValue(String stringValue) {
        this.doubleValue = Double.valueOf(stringValue);
        reCalcAdcVal();
    }

    public double getK() {
        return k;
    }

    public void setK(double k) {
        this.k = k;
        reCalcAdcVal();
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public int getAdcValue() {
        return adcValue;
    }

    public void setAdcValue(int adcValue) {
        this.adcValue = adcValue;
    }

    public P_ID getId() {
        return id;
    }

    public void setId(P_ID id) {
        this.id = id;
    }

    public void reCalcAdcVal(){
        this.adcValue = (int)Math.round(this.getDoubleValue() * this.k);
    }
}
