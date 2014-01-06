

package util;


public class Globals
{
	private static double minGuess;
	private static double maxGuess;
	private static double minSlip;
	private static double maxSlip;

	private static int randomSeed;
	private static String matlabPath;

	//hard coded slashes, this needs to be changed for the users computer anyway
	//home
	//private static final String MATLAB_PATH = "C:\\Program Files\\MATLAB2\\R2012a\\bin\\";

	//school
	//private static final String MATLAB_PATH = "C:\\Program Files\\MATLAB\\R2012a\\bin\\";


	//set

	public static void setMinGuess(double mg)
	{
		minGuess = mg;
	}

	public static void setMaxGuess(double mg)
	{
		maxGuess = mg;
	}

	public static void setMinSlip(double ms)
	{
		minSlip = ms;
	}

	public static void setMaxSlip(double ms)
	{
		maxSlip = ms;
	}

	public static void setMatlabPath(String mp)
	{
		matlabPath = mp;
	}

	public static void setRandomSeed(int rs)
	{
		randomSeed = rs;
	}


	//get

	public static double getMinGuess()
	{
		return minGuess;
	}

	public static double getMaxGuess()
	{
		return maxGuess;
	}

	public static double getMinSlip()
	{
		return minSlip;
	}

	public static double getMaxSlip()
	{
		return maxSlip;
	}

	public static String getMatlabPath()
	{
		return matlabPath;
	}

	public static int getRandomSeed()
	{
		return randomSeed;
	}

}


