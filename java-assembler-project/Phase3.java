
import java.util.LinkedList;
import java.util.List;

public class Phase3 {

    /* Translates each Instruction object into
     * 32-bit numbers.
     *
     * tals: list of Instructions to translate
     *
     * returns a list of instructions in their 32-bit binary representation
     *
     */
    public static List<Integer> translate_instructions(List<Instruction> tals) {
		
		List<Integer> binary = new LinkedList<Integer>(); //should return instructions in decimal form
		
		
		for(Instruction tal : tals)
		{
			//R-format instructions handled here
			if(tal.instruction_id == 2 || tal.instruction_id == 3 || tal.instruction_id == 8)
			{
				int opcode_R = 0b000000; //opcode is zero for all R-format
				int rs = tal.rs;
				int rt = tal.rt;
				int rd = tal.rd;
				int shamt = 0b00000; //always zero for the functions we include
				int funct = 0; //initialize, change based on instruction
				
				//addu funct code is 100001
				if(tal.instruction_id == 2)
				{
					funct = 0b100001;
				}
				//OR funct code is 100101
				else if(tal.instruction_id == 3)
				{
					funct = 0b100101;
				}
				//SLT funct code is 101010
				else if(tal.instruction_id == 8)
				{
					funct = 0b101010;
				}
				
				//concatenate fields to make one instruction
				int result = (opcode_R << 5) | rs;
				result = (result << 5) | rt;
				result = (result << 5) | rd;
				result = (result << 5) | shamt;
				result = (result << 6) | funct;
				
				binary.add(result);
			}
			else //if we reach this else, its an I-format Instruction
			{
				int opcode = 0; //initialize opcode variable (can't set value here because it differs for I format unlike R format where all = 0)
				int rs = tal.rs; //get rs from instruction tal
				int rt = tal.rt; //get rt from instruction tal
				int immediate = tal.immediate; //get immediate from instruction tal
				
				//addiu - opcode is 0b001001
				if(tal.instruction_id == 1)
				{
					opcode = 0b001001;
					immediate = immediate << 16;
					immediate = immediate >>> 16;
				}
				//beq - opcode is 0b000100
				else if(tal.instruction_id == 5)
				{
					opcode = 0b000100;
					
					//for beq immediate value is where it will jump to if condition is satisfied. 
					//immediate negative if we enter top if statement
					//shift left than right to give correct bit pattern
					immediate = immediate << 16;
					immediate = immediate >>> 16;
				}
				//bne
				else if(tal.instruction_id == 6)
				{
					opcode = 0b000101;
					
					//adjust
					
					immediate = immediate << 16;
					immediate = immediate >>> 16;
					
					
				}
				//lui
				else if(tal.instruction_id == 9)
				{
					opcode = 0b001111;
				}
				//ori
				else if(tal.instruction_id == 10)
				{
					opcode = 0b001101;
				}
				
				//concatenate the fields to make one instruction
				int result = (opcode << 5) | rs;
				result = (result << 5) | rt;
				result = (result << 16) | immediate;
				
				binary.add(result);
			}

			
		}
			return binary;
    }
	
}
