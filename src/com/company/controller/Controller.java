package com.company.controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Serhii Boiko on 04.08.2017.
 */
public class Controller extends KeyAdapter {
    private Queue<KeyEvent> keyEvents = new ArrayBlockingQueue<KeyEvent>(100);

    @Override
    public void keyPressed(KeyEvent e) {
        keyEvents.add(e);
    }

    public boolean hasKeyEvents() {
        return !keyEvents.isEmpty();
    }

    public KeyEvent getEventFromTop() {
        return keyEvents.poll();
    }
}
