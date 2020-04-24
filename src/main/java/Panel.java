import lombok.Getter;
import lombok.Setter;

import java.util.Locale;
import java.util.Random;

public class Panel {

    private String name;
    @Setter
    private double generatedVoltage;
    private double tempGeneratedVoltage;
    @Getter
    private double generatedNegativeCurrent;
    @Getter
    private double generatedPositiveCurrent;
    @Getter
    private double generatedPower;
    private double tempGeneratedPower;

    private double efficiency;

    private boolean onState;
    private boolean sun;

    private final double voltageVariance = 0.05;
    private final double upperVoltageLimit = 4.9;
    private final double lowerVoltageTimit = 4.3;

    private Random random;

    public Panel(String name, double generatedVoltage, double efficiency) {
        this.tempGeneratedVoltage = this.generatedVoltage = generatedVoltage;

        this.efficiency = efficiency;
        random = new Random();
        this.name = name;
        onState = true;
        sun = true;
    }

    private double addNoise(double voltage){
        if (voltage <= (upperVoltageLimit)){
            if (voltage >= (lowerVoltageTimit)) {
                voltage = voltage + ((random.nextDouble() * 2 * voltageVariance )- voltageVariance);
            } else {
                voltage = lowerVoltageTimit;
            }
        } else {
            voltage = upperVoltageLimit;
        }
        return voltage;
    }

    private void getCurrents(){
        generatedPositiveCurrent  = generatedPower / generatedVoltage;
        generatedNegativeCurrent = 0 - generatedPositiveCurrent;
    }

    public double getGeneratedVoltage(){
        if (onState && sun){
            generatedVoltage = addNoise(generatedVoltage);
            getCurrents();
            tempGeneratedVoltage = generatedVoltage;
        } else {
            generatedVoltage = 0;
        }
        return this.generatedVoltage;
    }

    public void setGeneratedPower(double generatedPower){
        this.generatedPower = generatedPower * 100 / efficiency;
        tempGeneratedPower = this.generatedPower;
        getCurrents();
    }

    public void enablePanel() {
        onState = true;
        if (sun){
            this.generatedPower = tempGeneratedPower;
            this.generatedVoltage = tempGeneratedVoltage;
            getCurrents();
        }
    }

    public void disablePanel() {
        onState = false;
        this.generatedPower = 0;
        this.generatedVoltage = 0;
        this.generatedNegativeCurrent = 0;
        this.generatedPositiveCurrent = 0;
    }

    public void startGeneration(){
        sun = true;
        if (onState){
            this.generatedPower = tempGeneratedPower;
            this.generatedVoltage = tempGeneratedVoltage;
            getCurrents();
        }
    }

    public void stopGeneration(){
        sun = false;
        this.generatedPower = 0;
        this.generatedVoltage = 0;
        this.generatedNegativeCurrent = 0;
        this.generatedPositiveCurrent = 0;
    }

    public static void main(String[] args) {
        Panel X = new Panel("X", 4.6, 92);


        for (long i = 0 ; i < 10000; i++){

            System.out.println(String.format(Locale.ROOT, "%.2f", X.generatedVoltage));
        }
    }
}
