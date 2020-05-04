/*
Part A - Mystery Caches
*/

#include <stdlib.h>
#include <stdio.h>

#include "support/mystery-cache.h"

/*
 * NOTE: When using access_cache() you do not need to provide a "real" memory
 * addresses. You can use any convenient integer value as a memory address,
 * you should not be able to cause a segmentation fault by providing a memory
 * address out of your programs address space as the argument to access_cache.
 */


/*
   Returns the size (in B) of each block in the cache.
*/
int get_block_size(void)
{
	/* YOUR CODE GOES HERE */
	int i = 0;

	while(1<2)
	{
		if(access_cache(i) == FALSE && i != 0)
		{
			return i;
		}
		i++;
	}

	return -1;
}



/*
   Returns the size (in B) of the cache.
*/
int get_cache_size(int block_size)
{
	/* YOUR CODE GOES HERE */
	int i;
	int x = 0;

	while(1<2)
        {
		access_cache(x);
		for(i = 0; i < x; i++)
		{
                	if(access_cache(i) == FALSE)
                	{
                        	return x;
                	}
                }
		x += block_size;
        }

		
	return -1;
}



/*
   Returns the associativity of the cache.
*/
int get_cache_assoc(int cache_size) 
{
	/* YOUR CODE GOES HERE */
	int i;
        int x = 0;
	//double answer;

	if(cache_size == 1)
	{
		return 1;
	}

        while(1<2)
        {
                access_cache(x);
                for(i = 0; i < x; i++)
                {
                        if(access_cache(i) == FALSE)
                        {
				if(i == 0)
				{
					return 1;
				}
				return cache_size/i;					
			}
                }
                x += cache_size;
        }

	return -1;
}



int main(void) {
  int size;
  int assoc;
  int block_size;

  cache_init(0, 0);

  block_size = get_block_size();
  size = get_cache_size(block_size);
  assoc = get_cache_assoc(size);

  printf("Cache block size: %d bytes\n", block_size);
  printf("Cache size: %d bytes\n", size);
  printf("Cache associativity: %d\n", assoc);

  return EXIT_SUCCESS;
}
