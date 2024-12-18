package org.example;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
    private static final int ROW_COUNT = 15;
    private static final int COL_COUNT = 20;
    private static final int INTERVAL = 200;
    private static final int CELL_SIZE = 40;

    public static void main(String[] args) {
        Frame mainFrame = new Frame("Snake") {
            @Override
            public void update(Graphics g) { // here to prevent update invoke g.clearRect(0, 0, width, height);
                if (isShowing()) {
                    paint(g);
                }
            }
        };
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        var board = new Board(ROW_COUNT, COL_COUNT, CELL_SIZE);
        mainFrame.add(board, BorderLayout.CENTER);
        mainFrame.pack();
        mainFrame.setResizable(false);
        mainFrame.setVisible(true);
        board.requestFocus();
        GameThread game = new GameThread(board, ROW_COUNT, COL_COUNT, INTERVAL);
        game.start();
        game.gameStart();
    }
}
