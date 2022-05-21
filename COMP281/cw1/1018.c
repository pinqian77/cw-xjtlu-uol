/*
 * Student ID: 201600772
 * Student Name: Pin Qian
 * Email: sgpqian@liverpool.ac.uk
 *
 * User: sgpqian
 *
 * Problem ID: 1018
 * RunID: 41255
 * Result: Accepted
 */
#include <stdio.h>

/* Define constants */
#define MAX_LENGTH 100

/* Function prototypes */
void reverse();

/* Declare global variables */
int count = MAX_LENGTH;

int main()
{
    reverse();
    return 0;
}

/* Reverse a string without using array by recursion */
void reverse()
{
    char next_char;
    /* Call reverse() until reach the end of file or maximum length */
    if (scanf("%c", &next_char) != EOF && count > 0)
    {
        count--;
        reverse();
        printf("%c", next_char);
    }
}