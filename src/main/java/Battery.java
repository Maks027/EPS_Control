import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Battery {
    LinkedHashMap<Double, Double> chargeToVoltage = new LinkedHashMap<>();
    private double batteryCapacity;
    private double stateOfCharge;

    public Battery(double batteryCapacity, double stateOfCharge) {
        this.batteryCapacity = batteryCapacity;
        this.stateOfCharge = stateOfCharge;
        loadDataFromFile(new File("src/main/resources/battery/charge_to_voltage.txt"), chargeToVoltage);
//        loadDataFromFile(new File("src/main/resources/battery/charge_to_voltage.txt"), chargeCurve);
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