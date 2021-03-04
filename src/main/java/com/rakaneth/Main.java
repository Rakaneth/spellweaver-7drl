package com.rakaneth;

import com.rakaneth.engine.GameState;
import com.rakaneth.view.KeyPressedListener;
import com.rakaneth.view.TestView;
import com.rakaneth.view.UIStack;
import com.valkryst.VTerminal.component.VFrame;
import com.valkryst.VTerminal.component.VPanel;
import com.valkryst.VTerminal.plaf.VTerminalLookAndFeel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static com.rakaneth.GameConfig.GAME_H;
import static com.rakaneth.GameConfig.GAME_W;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Main {
    public static void main(String[] args) {
        final var screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int fontSize = screenSize.width < 1600 ? 12 : 16;
        try {
            UIManager.setLookAndFeel(VTerminalLookAndFeel.getInstance(fontSize));
        } catch (final UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        final var frame = new JFrame("Vterminal Test");
        final var panel = new VPanel(GAME_W, GAME_H);
        final var stack = UIStack.getInstance();

        frame.add(panel);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.addKeyListener( (KeyPressedListener) e -> {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                System.exit(0);
            } else {
                stack.handle(e);
                SwingUtilities.invokeLater(() -> {
                    stack.render(panel);
                    panel.repaint();
                });
            }
        });
        final var state = new GameState();
        stack.push(new TestView(state));

        SwingUtilities.invokeLater(() -> {
            stack.render(panel);
        });
    }
}
