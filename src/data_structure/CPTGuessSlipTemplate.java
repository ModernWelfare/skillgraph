

package data_structure;

import java.util.List;


/**
 * CPT template for guess and slip nodes
 * 
 * @author Douglas Selent
 * @author Bohao Li <bli@wpi.edu>
 *
 */
public class CPTGuessSlipTemplate
{
	//keeping guess and slip together for now
	//I don't forsee them having parents and needing specialized cpt tables
	//Didn't create an abstract template class due to the array being the same number of dimensiions but different data


	//made equal dimensions for all, although half will not be used
	//nodes,guess/slip,range
	private double[][][] cptTemplate;

	//used for both guess and slip cpt templates
	public CPTGuessSlipTemplate(List<String> cptInfo)
	{
		int numberOfNodes = cptInfo.size();

		cptTemplate = new double[numberOfNodes][2][2];

		for(int i=0; i<numberOfNodes; i++)
		{
			String row = cptInfo.get(i);

			//only first two elements
			String[] ranges = row.split(",");

			for(int j=0; j<2; j++)
			{
				String[] bounds = ranges[j].split("-");

				try
				{
					double lowerBound = Double.parseDouble(bounds[0]);
					double upperBound = Double.parseDouble(bounds[1]);

					if(lowerBound > upperBound)
					{
						System.err.println("Lower bound > Upper bound in guess + slip template");
						System.err.println(i + " " + j);
						System.err.println(lowerBound + " > " + upperBound);
						System.exit(-1);
					}

					cptTemplate[i][j][0] = lowerBound;
					cptTemplate[i][j][1] = upperBound;
				}
				catch(Exception e)
				{
					e.printStackTrace();
					System.exit(-1);
				}
								
			}
		}
	}

	//copy constructor
	public CPTGuessSlipTemplate(CPTGuessSlipTemplate oldTemplate)
	{
		double[][][] oldMatrix = oldTemplate.getMatrix();
		setMatrix(oldMatrix);
	}

	public double[][][] getMatrix()
	{
		return cptTemplate;
	}

	//deep copies the other matrix
	public void setMatrix(double[][][] otherMatrix)
	{
		for(int i=0; i<otherMatrix.length; i++)
		{
			for(int j=0; j<otherMatrix[i].length; j++)
			{
				for(int k=0; k<otherMatrix[i][j].length; k++)
				{
					cptTemplate[i][j][k] = otherMatrix[i][j][k];
				}
			}
		}
	}

	public double getValue(int index, int guessSlip, int bound)
	{
		return cptTemplate[index][guessSlip][bound];
	}

	public void setValue(int index, int guessSlip, int bound, double value)
	{
		cptTemplate[index][guessSlip][bound] = value;
	}

	public double[] getRange(int index, int guessSlip)
	{
		double[] range = new double[2];

		range[0] = cptTemplate[index][guessSlip][0];
		range[1] = cptTemplate[index][guessSlip][1];

		return range;
	}

	public void setRange(int index, int guessSlip, double lowerBound, double upperBound)
	{
		cptTemplate[index][guessSlip][0] = lowerBound;
		cptTemplate[index][guessSlip][1] = upperBound;
	}

	public void setRange(int index, int guessSlip, double[] range)
	{
		cptTemplate[index][guessSlip][0] = range[0];
		cptTemplate[index][guessSlip][1] = range[1];
	}

	public double getLowerBound(int index, int guessSlip)
	{
		return cptTemplate[index][guessSlip][0];
	}

	public void setLowerBound(int index, int guessSlip, double lowerBound)
	{
		cptTemplate[index][guessSlip][0] = lowerBound;
	}

	public double getUpperBound(int index, int guessSlip)
	{
		return cptTemplate[index][guessSlip][1];
	}

	public void setUpperBound(int index, int guessSlip, double upperBound)
	{
		cptTemplate[index][guessSlip][0] = upperBound;
	}

	@Override
	public boolean equals(Object otherTemplate)
	{
		boolean returnValue = true;

		if (otherTemplate == null)
		{
			returnValue = false;
		} 
		else if(this == otherTemplate)
		{
			returnValue = true;
		}
		else if(!(otherTemplate instanceof CPTGuessSlipTemplate))
		{
			// probably an error

			returnValue = false;
		}
		else
		{
			CPTGuessSlipTemplate otherTemplate1 = (CPTGuessSlipTemplate) otherTemplate;

			for(int i=0; i<cptTemplate.length && returnValue; i++)
			{
				for(int j=0; j<cptTemplate[i].length && returnValue; j++)
				{
					for(int k=0; k<cptTemplate[i][j].length && returnValue; k++)
					{
						if(cptTemplate[i][j][k] != otherTemplate1.getValue(i, j, k))
						{
							returnValue = false;
						}
					}
				}
			}
		}

		return returnValue;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		for(int i=0; i<cptTemplate.length; i++)
		{
			for(int j=0; j<cptTemplate[i].length; j++)
			{
				sb.append(cptTemplate[i][j][0]);
				sb.append("-");
				sb.append(cptTemplate[i][j][1]);
				sb.append(",");
			}

			sb.deleteCharAt(sb.length()-1);
			sb.append("\n");
		}

		sb.deleteCharAt(sb.length()-1);

		return sb.toString();
	}
	
}
