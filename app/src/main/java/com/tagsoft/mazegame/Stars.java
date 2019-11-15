package com.tagsoft.mazegame;

public class Stars {
    public int star1Row, star2Row, star3Row, star1Col, star2Col, star3Col;

    public Stars(int stage) {
        switch (stage){
            case 1:
                star1Row=13;
                star1Col=10;
                star2Row=2;
                star2Col=5;
                star3Row=17;
                star3Col=19;
                break;
            case 2:
                star1Row=7;
                star1Col=7;
                star2Row=7;
                star2Col=8;
                star3Row=13;
                star3Col=2;
                break;
            case 3:
                star1Row=16;
                star1Col=3;
                star2Row=19;
                star2Col=5;
                star3Row=10;
                star3Col=19;
                break;
            case 4:
                star1Row=18;
                star1Col=14;
                star2Row=8;
                star2Col=10;
                star3Row=11;
                star3Col=12;
                break;
            case 5:
                star1Row=7;
                star1Col=20;
                star2Row=13;
                star2Col=16;
                star3Row=18;
                star3Col=7;
                break;
            case 6:
                star1Row=5;
                star1Col=5;
                star2Row=8;
                star2Col=16;
                star3Row=18;
                star3Col=20;
                break;
            case 7:
                star1Row=6;
                star1Col=3;
                star2Row=16;
                star2Col=9;
                star3Row=18;
                star3Col=20;
                break;
            case 8:
                star1Row=16;
                star1Col=1;
                star2Row=17;
                star2Col=11;
                star3Row=1;
                star3Col=14;
                break;
            case 9:
                star1Row=8;
                star1Col=10;
                star2Row=13;
                star2Col=10;
                star3Row=11;
                star3Col=2;
                break;
            case 10:
                star1Row=8;
                star1Col=9;
                star2Row=8;
                star2Col=13;
                star3Row=17;
                star3Col=13;
                break;
        }
    }
}
