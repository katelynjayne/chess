import ui.PreLogin;

public class Client {
   PreLogin prelogin = new PreLogin();
   public String eval(String input) {
      return switch (input) {
         case "quit" -> "";
         default -> help();
      };
   }

   private String help() {
      return prelogin.help();
   }
}
