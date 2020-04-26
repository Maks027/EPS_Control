import lombok.Getter;
import lombok.Setter;

import java.util.Locale;

public class ThermalSim {
    @Getter @Setter
    private double h;       //Heat transfer coefficient [W/(m^2 * K)]
    @Getter @Setter
    private double A;       //Area [m^2]
    @Getter
    private double m;       //Mass [kg]
    @Getter
    private double Cp;      //Specific heat capacity [J / (kg * K)]

    private double Cth;     //Thermal mass [J / K]
    @Getter @Setter
    private double T_amb;   //Ambient temperature
    @Getter @Setter
    private double T;       //Current temperature
    @Getter @Setter
    private double Q;         //Current thermal energy [J] = [W*s]
    private double lastQ;     //Last value

    private double P_tr;      //Transferred power
    private double lastP_tr;  //Last value

    private double dT;        //Temperature difference
    @Getter @Setter
    private double exposedArea;
    @Getter @Setter
    private double radiationIntensity;
    @Getter @Setter
    private double receivedThermalPower;
    private boolean isSunny;

    public ThermalSim(double h, double a, double m, double cp) {
        this.h = h;
        A = a;
        this.m = m;
        Cp = cp;

        Cth = this.m * this.Cp;
        System.out.println(Cth);
    }

    public void initSunExposure(double radiationIntensity, double exposedArea, boolean isSunny) {
        this.radiationIntensity = radiationIntensity;
        this.exposedArea = exposedArea;
        this.isSunny = isSunny;
    }

    public void startSunExposure(){
        this.isSunny = true;
    }
    public void stopSunExposure(){
        this.isSunny = false;
    }

    public boolean isExposed(){
        return this.isSunny;
    }

    public double getThermalPower(){
        if (isSunny){
            return this.radiationIntensity * this.exposedArea;
        } else {
            return 0;
        }
    }

    public void setM(double m){
        this.m = m;
        this.Cth = this.m * this.Cp;
    }

    public void setCp(double Cp){
        this.Cp = Cp;
        this.Cth = this.m * this.Cp;
    }

    private double calcEnergy(double tempDiff){
        return tempDiff * this.Cth;
    }

    private double calcTempDiff(double ambientTemp, double bodyTemp){
        return ambientTemp - bodyTemp;
    }

    private double calcTransferredPower(double tempDiff){
        return this.h * this.A * tempDiff;
    }

    public double transferEnergy(long timeIncrement){
        this.P_tr = calcTransferredPower(calcTempDiff(this.T_amb, this.T));

        double Q_tr = P_tr * timeIncrement;

        this.Q += Q_tr;

        this.T = this.Q / this.Cth + this.T_amb;

        return this.Q;
    }

    public double transferEnergy(long timeIncrement, double addedPower){
        this.P_tr = calcTransferredPower(calcTempDiff(this.T_amb, this.T));
        this.P_tr += addedPower;
        double Q_tr = P_tr * timeIncrement;

        this.Q += Q_tr;

        this.T = this.Q / this.Cth + this.T_amb;

        return this.Q;
    }

    public void setInitEnergy(double ambientTemp, double bodyTemp){
        this.T = bodyTemp;
        this.T_amb = ambientTemp;
        this.Q = calcEnergy(bodyTemp - ambientTemp);
    }

    public double getTempCelsius() {
        return this.T - 273;
    }

    public static void main(String[] args) {
        ThermalSim thermalSim = new ThermalSim(14, 0.004, 0.15, 4500);

         thermalSim.setInitEnergy(0, 290);

        double Q;

        for (int i = 0 ; i < 1000 ; i++){
            thermalSim.transferEnergy(1);
            System.out.println(String.format(Locale.ROOT, "%.2f", thermalSim.getT()));

        }


    }
}
