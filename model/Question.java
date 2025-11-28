package model;

/**
 * Question - Data model representing a quiz question
 * Encapsulates all question attributes with proper getter/setter methods
 */
public class Question {
    
    private int id;
    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private int correctOption;
    private int difficulty;
    private String topic;
    
    /**
     * Default constructor
     */
    public Question() {
    }
    
    /**
     * Parameterized constructor
     */
    public Question(int id, String question, String option1, String option2, 
                   String option3, String option4, int correctOption, 
                   int difficulty, String topic) {
        this.id = id;
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.correctOption = correctOption;
        this.difficulty = difficulty;
        this.topic = topic;
    }
    
    // Getters and Setters
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getQuestion() {
        return question;
    }
    
    public void setQuestion(String question) {
        this.question = question;
    }
    
    public String getOption1() {
        return option1;
    }
    
    public void setOption1(String option1) {
        this.option1 = option1;
    }
    
    public String getOption2() {
        return option2;
    }
    
    public void setOption2(String option2) {
        this.option2 = option2;
    }
    
    public String getOption3() {
        return option3;
    }
    
    public void setOption3(String option3) {
        this.option3 = option3;
    }
    
    public String getOption4() {
        return option4;
    }
    
    public void setOption4(String option4) {
        this.option4 = option4;
    }
    
    public int getCorrectOption() {
        return correctOption;
    }
    
    public void setCorrectOption(int correctOption) {
        this.correctOption = correctOption;
    }
    
    public int getDifficulty() {
        return difficulty;
    }
    
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
    
    public String getTopic() {
        return topic;
    }
    
    public void setTopic(String topic) {
        this.topic = topic;
    }
    
    /**
     * Get option by number (1-4)
     */
    public String getOption(int optionNumber) {
        switch (optionNumber) {
            case 1: return option1;
            case 2: return option2;
            case 3: return option3;
            case 4: return option4;
            default: return "";
        }
    }
    
    /**
     * Check if answer is correct
     */
    public boolean isCorrect(int selectedOption) {
        return selectedOption == correctOption;
    }
    
    /**
     * Get difficulty as string
     */
    public String getDifficultyString() {
        switch (difficulty) {
            case 1: return "Easy";
            case 2: return "Medium";
            case 3: return "Hard";
            default: return "Unknown";
        }
    }
    
    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", question='" + question + '\'' +
                ", difficulty=" + difficulty +
                ", topic='" + topic + '\'' +
                '}';
    }
}
