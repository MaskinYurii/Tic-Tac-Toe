import java.sql.SQLOutput;
import java.util.Scanner;

import static java.lang.Integer.*;
import static java.lang.Integer.parseInt;

public class TicTacToe {
    // Winner positions for X or O on this game
    final static int[][] WINNER_COMBINATIONS = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {1, 5, 9},
            {3, 5, 7}, {1, 4, 7}, {2, 5, 8}, {3, 6, 9}};
    public static void main(String[] args) {
        // Greeting users and explain rules
        greetingUsers();

        // Starting Tic-Tac-Toe game
        startGame();
    }

    private static void startGame() {
        // In start game scheme is empty
        String[] gameRows = {"___", "___", "___"};

        // Show game scheme
        showGameScheme(gameRows);

        // Moves counter. Each second move it's "0"
        int gameMovesCounter = 0;

        while (true) {
            // Get user move
            int[] userMove = askUserMove(gameRows);

            // New row state with user movement
            StringBuilder newRow = new StringBuilder();

            for (int i = 0; i < gameRows[userMove[0] - 1].length(); i++) {
                if ((i + 1) == userMove[1]) {

                    // If it's first player's move - add X. If second player's move - add O
                    if (gameMovesCounter % 2 == 0) {
                        newRow.append("X");
                    } else {
                        newRow.append("O");
                    }
                } else {
                    newRow.append(gameRows[userMove[0] - 1].charAt(i));
                }
            }

            // Change row with new row where we added user movement
            gameRows[userMove[0] - 1] = newRow.toString();

            // Show new game scheme
            showGameScheme(gameRows);

            // Make game status as one line and check game state.
            String gameLine = gameRows[0] + gameRows[1] + gameRows[2];
            String gameStatus = calcGameResult(gameLine);

            // Add one move to game counter
            gameMovesCounter++;

            // If game is finished - say about it and stop game
            if (!"Game not finished".equals(gameStatus)) {
                System.out.println(gameStatus);
                break;
            }
        }
    }

    /**
     * Greeting users and explain rules
     */
    private static void greetingUsers() {
        System.out.println("******************************************************************************************");
        System.out.println("Hi, players! Your come to Tic-Tac-Toe Game!");
        System.out.println("So, I will tell a little about the essence of the game.");
        System.out.println("First player have \"X\". Second player have \"O\"");
        System.out.println("You must do some move in next form: row column");
        System.out.println("Our game place have 3x3 size, so if i wanna put X on center, I'll write: ");
        System.out.println("\"2 2\" because second row it's middle row on game place...");
        System.out.println("...and second column it's middle column on game place.");
        System.out.println("Some player will won if he put him 3 symbols in one row or one column on one diagonal.");
        System.out.println("If anyone don't do it, it will draw.");
        System.out.println("Soo.... Good luck!");
        System.out.println("******************************************************************************************");
    }

    /**
     * Ask player him next move
     * @param gameRows current status of rows in game
     * @return user move answer
     */
    private static int[] askUserMove(String[] gameRows) {
        // Make scanner
        Scanner scanner = new Scanner(System.in);

        // String array for user movement
        String[] userMove;

        // Integer array for final and formatted user movement
        int[] result = new int[2];

        // This loop will repeat when user write incorrect move
        while (true) {
            // Distribute user move on 2 part: row and col
            userMove = scanner.nextLine().split(" ");

            // If user enter only one coordinate - say about it
            if (userMove.length < 2) {
                System.out.println("You may have two coordinates: row and column!");
                continue;
            }

            // If user move is correct - do some to do. If not - say about it.
            if (isInteger(userMove[0]) && isInteger(userMove[1])) {
                // Making variables with user row and column
                int userRow = Integer.parseInt(userMove[0]);
                int userCol = Integer.parseInt(userMove[1]);

                // Checking for col and row. It cannot be less 1 and greater then 3
                if (userRow > 3 || userRow < 1 || userCol > 3 || userCol < 1) {
                    System.out.println("Coordinates should be from 1 to 3!");
                    continue;
                }

                // If user preferred field is occupied - say about it.
                if ('X' == gameRows[userRow - 1].charAt(userCol - 1)
                        || 'O' == gameRows[userRow - 1].charAt(userCol - 1)) {
                    System.out.println("This cell is occupied! Choose another one!");
                    continue;
                }

                // If user row and column is correct - add it to result and stop loop
                result[0] = userRow;
                result[1] = userCol;
                break;
            } else {
                System.out.println("You should enter numbers!");
                continue;
            }
        }

        // Return user row and column as result
        return result;
    }

    /**
     * Check if string is a number during conversion:  It is integer or not
     * @param str string for checking
     * @return true if this string can be integer, False if not.
     */
    private static boolean isInteger(String str) {
        try {
            parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Calculate game status on each move
     * @param gameLine it's scheme of current game at one line
     * @return status of game: X/O won, Draw or Game not finished.
     */
    private static String calcGameResult(String gameLine) {
        String result = "";

        int xs = calculateNumberOfSymbol(gameLine, "X");
        int os = calculateNumberOfSymbol(gameLine, "O");

        boolean isXWinner = isWinnerSymbol(gameLine, "X");
        boolean isOWinner = isWinnerSymbol(gameLine, "O");

        if (isOWinner) {
            result = "O wins";
        } else if (isXWinner) {
            result = "X wins";
        } else if (xs + os == 9) {
            result = "Draw";
        } else {
            result = "Game not finished";
        }

        return result;
    }

    /**
     * Checking current symbol status on some string
     * @param inputString - string where we check symbol
     * @param symbol - symbol which we check in current string
     * @return true if this symbol won and false if it not.
     */
    private static boolean isWinnerSymbol(String inputString, String symbol) {
        boolean isWinner = false;

        for (int[] combination : WINNER_COMBINATIONS) {
            int counter = 0;
            for (int k : combination) {
                if (Character.toString(inputString.charAt(k - 1)).equals(symbol)) {
                    counter++;
                }
            }
            if (counter >= 3) {
                isWinner = true;
                break;
            }
        }

        return isWinner;
    }

    /**
     * Calculate number some symbol in current string
     *
     * @param string - string where we count symbols
     * @param symbol - symbol which we count in string
     * @return - number of symbol in current string
     */
    private static int calculateNumberOfSymbol(String string, String symbol) {
        int counter = 0;

        for (int i = 0; i < string.length(); i++) {
            if (Character.toString(string.charAt(i)).equals(symbol)) {
                counter++;
            }
        }

        return counter;
    }

    /**
     * Showing current game scheme to players
     * @param gameRows it status for each row in this game
     */
    private static void showGameScheme(String[] gameRows) {
        // String for result output
        String result = "";

        // Add one dashes line to result
        result += makeLineOfDashes();

        result += formatGameScheme(gameRows);
        result += "\n" + makeLineOfDashes();

        System.out.println(result);
    }

    /**
     * Making string with line of dashes.
     * It will use in first and last lines.
     *
     * @return string with line of dashes.
     */
    private static String makeLineOfDashes() {
        // String with dashes
        StringBuilder dashes = new StringBuilder();

        // Add 9 dashes to string
        for (int i = 0; i < 9; i++) {
            dashes.append("-");
        }

        return dashes.toString();
    }

    /**
     * Formatting game scheme from one line to 3x3 place
     *
     * @param gameRows it status for each row in this game
     * @return formatting for output string with scheme of the game
     */
    private static String formatGameScheme(String[] gameRows) {
        StringBuilder formattedScheme = new StringBuilder();

        for (int i = 0; i < 3; i++) {
            // Move to new line and adding left border in row
            formattedScheme.append("\n| ");

            // Add info line from game scheme
            // If symbol will "_" - add empty field
            for (int j = 0; j < gameRows[i].length(); j++) {
                // First symbol in each row must be without additional space
                if (j == 0) {
                    if (gameRows[i].charAt(j) == '_') {
                        formattedScheme.append(" ");
                    } else {
                        formattedScheme.append(gameRows[i].charAt(j));
                    }
                } else if (gameRows[i].charAt(j) == '_') {
                    formattedScheme.append("  ");
                } else {
                    formattedScheme.append(" ").append(gameRows[i].charAt(j));
                }
            }

            // Adding right border in row
            formattedScheme.append(" |");
        }

        // Return formatted scheme in String type
        return formattedScheme.toString();
    }
}
