import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

class Trial {

    public static void main(String[] args) {
        float avg = 0;
        for(int i =0; i< 100; i++){
            avg += averageNetTotal(2);
        }
        avg = (float) avg / 100;
        System.out.println(avg);
    }

    public static float averageNetTotal(int price)
    {
        float counter = 0;
        for (int i = 0; i < 1000000; i++) { 
            counter += doingTheCalculation(price);
        }
        counter = (float) counter/1000000;        
        return counter;
        
    }
    public static int doingTheCalculation(int price) {
        int netTotal = 0;
        int startingPrice = price;

        for(int i =0; i < 25; i++)
        {
            //18 are red, 18 are black, 2 are green. 38 total.
            int landsOn = (int) (Math.random() * 37);
            if(landsOn < 18)
            {
                netTotal += startingPrice * 2;
                startingPrice = price;
            } else 
            {
                startingPrice *= 2;
                netTotal -= startingPrice;
            }
            
        }

        return netTotal;
    }

}