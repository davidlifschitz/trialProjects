package edu.yu.cs.intro;

import java.util.Scanner;
import java.util.Timer;

public class CountdownTimer {
	// Instance Variables
	private long pad;

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		try {
			System.out.print("What two numbers would you like to time a countdown from?");
			long firstCount = scanner.nextLong();
			long secondCount = scanner.nextLong();
			System.out.print("Would you like to pad the time?");
			String answer = scanner.next();
			if (answer.equalsIgnoreCase("yes")) {
				System.out.print("What is your pad (in milliseconds)? ");
				CountdownTimer padTimer = new CountdownTimer(scanner.nextLong());
				long time1 = padTimer.countdown(firstCount);
				long time2 = padTimer.countdown(secondCount);
				System.out.println("Counting down from " + (double) (firstCount) + " took " + time1
						+ " milliseconds, which is " + (double) (time1 / 1000) + " seconds.");
				System.out.println("Counting down from " + (double) (secondCount) + " took " + time2
						+ " milliseconds, which is " + (double) (time2 / 1000) + " seconds.");
			} else if (answer.equalsIgnoreCase("no")) {
				System.out.println("");
				CountdownTimer timer = new CountdownTimer(0);
				long time3 = timer.countdown(firstCount);
				long time4 = timer.countdown(secondCount);
				System.out.println("Counting down from " + (double) (firstCount) + " took " + time3
						+ " milliseconds, which is " + (double) (time3 / 1000) + " seconds.");
				System.out.println("Counting down from " + (double) (secondCount) + " took " + time4
						+ " milliseconds, which is " + (double) (time4 / 1000) + " seconds.");
			} else {
				throw new IllegalArgumentException();
							}
		} catch (Exception e) {
			System.out.println("You entered an invalid input.");
		}
	}

	public CountdownTimer(long pad) {
		this.pad = pad;
	}

	public long countdown(long counterTime) {
		long counting = counterTime;

		if (this.pad == 0) {
			Stopwatch watch = new Stopwatch(this.pad);
			watch.start();
			for (long i = 0; i < counterTime; i++) {
				counting -= 1;
			}
			watch.stop();
			long time = watch.elapsed();
			return time;
		} else if (this.pad > 0) {
			Stopwatch watch = new Stopwatch(this.pad);
			watch.start();
			for (long i = 0; i < counterTime; i++) {
				counting -= 1;
			}
			watch.stop();
			long time = watch.elapsed();
			return time;
		} else {
			throw new IllegalArgumentException();
		}
	}
}
