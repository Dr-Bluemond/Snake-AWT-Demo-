package org.example;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Game {

    int rowc;
    int colc;

    int interval;
    public Snake snake;
    public Point food;

    public Direction direction;

    public Board board;

    Thread thread;

    public Game(Board board, int rowc, int colc, int interval) {
        this.board = board;
        this.rowc = rowc;
        this.colc = colc;
        this.interval = interval;
        board.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> {
                        if (direction != Direction.RIGHT) {
                            direction = Direction.LEFT;
                            synchronized (Game.this) {
                                Game.this.notify();
                            }
                        }
                    }
                    case KeyEvent.VK_RIGHT -> {
                        if (direction != Direction.LEFT) {
                            direction = Direction.RIGHT;
                            synchronized (Game.this) {
                                Game.this.notify();
                            }
                        }
                    }
                    case KeyEvent.VK_UP -> {
                        if (direction != Direction.DOWN) {
                            direction = Direction.UP;
                            synchronized (Game.this) {
                                Game.this.notify();
                            }
                        }
                    }
                    case KeyEvent.VK_DOWN -> {
                        if (direction != Direction.UP) {
                            direction = Direction.DOWN;
                            synchronized (Game.this) {
                                Game.this.notify();
                            }
                        }
                    }
                    case KeyEvent.VK_R -> {
                        if (thread.isAlive()) {
                            thread.interrupt();
                        }
                        start();
                    }
                }
            }
        });
    }

    boolean generateFood() {
        boolean[][] bitmap = new boolean[rowc][colc];
        for (Point point : snake.getBody()) {
            bitmap[point.row][point.col] = true;
        }
        int choice = rowc * colc - snake.getBody().size();
        if (choice == 0) {
            return false;
        }
        int i = new Random().nextInt(choice);
        for (int r = 0; r < rowc; r++) {
            for (int c = 0; c < colc; c++) {
                if (bitmap[r][c]) {
                    continue;
                } else if (i != 0) {
                    i--;
                } else {
                    food = new Point(r, c);
                    return true;
                }
            }
        }
        return false;
    }

    void paintGameOnBoard() {
        board.clear();
        snake.paintOnBoard(board);
        board.data[food.row][food.col] = Board.Cell.CELL_FOOD;
        board.repaint();
    }

    void start() {
        snake = new Snake(this, rowc, colc);
        direction = Direction.RIGHT;
        generateFood();
        board.showDead(false);
        paintGameOnBoard();
        thread = new Thread(() -> {
            boolean running = true;
            while (running) {
                try {
                    synchronized (Game.this) {
                        Game.this.wait(interval);
                    }
                } catch (InterruptedException e) {
                    running = false;
                }
                Snake.MoveResult result = snake.move(direction);
                switch (result) {
                    case DEAD -> {
                        running = false;
                        System.out.println("You dead");
                        board.showDead(true);
                    }
                    case EAT_FOOD -> {
                        if (!generateFood()) {
                            running = false;
                        }
                    }
                }
                paintGameOnBoard();
            }
        });
        thread.setName("GameThread");
        thread.start();
    }
}
