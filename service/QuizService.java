package service;

import db.DatabaseConnection;
import model.Question;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * QuizService - Core business logic and AI-based difficulty management
 * Handles: question fetching, adaptive difficulty, score tracking, result storage
 */
public class QuizService {
    
    private Connection connection;
    private List<Question> questionPool;
    private int currentDifficulty;
    private int consecutiveCorrect;
    private int consecutiveWrong;
    private int maxDifficultyReached;
    
    // Quiz state
    private String username;
    private int score;
    private int correctAnswers;
    private int wrongAnswers;
    private List<String> selectedTopics;
    
    /**
     * Constructor - initializes quiz service
     */
    public QuizService() {
        this.connection = DatabaseConnection.getConnection();
        this.questionPool = new ArrayList<>();
        this.currentDifficulty = 1; // Start with easy
        this.consecutiveCorrect = 0;
        this.consecutiveWrong = 0;
        this.maxDifficultyReached = 1;
        this.score = 0;
        this.correctAnswers = 0;
        this.wrongAnswers = 0;
        this.selectedTopics = new ArrayList<>();
    }
    
    /**
     * Set username for the quiz session
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    /**
     * Set selected topics (empty list = all topics)
     */
    public void setSelectedTopics(List<String> topics) {
        this.selectedTopics = topics;
    }
    
    /**
     * Load questions from database based on selected topics
     * @return true if questions loaded successfully
     */
    public boolean loadQuestions() {
        questionPool.clear();
        
        try {
            String query;
            PreparedStatement stmt;
            
            if (selectedTopics.isEmpty()) {
                // Load all topics
                query = "SELECT * FROM questions ORDER BY RAND()";
                stmt = connection.prepareStatement(query);
            } else {
                // Load specific topics
                StringBuilder topicFilter = new StringBuilder();
                for (int i = 0; i < selectedTopics.size(); i++) {
                    topicFilter.append("?");
                    if (i < selectedTopics.size() - 1) {
                        topicFilter.append(",");
                    }
                }
                
                query = "SELECT * FROM questions WHERE topic IN (" + topicFilter + ") ORDER BY RAND()";
                stmt = connection.prepareStatement(query);
                
                for (int i = 0; i < selectedTopics.size(); i++) {
                    stmt.setString(i + 1, selectedTopics.get(i));
                }
            }
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Question q = new Question(
                    rs.getInt("id"),
                    rs.getString("question"),
                    rs.getString("option1"),
                    rs.getString("option2"),
                    rs.getString("option3"),
                    rs.getString("option4"),
                    rs.getInt("correct_option"),
                    rs.getInt("difficulty"),
                    rs.getString("topic")
                );
                questionPool.add(q);
            }
            
            rs.close();
            stmt.close();
            
            // Shuffle questions for randomization
            Collections.shuffle(questionPool);
            
            System.out.println("✓ Loaded " + questionPool.size() + " questions");
            return !questionPool.isEmpty();
            
        } catch (SQLException e) {
            System.err.println("✗ Error loading questions from database");
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get next question based on current difficulty (AI feature)
     * @return Question object or null if no more questions
     */
    public Question getNextQuestion() {
        // Filter questions by current difficulty
        List<Question> availableQuestions = new ArrayList<>();
        
        for (Question q : questionPool) {
            if (q.getDifficulty() == currentDifficulty) {
                availableQuestions.add(q);
            }
        }
        
        // If no questions at current difficulty, try adjacent difficulties
        if (availableQuestions.isEmpty()) {
            for (Question q : questionPool) {
                if (Math.abs(q.getDifficulty() - currentDifficulty) <= 1) {
                    availableQuestions.add(q);
                }
            }
        }
        
        // Return random question from available pool
        if (!availableQuestions.isEmpty()) {
            Collections.shuffle(availableQuestions);
            Question selected = availableQuestions.get(0);
            questionPool.remove(selected); // Remove to avoid repetition
            return selected;
        }
        
        // Fallback: return any remaining question
        if (!questionPool.isEmpty()) {
            Question selected = questionPool.get(0);
            questionPool.remove(0);
            return selected;
        }
        
        return null; // No more questions
    }
    
    /**
     * Process answer and adjust difficulty (AI adaptive logic)
     * @param isCorrect whether the answer was correct
     */
    public void processAnswer(boolean isCorrect) {
        if (isCorrect) {
            score += (currentDifficulty * 10); // Higher difficulty = more points
            correctAnswers++;
            consecutiveCorrect++;
            consecutiveWrong = 0;
            
            // AI Logic: 3 correct in a row → increase difficulty
            if (consecutiveCorrect >= 3 && currentDifficulty < 3) {
                currentDifficulty++;
                consecutiveCorrect = 0;
                System.out.println("🎯 Difficulty increased to: " + currentDifficulty);
                
                // Track max difficulty reached
                if (currentDifficulty > maxDifficultyReached) {
                    maxDifficultyReached = currentDifficulty;
                }
            }
        } else {
            wrongAnswers++;
            consecutiveWrong++;
            consecutiveCorrect = 0;
            
            // AI Logic: 2 wrong in a row → decrease difficulty (but not below 1)
            if (consecutiveWrong >= 2 && currentDifficulty > 1) {
                currentDifficulty--;
                consecutiveWrong = 0;
                System.out.println("📉 Difficulty decreased to: " + currentDifficulty);
            }
        }
    }
    
    /**
     * Save quiz result to database
     * @return true if saved successfully
     */
    public boolean saveResult() {
        try {
            String topicsAttempted = selectedTopics.isEmpty() ? "All Topics" : String.join(", ", selectedTopics);
            
            String query = "INSERT INTO users (username, score, topic, correct_answers, wrong_answers, max_difficulty_reached) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            
            stmt.setString(1, username);
            stmt.setInt(2, score);
            stmt.setString(3, topicsAttempted);
            stmt.setInt(4, correctAnswers);
            stmt.setInt(5, wrongAnswers);
            stmt.setInt(6, maxDifficultyReached);
            
            int rowsAffected = stmt.executeUpdate();
            stmt.close();
            
            System.out.println("✓ Quiz result saved to database");
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("✗ Error saving result to database");
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get available topics from database
     * @return List of unique topic names
     */
    public List<String> getAvailableTopics() {
        List<String> topics = new ArrayList<>();
        
        try {
            String query = "SELECT DISTINCT topic FROM questions ORDER BY topic";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                topics.add(rs.getString("topic"));
            }
            
            rs.close();
            stmt.close();
            
        } catch (SQLException e) {
            System.err.println("✗ Error fetching topics");
            e.printStackTrace();
        }
        
        return topics;
    }
    
    // Getters for quiz statistics
    
    public String getUsername() {
        return username;
    }
    
    public int getScore() {
        return score;
    }
    
    public int getCorrectAnswers() {
        return correctAnswers;
    }
    
    public int getWrongAnswers() {
        return wrongAnswers;
    }
    
    public int getMaxDifficultyReached() {
        return maxDifficultyReached;
    }
    
    public String getTopicsAttempted() {
        return selectedTopics.isEmpty() ? "All Topics" : String.join(", ", selectedTopics);
    }
    
    public int getCurrentDifficulty() {
        return currentDifficulty;
    }
    
    public int getTotalQuestions() {
        return correctAnswers + wrongAnswers;
    }
}
