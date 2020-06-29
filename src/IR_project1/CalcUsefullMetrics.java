/**
 * @author Πλέσσιας Αλέξανδρος (ΑΜ.:2025201100068).
 */
package IR_project1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


/**
 * AllMembersUsefullMetrics calculate tf & idf.
 * Extend  ArrayList of each member with tf & idf:
 * ArrayList pos0 -> FREQ.
 * ArrayList pos1 -> TF.
 * ArrayList pos2 -> TFIDF.
 */
public class CalcUsefullMetrics {
    
    HashMap<String, TreeMap<String, ArrayList<Float>>> allMembersDictionary;


    public CalcUsefullMetrics(HashMap<String, TreeMap<String, ArrayList<Float>>> allMembersDictionary, TreeMap<String, Float> allTermsIDF) {
        
        this.allMembersDictionary = allMembersDictionary;
        
        for (Map.Entry<String, TreeMap<String, ArrayList<Float>>> memberDictionery : this.allMembersDictionary.entrySet()) {
            TreeMap<String, ArrayList<Float>> allTermsOfMember = memberDictionery.getValue();
            for (Map.Entry<String, ArrayList<Float>> oneTermOfDictionary : allTermsOfMember.entrySet()) {

                String term = oneTermOfDictionary.getKey();

                // 0. FREQ = documentToFrequency.getValue().get(0)
                
                // 1. Calc tf.
                float tf = 0;
                if (oneTermOfDictionary.getValue().get(0) > 0) {
                    tf = (float) (1 + Math.log10(oneTermOfDictionary.getValue().get(0)));
                    oneTermOfDictionary.getValue().add(tf);
                } else {
                    oneTermOfDictionary.getValue().add((float) 0);
                }

                // 2. IDF exist in ArrayList allTermsIDF.
                
                // 3.Calc TF-IDF.
                TreeMap<String, Float> aldlTermsIDF = new TreeMap<>();
                aldlTermsIDF.put(term, tf);
                Float tfidf = (float) (tf * allTermsIDF.get(term));
                oneTermOfDictionary.getValue().add(tfidf);
            }
        }
        

        
    }

    // Getter
    public HashMap<String, TreeMap<String, ArrayList<Float>>> getUsefullMetrics() {
        return allMembersDictionary;
    }
    
    // Print UsefullMetrics in a nice format.
    public void printUsefullMetrics() {
        // Print all VSMs.
        for (Map.Entry<String, TreeMap<String, ArrayList<Float>>> memberDictionery : this.allMembersDictionary.entrySet()) {
            String DEPmember = memberDictionery.getKey();
            TreeMap<String, ArrayList<Float>> documentToWordCount = memberDictionery.getValue();
            System.out.println();
            System.out.println(DEPmember+" terms: ");
            for (Map.Entry<String, ArrayList<Float>> oneTermOfDictionary : documentToWordCount.entrySet()) {
                String term = oneTermOfDictionary.getKey();
                ArrayList<Float> termMetrics = oneTermOfDictionary.getValue();
                
                System.out.println(term+": (freq="+termMetrics.get(0)+" tf="+termMetrics.get(1)+" tfidf="+termMetrics.get(2)+")");
            }
        }
    
    }
    
    
    
}
