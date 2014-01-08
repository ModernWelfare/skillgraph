package util;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import data_structure.Skill;
import data_structure.SkillGraph;

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

		// i = parent, j = child
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

	// copies matrix
	// assumes square matrix
	public static int[][] matrixCopy(final int otherMatrix[][])
	{
		int size = otherMatrix.length;
		int[][] newMatrix = new int[size][size];

		for (int i=0; i<size; i++)
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
		int numberOfSkills = skillGraph.getNumberOfSkills();
		List<Skill> skillList = topologicalSort(skillGraph);

		if(skillList.size() == numberOfSkills)
		{
			returnValue = false;
		}

		return returnValue;
	}

	// determine if the (undirected) skill graph is connected
	public static boolean isMatrixConnected(final int[][] skillMatrix)
	{
		boolean returnValue = false;

		int[][] workingMatrix = GraphFunctions.matrixCopy(skillMatrix);
		int size = workingMatrix.length;

		Stack<Integer> connectedVertices = new Stack<Integer>();
		List<Integer> fringeVertices = new ArrayList<Integer>();
		List<Integer> newFringeVertices = new ArrayList<Integer>();

		// convert to undirected graph

		for(int i=0; i<size; i++)
		{
			for(int j=i; j<size; j++)
			{
				workingMatrix[j][i] = workingMatrix[i][j];
			}
		}

		fringeVertices.add(0);
		connectedVertices.push(0);

		while (!fringeVertices.isEmpty())
		{
			newFringeVertices.clear();

			// for all vertices on the fringe
			for (int i=0; i<fringeVertices.size(); i++)
			{
				int vertex = fringeVertices.get(i);

				// add all vertices that are connected to this one
				for(int j=0; j<size; j++)
				{
					if(workingMatrix[vertex][j] == 1)
					{
						if(!newFringeVertices.contains(j) && connectedVertices.search(j) == -1)
						{
							newFringeVertices.add(j);
							connectedVertices.push(j);
						}
					}
				}
			}

			fringeVertices.clear();

			for (int i = 0; i < newFringeVertices.size(); i++)
			{
				fringeVertices.add(newFringeVertices.get(i));
			}
		}

		if(connectedVertices.size() == size)
		{
			returnValue = true;
		}

		/*
		else
		{
			System.out.println("Failed connectivity check");
			System.out.println(connectedVertices.size());
		 	printSkillMatrix(workingMatrix);
		}
		*/

		return returnValue;
	}

	// returns a topological sort of the graph
	// returns an incomplete list if the graph cannot be sorted (has cycles)
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

			// look for a skill with no incoming edges

			for(int i=0; i<numberOfSkills && skillToAdd == -1; i++)
			{
				// skip if already added
				if(!skillList.contains(skillGraph.getSkill(i)))
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
			}

			// add the skill to the topologically sorted list and update the
			// current matrix
			if(skillToAdd != -1)
			{
				skillList.add(skillGraph.getSkill(skillToAdd));

				for(int i=0; i<numberOfSkills; i++)
				{
					currentSkillMatrix[skillToAdd][i] = 0;
				}
			}
			else
			{
				stuck = true;
			}
		}

		return skillList;
	}

	public static boolean isLevelInRange(int[][] skillMatrix, int minLevel, int maxLevel)
	{
		boolean returnValue = false;
		int numberOfLevels = getNumberOfLevels(skillMatrix);

		if(minLevel <= numberOfLevels && maxLevel >= numberOfLevels)
		{
			returnValue = true;
		}

		return returnValue;
	}

	// slightly different topological sort
	public static int getNumberOfLevels(int[][] skillMatrix)
	{
		if(skillMatrix.length < 1)
		{
			System.err.println("Matrix of zero size when getting the number of levels in the skill graph");
			System.exit(-1);
		}

		int numberOfLevels = 0;
		int numberOfSkills = skillMatrix.length;
		int[][] currentSkillMatrix = matrixCopy(skillMatrix);
		List<Integer> skillList = new ArrayList<Integer>();
		boolean stuck = false;

		while(skillList.size() < numberOfSkills && !stuck)
		{
			List<Integer> addList = new ArrayList<Integer>();

			// look for *all* skills with no incoming edges

			for (int i=0; i<numberOfSkills; i++)
			{
				if(!skillList.contains(i))
				{
					boolean noParents = true;

					for(int j=0; j<numberOfSkills && noParents; j++)
					{
						if (currentSkillMatrix[j][i] == 1)
						{
							noParents = false;
						}
					}

					if(noParents)
					{
						addList.add(i);
					}
				}
			}

			// add the skill to the topologically sorted list and update the
			// current matrix
			for (int i=0; i<addList.size(); i++)
			{
				int skillToAdd = addList.get(i);
				skillList.add(skillToAdd);

				for(int j=0; j<numberOfSkills; j++)
				{
					currentSkillMatrix[skillToAdd][j] = 0;
				}
			}

			if(!addList.isEmpty())
			{
				numberOfLevels++;
			}
		}

		if(skillList.size() != numberOfSkills)
		{
			System.err.println("possible cycle detected when getting the number of levels in the skill graph");
			System.exit(-1);
		}

		return numberOfLevels;
	}

	/**
	 * Generates all files used for the graph
	 * 
	 * @param folderName
	 *            the name of the folder in which the graph file will be stored
	 */
	public static void generateGraphFiles(SkillGraph skillGraph, String folderName)
	{
		QuickFileWriter.createFolder(folderName);

		String skillMatrixFilePath = folderName + File.separator + "SkillGraph.csv";
		outputSkillMatrix(skillGraph.generateSkillMatrix(), skillMatrixFilePath);

		int[] skillItemNum = new int[skillGraph.getNumberOfSkills()];

		for(int i=0; i<skillGraph.getNumberOfSkills(); i++)
		{
			skillItemNum[i] = skillGraph.getSkill(i).getNumberOfItems();
		}

		String itemToSkillMappingFilePath = folderName + File.separator + "ItemToSkillMapping.csv";
		outputItemToSkillMapping(skillItemNum, itemToSkillMappingFilePath);

		String guessAndSlipFilePath = folderName + File.separator + "GuessAndSlipRanges.csv";
		outputGuessAndSlipFile(skillGraph.getNumberOfItems(), guessAndSlipFilePath);

		String mathematicaFilePath = folderName + File.separator;
		outputMathematicaGraphs(skillGraph, mathematicaFilePath);
		
	}

	/**
	 * Outputs the skill to skill mapping matrix to a file
	 * 
	 * @param skillMatrix
	 *            the skill matrix
	 * @param folderName
	 *            the name of the folder in which the file would be stored
	 * 
	 */
	public static void outputSkillMatrix(int[][] skillMatrix, String filePath)
	{
		StringBuilder sb = new StringBuilder();

		for(int i=0; i<skillMatrix.length; i++)
		{
			for(int j=0; j<skillMatrix[i].length; j++)
			{
				sb.append(Integer.toString(skillMatrix[i][j]) + ",");
			}

			sb.append("\n");
		}

		// remove the extra \n
		if(sb.length() > 1)
		{
			sb.deleteCharAt(sb.length()-1);
		}

		// write string to file

		String outputString = sb.toString();
		QuickFileWriter.writeFile(filePath, outputString, false);
	}

	/**
	 * ouputs the item to skill mapping file
	 * 
	 * @param itemNum
	 *            the array that holds the number of items that each skill has
	 * @param filePath
	 *            the file that the item to skill mapping string is to be stored
	 *            to
	 */
	public static void outputItemToSkillMapping(int[] itemNum, String filePath)
	{
		StringBuilder sb = new StringBuilder();

		int itemIndex = 0;

		for(int i=0; i<itemNum.length; i++)
		{
			for(int j=0; j<itemNum[i]; j++)
			{
				sb.append(Integer.toString(itemIndex) + "," + Integer.toString(i) + ",");
				sb.append("Item" + Integer.toString(itemIndex + 1) + "," + "Skill" + Integer.toString(i + 1));
				sb.append("\n");
				itemIndex++;
			}
		}

		// remove the extra \n
		if (sb.length() > 1)
		{
			sb.deleteCharAt(sb.length()-1);
		}

		// write string to file

		String outputString = sb.toString();
		QuickFileWriter.writeFile(filePath, outputString, false);
	}

	/**
	 * Outputs the file containing the guess and slip ranges for each item
	 * 
	 * @param folderName
	 *            the folder in which the file would be stored
	 * @param numberOfItems
	 *            the number of guess and slip entries that should be generated
	 */
	public static void outputGuessAndSlipFile(int numberOfItems, String filePath)
	{
		StringBuilder sb = new StringBuilder();

		for (int i=0; i<numberOfItems; i++)
		{
			String minGuess = Double.toString(Globals.getMinGuess());
			String maxGuess = Double.toString(Globals.getMaxGuess());
			String minSlip = Double.toString(Globals.getMinSlip());
			String maxSlip = Double.toString(Globals.getMaxSlip());

			sb.append(minGuess);
			sb.append("-");
			sb.append(maxGuess);

			sb.append(",");

			sb.append(minSlip);
			sb.append("-");
			sb.append(maxSlip);

			sb.append(",");

			sb.append("Guess" + Integer.toString(i + 1) + ",");
			sb.append("Slip" + Integer.toString(i + 1) + "\n");
		}

		// remove the extra \n
		if (sb.length() > 1)
		{
			sb.deleteCharAt(sb.length() - 1);
		}

		// write string to file

		String outputString = sb.toString();
		QuickFileWriter.writeFile(filePath, outputString, false);
	}

	public static void outputMathematicaGraphs(SkillGraph skillGraph, String filePath)
	{
		String path1 = filePath.concat("MGraph_All.txt");
		String path2 = filePath.concat("MGraph_NoEdgeLabels.txt");
		String path3 = filePath.concat("MGraph_NoGS.txt");
		String path4 = filePath.concat("MGraph_NoGS_NoEdgeLabels.txt");
		String path5 = filePath.concat("MGraph_OnlyNames.txt");
		String path6 = filePath.concat("MGraph_OnlyNames_NoEdgeLabels.txt");


		String outputString1 = MathematicaGenerator.getMathematicaString(skillGraph, 0, true);
		String outputString2 = MathematicaGenerator.getMathematicaString(skillGraph, 0, false);
		String outputString3 = MathematicaGenerator.getMathematicaString(skillGraph, 2, true);
		String outputString4 = MathematicaGenerator.getMathematicaString(skillGraph, 2, false);
		String outputString5 = MathematicaGenerator.getMathematicaString(skillGraph, 3, true);
		String outputString6 = MathematicaGenerator.getMathematicaString(skillGraph, 3, false);


		QuickFileWriter.writeFile(path1, outputString1, false);
		QuickFileWriter.writeFile(path2, outputString2, false);
		QuickFileWriter.writeFile(path3, outputString3, false);
		QuickFileWriter.writeFile(path4, outputString4, false);
		QuickFileWriter.writeFile(path5, outputString5, false);
		QuickFileWriter.writeFile(path6, outputString6, false);
	}

	public static void outputCPTRanges(String source, String destination)
	{
		QuickFileWriter.copyFile(source, destination);
	}

	public static void printSkillMatrix(int[][] skillMatrix)
	{
		for (int i=0; i<skillMatrix.length; i++)
		{
			for(int j=0; j<skillMatrix[i].length; j++)
			{
				System.out.print(skillMatrix[i][j]);
				System.out.print(" ");
			}

			System.out.println("\n");
		}
	}
}