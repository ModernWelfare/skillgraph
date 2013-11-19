

package data_structure;

import java.util.List;

/**
 * Class to implement the node for the slip parameter inside the skill graph
 * 
 * @author Douglas Selent
 * @author Bohao Li <bli@wpi.edu>
 * 
 */
public class CPTSkillTemplate
{
	//made equal dimensions for all, although half will not be used
	private double[][][] cptTemplate;

	public CPTSkillTemplate(List<String> cptInfo)
	{
		//assume square matrix
		int numberOfParents = cptInfo.size();

		cptTemplate = new double[numberOfParents][numberOfParents][2];

		for(int i=0; i<numberOfParents; i++)
		{
			String row = cptInfo.get(i);
			String[] ranges = row.split(",");

			for(int j=0; j<ranges.length; j++)
			{

				String bounds = ranges[j];

				if(bounds.equalsIgnoreCase("x"))
				{
					cptTemplate[i][j][0] = -1;
					cptTemplate[i][j][1] = -1;
				}
				else
				{
					String[] bounds2 = ranges[j].split("-");

					try
					{
						double lowerBound = Double.parseDouble(bounds2[0]);
						double upperBound = Double.parseDouble(bounds2[1]);

						if(lowerBound > upperBound)
						{
							System.err.println("Lower bound > Upper bound");
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
	}

	//copy constructor
	public CPTSkillTemplate(CPTSkillTemplate oldTemplate)
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

	public double getValue(int totalParents, int knownParents, int bound)
	{
		return cptTemplate[totalParents][knownParents][bound];
	}

	public void setValue(int totalParents, int knownParents, int bound, double value)
	{
		cptTemplate[totalParents][knownParents][bound] = value;
	}

	public double[] getRange(int totalParents, int knownParents)
	{
		double[] range = new double[2];

		range[0] = cptTemplate[totalParents][knownParents][0];
		range[1] = cptTemplate[totalParents][knownParents][1];

		return range;
	}

	public void setRange(int totalParents, int knownParents, double lowerBound, double upperBound)
	{
		cptTemplate[totalParents][knownParents][0] = lowerBound;
		cptTemplate[totalParents][knownParents][1] = upperBound;
	}

	public void setRange(int totalParents, int knownParents, double[] range)
	{
		cptTemplate[totalParents][knownParents][0] = range[0];
		cptTemplate[totalParents][knownParents][1] = range[1];
	}

	public double getLowerBound(int totalParents, int knownParents)
	{
		return cptTemplate[totalParents][knownParents][0];
	}

	public void setLowerBound(int totalParents, int knownParents, double lowerBound)
	{
		cptTemplate[totalParents][knownParents][0] = lowerBound;
	}

	public double getUpperBound(int totalParents, int knownParents)
	{
		return cptTemplate[totalParents][knownParents][1];
	}

	public void setUpperBound(int totalParents, int knownParents, double upperBound)
	{
		cptTemplate[totalParents][knownParents][0] = upperBound;
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
		else if(!(otherTemplate instanceof CPTSkillTemplate))
		{
			// probably an error

			returnValue = false;
		}
		else
		{
			CPTSkillTemplate otherTemplate1 = (CPTSkillTemplate) otherTemplate;

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

		//-1 = no cpt entry
		//format = "[lowerBound]X[upperBound],"
		for(int i=0; i<cptTemplate.length; i++)
		{
			for(int j=0; j<cptTemplate[i].length; j++)
			{
				sb.append(cptTemplate[i][j][0]);
				sb.append("x");
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
