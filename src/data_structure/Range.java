package data_structure;

/**
 * Class for holding the ranges used in the CPT table in the Bayes Net structure
 * 
 * @author Bohao Li <bli@wpi.edu>
 * 
 */
public class Range {

	private final double upperBound;

	private final double lowerBound;

	public Range(double upperBound, double lowerBound) {
		this.upperBound = upperBound;
		this.lowerBound = lowerBound;
	}

	/**
	 * Additional constructor to construct a range given the info
	 * 
	 * @param rangeInfo
	 *            a string in the format of "x-y" where x is the lower bound and
	 *            y is the upper bound Example: ".5-.7"
	 */
	public Range(String rangeInfo) {
		String[] upperAndLower = rangeInfo.split("-");
		this.lowerBound = Double.parseDouble(upperAndLower[0]);
		this.upperBound = Double.parseDouble(upperAndLower[1]);
	}

	public double getLowerBound() {
		return lowerBound;
	}

	public double getUpperBound() {
		return upperBound;
	}

	/**
	 * Function to generate a random value within the given range
	 * 
	 * @return double a random value within the given range
	 */
	public double generateRandomValue() {
		return lowerBound + Math.random() * (upperBound - lowerBound);
	}
}
