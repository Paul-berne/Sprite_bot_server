package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import control.Controller;
import model.Game;
import model.Question;
import model.Score;

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
    
    public void InsertGameBDD(Game game) {
        Game InsertGame = game;
        
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
    
    public void InsertScore(ArrayList<Score> lesScores) throws SQLException {
    	String sqlQUERY = "INSERT INTO SCORE VALUES (?, ?, TO_DATE(?, 'YYYY-MM-DD'), TO_TIMESTAMP(?, 'HH24:MI:SS'), TO_TIMESTAMP(?, 'HH24:MI:SS'), ?)";
    	PreparedStatement insertStatement = connection.prepareStatement(sqlQUERY);
    	for (Score score : lesScores) {
    	    try {
    	        insertStatement.setInt(1, score.getId_game());
    	        insertStatement.setString(2, score.getPseaudo());
    	        insertStatement.setString(3, score.getDate_game());
    	        insertStatement.setString(4, score.getTime_begin());
    	        insertStatement.setString(5, score.getTime_end());
    	        insertStatement.setInt(6, score.getPlayer_score());

    	        insertStatement.executeUpdate();
    	        
    	        sqlQUERY = "UPDATE PLAYER SET TOTAL_POINTS = TOTAL_POINTS + ? where player_pseudo = ?";
    	        PreparedStatement updateStatement = connection.prepareStatement(sqlQUERY);
    	        updateStatement.setInt(1, score.getPlayer_score());
    	        updateStatement.setString(2, score.getPseaudo());
    	        updateStatement.executeUpdate();
    	    } catch (SQLException e) {
    	        // TODO Auto-generated catch block
    	        e.printStackTrace();
    	    }            
    	}
    	insertStatement.close();
    }

    



}
