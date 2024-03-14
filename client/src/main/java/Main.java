import java.util.Objects;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Main {
    public static void main(String[] args) {
        System.out.println(SET_TEXT_COLOR_MAGENTA + DECORATOR);
        System.out.println(SET_TEXT_BOLD + "    Welcome to Chess! Type \"help\" to get started.");
        System.out.println(RESET_TEXT_BOLD_FAINT + DECORATOR);
        String result = "";
        Scanner scanner = new Scanner(System.in);
        Client client = new Client();
        while (!Objects.equals(result, "quit")) {
            result = scanner.nextLine();
            System.out.println(client.eval(result));
        }
    }
}