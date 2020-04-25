import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;


class BatteryHeater{
    private double consumedCurrent;
    private boolean onState;

    public BatteryHeater(double consumedCurrent, boolean onState) {
        this.consumedCurrent = consumedCurrent;
        this.onState = onState;
    }

    public double getGeneratedPower(double voltage){
        if (onState){
            return this.consumedCurrent * voltage / 1000;
        } else {
            return 0;
        }
    }
    public double getConsumedCurrent(){
        if (onState){
            return this.consumedCurrent;
        } else {
            return 0;
        }
    }

    public void turnOn(){
        this.onState = true;
    }

    public void turnOff() {
        this.onState = false;
    }

    public boolean isOn() {
        return this.onState;
    }

}

public class Battery {
    LinkedHashMap<Double, Double> chargeToVoltage = new LinkedHashMap<>();
    private double batteryCapacity;
    private double stateOfCharge;

    @Getter
    private double batteryTemperature;
    private double tempSens1;
    private double tempSens2;
    private double tempSens3;

    private double totalGeneratedHeat;
    public BatteryHeater heater1;
    public BatteryHeater heater2;
    public BatteryHeater heater3;


    public Battery(double batteryCapacity, double stateOfCharge) {
        this.batteryCapacity = batteryCapacity;
        this.stateOfCharge = stateOfCharge;

        loadDataFromFile(new File("src/main/resources/battery/charge_to_voltage.txt"), chargeToVoltage);
        heater1 = new BatteryHeater(150, false);
        heater2 = new BatteryHeater(200, false);
        heater3 = new BatteryHeater(230, false);
    }

    public void autoHeat(double temperature){
        this.batteryTemperature = temperature;

        if (this.stateOfCharge > 0) {
            if (batteryTemperature <= 5) {
                heater1.turnOn();
                if (batteryTemperature <= 1){
                    heater2.turnOn();
                    if (batteryTemperature <= -2) {
                        heater3.turnOn();
                    } else if (batteryTemperature > 0){
                        heater3.turnOff();
                    }
                } else if (batteryTemperature > 4) {
                    heater2.turnOff();
                    heater3.turnOff();
                }
            } else if (batteryTemperature > 8){
                heater1.turnOff();
                heater2.turnOff();
                heater3.turnOff();
            }
        } else {
            heater1.turnOff();
            heater2.turnOff();
            heater3.turnOff();
        }
    }

    public void setBatteryTemperature(double temperature){
        this.batteryTemperature = temperature;
    }


    private double getHeatersCurrent(){
        return heater1.getConsumedCurrent() + heater2.getConsumedCurrent() + heater3.getConsumedCurrent();
    }

    public double getTotalGeneratedHeat() {
        double voltage = this.getBatteryVoltage();
        return heater1.getGeneratedPower(voltage) + heater2.getGeneratedPower(voltage) + heater3.getGeneratedPower(voltage);
    }

    public void loadDataFromFile(File file, LinkedHashMap<Double, Double> map){
        Scanner sc = null;
        try {
            sc = new Scanner(file).useDelimiter("\r\n");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String[] valPair;
        double xVal;
        double yVal;

        while (sc.hasNext()){
            valPair = sc.next().split(",");
            xVal = Double.valueOf(valPair[0]) * batteryCapacity;
            yVal = Double.valueOf(valPair[1]);
            map.put(xVal, yVal);
        }
    }

    public void BatteryCharge(long timeIncrement, double current){ //time in seconds, current in milliamps
        if (stateOfCharge < batteryCapacity)
            stateOfCharge += (double) timeIncrement / 3600 * current; //mAh;
        else
            stateOfCharge = batteryCapacity;
    }

    public void BatteryDischarge(long timeIncrement, double current){
        if (stateOfCharge > 0)
            stateOfCharge -= (double) timeIncrement / 3600 * current; //mAh;
        else
            stateOfCharge = 0;
    }

    public double ModifySOC(long timeIncrement, double current){
        if ((stateOfCharge >= (0.8 * batteryCapacity) && (current > 0))){
            double k = Math.log(batteryCapacity - stateOfCharge) / Math.log(batteryCapacity - 0.8 * batteryCapacity);
            if (k <= 0)
                k = 0;
            current = current * k;
        }

        if ((current < 0) && (stateOfCharge <= 0)){ //If the battery is discharging
            current = 0;
        }

        if (stateOfCharge < batteryCapacity)
            if (stateOfCharge >= 0)
                stateOfCharge += (double) timeIncrement / 3600 * current; //mAh;
            else {
                stateOfCharge = 0;
            }
        else
            stateOfCharge = batteryCapacity;

        return  current;
    }

    public double getBatteryVoltage(){
        double voltage = 0;
//        if (stateOfCharge < this.batteryCapacity){
            for (Map.Entry<Double, Double> e : chargeToVoltage.entrySet()){
                if (e.getKey() <= stateOfCharge){

                    voltage = e.getValue();
                    break;
                }
            }
//        }else{
//            voltage = chargeToVoltage.get(batteryCapacity);
//        }
        return voltage;
    }

    public double getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(double batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
        chargeToVoltage.clear();
        loadDataFromFile(new File("src/main/resources/battery/charge_to_voltage.txt"), chargeToVoltage);
        //chargeToVoltage.forEach((k, v) -> System.out.println(k + "  " + v));
    }

    public double getStateOfCharge() {
        return stateOfCharge;
    }

    public void setStateOfCharge(double stateOfCharge) {
        this.stateOfCharge = stateOfCharge;
    }


    public static void main(String[] args) {
//        Battery bat = new Battery(2800);
//
//        bat.chargeToVoltage.forEach((k, v) -> System.out.println(k + "  " + v));
//
//
//        System.out.println(bat.getBatteryVoltage(500));


    }
}