package geneticalgorithm;

import org.jgap.IChromosome;

import java.util.*;

import org.jgap.RandomGenerator;

public class SudokuConverter {
    private final int[] sudoku;
    private final RandomGenerator randomGen;

    private final ArrayList<Integer> boundaries;


    //constructor
    public SudokuConverter(int[] sudoku, RandomGenerator randomGen) {
        this.sudoku = sudoku;
        boundaries = new ArrayList<>();
        this.randomGen = randomGen;
        initMap();
    }

    public ArrayList<Integer> getBoundaries() {
        return boundaries;
    }

    //initializes the map
    private void initMap() {
        int genId = 0;
        int last = -1;

        for (int i = 0; i < sudoku.length; i++) {
            if (sudoku[i] == 0) {
                if((i + 1) % 9 == 0) {
                    boundaries.add(genId);
                    last = -1;
                } else {
                    last = genId;
                }
                genId++;
            } else {
                if(last != -1 && (i + 1) % 9 == 0) {
                    boundaries.add(last);
                    last = -1;
                }
            }
        }
    }

    public int getRandomEmptyPosition() {
        int i = randomGen.nextInt(boundaries.size());
        return boundaries.get(i);
    }

    public int[] chromosomeToSudoku(IChromosome chromosome){
        int[] readySudoku = new int[81];
        int placeInChromosome = 0;
        for (int i = 0; i < sudoku.length; i++) {
            if (sudoku[i] == 0) {
                readySudoku[i] = (Integer) chromosome.getGene(placeInChromosome).getAllele();
                placeInChromosome += 1;
            }
            else{
                readySudoku[i] = sudoku[i];
            }
        }
        return readySudoku;
    }
}
