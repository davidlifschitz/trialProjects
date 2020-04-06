package edu.yu.cs.intro;
import java.util.Arrays;
public class RefactoredShareSameBirthday {
    public static void main (String[] args){
        System.out.println("Number of experiments per population size: 10,000");
        System.out.println("Population size: 10. Shared Birthday Frequency: " + ((float) runExperiments(10,10000)));
        System.out.println("Population size: 23. Shared Birthday Frequency: " + ((float) runExperiments(23,10000)));
        System.out.println("Population size: 70. Shared Birthday Frequency: " + ((float) runExperiments(70,10000)));
    }
    public static boolean runSingleExperiment(int populationSize) throws IllegalArgumentException{   
        if (populationSize <= 0){
            throw new IllegalArgumentException("The population size you inputted is less than or equal to zero and therefore impossible.");
        } else {
        boolean booleanCounter = false;
        int[] arrayOfBdays = new int[populationSize];
        for(int j = 0; j < populationSize; j++){
            arrayOfBdays[j] = (int) (366 * Math.random());
        }
        Arrays.sort(arrayOfBdays);
        for (int k = 0; k < populationSize - 1; k++){         
            if (arrayOfBdays[k] == arrayOfBdays[k + 1]){
                booleanCounter = true;
                break;
            } else {
                booleanCounter = false;
            }
        }
        return booleanCounter; 
        }   
    }
    public static double runExperiments (int populationSize, int nExperiments) throws IllegalArgumentException{
        if(populationSize <= 0){
            throw new IllegalArgumentException("The population size you inputted is less than or equal to zero and therefore impossible.");
        } else if (nExperiments <= 0) {
            throw new IllegalArgumentException("The number of experiments you want is less than or equal to zero and therefore impossible.");
        } else {
            double counter = 0.0;
        for (int i = 0; i < nExperiments; i++){
            if (runSingleExperiment(populationSize) == true){
                    counter++;
                }
            }
            return (float) (counter/nExperiments);
        }
        }    
    }

