
package util;

import java.util.ArrayList;
import java.util.List;
import java.awt.Point;

import data_structure.*;

/**
 * class for holding various functions on a skill graph
 * 
 * @author Bohao Li <bli@wpi.edu>
 * @author Douglas Selent
 * 
 */
public class GraphFunctions
{
	private GraphFunctions()
	{
	}

	/**
	 * function to get all possible merges given the skill graph
	 * 
	 * @return a list of Points of the two skill indices that can be merged
	 */
	public static List<Point> getAllPossibleMerges(final SkillGraph skillGraph)
	{
		int numberOfSkills = skillGraph.getNumberOfSkills();
		int[][] skillMatrix = skillGraph.generateSkillMatrix();
		List<Point> possibleMerges = new ArrayList<Point>();

		//i = parent, j = child
		for(int i=0; i<numberOfSkills; i++)
		{
			for(int j=0; j<numberOfSkills; j++)
			{
				if(skillMatrix[i][j] == 1)
				{
					if(i == j)
					{
						System.err.println("Self cycle detected");
						System.out.println("Skill index = " + i);
						System.exit(-1);
					}
					else
					{
						SkillGraph skillGraph2 = new SkillGraph(skillGraph);
						skillGraph2.mergeSkills(i, j);

						boolean cycles = cycleTest(skillGraph2);

						if(!cycles)
						{
							Point mergePair = new Point(i, j);
							possibleMerges.add(mergePair);
						}
					}
				}
			}
		}

		return possibleMerges;
	}

	//copies matrix
	//assumes square matrix
	private static int[][] matrixCopy(final int otherMatrix[][])
	{
		int size = otherMatrix.length;
		int[][] newMatrix = new int[size][size];

		for(int i=0; i<size; i++)
		{
			for(int j=0; j<size; j++)
			{
				newMatrix[i][j] = otherMatrix[i][j];
			}
		}


		return newMatrix;
	}

	public static boolean cycleTest(final SkillGraph skillGraph)
	{
		boolean returnValue = true;
		final int[][] skillMatrix = skillGraph.generateSkillMatrix();
		int numberOfSkills = skillGraph.getNumberOfSkills();
		List<Skill> skillList = topologicalSort(skillGraph);
		
		if(skillList.size() == numberOfSkills)
		{
			returnValue = false;
		}

		return returnValue;
	}

	//returns a topological sort of the graph
	//returns an incomplete list if the graph cannot be sorted (has cycles)
	public static List<Skill> topologicalSort(final SkillGraph skillGraph)
	{
		List<Skill> skillList = new ArrayList<Skill>();

		int numberOfSkills = skillGraph.getNumberOfSkills();
		int[][] skillMatrix = skillGraph.generateSkillMatrix();
		int[][] currentSkillMatrix = matrixCopy(skillMatrix);
		
		boolean stuck = false;

		while(skillList.size() < numberOfSkills && !stuck)
		{
			int skillToAdd = -1;

			//look for a skill with no incoming edges

			for(int i=0; i<numberOfSkills && skillToAdd == -1; i++)
			{
				boolean noParents = true;

				for(int j=0; j<numberOfSkills && noParents; j++)
				{
					if(currentSkillMatrix[j][i] == 1)
					{
						noParents = false;
					}
				}

				if(noParents)
				{
					skillToAdd = i;
				}
			}

			//add the skill to the topologically sorted list and update the current matrix
			if(skillToAdd != -1)
			{
				skillList.add(skillGraph.getSkill(skillToAdd));

				for(int i=0; i<numberOfSkills; i++)
				{
					currentSkillMatrix[skillToAdd][i] = 0;
				}
			}
		}
		

		return skillList;
	}
}