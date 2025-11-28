package gui;

import service.QuizService;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * LoginFrame - Entry point GUI for the quiz application
 * Allows user to enter username and start the quiz
 */
public class LoginFrame extends Frame implements ActionListener {
    
    private TextField usernameField;
    private Button startButton;
    private Label messageLabel;
    private QuizService quizService;
    
    /**
     * Constructor - initializes login UI
     */
    public LoginFrame() {
        quizService = new QuizService();
        
        // Frame settings
        setTitle("AI-Based Online Quiz System - Login");
        setSize(500, 350);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null); // Center on screen
        setBackground(new Color(240, 248, 255));
        
        // Title Label
        Label titleLabel = new Label("🎓 AI Quiz System", Label.CENTER);
        titleLabel.setBounds(50, 50, 400, 40);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(0, 102, 204));
        add(titleLabel);
        
        // Subtitle
        Label subtitleLabel = new Label("Adaptive Learning Quiz Platform", Label.CENTER);
        subtitleLabel.setBounds(50, 95, 400, 25);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(100, 100, 100));
        add(subtitleLabel);
        
        // Username Label
        Label usernameLabel = new Label("Enter Your Name:");
        usernameLabel.setBounds(100, 150, 120, 25);
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        add(usernameLabel);
        
        // Username TextField
        usernameField = new TextField();
        usernameField.setBounds(220, 150, 180, 25);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        add(usernameField);
        
        // Start Button
        startButton = new Button("Start Quiz →");
        startButton.setBounds(175, 200, 150, 35);
        startButton.setFont(new Font("Arial", Font.BOLD, 14));
        startButton.setBackground(new Color(0, 153, 76));
        startButton.setForeground(Color.WHITE);
        startButton.addActionListener(this);
        add(startButton);
        
        // Message Label
        messageLabel = new Label("", Label.CENTER);
        messageLabel.setBounds(50, 250, 400, 25);
        messageLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        messageLabel.setForeground(Color.RED);
        add(messageLabel);
        
        // Footer
        Label footerLabel = new Label("Powered by AI Adaptive Learning Engine", Label.CENTER);
        footerLabel.setBounds(50, 290, 400, 20);
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        footerLabel.setForeground(new Color(150, 150, 150));
        add(footerLabel);
        
        // Window close handler
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
        
        setVisible(true);
    }
    
    /**
     * Handle button click event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            String username = usernameField.getText().trim();
            
            // Validation
            if (username.isEmpty()) {
                messageLabel.setText("⚠ Please enter your name!");
                return;
            }
            
            if (username.length() < 2) {
                messageLabel.setText("⚠ Name must be at least 2 characters!");
                return;
            }
            
            // Set username in service
            quizService.setUsername(username);
            
            // Open Topic Selection Frame
            new TopicSelectionFrame(quizService);
            
            // Close login frame
            dispose();
        }
    }
}
