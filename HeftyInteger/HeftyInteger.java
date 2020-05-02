public class HeftyInteger {

	private final byte[] ONE = {(byte) 1};

	private byte[] val;

	/**
	 * Construct the HeftyInteger from a given byte array
	 * @param b the byte array that this HeftyInteger should represent
	 */
	public HeftyInteger(byte[] b) 
	{
		val = b;
	}

	/**
	 * Return this HeftyInteger's val
	 * @return val
	 */
	public byte[] getVal() {
		return val;
	}

	/**
	 * Return the number of bytes in val
	 * @return length of the val byte array
	 */
	public int length() {
		return val.length;
	}

	/**
	 * Add a new byte as the most significant in this
	 * @param extension the byte to place as most significant
	 */
	public void extend(byte extension) {
		byte[] newv = new byte[val.length + 1];
		newv[0] = extension;
		for (int i = 0; i < val.length; i++) {
			newv[i + 1] = val[i];
		}
		val = newv;
	}

	/**
	 * If this is negative, most significant bit will be 1 meaning most
	 * significant byte will be a negative signed number
	 * @return true if this is negative, false if positive
	 */
	public boolean isNegative() {
		return (val[0] < 0);
	}

	// returns true if this is equal to 0
	public boolean isZero()	
	{
		for(int i = 0; i < this.length(); i++)
		{
			if(this.val[i] != 0x00)
			{
				return false;
			}
		}
		return true;
	}

	// returns true if this is equal to other
	public boolean equals(HeftyInteger other)
	{
		if(this.subtract(other).isZero())
		{
			return true;
		}
		return false;
	}

	/**
	 * Computes the sum of this and other
	 * @param other the other HeftyInteger to sum with this
	 */
	public HeftyInteger add(HeftyInteger other) {
		byte[] a, b;
		// If operands are of different sizes, put larger first ...
		if (val.length < other.length()) {
			a = other.getVal();
			b = val;
		}
		else {
			a = val;
			b = other.getVal();
		}

		// ... and normalize size for convenience
		if (b.length < a.length) {
			int diff = a.length - b.length;

			byte pad = (byte) 0;
			if (b[0] < 0) {
				pad = (byte) 0xFF;
			}

			byte[] newb = new byte[a.length];
			for (int i = 0; i < diff; i++) {
				newb[i] = pad;
			}

			for (int i = 0; i < b.length; i++) {
				newb[i + diff] = b[i];
			}

			b = newb;
		}

		// Actually compute the add
		int carry = 0;
		byte[] res = new byte[a.length];
		for (int i = a.length - 1; i >= 0; i--) {
			// Be sure to bitmask so that cast of negative bytes does not
			//  introduce spurious 1 bits into result of cast
			carry = ((int) a[i] & 0xFF) + ((int) b[i] & 0xFF) + carry;

			// Assign to next byte
			res[i] = (byte) (carry & 0xFF);

			// Carry remainder over to next byte (always want to shift in 0s)
			carry = carry >>> 8;
		}

		HeftyInteger res_li = new HeftyInteger(res);

		// If both operands are positive, magnitude could increase as a result
		//  of addition
		if (!this.isNegative() && !other.isNegative()) {
			// If we have either a leftover carry value or we used the last
			//  bit in the most significant byte, we need to extend the result
			if (res_li.isNegative()) {
				res_li.extend((byte) carry);
			}
		}
		// Magnitude could also increase if both operands are negative
		else if (this.isNegative() && other.isNegative()) {
			if (!res_li.isNegative()) {
				res_li.extend((byte) 0xFF);
			}
		}

		// Note that result will always be the same size as biggest input
		//  (e.g., -127 + 128 will use 2 bytes to store the result value 1)
		return res_li;
	}

	/**
	 * Negate val using two's complement representation
	 * @return negation of this
	 */
	public HeftyInteger negate() {
		byte[] neg = new byte[val.length];
		int offset = 0;

		// Check to ensure we can represent negation in same length
		//  (e.g., -128 can be represented in 8 bits using two's
		//  complement, +128 requires 9)
		if (val[0] == (byte) 0x80) { // 0x80 is 10000000
			boolean needs_ex = true;
			for (int i = 1; i < val.length; i++) {
				if (val[i] != (byte) 0) {
					needs_ex = false;
					break;
				}
			}
			// if first byte is 0x80 and all others are 0, must extend
			if (needs_ex) {
				neg = new byte[val.length + 1];
				neg[0] = (byte) 0;
				offset = 1;
			}
		}

		// flip all bits
		for (int i  = 0; i < val.length; i++) {
			neg[i + offset] = (byte) ~val[i];
		}

		HeftyInteger neg_li = new HeftyInteger(neg);

		// add 1 to complete two's complement negation
		return neg_li.add(new HeftyInteger(ONE));
	}

	/**
	 * Implement subtraction as simply negation and addition
	 * @param other HeftyInteger to subtract from this
	 * @return difference of this and other
	 */
	public HeftyInteger subtract(HeftyInteger other) {
		return this.add(other.negate());
	}

	/**
	 * Compute the product of this and other
	 * @param other HeftyInteger to multiply by this
	 * @return product of this and other
	 */
	public HeftyInteger multiply(HeftyInteger other) 
	{
		// YOUR CODE HERE (replace the return, too...)
		int lengthX = this.length();
		int lengthY = other.length();
		HeftyInteger tempX = new HeftyInteger(this.val);	// this is simply so that I can use the negate function properly

		boolean positive = true;

		byte[] sample = new byte[1];
		HeftyInteger result = new HeftyInteger(sample);
		HeftyInteger digit;

		if(tempX.isNegative() ^ other.isNegative())	// determining the sign to apply at the end of the multiplication
		{
			positive = false;
		}

		//  make the numbers positive if they're negative
		if(tempX.isNegative())	tempX = tempX.negate();	
		if(other.isNegative())	other = other.negate();

		for(int x = lengthX - 1; x >= 0; x--)	// starting at the LSB and looping to the MSB
		{
			digit = new HeftyInteger(sample);	// current digit we are multiplying
			for(int y = lengthY - 1; y >= 0; y--)
			{
				int num = ((int)tempX.val[x] & 0xff) * ((int)other.val[y] & 0xff);	// convert bytes to ints and multiply them

				// transfer this new int into a byte array and then HeftyInteger
				byte[] base = new byte[2];	
				base[0] = (byte)((num >>> 8) & 0xff);
				base[1] = (byte)(num & 0xff);
				HeftyInteger hefty = new HeftyInteger(base);
				
				// shift it into its final place in the current digit
				int shifty = lengthY - 1 - y;
				hefty = hefty.shift(shifty);

				if(hefty.isNegative())	// if it's negative, zero-extended it
				{
					hefty.extend((byte) 0);
				}

				digit = digit.add(hefty);	// add it to the current digit
			}

			// shift the current digit into it's final place in the product
			int shiftx = lengthX - 1 - x;
			digit = digit.shift(shiftx);
			result = result.add(digit);
		}

		if(!positive)	// if it's supposed to be negative, negate it
		{
			result = result.negate();
		}
		else if(positive && result.isNegative())	// if it's supposed to be positive but it isn't, zero-extend it
		{
			result.extend((byte) 0);
		}

		return result;
	}

	private	HeftyInteger shift(int n)	// simple method to shift a HeftyInteger by n BYTES
	{
		byte[] temp = new byte[n+this.length()];
		for(int i = 0; i < temp.length; i++)
		{
			if(i < this.length())
			{
				temp[i] = this.val[i];
			}
			else
			{
				temp[i] = (byte) 0;
			}
		}
		HeftyInteger shifted = new HeftyInteger(temp);
		
		return shifted;
	}

	/**
	 * Run the extended Euclidean algorithm on this and other
	 * @param other another HeftyInteger
	 * @return an array structured as follows:
	 *   0:  the GCD of this and other
	 *   1:  a valid x value
	 *   2:  a valid y value
	 * such that this * x + other * y == GCD in index 0
	 */
	public HeftyInteger[] XGCD(HeftyInteger other) 
	{
		// YOUR CODE HERE (replace the return, too...)
		HeftyInteger[] result = new HeftyInteger[3];

		HeftyInteger a = new HeftyInteger(this.getVal());
		HeftyInteger b = new HeftyInteger(other.getVal());

		if(a.isZero() || b.isZero())	// if either of them are 0, return zeros
		{
			result[0] = new HeftyInteger(new byte[1]);
			result[1] = new HeftyInteger(new byte[1]);
			result[2] = new HeftyInteger(new byte[1]);
			return result;
		}

		if(a.equals(b))		// if they're the same number, give the correct output
		{
			result[0] = a;
			result[1] = new HeftyInteger(new byte[1]);
			result[2] = new HeftyInteger(ONE);
			return result;
		}

		HeftyInteger one = new HeftyInteger(ONE);

		if(a.equals(one) || b.equals(one))	// if one of them is 1, set the values correctly
		{
			System.out.println("here");
			result[0] = new HeftyInteger(ONE);

			if(a.equals(one))	
			{
				result[1] = new HeftyInteger(ONE);
				result[2] = new HeftyInteger(new byte[1]);
			}
			else
			{
				result[1] = new HeftyInteger(new byte[1]);
				result[2] = new HeftyInteger(ONE);
			}
			
			return result;
		}

		if(this.compareTo(other) < 0)	// if this is bigger than other, swap them
		{
			a = new HeftyInteger(other.getVal());
			b = new HeftyInteger(this.getVal());
		}

		HeftyInteger[] div = a.divide(b);		
		HeftyInteger[] quotients = new HeftyInteger[a.length()*8];

		HeftyInteger quo = div[0];
		HeftyInteger rem = div[1];
		quotients[0] = quo;

		HeftyInteger oneHeft = new HeftyInteger(ONE);

		byte[] zero = {(byte) 0};
		
		int count = 1;

		// simple GCD loop
		while(rem.compareTo(oneHeft) > 0) // while the remainder is greater than 0
		{
			a = b;
			b = rem;

			div = a.divide(b);
			quo = div[0];
			rem = div[1];

			quotients[count] = quo;
			result[0] = b;	// the last value of b is the GCD
			count++;
		}

		HeftyInteger x = new HeftyInteger(ONE);
		HeftyInteger y = new HeftyInteger(zero);

		System.out.println();
		count--;
		while(count >= 0)	// building the Bezout coefficients back up
		{
			HeftyInteger temp = new HeftyInteger(x.getVal());
			x = y;
			y = temp.subtract(quotients[count].multiply(x));

			count--;
		}

		result[1] = x;
		result[2] = y;

		return result;
	}

	public HeftyInteger[] divide(HeftyInteger divisor)
	{
		HeftyInteger dividend = new HeftyInteger(this.getVal());
		HeftyInteger quotient = new HeftyInteger(new byte[1]);
		HeftyInteger remainder = new HeftyInteger(new byte[1]);
		HeftyInteger[] result = new HeftyInteger[2];
		
		HeftyInteger temp = new HeftyInteger(new byte[1]);

		if(this.compareTo(divisor) < 0)	// if you try to divide a number by something larger than it
		{
			// return 0 as the quotient and the dividend as the remainder
			result[0] = quotient;
			result[1] = dividend;
	
			return result;
		}

		for(int i = 0; i < dividend.length(); i++)	// from the MSB to the LSB of the dividend
		{
			byte[] num = {dividend.val[i]};	// the current byte of the dividend

			HeftyInteger pos = new HeftyInteger(num);	
			if(pos.isNegative())	pos.extend((byte) 0);	// if the current byte of the dividend is negateive, zero-extend it

			temp = temp.add(pos);	// add the current byte to this temp dividend
			
			byte[] zero = {(byte) 0};	
			byte[] one = {(byte) 1};
			HeftyInteger fact = new HeftyInteger(zero);

			// finding the number that multiplies with the divisor but it less than the dividend
			while(temp.compareTo(divisor.multiply(fact)) > 0)	
			{
				fact = fact.add(new HeftyInteger(one));
			}
			fact = fact.subtract(new HeftyInteger(one));	// it goes one too far so subtract 1

			quotient = quotient.add(fact);	// add the quotient

			temp = temp.subtract(divisor.multiply(fact));	// subtract the biggest multiplier from the temp dividend

			if(i == dividend.length()-1)	// if it's at the last byte of the dividend, set the remainder
			{
				remainder = temp;
				continue;	// skip the last code of the loop
			}

			// shift the quotient and temp dividend 
			quotient = quotient.shift(1);
			temp = temp.shift(1);
		}

		// gotta trim both numbers before returning
		quotient = trim(quotient);
		remainder = trim(remainder);

		// add quotient and remainder to result and return it
		result[0] = quotient;
		result[1] = remainder;

		return result;
	}

	public int compareTo(HeftyInteger other)
	{
		if(this.subtract(other).isNegative())	// subtract this number from the other number; if the answer is negative then this is less than other
		{
			return -1;	// return -1 to signify less than
		}

		return 1;	// return 1 to signify greater than
	}

	private HeftyInteger trim(HeftyInteger num)
	{
		int count = 0;	// count of the number of bytes that are being used for padding

		// find if the padding is 0's or 1's
		byte leading = (byte) 0x00;
		if(this.isNegative())
		{
			leading = (byte) 0xFF;
		}

		// find how many bytes it is currently padded by
		for(int i = 0; i < num.length(); i++)
		{
			if(num.val[i] == leading)	// if this byte is just padding
			{
				count++;
			}
			else	// break when it reaches non-padded bytes
			{
				break;
			}
		}

		count--;	// reduce the padding size, one byte of padding needs to remain
		
		if(count > 1)	// if there even is any padding
		{
			byte[] trimmed = new byte[num.length()-count];	// new byte array with size after padding is removed
			HeftyInteger temp = new HeftyInteger(trimmed);
			
			for(int i = 0; i < temp.length(); i++)
			{
				temp.val[i] = num.val[i+count];	// add only the non-padded bytes to the new array/HeftyInteger
			}
			return temp;
		}

		return num;	// return the original number if there was no padding
	}
}
