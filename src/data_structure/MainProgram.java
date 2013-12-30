package data_structure;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import util.Constants;
import util.FileReader;
import util.GraphFunctions;
import util.MatlabFileWriter;
import util.QuickFileWriter;
import util.RandomGraphGenerator;

public class MainProgram
{

	public static void main(String args[])
	{
		
		int[] graphParameters = { 2, 3, 2, 3, 1, 3 };
		String graphName = "test_graph";
		int numberOfFakeSkills = 1;

		runSearch(graphName, graphParameters, numberOfFakeSkills);


	}

	private static void runSearch(String graphName, int[] graphParameters, int numberOfFakeSkills)
	{
		QuickFileWriter.createFolder(graphName);

		String originalPath = "ground_truth";
		RandomGraphGenerator.generateRandomGraph(graphParameters, graphName + File.separator + originalPath);

		SkillGraph graph = new SkillGraph(
				graphName + File.separator + originalPath + File.separator + "SkillGraph.csv",
				graphName + File.separator + originalPath + File.separator + "ItemToSkillMapping.csv",
				"Example" + File.separator + "CPT_Ranges1.csv",
				graphName + File.separator + originalPath + File.separator + "GuessAndSlipRanges.csv");

		for(int i=0; i<numberOfFakeSkills; i++)
		{
			graph.generateFakeSkill();
		}

		GraphFunctions.generateGraphFiles(graph, graphName + File.separator + "fake_graph");

		// get the number of evaluation iterations
		int iterationNum = graph.getNumberOfSkills()-1;

		// set the current graph we're working with
		SkillGraph currentGraph = graph;

		for(int i=0; i<iterationNum; i++)
		{
			String iterationNumber = "Iteration_" + Integer.toString(i+1);
			QuickFileWriter.createFolders(graphName + File.separator + iterationNumber);

			currentGraph = selectBestMergedGraph(currentGraph, graphName + File.separator + iterationNumber);
			GraphFunctions.generateGraphFiles(currentGraph, graphName + File.separator + iterationNumber + File.separator + "best_graph");
		}
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
			int graphIndex = 0;
			int selectedIndex = 0;
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
				matlabResults.add(getGraphEvaluationResults(g, graphIndex));
				graphIndex++;
			}

			selectedIndex = selectBestResultIndex(matlabResults);
			printResults(iterationDir, matlabResults, selectedIndex);

			bestGraph = mergedGraphs.get(selectedIndex);
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
		}

		sb.append("Selected index: " + Integer.toString(selectedIndex) + "\n");
		QuickFileWriter.writeFile(filePath, sb.toString());
	}

	/**
	 * Selects the best graph given a list of result strings
	 * 
	 * @param matlabResults
	 *            a list of strings of matlab results
	 * @return the index of the best result string
	 */
	private static int selectBestResultIndex(List<String> matlabResults)
	{
		int minIndex = -1;
		String firstResult = matlabResults.get(0);
		double smallestRMSE = Double.parseDouble(firstResult.split(" ")[2]);

		for(int i=0; i<matlabResults.size(); i++)
		{
			String s = matlabResults.get(i);
			double rmse = Double.parseDouble(s.split(" ")[2]);

			if(rmse <= smallestRMSE)
			{
				smallestRMSE = rmse;
			}

			minIndex = i;
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
	private static String getGraphEvaluationResults(SkillGraph graph, int graphIndex)
	{
		String returnString = "";
		MatlabFileWriter.outPutSkillGraphMatlabFile(graph, graphIndex, "sampleDag.m");

		try
		{

			/*
			// run the matlab command on a new process
			Process p = Runtime.getRuntime().exec(Constants.MATLAB_PATH +
					"matlab -nodisplay -wait -nosplash -nodesktop -r \"cd bnt; "
							+ "addpath(genpathKPM(pwd)); "
							+ "cd ../matlab_scripts; "
							+ "run CreateDLMObject; " + "run Evaluation1; "
							+ "exit;\"");
			*/

			

			Process p = Runtime.getRuntime().exec(Constants.MATLAB_PATH +
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

			returnString = FileReader.readCSVFile("results" + File.separator + "results.txt").get(0);
			return returnString;

		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			System.exit(-1);
		}

		return returnString;
	}
}
