import chess.*;
import dataAccess.DataAccessException;
import dataAccess.sqlDAOs.SQLUserDAO;
import server.Server;

public class Main {
    public static void main(String[] args) {
        try {
            SQLUserDAO user = new SQLUserDAO();
        }
        catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        Server server = new Server();
        server.run(8080);
    }
}