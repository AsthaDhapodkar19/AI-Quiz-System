package gui;

import model.Question;
import service.QuizService;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * QuizFrame - Main quiz interface
 * Displays questions one at a time with adaptive difficulty
 */
public class QuizFrame extends Frame implements ActionListener {
    
    private QuizService quizService;
    private Question currentQuestion;
    private int questionNumber;
    
    // UI Components
    private Label questionNumberLabel;
    private Label difficultyLabel;
    private Label topicLabel;
    private Label questionLabel;
    private CheckboxGroup optionsGroup;
    private Checkbox option1, option2, option3, option4;
    private Button nextButton;
    private Label feedbackLabel;
    private Label scoreLabel;
    
    /**
     * Constructor - initializes quiz UI
     */
    public QuizFrame(QuizService quizService) {
        this.quizService = quizService;
        this.questionNumber = 0;
        
        // Frame settings
        setTitle("AI Quiz - " + quizService.getUsername());
        setSize(700, 550);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);
        setBackground(new Color(245, 245, 250));
        
        // Header Panel
        Panel headerPanel = new Panel();
        headerPanel.setBounds(0, 0, 700, 70);
        headerPanel.setBackground(new Color(0, 102, 204));
        headerPanel.setLayout(null);
        add(headerPanel);
        
        // Question Number Label
        questionNumberLabel = new Label("Question 0/0", Label.LEFT);
        questionNumberLabel.setBounds(20, 15, 200, 25);
        questionNumberLabel.setFont(new Font("Arial", Font.BOLD, 16));
        questionNumberLabel.setForeground(Color.WHITE);
        headerPanel.add(questionNumberLabel);
        
        // Score Label
        scoreLabel = new Label("Score: 0", Label.RIGHT);
        scoreLabel.setBounds(480, 15, 200, 25);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        scoreLabel.setForeground(Color.WHITE);
        headerPanel.add(scoreLabel);
        
        // Difficulty Label
        difficultyLabel = new Label("Difficulty: Easy", Label.CENTER);
        difficultyLabel.setBounds(250, 15, 200, 25);
        difficultyLabel.setFont(new Font("Arial", Font.BOLD, 14));
        difficultyLabel.setForeground(new Color(255, 255, 150));
        headerPanel.add(difficultyLabel);
        
        // Topic Label
        topicLabel = new Label("Topic: -", Label.CENTER);
        topicLabel.setBounds(250, 40, 200, 20);
        topicLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        topicLabel.setForeground(Color.WHITE);
        headerPanel.add(topicLabel);
        
        // Question Label (Text Area simulation)
        questionLabel = new Label("", Label.CENTER);
        questionLabel.setBounds(50, 100, 600, 60);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        questionLabel.setForeground(new Color(50, 50, 50));
        add(questionLabel);
        
        // Options Group
        optionsGroup = new CheckboxGroup();
        
        option1 = new Checkbox("", optionsGroup, false);
        option1.setBounds(100, 190, 500, 30);
        option1.setFont(new Font("Arial", Font.PLAIN, 14));
        add(option1);
        
        option2 = new Checkbox("", optionsGroup, false);
        option2.setBounds(100, 230, 500, 30);
        option2.setFont(new Font("Arial", Font.PLAIN, 14));
        add(option2);
        
        option3 = new Checkbox("", optionsGroup, false);
        option3.setBounds(100, 270, 500, 30);
        option3.setFont(new Font("Arial", Font.PLAIN, 14));
        add(option3);
        
        option4 = new Checkbox("", optionsGroup, false);
        option4.setBounds(100, 310, 500, 30);
        option4.setFont(new Font("Arial", Font.PLAIN, 14));
        add(option4);
        
        // Feedback Label
        feedbackLabel = new Label("", Label.CENTER);
        feedbackLabel.setBounds(50, 360, 600, 30);
        feedbackLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(feedbackLabel);
        
        // Next Button
        nextButton = new Button("Submit Answer");
        nextButton.setBounds(275, 420, 150, 40);
        nextButton.setFont(new Font("Arial", Font.BOLD, 14));
        nextButton.setBackground(new Color(0, 153, 76));
        nextButton.setForeground(Color.WHITE);
        nextButton.addActionListener(this);
        add(nextButton);
        
        // Progress info
        Label progressLabel = new Label("AI adapts difficulty based on your performance", Label.CENTER);
        progressLabel.setBounds(50, 480, 600, 20);
        progressLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        progressLabel.setForeground(new Color(100, 100, 100));
        add(progressLabel);
        
        // Window close handler
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
        
        // Load first question
        loadNextQuestion();
        
        setVisible(true);
    }
    
    /**
     * Load next question from service
     */
    private void loadNextQuestion() {
        currentQuestion = quizService.getNextQuestion();
        
        if (currentQuestion == null) {
            // No more questions - show results
            showResults();
            return;
        }
        
        questionNumber++;
        
        // Update UI
        questionNumberLabel.setText("Question " + questionNumber);
        scoreLabel.setText("Score: " + quizService.getScore());
        
        String diffText = "";
        switch (quizService.getCurrentDifficulty()) {
            case 1: diffText = "Easy"; break;
            case 2: diffText = "Medium"; break;
            case 3: diffText = "Hard"; break;
        }
        difficultyLabel.setText("Difficulty: " + diffText);
        topicLabel.setText("Topic: " + currentQuestion.getTopic());
        
        questionLabel.setText(currentQuestion.getQuestion());
        option1.setLabel("A) " + currentQuestion.getOption1());
        option2.setLabel("B) " + currentQuestion.getOption2());
        option3.setLabel("C) " + currentQuestion.getOption3());
        option4.setLabel("D) " + currentQuestion.getOption4());
        
        // Reset selection and feedback
        optionsGroup.setSelectedCheckbox(null);
        feedbackLabel.setText("");
        nextButton.setLabel("Submit Answer");
        nextButton.setEnabled(true);
    }
    
    /**
     * Handle button click
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == nextButton) {
            
            if (nextButton.getLabel().equals("Submit Answer")) {
                // Check answer
                Checkbox selected = optionsGroup.getSelectedCheckbox();
                
                if (selected == null) {
                    feedbackLabel.setText("⚠ Please select an option!");
                    feedbackLabel.setForeground(Color.RED);
                    return;
                }
                
                // Determine selected option number
                int selectedOption = 0;
                if (selected == option1) selectedOption = 1;
                else if (selected == option2) selectedOption = 2;
                else if (selected == option3) selectedOption = 3;
                else if (selected == option4) selectedOption = 4;
                
                // Check if correct
                boolean isCorrect = currentQuestion.isCorrect(selectedOption);
                
                // Process answer (updates score and difficulty)
                quizService.processAnswer(isCorrect);
                
                // Show feedback
                if (isCorrect) {
                    feedbackLabel.setText("✓ Correct! +" + (currentQuestion.getDifficulty() * 10) + " points");
                    feedbackLabel.setForeground(new Color(0, 153, 76));
                } else {
                    feedbackLabel.setText("✗ Wrong! Correct answer: " + 
                        (char)('A' + currentQuestion.getCorrectOption() - 1) + 
                        ") " + currentQuestion.getOption(currentQuestion.getCorrectOption()));
                    feedbackLabel.setForeground(Color.RED);
                }
                
                // Update button
                nextButton.setLabel("Next Question →");
                scoreLabel.setText("Score: " + quizService.getScore());
                
            } else {
                // Load next question
                loadNextQuestion();
            }
        }
    }
    
    /**
     * Show results screen
     */
    private void showResults() {
        // Save result to database
        quizService.saveResult();
        
        // Open result frame
        new ResultFrame(quizService);
        
        // Close quiz frame
        dispose();
    }
}
