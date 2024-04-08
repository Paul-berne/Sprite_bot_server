package DAO;

import java.net.ResponseCache;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import control.Controller;
import model.Answer;

public class DAOsqlAnswer {
    
    private Controller myController;
    
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    
    private ArrayList<Answer> lesAnswer;

    // Implémentation
    public DAOsqlAnswer(Controller theController) {
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

    // Méthode pour lire les réponses associées à une question depuis la base de données
    public void LireAnswerSQL(int numeroQuestion) {
        try {
            // Initialise la liste des réponses
            lesAnswer = new ArrayList<Answer>();

            // Requête SQL pour récupérer les réponses associées à la question spécifiée
            String sqlQuery = "select codeanswer, descriptionanswer, iscorrect from answer inner join question on answer.id_question = question.id_question where question.id_question = " + numeroQuestion + ";";
            resultSet = statement.executeQuery(sqlQuery);

            // Parcourt les résultats de la requête
            while (resultSet.next()) {
                String code_Answer = resultSet.getString("codeanswer");
                boolean is_correct = resultSet.getBoolean("iscorrect");
                String reponseDesc = resultSet.getString("descriptionanswer");

                // Crée un objet Answer et l'ajoute à la liste
                Answer answer = new Answer(code_Answer,reponseDesc, is_correct);
                lesAnswer.add(answer);
            }

        } catch (Exception e) {
            // Gère les exceptions liées à la lecture des réponses depuis la base de données
            e.printStackTrace();
        }
    }

    // Getter pour la liste des réponses
    public ArrayList<Answer> getLesAnswer() {
        return lesAnswer;
    }
}
