package com.rakaneth.view;

import com.valkryst.VTerminal.component.VPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Stack;

final public class UIStack {
    public static UIStack instance = null;
    final private Stack<GameView> viewStack;
    private UIStack() {
        viewStack = new Stack<>();
    }

    public static UIStack getInstance() {
        if (instance == null) {
            instance = new UIStack();
        }
        return instance;
    }


    public void render(VPanel panel) {
        panel.reset();
        for (GameView view: viewStack) {
            view.render(panel);
        }
        panel.repaint();
    }

    public void handle(KeyEvent key) {
        viewStack.peek().handle(key);
    }

    public void pop(){
        viewStack.pop();
    }

    public GameView peek() {
        return viewStack.peek();
    }

    public void push(GameView view) {
        viewStack.push(view);
    }


}
