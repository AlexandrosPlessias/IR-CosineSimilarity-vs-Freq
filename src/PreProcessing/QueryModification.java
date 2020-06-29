/**
 * @author Πλέσσιας Αλέξανδρος (ΑΜ.:2025201100068).
 */
package PreProcessing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * QueryModification is a variation of class MemberDictionary.
 * Store Query in form: TreeMap<term, ArrayList with frequency>.
 * First dismiss special characters, white-spaces and stop-words.
 * After create tokens and implement stemming and delete numbers(-nd-th-rd-d)
 * and strings with size 1 or 2.
 */

public class QueryModification {

    private final TreeMap<String, ArrayList<Float>> queryDictionary;

    public QueryModification(String userQuery, TreeMap<String, ArrayList<String>> dictionary) {

        this.queryDictionary = new TreeMap<>();

        /*
         Remove all unnecessary spaces.
         Remove symbols like .,-,=,{,},:,"," and words: title, booktitle and journal.
         Convert all sentences to lowercase.
         This is a EARLY STOPWORD REMOVAL.
         */
        userQuery = userQuery.replaceAll("(\\s+)", " ");
        userQuery = userQuery.replaceAll("(\\.)|(-)|(=)|(\\{)|(\\})|(,)|(:)|(\\?)|(\\btitle\\b)|(\\bbooktitle\\b)|(\\bjournal\\b)", "");
        userQuery = userQuery.toLowerCase();

        // Saperate every sentence(by " ") to words and add them to NEW arrayList bagOfWords.
        ArrayList<String> bagOfWords = new ArrayList<>();
        String tempStrings[] = userQuery.split(" ");
        bagOfWords.addAll(Arrays.asList(tempStrings));

        // Remove all empty records. 
        bagOfWords.removeAll(Arrays.asList(null, ""));

        // Remove all stop words.      
        bagOfWords = new StopWordsRemoval(bagOfWords).getfileWithoutStopWords();
        // Porter Stemming in all words.
        Porter StemPorter = new Porter();
        for (int i = 0; i < bagOfWords.size(); i++) {
            bagOfWords.set(i, StemPorter.stripAffixes(bagOfWords.get(i)));
        }
        // Remove all numbers, number-nd, number-st, number-th, number-rd, number-d and strings with size 1 or 2.
        // Also, 29juli and 30septemb.
        for (Iterator<String> it = bagOfWords.iterator(); it.hasNext();) {
            if (it.next().matches("([0-9]+)|([0-9]+nd)|([0-9]+st)|([0-9]+th)|([0-9]+rd)|([0-9]+d)|(\\b[a-zA-Z]{1,2}\\b)|(\\b29juli\\b)|(\\b30septemb\\b)")) {
                it.remove();
            }
        }
        // Remove all empty records AGAIN. 
        bagOfWords.removeAll(Arrays.asList(null, ""));
       
        // Remove all words don't exist in Dictionary.       
        bagOfWords.retainAll(dictionary.keySet());            

        // Create of one memeber Dictionary.
        for (String word : bagOfWords) {
            // This word has not been found anywhere before,
            // so create a Map to hold document-map counts.
            if (!(queryDictionary.containsKey(word))) {
                ArrayList<Float> tempArrayList = new ArrayList();
                tempArrayList.add(Float.valueOf(1));
                queryDictionary.put(word, tempArrayList);
            } else {
                Float currentCount = queryDictionary.get(word).get(0);
                currentCount++;
                ArrayList<Float> tempArrayList = new ArrayList();
                tempArrayList.add(currentCount);
                queryDictionary.put(word, tempArrayList);
            }

        }

    }
    
    // Getter.
    public TreeMap<String, ArrayList<Float>> getQueryModification() {
        return queryDictionary;
    }

}
