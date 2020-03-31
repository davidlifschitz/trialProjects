package test1;

import java.util.HashMap;
import java.util.Map;

public class GPACalc 
{
    Map<Integer,Subject> creditsToClass = new HashMap<>();
    double totalGrade = 0.0;

    public GPACalc()
    {}

    public void addClass(int numOfCredits,Subject className)
    {
        creditsToClass.put(numOfCredits, className);
        className.credit = numOfCredits;
    }

}