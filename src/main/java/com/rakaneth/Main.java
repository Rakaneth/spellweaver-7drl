package com.rakaneth;

import com.rakaneth.view.KeyPressedListener;
import com.rakaneth.view.PlayView;
import com.rakaneth.view.UIStack;
import com.valkryst.VTerminal.component.VPanel;
import com.valkryst.VTerminal.plaf.VTerminalLookAndFeel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

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
        final var frame = new JFrame("Spellweaver");
        final var panel = new VPanel(GAME_W, GAME_H);
        final var stack = UIStack.getInstance();
        final var state = GameConfig.newGame();

        frame.add(panel);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.addKeyListener((KeyPressedListener) e -> {
            SwingUtilities.invokeLater(() -> {
                boolean update = stack.handle(e);
                if (update) state.update();
                stack.render(panel);
            });
        });

        stack.push(new PlayView(state));

        SwingUtilities.invokeLater(() -> {
            stack.render(panel);
        });
    }
}
