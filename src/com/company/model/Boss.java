package com.company.model;

import com.company.controller.Space;

/**
 * Created by Serhii Boiko on 08.08.2017.
 */
public class Boss extends Ufo {
    private int hp;

    public Boss(double x, double y) {
        super(x, y);
        hp = Space.game.getLevel() * 2;
    }

    public int getHp() {
        return hp;
    }

    public void lostLife() {
        hp--;
    }

    @Override
    public void move() {
        double dx = Math.random() * 4 - 2;
        double dy = Math.random() * 4 - 2;

        x += dx;
        y += dy;

        checkBorders(radius, Space.game.getWidth() - radius + 1, radius - 1, Space.game.getHeight() / 2 - 1);

        int random10 = (int) (Math.random() * 15);
        if (random10 == 0)
            fire();
    }

    @Override
    public void fire() {
        Space.game.getBombs().add(new Bomb(x - 2, y + 3));
        Space.game.getBombs().add(new Bomb(x + 2, y + 3));
    }
}
