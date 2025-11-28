package gui;

import service.QuizService;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * TopicSelectionFrame - Allows user to choose quiz topics
 * User can select multiple topics or skip to attempt all topics
 */
public class TopicSelectionFrame extends Frame implements ActionListener, ItemListener {
    
    private QuizService quizService;
    private List<Checkbox> topicCheckboxes;
    private Button proceedButton;
    private Button skipButton;
    private Label messageLabel;
    private Label selectedCountLabel;
    
    /**
     * Constructor - initializes topic selection UI
     */
    public TopicSelectionFrame(QuizService quizService) {
        this.quizService = quizService;
        this.topicCheckboxes = new ArrayList<>();
        
        // Frame settings
        setTitle("Select Quiz Topics");
        setSize(500, 450);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);
        setBackground(new Color(240, 248, 255));
        
        // Title Label
        Label titleLabel = new Label("Choose Your Topics", Label.CENTER);
        titleLabel.setBounds(50, 40, 400, 35);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 102, 204));
        add(titleLabel);
        
        // Instruction Label
        Label instructionLabel = new Label("Select one or more topics to focus on:", Label.CENTER);
        instructionLabel.setBounds(50, 80, 400, 25);
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        instructionLabel.setForeground(new Color(80, 80, 80));
        add(instructionLabel);
        
        // Get available topics from database
        List<String> availableTopics = quizService.getAvailableTopics();
        
        // Create checkboxes for each topic
        int yPosition = 120;
        for (String topic : availableTopics) {
            Checkbox cb = new Checkbox(topic);
            cb.setBounds(150, yPosition, 200, 25);
            cb.setFont(new Font("Arial", Font.PLAIN, 14));
            cb.addItemListener(this);
            topicCheckboxes.add(cb);
            add(cb);
            yPosition += 35;
        }
        
        // Selected count label
        selectedCountLabel = new Label("Selected: 0 topics", Label.CENTER);
        selectedCountLabel.setBounds(50, yPosition + 10, 400, 25);
        selectedCountLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        selectedCountLabel.setForeground(new Color(0, 153, 76));
        add(selectedCountLabel);
        
        // Proceed Button
        proceedButton = new Button("Proceed with Selected");
        proceedButton.setBounds(100, yPosition + 50, 160, 35);
        proceedButton.setFont(new Font("Arial", Font.BOLD, 13));
        proceedButton.setBackground(new Color(0, 153, 76));
        proceedButton.setForeground(Color.WHITE);
        proceedButton.addActionListener(this);
        add(proceedButton);
        
        // Skip Button
        skipButton = new Button("Skip (All Topics)");
        skipButton.setBounds(270, yPosition + 50, 130, 35);
        skipButton.setFont(new Font("Arial", Font.BOLD, 13));
        skipButton.setBackground(new Color(255, 165, 0));
        skipButton.setForeground(Color.WHITE);
        skipButton.addActionListener(this);
        add(skipButton);
        
        // Message Label
        messageLabel = new Label("", Label.CENTER);
        messageLabel.setBounds(50, yPosition + 95, 400, 25);
        messageLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        messageLabel.setForeground(Color.RED);
        add(messageLabel);
        
        // Window close handler
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
        
        setVisible(true);
    }
    
    /**
     * Update selected count label
     */
    private void updateSelectedCount() {
        int count = 0;
        for (Checkbox cb : topicCheckboxes) {
            if (cb.getState()) {
                count++;
            }
        }
        selectedCountLabel.setText("Selected: " + count + " topic" + (count != 1 ? "s" : ""));
    }
    
    /**
     * Handle checkbox state changes
     */
    @Override
    public void itemStateChanged(ItemEvent e) {
        updateSelectedCount();
    }
    
    /**
     * Handle button click events
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == proceedButton) {
            // Get selected topics
            List<String> selectedTopics = new ArrayList<>();
            for (Checkbox cb : topicCheckboxes) {
                if (cb.getState()) {
                    selectedTopics.add(cb.getLabel());
                }
            }
            
            // Validation
            if (selectedTopics.isEmpty()) {
                messageLabel.setText("⚠ Please select at least one topic or click Skip!");
                return;
            }
            
            // Set selected topics
            quizService.setSelectedTopics(selectedTopics);
            startQuiz();
            
        } else if (e.getSource() == skipButton) {
            // Empty list means all topics
            quizService.setSelectedTopics(new ArrayList<>());
            startQuiz();
        }
    }
    
    /**
     * Load questions and start quiz
     */
    private void startQuiz() {
        messageLabel.setText("Loading questions...");
        messageLabel.setForeground(new Color(0, 153, 76));
        
        // Load questions from database
        boolean loaded = quizService.loadQuestions();
        
        if (!loaded) {
            messageLabel.setText("✗ Error loading questions! Check database connection.");
            messageLabel.setForeground(Color.RED);
            return;
        }
        
        // Open Quiz Frame
        new QuizFrame(quizService);
        
        // Close topic selection frame
        dispose();
    }
}
