/**
 * @author Πλέσσιας Αλέξανδρος (ΑΜ.:2025201100068).
 */
package IR_project1;

import PreProcessing.Porter;
import PreProcessing.StopWordsRemoval;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * MemberDictionary get member file with full text do the necessary processing
 * and store all members in form: TreeMap<term, ArrayList with frequency>.
 * First get the parts of file i want and after dismiss special characters,
 * trivial words(like title,book-title & journal), white-spaces and stop-words.
 * After create tokens and implement stemming and delete numbers(-nd-th-rd-d)
 * and strings with size 1 or 2.
 */
public class MemberDictionary {

    private final String DEPmemberName;
    private final TreeMap<String, ArrayList<Float>> DEPmemberDictionary;

    public MemberDictionary(String DEPmemder, String DEPsMemberFile) {

        // Get real surname without .bib in end.
        this.DEPmemberName = DEPmemder.replaceAll(".bib", "");
        this.DEPmemberDictionary = new TreeMap<>();
        ArrayList<String> sentencesOfText = new ArrayList<>();

        /* 
         Use REGEXP for find all titles of DEP's memeber.
         Pattern analysis: "title" whitespaces "=" whitespaces "{" ( (anything)||((anything \n anything))||((anything \n anything \n anything))) ) "},"
         Anything means all characters until \n.
         */
        Pattern titlePattern = Pattern.compile("(\\btitle\\b(\\s)*=(\\s)*\\{(((.*)|((.*\\n.*))|(.*\\n.*\\n.*))\\},))");
        Matcher titlePatternMatcher = titlePattern.matcher(DEPsMemberFile);
        while (titlePatternMatcher.find()) {
            sentencesOfText.add(titlePatternMatcher.group(0));

        }

        /* 
         Use REGEXP for find all titles of DEP's memeber.
         Pattern analysis: ("booktitle"||"journal") whitespaces "=" whitespaces "{" ( (anything)||((anything \n anything))||((anything \n anything \n anything))) ) "},"
         Anything means all characters until \n.
         */
        Pattern bookOrJournalTtlPattern = Pattern.compile("(((\\bbooktitle\\b)|(\\bjournal\\b))(\\s)*=(\\s)*\\{(((.*)|((.*\\n.*))|(.*\\n.*\\n.*))\\},))");
        Matcher bookOrJournalTtlPatternMatcher = bookOrJournalTtlPattern.matcher(DEPsMemberFile);
        while (bookOrJournalTtlPatternMatcher.find()) {
            sentencesOfText.add(bookOrJournalTtlPatternMatcher.group(0));
        }

        /*
         Remove all unnecessary spaces.
         Remove symbols like -,=,{,},:,"," and words: title, booktitle and journal.
         Convert all sentences to lowercase.
         This is a EARLY STOPWORD REMOVAL.
         */
        for (int i = 0; i < sentencesOfText.size(); i++) {
            sentencesOfText.set(i, sentencesOfText.get(i).replaceAll("(\\s+)", " "));
            sentencesOfText.set(i, sentencesOfText.get(i).replaceAll("(-)|(=)|(\\{)|(\\})|(,)|(:)|(\\?)|(\\btitle\\b)|(\\bbooktitle\\b)|(\\bjournal\\b)", ""));
            sentencesOfText.set(i, sentencesOfText.get(i).toLowerCase());
        }

        // Saperate every sentence(by " ") to words and add them to NEW arrayList bagOfWords.
        ArrayList<String> bagOfWords = new ArrayList<>();
        for (String sentenceOfText : sentencesOfText) {
            String tempStrings[] = sentenceOfText.split(" ");
            bagOfWords.addAll(Arrays.asList(tempStrings));
        }

        // Free(?) of arraylist.
        sentencesOfText.clear();

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

        // Create of one memeber Dictionary.
        for (String word : bagOfWords) {
            // This word has not been found anywhere before,
            // so create a Map to hold document-map counts.
            if (!(DEPmemberDictionary.containsKey(word))) {
                ArrayList<Float> tempArrayList = new ArrayList();
                tempArrayList.add(Float.valueOf(1));
                DEPmemberDictionary.put(word, tempArrayList);
            } else {
                Float currentCount = DEPmemberDictionary.get(word).get(0);
                currentCount++;
                ArrayList<Float> tempArrayList = new ArrayList();
                tempArrayList.add(currentCount);
                DEPmemberDictionary.put(word, tempArrayList);
            }
        }

    }
    
    // Getters.
    public String getMemberName() {
        return DEPmemberName;
    }

    public TreeMap<String, ArrayList<Float>> getMemberDictionary() {
        return DEPmemberDictionary;
    }

}
