import java.io.IOException;

/**
 * Utility methods
 */
public class Util {

    public static void promptEnterKey(){
        System.out.println("Press \"ENTER\" to continue...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void lineBreak() {
        System.out.print(Constants.Formatting.LINE_BREAK);
    }

    public static void newPage() {
        System.out.print(Constants.Formatting.NEW_PAGE);
    }

}
