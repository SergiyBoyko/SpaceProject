package com.company.model;

/**
 * Created by Serhii Boiko on 09.08.2017.
 */
public class Explosion extends BaseObject {
    private int frame;
    public Explosion(double x, double y) {
        super(x, y, 0.1);
        frame = 9;
    }

    public void removeOneFrame() {
        frame--;
    }

    public int getFrame() {
        return frame;
    }
}
