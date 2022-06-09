import geneticalgorithm.GeneticAlgorithm;
import com.qqwing.QQWing;

import java.util.Arrays;

public class App {

    public static void main(String[] args) {
        
        QQWing sudoku = new QQWing();
        
        sudoku.setPuzzle(new int[] {
            1, 9, 0, 0, 2, 0, 5, 0, 8, 
            0, 6, 7, 0, 0, 0, 0, 4, 0, 
            0, 0, 4, 6, 8, 3, 0, 9, 0, 
            3, 0, 0, 7, 0, 0, 2, 0, 9, 
            0, 0, 0, 1, 0, 0, 6, 0, 5, 
            0, 0, 0, 5, 9, 8, 0, 0, 4, 
            4, 0, 5, 8, 0, 0, 9, 0, 6, 
            2, 0, 6, 0, 4, 0, 0, 5, 1, 
            9, 0, 1, 0, 0, 6, 0, 7, 0, 
        });

        int[] sdkAsArray = sudoku.getPuzzle();

        Arrays.stream(sdkAsArray).forEach(c -> System.out.print(" " + c));
        System.out.println();
        sudoku.printPuzzle();

        GeneticAlgorithm algorithm = new GeneticAlgorithm(sdkAsArray);

        System.out.println("Solution:");
        sudoku.solve();
        sudoku.printSolution();
        
        int[] sol = algorithm.solve();
        sudoku.setPuzzle(sol);
        sudoku.printPuzzle();
    }
}