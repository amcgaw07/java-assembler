
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Phase2 {

    /* Returns a list of copies of the Instructions with the
     * immediate field of the instruction filled in
     * with the address calculated from the branch_label.
     *
     * The instruction should not be changed if it is not a branch instruction.
     *
     * unresolved: list of instructions without resolved addresses
     * first_pc: address where the first instruction will eventually be placed in memory
     */
    public static List<Instruction> resolve_addresses(List<Instruction> unresolved, int first_pc) {
		
		HashMap<Integer, Integer> labels_to_addresses = new HashMap<Integer, Integer>(); //map addresses to instruction lines
		
		int pcCopy = first_pc;
		//first pass over instruction array, maps labels to their address in the pc
		for(Instruction toBeResolved : unresolved)
		{
			if(toBeResolved.label_id != 0)
			{
				labels_to_addresses.put(toBeResolved.label_id, pcCopy);
			}
			pcCopy += 4; //increment program counter (PC + 4)
		}
		
		List<Instruction> resolvedAddressList = new LinkedList<Instruction>(); //new list to put in instructions whos addresses are resolved
		
		int currPC = first_pc;
		//second pass over instruction array to resolve addresses
		for(Instruction toBeResolved : unresolved)
		{
			if(toBeResolved.branch_label == 0)
			{
				resolvedAddressList.add(toBeResolved.copy());
				currPC += 4;
			}
			else
			{	
				int branchPC = currPC;
				int labelPC = labels_to_addresses.get(toBeResolved.branch_label);
				
				
				int resolvedImmediate = (labelPC - (branchPC + 4)) / 4;
				
				Instruction temp = toBeResolved.copy(); //copy stored into temp so we can change the immediate below
				temp.immediate = resolvedImmediate; //adjust the immediate value to the new calculated one
				
				resolvedAddressList.add(temp);
				currPC += 4;
				
			}
		}
		
        return resolvedAddressList;
    }

}
