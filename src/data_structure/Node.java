

package data_structure;

/**
 * Super class for all nodes in the network
 * 
 * @author Bohao Li <bli@wpi.edu>
 * @author Douglas Selent
 * 
 */
public abstract class Node
{

	protected String name;
	protected int nodeIndex;
	protected int index;
	protected CPT cptTable;

	public Node(int ix, int nx, String n)
	{
		index = ix;
		nodeIndex = nx;
		name = n;

		//using as default cpt table
		cptTable = new CPT();
	}

	public void setIndex(int ix)
	{
		index = ix;
	}

	public int getIndex()
	{
		return index;
	}

	public void setNodeIndex(int nx)
	{
		nodeIndex = nx;
	}

	public int getNodeIndex()
	{
		return nodeIndex;
	}

	public void setName(String n)
	{
		name = n;
	}

	public String getName()
	{
		return name;
	}

	public void setCPTTable(CPT cpt)
	{
		cptTable = cpt;
	}

	public CPT getCPTTable()
	{
		return cptTable;
	}

	public abstract String getStringCPTTable();

	//CPT table for items must be in this order, nodes must also be in the correct order
	//this differs from the example matlab file ordering (guess and slip changed order)
	/*
	skill	Guess	Slip	correct
	0	0	0	0
	1	0	0	0
	0	1	0	0
	1	1	0	0
	0	0	1	0
	1	0	1	0
	0	1	1	0
	1	1	1	0
	0	0	0	1
	1	0	0	1
	0	1	0	1
	1	1	0	1
	0	0	1	1
	1	0	1	1
	0	1	1	1
	1	1	1	1
	*/
	
	//In matlab the nodes must be created in this order
	//skill, guess, slip
	//item cpt is fixed/deterministic given the above three^
	//will give a specific item cpt, not assume to be wrapped in a loop
	public String convertCPTToMatlab(boolean inLoop)
	{
		StringBuilder sb = new StringBuilder();

		int cptSize = cptTable.getSize();
		String[] cptStringArray = new String[cptSize*2];
		String[] cptOrder = generateCPTOrder(cptSize);

		for(int i=0; i<cptSize; i++)
		{
			cptStringArray[i] = Double.toString(cptTable.getProbability(cptOrder[i], 0));

			//System.out.println("CPT ORDER = " + cptOrder[i]);
			
		}

		for(int i=0; i<cptSize; i++)
		{
			cptStringArray[i+cptSize] = Double.toString(cptTable.getProbability(cptOrder[i], 1));
		}

		sb.append("bnet.CPD{i} = tabular_CPD(bnet, i,  'CPT', [ ");

		for(int i=0; i<cptStringArray.length; i++)
		{
			sb.append(cptStringArray[i]);
			sb.append(" ");
		}

		sb.append("]);");
		sb.append("\n");

		String matlabString = sb.toString();

		if(!inLoop)
		{
			matlabString = matlabString.replaceAll("i", Integer.toString(nodeIndex+1));
		}
		
		return matlabString;
	}

	protected String[] generateCPTOrder(int cptSize)
	{
		String[] cptOrder = new String[cptSize];

		int numberOfCharacters = 0;

		if(cptSize == 1)
		{
			numberOfCharacters = 1;
		}
		else
		{

			while(Math.pow(2, numberOfCharacters) < cptSize)
			{
				numberOfCharacters++;
			}
		}

		//need this order for items {"000", "100", "010", "110", "001", "101", "011", "111"};

		for(int i=0; i<cptOrder.length; i++)
		{
			String reversedString = Integer.toBinaryString(i);

			while(reversedString.length() < numberOfCharacters)
			{
				reversedString = "0".concat(reversedString);
			}


			String normalString = (new StringBuilder(reversedString).reverse().toString());

			cptOrder[i] = normalString;
		}

		return cptOrder;
	}
}
