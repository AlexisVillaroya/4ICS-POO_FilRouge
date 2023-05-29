package model;

public class Move {
    private Coord initCoord;
    private Coord targetCoord;

    public Move(Coord initCoord, Coord targetCoord) {
        this.initCoord = initCoord;
        this.targetCoord = targetCoord;
    }

    public Coord getInitCoord() {
        return initCoord;
    }

    public Coord getTargetCoord() {
        return targetCoord;
    }
}

