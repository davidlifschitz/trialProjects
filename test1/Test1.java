package test1;

import test1.assignment.Final;

public class Test1
{


    public static void main(String[] args)
    {
        GPACalc calc = new GPACalc();
        
        Subject math = new Subject();
        calc.addClass(4, math);
        Final mathFinal = new Final();
        mathFinal.setGrade(100.0);
        mathFinal.setPercent(100);
        math.addAssignment(mathFinal);

    }









}