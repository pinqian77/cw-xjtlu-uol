/*
 * Student ID: 201600772
 * Student Name: Pin Qian
 * Email: sgpqian@liverpool.ac.uk
 *
 * User: sgpqian
 *
 * Problem ID: 1030
 * RunID: 41277
 * Result: Accepted
 */

#include <stdio.h>

/* Function prototypes */
int findNthDigit(long numerator, long denominator, long n);

int main()
{
    /* Declare variables and get inputs*/
    long numerator, denominator, n;
    scanf("%d %d %d", &numerator, &denominator, &n);

    /* Get nth digit and output the result */
    int result = findNthDigit(numerator, denominator, n);
    printf("%d", result);

    return 0;
}

/* To find the nth digit */
int findNthDigit(long numerator, long denominator, long n)
{
    int result;
    /* While n > 0, divide to get the nth digit,
       and move on to the next digit. */
    while (n > 0)
    {
        numerator *= 10;
        result = numerator / denominator;
        numerator %= denominator;
        n--;
    }
    return result;
}