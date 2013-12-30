package util;

import data_structure.Guess;
import data_structure.Item;
import data_structure.Skill;
import data_structure.SkillGraph;
import data_structure.Slip;

/**
 * Class to generate the mathematica output for a skill graph
 * 
 * 
 * @author Douglas Selent
 * 
 */
public class MathematicaGenerator
{
	private static final String[] alphabet = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

	// no i,l,o
	private static final String[] modifiedAlphabet = { "A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "M", "N", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

	private static final String[] testAlphabet = { "A", "B", "C", "D" };

	private MathematicaGenerator()
	{
	}

	// mathematica is picky on its input
	// using "z" as a separator betwen index and name
	// prefixing index with "I"
	// type: 0 = all, 1 = no indices, 2 = no guess/slip, 3 = only names
	public static String getMathematicaString(SkillGraph skillGraph, int type, boolean edgeLabels)
	{
		int edgeCount = 1;
		StringBuilder sb = new StringBuilder();

		sb.append("LayeredGraphPlot[");
		sb.append("{");

		// graph skills and items
		for(int i = 0; i < skillGraph.getNumberOfSkills(); i++)
		{
			Skill parent = skillGraph.getSkill(i);

			String parentName = parent.getName();
			String parentIndex = Integer.toString(parent.getIndex());
			String parentIdentifier = "";

			if(type == 1 || type == 3)
			{
				parentIdentifier = parentName;
			}
			else
			{
				parentIdentifier = "I" + parentIndex + "z" + parentName;
			}

			int numberOfChildren = parent.getNumberOfChildren();
			int numberOfItems = parent.getNumberOfItems();

			// graph skill-skill links
			for(int j=0; j<numberOfChildren; j++)
			{
				Skill child = parent.getChild(j);

				String childName = child.getName();
				String childIndex = Integer.toString(child.getIndex());
				String childIdentifier = "";

				if (type == 1 || type == 3)
				{
					childIdentifier = childName;
				}
				else
				{
					childIdentifier = "I" + childIndex + "z" + childName;
				}

				if(edgeLabels)
				{
					sb.append("{");
					sb.append(parentIdentifier);
					sb.append("->");
					sb.append(childIdentifier);
					sb.append(", ");
					sb.append("\"");
					sb.append(getEdgeLabel(edgeCount));
					sb.append("\"");
					sb.append("}");
					sb.append(", ");
				}
				else
				{
					sb.append(parentIdentifier);
					sb.append("->");
					sb.append(childIdentifier);
					sb.append(", ");
				}

				edgeCount++;
			}

			// graph skill-item links
			for(int j=0; j<numberOfItems; j++)
			{
				Item item = parent.getItem(j);

				String itemName = item.getName();
				String itemIndex = Integer.toString(item.getIndex());
				String itemIdentifier = "";

				if (type == 1 || type == 3)
				{
					itemIdentifier = itemName;
				}
				else
				{
					itemIdentifier = "I" + itemIndex + "z" + itemName;
				}

				if(edgeLabels)
				{
					sb.append("{");
					sb.append(parentIdentifier);
					sb.append("->");
					sb.append(itemIdentifier);
					sb.append(", ");
					sb.append("\"");
					sb.append(getEdgeLabel(edgeCount));
					sb.append("\"");
					sb.append("}");
					sb.append(", ");
				}
				else
				{
					sb.append(parentIdentifier);
					sb.append("->");
					sb.append(itemIdentifier);
					sb.append(", ");
				}

				edgeCount++;
			}
		}

		if(type < 2)
		{
			// graph guess and slip links
			for(int i=0; i<skillGraph.getNumberOfItems(); i++)
			{
				Item item = skillGraph.getItem(i);
				Guess guess = item.getGuess();
				Slip slip = item.getSlip();

				String itemName = item.getName();
				String itemIndex = Integer.toString(item.getIndex());
				String itemIdentifier = "";

				if (type == 1 || type == 3)
				{
					itemIdentifier = itemName;
				}
				else
				{
					itemIdentifier = "I" + itemIndex + "z" + itemName;
				}

				String guessName = guess.getName();
				String guessIndex = Integer.toString(guess.getIndex());
				String guessIdentifier = "";

				if(type == 1 || type == 3)
				{
					guessIdentifier = guessName;
				}
				else
				{
					guessIdentifier = "I" + guessIndex + "z" + guessName;
				}

				String slipName = slip.getName();
				String slipIndex = Integer.toString(slip.getIndex());
				String slipIdentifier = "";

				if(type == 1 || type == 3)
				{
					slipIdentifier = slipName;
				}
				else
				{
					slipIdentifier = "I" + slipIndex + "z" + slipName;
				}

				if (edgeLabels)
				{
					sb.append("{");
					sb.append(guessIdentifier);
					sb.append("->");
					sb.append(itemIdentifier);
					sb.append(", ");
					sb.append("\"");
					sb.append(getEdgeLabel(edgeCount));
					sb.append("\"");
					sb.append("}");
					sb.append(", ");
				}
				else
				{
					sb.append(guessIdentifier);
					sb.append("->");
					sb.append(itemIdentifier);
					sb.append(", ");
				}

				edgeCount++;

				if(edgeLabels)
				{
					sb.append("{");
					sb.append(slipIdentifier);
					sb.append("->");
					sb.append(itemIdentifier);
					sb.append(", ");
					sb.append("\"");
					sb.append(getEdgeLabel(edgeCount));
					sb.append("\"");
					sb.append("}");
					sb.append(", ");
				}
				else
				{
					sb.append(slipIdentifier);
					sb.append("->");
					sb.append(itemIdentifier);
					sb.append(", ");
				}

				edgeCount++;
			}
		}

		if(sb.length() > 2)
		{
			sb.deleteCharAt(sb.length()-1);
			sb.deleteCharAt(sb.length()-1);
		}

		sb.append("}, ");
		sb.append("VertexLabeling -> True]");
		sb.append("\n");

		return sb.toString();
	}

	private static String getEdgeLabel(int edgeCount)
	{
		int alphabetSize = modifiedAlphabet.length;
		String edgeLabel = "";
		int start = edgeCount;

		while(start > 0)
		{
			int symbolSpot = (start - 1) % alphabetSize;
			edgeLabel = modifiedAlphabet[symbolSpot].concat(edgeLabel);
			start = (start - (symbolSpot + 1)) / alphabetSize;
		}

		return edgeLabel;
	}

	public static void generateOutput(SkillGraph skillGraph, String filePath, int type, boolean edgeLabels)
	{
		if(type < 0 || type > 3)
		{
			type = 3;
		}

		String outputString = getMathematicaString(skillGraph, type, edgeLabels);

		QuickFileWriter.writeFile(filePath, outputString);
	}
}