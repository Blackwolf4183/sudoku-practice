package geneticalgorithm;

import org.jgap.FitnessFunction;
import org.jgap.IChromosome;

import java.util.Arrays;

public class SudokuFitnessFunction extends FitnessFunction {

    int[] sudoku;

    public SudokuFitnessFunction(int[] sudoku) {
        this.sudoku = sudoku;
    }

    @Override
    protected double evaluate(IChromosome chromosome) { 
        /*  
         *  Function that adds chromosomes to the empty cells of our sudoku
         *  and then calculates its fitness checking columns and squares
         */
        int[] chromosomeSudoku = chromosomeToSudoku(chromosome);
        return checkColumns(chromosomeSudoku) + checkSquares(chromosomeSudoku);

    }

    private int[] chromosomeToSudoku(IChromosome c){
        /*
         *  Function that creates a sudoku which
         *  fills the empty cells with chromosomes
         */
        int[] chromosomeSudoku = new int[81];   // A sudoku has 81 cells
        int pos = 0;

        for (int i = 0; i < sudoku.length; i++) {
            if (sudoku[i] == 0) {
                chromosomeSudoku[i] = (Integer) c.getGene(pos).getAllele();
                pos += 1;
            } else
                chromosomeSudoku[i] = sudoku[i];
        }

        return chromosomeSudoku;
    }

    private int checkColumns(int[] sudoku) {
        /*
         *  Function that checks the columns of the sudoku
         *  to see the possible options to take
         */
        int constrains = 0;
        int cols = 9;               // Number of columns of the sudoku
        int pos;                    // Var to track array position
        int[] cells = new int[cols];

        for (int row = 0; row < cols; row++) { 
            
            Arrays.fill(cells, 0); // We initialize the array with 0's
            pos = 0;

            while(pos < cols) {

                int numCell = sudoku[pos * cols + row];
                int posInTable = numCell - 1;
                
                if(cells[posInTable] == 1) break;

                cells[posInTable] += 1;
                pos++;
            }

            if (pos == cols) constrains += 1;

        }

        return constrains;
    }


    private int checkSquares(int [] sudoku) {
        /*
         *  Function that checks the squares of the sudoku
         *  to see the possible options to take
         */
        int constrains = 0;
        int pos;
        int cols = 9, rows = 3;
        int[] cells = new int[cols];  
        
        for (int i = 0; i < cols; i++) {
            Arrays.fill(cells, 0); // We initialize the array with 0's

            for (int j = 0; j < rows; j++) {
                for (int k = 0; k < rows; k++) {
                    pos = 9 * (i / 3 * 3 + j) + i % 3 * 3 + k; // Must return an integer position
                    int numCell = sudoku[pos];
                    cells[numCell - 1] += 1;
                }
            }

            if (CheckIfFulfilled(cells)) 
                constrains += 1;
        }

        return constrains;
    }

    public boolean CheckIfFulfilled(int[] table) {
        /*
         *  Function that checks if all constrains have been checked
         */
        return Arrays.stream(table).allMatch(v -> v == 1);
    }
}