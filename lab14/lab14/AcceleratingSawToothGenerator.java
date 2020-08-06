package lab14;

import lab14lib.Generator;

public class AcceleratingSawToothGenerator implements Generator {
    private double period, factor;
    private int state;

    public AcceleratingSawToothGenerator(double period, double factor) {
        state = 0;
        this.period = period;
        this.factor = factor;
    }

    public double next() {
        state = (state + 1) % (int) period;
        if (state == 0)
            period = period * factor;
        return state * 2 / period - 1;
    }

}
