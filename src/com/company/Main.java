package com.company;


import com.company.controller.Space;
import com.company.model.SpaceShip;

import javax.swing.*;

public class Main {

    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame();
        frame.setTitle("SpaceWAR - alpha");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(950, 625);
        frame.setResizable(false);


        Space.game = new Space(30, 25);
        Space.game.setShip(new SpaceShip(Space.game.getWidth() / 2, Space.game.getHeight() - 2));

        frame.add(Space.game.getView());

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        Space.game.run();
    }

}
