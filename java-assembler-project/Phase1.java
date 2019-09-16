
import java.util.LinkedList;
import java.util.List;

public class Phase1 {

    /* Translates the MAL instruction to 1-3 TAL instructions
     * and returns the TAL instructions in a list
     *
     * mals: input program as a list of Instruction objects
     *
     * returns a list of TAL instructions (should be same size or longer than input list)
     */
    public static List<Instruction> mal_to_tal(List<Instruction> mals) {
		final int AT_REGISTER_VALUE = 1; //the $at register is register #1, hardcoded as such so it is constant throughout
		List<Instruction> tals = new LinkedList<Instruction>();
		
		//instruction format *** new Instruction(instruction_id, rd, rs, rt, Imm, jump_Add, shift_amt, label_id, branch_label)
		//for loop goes through each instruction individually from the inputted 'mals' list
		//all non - pseudo instructions copy their parent
		//pseudo either break into their real instructions or stay based on conditions (immediate field > or < 16 bits) (bne and beq are never not pseudo)
		for(Instruction mal: mals)
		{
		
			//addiu - sometimes pseudo (addiu rt,rs,imm)
			if(mal.instruction_id == 1)
			{
				
				int numBits = countBits(mal.immediate);
				
				if(numBits > 16)
				{
					int upperSixteen = mal.immediate >> 16; //to isolate first (1,2,3 or 4) bytes i.e recieve 0x00F0 from 0xF000000
					int lowerSixteen = (mal.immediate & 0xFFFF); //isolate last 4 bits (& with 0000 0000 0000 0000 1111 1111 1111 1111 bitmask) *would take on 0x0000 from example aboce
					
					
					//lui - (lui at, upper 16-bit)
					tals.add(new Instruction(9, 0, 0, AT_REGISTER_VALUE,upperSixteen, 0, 0, mal.label_id, 0 ));
					//ori - (ori at, at, lower 16-bit)
					tals.add(new Instruction(10, 0, AT_REGISTER_VALUE, AT_REGISTER_VALUE, lowerSixteen, 0, 0, 0, 0));
					//addu - (rt, rs, at)
					tals.add(new Instruction(2, mal.rt, mal.rs, AT_REGISTER_VALUE, 0, 0, 0, 0, 0));
				}
				else
				{
					tals.add(mal.copy());
				}
				
			}
			
			
			//addu - nonpseudo
			if(mal.instruction_id == 2)
			{
				tals.add(mal.copy());
			}
			
			
			//or - nonpseudo
			if(mal.instruction_id == 3)
			{
				tals.add(mal.copy());
			}
			
			
			//beq - nonpseudo
			if(mal.instruction_id == 5)
			{
				tals.add(mal.copy());
			}
			
			
			//bne - nonpseudo
			if(mal.instruction_id == 6)
			{
				tals.add(mal.copy());
			}
			
			
			//slt - nonpseudo
			if(mal.instruction_id == 8)
			{
				tals.add(mal.copy());
			}
			
			
			//lui - nonpseudo
			if(mal.instruction_id == 9)
			{
				tals.add(mal.copy());
			}
			
			
			//ori - sometimes pseudo
			if(mal.instruction_id == 10)
			{
				int numBits = countBits(mal.immediate);
				int upperSixteen = mal.immediate >> 16; //to isolate first (1,2,3 or 4) bits
				int lowerSixteen = (mal.immediate & 0xFFFF); //isolate last 4 bits (& with 0000 0000 0000 0000 1111 1111 1111 1111 bitmask)
				
				if(numBits <=16)
				{
					tals.add(mal.copy());
				}
				else
				{
					
					//lui - (lui at, upper 16-bit)
					tals.add(new Instruction(9, 0, 0, AT_REGISTER_VALUE,upperSixteen, 0, 0, mal.label_id, 0 ));
					//ori - (ori at, at, lower 16-bit)
					tals.add(new Instruction(10, 0, AT_REGISTER_VALUE, AT_REGISTER_VALUE, lowerSixteen, 0, 0, 0, 0));
					//or - (rd, rs, rt)
					tals.add(new Instruction(3, mal.rs, mal.rt, AT_REGISTER_VALUE, 0, 0, 0, 0, 0));
				}
			}
			
			//blt - always pseudo
			if(mal.instruction_id == 100)
			{
				//blt breaks into slt and bne
				
				//slt $at, rs, rt
				tals.add(new Instruction(8, AT_REGISTER_VALUE, mal.rs, mal.rt,0,0,0,mal.label_id,0));
				//bne at, zero, label_id
				tals.add(new Instruction(6, 0, AT_REGISTER_VALUE, 0, 0, 0, 0, 0, mal.branch_label));
			}
			
			
			//bge - always pseudo
			if(mal.instruction_id == 101 )
			{
				//bge breaks into slt and beq
				
				//slt at, rs, rt
				tals.add(new Instruction(8, AT_REGISTER_VALUE, mal.rs,mal.rt,0, 0, 0, mal.label_id, 0));
				
				//beq at, zero, label_id
				tals.add(new Instruction(5, 0, AT_REGISTER_VALUE, 0, 0, 0, 0, 0, mal.branch_label) );
			}
			
		}
        return tals;
    }
	
	//helper methods
	public static int countBits (int number)
	{
		int count = 0; 
        while (number != 0) 
        { 
            count++; 
            number >>= 1; 
        } 
          
        return count; 
    }

}
