package com.company.controller;

import com.company.View;
import com.company.model.*;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Главный класс игры - Космос (Space)
 */
public class Space {
    //Ширина и высота игрового поля
    private int width;
    private int height;

    private View view;
    private Controller controller;
//    private Canvas canvas;

    //Космический корабль
    private SpaceShip ship;
    //Список НЛО
    private ArrayList<Ufo> ufos = new ArrayList<Ufo>();
    //Список бомб
    private ArrayList<Bomb> bombs = new ArrayList<Bomb>();
    //Список ракет
    private ArrayList<Rocket> rockets = new ArrayList<Rocket>();
    //Список босов
    private ArrayList<Boss> bosses = new ArrayList<Boss>();
    //Список взрывов
    private ArrayList<Explosion> explosions = new ArrayList<Explosion>();

    private int level;
    private int score;
    private boolean bossAval;
    private boolean pause;
    private boolean intro;
    private boolean start;
    private int textX, textY;
    private boolean secondText;
    private boolean showPressEnter;
    private boolean enterPressed;

    public boolean isEnterPressed() {
        return enterPressed;
    }

    public boolean isSecondText() {
        return secondText;
    }

    public int getTextX() {
        return textX;
    }

    public int getTextY() {
        return textY;
    }

    public boolean isBossAval() {
        return bossAval;
    }

    public boolean isPause() {
        return pause;
    }

    public int getScore() {
        return score;
    }

    public int getLevel() {
        return level;
    }

    public boolean isIntro() {
        return intro;
    }

    public boolean isStart() {
        return start;
    }

    public boolean isShowPressEnter() {
        return showPressEnter;
    }

    public Space(int width, int height) {
        this.width = width;
        this.height = height;
        controller = new Controller();
        view = new View(controller);
    }

    private void showGameStart() {
        intro = true;
        start = false;
        secondText = false;
        showPressEnter = false;
        enterPressed = false;
        while (intro) {
            view.repaint();
            Space.sleep(3000);
            start = true;
            view.repaint();
            textX = 462/2;
            textY = -200;
            while (textY < 50) {
                textY+=10;
                view.repaint();
                Space.sleep(50);
            }
            secondText = true;
            textX = 347/2;
            textY = -200;
            while (textY < 150) {
                textY+=10;
                view.repaint();
                Space.sleep(50);
            }
            showPressEnter = true;
            view.repaint();
            while (true) {
                if (controller.hasKeyEvents() && controller.getEventFromTop().getKeyCode() == KeyEvent.VK_ENTER) {
                    break;
                }
                Space.sleep(300);
                showPressEnter = !showPressEnter;
                view.repaint();
            }
            enterPressed = true;
            view.repaint();
            Space.sleep(3000);
            intro = false;
            start = false;
        }
    }
    /**
     * Основной цикл программы.
     * Тут происходят все важные действия
     */
    public void run() {
        //Приветствие
        showGameStart();
        //Создаем холст для отрисовки.

        pause = false;
        //Создаем объект "наблюдатель за клавиатурой" и стартуем его.
        bossAval = false;
        while (true) {
            //Игра работает, пока корабль жив
            while (ship.isAlive() && !view.isGameWon()) {

                if (controller.hasKeyEvents() && controller.getEventFromTop().getKeyCode() == KeyEvent.VK_P) {
                    pause = !pause;
                }

                while (!pause && ship.isAlive()) {

                    level = score / 40;
                    if (level > 9 && ship.getLevel() < 1) {
                        ship.extRadius();
                        ship.levelUp();
                        ship.lifeUp();
                        ship.lifeUp();
                    }
                    if (level > 19 && ship.getLevel() < 2) {
                        ship.levelUp();
                        ship.lifeUp();
                    }

                    if (level > 34 && !bossAval) {
                        createBoss();
                        bossAval = true;
                    }

                    //"наблюдатель" содержит события о нажатии клавиш?
                    if (controller.hasKeyEvents()) {
                        KeyEvent event = controller.getEventFromTop();
                        //Если "стрелка влево" - сдвинуть фигурку влево
//                        System.out.print(event.getKeyCode());
                        if (event.getKeyCode() == KeyEvent.VK_LEFT)
                            ship.moveLeft();
                            //Если "стрелка вправо" - сдвинуть фигурку вправо
                        else if (event.getKeyCode() == KeyEvent.VK_RIGHT)
                            ship.moveRight();
                        else if (event.getKeyCode() == KeyEvent.VK_DOWN)
                            ship.stopMove();
                            //Если "пробел" - запускаем шарик
                        else if (event.getKeyCode() == KeyEvent.VK_SPACE)
                            ship.fire();
                        else if (event.getKeyCode() == KeyEvent.VK_Q) {
                            System.exit(0);
                        } else if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
                            restart();
                        } else if (event.getKeyCode() == KeyEvent.VK_P) {
                            pause = !pause;
                        }
                    }

                    //двигаем все объекты игры
                    moveAllItems();

                    //проверяем столкновения
                    checkBombs();
                    checkRockets();
                    //удаляем умершие объекты из списков

                    //Создаем НЛО (1 раз в 10 ходов)
                    createUfo();

                    //Отрисовываем все объекты на холст, а холст выводим на экран
//                    canvas.clear();
//                    draw(canvas);
//                    canvas.print();
                    view.repaint();

                    //Пауза 300 миллисекунд
                    Space.sleep(70);
                    if (ship.getHp() != 0 && !ship.isAlive()) {
                        ship.rise();
                        ship.lostLife();
                    }
                    for (Boss boss : bosses) {
                        if (boss.getHp() != 0 && !boss.isAlive()) {
                            boss.rise();
                            boss.lostLife();
                        }
                    }
                    removeDead();

                    if (bossAval && bosses.isEmpty()) {
                        score += 1000;
                        view.setGameWon(true);
                        break;
                    }

                }
            }

            if (!view.isGameLost() && !ship.isAlive()) {
                view.setGameLost(true);
                view.repaint();
                while (true) {
                    if (controller.hasKeyEvents() && controller.getEventFromTop().getKeyCode() == KeyEvent.VK_ESCAPE) {
                        restart();
                        break;
                    }

                    if (controller.hasKeyEvents() && controller.getEventFromTop().getKeyCode() == KeyEvent.VK_ESCAPE) {
                        restart();
                    }

                    if (controller.hasKeyEvents() && controller.getEventFromTop().getKeyCode() == KeyEvent.VK_Q) {
                        System.exit(0);
                    }
                }
                System.out.println("Game Over!");
            }

            if (view.isGameWon() && ship.isAlive()) {
                view.repaint();
                while (true) {
                    if (controller.hasKeyEvents() && controller.getEventFromTop().getKeyCode() == KeyEvent.VK_ESCAPE) {
                        restart();
                        break;
                    }

                    if (controller.hasKeyEvents() && controller.getEventFromTop().getKeyCode() == KeyEvent.VK_ESCAPE) {
                        restart();
                    }

                    if (controller.hasKeyEvents() && controller.getEventFromTop().getKeyCode() == KeyEvent.VK_Q) {
                        System.exit(0);
                    }
                }
            }


            //Выводим сообщение "Game Over"
        }
    }

    private void showIntro() {

    }

    private void restart() {
        view.setGameLost(false);
        view.setGameWon(false);
        ship = new SpaceShip(width / 2, height - 2);
        ufos = new ArrayList<Ufo>();
        rockets = new ArrayList<Rocket>();
        bombs = new ArrayList<Bomb>();
        bosses = new ArrayList<Boss>();
        score = 0;
        bossAval = false;
        pause = false;
    }

    /**
     * Двигаем все объекты игры
     */
    public void moveAllItems() {
        for (BaseObject object : getAllItems()) {
            object.move();
        }
    }

    /**
     * Метод возвращает общий список, который содержит все объекты игры
     */
    public List<BaseObject> getAllItems() {
        ArrayList<BaseObject> list = new ArrayList<BaseObject>(ufos);
        list.add(ship);
        list.addAll(bombs);
        list.addAll(rockets);
        list.addAll(bosses);
        return list;
    }

    /**
     * Создаем новый НЛО. 1 раз на 10 вызовов.
     */
    public void createUfo() {
        if (ufos.size() > level / 4 || bossAval) return;

        int random10 = (int) (Math.random() * 10);
        if (random10 == 0) {
            double x = Math.random() * width;
            double y = Math.random() * height / 2;
            ufos.add(new Ufo(x, y));
        }
    }

    public void createBoss() {
        double x = Math.random() * width;
        double y = Math.random() * height / 4;
        bosses.add(new Boss(x, y));
    }

    /**
     * Проверяем бомбы.
     * а) столкновение с кораблем (бомба и корабль умирают)
     * б) падение ниже края игрового поля (бомба умирает)
     */
    public void checkBombs() {
        for (Bomb bomb : bombs) {
            if (ship.isIntersect(bomb)) {
                ship.die();
                bomb.die();
            }

            if (bomb.getY() >= height)
                bomb.die();
        }
    }

    /**
     * Проверяем рокеты.
     * а) столкновение с НЛО (ракета и НЛО умирают)
     * б) вылет выше края игрового поля (ракета умирает)
     */
    public void checkRockets() {
        for (Rocket rocket : rockets) {
            for (Ufo ufo : ufos) {
                if (ufo.isIntersect(rocket)) {
                    ufo.die();
                    rocket.die();
                    score += 10;
                }
            }

            for (Boss boss : bosses) {
                if (boss.isIntersect(rocket)) {
                    rocket.die();
                    boss.die();
                    score += 5;
                }
            }
            if (rocket.getY() <= 0)
                rocket.die();
        }
    }

    /**
     * Удаляем умерсшие объекты (бомбы, ракеты, НЛО) из списков
     */
    public void removeDead() {
        for (BaseObject object : new ArrayList<BaseObject>(bombs)) {
            if (!object.isAlive())
                bombs.remove(object);
        }

        for (BaseObject object : new ArrayList<BaseObject>(rockets)) {
            if (!object.isAlive())
                rockets.remove(object);
        }

        for (BaseObject object : new ArrayList<BaseObject>(ufos)) {
            if (!object.isAlive())
                ufos.remove(object);
        }

        for (BaseObject object : new ArrayList<BaseObject>(bosses)) {
            if (!object.isAlive())
                bosses.remove(object);
        }

        for (BaseObject object : new ArrayList<BaseObject>(explosions)) {
            if (!object.isAlive())
                bosses.remove(object);
        }
    }

    /**
     * Отрисовка всех объектов игры:
     * а) заполняем весь холст точесками.
     * б) отрисовываем все объекты на холст.
     */
//    public void draw(Canvas canvas) {
//        //draw game
//        for (int i = 0; i < width + 2; i++) {
//            for (int j = 0; j < height + 2; j++) {
//                canvas.setPoint(i, j, '.');
//            }
//        }
//
//        for (int i = 0; i < width + 2; i++) {
//            canvas.setPoint(i, 0, '-');
//            canvas.setPoint(i, height + 1, '-');
//        }
//
//        for (int i = 0; i < height + 2; i++) {
//            canvas.setPoint(0, i, '|');
//            canvas.setPoint(width + 1, i, '|');
//        }
//
//        for (BaseObject object : getAllItems()) {
//            object.draw(canvas);
//        }
//    }

    public SpaceShip getShip() {
        return ship;
    }

    public void setShip(SpaceShip ship) {
        this.ship = ship;
    }

    public ArrayList<Ufo> getUfos() {
        return ufos;
    }

    public ArrayList<Boss> getBosses() {
        return bosses;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ArrayList<Bomb> getBombs() {
        return bombs;
    }

    public ArrayList<Rocket> getRockets() {
        return rockets;
    }

    public ArrayList<Explosion> getExplosions() {
        return explosions;
    }

    public View getView() {
        return view;
    }

//    public Canvas getCanvas() {
//        return canvas;
//    }

    public static Space game;

    /**
     * Метод делает паузу длинной delay миллисекунд.
     */
    public static void sleep(int delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
        }
    }
}
