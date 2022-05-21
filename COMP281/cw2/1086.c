/*
 * Student ID: 201600772
 * Student Name: Pin Qian
 * Email: sgpqian@liverpool.ac.uk
 *
 * User: sgpqian
 *
 * Problem ID: 1086
 * RunID: 51081
 * Result: Accepted
 */

#include <stdio.h>

/* Function prototypes */
void compress();
void expand();

int main()
{
    /* Read in first character to decide mode */
    char mode;
    scanf("%c", &mode);
    if (mode == 'C')
    {
        scanf("%c", &mode);
        compress();
    }
    if (mode == 'E')
    {
        scanf("%c", &mode);
        expand();
    }
    return 0;
}

/* Function to compress using RLE */
void compress()
{
    char slow = '\0', fast; /* Record the two adjacent characters respectively */
    int count = 1;          /* Record the occurance number of the value of slow */

    /* Read in one by one until EOF */
    while (scanf("%c", &fast) != EOF)
    {
        /* Increase count if same */
        if (fast == slow)
        {
            count++;
        }
        /* Case 0 slow is '\0': update slow */
        else if (slow == '\0')
        {
            slow = fast;
            continue;
        }
        /* Case 1 single: output result */
        else if (count == 1)
        {
            printf("%c", slow);
        }
        /* Case 2 more than one: output and reset count */
        else
        {
            printf("%c%c%d*", slow, slow, count);
            count = 1;
        }
        slow = fast;
    }
    /* After fast reaches EOF */
    if (slow != '\0')
    {
        printf("%c", slow);
    }
}

/* Function to expand RLE */
void expand()
{
    char slow = '\0', fast; /* Record the two adjacent characters respectively */
    int count = 1;          /* Record the occurance number of the value of slow */
    int num = 0;            /* Record the number of same characters */

    /* Read in one by one until EOF */
    while (scanf("%c", &fast) != EOF)
    {
        /* Increase count if same and count equals to 1 */
        if (fast == slow && count == 1)
        {
            count++;
        }
        /* Case 0 slow is '\0': update slow */
        else if (slow == '\0')
        {
            slow = fast;
            continue;
        }
        /* Case 1 single: output result */
        else if (count == 1)
        {
            printf("%c", slow);
        }
        /* Case 2 more than one the same*/
        else
        {
            /* Identify quantity of same characters */
            num = num * 10 + ((int)fast - 48);
            while (scanf("%c", &fast) != EOF)
            {
                if (fast == '*')
                    break;
                num = num * 10 + ((int)fast - 48);
            }

            /* Print all same characters */
            while (num > 0)
            {
                printf("%c", slow);
                num--;
            }
            count = 1;   
            slow = '\0'; /* Reset slow to '\0' to avoid '***' */
            continue;
        }
        slow = fast;
    }
    // After fast reaches EOF
    if (slow != '\0')
    {
        printf("%c", slow);
    }
}