import javax.xml.crypto.Data;
import java.io.File;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class Main201600772 {

    public static ArrayList<String> ReadData(String pathname) {
        ArrayList<String> strlist = new ArrayList<String>();
        try {

            File filename = new File(pathname);
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filename));
            BufferedReader br = new BufferedReader(reader);
            int j = 0;
            String line = "";
            while ((line = br.readLine()) != null) {
                strlist.add(line);
            }
            return strlist;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strlist;
    }

    public static ArrayList<ArrayList<Integer>> DataWash(ArrayList<String> Datalist) {
        ArrayList<ArrayList<Integer>> AIS = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> IS = new ArrayList<Integer>();
        for (int i = 0; i < Datalist.size(); i++) {
            String Tstr = Datalist.get(i);
            if (Tstr.equals("A") == false) {
                IS.add(Integer.parseInt(Tstr));
            }
            if (Tstr.equals("A")) {
                ArrayList<Integer> elemAIS = new ArrayList<Integer>(IS);
                AIS.add(elemAIS);
                IS.clear();
            }
        }
        return AIS;
    }

    // %%%%%%%%%%%%%%%%%%%%%%% Procedure LongestRange() that will contain your code:

    public static int LongestRange(int[] A, int n) {
        /*
         * Input is array of input sequence (a_1 <= a_2 <= ... <= a_n) as
         * A[0,1,...,n-1], that is,
         * a_1 = A[0], a_2 = A[1], ..., a_n = A[n-1].
         * n = number of integers in sequence A
         * This procedure should return the value of the longest range (>= 1) or 0 if
         * there is no peak.
         * It should NOT return the respective longest range of peaks!
         */

        /*
         * Here should be the description of the main ideas of your solution:
         * describe the recursive relation used in your dynamic programming
         * solution and outline how you implement it sequentially in your code below.
         * 
         * SOME NOTATION: s.t. is abbreviation of "such that"
         * a <= b (a is smaller than or equal to b)
         * a >= b (a is greater than or equal to b)
         * max [ a , b ] denotes the larger among a and b
         * Given an array T[1,...,n]
         * then M = max_{k : some condition C(k) holds} [ T[k] ],
         * M denotes the largest value T[k] over all indices k
         * such that condition C(k) holds.
         * Here we denote by {k : some condition C(k) holds} the set of all
         * elements k which make the condition C(k) hold true.
         * 
         * 
         * Let us first define the dynamic programming (DP) array dp[1,...,n].
         * Define dp[i] to be the length of the longest range of peaks that ends at (and
         * includes) position i.
         * 
         * DP array is first initialised as follows:
         * First, check the length of the given array, if length < 3, return result 0;
         * If length >= 3, then there are two situations:
         * 1. If dp[2] > dp[1] and dp[3], indicting that a peak is formed, then
         * initialise the DP array by filling it with 1.
         * 2. If the first three can not form a peak, then initialise it by filling it
         * with 0 (It's default by JAVA when we declare the array dp[1,...,n]).
         * 
         * DP can be solved recursively as follows:
         * Suppose the given array is T[1,...,n].
         * 
         * If we have known dp[1], dp[2],..., dp[i-1], then we can compute dp[i] by
         * dp[i] = 1 or dp[i] = 1 + max_{ dp[j] : C1(j) && C2(j) && C3(j) }, where
         * C1(j) is j < i - 2, which regulates two peaks must be disjoint;
         * C2(j) is T[j - 1] > T[j] and T[j - 2] < T[j - 1], which regulates the three
         * elements ending at the j position must form a peak;
         * C3(j) is T[i - 2] >= T[j] and T[i - 2] <= T[j - 1], which regulates a valid
         * peak merge operation.
         * 
         * The main ideas of a sequential implementation of the above recursive solution
         * that shows how to fill in the DP array is as follows:
         * Check all T[j] that satisfies above conditions. In this way, if the condition
         * is satisfied, then we can increase the longest range of peaks that ends at
         * T[j] by one more value via adding T[i] to the range of this state. Note that
         * when the three elements ending at the i position form a peak, but there is no
         * T[j] satisfies the condition, we define dp[i] = 1 ;
         */

        /*
         * Here should be the statement and description of the running time
         * analysis of your sequential implementation: describe how the running time
         * depends on n (length of the input sequence), and give a short argument. Use
         * the big-O notation.
         * 
         * To compute dp[i], we need to check all of the values T[1],...,T[i-3], i.e.,
         * O(i) time. Totally, computing all of the dp[i] values takes O(1+2+3+...+n) =
         * O(n^2) time. Then, finding the result by max[dp[1], dp[2],..., dp[n]] takes
         * O(n) time.
         * 
         * Summarising, this argument shows that the worst case running time of this
         * whole implementation is O(n^2).
         */

        /* Here below write your sequential implementation: */

        // Special case
        if (A.length < 3) {
            return 0;
        }
        // Declare dp table
        int[] dp = new int[n];
        if (A[1] > A[0] && A[2] < A[1]) {
            for (int i = 0; i < dp.length; i++) {
                dp[i] = 1;
            }
        }
        // Solved recursively
        int res = 0;
        for (int i = 3; i < n; i++) {
            if (A[i - 1] > A[i] && A[i - 2] < A[i - 1]) {
                dp[i] = 1;
                for (int j = 2; j < i - 2; j++) {
                    if ((A[j - 1] > A[j] && A[j - 2] < A[j - 1]) && (A[i - 2] >= A[j] && A[i - 2] <= A[j - 1])) {
                        dp[i] = Math.max(dp[i], 1 + dp[j]);
                    }
                }
            }
            res = Math.max(res, dp[i]);
        }
        return res;
    } // end of procedure LongestRange()

    public static int Computation(ArrayList<Integer> Instance, int opt) {
        // opt=1 here means option 1 as in -opt1, and opt=2 means option 2 as in -opt2
        int NGounp = 0;
        int size = 0;
        int Correct = 0;
        size = Instance.size();

        int[] A = new int[size - opt];
        // NGounp = Integer.parseInt((String)Instance.get(0));
        NGounp = Instance.get(0); // NOTE: NGounp = 0 always, as it is NOT used for any purpose
                                  // It is just the first "0" in the first line before instance starts.
        for (int i = opt; i < size; i++) {
            A[i - opt] = Instance.get(i);
        }
        int Size = A.length;
        if (NGounp > Size) {
            return (-1);
        } else {
            // Size
            int R = LongestRange(A, Size);
            return (R);
        }
    }

    public static String Test;

    public static void main(String[] args) {
        if (args.length == 0) {
            String msg = "Rerun with flag: " +
                    "\n\t -opt1 to get input from dataOne.txt " +
                    "\n\t -opt2 to check results in dataTwo.txt";
            System.out.println(msg);
            return;
        }
        Test = args[0];
        int opt = 2;
        String pathname = "dataTwo.txt";
        if (Test.equals("-opt1")) {
            opt = 1;
            pathname = "dataOne.txt";
        }

        ArrayList<String> Datalist = new ArrayList<String>();
        Datalist = ReadData(pathname);
        ArrayList<ArrayList<Integer>> AIS = DataWash(Datalist);

        int Nins = AIS.size();
        int NGounp = 0;
        int size = 0;
        if (Test.equals("-opt1")) {
            for (int t = 0; t < Nins; t++) {
                int Correct = 0;
                int Result = 0;
                ArrayList<Integer> Instance = AIS.get(t);
                Result = Computation(Instance, opt);
                System.out.println(Result);
            }
        } else {
            int wrong_no = 0;
            int Correct = 0;
            int Result = 0;
            ArrayList<Integer> Wrong = new ArrayList<Integer>();
            for (int t = 0; t < Nins; t++) {
                ArrayList<Integer> Instance = AIS.get(t);
                Result = Computation(Instance, opt);
                System.out.println(Result);
                Correct = Instance.get(1);
                if (Correct != Result) {
                    Wrong.add(t + 1);
                    wrong_no = wrong_no + 1;
                }
            }
            if (Wrong.size() > 0) {
                System.out.println("Index of wrong instance(s):");
            }
            for (int j = 0; j < Wrong.size(); j++) {
                System.out.print(Wrong.get(j));
                System.out.print(",");

                /*
                 * ArrayList Instance = (ArrayList)Wrong.get(j);
                 * for (int k = 0; k < Instance.size(); k++){
                 * System.out.println(Instance.get(k));
                 * }
                 */
            }
            System.out.println("");
            System.out.println("Percentage of correct answers:");
            System.out.println(((double) (Nins - wrong_no) / (double) Nins) * 100);

        }

    }
}
