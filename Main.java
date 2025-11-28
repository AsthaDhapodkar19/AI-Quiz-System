import db.DatabaseConnection;
import gui.LoginFrame;

/**
 * Main - Entry point for AI-Based Online Quiz System
 * Initializes database connection and launches login GUI
 * 
 * @author Your Name
 * @version 1.0
 */
public class Main {
    
    public static void main(String[] args) {
        
        // Print application header
        printHeader();
        
        // Test database connection
        System.out.println("🔄 Testing database connection...");
        boolean connected = DatabaseConnection.testConnection();
        
        if (!connected) {
            System.err.println("\n❌ FATAL ERROR: Cannot connect to database!");
            System.err.println("Please ensure:");
            System.err.println("  1. MySQL server is running");
            System.err.println("  2. Database 'quizdb' exists");
            System.err.println("  3. Username/password in DatabaseConnection.java are correct");
            System.err.println("  4. MySQL JDBC driver (mysql-connector-java.jar) is in classpath");
            System.exit(1);
        }
        
        System.out.println("✓ Database connection successful!\n");
        
        // Launch GUI
        System.out.println("🚀 Launching AI Quiz System GUI...\n");
        new LoginFrame();
    }
    
    /**
     * Print application header
     */
    private static void printHeader() {
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("        AI-BASED ONLINE QUIZ SYSTEM v1.0");
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("  ✓ Adaptive Difficulty Algorithm");
        System.out.println("  ✓ Multi-Topic Support");
        System.out.println("  ✓ Real-time Score Tracking");
        System.out.println("  ✓ Performance Analytics");
        System.out.println("═══════════════════════════════════════════════════════\n");
    }
}
