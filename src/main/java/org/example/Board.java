package org.example;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Board extends Container {
    enum Cell {
        CELL_HEAD,
        CELL_BODY,
        CELL_EMPTY,
        CELL_FOOD,

    }

    int rowc;
    int colc;

    int cellSize;

    int width;
    int height;

    BufferedImage bufImg;
    Cell[][] data;

    boolean dead;
    Label deadLabel;


    public Board(int rowc, int colc, int cellSize) {
        this.rowc = rowc;
        this.colc = colc;
        this.cellSize = cellSize;
        width = colc * cellSize;
        height = rowc * cellSize;
        bufImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        this.dead = false;
        data = new Cell[rowc][colc];
        setPreferredSize(new Dimension(width, height));
        clear();
        this.setLayout(new BorderLayout());
        deadLabel = new Label("You dead! Press R to restart.");
        deadLabel.setFont(new Font("Arial", Font.BOLD, 20));
        this.add(deadLabel, BorderLayout.NORTH);
    }

    void clear() {
        for (int row = 0; row < rowc; row++) {
            for (int col = 0; col < colc; col++) {
                data[row][col] = Cell.CELL_EMPTY;
            }
        }
    }

    private void paintComponent(Graphics g) {
        var g2 = bufImg.createGraphics();
        g2.setColor(new Color(0x303030));
        g2.fill(new Rectangle(0, 0, width, height));
        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 20; col++) {
                var rec = new Rectangle(
                        col * cellSize + 2,
                        row * cellSize + 2,
                        cellSize - 4,
                        cellSize - 4);
                switch (data[row][col]) {
                    case CELL_EMPTY -> g2.setColor(Color.BLACK);
                    case CELL_BODY -> g2.setColor(Color.WHITE);
                    case CELL_HEAD -> g2.setColor(Color.RED);
                    case CELL_FOOD -> g2.setColor(Color.GREEN);
                }
                g2.fill(rec);
            }
        }
        g.drawImage(bufImg, 0, 0, null);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        paintComponent(g);
    }

    public void showDead(boolean dead) {
        this.dead = dead;
        deadLabel.setVisible(dead);
        revalidate();
    }
}