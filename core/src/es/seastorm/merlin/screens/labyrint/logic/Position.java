package es.seastorm.merlin.screens.labyrint.logic;

public class Position {
    int x1, y1, x2, y2, num;

    public Position(int x1, int y1, int x2, int y2, int num) {
        super();
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.num = num;
    };

    public boolean equals (Position p){
        return x1==p.x1 && y1==p.y1 && x2==p.x2 && y2==p.y2;
    }
}
