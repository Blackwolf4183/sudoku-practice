import geneticalgorithm.GeneticAlgorithm;
import com.qqwing.QQWing;


import java.util.Arrays;

public class App {

    public static void main(String[] args) {
        QQWing mySudoku = new QQWing();
        mySudoku.setPuzzle(new int[] {0, 5, 0, 7, 6, 3, 1, 9, 0, 0, 0, 0, 1, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, 0, 0, 0, 0, 1, 0, 4, 0, 8, 0, 0, 0, 3, 0, 0, 6, 0, 0, 4, 0, 7, 9, 0, 0, 0, 1, 0, 0, 0, 0, 5, 3, 0, 0, 0, 0, 9, 7, 0, 4, 0, 0, 0, 0, 0, 8, 0, 0, 0, 0, 0
        });

        int[] sudoku = mySudoku.getPuzzle();

        Arrays.stream(sudoku).forEach(c -> System.out.print(" " + c));
        System.out.println();
        mySudoku.printPuzzle();
        GeneticAlgorithm algorithm = new GeneticAlgorithm(sudoku);
        System.out.println("Solution:");
        mySudoku.solve();
        mySudoku.printSolution();
        int[] sol = algorithm.solve();
        mySudoku.setPuzzle(sol);
        mySudoku.printPuzzle();
    }
}