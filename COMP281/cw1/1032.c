/*
 * Student ID: 201600772
 * Student Name: Pin Qian
 * Email: sgpqian@liverpool.ac.uk
 *
 * User: sgpqian
 *
 * Problem ID: 1032
 * RunID: 41289
 * Result: Accepted
 */

#include <stdio.h>

/* Define constants */
#define MAX_LENGTH 50

/* Function prototypes */
int getLength(const char *str);
void getPrefixTable(int *prefix_table, const char *pattern, int pattern_length);
int searchPattern(const char *text, const char *pattern);

int main()
{
    /* Get inputs */
    char text[MAX_LENGTH], pattern1[MAX_LENGTH], pattern2[MAX_LENGTH];
    gets(text);
    gets(pattern1);
    gets(pattern2);

    /* Call function to get counts */
    int count1 = searchPattern(text, pattern1);
    int count2 = searchPattern(text, pattern2);

    /* Output the result */
    printf("%d %d\n", count1, count2);

    return 0;
}

/* Get length of a string */
int getLength(const char *str)
{
    int i = 0;
    while (str[i] != '\0')
    {
        i++;
    }
    return i;
}

/* Get prefix table */
void getPrefixTable(int *prefix_table, const char *pattern, int pattern_length)
{
    /* Initialize the first position with 0 */
    prefix_table[0] = 0;
    /* Go through the pattern to update prefix table */
    for (int i = 1, j = 0; i < pattern_length; i++)
    {
        /* when not match, j is to find the previous match position, i is increased  */
        while (j > 0 && pattern[i] != pattern[j])
        {
            j = prefix_table[j - 1];
        }
        /* when match, increase j and i both */
        if (pattern[i] == pattern[j])
        {
            j++;
        }
        /* Fill i position with value j */
        prefix_table[i] = j;
    }
}

/* Search pattern in a text string*/
int searchPattern(const char *text, const char *pattern)
{
    int text_length = getLength(text);
    int pattern_length = getLength(pattern);

    /* Get prefix table */
    int prefix_table[pattern_length];
    getPrefixTable(prefix_table, pattern, pattern_length);

    /* Go through the text to count occurance of the pattern */
    int count = 0;
    for (int i = 0, j = 0; i < text_length; i++)
    {
        /* when not match, j is to find the previous match position, i is increased */
        while (j > 0 && text[i] != pattern[j])
        {
            j = prefix_table[j - 1];
        }
        /* when match, increase j and i both */
        if (text[i] == pattern[j])
        {
            j++;
        }
        /* when pattern is find in text, increase count, increase i */
        if (j == pattern_length)
        {
            count++;
        }
    }
    return count;
}