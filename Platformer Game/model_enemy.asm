# tep32
# Thomas Potter (Teddy)
### This file contains the model for entity enemy

.data
	# We don't need this array to be visible outside! So no .globl
	array_of_enemy_structs:	.word	0:20 # Total size = 5 enemies * 4 words

.text

# This function needs to be visible outside of this file
.globl enemy_get_element
# address pixel_get_element(index)
enemy_get_element:
	la	t0, array_of_enemy_structs
				# First we load the address of the beginning of the array
	mul	t1, a0, 16	# Then we multiply the index by 12
				#	(the size of a pixel struct) to calculate the offset
	add	v0, t0, t1	# Finally add the offset to the address of the beginning of the array
	# Now v0 contains the address of the element i of the array
	jr	ra
