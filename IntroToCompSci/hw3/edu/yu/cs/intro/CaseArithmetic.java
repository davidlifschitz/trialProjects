package edu.yu.cs.intro;
import java.util.Scanner;
class CaseArithmetic {
	public static void main(String[] args) {
		Scanner scanName = new Scanner(System.in);
		System.out.println("What operation would you like to do? You can enter add, sub, mul, div");
		System.out.print("Please enter it now: ");
		String operation = scanName.next();
		System.out.print("Please enter the first operand: ");
		double firstNum = scanName.nextInt();
		System.out.print("Please enter the second operand: ");
		double secondNum = scanName.nextInt();
		switch(operation) {
			case "add":
				System.out.println(firstNum + " + " + secondNum + " = " + (firstNum+secondNum));
				break;
			case "sub":
				System.out.println(firstNum + " - " + secondNum + " = " + (firstNum-secondNum));	
				break;	
			case "mul":
				System.out.println(firstNum + " * " + secondNum + " = " + (firstNum*secondNum));
				break;
			case "div":
				System.out.println(firstNum + " / " + secondNum + " = " + (firstNum/secondNum));
				break;
			default:
				System.out.println("Invalid operation: " + "'" + operation + "'");

		}
	}	
}