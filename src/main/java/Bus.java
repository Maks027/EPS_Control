import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;


public class Bus {

    public Bus(double requiredPower, double busVoltage, double efficiency, JTextField textFieldPower, JTextField textFieldCurrent) {
        this.requiredPower = requiredPower;
        this.requiredCurrent = requiredPower / busVoltage;
        this.efficiency = efficiency;
        this.textFieldCurrent = textFieldCurrent;
        this.textFieldPower = textFieldPower;
        this.textFieldCurrent.setText(String.format(Locale.ROOT, "%.2f", this.requiredCurrent));
        this.textFieldPower.setText(String.format(Locale.ROOT, "%.2f", this.requiredPower));
        this.busVoltage = busVoltage;
    }
    public Bus(){}

    @Setter @Getter
    private JTextField textFieldCurrent;
    @Setter @Getter
    private JTextField textFieldPower;
    @Getter
    private double requiredPower;
    @Getter
    private double requiredCurrent;
    @Setter @Getter
    private double consumedPower;
    @Setter @Getter
    private double consumedCurrent;
    @Setter @Getter
    private double efficiency;
    @Getter
    private TimeSim.BusState busState;
    @Setter @Getter
    private double busVoltage;


    public void setRequiredPower(){
        this.requiredPower = this.consumedPower = Double.valueOf(textFieldPower.getText());
        this.requiredCurrent = this.consumedCurrent = this.requiredPower / this.busVoltage;
        this.textFieldCurrent.setText(String.format(Locale.ROOT, "%.2f", this.requiredCurrent));
    }

    public void setRequiredPower(double voltage){
        this.requiredPower = this.consumedPower = Double.valueOf(textFieldPower.getText());
        this.requiredCurrent = this.consumedCurrent = requiredPower / voltage;
        this.textFieldCurrent.setText(String.format(Locale.ROOT, "%.2f", this.requiredCurrent));
    }

    public void setRequiredCurrent(){
        this.requiredCurrent = Double.valueOf(textFieldCurrent.getText());
        this.requiredPower = this.requiredCurrent * this.busVoltage;
        this.textFieldPower.setText(String.format(Locale.ROOT, "%.2f", this.requiredPower));
    }

    public void turnON(){
        this.busState = TimeSim.BusState.ON;

        this.consumedCurrent = this.requiredCurrent;
        this.consumedPower = this.requiredPower;
        this.textFieldPower.setBackground(Color.white);
        this.textFieldPower.setBackground(Color.white);
        this.textFieldCurrent.setText(String.format(Locale.ROOT, "%.2f", this.requiredCurrent));
        this.textFieldPower.setText(String.format(Locale.ROOT, "%.2f", this.requiredPower));
    }
    public void turnOFF(){
        this.busState = TimeSim.BusState.OFF;

        this.consumedCurrent = 0;
        this.consumedPower = 0;
        this.textFieldPower.setBackground(Color.red);
        this.textFieldCurrent.setBackground(Color.red);

    }
}
