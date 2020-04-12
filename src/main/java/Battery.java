import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Battery {
    LinkedHashMap<Double, Double> chargeToVoltage = new LinkedHashMap<>();

    private double batteryCapacity;

    public Battery(double batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
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

    public double BatteryCharge(long time, double current){ //time in seconds, current in milliamps
        double capacity = (double) time / 3600 * current; //mAh;
        return capacity;
    }

    public double BatteryDischarge(long time, double current){
        double capacity = batteryCapacity - ((double) time / 3600 * current);
        return capacity;
    }

    public double getBatteryVoltage(double capacity){
        double voltage = 0;
        if (capacity < this.batteryCapacity){
            for (Map.Entry<Double, Double> e : chargeToVoltage.entrySet()){
                if (e.getKey() <= capacity){
                    voltage = e.getValue();
                    break;
                }
            }
        }else{
            voltage = chargeToVoltage.get(batteryCapacity);
        }
        return voltage;
    }


    public double getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(double batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    public static void main(String[] args) {
        Battery bat = new Battery(2800);

        bat.chargeToVoltage.forEach((k, v) -> System.out.println(k + "  " + v));


        System.out.println(bat.getBatteryVoltage(500));


    }
}