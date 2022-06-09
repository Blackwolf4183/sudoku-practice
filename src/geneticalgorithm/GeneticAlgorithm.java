package geneticalgorithm;

import org.jgap.*;
import org.jgap.event.EventManager;
import org.jgap.impl.*;

import java.util.*;
import java.util.stream.Collectors;

public class GeneticAlgorithm {

    private final static int POPULATION = 25;

    private final int[] sudoku;
    private final Configuration configuration;
    private SudokuConverter converter;

    public GeneticAlgorithm(int[] sudoku) {
        // constructor
        this.sudoku = sudoku;

        // reset the configuration
        Configuration.reset();

        // initialize configuration
        configuration = new Configuration();
        configuration.setPreservFittestIndividual(true);

        FitnessFunction fitnessFunc = new SudokuFitnessFunction(sudoku);

        try {
            // setting the parameters

            configuration.setBreeder(new GABreeder());
            configuration.setRandomGenerator(new StockRandomGenerator());
            configuration.setEventManager(new EventManager());

            BestChromosomesSelector rouletteSelector = new BestChromosomesSelector(configuration, 0.8);

            configuration.addNaturalSelector(rouletteSelector, false);
            configuration.setMinimumPopSizePercent(100);
            configuration.setSelectFromPrevGen(1.0D);
            configuration.setRandomGenerator(new SeededRandomGenerator(1));
            configuration.setKeepPopulationSizeConstant(true);
            configuration.setFitnessEvaluator(new DefaultFitnessEvaluator());
            configuration.setChromosomePool(new ChromosomePool());

            // stablish fitness function
            configuration.setFitnessFunction(fitnessFunc);

            converter = new SudokuConverter(sudoku, configuration.getRandomGenerator());

            // CONSTRAINTS
            RowConstrainedCrossoverOperator crossoverOperator = new RowConstrainedCrossoverOperator(configuration,
                    converter);
            RowConstrainedMutationOperator mutationOperator = new RowConstrainedMutationOperator(configuration,
                    converter);

            configuration.addGeneticOperator(crossoverOperator);
            configuration.addGeneticOperator(mutationOperator);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // method to solve the sudoku
    public int[] solve() {

        Genotype population = null;

        try {
            population = new Genotype(configuration, generatePopulation());
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }

        // Evolve the population the max number of times.

        population.evolve(5000);

        // get the fittest chromosome
        IChromosome bestSol = population.getFittestChromosome();

        System.out.println("The best solution has a fitness value of " +
                bestSol.getFitnessValue());
        System.out.println("It contained the following: ");

        return converter.chromosomeToSudoku(bestSol);
    }

    // method to generate the population
    private Population generatePopulation() throws InvalidConfigurationException {
        int amount = (int) Arrays.stream(sudoku).filter(c -> c == 0).amount();
        List<List<Gene>> baseGenes = generateValidGenes();
        IChromosome[] chromosomes = new IChromosome[POPULATION];

        for (int i = 0; i < POPULATION; i++) {
            shuffleGenes(baseGenes);
            chromosomes[i] = new Chromosome(configuration,
                    baseGenes.stream().flatMap(List::stream).collect(Collectors.toList()).toArray(new Gene[amount]));
        }

        configuration.setSampleChromosome(chromosomes[0]);
        configuration.setPopulationSize(POPULATION);
        return new Population(configuration, chromosomes);
    }

    //shuffle the genes
    private void shuffleGenes(List<List<Gene>> genes) {
        for (List<Gene> geneList : genes) {
            Collections.shuffle(geneList, new Random(1));
        }
    }



    private List<Integer> generateCandidates() {
        List<Integer> candidates = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            candidates.add(i);
        }
        return candidates;
    }

    private List<List<Gene>> generateValidGenes() throws InvalidConfigurationException {
        List<List<Gene>> sol = new ArrayList<>();
        List<Integer> candidates = generateCandidates();

        for (int i = 0; i < sudoku.length; i++) {
            Integer val = sudoku[i];

            if (val != 0) candidates.remove(val);
            

            if ((i + 1) % 9 == 0) {
                List<Gene> genes = new ArrayList<>();
                for (int gen : candidates) {
                    IntegerGene gene = new IntegerGene(configuration, 1, 9);
                    gene.setAllele(gen);
                    genes.add(gene);
                }
                sol.add(genes);
                candidates = generateCandidates();
            }
        }
        return sol;
    }
}