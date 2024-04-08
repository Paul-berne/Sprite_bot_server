package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import control.Controller;
import model.Game;
import model.Question;

public class DAOsqlGame {

    private Controller myController;

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    
    public DAOsqlGame(Controller theController) {
    	try {
            // Initialise le contrôleur et établit la connexion à la base de données
            this.myController = theController;
            String dbname = this.myController.getMyConfiguration().readProperty("databaseprivate.url");
            String username = this.myController.getMyConfiguration().readProperty("database.username");
            String password = this.myController.getMyConfiguration().readProperty("database.password");

            try {
            	this.connection = DriverManager.getConnection(dbname, username, password);	
			} catch (Exception e) {
				dbname = this.myController.getMyConfiguration().readProperty("databasepublic.url");
                this.connection = DriverManager.getConnection(dbname, username, password);
			}
            this.statement = connection.createStatement();
        } catch (SQLException e) {
            // Gère les exceptions liées à la connexion à la base de données
            e.printStackTrace();
        }
	}
    
    public int IdOfTheGame() {
    	String sqlQuery = "select max(id_game) from game";
    	int result = 1;
        try {
			resultSet = statement.executeQuery(sqlQuery);
			while (resultSet.next()) {
				result = resultSet.getInt("max")  + 1;	
			}
			System.out.println(result);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return result;
    }
    
    public void InsertGameBDD() {
        Game InsertGame = myController.getLaGame();
        
        // Assurez-vous de terminer la requête SQL avec une parenthèse fermante et un point-virgule
        String sqlQUERY = "INSERT INTO Game VALUES (" + InsertGame.getId_game() + ", '" + InsertGame.getType_game() + "');";
        
        try {
            statement.executeUpdate(sqlQUERY);
            
            for (Question question : InsertGame.getLesQuestions()) {
                sqlQUERY = "INSERT INTO game_question VALUES (" + InsertGame.getId_game() + ", " + question.getId() + ");";
                statement.executeUpdate(sqlQUERY);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
