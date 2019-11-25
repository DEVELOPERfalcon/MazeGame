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
            case 11:
                star1Row=1;
                star1Col=6;
                star2Row=12;
                star2Col=2;
                star3Row=2;
                star3Col=14;
                break;
            case 12:
                star1Row=6;
                star1Col=1;
                star2Row=11;
                star2Col=18;
                star3Row=15;
                star3Col=14;
                break;
            case 13:
                star1Row=10;
                star1Col=10;
                star2Row=12;
                star2Col=9;
                star3Row=18;
                star3Col=2;
                break;
            case 14:
                star1Row=8;
                star1Col=8;
                star2Row=12;
                star2Col=11;
                star3Row=19;
                star3Col=1;
                break;
            case 15:
                star1Row=0;
                star1Col=2;
                star2Row=13;
                star2Col=9;
                star3Row=12;
                star3Col=19;
                break;
            case 16:
                star1Row=1;
                star1Col=4;
                star2Row=1;
                star2Col=7;
                star3Row=0;
                star3Col=11;
                break;
            case 17:
                star1Row=5;
                star1Col=5;
                star2Row=4;
                star2Col=15;
                star3Row=16;
                star3Col=9;
                break;
            case 18:
                star1Row=3;
                star1Col=6;
                star2Row=0;
                star2Col=16;
                star3Row=15;
                star3Col=11;
                break;
            case 19:
                star1Row=3;
                star1Col=8;
                star2Row=3;
                star2Col=19;
                star3Row=8;
                star3Col=2;
                break;
            case 20:
                star1Row=7;
                star1Col=13;
                star2Row=11;
                star2Col=11;
                star3Row=18;
                star3Col=16;
                break;
            case 21:
                star1Row=12;
                star1Col=2;
                star2Row=5;
                star2Col=14;
                star3Row=13;
                star3Col=14;
                break;
            case 22:
                star1Row=0;
                star1Col=2;
                star2Row=1;
                star2Col=19;
                star3Row=17;
                star3Col=19;
                break;
            case 23:
                star1Row=0;
                star1Col=7;
                star2Row=0;
                star2Col=20;
                star3Row=7;
                star3Col=10;
                break;
            case 24:
                star1Row=6;
                star1Col=18;
                star2Row=19;
                star2Col=13;
                star3Row=19;
                star3Col=15;
                break;
            case 25:
                star1Row=5;
                star1Col=19;
                star2Row=11;
                star2Col=8;
                star3Row=18;
                star3Col=10;
                break;
            case 26:
                star1Row=4;
                star1Col=8;
                star2Row=12;
                star2Col=12;
                star3Row=9;
                star3Col=17;
                break;
            case 27:
                star1Row=1;
                star1Col=2;
                star2Row=3;
                star2Col=14;
                star3Row=15;
                star3Col=5;
                break;
            case 28:
                star1Row=5;
                star1Col=6;
                star2Row=9;
                star2Col=2;
                star3Row=9;
                star3Col=11;
                break;
            case 29:
                star1Row=0;
                star1Col=19;
                star2Row=15;
                star2Col=4;
                star3Row=11;
                star3Col=15;
                break;
            case 30:
                star1Row=9;
                star1Col=4;
                star2Row=10;
                star2Col=5;
                star3Row=6;
                star3Col=16;
                break;
            case 31:
                star1Row=4;
                star1Col=16;
                star2Row=10;
                star2Col=5;
                star3Row=19;
                star3Col=19;
                break;
            case 32:
                star1Row=4;
                star1Col=11;
                star2Row=7;
                star2Col=18;
                star3Row=17;
                star3Col=3;
                break;
            case 33:
                star1Row=2;
                star1Col=4;
                star2Row=17;
                star2Col=4;
                star3Row=8;
                star3Col=19;
                break;
            case 34:
                star1Row=10;
                star1Col=9;
                star2Row=10;
                star2Col=12;
                star3Row=18;
                star3Col=8;
                break;
            case 35:
                star1Row=12;
                star1Col=1;
                star2Row=12;
                star2Col=20;
                star3Row=18;
                star3Col=8;
                break;
            case 36:
                star1Row=4;
                star1Col=8;
                star2Row=0;
                star2Col=20;
                star3Row=17;
                star3Col=11;
                break;
            case 37:
                star1Row=8;
                star1Col=1;
                star2Row=0;
                star2Col=20;
                star3Row=19;
                star3Col=1;
                break;
            case 38:
                star1Row=11;
                star1Col=11;
                star2Row=12;
                star2Col=17;
                star3Row=19;
                star3Col=1;
                break;
            case 39:
                star1Row=2;
                star1Col=1;
                star2Row=1;
                star2Col=8;
                star3Row=18;
                star3Col=20;
                break;
            case 40:
                star1Row=3;
                star1Col=4;
                star2Row=1;
                star2Col=19;
                star3Row=15;
                star3Col=1;
                break;
            case 41:
                star1Row=11;
                star1Col=12;
                star2Row=13;
                star2Col=18;
                star3Row=19;
                star3Col=1;
                break;
            case 42:
                star1Row=13;
                star1Col=5;
                star2Row=10;
                star2Col=13;
                star3Row=15;
                star3Col=16;
                break;
                /*
            case 43:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 44:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 45:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 46:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 47:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 48:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 49:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 50:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 51:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 52:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 53:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 54:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 55:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 56:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 57:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 58:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 59:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 60:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 61:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 62:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 63:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 64:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 65:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 66:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 67:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 68:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 69:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 70:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 71:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 72:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 73:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 74:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 75:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 76:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 77:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 78:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 79:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 80:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 81:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 82:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 83:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 84:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 85:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 86:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 87:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 88:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 89:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 90:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 91:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 92:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 93:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 94:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 95:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 96:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 97:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 98:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 99:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
            case 100:
                star1Row=;
                star1Col=;
                star2Row=;
                star2Col=;
                star3Row=;
                star3Col=;
                break;
                */
        }
    }
}
