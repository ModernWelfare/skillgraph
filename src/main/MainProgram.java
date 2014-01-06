package main;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import data_storage.ResultStorage;
import data_structure.*;
import util.*;

//guess + slip setting is designed wrong but will work for now
//results should be set after graph creation by getting all graph information from the graph
public class MainProgram
{
	private static int bestGraphIndex;
	private static double bestRMSE;
	private static int numberOfStudents;
	private static SkillGraph originalGraph;
	private static SkillGraph bestGraph;

	//original graph indices
	private static int graphIndex;

	private static void runSearch(String graphName, int[] graphParameters, int numberOfFakeSkills, ResultStorage results)
	{
		QuickFileWriter.createFolder(graphName);

		//output original graph

		String originalPath = "ground_truth";
		RandomGraphGenerator.generateRandomGraph(graphParameters, graphName + File.separator + originalPath);

		SkillGraph graph = new SkillGraph(
				graphName + File.separator + originalPath + File.separator + "SkillGraph.csv",
				graphName + File.separator + originalPath + File.separator + "ItemToSkillMapping.csv",
				"Example" + File.separator + "CPT_Ranges1.csv",
				graphName + File.separator + originalPath + File.separator + "GuessAndSlipRanges.csv");

		GraphFunctions.outputMathematicaGraphs(graph, graphName + File.separator + originalPath + File.separator);
		MatlabFileWriter.outputSkillGraphMatlabFile(graph, getGraphIndex(), "currentDag.m", getNumberOfStudents());

		setOriginalSkillGraph(graph);

		int levels = GraphFunctions.getNumberOfLevels(graph.generateSkillMatrix());
		results.setNumberOfLevels(levels);

		//generate data for real graph

		generateDataFromDag(graphName + File.separator + originalPath + File.separator);

		//create fake skills

		for(int i=0; i<numberOfFakeSkills; i++)
		{
			graph.generateFakeSkill();
		}

		GraphFunctions.generateGraphFiles(graph, graphName + File.separator + "fake_graph");
		MatlabFileWriter.outputSkillGraphMatlabFile(graph, getGraphIndex(), "currentDag.m", getNumberOfStudents());

		//run search on fake graph

		// get the number of evaluation iterations
		int iterationNum = graph.getNumberOfSkills()-1;

		// set the current graph we're working with
		SkillGraph currentGraph = graph;

		for(int i=0; i<iterationNum; i++)
		{
			String iterationNumber = "Iteration_" + Integer.toString(i+1);
			QuickFileWriter.createFolders(graphName + File.separator + iterationNumber);

			currentGraph = selectBestMergedGraph(currentGraph, graphName + File.separator + iterationNumber);
			String bestGraphIndexString = "best_graph_" + Integer.toString(getBestGraphIndex());
			GraphFunctions.generateGraphFiles(currentGraph, graphName + File.separator + iterationNumber + File.separator + bestGraphIndexString);
		}
	}

	private static void setBestGraphIndex(int bix)
	{
		bestGraphIndex = bix;
	}

	private static int getBestGraphIndex()
	{
		return bestGraphIndex;
	}

	private static void setNumberOfStudents(int nos)
	{
		numberOfStudents = nos;
	}

	private static int getNumberOfStudents()
	{
		return numberOfStudents;
	}

	private static void setGraphIndex(int gx)
	{
		graphIndex = gx;
	}

	private static int getGraphIndex()
	{
		return graphIndex;
	}

	private static void setBestRMSE(double rmse)
	{
		bestRMSE = rmse;
	}

	private static double getBestRMSE()
	{
		return bestRMSE;
	}

	private static void setOriginalSkillGraph(SkillGraph graph)
	{
		originalGraph = graph;
	}

	private static SkillGraph getOriginalSkillGraph()
	{
		return originalGraph;
	}

	private static void setBestSkillGraph(SkillGraph graph)
	{
		bestGraph = graph;
	}

	private static SkillGraph getBestSkillGraph()
	{
		return bestGraph;
	}

	/**
	 * selects the best merged graph given a graph
	 * 
	 * @param graph
	 *            the graph to be merged
	 * @return the best performing merged graph
	 */
	private static SkillGraph selectBestMergedGraph(SkillGraph graph, String iterationDir)
	{
		SkillGraph bestGraph = graph;

		if(graph.getNumberOfSkills() != 1)
		{
			int selectedIndex = 0;
			int iterationIndex = 0;
			List<String> matlabResults = new ArrayList<String>();
			List<Point> possibleMerges = GraphFunctions.getAllPossibleMerges(graph);
			List<SkillGraph> mergedGraphs = new ArrayList<SkillGraph>();

			GraphFunctions.generateGraphFiles(graph, iterationDir + File.separator + "starting_graph");

			for(Point p : possibleMerges)
			{
				// first create a fresh copy of the original graph
				SkillGraph mergedGraph = new SkillGraph(graph);

				// perform the merge
				mergedGraph.mergeSkills(p.x, p.y);

				// outputs the merged graphs
				GraphFunctions.generateGraphFiles(mergedGraph, iterationDir + File.separator + Integer.toString(possibleMerges.indexOf(p)));
				mergedGraphs.add(mergedGraph);
			}

			// evaluate the graphs and store the results
			for(SkillGraph g : mergedGraphs)
			{
				matlabResults.add(getGraphEvaluationResults(g, iterationIndex, iterationDir));
				iterationIndex++;
			}

			selectedIndex = selectBestResultIndex(matlabResults, mergedGraphs);
			printResults(iterationDir, matlabResults, selectedIndex);

			bestGraph = mergedGraphs.get(selectedIndex);
			setBestGraphIndex(selectedIndex);
		}

		return bestGraph;
	}

	private static void printResults(String iterationDir, List<String> matlabResults, int selectedIndex)
	{
		String filePath = iterationDir + File.separator + "results.txt";
		StringBuilder sb = new StringBuilder();

		for(String s : matlabResults)
		{
			sb.append(s);
			sb.append("\n");
		}

		sb.append("Selected index: " + Integer.toString(selectedIndex));
		QuickFileWriter.writeFile(filePath, sb.toString(), false);
	}

	/**
	 * Selects the best graph given a list of result strings
	 * 
	 * @param matlabResults
	 *            a list of strings of matlab results
	 * @return the index of the best result string
	 */
	private static int selectBestResultIndex(List<String> matlabResults, List<SkillGraph> graphList)
	{
		int minIndex = 0;
		String firstResult = matlabResults.get(0);
		double smallestRMSE = Double.parseDouble(firstResult.split(" ")[2]);

		for(int i=0; i<matlabResults.size(); i++)
		{
			String s = matlabResults.get(i);
			double rmse = Double.parseDouble(s.split(" ")[2]);

			if(rmse <= smallestRMSE)
			{
				smallestRMSE = rmse;
				minIndex = i;

				if(smallestRMSE <= bestRMSE)
				{
					bestRMSE = smallestRMSE;
					setBestSkillGraph(graphList.get(i));
				}
			}
		}

		return minIndex;
	}

	/**
	 * Generates a matlab file that represents the given graph, runs matlab code
	 * on the generated file and retrieves the matlab evaluation results
	 * 
	 * @param graph
	 *            the graph to be evaluated
	 * @param graphIndex
	 *            the index of the graph
	 * @return a string representing the evaluation results
	 */
	private static String getGraphEvaluationResults(SkillGraph graph, int iterationIndex, String iterationDir)
	{
		String returnString = "";
		MatlabFileWriter.outputSkillGraphMatlabFile(graph, getGraphIndex(), "currentDag.m", getNumberOfStudents());

		try
		{

			/*
			// run the matlab command on a new process
			Process p = Runtime.getRuntime().exec(Globals.getMatlabPath() +
					"matlab -nodisplay -wait -nosplash -nodesktop -r \"cd bnt; "
							+ "addpath(genpathKPM(pwd)); "
							+ "cd ../matlab_scripts; "
							+ "run CreateDLMObject; " + "run Evaluation1; "
							+ "exit;\"");
			*/



			Process p = Runtime.getRuntime().exec(Globals.getMatlabPath() +
					"matlab -nodisplay -wait -nosplash -nodesktop -r \" "
							+ "cd matlab_scripts; "
							+ "run CreateDLMObject; " + "run Evaluation1; "
							+ "exit;\"");



			p.waitFor();

			int returnValue = p.exitValue();
			

			if(returnValue != 0)
			{
				System.err.println("MATLAB process ended with a non-zero return value");
				System.exit(-1);
			}

			// return the result string generated by matlab when the process is done

			String source = "matlab_scripts" + File.separator + "predictions.txt";
			String destination = iterationDir + File.separator + Integer.toString(iterationIndex) + File.separator + "predictions.txt";

			QuickFileWriter.copyFile(source, destination);

			returnString = FileReader.readFile("matlab_scripts" + File.separator + "results.txt").get(0);
			return returnString;

		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			System.exit(-1);
		}

		return returnString;
	}

	private static void generateDataFromDag(String filePath)
	{
		try
		{
			Process p = Runtime.getRuntime().exec(Globals.getMatlabPath() +
					"matlab -nodisplay -wait -nosplash -nodesktop -r \" "
							+ "cd matlab_scripts; "
							+ "run GenerateDataFromDag; "
							+ "exit;\"");

			p.waitFor();
			int returnValue = p.exitValue();
			
			if(returnValue != 0)
			{
				System.err.println("MATLAB process ended with a non-zero return value");
				System.exit(-1);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			System.exit(-1);
		}

		//copy over graph files

		String fileName = "UID" + Integer.toString(getGraphIndex()) + "Data" + getNumberOfStudents() + ".csv";
		String source = "matlab_scripts" + File.separator + fileName;
		String destination = filePath + fileName;

		String fileName2 = "UID" + Integer.toString(getGraphIndex()) + "LatentData" + getNumberOfStudents() + ".csv";
		String source2 = "matlab_scripts" + File.separator + fileName2;
		String destination2 = filePath + fileName2;

		QuickFileWriter.copyFile(source, destination);
		QuickFileWriter.copyFile(source2, destination2);
	}

	public static void main(String args[])
	{

		graphIndex = 0;
		Globals.setMatlabPath("C:\\Program Files\\MATLAB\\R2012a\\bin\\");
		Globals.setRandomSeed(4);



		bestGraphIndex = -1;
		bestRMSE = 9999;
		numberOfStudents = 50;
		Globals.setMinGuess(0.1);
		Globals.setMaxGuess(0.3);
		Globals.setMinSlip(0.05);
		Globals.setMaxSlip(0.1);

		ResultStorage results = new ResultStorage();

		
		//skill lower/upper, item lower/upper, level lower/upper
		int[] graphParameters = { 2, 3, 2, 3, 1, 3 };
		String graphName = "test_graph";
		int numberOfFakeSkills = 1;

		runSearch(graphName, graphParameters, numberOfFakeSkills, results);

		boolean learnedBack = originalGraph.compareGraph(bestGraph);

		results.setBestRMSE(getBestRMSE());
		results.setLearnedBack(learnedBack);

		QuickFileWriter.writeFile("results.csv", results.toString(), true);


		/*

		int studentArray[] = {50, 100, 150, 200};
		int itemArray[] = {2, 4, 6, 8};
		int skillArray[] = {3, 5, 7, 9};

		//only use possible levels
		//this can cause problems, just go with random levels for now
		//int levelArray[] = {2, 3, 4, 5, 6, 7, 8, 9};

		double guessArray[] = {0.0, 0.1, 0.3, 0.5};
		double slipArray[] = {0.0, 0.08, 0.16, 0.25};

		//only use possible
		int numberOfFakeSkills[] = {1, 2, 3, 4};

		//student indices
		for(int i=0; i<studentArray.length; i++)
		{
			int students = studentArray[i];
			setNumberOfStudents(students);

			//item indices
			for(int j=0; j<itemArray.length; j++)
			{
				int items = itemArray[j];

				//skill indices
				for(int k=0; k<skillArray.length; k++)
				{
					int skills = skillArray[k];

					for(int l=0; l<guessArray.length; l++)
					{
						double guess = guessArray[l];
						double slip = slipArray[l];

						//this will work, but is not correct way
						Globals.setMinGuess(guess);
						Globals.setMaxGuess(guess);
						Globals.setMinSlip(slip);
						Globals.setMaxSlip(slip);						

						for(int m=0; m<numberOfFakeSkills.length; m++)
						{
							int fakeSkills = numberOfFakeSkills[m];

							if(items-fakeSkills > 0)
							{
								bestGraphIndex = -1;
								bestRMSE = 9999;

								int lowerLevel = 2;
								int upperLevel = skills;

								int graphParameters[] = new int[6];

								graphParameters[0] = skills;
								graphParameters[1] = skills;
								graphParameters[2] = items;
								graphParameters[3] = items;
								graphParameters[4] = lowerLevel;
								graphParameters[5] = upperLevel;

								StringBuilder sb = new StringBuilder();

								sb.append("Graph");
								sb.append(i);
								sb.append(j);
								sb.append(k);
								sb.append(l);
								sb.append(m);
								sb.append("_");
								sb.append("s");
								sb.append(students);
								sb.append("i");
								sb.append(items);
								sb.append("s");
								sb.append(skills);
								sb.append("g");
								sb.append((new Double(guess*100)).intValue());
								sb.append("s");
								sb.append((new Double(slip*100)).intValue());
								sb.append("f");
								sb.append(fakeSkills);

								String graphName = sb.toString();

								//

								ResultStorage results = new ResultStorage();
								
								results.setUID(graphIndex);
								results.setNumberOfStudents(students);
								results.setNumberOfItems(items);
								results.setNumberOfRealSkills(skills);

								//needs to be set when graph is generated
								//results.setNumberOfLevels();

								results.setGuessValue(guess);
								results.setSlipValue(slip);
								results.setNumberOfFakeSkills(fakeSkills);


								runSearch(graphName, graphParameters, fakeSkills, results);

								boolean learnedBack = originalGraph.compareGraph(bestGraph);

								results.setBestRMSE(getBestRMSE());
								results.setLearnedBack(learnedBack);

								QuickFileWriter.writeFile("results.csv", results.toString(), true);
								graphIndex++;
							}
						}
					}
				}
			}
		}

		*/

	}

}
