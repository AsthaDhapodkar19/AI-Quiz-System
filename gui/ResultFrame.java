package gui;

import service.QuizService;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * ResultFrame - Displays final quiz results and statistics
 * Shows score, accuracy, topics attempted, and difficulty reached
 */
public class ResultFrame extends Frame implements ActionListener {
    
    private QuizService quizService;
    private Button exitButton;
    
    /**
     * Constructor - initializes result UI
     */
    public ResultFrame(QuizService quizService) {
        this.quizService = quizService;
        
        // Frame settings
        setTitle("Quiz Results - " + quizService.getUsername());
        setSize(600, 550);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);
        setBackground(new Color(240, 248, 255));
        
        // Calculate statistics
        int totalQuestions = quizService.getTotalQuestions();
        int correctAnswers = quizService.getCorrectAnswers();
        int wrongAnswers = quizService.getWrongAnswers();
        int score = quizService.getScore();
        int maxDifficulty = quizService.getMaxDifficultyReached();
        String topics = quizService.getTopicsAttempted();
        
        double accuracy = totalQuestions > 0 ? 
            (correctAnswers * 100.0 / totalQuestions) : 0;
        
        // Header
        Label headerLabel = new Label("🎉 Quiz Completed!", Label.CENTER);
        headerLabel.setBounds(50, 40, 500, 40);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerLabel.setForeground(new Color(0, 102, 204));
        add(headerLabel);
        
        // Username
        Label usernameLabel = new Label("Participant: " + quizService.getUsername(), Label.CENTER);
        usernameLabel.setBounds(50, 90, 500, 25);
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        usernameLabel.setForeground(new Color(80, 80, 80));
        add(usernameLabel);
        
        // Divider
        Canvas divider1 = new Canvas();
        divider1.setBounds(100, 125, 400, 2);
        divider1.setBackground(new Color(200, 200, 200));
        add(divider1);
        
        // Score Panel
        Panel scorePanel = new Panel();
        scorePanel.setBounds(150, 145, 300, 80);
        scorePanel.setBackground(new Color(0, 153, 76));
        scorePanel.setLayout(null);
        add(scorePanel);
        
        Label scoreTextLabel = new Label("Final Score", Label.CENTER);
        scoreTextLabel.setBounds(0, 15, 300, 25);
        scoreTextLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        scoreTextLabel.setForeground(Color.WHITE);
        scorePanel.add(scoreTextLabel);
        
        Label scoreValueLabel = new Label(String.valueOf(score), Label.CENTER);
        scoreValueLabel.setBounds(0, 40, 300, 30);
        scoreValueLabel.setFont(new Font("Arial", Font.BOLD, 32));
        scoreValueLabel.setForeground(Color.WHITE);
        scorePanel.add(scoreValueLabel);
        
        // Statistics
        int yPos = 250;
        
        // Total Questions
        Label totalQLabel = new Label("Total Questions Attempted:", Label.LEFT);
        totalQLabel.setBounds(120, yPos, 250, 25);
        totalQLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        add(totalQLabel);
        
        Label totalQValue = new Label(String.valueOf(totalQuestions), Label.RIGHT);
        totalQValue.setBounds(370, yPos, 110, 25);
        totalQValue.setFont(new Font("Arial", Font.BOLD, 14));
        totalQValue.setForeground(new Color(0, 102, 204));
        add(totalQValue);
        
        yPos += 30;
        
        // Correct Answers
        Label correctLabel = new Label("Correct Answers:", Label.LEFT);
        correctLabel.setBounds(120, yPos, 250, 25);
        correctLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        add(correctLabel);
        
        Label correctValue = new Label(String.valueOf(correctAnswers), Label.RIGHT);
        correctValue.setBounds(370, yPos, 110, 25);
        correctValue.setFont(new Font("Arial", Font.BOLD, 14));
        correctValue.setForeground(new Color(0, 153, 76));
        add(correctValue);
        
        yPos += 30;
        
        // Wrong Answers
        Label wrongLabel = new Label("Wrong Answers:", Label.LEFT);
        wrongLabel.setBounds(120, yPos, 250, 25);
        wrongLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        add(wrongLabel);
        
        Label wrongValue = new Label(String.valueOf(wrongAnswers), Label.RIGHT);
        wrongValue.setBounds(370, yPos, 110, 25);
        wrongValue.setFont(new Font("Arial", Font.BOLD, 14));
        wrongValue.setForeground(Color.RED);
        add(wrongValue);
        
        yPos += 30;
        
        // Accuracy
        Label accuracyLabel = new Label("Accuracy:", Label.LEFT);
        accuracyLabel.setBounds(120, yPos, 250, 25);
        accuracyLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        add(accuracyLabel);
        
        Label accuracyValue = new Label(String.format("%.1f%%", accuracy), Label.RIGHT);
        accuracyValue.setBounds(370, yPos, 110, 25);
        accuracyValue.setFont(new Font("Arial", Font.BOLD, 14));
        accuracyValue.setForeground(new Color(0, 102, 204));
        add(accuracyValue);
        
        yPos += 30;
        
        // Max Difficulty Reached
        Label diffLabel = new Label("Highest Difficulty Reached:", Label.LEFT);
        diffLabel.setBounds(120, yPos, 250, 25);
        diffLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        add(diffLabel);
        
        String diffText = "";
        switch (maxDifficulty) {
            case 1: diffText = "Easy"; break;
            case 2: diffText = "Medium"; break;
            case 3: diffText = "Hard"; break;
        }
        
        Label diffValue = new Label(diffText, Label.RIGHT);
        diffValue.setBounds(370, yPos, 110, 25);
        diffValue.setFont(new Font("Arial", Font.BOLD, 14));
        diffValue.setForeground(new Color(255, 140, 0));
        add(diffValue);
        
        yPos += 30;
        
        // Topics
        Label topicsLabel = new Label("Topics Attempted:", Label.LEFT);
        topicsLabel.setBounds(120, yPos, 250, 25);
        topicsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        add(topicsLabel);
        
        Label topicsValue = new Label(topics.length() > 20 ? 
            topics.substring(0, 17) + "..." : topics, Label.RIGHT);
        topicsValue.setBounds(370, yPos, 110, 25);
        topicsValue.setFont(new Font("Arial", Font.BOLD, 14));
        topicsValue.setForeground(new Color(0, 102, 204));
        add(topicsValue);
        
        // Divider
        Canvas divider2 = new Canvas();
        divider2.setBounds(100, 445, 400, 2);
        divider2.setBackground(new Color(200, 200, 200));
        add(divider2);
        
        // Performance message
        String message = "";
        if (accuracy >= 80) {
            message = "🌟 Excellent Performance!";
        } else if (accuracy >= 60) {
            message = "👍 Good Job!";
        } else if (accuracy >= 40) {
            message = "📚 Keep Practicing!";
        } else {
            message = "💪 Don't Give Up!";
        }
        
        Label messageLabel = new Label(message, Label.CENTER);
        messageLabel.setBounds(50, 460, 500, 25);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        messageLabel.setForeground(new Color(0, 153, 76));
        add(messageLabel);
        
        // Exit Button
        exitButton = new Button("Exit Quiz");
        exitButton.setBounds(225, 495, 150, 35);
        exitButton.setFont(new Font("Arial", Font.BOLD, 14));
        exitButton.setBackground(new Color(220, 53, 69));
        exitButton.setForeground(Color.WHITE);
        exitButton.addActionListener(this);
        add(exitButton);
        
        // Window close handler
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
        
        setVisible(true);
    }
    
    /**
     * Handle button click
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == exitButton) {
            System.out.println("Thank you for using AI Quiz System!");
            System.exit(0);
        }
    }
}
