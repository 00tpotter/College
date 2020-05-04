Thomas (Teddy) Potter
tep32
tep32@pitt.edu

All instructions should be implemented.
Everything should work properly.

ALUOp - the signal for the ALU operation
MemtoReg - enables writing from memory to register
WriteB - the write enable signal for WE_B
MemRead - enables "ld" on the RAM so that the memory from RAM can be read
BranchSelect - determines which branch operation should be selected to determine if the branch is successful
MemWrite - enables "str" on the RAM so that data can be stored in memory
RegWrite - the write enable signal for WE_A
ALUSrc - sets the B input of the ALU to an immediate rather than a register
halt - halts the processor
put - displays the output on the displays
liControl - allows for writing to a register directly from the zero-extended immediate which is needed for li
SignedUnsigned - allows for either a signed or unsigned immediate to be passed along to the ALU for operations such as addui and addi
jump - enables a jump to occur
jalSelect - allows for the jal instruction to write PC+1 to rs
jrSelect - allows for a register to be jumped to rather than an immediate
branch - enables a branch to occur
