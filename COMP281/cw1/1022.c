/*
 * Student ID: 201600772
 * Student Name: Pin Qian
 * Email: sgpqian@liverpool.ac.uk
 *
 * User: sgpqian
 *
 * Problem ID: 1022
 * RunID: 41275
 * Result: Accepted
 */

#include <stdio.h>

/* Function prototypes */
void checkType(int next_char);

/* Declare global variables */
int num_english_chars = 0;
int num_digits = 0;
int num_spaces = 0;
int num_other_chars = 0;

int main()
{
    /* Read in and check type until the end of the file */
    char next_char;
    while (scanf("%c", &next_char) != EOF)
    {
        checkType(next_char);
    }
    /* Output the result */
    printf("%d %d %d %d\n", num_english_chars, num_digits, num_spaces, num_other_chars);
    return 0;
}

/* Given a char, check its type */
void checkType(int next_char)
{
    /* check if it is english character */
    if ((next_char >= 'a' && next_char <= 'z') || (next_char >= 'A' && next_char <= 'Z'))
    {
        num_english_chars++;
    }
    /* Check if it is digit */
    else if (next_char >= '0' && next_char <= '9')
    {
        num_digits++;
    }
    /* Check if it is space */
    else if (next_char == ' ')
    {
        num_spaces++;
    }
    /* Check if it is other character*/
    else
    {
        num_other_chars++;
    }
}