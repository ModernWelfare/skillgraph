
package data_storage;

//class that holds the result of a iterative search
public class ResultStorage
{
	private int uid;
	private int numberOfStudents;
	private int numberOfItems;
	private int numberOfRealSkills;
	private int numberOfLevels;
	private double guessValue;
	private double slipValue;
	private int numberOfFakeSkills;
	private double bestRMSE;
	private boolean learnedBack;


	public ResultStorage()
	{
		uid = -1;
		numberOfStudents = -1;
		numberOfItems = -1;
		numberOfRealSkills = -1;
		numberOfLevels = -1;
		guessValue = -1.00;
		slipValue = -1.00;
		numberOfFakeSkills = -1;
		bestRMSE = -1.0000;
		boolean learnedBack = false;
	}

	//set functions

	public void setUID(int graphUID)
	{
		uid = graphUID;
	}

	public void setNumberOfStudents(int nos)
	{
		numberOfStudents = nos;
	}

	public void setNumberOfItems(int not)
	{
		numberOfItems = not;
	}

	public void setNumberOfRealSkills(int nors)
	{
		numberOfRealSkills = nors;
	}

	public void setNumberOfLevels(int nol)
	{
		numberOfLevels = nol;
	}

	public void setGuessValue(double gv)
	{
		guessValue = gv;
	}

	public void setSlipValue(double sv)
	{
		slipValue = sv;
	}

	public void setNumberOfFakeSkills(int nofs)
	{
		numberOfFakeSkills = nofs;
	}

	public void setBestRMSE(double brmse)
	{
		bestRMSE = brmse;
	}

	public void setLearnedBack(boolean lb)
	{
		learnedBack = lb;
	}


	//get functions

	public int getUID()
	{
		return uid;
	}

	public int getNumberOfStudents()
	{
		return numberOfStudents;
	}

	public int getNumverOfItems()
	{
		return numberOfItems;
	}

	public int getNumberOfRealSkills()
	{
		return numberOfRealSkills;
	}

	public int getNumberOfLevels()
	{
		return numberOfLevels;
	}

	public double getGuessValue()
	{
		return guessValue;
	}

	public double getSlipValue()
	{
		return slipValue;
	}

	public int getNumberOfFakeskills()
	{
		return numberOfFakeSkills;
	}

	public double getBestRMSE()
	{
		return bestRMSE;
	}

	public boolean getLearnedBack()
	{
		return learnedBack;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		sb.append(uid);
		sb.append(",");
		sb.append(numberOfStudents);
		sb.append(",");
		sb.append(numberOfItems);
		sb.append(",");
		sb.append(numberOfRealSkills);
		sb.append(",");
		sb.append(numberOfLevels);
		sb.append(",");
		sb.append(guessValue);
		sb.append(",");
		sb.append(slipValue);
		sb.append(",");
		sb.append(numberOfFakeSkills);
		sb.append(",");
		sb.append(bestRMSE);
		sb.append(",");
		sb.append(learnedBack);

		return sb.toString();
	}
}


