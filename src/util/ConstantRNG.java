
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

	public ConstantRNG()
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

	public static double getNextNumberUniform(double lowerBound, double upperBound)
	{
		double randomNumber = getNextNumberRawU();
		
		randomNumber = (lowerBound + randomNumber * (upperBound - lowerBound));

		//round to 2 decimal places
		randomNumber = (Math.floor(randomNumber*100)) / 100;

		return randomNumber;
	}

	public static double getNextNumberGuassian(double lowerBound, double upperBound)
	{
		double randomNumber = getNextNumberRawG();
		randomNumber = (lowerBound + randomNumber * (upperBound - lowerBound));
		randomNumber = (Math.floor(randomNumber*100)) / 100;

		return randomNumber;
	}
}