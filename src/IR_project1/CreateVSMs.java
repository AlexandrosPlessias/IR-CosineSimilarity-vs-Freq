/**
 * @author Πλέσσιας Αλέξανδρος (ΑΜ.:2025201100068).
 */
package IR_project1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * CreateVSMs for members and query.
 * The Vector Space Model has the form: <term1<freq>>,<term2<freq>>, ... <termn<freq>> 
 * if term don't exist in member dictionary added to VSM with freq=0.
 */
public class CreateVSMs {

    HashMap<String, TreeMap<String, ArrayList<Float>>> AllMembersDictionary;

    public CreateVSMs(HashMap<String, TreeMap<String, ArrayList<Float>>> AllMembersDictionary, TreeMap<String, ArrayList<String>> dictionary) {

        this.AllMembersDictionary=AllMembersDictionary;
        
        // Extend member dictionary.
        for (Map.Entry<String, TreeMap<String, ArrayList<Float>>> memberDictionery : this.AllMembersDictionary.entrySet()) {
            TreeMap<String, ArrayList<Float>> allTermsOfMember = memberDictionery.getValue();
            for (Map.Entry<String, ArrayList<String>> oneTermOfDictionary : dictionary.entrySet()) {
                String term = oneTermOfDictionary.getKey();
                // If term don't exist add to vector with frequency 0.
                if (!(allTermsOfMember.containsKey(term))) {
                    ArrayList<Float> termFrequency = new ArrayList<>();
                    termFrequency.add((float) 0);
                    allTermsOfMember.put(term, termFrequency);
                }
            }
        }

    }

    // Getter.
    public HashMap<String, TreeMap<String, ArrayList<Float>>> getVSMs() {
        return AllMembersDictionary;
    }
    

    // Print VSMs in a nice format.
    public void printVSMs() {
        //Print all VSMs.
        for (Map.Entry<String, TreeMap<String, ArrayList<Float>>> memberDictionery : this.AllMembersDictionary.entrySet()) {
            String DEPmember = memberDictionery.getKey();
            TreeMap<String, ArrayList<Float>> documentToWordCount = memberDictionery.getValue();
            System.out.println();
            System.out.println(DEPmember + ": ");
            for (Map.Entry<String, ArrayList<Float>> oneTermOfDictionary : documentToWordCount.entrySet()) {
                String term = oneTermOfDictionary.getKey();
                Float termFrequency = oneTermOfDictionary.getValue().get(0);
                System.out.print(" (" + term + "," + termFrequency + ")");
            }
        }

    }

}
