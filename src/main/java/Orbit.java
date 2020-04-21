import lombok.Getter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class Orbit {

    private final BigInteger R = new BigInteger("6371000"); //Earth radius
    private final BigInteger GM = new BigInteger("398600000000000"); // G*M

    private final long earthRadius = 6371000;
    private long orbitRadius;
    private long angle;

    @Getter
    private double shadowPercent;
    @Getter
    private long orbitPeriod;
    @Getter
    private double orbitVelocity;
    @Getter
    private long orbitHeight;

    public Orbit(long orbitPeriod) {
        this.orbitPeriod = orbitPeriod;
        calcOrbitVelocityAndHeight(orbitPeriod);
        calcShadowPercent();
    }

    public void calcOrbitVelocityAndHeight(long period){
        this.orbitPeriod = period * 60;
        // R = (T^2 * G * M / 4 * pi()^2)^(1 / 3)
        this.orbitRadius = new CubicRoot().root(3, new BigDecimal(GM.multiply(BigInteger.valueOf(this.orbitPeriod * this.orbitPeriod)))
                .divide(BigDecimal.valueOf(39.4784), 2, RoundingMode.HALF_DOWN)).longValue();
        this.orbitHeight = this.orbitRadius - this.earthRadius;
        this.orbitVelocity = GM.divide(R.add(BigInteger.valueOf(this.orbitHeight))).sqrt().doubleValue(); // V = sqrt(G * M / R)
        calcShadowPercent();
    }

    public void calcOrbitPeriodAndHeight(double velocity){
        this.orbitVelocity = velocity * 1000;
        // Rorb = (G * M) / (V^2)
        this.orbitRadius = GM.divide(BigInteger.valueOf((long)(this.orbitVelocity * this.orbitVelocity))).longValue();
        // Horb = Rorb - Rearth
        this.orbitHeight = this.orbitRadius - this.earthRadius;
        // T = 2 * pi() * R / V
        this.orbitPeriod = (long)(6.28 * this.orbitRadius / this.orbitVelocity);
        calcShadowPercent();

    }

    public void calcOrbitPeriodAndVelocity(long height){
        this.orbitHeight = height * 1000;
        this.orbitRadius = this.orbitHeight + earthRadius;
        this.orbitPeriod = (long)(2 * Math.PI * (R.doubleValue() + this.orbitHeight) / this.orbitVelocity); // T = 2 * pi() * Rorb / V
        this.orbitVelocity = GM.divide(BigInteger.valueOf(this.orbitRadius)).sqrt().doubleValue(); //V = sqrt(G * M / R) (m/s)
        calcShadowPercent();
    }

    private void calcShadowPercent(){
        this.angle = 2 * (long) Math.toDegrees(Math.asin((double) this.earthRadius / this.orbitRadius));
        this.shadowPercent = (double) this.angle / 360;
    }

}
