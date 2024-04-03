import java.util.Objects;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Main {
    public static void main(String[] args) {
        System.out.println(SET_TEXT_COLOR_MAGENTA + DECORATOR);
        System.out.println(SET_TEXT_BOLD + "    Welcome to Chess! Type \"help\" to get started.");
        System.out.println(RESET_TEXT_BOLD_FAINT + DECORATOR);
        String input = "";
        Scanner scanner = new Scanner(System.in);
        Client client = new Client();
        while (!Objects.equals(input, "quit")) {
            input = scanner.nextLine();
            String output = client.eval(input);
            if (!Objects.equals(output, "")) {
                System.out.println(output);
            }
        }
        System.out.println(SET_TEXT_COLOR_MAGENTA + "Bye!");
        System.out.println(DECORATOR);
    }
}