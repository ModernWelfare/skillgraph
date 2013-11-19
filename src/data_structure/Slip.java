package data_structure;

/**
 * Class to implement the node for the slip parameter inside the skill graph
 * 
 * @author Bohao Li <bli@wpi.edu>
 * @author Douglas Selent
 * 
 */
public class Slip extends Node implements Comparable<Slip>
{
	private Item item;

	public Slip(int index, String name)
	{
		super(index, name);
	}

	public Slip(Slip otherSlip)
	{
		super(otherSlip.getIndex(), otherSlip.getName());
		item = new Item(otherSlip.getItem());
		cptTable = new CPT(otherSlip.getCPTTable());
	}

	public Item getItem()
	{
		return item;
	}

	public void setItem(Item im)
	{
		item = im;
	}

	//output the cpt table in an easily readable form
	//assumes no commas or other annoying characters in the name
	public String getStringCPTTable()
	{
		StringBuilder sb = new StringBuilder();

		sb.append(index + ": " + name + " = did not slip");
		sb.append(",");
		sb.append(index + ": " + name + " = slipped");
		
		sb.append("\n");

		int cptTableSize = cptTable.getSize();

		for(int i=0; i<cptTableSize; i++)
		{
			String binaryString = Integer.toBinaryString(i);

			for(int j=0; j<2; j++)
			{
				sb.append(cptTable.getProbability(binaryString, j));
				sb.append(",");				
			}

			sb.deleteCharAt(sb.length()-1);
			sb.append("\n");
		}

		sb.deleteCharAt(sb.length()-1);

		return sb.toString();
	}

	// see if two items are equal
	// throw error if same name and different indices
	// note: only compares name and index
	@Override
	public boolean equals(Object otherSlip)
	{
		boolean returnValue = false;

		if(otherSlip == null)
		{
			returnValue = false;
		}
		else if(this == otherSlip)
		{
			returnValue = true;
		}
		else if(!(otherSlip instanceof Slip))
		{
			// probably an error

			returnValue = false;
		}
		else
		{
			Slip otherSlip1 = (Slip) otherSlip;

			if(name.equals(otherSlip1.getName()) && index == otherSlip1.getIndex())
			{
				if(cptTable.equals(otherSlip1.getCPTTable()))
				{
					returnValue = true;
				}
				else
				{
					System.err.println("Two slip nodes have the same name and index but different cpt's");
					System.err.println(name + " " + index);
					System.err.println(otherSlip1.getName() + " " + otherSlip1.getIndex());
					System.exit(-1);
				}
			}
			else if(name.equals(otherSlip1.getName()) && index != otherSlip1.getIndex())
			{
				System.err.println("Two slip nodes have the same name but different indices");
				System.err.println(name + " " + index);
				System.err.println(otherSlip1.getName() + " " + otherSlip1.getIndex());
				System.exit(-1);
			}
			else if(!name.equals(otherSlip1.getName()) && index == otherSlip1.getIndex())
			{
				System.err.println("Two slip nodes have different names but the same indices");
				System.err.println(name + " " + index);
				System.err.println(otherSlip1.getName() + " " + otherSlip1.getIndex());
				System.exit(-1);
			}
		}

		return returnValue;
	}

	@Override
	public int compareTo(Slip otherSlip)
	{
		// die if null
		if(otherSlip == null)
		{
			System.err.println("Trying to compare to a null slip node");
			System.exit(-1);
		}

		return (index - otherSlip.getIndex());
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		sb.append("Slip index = " + index);
		sb.append("\n");
		sb.append("Slip name = " + name);
		sb.append("\n");
		sb.append("Item name = " + item.getName());
		sb.append("\n");
		sb.append("Slip CPT Table = ");
		sb.append("\n");
		sb.append(cptTable);
		sb.append("\n");

		return sb.toString();
	}
}
