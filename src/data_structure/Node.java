

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
	protected int index;
	protected CPT cptTable;

	public Node(int ix, String n)
	{
		index = ix;
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
}
