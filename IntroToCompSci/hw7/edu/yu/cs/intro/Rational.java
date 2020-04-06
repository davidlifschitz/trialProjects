package edu.yu.cs.intro;

public class Rational{
	private int numerator;
	private int denominator;

	public Rational(){
		this.numerator = 1;
		this.denominator = 1;
	}
	public Rational(int numerator, int denominator){
	this.numerator = numerator;
	this.denominator = denominator;
	}
	public String printRational() throws IllegalArgumentException{
		if (this.denominator == 0){
			throw new IllegalArgumentException();
		} else {
		return "The Numerator is: " + this.numerator + "," + " The Denominator is: " + this.denominator;
		}
	}
	public int getNumerator(){
	return this.numerator;
	}
	public int getDenominator() throws IllegalArgumentException{
	if(this.denominator == 0) {
		throw new IllegalArgumentException();
	} else {
		return this.denominator;
	  }
	}
	public void invert() throws IllegalArgumentException{
	int invertPlace = this.numerator;
	this.numerator = this.denominator;
	this.denominator = invertPlace;
		if (this.denominator == 0){
			throw new UnsupportedOperationException();
		}
	}
	public double toDouble() throws IllegalArgumentException{
		double num = (double) (this.numerator);
		double den = (double) (this.denominator);
		if (den == 0){
			throw new IllegalArgumentException();
		} else {
			return num/den;
		  }	
	}
	public Rational reduce(){
	//create an rational object instance with the parameters of num and den.
	Rational newRat = new Rational(this.numerator,this.denominator);
	//get the numerator 
	int num = newRat.numerator;
	//get the denominator
	int den = newRat.denominator;
	//calls the private method getGCD(numerator, denominator) and assigns it to a int.
	int theGreatest = getGCD(num, den);
	//then divide the numerator and denominator by that GCD.
	newRat.numerator = newRat.numerator / theGreatest;
	newRat.denominator = newRat.denominator / theGreatest;
	return newRat; 
	}
	private int getGCD(int numerator, int denominator) throws IllegalArgumentException {
		int a = numerator;
		int b = denominator;

		if (a == 0) {
          return b; 
      	} else if (b == 0) {
          throw new IllegalArgumentException(); 
        } else if (a == b) {
            return a; 
        } else if (a > b) {
            return getGCD(a-b, b); 
        } else {
        return getGCD(a, b-a); 	
    	}
        
	}	 
	public Rational add(final Rational that){
		Rational addRat = that;
		int a1 = this.numerator;
		int a2 = this.denominator;
		int b1 = addRat.numerator;
		int b2 = addRat.denominator;
		int newDen = a2 * b2;
		int newNum = (a1*b2) + (a2*b1);
		int theGreatest = getGCD(newNum, newDen);
		addRat.numerator = newNum/theGreatest;
		addRat.denominator = newDen/theGreatest;
		return addRat;
	}
	public static void main(String[] args){
		try{ 
			Rational oldRat = new Rational();
			
			System.out.println(oldRat.printRational());

			System.out.println(oldRat.getNumerator());
			
			System.out.println(oldRat.getDenominator());
			
			Rational reducedOldRat = oldRat.reduce();
			System.out.println(reducedOldRat.printRational()); 
			
			Rational secondOldRat = new Rational(10,20);
			Rational sumOfOldRats = oldRat.add(secondOldRat);
			System.out.println(sumOfOldRats.printRational());
			
			oldRat.invert();
			System.out.println(oldRat.printRational());
			
			Rational newRat = new Rational(10,5);
			
			System.out.println(newRat.printRational());
			
			System.out.println(newRat.getNumerator());
			
			System.out.println(newRat.getDenominator());
			
			Rational reducedNewRat = newRat.reduce();
			System.out.println(reducedNewRat.printRational()); 
			
			
			Rational secondNewRat = new Rational(10,20);
			Rational sumOfNewRats = newRat.add(secondNewRat);
			System.out.println(sumOfNewRats.printRational());

			newRat.invert();
			System.out.println(newRat.printRational());
		} catch(IllegalArgumentException e){
			 System.out.println("You Tried to Divide by Zero!");
		  } catch(UnsupportedOperationException e){
			  System.out.println("You're a sneaky one!");		  }
	}
}