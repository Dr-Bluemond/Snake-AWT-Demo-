package org.example;

import java.util.LinkedList;

public class Snake {

    int rowc;
    int colc;
    LinkedList<Point> body;
    Game game;

    public Snake(Game game, int rowc, int colc) {
        this.game = game;
        this.rowc = rowc;
        this.colc = colc;
        body = new LinkedList<>();
        int tr = rowc / 2;
        int tc = colc / 2;
        body.add(new Point(tr, tc - 4));
        body.add(new Point(tr, tc - 3));
        body.add(new Point(tr, tc - 2));
        body.add(new Point(tr, tc - 1));
    }

    public LinkedList<Point> getBody() {
        return body;
    }

    void paintOnBoard(Board board) {
        for (Point bodyPart : body) {
            board.data[bodyPart.row][bodyPart.col] = Board.Cell.CELL_BODY;
        }
        var head = body.getLast();
        board.data[head.row][head.col] = Board.Cell.CELL_HEAD;
    }


    enum MoveResult {
        PASS,
        EAT_FOOD,
        DEAD,
    }

    MoveResult move(Direction dir) {
        Point lastHead = body.getLast();
        Point newPoint = lastHead;
        switch (dir) {
            case LEFT -> newPoint = new Point(lastHead.row, lastHead.col - 1);
            case RIGHT -> newPoint = new Point(lastHead.row, lastHead.col + 1);
            case UP -> newPoint = new Point(lastHead.row - 1, lastHead.col);
            case DOWN -> newPoint = new Point(lastHead.row + 1, lastHead.col);
        }
        if (newPoint.equals(game.food)) {
            body.add(newPoint);
            return MoveResult.EAT_FOOD;
        }
        if (newPoint.row < 0 || newPoint.row >= rowc || newPoint.col < 0 || newPoint.col >= colc) {
            return MoveResult.DEAD;
        }
        if (body.stream().skip(1).anyMatch(newPoint::equals)) {
            return MoveResult.DEAD;
        }
        body.add(newPoint);
        body.removeFirst();
        return MoveResult.PASS;
    }
}
