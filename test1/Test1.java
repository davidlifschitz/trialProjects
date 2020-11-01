package test1;

import java.util.Scanner;

import test1.assignment.Final;

public class Test1
{


    public static void main(String[] args)
    {
        GPACalc calc = new GPACalc();
        Scanner scanner = new Scanner(System.in);
        System.out.println("what is the name of your class?");
        String subjectName = scanner.next();
        Subject firstSubject = new Subject(subjectName);
        System.out.println("What is your grade worth out of the total grade?");
        int firstGradePercent = scanner.nextInt();
        System.out.println("What is your grade?");
        double firstGrade = scanner.nextDouble();
        
        calc.addClass(4, firstSubject);
        Final mathFinal = new Final();
        mathFinal.setGrade(firstGrade);
        mathFinal.setPercent(firstGradePercent);
        firstSubject.addAssignment(mathFinal);

    }









}