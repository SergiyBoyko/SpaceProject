package com.company.model;

import com.company.controller.Space;

/**
 * Класс для космического корабля
 */
public class SpaceShip extends BaseObject {
    //картинка корабля для отрисовки
    private static int[][] matrix = {
            {0, 0, 0, 0, 0},
            {0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0},
            {1, 0, 1, 0, 1},
            {1, 1, 1, 1, 1},
    };

    private int hp;
    private int level;

    public int getHp() {
        return hp;
    }

    public void lostLife() {
        hp--;
    }

    public void lifeUp() {
        hp++;
    }

    public void levelUp() {
        level++;
    }

    public int getLevel() {
        return level;
    }

    public double getDx() {
        return dx;
    }

    public void extRadius() {
        this.radius += 1;
    }

    //вектор движения (-1 влево,+1 вправо)
    private double dx = 0;

    public SpaceShip(int x, int y) {
        super(x, y, 2);
        level = 0;
        hp = 3;
    }

    public void stopMove() {
        dx = 0;
    }

    /**
     * Устанавливаем вектор движения влево
     */
    public void moveLeft() {
        dx = -1;
    }

    /**
     * Устанавливаем вектор движения вправо
     */
    public void moveRight() {
        dx = 1;
    }

    /**
     * Метод рисует свой объект на "канвасе".
     */
    @Override
    public void draw(Canvas canvas) {
        canvas.drawMatrix(x - radius + 1, y - radius + 1, matrix, 'M');
    }

    /**
     * Двигаем себя на один ход.
     * Проверяем столкновение с границами.
     */
    @Override
    public void move() {
        x = x + dx;

        checkBorders(radius, Space.game.getWidth() - radius + 1, 1, Space.game.getHeight() + 1);
    }

    /**
     * Стреляем.
     * Создаем две ракеты: слева и справа от корабля.
     */
    public void fire() {
        if (level == 0)
            Space.game.getRockets().add(new Rocket(x, y-1));
        else if (level == 1) {
            Space.game.getRockets().add(new Rocket(x - 1, y));
            Space.game.getRockets().add(new Rocket(x + 1, y));
        } else {
            Space.game.getRockets().add(new Rocket(x, y - 1));
            Space.game.getRockets().add(new Rocket(x - 2, y));
            Space.game.getRockets().add(new Rocket(x + 2, y));

        }
    }
}
