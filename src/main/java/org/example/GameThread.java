package org.example;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class GameThread extends Thread {

    int rowc;
    int colc;

    int interval;
    public Snake snake;
    public Point food;

    public Direction direction;
    public Board board;

    private final BlockingQueue<Integer> event_queue = new LinkedBlockingQueue<>();

    public static final int EVENT_LEFT = 0;
    public static final int EVENT_RIGHT = 1;
    public static final int EVENT_UP = 2;
    public static final int EVENT_DOWN = 3;
    public static final int EVENT_RESTART = 4;


    public GameThread(Board board, int rowc, int colc, int interval) {
        this.board = board;
        this.rowc = rowc;
        this.colc = colc;
        this.interval = interval;
        board.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> event_queue.offer(EVENT_LEFT);
                    case KeyEvent.VK_RIGHT -> event_queue.offer(EVENT_RIGHT);
                    case KeyEvent.VK_UP -> event_queue.offer(EVENT_UP);
                    case KeyEvent.VK_DOWN -> event_queue.offer(EVENT_DOWN);
                    case KeyEvent.VK_R -> event_queue.offer(EVENT_RESTART);
                }
            }
        });
    }

    public void gameStart() {
        event_queue.offer(EVENT_RESTART);
    }

    public void run() {
        boolean running = false;
        while (true) {
            try {
                if (!running) {
                    Integer event = event_queue.take();
                    if (event == EVENT_RESTART) {
                        running = true;
                        init();
                    }
                    continue;
                }

                long lastMove = System.currentTimeMillis();
                long now;
                while ((now = System.currentTimeMillis()) - lastMove < interval) {
                    Integer event = event_queue.poll(interval - (now - lastMove), TimeUnit.MILLISECONDS);
                    if (event != null) {
                        switch (event) {
                            case EVENT_LEFT -> {
                                if (direction == Direction.RIGHT) {
                                    continue;
                                } else {
                                    direction = Direction.LEFT;
                                }
                            }
                            case EVENT_RIGHT -> {
                                if (direction == Direction.LEFT) {
                                    continue;
                                } else {
                                    direction = Direction.RIGHT;
                                }
                            }
                            case EVENT_UP -> {
                                if (direction == Direction.DOWN) {
                                    continue;
                                } else {
                                    direction = Direction.UP;
                                }
                            }
                            case EVENT_DOWN -> {
                                if (direction == Direction.UP) {
                                    continue;
                                } else {
                                    direction = Direction.DOWN;
                                }
                            }
                            case EVENT_RESTART -> {
                                init();
                                continue;
                            }
                        }
                        break;
                    }
                }
            } catch (InterruptedException e) {
                break;
            }
            Snake.MoveResult result = snake.move(direction);
            switch (result) {
                case DEAD -> {
                    running = false;
                    board.showDead(true);
                }
                case EAT_FOOD -> {
                    boolean success = generateFood();
                    if (!success) {
                        running = false;
                    }
                }
            }
            paintGameOnBoard();
        }
    }

    private void init() {
        snake = new Snake(this, rowc, colc);
        direction = Direction.RIGHT;
        generateFood();
        board.showDead(false);
        paintGameOnBoard();
    }

    private boolean generateFood() {
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


    private void paintGameOnBoard() {
        board.clear();
        snake.paintOnBoard(board);
        board.data[food.row][food.col] = Board.Cell.CELL_FOOD;
        board.repaint();
    }

}
