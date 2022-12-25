package org.example;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
    public static void main(String[] args) {
        Frame mainFrame = new Frame("Snake");
        mainFrame.setVisible(true);
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        var board = new Board(15, 20);
        mainFrame.add(board, BorderLayout.CENTER);
        var insets = mainFrame.getInsets();
        mainFrame.setSize(800, insets.top + 600);
        mainFrame.setResizable(false);
        Game game = new Game(board, 15, 20);
        game.start();
        board.requestFocus();
    }
}
