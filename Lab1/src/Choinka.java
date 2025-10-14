public class Choinka {
    public static void main(String[] args) {
        int height = Integer.parseInt(args[0]);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < 2*height; j++) {
                if (j >= height-i && j <= height+i) {
                    System.out.print("*");
                } else {
                    System.out.print(' ');
                }
            }
            System.out.println();
        }
    }
}