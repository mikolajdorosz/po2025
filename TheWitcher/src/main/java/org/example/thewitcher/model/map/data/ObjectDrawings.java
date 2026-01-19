package org.example.thewitcher.model.map.data;

public final class ObjectDrawings {
    public static char player() { return 'G'; }
    public static char armorer() { return 'A'; }
    public static char blacksmith() { return 'B'; }
    public static char innkeeper() { return 'I'; }
    public static char merchant() { return 'M'; }
    public static char sorceress() { return 'S'; }
    public static char bandit() { return 'b'; }
    public static char ghul() { return 'g'; }
    public static char wolf() { return 'w'; }

    public static char path() { return '.'; }
    public static char grass() { return ' '; }
    public static char herb() { return '*'; }
    public static char loot() { return 'x'; }

    public static String[] inn5x22() {
        return new String[]{
                "  __________________  ",
                " //_____ // \\\\ ____\\\\ ",
                "//  ___ //___\\\\ ____\\\\",
                " || |__| || ||   |_|| ",
                " ||______||_||_____|| "
        };
    }
    public static String[] workshop9x18() {
        return new String[] {
                "        __        ",
                "        ||        ",
                "        ||______  ",
                "  _____/|| ____ \\ ",
                " /__\\ __||_______\\",
                "/|  |\\  || __    |",
                " |__|__/||_|_|___|",
                "... .. ... ... . .",
                "..|||.|_|.../\\-/\\.",
        };
    }
    public static String[] wagon4x11() {
        return new String[] {
                " _______   ",
                "|____|__|  ",
                "\\____\\__/--",
                ".O.O.O.O...",
        };
    }
    public static String[] cottage8x25() {
        return new String[] {
                "|--|--|--|--|--|--|--|--|",
                "|* *    ________   * * *|",
                "|*   //_____// \\\\  * * *|",
                "|*  // ____//___\\\\   * *|",
                "|*   | |__| || ||    * *|",
                "|* * |______|| ||........",
                "|* *.............. * * *|",
                "|--|--|--|--|--|--|--|--|"
        };
    }
    public static String[] camp6x14() {
        return new String[] {
                "  __          ",
                " / /\\     __  ",
                "/_/__\\ . /\\ \\ ",
                "  ....../__\\_\\",
                " ..--&/...... ",
                "  ../--..     ",
        };
    }
    public static String[] ghulNest1x5() {
        return new String[] {
                "_/-\\_",
        };
    }
    public static String[] cabinR3x10() {
        return new String[] {
                " ________ ",
                "/_____/__\\",
                "|____ |__|",
        };
    }
    public static String[] cabinL3x10() {
        return new String[] {
                " ________ ",
                "/__\\_____\\",
                "|__| ____|",
        };
    }
    public static String[] tree5x6() {
        return new String[] {
                " @@@@@",
                "@@ @ @",
                " @@@@ ",
                "  ||  ",
                "  ||  ",
        };
    }
    public static String[] smallTree3x4() {
        return new String[] {
                "@@@ ",
                " @@@",
                "  | ",
        };
    }
    public static String[] conifer4x4() {
        return new String[] {
                " /\\ ",
                "//\\\\",
                "//\\\\",
                " || ",
        };
    }
    public static String[] smallConifer3x3() {
        return new String[] {
                " /\\",
                "/\\\\",
                " | ",
        };
    }
    public static String[] coniferForest10x20() {
        return new String[] {
                "    /\\       /\\     ",
                " /\\//\\\\   /\\//\\\\ /\\ ",
                "//\\\\/\\\\   /\\\\/\\  /\\\\",
                "//\\\\||     | ||   | ",
                " ||    /\\     /\\    ",
                "      ///\\   //\\\\   ",
                "/\\   /\\//\\\\  //\\\\   ",
                "/\\\\ //\\\\/\\\\   ||  /\\",
                " |  //\\\\||       //\\",
                "     ||           | ",
        };
    }
    public static String[] mixedForest10x20() {
        return new String[] {
                "  @@/\\   @@@ /\\     ",
                "@/\\//\\\\   @@@/\\\\ /\\ ",
                "//\\\\/\\\\    |//\\  /\\\\",
                "//\\\\||       |/\\  | ",
                " ||    @@@@  //\\\\   ",
                "     @@ @ @  //\\\\   ",
                "@@@  /\\//\\\\   ||    ",
                "@@  //\\\\/\\\\      @@@",
                " |  //\\\\||      @@@ ",
                "     ||          |  ",
        };
    }
    public static String[] deciduousForest10x20() {
        return new String[] {
                "@@@@@     @@@@@     ",
                "@ @ @@  @@@ @ @  @@@",
                " @@@@  @@@@@@@  @@@ ",
                "@@@|    |  ||    |  ",
                " @@@       ||       ",
                "  |  @@@@@    @@@@@ ",
                "    @@ @ @@@ @ @@@@ ",
                "@@@  @@@@@@   @@@@@@",
                " @@@  || |     || | ",
                "  |   ||       ||   ",
        };
    }
}
