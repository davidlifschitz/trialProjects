package edu.yu.cs.intro;
import java.util.Arrays;
class ShareSameBirthday {
  public static void main(String[] args) {
    System.out.println("Number of experiments per population size: 10,000");
    System.out.println("Population size: 10. Shared Birthday Frequency: " + ((float) popSize(10) / 10000));
    System.out.println("Population size: 23. Shared Birthday Frequency: " + ((float) popSize(23) / 10000));
    System.out.println("Population size: 70. Shared Birthday Frequency: " + ((float) popSize(70) / 10000));
  }
  private static int popSize(int sizeOfArray) {
    int counter = 0;

    int[] arrayOfBdays = new int[sizeOfArray];

    for (int k = 0; k < 10000; k++) {

      for (int j = 0; j < sizeOfArray; j++) {

        arrayOfBdays[j] = (int) (366 * Math.random());

      }
      
      Arrays.sort(arrayOfBdays);

      for (int j = 0; j < sizeOfArray - 1; j++) {

        if (arrayOfBdays[j] == arrayOfBdays[j + 1]) {

          counter++;

          break;
          
        }
      }
    }
    return (counter);
  }
}