import java.util.Scanner;

public class DecodeLabs_Java_P2 {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // ── PHASE I: INPUT (The Gatekeeper) ────────────────────────────────

        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║    STUDENT GRADE CALCULATOR v1.0     ║");
        System.out.println("║         Powered by DecodeLabs        ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.println();

        // Get student name
        System.out.print("Enter Student Name  : ");
        String studentName = sc.nextLine().trim();

        // Get number of subjects (validated)
        int numberOfSubjects = 0;
        while (numberOfSubjects <= 0) {
            System.out.print("Enter Number of Subjects : ");
            try {
                numberOfSubjects = Integer.parseInt(sc.nextLine().trim());
                if (numberOfSubjects <= 0) {
                    System.out.println("  [ERROR] Number of subjects must be greater than 0. Try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("  [ERROR] Invalid input. Please enter a whole number.");
            }
        }

        // ── PHASE II: PROCESS (The Engine Room) ────────────────────────────

        // Gear 1: Accumulator Loop — collect and sum marks
        int totalMarks = 0;

        System.out.println();
        System.out.println("Enter marks for each subject (0 - 100):");
        System.out.println("──────────────────────────────────────");

        for (int i = 1; i <= numberOfSubjects; i++) {
            int mark = -1;

            // Defensive programming: validate each mark before accepting
            while (mark < 0 || mark > 100) {
                System.out.printf("  Subject %d marks : ", i);
                try {
                    mark = Integer.parseInt(sc.nextLine().trim()); // Solution B: eliminates buffer trap
                    if (mark < 0 || mark > 100) {
                        System.out.println("  [ERROR] Marks must be between 0 and 100. Try again.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("  [ERROR] Invalid input. Please enter a number between 0 and 100.");
                    mark = -1; // reset to keep loop going
                }
            }

            totalMarks += mark; // Accumulate into total
        }

        // Gear 2: The Math — type cast to double to avoid integer truncation
        // Without (double): 90/100 → 0 (wrong!)
        // With    (double): 90/100 → 0.9 → 90.0% (correct!)
        double averagePercentage = (double) totalMarks / (numberOfSubjects * 100) * 100;

        // Gear 3: The Logic Ladder — classify grade (strictest condition checked first)
        String grade;
        String remarks;

        if (averagePercentage >= 90) {
            grade   = "A";
            remarks = "Outstanding";
        } else if (averagePercentage >= 80) {
            grade   = "B";
            remarks = "Excellent";
        } else if (averagePercentage >= 70) {
            grade   = "C";
            remarks = "Good";
        } else if (averagePercentage >= 60) {
            grade   = "D";
            remarks = "Satisfactory";
        } else if (averagePercentage >= 50) {
            grade   = "E";
            remarks = "Pass";
        } else {
            grade   = "F";
            remarks = "Fail";
        }

        // Pass / Fail indicator
        String status = averagePercentage >= 50 ? "PASS" : "FAIL";

        // ── PHASE III: OUTPUT (The Presentation Layer) ──────────────────────
        // Use printf for clean formatting — avoids ugly 85.3333333...%

        System.out.println();
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║           RESULT CARD                ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.printf( "  Student Name    : %s%n",        studentName);
        System.out.printf( "  Total Subjects  : %d%n",        numberOfSubjects);
        System.out.printf( "  Total Marks     : %d / %d%n",   totalMarks, numberOfSubjects * 100);
        System.out.printf( "  Average         : %.2f%%%n",    averagePercentage);
        System.out.printf( "  Grade           : %s%n",        grade);
        System.out.printf( "  Remarks         : %s%n",        remarks);
        System.out.printf( "  Status          : %s%n",        status);
        System.out.println("══════════════════════════════════════");

        sc.close();
    }
}