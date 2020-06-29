/**
 * @author Πλέσσιας Αλέξανδρος (ΑΜ.:2025201100068).
 */
package IR_project1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Dictionary store inverted index and calculate all allTermsIDF.
 * Inverted index has the form term-> [depname], [depname] ... ,[depname].
 * For term-idf use type: log(N/nt).
 *
 */
public class Dictionary {

    // I choose TreeMap because a) I want all elements be sorted and b)dONT care for performance(small index) 
    private final TreeMap<String, ArrayList<String>> dictionary;
    private final TreeMap<String, Float> allTermsIDF = new TreeMap<>();

    public Dictionary(HashMap<String, TreeMap<String, ArrayList<Float>>> AllMembersDictionary) {

        this.dictionary = new TreeMap<>();

        // Fill dictionary.
        for (Map.Entry<String, TreeMap<String, ArrayList<Float>>> memberDictionery : AllMembersDictionary.entrySet()) {
            TreeMap<String, ArrayList<Float>> allTermsOfMember = memberDictionery.getValue();
            for (Map.Entry<String, ArrayList<Float>> oneTermOfDictionary : allTermsOfMember.entrySet()) {
                String word = oneTermOfDictionary.getKey();
                //  If term don't exist add to dictionary.
                if (!(this.dictionary.containsKey(word))) {
                    this.dictionary.put(word, new ArrayList<String>());
                }
                // Add the member which have this term.
                ArrayList<String> doc = this.dictionary.get(word);
                doc.add(memberDictionery.getKey());
                this.dictionary.put(word, doc);
            }
        }

        // Create idfs array for futere use.
        for (Map.Entry<String, ArrayList<String>> wordToDocument : this.dictionary.entrySet()) {
            String word = wordToDocument.getKey();
            // Cast one of 2 integers to solve integer division problem otherwise get 0 all time.
            double idf = Math.log10(((double) AllMembersDictionary.size()) / this.dictionary.get(word).size());
            this.allTermsIDF.put(word, (float) idf);
        }
    }

    // Getters.
    public TreeMap<String, ArrayList<String>> getDictionary() {
        return this.dictionary;
    }

    public TreeMap<String, Float> getAllTermsIDF() {
        return this.allTermsIDF;
    }

    // Print dictionary in a nice format.
    public void printDictionary() {
        System.out.println("Dictionary: ");
        for (Map.Entry<String, ArrayList<String>> wordToDocument : dictionary.entrySet()) {
            System.out.println("Term: "+wordToDocument.getKey() + " -> " + wordToDocument.getValue()+".");
        }
    }
    
    // Print idfs in a nice format.
    public void printIDFs() {
        System.out.println("All IDFs: ");
        for (Map.Entry<String, Float> oneTermIDF : allTermsIDF.entrySet()) {
            System.out.println("Term: " + oneTermIDF.getKey() + " have IDF= " + oneTermIDF.getValue()+".");
        }
    }

}
