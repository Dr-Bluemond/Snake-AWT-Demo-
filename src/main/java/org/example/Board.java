package org.example;

import java.awt.*;

public class Board extends Canvas {
    enum Cell {
        CELL_HEAD,
        CELL_BODY,
        CELL_EMPTY,
        CELL_FOOD,

    }

    int rowc;
    int colc;
    Cell[][] data; // each cell's size is 40*40

    boolean dead;


    public Board(int rowc, int colc) {
        this.rowc = rowc;
        this.colc = colc;
        this.dead = false;
        data = new Cell[rowc][colc];
        clear();
    }

    void clear() {
        for (int row = 0; row < rowc; row++) {
            for (int col = 0; col < colc; col++) {
                data[row][col] = Cell.CELL_EMPTY;
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        var g2 = (Graphics2D) g;
        g2.setColor(new Color(0x303030));
        g2.fill(new Rectangle(0, 0, 800, 600));
        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 20; col++) {
                var rec = new Rectangle(col * 40 + 2, row * 40 + 2, 36, 36);
                switch (data[row][col]) {
                    case CELL_EMPTY -> g2.setColor(Color.BLACK);
                    case CELL_BODY -> g2.setColor(Color.WHITE);
                    case CELL_HEAD -> g2.setColor(Color.RED);
                    case CELL_FOOD -> g2.setColor(Color.GREEN);
                }
                g2.fill(rec);
            }
        }
        if (dead) {
            g2.setColor(Color.WHITE);
            g2.drawString("You dead. Press (R) to play again.", 0, 50);
        }
    }

    public void showDead(boolean dead) {
        this.dead = dead;
    }
}