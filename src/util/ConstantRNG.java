package util;

import java.util.Random;

/**
 * Class for generating random numbers
 * 
 * @author Douglas Selent
 * 
 */
public class ConstantRNG
{
	private static final int RANDOM_SEED = 3;
	private static final Random random = new Random(RANDOM_SEED);

	private ConstantRNG()
	{
		//
	}

	public static double getNextNumberRawU()
	{
		return random.nextDouble();
	}

	public static double getNextNumberRawG()
	{
		return random.nextGaussian();
	}

	public static int getNextInt()
	{
		return random.nextInt();
	}

	public static int getNextInt(int lowerBound, int upperBound)
	{
		int adjustedUpperBound = (upperBound+1) - lowerBound;
		int randomNumber = random.nextInt(adjustedUpperBound);
		int adjustedNumber = randomNumber + lowerBound;

		return adjustedNumber;
	}

	/**
	 * @param lowerBound
	 * @param upperBound
	 * @return
	 */
	public static double getNextNumberUniform(double lowerBound, double upperBound)
	{
		double randomNumber = getNextNumberRawU();

		randomNumber = (lowerBound + randomNumber * (upperBound - lowerBound));

		// round to 2 decimal places
		randomNumber = (Math.floor(randomNumber * 100)) / 100;

		return randomNumber;
	}

	/**
	 * @param lowerBound
	 * @param upperBound
	 * @return
	 */
	public static double getNextNumberGuassian(double lowerBound, double upperBound)
	{
		double randomNumber = getNextNumberRawG();
		randomNumber = (lowerBound + randomNumber * (upperBound - lowerBound));
		randomNumber = (Math.floor(randomNumber * 100)) / 100;

		return randomNumber;
	}
	
	/**
	 * @param lowerBound
	 * @param upperBound
	 * @return
	 */
	public static int getNextNumberInteger(int lowerBound, double upperBound){
		double randomNumber = getNextNumberRawU();
		
		randomNumber = (lowerBound + randomNumber * (upperBound - lowerBound));
		
		randomNumber = Math.floor(randomNumber + 0.5);
		return (int)randomNumber;
	}
}