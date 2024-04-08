package model;

import java.util.ArrayList;

public class Question {
    
    // Variables d'instance
    private int id;
    private String descriptionQuestion;
    private ArrayList<Answer> answers;

    // Constructeur
    public Question(int id, String descriptionQuestion, ArrayList<Answer> answers) {
        // Initialise une question avec les informations spécifiées
        this.id = id;
        this.descriptionQuestion = descriptionQuestion;
        this.answers = answers;
    }

    // Getter pour l'identifiant de la question
    public int getId() {
        return id;
    }

    // Setter pour définir l'identifiant de la question
    public void setId(int id) {
        this.id = id;
    }

    // Getter pour la description de la question
    public String getDescriptionQuestion() {
        return descriptionQuestion;
    }

    // Setter pour définir la description de la question
    public void setDescriptionQuestion(String descriptionQuestion) {
        this.descriptionQuestion = descriptionQuestion;
    }

    // Getter pour la liste des réponses associées à la question
    public ArrayList<Answer> getAnswers() {
        return answers;
    }

    // Setter pour définir la liste des réponses associées à la question
    public void setAnswers(ArrayList<Answer> answers) {
        this.answers = answers;
    }
}
