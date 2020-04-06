
/**
* Basic outline of the solution:
1) Get input from user
2) Compute the sequence (which stops at 1) and the number of values in the sequence
3) Display the sequence and the number of values in the sequence
* **************************************
* First pseudocode refinement:
1) Get input from user 
2) while the value is greater than 1
    a) compute the sequence
        1) make a different set of instructions for evens and odds
        2) each time keep count of number of variables
    b) display the sequence and the number of variables

* ***************************************
* pseudocode of the solution, which is roughly
* 1:1 with the actual code:
Main method:
1) Get input from user
2) call formulaMethod with the value of the input

formulaMethod:
while value > 1 
counter (length of sequence) = 0    
    set an empty string
    if  the value%2 = 0
        value = (3* value)+1
        counter = counter + 1
        return value as an additional part of the string
    if the value%2 = 1
        value = value/2
        counter = counter + 1
        return value as an additional part of the string
After value = 1
    display string of sequence
    display string of counter
*/
import java.util.Scanner;

public class ThreeNPlusOne {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter a number:");
        double theValue = scanner.nextDouble();
        System.out.println(formulaMethod(theValue));
    }

    private static String formulaMethod(double value) {
        int counter = 1;
        double firstValue = value;
        String sequence = "" + (int) (firstValue);
        while (value > 1) {
            if (value % 2 == 0) {
                counter++;
                value = value / 2;
                sequence = sequence + "," + (int) (value);
            } else if (value % 2 == 1) {
                counter++;
                value = (3 * value) + 1;
                sequence = sequence + "," + (int) (value);
            }
        }
        System.out.println("The number of values is: " + counter);
        return sequence;
    }

}