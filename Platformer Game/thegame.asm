# tep32
# Thomas Potter (Teddy)

.include "convenience.asm"
.include "game_settings.asm"
.include "enemy_struct.asm"
.include "projectile_struct.asm"


#	Defines the number of frames per second: 16ms -> 60fps
.eqv	GAME_TICK_MS		16

.data
# don't get rid of these, they're used by wait_for_next_frame.
last_frame_time:  .word 0
frame_counter:    .word 0
	
	
	board:		.word
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
		0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0
		0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0
		0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
		0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0
		0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0
		0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0
		0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0
		0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
		1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1
		
	tile:		.byte
		5, 5, 5, 5, 5
		5, 3, 3, 3, 5
		5, 3, 5, 3, 5
		5, 3, 3, 3, 5
		5, 5, 5, 5, 5
		
	background:	.byte
		7, 7, 7, 7, 7
		7, 7, 7, 7, 7
		7, 7, 7, 7, 7
		7, 7, 7, 7, 7
		7, 7, 7, 7, 7
		
	player:		.byte
		0xFF, 0xFF, 0, 0xFF, 0xFF
		0, 0, 2, 0, 0
		0xFF, 2, 2, 2, 0xFF
		0xFF, 0, 0, 0, 0XFF
		0xFF, 0, 0xFF, 0, 0xFF
		
	enemy:		.byte
		0xFF, 0xFF, 0, 0xFF, 0xFF
		0, 0, 6, 0, 0
		0xFF, 6, 6, 6, 0xFF
		0xFF, 0, 0, 0, 0XFF
		0xFF, 0, 0xFF, 0, 0xFF
		
	lives:		.word 4
	
	player_x:	.word 7
	player_y:	.word 10
	player_LR:	.word 1
	player_inv:	.word 0
	blink_timer:	.word 300
	jump_height:	.word 11
	is_jumping:	.word 0
	
	enemy_tick:	.word 0
	
	projectile:	.word	0:4
	
	enemies_shot:	.word 0
	win_message:	.asciiz "You won!"
	lose_message:	.asciiz "You lose!"


.text
# --------------------------------------------------------------------------------------------------

.globl game
game:
	# set up anything you need to here,
	# and wait for the user to press a key to start.

	# Wait for a key input
_game_wait:
	jal	input_get_keys
	beqz	v0, _game_wait
	jal 	set_enemies_default
	jal 	set_projectile_default

_game_loop:
	# check for input,
	jal     handle_input

	# update everything,

	# draw everything
	jal 	draw_board
	jal 	draw_lives
	jal	check_enemies_shot
	jal 	check_enemies_hit
	jal 	slow_enemies
	
	jal	draw_enemies
	jal 	move_player
	jal 	handle_projectile
	jal	handle_jumping

	jal	you_win
	jal 	you_lose
	jal	display_update_and_clear

	## This function will block waiting for the next frame!
	jal	wait_for_next_frame
	b	_game_loop

_game_over:
	exit



# --------------------------------------------------------------------------------------------------
# call once per main loop to keep the game running at 60FPS.
# if your code is too slow (longer than 16ms per frame), the framerate will drop.
# otherwise, this will account for different lengths of processing per frame.

wait_for_next_frame:
	enter	s0
	lw	s0, last_frame_time
_wait_next_frame_loop:
	# while (sys_time() - last_frame_time) < GAME_TICK_MS {}
	li	v0, 30
	syscall # why does this return a value in a0 instead of v0????????????
	sub	t1, a0, s0
	bltu	t1, GAME_TICK_MS, _wait_next_frame_loop

	# save the time
	sw	a0, last_frame_time

	# frame_counter++
	lw	t0, frame_counter
	inc	t0
	sw	t0, frame_counter
	leave	s0

# --------------------------------------------------------------------------------------------------


# function to draw the map/board - requires no inputs
draw_board:
	enter s0, s1
	
	la s0, board	# starting address of the board
	li s1, 0	# make this our counter for the loop
	
draw_board_loop:

	div t0, s1, 12		# get the row index
	mfhi t1			# get the remainder of the division
	#div t1, t1, 4		# calculate the col index

	mul t0, t0, 5		# get row placement on screen
	mul t1, t1, 5		# get col placement on the screen
	
	add t1, t1, 2		# center on screen
	
	lw t4, (s0)
	beq t4, 0, draw_background
	
	draw_tile:	# draw a tile
		move a1, t0
		move a0, t1
		la a2, tile
		jal display_blit_5x5_trans
		j draw_board_loop_end
	
	draw_background:	# draw a background piece
		move a1, t0
		move a0, t1
		la a2, background
		jal display_blit_5x5_trans
		
draw_board_loop_end:
	inc s1
	add s0, s0, 4
	blt s1, 132, draw_board_loop
	
draw_board_exit:
	leave s0, s1
	
# function to simple make the banner at the bottom of the screen that displays the lives
draw_lives:
#	a0 = top-left corner x
#	a1 = top-left corner y
#	a2 = width
#	a3 = height
#	v1 = color (use one of the constants above)
	enter
	
	li a0, 2
	li a1, 57
	li a2, 60
	li a3, 7
	li v1, 7
	
	jal display_fill_rect
	
	lw t0, lives
	beq t0, 4, three_lives
	beq t0, 3, two_lives
	beq t0, 2, one_life
	beq t0, 1, zero_lives
	
	# if the player has 3 lives left
	three_lives:
		li a0, 36
		li a1, 58
		la a2, player
		jal display_blit_5x5_trans
	
	# if the player has 2 lives left
	two_lives:
		li a0, 43
		li a1, 58
		la a2, player
		jal display_blit_5x5_trans
	
	# if the player has 1 life left
	one_life:
		li a0, 50
		li a1, 58
		la a2, player
		jal display_blit_5x5_trans
		
	# if the player has 0 lives left
	zero_lives:
		leave
	
# simply draws the player on the screen in its (x,y) position
draw_player:
#	a0 = top-left x
#	a1 = top-left y
#	a2 = pointer to pattern (an array of 25 bytes stored row-by-row)
	enter
	
	lw a0, player_x
	lw a1, player_y
	la a2, player
	
	jal display_blit_5x5_trans
	
	leave

# function for controlling player movement and drawing it on the screen
move_player:
	enter s1, s2, s3, s4, s5
	# Your code goes in here

	lw s1, left_pressed
	lw s2, right_pressed
	lw s3, up_pressed	
	
	# down is essentially always active unless it's sitting on something
	lw a0, player_x
	lw a1, player_y
	jal check_down
	move s4, v0
	
	lw s5, action_pressed
	
	beq s1, 1, left
	beq s2, 1, right
	beq s3, 1, up	
	beq s4, 1, down
	beq s5, 1, b_press
	
	j move_player_exit
	
	# if the player can move left, then update its position to go left
	left:
		lw a0, player_x
		lw a1, player_y
		jal check_left
		move t0, v0
		
		beq t0, 0, move_player_exit
		
		lw t6, player_x
		dec t6
		sw t6, player_x
		
		li t0, 0
		sw t0, player_LR
		
		j move_player_exit
	
	# if the player can move right, then update its position to go right
	right:
		lw a0, player_x
		add a0, a0, 5
		lw a1, player_y
		jal check_right
		move t0, v0
		
		beq t0, 0, move_player_exit
		
		lw t6, player_x
		inc t6
		sw t6, player_x
		
		li t0, 1
		sw t0, player_LR
		
		j move_player_exit
	
	# if the player can jump, then activate is_jumping
	up:
		lw a0, player_x
		lw a1, player_y
		jal check_down
		move t0, v0
		beq t0, 1, move_player_exit
	
		lw t1, is_jumping
		beq t1, 1, move_player_exit
		li t2, 1
		sw t2, is_jumping
		
		j move_player_exit
	
	# move the player down a space
	down:		
		lw t6, player_y
		inc t6
		sw t6, player_y
		
		j move_player_exit
	
	# shoot the projectile
	b_press:
		la t0, projectile
		lw t1, proj_moving(t0)
		beq t1, 1, move_player_exit
		li t2, 1
		sw t2, proj_moving(t0)
		
move_player_exit:	
	jal player_blink
	leave
	
# checks if the player is currently jumping
handle_jumping:
	enter s0, s1
	
	lw s0, is_jumping
	lw s1, jump_height
	beq s0, 1, keep_jumping	
	
	# if it's not jumping, then reset the jump height and check if it's standing on a platform before jumping again
	not_jumping:
		li t7, 0
		sw t7, is_jumping
		
		lw a0, player_x
		lw a1, player_y
		jal check_down
		move t0, v0
		beq t0, 1, handle_jumping_exit
		
		li t0, 11
		sw t0, jump_height
		j handle_jumping_exit
	
	# if the player is jumping, let it keep jumping as long as it hasn't exceeded the jump height
	keep_jumping:
		lw a0, player_x
		lw a1, player_y
		jal check_up
		move t0, v0
		beq t0, 0, handle_jumping_exit
		
		dec s1
		beq s1, 0, not_jumping
		sw s1, jump_height		
		
		lw t6, player_y
		dec t6
		sw t6, player_y	
		
		
handle_jumping_exit:
	leave s0, s1
	
# if the player is invincible, then allow it to blink, otherwise just draw it normally
player_blink:
	enter s0, s1
	
	lw s0, player_inv
	lw s1, blink_timer
	div t1, s1, 20
	mfhi t2
	beq s0, 1, disp_blink
	
	# just displays the player normally when it's not invincible
	disp_normal:
		li t0, 0
		sw t0, player_inv
		li t5, 300
		sw t5, blink_timer
		jal draw_player 
		j player_blink_exit
	
	# displays the player blinking while the timer isn't 0 and the player is invincible
	disp_blink:
		dec s1
		sw s1, blink_timer
		beq s1, 0, disp_normal
		bge t2, 10, player_blink_exit
		jal draw_player
		j player_blink_exit
	
player_blink_exit:
	leave s0, s1
		
# set the default values of the project when the program starts
set_projectile_default:
	enter
		
	la t0, projectile
	
	lw t1, player_x
	lw t2, player_y
	li t3, 0
	li t4, 1
	add t1, t1, 5
	inc t2
	sw t1, proj_x(t0)
	sw t2, proj_y(t0)
	sw t3, proj_moving(t0)
	sw t4, proj_LR(t0)
	
	leave
	
# handle all of the needs of the projectile
handle_projectile:
	enter s0, s1, s2
	la s0, projectile	# address of the projectile
	
	lw s1, proj_moving(s0)	# projectiles's moving status
	lw s2, player_LR	# direction player is facing
	beq s1, 1, shoot_proj
	
	# when the projectile is not moving, the player holds it
	with_projectile:
		li t6, 0
		sw t6, proj_moving(s0)
		beq s2, 1, proj_right
		
		# draw the projectile to the left of the player
		proj_left:
			lw a0, player_x
			lw a1, player_y
			dec a0
			inc a1
			sw a0, proj_x(s0)
			sw a1, proj_y(s0)
			li a2, 3
			jal display_set_pixel
			j handle_projectile_exit
		
		# draw the projectile to the right of the player
		proj_right:
			lw a0, player_x
			lw a1, player_y
			add a0, a0, 5
			inc a1
			sw a0, proj_x(s0)
			sw a1, proj_y(s0)
			li a2, 3
			jal display_set_pixel
			j handle_projectile_exit
			
	# if the projectile is still moving, keep it moving until it hits something
	shoot_proj:
		beq s2, 1, shoot_proj_right
		
		# keep it moving left
		shoot_proj_left:
			lw a0, proj_x(s0)
			lw a1, proj_y(s0)
			jal check_left
			move t0, v0
			beq t0, 0, with_projectile
			
			li t5, 0
			sw t5, proj_LR(s0)
			lw a0, proj_x(s0)
			sub a0, a0, 2
			sw a0, proj_x(s0)
			lw a1, proj_y(s0)
			li a2, 3
			jal display_set_pixel		
			j handle_projectile_exit
		
		# keep it moving right
		shoot_proj_right:
			lw a0, proj_x(s0)
			lw a1, proj_y(s0)
			jal check_right
			move t0, v0
			beq t0, 0, with_projectile
			
			li t5, 1
			sw t5, proj_LR(s0)
			lw a0, proj_x(s0)
			add a0, a0, 2
			sw a0, proj_x(s0)
			lw a1, proj_y(s0)
			li a2, 3
			jal display_set_pixel	
			
			j handle_projectile_exit
	
handle_projectile_exit:
	leave s0, s1, s2
	
# check pixels to the left to ensure that the player/enemy/projectile stays in bounds and doesn't go through walls or enemies
check_left:
	enter 
	
	move t0, a0
	move t1, a1
	dec t0
	
	blt t0, 0, check_left_invalid
	bge t0, 60, check_left_invalid
	
	move a0, t0
	move a1, t1
	jal display_get_pixel
	move t2, v0
	
	bne t2, 7, check_left_invalid
	

	check_left_valid:
		li v0, 1
		leave
		
	check_left_invalid:
		li v0, 0
		leave
	
# check pixels to the right to ensure that the player/enemy/projectile stays in bounds and doesn't go through walls or enemies
check_right:
	enter 
	
	move t0, a0
	move t1, a1
	
	blt t0, 0, check_right_invalid
	bge t0, 62, check_right_invalid
	
	move a0, t0
	move a1, t1
	jal display_get_pixel
	move t2, v0
	
	bne t2, 7, check_right_invalid
	

	check_right_valid:
		li v0, 1
		leave
		
	check_right_invalid:
		li v0, 0
		leave
		
# check pixels to above the object to ensure that the player/enemy/projectile stays in bounds and doesn't go through walls or enemies
check_up:
	enter 
	
	move t0, a0
	move t1, a1
	dec t1
	
	blt t1, 0, check_up_invalid
	bge t1, 60, check_up_invalid
	
	move a0, t0
	move a1, t1
	jal display_get_pixel
	move t2, v0
	
	bne t2, 7, check_up_invalid
	

	check_up_valid:
		li v0, 1
		leave
		
	check_up_invalid:
		li v0, 0
		leave

# check pixels to below the object to ensure that the player/enemy/projectile stays in bounds and doesn't go through walls or enemies
check_down:
	enter s0, s1, s2, s3, s5
	
	move s0, a0
	move s1, a1
	add s5, s0, 4
	add s1, s1, 5
	
	blt s1, 0, check_down_invalid
	bge s1, 60, check_down_invalid
	
	move a0, s0
	move a1, s1
	jal display_get_pixel
	move s2, v0
	
	move a0, s5
	move a1, s1
	jal display_get_pixel
	move s3, v0
	
	bne s2, 7, check_down_invalid
	bne s3, 7, check_down_invalid
	

	check_down_valid:
		li v0, 1
		leave s0, s1, s2, s3, s5
		
	check_down_invalid:
		li v0, 0
		leave s0, s1, s2, s3, s5
		
# set predetermined locations for the enemies		
set_enemies_default:
	enter s0
	
	# Set default position of enemy 1
	li a0, 0
	jal enemy_get_element
	move s0, v0
	
	li t0, 25
	li t1, 5
	li t2, 1 # 1 = active
	li t3, 0 # 0 = right
	sw t0, enemy_x(s0)
	sw t1, enemy_y(s0)
	sw t2, enemy_active(s0)
	sw t3, enemy_LR(s0)	
	
	# Set default position of enemy 2
	li a0, 1
	jal enemy_get_element
	move s0, v0
	
	li t0, 15
	li t1, 45
	li t2, 1 # 1 = active
	li t3, 1 # 1 = left
	sw t0, enemy_x(s0)
	sw t1, enemy_y(s0)
	sw t2, enemy_active(s0)
	sw t3, enemy_LR(s0)	
	
	# Set default position of enemy 3
	li a0, 2
	jal enemy_get_element
	move s0, v0
	
	li t0, 35
	li t1, 45
	li t2, 1 # 1 = active
	li t3, 0 # 0 = right
	sw t0, enemy_x(s0)
	sw t1, enemy_y(s0)
	sw t2, enemy_active(s0)
	sw t3, enemy_LR(s0)
	
	# Set default position of enemy 4
	li a0, 3
	jal enemy_get_element
	move s0, v0
	
	li t0, 40
	li t1, 35
	li t2, 1 # 1 = active
	li t3, 1 # 1 = left
	sw t0, enemy_x(s0)
	sw t1, enemy_y(s0)
	sw t2, enemy_active(s0)
	sw t3, enemy_LR(s0)
	
	# Set default position of enemy 5
	li a0, 4
	jal enemy_get_element
	move s0, v0
	
	li t0, 20
	li t1, 35
	li t2, 1 # 1 = active
	li t3, 0 # 0 = right
	sw t0, enemy_x(s0)
	sw t1, enemy_y(s0)
	sw t2, enemy_active(s0)
	sw t3, enemy_LR(s0)
	
	leave s0
	
# move the enemies on the board
move_enemies:
	enter s0, s1
	
	li s1, 0
	
move_enemies_loop:

	move a0, s1
	jal enemy_get_element
	move s0, v0
	
	# checks the direction the enemy should be moving
	lw t3, enemy_LR(s0)
	beq t3, 0, enemy_right

	# move that enemy to the left, if it hits something, change its direction
	enemy_left:
		lw a0, enemy_x(s0)
		lw a1, enemy_y(s0)
		jal check_left
		move t0, v0
		
		beq t0, 0, move_enemies_switch_to_right
		
		lw t6, enemy_x(s0)
		dec t6
		sw t6, enemy_x(s0)
		
		j move_enemies_exit
	
	# move that enemy to the right, if it hits something, change its direction
	enemy_right:
		lw a0, enemy_x(s0)
		add a0, a0, 5
		lw a1, enemy_y(s0)
		jal check_right
		move t0, v0
		
		beq t0, 0, move_enemies_switch_to_left
		
		lw t6, enemy_x(s0)
		inc t6
		sw t6, enemy_x(s0)
		
		j move_enemies_exit
		
# switches the direction its moving to the left
move_enemies_switch_to_left:
	li t2, 1
	sw t2, enemy_LR(s0)
	j move_enemies_exit
	
# switches the direction its moving to the right
move_enemies_switch_to_right:
	li t2, 0
	sw t2, enemy_LR(s0)
	
move_enemies_exit:	
	jal draw_enemies

	inc s1
	ble s1, 4, move_enemies_loop
		
	leave s0, s1
	
# function written to slow down the movement of the enemies so they're not moving at 1 pixel per frame
# it essentially only allows the enemies to be displayed every 4 frames
slow_enemies:
	enter s0
	
	lw s0, enemy_tick
	div t1, s0, 4
	mfhi t2
	
	beq s0, 3, slow_enemies_reset	# reset the counter
	bne t2, 0, slow_enemies_normal
	
	jal  move_enemies
	inc s0
	sw s0, enemy_tick
	j slow_enemies_exit
	
slow_enemies_reset:
	li t0, 0
	sw t0, enemy_tick
	j slow_enemies_exit
		
slow_enemies_normal:	
	inc s0
	sw s0, enemy_tick	
		
slow_enemies_exit:
	leave s0
	
	
# go through each enemy, if it's active, then draw it on the screen
draw_enemies:
	enter s0, s1
	li s1, 0
	
draw_enemies_loop:
	bge s1, 5, draw_enemies_exit

	move a0, s1
	jal enemy_get_element
	move s0, v0
	
	lw t0, enemy_active(s0)
	beq t0, 0, draw_enemies_skip
	
	lw a0, enemy_x(s0)
	lw a1, enemy_y(s0)
	la a2, enemy
	jal display_blit_5x5_trans
	
draw_enemies_skip:
	inc s1
	j draw_enemies_loop
	
draw_enemies_exit:
	leave s0, s1
	
# check the enemies for if they have been hit by a projectile
# checks the left and right sides coordinates of the enemy with the coordinates of the projectile
# also only allows for an enemy to be shot if it is active
check_enemies_shot:
	enter s0, s1, s2
	li s1, 0
	la s2, projectile
	
check_enemies_shot_loop:
	bge s1, 5, check_enemies_shot_exit

	move a0, s1
	jal enemy_get_element
	move s0, v0
	
	lw t6, enemy_active(s0)
	beq t6, 0, check_enemies_shot_skip
	
	lw t0, enemy_x(s0)
	lw t1, enemy_y(s0)
	inc t1
	lw t2, proj_x(s2)
	lw t3, proj_y(s2)	
	lw t4, proj_LR(s2)
	lw t5, proj_moving(s2)
	beq t4, 0, check_enemies_shot_right # if it's coming from the left, the enemy is shot on the right
	
	# if the enemy was shot from the left
	check_enemies_shot_left:
		bne t0, t2, check_enemies_shot_skip
		bne t1, t3, check_enemies_shot_skip
		bne t5, 1, check_enemies_shot_skip
		
		li t7, 0
		sw t7, enemy_active(s0)	
		
		lw t6, enemies_shot
		inc t6
		sw t6, enemies_shot	
	
	# if the enemy was shot from the right
	check_enemies_shot_right:
		add t0, t0, 4
		bne t0, t2, check_enemies_shot_skip
		bne t1, t3, check_enemies_shot_skip
		bne t5, 1, check_enemies_shot_skip
		
		li t7, 0
		sw t7, enemy_active(s0)	
		
		lw t6, enemies_shot
		inc t6
		sw t6, enemies_shot	
	
	
check_enemies_shot_skip:
	inc s1
	j check_enemies_shot_loop
	
check_enemies_shot_exit:
	leave s0, s1, s2
	
# checks if you ever walk into an enemy, similar to check_enemies_shot implementation,
# but with the coordinates of the player's left and right sides
# only allows an enemy to hit the player if the enemy is active
check_enemies_hit:
	enter s0, s1
	li s1, 0
	
check_enemies_hit_loop:
	bge s1, 5, check_enemies_hit_exit

	move a0, s1
	jal enemy_get_element
	move s0, v0
	
	lw t6, enemy_active(s0)
	beq t6, 0, check_enemies_hit_skip
	lw t0, enemy_x(s0)
	lw t1, enemy_y(s0)
	inc t1
	lw t2, player_x
	lw t3, player_y	
	inc t3
	lw t4, player_LR
	lw t5, player_inv
	beq t4, 0, check_enemies_hit_right # if the player is moving from the left, the enemy is hit on the right
	
	# if the enemy was hit from the left
	check_enemies_hit_left:
		add t2, t2, 4
		
		bne t0, t2, check_enemies_hit_skip
		bne t1, t3, check_enemies_hit_skip
		beq t5, 1, check_enemies_hit_skip	
		
		lw t6, lives
		dec t6
		sw t6, lives	
		
		li t5, 1
		sw t5, player_inv
	
	# if the enemy was hit from the right
	check_enemies_hit_right:
		add t0, t0, 4
		bne t0, t2, check_enemies_hit_skip
		bne t1, t3, check_enemies_hit_skip
		beq t5, 1, check_enemies_hit_skip	
		
		lw t6, lives
		dec t6
		sw t6, lives	
		
		li t5, 1
		sw t5, player_inv	
	
check_enemies_hit_skip:
	inc s1
	j check_enemies_hit_loop
	
check_enemies_hit_exit:
	leave s0, s1
	
	
# end the game, display the player wins message
# goes through each enemy and checks if it's active, if there are 5 inactive enemies, then you win
you_win:
	enter s0, s1, s2
	li s0, 0	# number of inactive enemies
	li s1, 0	# selected enemy
		
you_win_loop:
	bge s1, 5, you_win_exit
	
	move a0, s1
	jal enemy_get_element
	move s2, v0
	
	lw t0, enemy_active(s2)		# enemy's active status
	beq t0, 1, skip_win		# if the enemy is active, go to the next enemy
	inc s0				# else, add 1 to the inactive enemy counter
	beq s0, 5, actually_win
	
skip_win:
	inc s1
	j you_win_loop	

actually_win:
	li a0, 10
	li a1, 15
	la a2, win_message
	jal display_draw_text
	jal display_update_and_clear
	
	j _game_over
	
you_win_exit:
	leave s0, s1, s2
		
# end the game, display the player losing message
# just checks how many lives you have left to determine if you lose or not
# my life systems works so that you technically have 4 lives, 
# meaning that you still have one last life when all 3 lives on the screen are gone
# (essentially the movable player on the screen is another life)
you_lose:
	enter 
	lw t0, lives
	bgt t0, 0, skip_lose
	
	li a0, 7
	li a1, 15
	la a2, lose_message
	jal display_draw_text
	jal display_update_and_clear
	
	j _game_over
	
	skip_lose:
		leave
	

