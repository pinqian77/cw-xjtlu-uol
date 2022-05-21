/*
 * Student ID: 201600772
 * Student Name: Pin Qian
 * Email: sgpqian@liverpool.ac.uk
 *
 * User: sgpqian
 *
 * Problem ID: 1014
 * RunID: 41256
 * Result: Accepted
 */

#include <stdio.h>

/* Define constants */
#define PI 3.14

/* Function prototypes */
float computeArea(int radius);
float computeCircumference(int radius);

int main()
{
    /* Declare variables */
    float sum_area, sum_circumference = 0.;
    int radius_inner, radius_outer;

    /* Get inputs*/
    scanf("%d %d", &radius_inner, &radius_outer);

    /* Sum up areas and circumferences */
    while (radius_inner <= radius_outer)
    {
        sum_area += computeArea(radius_inner);
        sum_circumference += computeCircumference(radius_inner);
        radius_inner++;
    }

    /* Output the result in 3 digits precision */
    printf("%.3f\n", sum_area);
    printf("%.3f\n", sum_circumference);

    return 0;
}

/* Compute area of a circle */
float computeArea(int radius)
{
    return PI * radius * radius;
}

/* Compute circumference of a circle */
float computeCircumference(int radius)
{
    return 2 * PI * radius;
}
