# Thomas (Teddy) Potter
# tep32@pitt.edu

.eqv  BOARD_SIZE 25
.eqv  BOARD_WIDTH 5
.eqv  BOARD_HEIGHT 5
.eqv  N_MINES 5



.data
	welc:		.asciiz "Hello and welcome to MARSweeper!\nYou will enter the coordinates of a space to reveal it.\nYou get a point for every tile you correctly reveal.\nThe number of a space indicates how many bombs are adjacent to it.\nHave fun!\n"
	debug:  	.word 0
	rowStr:		.asciiz "Enter a row: "
	colStr:		.asciiz "Enter a column: "
	invalid:	.asciiz "Your selection is invalid.\n"
	win:		.asciiz "Congrats! You won!\nYour score was: "
	lose:		.asciiz "YIKES. You hit a mine and lose. Better luck next time!\nYour score was: "
	score:		.word 0
	row:		.word 0
	col: 		.word 0
	mine: 		.asciiz "M"
	nextLine:	.asciiz "\n"
	space:		.asciiz " "
	header:		.asciiz "\n  0 1 2 3 4 \n -----------\n0"
	count:		.word 0
	inputCount:	.word 1
	tRow:		.word 0
	tCol:		.word 0 
	tilesRevealed:	.word 0
	
	board:		.word  '0', '0', '0', '0', '0' 
         		.word  '0', '0', '0', '0', '0'
         		.word  '0', '0', '0', '0', '0'
         		.word  '0', '0', '0', '0', '0'
         		.word  '0', '0', '0', '0', '0'
         		
        status:		.word  '*', '*', '*', '*', '*' 
         		.word  '*', '*', '*', '*', '*'
         		.word  '*', '*', '*', '*', '*'
         		.word  '*', '*', '*', '*', '*'
         		.word  '*', '*', '*', '*', '*'
	
.text
.globl main
main:
	# Prints welcome message
	la a0, welc
	li v0, 4
	syscall
	
	# Start game loop
	jal game_loop
	
	# Exit program
	li v0, 10
	syscall
	
# main loop for running the game
game_loop:	
	push ra	
	
	
	# Print board
	la a0, header 
	li v0, 4
	syscall
	
	lw t6, debug
	beq t6, 0, print_status
	beq t6, 1, print_debug
	
	print_status:
		la a0, status
		li a1, 0
		li a2, 1
		jal print_board
		j continue_loop
		
	print_debug:
		la a0, board
		li a1, 0
		li a2, 1
		jal print_board
		
continue_loop:	

	# Prompt user for input
	jal user_input
	
	# Generate board and update the tiles (if first iteration)
	lw a3, count
	lw t0, inputCount
	bne t0, 1, rest_of_loop
	jal generate_board
	jal updateTiles
	lw t0, inputCount
	add t0, t0, 1
	sw t0, inputCount
	
rest_of_loop:
	# Reveal tile
	jal reveal_tile
	
	# Check for mine
	
	# Loop back to the top
	
	j game_loop
	
# Jump back to main: and exit program
	pop ra
	jr ra
	
print_board:	
	move t0, a0 # temporarily stores a0 in t0 so that it isn't overwritten
	
	la a0, space # just prints out a space so that we can spread out the tiles
	li v0, 4
	syscall
	
	move a0, t0 # prints out the contents of the tile, uses value saved in t0
	li v0, 4
	syscall
	
	add t0, t0, 4 # increases the address by the word size to go to the next location in the array
	add a1, a1, 1 # increase 1 in the counter of tiles left to print
	
	move a0, t0	# copies the new address into a0
	
	li t2, 0	# this part is just for formatting
	div t2, a1, BOARD_HEIGHT	# divides the counter of tiles to see if we're on a 5th tile/new row (if mod == 0)
	mfhi t1
	
	beq t1, 0, print_nextLine
	
	ble a1, 24, print_board		# loops back to the top
	
	jr ra
	
print_nextLine:
	
	la a0, nextLine		# prints the next line character to show a new row
	li v0, 4
	syscall
	
	beq a2, BOARD_HEIGHT, exit_print	# exits if we've gone past the number of rows in the array
	
	move a0, a2		# prints the number stored in a2, which should be row number
	li v0, 1
	syscall
	
	add a2, a2, 1		# increments a2
	
	move a0, t0		# restores a0 so that it can be used again when it loops
	
	j print_board
	
exit_print:
	jr ra
	
user_input:
# Prompt player for a row and column and retrieve their inputs
	# row prompt
	la a0, rowStr 
	li v0, 4
	syscall
	
	# set row variable
	li v0, 5
	syscall
	sw v0, row
	
	# column prompt
	la a0, colStr
	li v0, 4
	syscall
	
	# set column variable
	li v0, 5
	syscall
	sw v0, col
	
# Check validity of inputs
	valid:
		lw t0, row
		lw t1, col
		bge t0, BOARD_WIDTH, invalid_input
		bge t1, BOARD_HEIGHT, invalid_input
		blt t0, 0, invalid_input
		blt t1, 0, invalid_input

	jr ra

# Tells player their input is invalid and prompts them to reenter the numbers
invalid_input:
	
	# invalid prompt
	la a0, invalid
	li v0, 4
	syscall
	
	j user_input
	
calc_addr:
#	 a0: The base address of the matrix
#	 a1: The index (j) of the row
#	 a2: The index (i) of the column
	push ra

	# Set the offset in t0
	li t0, 4
	mul t0, t0, BOARD_WIDTH
	
	# (i x 20) part of equation in t1
	li t1, 0
	mul t1, a1, t0
	
	# (j x 4) part of equation in t2
	li t2, 0
	mul t2, a2, 4
	
	# sum equation in t3
	li t3, 0
	add t3, t1, t2
	add t3, t3, a0

	move v0, t3 # store in v0 to return address of location in array
	
	pop ra
	jr ra
	
	
rand_int: 
	# Generate a random number and return it in v0
	push ra 
	
	li v0, 42
	li a0, 0
	li a1, 25
	syscall
	move v0, a0
	
	pop ra
	jr ra

generate_board:
	push ra
	
	jal rand_int	# Generate a random location for the bomb
	
	move a0, v0
	jal check_bomb		# Make sure that a bomb isn't placed where one already exists
	move s7, v0
	
	la s0, board
	lw s1, mine
	li s2, 4 		# 4 byte offset
	mul s2, s2, s7		# create offset (4 bytes x random index)
	add s0, s0, s2		# add offset to board address
	sw s1, (s0)		# store "M" (mine) at that address
	
	add a3, a3, 1 		# add 1 to the counter
	blt a3, N_MINES, generate_board		# makes sure it only loops 5 times and places 5 mines
	
	pop ra
	jr ra
	
check_bomb:
	push ra
	move s6, a0
	
	la a0, board
	lw a1, row
	lw a2, col
	jal calc_addr
	move t5, v0
	
	la t0, board
	lw t1, mine
	li t2, 4 		# 4 byte offset
	mul t2, t2, s6		# create offset (4 bytes x random index)
	add t0, t0, t2		# add offset to board address
	
	lw t4, (t0)
	
	beq t5, t0, regenerate_bomb	# regenerate if bomb is placed on first input
	
	# maybe check if the space is set to 0 instead of M???
	beq t1, t4, regenerate_bomb	# regenerate if bomb is place where one already exists
	
	move v0, s6
	pop ra
	jr ra

regenerate_bomb:
# if for some reason the bomb isn't valid, 
# this function generates a new random number and checks it again	
	li v0, 42
	li a0, 0
	li a1, 25
	syscall
	move v0, a0
	
	move a0, v0
	j check_bomb
	
	jr ra
	
updateTiles:
# go through the board, check for mines, and place the number values in the correct spaces
	push ra
	push s0
	push s1
	push s2
	push s3
	
	la s0, board
	lw s1, mine
	lw s2, (s0)
	li s3, 0
	
	# loop to iterate through the board
	iterate_loop:		
		move a0, s0 		
		bne s1, s2, loop_other	# if the current value is NOT a mine, go to jump over the addNums and go to the next part of the loop
		jal addNums
		
	loop_other:
		add s0, s0, 4
		add s3, s3, 1
		lw s2, (s0)
		ble s3, 24, iterate_loop # loop!
		
	pop s3	
	pop s2	
	pop s1
	pop s0
	pop ra
	jr ra
	
addNums:
# do the actual checking and adding of values for the spaces
# a0 should be the address of the mine
	push ra
	push s0
	push s1
	push s2
	
	#li v0, 4
	#syscall
	
	move s0, a0	# address of mine
	
	jal get_loc	# get the location of the mine, saved in tRow and tCol
	
	lw s1, tRow
	lw s2, tCol
	
	# change top left space (row-1, col-1)
	sub t0, s1, 1 	# row of the top left space
	sub t1, s2, 1	# col of the top left space
	move a1, t0
	move a2, t1
	jal validate_loc	# check that this is within the bounds of the matrix, v0 is 1 if true and 0 if false
	
	la a0, board
	beq v0, 1, increment_tile
	
	# change top space (row-1, col)
	sub t0, s1, 1 	# row of the top space
	move t1, s2	# col of the top space
	move a1, t0
	move a2, t1
	jal validate_loc	# check that this is within the bounds of the matrix, v0 is 1 if true and 0 if false
	
	la a0, board
	beq v0, 1, increment_tile
	
	# change top right space (row-1, col+1)
	sub t0, s1, 1 	# row of the top right space
	add t1, s2, 1	# col of the top right space
	move a1, t0
	move a2, t1
	jal validate_loc	# check that this is within the bounds of the matrix, v0 is 1 if true and 0 if false
	
	la a0, board
	beq v0, 1, increment_tile
	
	# change left space (row, col-1)
	move t0, s1 	# row of the top space
	sub t1, s2, 1	# col of the top space
	move a1, t0
	move a2, t1
	jal validate_loc	# check that this is within the bounds of the matrix, v0 is 1 if true and 0 if false
	
	la a0, board
	beq v0, 1, increment_tile
	
	# change right space (row, col+1)
	move t0, s1 	# row of the top space
	add t1, s2, 1	# col of the top space
	move a1, t0
	move a2, t1
	jal validate_loc	# check that this is within the bounds of the matrix, v0 is 1 if true and 0 if false
	
	la a0, board
	beq v0, 1, increment_tile
	
	# change bottom left space (row+1, col-1)
	add t0, s1, 1 	# row of the top space
	sub t1, s2, 1	# col of the top space
	move a1, t0
	move a2, t1
	jal validate_loc	# check that this is within the bounds of the matrix, v0 is 1 if true and 0 if false
	
	la a0, board
	beq v0, 1, increment_tile
	
	# change bottom (row+1, col)
	add t0, s1, 1 	# row of the top space
	move t1, s2	# col of the top space
	move a1, t0
	move a2, t1
	jal validate_loc	# check that this is within the bounds of the matrix, v0 is 1 if true and 0 if false
	
	la a0, board
	beq v0, 1, increment_tile
	
	# change bottom right space (row+1, col+1)
	add t0, s1, 1 	# row of the top space
	add t1, s2, 1	# col of the top space
	move a1, t0
	move a2, t1
	jal validate_loc	# check that this is within the bounds of the matrix, v0 is 1 if true and 0 if false
	
	la a0, board
	beq v0, 1, increment_tile
	
	
	pop s2
	pop s1
	pop s0
	pop ra	
	jr ra
	
increment_tile:
	push ra
	
	jal calc_addr		# calculate the array at that row and col
	lw t7, (v0)
	
	lw t6, mine
	beq t7, t6, exit_inc	# if that tile is a mine ("M") then don't increment it
	
	add t7, t7, 1		# increment its value
	sw t7, (v0)
	
	exit_inc:
		pop ra
		jr ra
	
validate_loc:
# makes sure that the values being changed are actually on the board
	push ra

	la t0, board # first address of array
	#li t1, 4
	#mul t1, t1, BOARD_SIZE  # last address of array
	
	#jal get_loc
	
	#lw t2, tRow
	#lw t3, tCol
	
	move t2, a1
	move t3, a2
	
	bgt t2, 4, return_false
	blt t2, 0, return_false
	bgt t3, 4, return_false
	blt t3, 0, return_false
	
	#bgt t1, a0, return_false	# if it's past the address bounds of the array, return false
	#blt t0, a0, return_false	# if it's before the address bounds of the array, return false
	
	li v0, 1
	pop ra
	jr ra	# return 1 (true), the location was valid
	
	return_false:
	# just returns 0 (false) because a location was invalid
		li v0, 0
		pop ra
		jr ra	# return 0
		
get_loc:
# does the reverse of calc_addr
# it gets the coordinates of the space on the board from the address
# a0 = address of tile
	move t2, a0
	la t3, board
	sub t2, t2, t3
	div t0, t2, 20 		# calculate the row index
	mfhi t1			# get the remainder of the division
	div t1, t1, 4		# calculate the col index
	
	sw t0, tRow		# store these values
	sw t1, tCol
		
	jr ra
	
reveal_tile:
# checks the tile that was input and reveals its value on the board
	push ra
	push s0
	push s1
	
	la a0, board		
	lw a1, row
	lw a2, col
	jal calc_addr
	move s0, v0		# gets the actual value of that tile in the real board
		
	la a0, status
	lw a1, row
	lw a2, col
	jal calc_addr		# gets the location of the tile in the user's board
	move s1, v0		
	
	lw t1, (s0)
	sw t1, (s1)		# reveal the tile	
	lw t0, mine
	
	li t5, 42
	sw t5, (s0)		# change the revealed tile to a * (for debugging mode) 
	
	beq t0, t1, game_over 	# game over if you hit a mine
	
	#sub t1, t1, 48		# subtracting the ASCII offset, it's weird with chars and ints
	#lw t2, score		# increment the score
	#add t2, t1, t2
	#sw t2, score
	
	lw t3, score	# see how many tiles have been revealed
	add t3, t3, 1
	sw t3, score
	
	li t4, BOARD_SIZE	# how many tiles need to be revealed to win
	sub t4, t4, N_MINES
	
	beq t3, t4, game_win	# if all the tiles are cleared, you win
	
	pop s1
	pop s0
	pop ra
	jr ra
	
game_win:
# if all the tiles are revealed and the player hasn't hit a mine,
# print the win screen, show the score and exit the program
# prints the losing message
	la a0, win
	li v0, 4
	syscall
	
	# prints the score
	lw a0, score
	li v0, 1
	syscall
	
	# exit program
	li v0, 10
	syscall
	
game_over:
# tells the player they hit a mine, prints the score, and exits the program
	# prints the losing message
	la a0, lose 
	li v0, 4
	syscall
	
	# prints the score
	lw a0, score
	li v0, 1
	syscall
	
	# exit program
	li v0, 10
	syscall
	
	
	
	
	
