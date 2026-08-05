import java.util.Random;
import java.util.Scanner;

public class RPSLS {

    // Enum for all possible moves
    enum Move {
        ROCK,
        PAPER,
        SCISSORS,
        LIZARD,
        SPOCK
    }

    // Method to decide the winner
    public static int winner(Move a, Move b) {

        if (a == b) {
            return 0;
        }

        return switch (a) {

            case ROCK ->
                (b == Move.SCISSORS || b == Move.LIZARD) ? 1 : -1;

            case PAPER ->
                (b == Move.ROCK || b == Move.SPOCK) ? 1 : -1;

            case SCISSORS ->
                (b == Move.PAPER || b == Move.LIZARD) ? 1 : -1;

            case LIZARD ->
                (b == Move.SPOCK || b == Move.PAPER) ? 1 : -1;

            case SPOCK ->
                (b == Move.SCISSORS || b == Move.ROCK) ? 1 : -1;
        };
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Random random = new Random();

        int playerScore = 0;
        int computerScore = 0;

        Move[] moves = Move.values();

        System.out.println("===== Rock Paper Scissors Lizard Spock =====");

        for (int round = 1; round <= 5; round++) {

            System.out.println("\nRound " + round);
            System.out.print("Enter your move (ROCK, PAPER, SCISSORS, LIZARD, SPOCK): ");

            String input = sc.next().toUpperCase();

            Move playerMove = Move.valueOf(input);

            Move computerMove = moves[random.nextInt(moves.length)];

            System.out.println("Your Move     : " + playerMove);
            System.out.println("Computer Move : " + computerMove);

            int result = winner(playerMove, computerMove);

            if (result == 1) {
                System.out.println("You win this round!");
                playerScore++;
            }
            else if (result == -1) {
                System.out.println("Computer wins this round!");
                computerScore++;
            }
            else {
                System.out.println("Round is a tie!");
            }
        }

        System.out.println("\n===== Final Result =====");
        System.out.println("Your Score     : " + playerScore);
        System.out.println("Computer Score : " + computerScore);

        if (playerScore > computerScore) {
            System.out.println("You win " + playerScore + "-" + computerScore);
        }
        else if (computerScore > playerScore) {
            System.out.println("Computer wins " + computerScore + "-" + playerScore);
        }
        else {
            System.out.println("The match is a tie!");
        }

        sc.close();
    }
}