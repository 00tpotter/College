/* 
 * CS:APP Data Lab 
 * 
 * <Thomas (Teddy) Potter tep32@pitt.edu>
 * 
 * bits.c - Source file with your solutions to the Lab.
 *          This is the file you will hand in to your instructor.
 *
 * WARNING: Do not include the <stdio.h> header; it confuses the dlc
 * compiler. You can still use printf for debugging without including
 * <stdio.h>, although you might get a compiler warning. In general,
 * it's not good practice to ignore compiler warnings, but in this
 * case it's OK.  
 */

#if 0
/*
 * Instructions to Students:
 *
 * STEP 1: Read the following instructions carefully.
 */

You will provide your solution to the Data Lab by
editing the collection of functions in this source file.

INTEGER CODING RULES:

Replace the "return" statement in each function with one
or more lines of C code that implements the function. Your code 
must conform to the following style:

int Funct(arg1, arg2, ...) {
	/* brief description of how your implementation works */
	int var1 = Expr1;
	...
		int varM = ExprM;

	varJ = ExprJ;
	...
		varN = ExprN;
	return ExprR;
}

Each "Expr" is an expression using ONLY the following:
1. Integer constants 0 through 255 (0xFF), inclusive. You are
not allowed to use big constants such as 0xffffffff.
2. Function arguments and local variables (no global variables).
3. Unary integer operations ! ~
4. Binary integer operations & ^ | + << >>

Some of the problems restrict the set of allowed operators even further.
Each "Expr" may consist of multiple operators. You are not restricted to
one operator per line.

You are expressly forbidden to:
1. Use any control constructs such as if, do, while, for, switch, etc.
2. Define or use any macros.
3. Define any additional functions in this file.
4. Call any functions.
5. Use any other operations, such as &&, ||, -, or ?:
6. Use any form of casting.
7. Use any data type other than int.  This implies that you
cannot use arrays, structs, or unions.


You may assume that your machine:
1. Uses 2s complement, 32-bit representations of integers.
2. Performs right shifts arithmetically.
3. Has unpredictable behavior when shifting if the shift amount
is less than 0 or greater than 31.


EXAMPLES OF ACCEPTABLE CODING STYLE:
/*
 * pow2plus1 - returns 2^x + 1, where 0 <= x <= 31
 */
int pow2plus1(int x) {
	/* exploit ability of shifts to compute powers of 2 */
	return (1 << x) + 1;
}

/*
 * pow2plus4 - returns 2^x + 4, where 0 <= x <= 31
 */
int pow2plus4(int x) {
	/* exploit ability of shifts to compute powers of 2 */
	int result = (1 << x);
	result += 4;
	return result;
}

FLOATING POINT CODING RULES

For the problems that require you to implement floating-point operations,
	the coding rules are less strict.  You are allowed to use looping and
	conditional control.  You are allowed to use both ints and unsigneds.
	You can use arbitrary integer and unsigned constants. You can use any arithmetic,
	logical, or comparison operations on int or unsigned data.

	You are expressly forbidden to:
	1. Define or use any macros.
	2. Define any additional functions in this file.
	3. Call any functions.
	4. Use any form of casting.
	5. Use any data type other than int or unsigned.  This means that you
	cannot use arrays, structs, or unions.
	6. Use any floating point data types, operations, or constants.


	NOTES:
	1. Use the dlc (data lab checker) compiler (described in the handout) to 
	check the legality of your solutions.
	2. Each function has a maximum number of operations (integer, logical,
			or comparison) that you are allowed to use for your implementation
	of the function.  The max operator count is checked by dlc.
	Note that assignment ('=') is not counted; you may use as many of
	these as you want without penalty.
	3. Use the btest test harness to check your functions for correctness.
	4. Use the BDD checker to formally verify your functions
	5. The maximum number of ops for each function is given in the
	header comment for each function. If there are any inconsistencies 
	between the maximum ops in the writeup and in this file, consider
	this file the authoritative source.

	/*
	 * STEP 2: Modify the following functions according the coding rules.
	 * 
	 *   IMPORTANT. TO AVOID GRADING SURPRISES:
	 *   1. Use the dlc compiler to check that your solutions conform
	 *      to the coding rules.
	 *   2. Use the BDD checker to formally verify that your solutions produce 
	 *      the correct answers.
	 */


#endif
//1
/* 
 * bitXor - x^y using only ~ and & 
 *   Example: bitXor(4, 5) = 1
 *   Legal ops: ~ &
 *   Max ops: 14
 *   Rating: 1
 */
int bitXor(int x, int y)
{
	// Uses the properties of NOT and intersection to derive XOR
	int a = x & y;
	int b = ~x & ~y;
	return ~a & ~b;
}


/* 
 * tmin - return minimum two's complement integer 
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 4
 *   Rating: 1
 */
int tmin(void)
{
	// exploits the shift operation to create the 32-bit two's complement minimum
	int result = 1 << 31;	
	return result;
}


//2
/* 
 * allOddBits - return 1 if all odd-numbered bits in word set to 1
 *   where bits are numbered from 0 (least significant) to 31 (most significant)
 *   Examples allOddBits(0xFFFFFFFD) = 0, allOddBits(0xAAAAAAAA) = 1
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 12
 *   Rating: 2
 */
int allOddBits(int x)
{
	// sets up a bit mask of the correct 101010 pattern, then checks with the input and gives 0 if true, then negate the final answer
	
	int temp;
	int mask;
	int result;
	temp = (0xAA << 8) + (0xAA);
	temp = (temp << 8) + 0xAA;
	temp = (temp << 8) + 0xAA;
	mask = x & temp; 
	result = temp ^ mask;	
	return !result;
}


//3
/* 
 * conditional - same as x ? y : z 
 *   Example: conditional(2,4,5) = 4
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 16
 *   Rating: 3
 */
int conditional(int x, int y, int z)
{
	// Uses the "non-negative is true" principle to determine if x is true or false, and then determines which operand to return
	int bool = ~(!!x) + 1;	// fixes off by one error that I was getting
	int result = (bool & y) | ((~bool) & z);
	return result;
}


//4
/* 
 * logicalNeg - implement the ! operator, using all of 
 *              the legal operators except !
 *   Examples: logicalNeg(3) = 0, logicalNeg(0) = 1
 *   Legal ops: ~ & ^ | + << >>
 *   Max ops: 12
 *   Rating: 4 
 */
int logicalNeg(int x)
{
	// Uses the two's complement and one's complement to solve for the negation of the input;
	int twocomp = ~x + 1;
	int result = ((~x) ^ twocomp) & (~x);
	result = result >> 31;
	return result & 1;
}


//float
/* 
 * floatFloat2Int - Return bit-level equivalent of expression (int) f
 *   for floating point argument f.
 *   Argument is passed as unsigned int, but
 *   it is to be interpreted as the bit-level representation of a
 *   single-precision floating point value.
 *   Anything out of range (including NaN and infinity) should return
 *   0x80000000u.
 *   Legal ops: Any integer/unsigned operations incl. ||, &&. also if, while
 *   Max ops: 30
 *   Rating: 4
 */
int floatFloat2Int(unsigned uf) 
{
	// first separates "uf" into ints 3 main parts, then checks for special cases,
	// calculates the "E" value, and then the "frac" portion with the end 0's truncated
	// finally adds the "1." to the front and determines the sign

	unsigned signBit = uf >> 31;
	unsigned exponent = (uf << 1);
	unsigned mantissa = uf << 9;
	unsigned denorm;
	int result;
	unsigned bigE;

	exponent = (exponent >> 24);
	mantissa = mantissa >> 9;

	if(exponent == 0xFF)
	{
		return 0x80000000u;
	}

	if(uf == 0 || exponent == 0 || exponent < 127)
	{
		return 0;
	}	

        bigE = exponent - 127;

	if(bigE >= 31)
	{
		return 0x80000000u;	
	}

	if(mantissa == (0x7FFFFFFF >> 9))
	{
		denorm = mantissa;	
	}

	if(bigE <= 22)
	{
		denorm = mantissa >> (23 - bigE);
	}
	else
	{
		denorm = mantissa << (bigE - 23);
	}

	result = denorm + (1 << bigE);
	if(signBit == 1)
	{
		result = -result;
	}
		
	return result;
}
