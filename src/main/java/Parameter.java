public class Parameter {
    private int id;
    private int adcValue;
    private String name;

    private double k;
    private double doubleValue;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAdcValue() {
        return adcValue;
    }

    public void setAdcValue(int adcValue) {
        this.adcValue = adcValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Parameter(){

    }

    public Parameter(String name, int id, double value, double k) {
        this.id = id;
        this.doubleValue = value;
        this.k = k;
        this.adcValue = floatToADC(value, k);
        this.name = name;
    }



    public int floatToADC(double val, double k){
        return (int)Math.round(val * k);
    }
}
