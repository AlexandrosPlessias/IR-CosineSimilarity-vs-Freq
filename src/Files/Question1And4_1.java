/**
 * @author Πλέσσιας Αλέξανδρος (ΑΜ.:2025201100068).
 */
package Files;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Question1And4_1 is the implementation of question 1 and 4.1. 
 * Create a temp treeMap where store ONLY member's terms and tf-idf (or freq) after 
 * sort them and write in file ONLY first N-terms repeat the process for each member.
 */
public class Question1And4_1 {

    private final File resultsFile;
    private final HashMap<String, TreeMap<String, ArrayList<Float>>> allMembersDictionary;
    private final int N;
    
    // Constractor.
    public Question1And4_1( int N, File resultsFile, HashMap<String, TreeMap<String, ArrayList<Float>>> allMembersDictionary) {

        this.resultsFile = resultsFile;
        this.allMembersDictionary = allMembersDictionary;
        this.N = N;

    }

    public void doQuenstion1() {
        try (FileWriter fw = new FileWriter(resultsFile + File.separator + "prof-description.txt")) {

            fw.append("   DEP's Name: (term,tfidf) N-pairs");
            fw.append(System.getProperty("line.separator"));
            fw.append(System.getProperty("line.separator"));

            for (Map.Entry<String, TreeMap<String, ArrayList<Float>>> wordTDocument : allMembersDictionary.entrySet()) {
                TreeMap<String, ArrayList<Float>> documentTWordCount = wordTDocument.getValue();

                String DEPmember = wordTDocument.getKey();
                String formatedDEPmember = String.format("%13s", DEPmember);
                fw.append(formatedDEPmember + ": ");

                // Create temp hashtree for TERM, TF-IDF ONLY !!!
                TreeMap<String, Float> memberTermAndTFIDF = new TreeMap<>();
                for (Map.Entry<String, ArrayList<Float>> documentToFrequency : documentTWordCount.entrySet()) {
                    memberTermAndTFIDF.put(documentToFrequency.getKey(), documentToFrequency.getValue().get(2));
                }

                // Use list for better/easyiest use of TreeMap(TERM, TF-IDF).
                List<Map.Entry<String, Float>> sortedTermAndTFIDF = new ArrayList<>(memberTermAndTFIDF.entrySet());
                // Comparator by value
                Collections.sort(sortedTermAndTFIDF, new Comparator<Map.Entry<String, Float>>() {
                    @Override
                    public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2) {
                        return -(o1.getValue().compareTo(o2.getValue())); // Invert for descending order(max to min).
                    }
                });

                // Write ONLY the first N elements i.e. the most important. 
                for (int i = 0; i < N; i++) {
                    String term = sortedTermAndTFIDF.get(i).getKey();
                    Float tfidf = sortedTermAndTFIDF.get(i).getValue();
                    fw.append("(" + term + "," + tfidf + ") ");
                }

                fw.append(System.getProperty("line.separator"));
                fw.flush(); // Clean buffer.
            }

            fw.close();
            System.out.println("Question 1 executed !!!");
        } catch (IOException ex) {
            System.err.println("Could Write to: " + resultsFile + File.separator + "prof-description.txt");
            ex.getMessage();
        }
    }

    public void doQuenstion4_1() {
        try (FileWriter fw = new FileWriter(resultsFile + File.separator + "prof-description-Question4.txt")) {

            fw.append("   DEP's Name: (term,freq) N-pairs");
            fw.append(System.getProperty("line.separator"));
            fw.append(System.getProperty("line.separator"));

            for (Map.Entry<String, TreeMap<String, ArrayList<Float>>> wordTDocument : allMembersDictionary.entrySet()) {
                TreeMap<String, ArrayList<Float>> documentTWordCount = wordTDocument.getValue();

                String DEPmember = wordTDocument.getKey();
                String formatedDEPmember = String.format("%13s", DEPmember);
                fw.append(formatedDEPmember + ": ");

                // Create temp hashtree for TERM, FREQ ONLY !!!
                TreeMap<String, Float> memberTermAndTFIDF = new TreeMap<>();
                for (Map.Entry<String, ArrayList<Float>> documentToFrequency : documentTWordCount.entrySet()) {
                    memberTermAndTFIDF.put(documentToFrequency.getKey(), documentToFrequency.getValue().get(0));
                }

                // Use list for better/easyiest use of TreeMap(TERM, FREQ).
                List<Map.Entry<String, Float>> sortedTermAndTFIDF = new ArrayList<>(memberTermAndTFIDF.entrySet());
                // Comparator by value
                Collections.sort(sortedTermAndTFIDF, new Comparator<Map.Entry<String, Float>>() {
                    @Override
                    public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2) {
                        return -(o1.getValue().compareTo(o2.getValue())); // Invert for descending order(max to min).
                    }
                });

                // Write ONLY the first N elements i.e. the most important. 
                for (int i = 0; i < N; i++) {
                    String term = sortedTermAndTFIDF.get(i).getKey();
                    Float tfidf = sortedTermAndTFIDF.get(i).getValue();
                    fw.append("(" + term + "," + tfidf + ") ");
                }

                fw.append(System.getProperty("line.separator"));
                fw.flush(); // Clean buffer.
            }

            fw.close();
            System.out.println("Question 4.1 executed !!!");
        } catch (IOException ex) {
            System.err.println("Could Write to: " + resultsFile + File.separator + "prof-description.txt");
            ex.getMessage();
        }

    }

}
