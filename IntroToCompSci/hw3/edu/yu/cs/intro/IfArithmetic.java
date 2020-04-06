package edu.yu.cs.intro;
import java.util.Scanner;
class IfArithmetic {
	public static void main(String[] args) {
		Scanner scanName = new Scanner(System.in);
		System.out.println("What operation would you like to do? You can enter add, sub, mul, div");
		System.out.print("Please enter it now: ");
		String operation = scanName.next();
		System.out.print("Please enter the first operand: ");
		double firstNum = scanName.nextInt();
		System.out.print("Please enter the second operand: ");
		double secondNum = scanName.nextInt();
		if (operation.equals("add")) {
			System.out.println(firstNum + " + " + secondNum + " = " + (firstNum+secondNum));
		} else if(operation.equals("sub")) {
			System.out.println(firstNum + " - " + secondNum + " = " + (firstNum-secondNum));
		} else if(operation.equals("mul")) {		
			System.out.println(firstNum + " * " + secondNum + " = " + (firstNum*secondNum));
		} else if(operation.equals("div")) {
			System.out.println(firstNum + " / " + secondNum + " = " + (firstNum/secondNum));
		} else {
			System.out.println("Invalid operation: " + "'" + operation + "'");
		}
	}
}