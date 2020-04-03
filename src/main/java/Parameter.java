public class Parameter {
    private int address;
    private int adcValue;
    private double k;
    private double doubleValue;
    P_ID id;

    public Parameter(){}

    public Parameter(P_ID id, int address, double value) {
        this.address = address;
        this.doubleValue = value;
        this.id = id;
    }

    public double getDoubleValue() {
        return doubleValue;
    }

    public void setDoubleValue(double doubleValue) {
        this.doubleValue = doubleValue;
    }

    public double getK() {
        return k;
    }

    public void setK(double k) {
        this.k = k;
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

    public int floatToADC(double val, double k){
        return (int)Math.round(val * k);
    }

    public void calcAdcVal(double val, double k){
        this.k = k;
        this.adcValue = floatToADC(val, this.k);
    }
}
