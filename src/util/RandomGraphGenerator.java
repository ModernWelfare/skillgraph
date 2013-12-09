package util;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;


/**
 * Class to hold the functions to generate a random graph given the input
 * parameters
 * 
 * @author Bohao Li <bli@wpi.edu>
 * @author Douglas Selent
 * 
 */
public class RandomGraphGenerator
{

	/**
	 * Function to generate a random skill graph
	 * 
	 * @param skillNumLowerBound
	 *            the lower bound of the number of skills
	 * @param skillNumUpperBound
	 *            the upper bound of the number of skills
	 * @param itemNumLowerBound
	 *            the lower bound of the number of items tied to each skill
	 * @param itemNumUpperBound
	 *            the upper bound of the number of items tied to each skill
	 */
	public static void generateRandomGraph(int[] parameters, String outputFolderPath)
	{
		if(!checkParameterBounds(parameters))
		{
			System.exit(-1);
		}

		int skillNumLowerBound = parameters[0];
		int skillNumUpperBound = parameters[1];
		int itemNumLowerBound = parameters[2];
		int itemNumUpperBound = parameters[3];
		int numberOfLevelsLowerBound = parameters[4];
		int numberOfLevelsUpperBound = parameters[5];

		//bounds check

		int totalItemNum = 0;


		//generate the semi-random skill-skill matrix
		int[][] skillMatrix = makeRandomMatrix(skillNumLowerBound, skillNumUpperBound, numberOfLevelsLowerBound, numberOfLevelsUpperBound);
		int numberOfSkills = skillMatrix.length;

		// initialize the array to hold the number of items associated to each skill
		int[] itemNum = new int[numberOfSkills];

		// generate the number of items for each skill and store them to an
		// array
		for (int i=0; i<numberOfSkills; i++)
		{
			itemNum[i] = ConstantRNG.getNextInt(itemNumLowerBound, itemNumUpperBound);
			totalItemNum += itemNum[i];
		}

		File dir = new File(outputFolderPath);

		if(!dir.exists())
		{
			dir.mkdir();
		}

		String skillPath = outputFolderPath + File.separator + "SkillGraph.csv";
		String itemToSkillMappingPath = outputFolderPath + File.separator + "ItemToSkillMapping.csv";
		String guessSlipPath = outputFolderPath + File.separator + "GuessAndSlipRanges.csv";

		outputSkillMatrix(skillMatrix, skillPath);
		outputItemToSkillMapping(itemNum, itemToSkillMappingPath);
		outputGuessAndSlipFile(totalItemNum, guessSlipPath);
	}


	private static boolean checkParameterBounds(final int[] parameters)
	{
		boolean returnValue = true;

		//check lower bounds

		if(parameters[0] < 1)
		{
			System.err.println("Lower bound for number of skills is less than 1 " + parameters[0]);
			returnValue = false;
		}

		if(parameters[2] < 1)
		{
			System.err.println("Lower bound for number of items per skill is less than 1 " + parameters[2]);
			returnValue = false;
		}

		if(parameters[4] < 1)
		{
			System.err.println("Lower bound for number of levels is less than 1 " + parameters[4]);
			returnValue = false;
		}

		if(parameters[0] > parameters[1])
		{
			System.err.println("Number of skills lower bound > upper bound " + parameters[0] + " " + parameters[1]);
			returnValue = false;
		}

		if(parameters[2] > parameters[3])
		{
			System.err.println("Number of items per skill lower bound > upper bound " + parameters[2] + " " + parameters[3]);
			returnValue = false;
		}

		if(parameters[4] > parameters[5])
		{
			System.err.println("Number of levels lower bound > upper bound " + parameters[4] + " " + parameters[5]);
			returnValue = false;
		}

		return returnValue;
	}

			

	/**
	 *for each number of nodes
	 *	rank the nodes
	 *	an edge cannot be drawn from higher rank to lower rank
	 *	that eliminates isomorphisms
	 *	
	 *iteration0 = start with unconnected graph with rankes nodes [save this graph, I=0]
	 *iteration1 (edge number) = generate all possible graphs with one edge [save these graphs, I=2]
	 *iteration2 (edge number) = for each saves graph of the previous iteration - generate all possible graphs with one more edge [save these graphs]
	 *repeat until no more edges can be drawn
	 *
	 *no cycle check needed
	 *connected check
	 *level check
	 *
	 *choose one of the graphs randomly
	*/
	public static int[][] makeRandomMatrix(int skillNumLowerBound, int skillNumUpperBound, int numberOfLevelsLowerBound, int numberOfLevelsUpperBound)
	{
		//generate all possible matrices
		//remove duplicates
		//remove ones that fail check
		//pick one randomly
		//return that one


		//array list to hold all generated skill matrices
		//this initialization of array lists of generic primitives compiles (not exactly sure why)
		List<int[][]> allSkillMatrices = new ArrayList<int[][]>();
		List<int[][]> allValidSkillMatrices = new ArrayList<int[][]>();


		//for each number of skills generate all possible matrices
		for(int i=skillNumLowerBound; i<=skillNumUpperBound; i++)
		{
			//holds all skill matrices generated for a specific number of skills
			List<int[][]> iterationSkillMatrices = new ArrayList<int[][]>();

			int[][] skillMatrix = new int[i][i];
			int numberOfSkills = skillMatrix.length;

			// start with a disconnected graph
			for(int j=0; j<numberOfSkills; j++)
			{
				for(int k=0; k<numberOfSkills; k++)
				{
					skillMatrix[j][k] = 0;
				}
			}

			//holds skill matrices for the previous and current number of edges in the graph
			List<int[][]> previousEdgeMatrices = new ArrayList<int[][]>();
			List<int[][]> edgeSkillMatrices = new ArrayList<int[][]>();

			iterationSkillMatrices.add(skillMatrix);
			previousEdgeMatrices.add(skillMatrix);

			while(!previousEdgeMatrices.isEmpty())
			{
				//for all skill graphs with a number of edges, add one more edge in all possible ways
				for(int j=0; j<previousEdgeMatrices.size(); j++)
				{
					int[][] currentSkillMatrix = previousEdgeMatrices.get(j);

					for(int k=0; k<numberOfSkills-1; k++)
					{
						for(int l=k+1; l<numberOfSkills; l++)
						{
							if(currentSkillMatrix[k][l] == 0)
							{
								int[][] newSkillMatrix = GraphFunctions.matrixCopy(currentSkillMatrix);
								newSkillMatrix[k][l] = 1;
								edgeSkillMatrices.add(newSkillMatrix);
								iterationSkillMatrices.add(newSkillMatrix);
							}
						}
					}
				}

				//copy current to previous and repeat

				previousEdgeMatrices.clear();

				while(!edgeSkillMatrices.isEmpty())
				{
					previousEdgeMatrices.add(edgeSkillMatrices.remove(0));
				}
			}

			//remove duplicates from iteration, sort + iterate
			//add to whole

			sortMatrices(iterationSkillMatrices);

			int[][] previous = iterationSkillMatrices.get(0);

			for(int j=0; j<iterationSkillMatrices.size(); j++)
			{
				int[][] current = iterationSkillMatrices.get(j);

				if(compareMatrices(previous, current) != 0 || j == 0)
				{
					allSkillMatrices.add(current);
				}

				previous = current;
			}

			//System.out.println("Number ofiteration  graphs = " + iterationSkillMatrices.size());
		}

		//check all and remove failed checks

		//System.out.println("Number of graphs = " + allSkillMatrices.size());

		for(int j=0; j<allSkillMatrices.size(); j++)
		{
			int[][] currentSkillMatrix = allSkillMatrices.get(j);

			if(GraphFunctions.isMatrixConnected(currentSkillMatrix) && GraphFunctions.isLevelInRange(currentSkillMatrix, numberOfLevelsLowerBound, numberOfLevelsUpperBound))
			{
				allValidSkillMatrices.add(currentSkillMatrix);
			}
		}
		

		/*
		for(int i=0; i<allSkillMatrices.size(); i++)
		{
			System.out.println("Matrix " + i);
			GraphFunctions.printSkillMatrix(allSkillMatrices.get(i));
		}
		*/

		/*
		for(int i=0; i<allValidSkillMatrices.size(); i++)
		{
			System.out.println("Matrix " + i);
			GraphFunctions.printSkillMatrix(allValidSkillMatrices.get(i));
		}
		*/

		//choose and return random one
		
		int randomGraphIndex = ConstantRNG.getNextInt(0, allValidSkillMatrices.size()-1);

		//GraphFunctions.printSkillMatrix(allValidSkillMatrices.get(randomGraphIndex));

		return allValidSkillMatrices.get(randomGraphIndex);
	}

	private static void sortMatrices(List<int[][]> iterationSkillMatrices)
	{
		int size = iterationSkillMatrices.size();

		//basic selection sort for now
		for(int i=0; i<size; i++)
		{
			for(int j=i+1; j<size; j++)
			{
				int[][] matrix1 = iterationSkillMatrices.get(i);
				int[][] matrix2 = iterationSkillMatrices.get(j);

				//swap if out of order
				if(compareMatrices(matrix1, matrix2) > 0)
				{
					iterationSkillMatrices.set(i, matrix2);
					iterationSkillMatrices.set(j, matrix1);
				}
			}
		}
	}

	//-1 if matrix 1 is smaller, 0 if equal, 1 if matrix 2 is smaller
	//bigger = first to have a 1
	//assumes equal size
	private static int compareMatrices(int[][] matrix1, int[][] matrix2)
	{
		int returnValue = 0;

		for(int i=0; i<matrix1.length && returnValue == 0; i++)
		{
			for(int j=0; j<matrix1.length && returnValue == 0; j++)
			{
				if(matrix1[i][j] != matrix2[i][j])
				{
					if(matrix1[i][j] == 0)
					{
						returnValue = -1;
					}
					else
					{
						returnValue = 1;
					}
				}
			}
		}

		return returnValue;
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
	private static void outputSkillMatrix(int[][] skillMatrix, String filePath) 
	{
		StringBuilder sb = new StringBuilder();

		for (int i=0; i<skillMatrix.length; i++)
		{
			for (int j=0; j<skillMatrix[i].length; j++)
			{
				sb.append(Integer.toString(skillMatrix[i][j]) + ",");
			}

			sb.append("\n");
		}

		//remove the extra \n
		if(sb.length() > 1)
		{
			sb.deleteCharAt(sb.length()-1);
		}	

		//write string to file

		String outputString = sb.toString();
        	writeFile(filePath, outputString);
	}

	private static void outputItemToSkillMapping(int[] itemNum, String filePath)
	{
		StringBuilder sb = new StringBuilder();

		int itemIndex = 0;

		for(int i=0; i<itemNum.length; i++)
		{
			for(int j=0; j<itemNum[i]; j++)
			{
				sb.append(Integer.toString(itemIndex) + "," + Integer.toString(i) + ",");
				sb.append("Item" + Integer.toString(itemIndex + 1) + "," + "Skill" + Integer.toString(i+1));
				sb.append("\n");
				itemIndex++;
			}
		}

		//remove the extra \n
		if(sb.length() > 1)
		{
			sb.deleteCharAt(sb.length()-1);
		}

		//write string to file

		String outputString = sb.toString();
        	writeFile(filePath, outputString);
	}

	/**
	 * Outputs the file containing the guess and slip ranges for each item
	 * 
	 * @param folderName
	 *            the folder in which the file would be stored
	 * @param numberOfItems
	 *            the number of guess and slip entries that should be generated
	 */
	private static void outputGuessAndSlipFile(int numberOfItems, String filePath)
	{
		StringBuilder sb = new StringBuilder();

		for(int i=0; i<numberOfItems; i++)
		{
			String minGuess = Double.toString(Constants.MIN_GUESS);
			String maxGuess = Double.toString(Constants.MAX_GUESS);
			String minSlip = Double.toString(Constants.MIN_SLIP);
			String maxSlip = Double.toString(Constants.MAX_SLIP);

			sb.append(minGuess);
			sb.append("-");
			sb.append(maxGuess);

			sb.append(",");

			sb.append(minSlip);
			sb.append("-");
			sb.append(maxSlip);

			sb.append(",");

			sb.append("Guess" + Integer.toString(i+1) + ", ");
			sb.append("Slip" + Integer.toString(i+1) + "\n");
		}

		//remove the extra \n
		if(sb.length() > 1)
		{
			sb.deleteCharAt(sb.length()-1);
		}

		//write string to file

		String outputString = sb.toString();
        	writeFile(filePath, outputString);
	}

	private static void writeFile(String filePath, String outputString)
	{
		try
		{
			FileWriter file = new FileWriter(filePath, false);
            
			file.write(outputString);				
			file.flush();
			file.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(-1);
		}
	}


}
