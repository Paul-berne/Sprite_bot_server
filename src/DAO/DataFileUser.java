package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;

import control.Controller;
import model.Player;
import tools.BCrypt;

public class DataFileUser {
    
    // Spécification
    private Controller myController;
    
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    
    // Implémentation
    public DataFileUser(Controller theController) {
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
           
        } catch (SQLException e) {
            // Gère les exceptions liées à la connexion à la base de données
            e.printStackTrace();
        }
    }

    public boolean VerifyUserExist(String theLogin, String thePassword) throws ParseException {
        boolean verif = false;

        try {
            // Requête SQL pour récupérer les informations de l'utilisateur spécifié
            String sqlQuery = "SELECT player_pseudo, password, player_rank FROM player WHERE player_pseudo = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, theLogin);
            resultSet = preparedStatement.executeQuery();

            // Parcourt les résultats de la requête
            if (resultSet.next()) {
                String login = resultSet.getString("player_pseudo");
                String password = resultSet.getString("password");
                String player_rank = resultSet.getString("player_rank");
                
                // Vérifie si les informations de connexion correspondent
                if (login.equals(theLogin) && BCrypt.checkpw(thePassword, password)) {
                    verif = true;
                    myController.getLePlayer().setNomclassement(player_rank);
                }
            }
        } catch (SQLException e) {
            // Gère les exceptions liées à la lecture des informations de l'utilisateur
            e.printStackTrace();
        } finally {
            // Ferme les ressources (resultSet, statement, connection)
            
        }

        return verif;
    }


 

	// Méthode pour changer le mot de passe de l'utilisateur
    public void ChangePasswordUser(String theLogin, String thePassword) {
        // Hash le nouveau mot de passe avec un sel
        String passwordToHash = thePassword;
        String salt = BCrypt.gensalt();
        String hashedPassword = BCrypt.hashpw(passwordToHash, salt);

        // Requête SQL pour mettre à jour le mot de passe de l'utilisateur
        String sqlQuery = "UPDATE player SET password = ? WHERE player_pseudo = ?";
        try {
            preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, hashedPassword);
            preparedStatement.setString(2, theLogin);
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Le mot de passe a été mis à jour avec succès.");
            }
        } catch (SQLException e) {
            // Gère les exceptions liées à la mise à jour du mot de passe
            e.printStackTrace();
        }
    }

}
