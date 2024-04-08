package model;

public class Answer {
    
    // Variables d'instance
    private String codeAnswer;
    private String descriptionAnswer;
    private boolean isCorrect;

    // Constructeur
     public Answer(String codeAnswer, String descriptionAnswer, boolean isCorrect) {
		super();
		this.codeAnswer = codeAnswer;
		this.descriptionAnswer = descriptionAnswer;
		this.isCorrect = isCorrect;
	}

    // Getter pour la description de la réponse
    public String getDescriptionAnswer() {
        return descriptionAnswer;
    }

   

	// Setter pour la description de la réponse
    public void setDescriptionAnswer(String descriptionAnswer) {
        this.descriptionAnswer = descriptionAnswer;
    }

    // Getter pour savoir si la réponse est correcte
    public boolean getIsCorrect() {
        return isCorrect;
    }

    // Setter pour définir si la réponse est correcte
    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    // Getter pour le code de la réponse
    public String getCodeAnswer() {
        return codeAnswer;
    }

    // Setter pour définir le code de la réponse
    public void setCodeAnswer(String codeAnswer) {
        this.codeAnswer = codeAnswer;
    }
}
