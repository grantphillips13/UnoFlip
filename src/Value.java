public enum Value {
    ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, WILD, WILD_DRAW_TWO, REVERSE, SKIP, DRAW_ONE, SKIP_EVERYONE, DRAW_FIVE, WILD_DRAW_COLOR, FLIP;

    public int getPointValue() {
        switch (this) {
            case ONE:
                return 1;
            case TWO:
                return 2;
            case THREE:
                return 3;
            case FOUR:
                return 4;
            case FIVE:
                return 5;
            case SIX:
                return 6;
            case SEVEN:
                return 7;
            case EIGHT:
                return 8;
            case NINE:
                return 9;
            case SKIP:
            case REVERSE:
            case FLIP:
                return 20;
            case SKIP_EVERYONE:
                return 30;
            case DRAW_ONE:
                return 10;
            case DRAW_FIVE:
                return 20;
            case WILD:
                return 40;
            case WILD_DRAW_TWO:
                return 50;
            case WILD_DRAW_COLOR:
                return 60;
                default:
                return 0;
        }
    }

    // This class represents the possible value a card could have

}
