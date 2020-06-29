/**
 * @author Πλέσσιας Αλέξανδρος (ΑΜ.:2025201100068).
 */
package IR_project1;

import Files.Question1And4_1;
import Files.Question2And4_2;
import Files.Question3And4_3;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * Main get input from user create dictionary, all users dictionaries, 
 * calculate idfs and execute all the questions of exercise.
 * Also in comments exist helpful functions.
 */
public class Main {

    public static void main(String[] args) {

        String folderName = "papers";
        HashMap<String, TreeMap<String, ArrayList<Float>>> allMembersDictionary;

        // All DEP memebers dictionaries in one HashMap.
        AllMembersDictionaries AllMembersDictionariesObject = new AllMembersDictionaries(folderName);
        allMembersDictionary = AllMembersDictionariesObject.getAllDEPmembesrDictionary(); // Initialize.

 
        Dictionary dictionaryObject = new Dictionary(allMembersDictionary);
        TreeMap<String, ArrayList<String>> dictionary = dictionaryObject.getDictionary();           // Inverted index. terms->{DEPmember name,DEPmember name, ... ,DEPmember name } .
        //dictionaryObject.printDictionary();
        TreeMap<String, Float> allTermsIDF = dictionaryObject.getAllTermsIDF();         // Create idfs array for futere use.
        //dictionaryObject.printIDFs();
        

        //Create VSMs for each DEP memeber.
        CreateVSMs AllMembersVSMs = new CreateVSMs(allMembersDictionary, dictionary);
        allMembersDictionary = AllMembersVSMs.getVSMs(); // Overwrite.
        //AllMembersVSMs.printVSMs();

        
        //Calculate all tf and tf-idf. 
        CalcUsefullMetrics AllMembersUsefullMetricsObject = new CalcUsefullMetrics(allMembersDictionary, allTermsIDF);
        allMembersDictionary = AllMembersUsefullMetricsObject.getUsefullMetrics(); //Overwrite aqain.

        
        // Create Results directory..
        File resultsFile = new File("Results");
        resultsFile.mkdirs();

        
        // Get input from user.
        String userQuery = "";
        int N;
        int M;
        try (Scanner scan = new Scanner(System.in)) {
            System.out.println("Please, give a query:");
            while (scan.hasNextLine()) {
                String temp = scan.nextLine();
                userQuery = userQuery + temp;
                if (!scan.equals("\\n")) {
                    break;
                }
            }

            System.out.println("Please, give N value:");
            N = scan.nextInt(); // The fist N-impotant elements.

            System.out.println("Please give M value (from 1-25):");
            M = scan.nextInt(); // The first M-impotant colleagues for each member.

            scan.close();
        }
         System.out.println();
        
         
        // Question 1 & Question 4_1.
        Question1And4_1 question1And4_1 = new Question1And4_1(N, resultsFile, allMembersDictionary);
        question1And4_1.doQuenstion1();
        question1And4_1.doQuenstion4_1();

        // Question 2 & Question 4_2.
        Question2And4_2 question2And4_2 = new Question2And4_2(resultsFile, userQuery, dictionary, allTermsIDF, allMembersDictionary);
        question2And4_2.doQuenstion2();
        question2And4_2.doQuenstion4_2();
       
        // Question 3 & Question 4_3.
        Question3And4_3 question3And4_3 = new Question3And4_3(M, resultsFile, allMembersDictionary);
        System.out.println("Now wait for ~18 seconds(it depends on the system) for Quenstion3... ");
        question3And4_3.doQuestion3();
        //question3And4_3.saveArrayOfQuestion3();
        System.out.println("Now wait for another ~20 seconds(it depends on the system) for Quenstion4_3... ");
        question3And4_3.doQuestion4_3();
        //question3And4_3.saveArrayOfQuestion4_3(); 
        
        System.out.println();
        System.out.println("See the results in Project's folder Results.");
        System.out.println("Goodbye!!! Have a nice day.");
        System.exit(0);

        // Write the Report .
    }
}
