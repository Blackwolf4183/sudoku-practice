package geneticalgorithm;
import org.jgap.*;
import org.jgap.impl.MutationOperator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class RowConstrainedMutationOperator extends MutationOperator {
    
    
    private final SudokuConverter converter;


    public RowConstrainedMutationOperator(Configuration conf, SudokuConverter c) throws InvalidConfigurationException {
        super(conf, 12);
        this.converter = c;
    }

    public void operate(Population a_population, List a_candidateChromosomes) {
        if (a_population != null && a_candidateChromosomes != null) {
            int mutRate = getMutationRate();
            boolean mutate;

            RandomGenerator generator = this.getConfiguration().getRandomGenerator();
            int size = Math.min(this.getConfiguration().getPopulationSize(), a_population.size());

            for (int i = 0; i < size; ++i) {
                IChromosome chromosome = a_population.getChromosome(i);
                Gene[] genes1 = chromosome.getGenes();
                IChromosome copyOfChromosome = null;
                Gene[] genes = null;

                for (int j = 0; j < genes1.length; ++j) {
                    mutate = generator.nextInt(mutRate) == 0;
                    if (mutate) {
                        if (copyOfChromosome == null) {
                            copyOfChromosome = (IChromosome) chromosome.clone();
                            a_candidateChromosomes.add(copyOfChromosome);
                            genes = copyOfChromosome.getGenes();

                            if (this.m_monitorActive) {
                                copyOfChromosome.setUniqueIDTemplate(chromosome.getUniqueID(), 1);
                            }
                        }

                        // j index of gene to change
                        //l index of the end of each row - we are looking for the end of our row, in lastEnd we save the index of
                        //end of the last row (so +1 it gives us the beggining)

                        // get current row
                        // pick another element within that row
                        // swap them
                        ArrayList<Integer> boundariesList = converter.getBoundaries();
                        int lastEnd = 0;
                        int start_row;
                        int finish_row;
                        //boolean mutationDone = False; - is break enough to finish for after we made the mutation for this gene
                        for (Integer rowEnd: boundariesList) {
                            if (rowEnd < j) {
                                lastEnd = rowEnd;
                            } else {
                                start_row = lastEnd + 1;
                                finish_row = rowEnd;

                                int finalJ = j;
                                int[] range = IntStream.rangeClosed(start_row, finish_row).filter(n -> n != finalJ).toArray();
                                if(range.length == 0) {
                                    continue;
                                }

                                int geneToExchange = range[generator.nextInt(range.length)];

                                //somehow get to both genes (id = j and id = geneToExchange) and exchange them
                                Object tempValue = genes[j].getAllele();
                                genes[j].setAllele(genes[geneToExchange].getAllele());
                                genes[geneToExchange].setAllele(tempValue);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
}