import java.util.Random;
import java.util.Scanner;
import java.util.InputMismatchException;

public class Decodelabs_Java_P1 {
    
    // Constants for game configuration
    private static final int MIN_RANGE = 1;
    private static final int MAX_RANGE = 100;
    private static final int MAX_ATTEMPTS = 10;
    
    // Game state variables
    private Random random;
    private Scanner scanner;
    private int totalScore = 0;
    private int gamesPlayed = 0;
    
    public void startGame() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║   WELCOME TO NUMBER GAME");
        System.out.println("║   Range: 1 - 100 | Max Attempts: 10    ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        boolean playAgain = true;
        
        // Session persistence: do-while loop for multiple rounds
        do {
            playOneRound();
            playAgain = askPlayAgain();
        } while (playAgain);
        
        displayFinalScore();
        scanner.close();
    }
    
  
    private void playOneRound() {
        gamesPlayed++;
        
        // Generate random number (1-100) using zero-index shift
        int targetNumber = random.nextInt(MAX_RANGE) + 1; // nextInt(100) + 1 = 1-100
        int attemptCount = 0;
        int userGuess = 0;
        boolean hasWon = false;
        
        System.out.println("\n─────────────────────────────────────");
        System.out.println("Game #" + gamesPlayed + " | Think of a number between 1 and 100");
        System.out.println("─────────────────────────────────────\n");
        
        // Feedback loop: Continue until correct number is guessed or attempts limit reached
        while (!hasWon && attemptCount < MAX_ATTEMPTS) {
            try {
                // Input: Get user guess
                System.out.print("Attempt #" + (attemptCount + 1) + " - Enter your guess: ");
                userGuess = getValidInput();
                
                attemptCount++;
                
                // Decision: Check if guess matches target
                if (userGuess == targetNumber) {
                    // Win condition
                    hasWon = true;
                    int score = calculateScore(attemptCount);
                    totalScore += score;
                    
                    System.out.println("\n✓ Correct! You found the number: " + targetNumber);
                    System.out.println("  Attempts used: " + attemptCount + "/" + MAX_ATTEMPTS);
                    System.out.println("  Points earned: " + score);
                } 
                else if (userGuess > targetNumber) {
                    // Feedback: Too High
                    System.out.println("  ✗ Too High! Try a lower number.\n");
                } 
                else {
                    // Feedback: Too Low
                    System.out.println("  ✗ Too Low! Try a higher number.\n");
                }
                
            } catch (InputMismatchException e) {
                // Defensive Engineering: Handle invalid input
                System.out.println("  ✗ Invalid input! Please enter a valid integer.\n");
            }
        }
        
        // Loss condition
        if (!hasWon) {
            System.out.println("\n✗ Game Over! You've reached the maximum attempts.");
            System.out.println("  The correct number was: " + targetNumber);
        }
    }
    
    private int getValidInput() throws InputMismatchException {
        try {
            if (!scanner.hasNextInt()) {
                scanner.nextLine(); // Flush the invalid input buffer
                throw new InputMismatchException("Input is not an integer");
            }
            
            int input = scanner.nextInt();
            scanner.nextLine(); // Flush the newline character (CRITICAL: avoids Scanner trap)
            
            // Validate range
            if (input < MIN_RANGE || input > MAX_RANGE) {
                System.out.println("  ⚠ Please enter a number between " + MIN_RANGE + " and " + MAX_RANGE + "\n");
                return getValidInput(); // Recursive call for valid input
            }
            
            return input;
        } catch (InputMismatchException e) {
            scanner.nextLine(); // Clear the scanner buffer
            throw e;
        }
    }
    
    /**
     * Calculate score based on attempts
     * Better performance = Higher score
     * 
     * @param attempts Number of attempts used
     * @return calculated score
     */
    private int calculateScore(int attempts) {
        // Score formula: Maximum 100 points, decreases with more attempts
        return Math.max(10, 100 - (attempts * 9));
    }
   
    private boolean askPlayAgain() {
        System.out.print("\nPlay again? [Y/N]: ");
        String response = scanner.nextLine().trim().toUpperCase();
        return response.equals("Y") || response.equals("YES");
    }
    
    private void displayFinalScore() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         GAME SESSION SUMMARY           ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("  Total Games Played: " + gamesPlayed);
        System.out.println("  Total Score: " + totalScore + " points");
        
        if (gamesPlayed > 0) {
            int averageScore = totalScore / gamesPlayed;
            System.out.println("  Average Score: " + averageScore + " points/game");
        }
        
        System.out.println("\nThank you for playing! Keep practicing to improve your score.");
        System.out.println("═════════════════════════════════════════\n");
    }
    
    public static void main(String[] args) {
        Decodelabs_Java_P1 game = new Decodelabs_Java_P1();
        game.startGame();
    }
}