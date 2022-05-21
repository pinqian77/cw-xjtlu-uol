/*
 * Student ID: 201600772
 * Student Name: Pin Qian
 * Email: sgpqian@liverpool.ac.uk
 *
 * User: sgpqian
 *
 * Problem ID: 1090
 * RunID: 53692
 * Result: Accepted
 */

#include <string.h>
#include <stdio.h>
#include <stdlib.h>

/* Define constants */
#define MAX_LINE 100

/* wordNode_t contains a pointer to a word
   and an int to record word count */
typedef struct
{
    char *word;
    size_t count;
} wordNode_t;

/* dict_t contains an pointer to wordNode_t type pointers
   and an integer to record total unique word count */
typedef struct
{
    wordNode_t **dictionary;
    size_t count;
} dict_t;

/* Function prototypes */
void checkIfMemoryIsAllocated(void *ptr);
void preprocessWord(char *pWord);
int searchWord(char *pWord, dict_t *dict);
void createWordNode(char *pWord, dict_t *dict);
void sortWord(dict_t *dict);
void swapWord(wordNode_t *wordA, wordNode_t *wordB);

int main()
{
    /* Initialize a dict to store pointers*/
    dict_t dict;
    dict.count = 0;
    dict.dictionary = malloc(sizeof(wordNode_t *));
    checkIfMemoryIsAllocated(dict.dictionary);

    /* Read in line by line */
    char line[MAX_LINE];
    char *pWord;
    while (fgets(line, MAX_LINE, stdin))
    {
        /* Removes '\n' due to fgets() */
        line[strlen(line) - 1] = '\0';

        /* Get word one by one*/
        const char *delim = " ,.;:!?";
        pWord = strtok(line, delim);
        while (pWord != NULL)
        {
            preprocessWord(pWord);

            /* Check if we have met the word before */
            if (searchWord(pWord, &dict) == 1)
            {
                createWordNode(pWord, &dict);
            }

            /* Reset to get next word */
            pWord = strtok(NULL, delim);
        }
    }

    /* Sort words alphabetically */
    sortWord(&dict);

    /* Print result and free memory */
    for (size_t i = 0; i < dict.count; i++)
    {
        printf("%s => %d\n", dict.dictionary[i]->word, dict.dictionary[i]->count);
        free(dict.dictionary[i]->word);
        free(dict.dictionary[i]);
    }
    free(dict.dictionary);
}

/* To check if memory is allocated successfully */
void checkIfMemoryIsAllocated(void *ptr)
{
    if (ptr == NULL)
    {
        printf("out of memory!\n");
        exit(1);
    }
}

/* Convert uppercase to lowercase and remove non-alpha characters*/
void preprocessWord(char *pWord)
{
    size_t i, j = 0;
    for (i = 0; i < strlen(pWord); i++)
    {
        /* Convert uppercase to lowercase */
        if (pWord[i] >= 'A' && pWord[i] <= 'Z')
        {
            pWord[i] = pWord[i] + 32;
        }
        /* Only allow lowercase to be stored */
        if (pWord[i] >= 'a' && pWord[i] <= 'z')
        {
            pWord[j] = pWord[i];
            j++;
        }
    }
    /* End of a word */
    pWord[j] = '\0';
}

/* if the word exists, increase its count and return 0;
   else create a new node and return 1 */
int searchWord(char *pWord, dict_t *dict)
{
    for (size_t i = 0; i < dict->count; i++)
    {
        if (strcmp(dict->dictionary[i]->word, pWord) == 0)
        {
            dict->dictionary[i]->count++;
            return 0;
        }
    }
    return 1;
}

/* Create a new node if the word is new */
void createWordNode(char *pWord, dict_t *dict)
{
    /* Allocate memory */
    dict->dictionary = realloc(dict->dictionary, (dict->count + 1) * sizeof(wordNode_t *));
    checkIfMemoryIsAllocated(dict->dictionary);

    wordNode_t *node = malloc(sizeof(pWord) + sizeof(size_t));
    checkIfMemoryIsAllocated(node);

    node->word = malloc(strlen(pWord) + 1);
    checkIfMemoryIsAllocated(node->word);

    /* Move value to the memory we allocated */
    strcpy(node->word, pWord);
    node->count = 1;
    dict->dictionary[dict->count] = node;

    /* Increase count of unique words */
    dict->count++;
}

/* Sort words by bubble sort */
void sortWord(dict_t *dict)
{
    for (size_t i = 0; i < dict->count; i++)
    {
        for (size_t j = i + 1; j < dict->count; j++)
        {
            if (strcmp(dict->dictionary[i]->word, dict->dictionary[j]->word) >= 0)
            {
                swapWord(dict->dictionary[i], dict->dictionary[j]);
            }
        }
    }
}

/* A help function to assist sortWord() swapping value of nodes */
void swapWord(wordNode_t *wordA, wordNode_t *wordB)
{
    wordNode_t tmp;
    tmp.word = wordA->word;
    tmp.count = wordA->count;

    wordA->word = wordB->word;
    wordA->count = wordB->count;

    wordB->word = tmp.word;
    wordB->count = tmp.count;
}