package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

import control.Controller;

import model.Question;
import model.Answer;


public class DAOsqlQuestion {

    private Controller myController;

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    private ArrayList<Question> lesQuestions;
    private Question laQuestion;

    // Implémentation
    public DAOsqlQuestion(Controller theController) {
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

    // Méthode pour lire la question associée à un numéro de question depuis la base de données
    public void LireQuestionSQL(int numeroQuestion) {
        try {
            // Requête SQL pour récupérer la question associée au numéro spécifié
            String sqlQuery = "select question.id_question, descriptionquestion from question where id_question = " + numeroQuestion + ";";
            resultSet = statement.executeQuery(sqlQuery);

            // Parcourt les résultats de la requête
            while (resultSet.next()) {
                int questionId = resultSet.getInt("id_question");
                String questionDesc = resultSet.getString("descriptionquestion");
                this.laQuestion = new Question(questionId, questionDesc, new ArrayList<Answer>());
                this.lesQuestions.add(this.laQuestion);
            }

        } catch (Exception e) {
            // Gère les exceptions liées à la lecture de la question depuis la base de données
            e.printStackTrace();
        }
    }
    
  //side server
    public void initializeQuestions() {
        // Initialise la liste de questions de manière aléatoire
        this.lesQuestions = new ArrayList<Question>();
        Random random = new Random();
        ArrayList<Integer> numerosUtilises = new ArrayList<Integer>();

        for (int i = 0; i < 5; i++) {
            int randomNumber;

            do {
                randomNumber = random.nextInt(20) + 1;
            } while (numerosUtilises.contains(randomNumber));

            numerosUtilises.add(randomNumber);

            // Charge la question à partir de la base de données
            LireQuestionSQL(randomNumber);    
            myController.getLeStubAnswer().LireAnswerSQL(randomNumber);
            this.laQuestion.setAnswers(myController.getLeStubAnswer().getLesAnswer());
/**
            // Charge les réponses à partir de la base de données
            this.leStubAnswer = new DAOsqlAnswer(this);
            this.leStubAnswer.LireAnswerSQL(randomNumber);
            this.lesReponses = leStubAnswer.getLesAnswer();
            laQuestion.setAnswers(lesReponses);
            **/
            
        }
    }

	public ArrayList<Question> getLesQuestions() {
		return lesQuestions;
	}
    
}
