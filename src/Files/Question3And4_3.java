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
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * Question3And4_3 the implementation of question3 and question4_3. Application
 * of similarity cosine type for each DEP member vector with all other DEP
 * member vectors. For question3 use as weight tfidf and simCos - for
 * question4_3 use freq and DOT PRODUCT. Also use look Up Table for gain time
 * don't do double calculations.
 */
public class Question3And4_3 {

    private final int M;
    private final File resultsFile;
    private String fileNameQuestion3;
    private String fileNameQuestion4_3;
    private final HashMap<String, TreeMap<String, ArrayList<Float>>> allMembersDictionary;

    // Use for gain time dont do double calculations.
    private String lookUpTableQuestion3[][];
    private String lookUpTableQuestion4_3[][];

    // Constractor.
    public Question3And4_3(int M, File resultsFile, HashMap<String, TreeMap<String, ArrayList<Float>>> allMembersDictionary) {
        this.M = M;
        this.resultsFile = resultsFile;
        this.allMembersDictionary = allMembersDictionary;

        //Store all keys to an arraylst
        ArrayList<String> allMembersNames = new ArrayList<>();
        for (Entry<String, TreeMap<String, ArrayList<Float>>> entry : allMembersDictionary.entrySet()) {
            allMembersNames.add(entry.getKey());
        }

        // Create Look Up Table for Question3.
        lookUpTableQuestion3 = new String[allMembersDictionary.size() + 1][allMembersDictionary.size() + 1];
        for (int i = 0; i < allMembersDictionary.size() + 1; i++) {
            for (int j = 0; j < allMembersDictionary.size() + 1; j++) {

                if (i == 0 && j == 0) {
                    lookUpTableQuestion3[i][j] = "NAMES";
                } else if (i == 0) {
                    lookUpTableQuestion3[i][j] = allMembersNames.get(j - 1);
                } else if (j == 0) {
                    lookUpTableQuestion3[i][j] = allMembersNames.get(i - 1);
                } else if (i == j) {
                    lookUpTableQuestion3[i][j] = "1";
                } else if (i <= j) {
                    lookUpTableQuestion3[i][j] = "WRITE";
                } else {
                    lookUpTableQuestion3[i][j] = "READ";
                }
            }
        }

        // Create Look Up Table for Question4_3.
        lookUpTableQuestion4_3 = new String[allMembersDictionary.size() + 1][allMembersDictionary.size() + 1];
        for (int i = 0; i < allMembersDictionary.size() + 1; i++) {
            for (int j = 0; j < allMembersDictionary.size() + 1; j++) {

                if (i == 0 && j == 0) {
                    lookUpTableQuestion4_3[i][j] = "NAMES";
                } else if (i == 0) {
                    lookUpTableQuestion4_3[i][j] = allMembersNames.get(j - 1);
                } else if (j == 0) {
                    lookUpTableQuestion4_3[i][j] = allMembersNames.get(i - 1);
                } else if (i == j) {
                    lookUpTableQuestion4_3[i][j] = "WRITE";
                } else if (i <= j) {
                    lookUpTableQuestion4_3[i][j] = "WRITE";
                } else {
                    lookUpTableQuestion4_3[i][j] = "READ";
                }
            }
        }

    }

    // Question 3.
    public void doQuestion3() {

        // Create filename.
        this.fileNameQuestion3 = resultsFile + File.separator + "similar_profs.txt";

        // Open file and write header.
        try (FileWriter fw = new FileWriter(this.fileNameQuestion3)) {
            fw.append("   DEP_member: (otherProf,simCos) M-times from high(pos_2) to low(M+1). Don't show the first(always one).");
            fw.append(System.getProperty("line.separator"));
        } catch (IOException ex) {
            System.err.println("Could Open to: " + this.fileNameQuestion3);
            ex.getMessage();
        }

        // Calc one dep member with all other dep members.
        for (Map.Entry<String, TreeMap<String, ArrayList<Float>>> memberVSM : this.allMembersDictionary.entrySet()) {
            TreeMap<String, ArrayList<Float>> memberAllTermWithMetrics = memberVSM.getValue();
            String memberName = memberVSM.getKey();
            doQuestion3(memberName, memberAllTermWithMetrics);
        }

        System.out.println("Question 3 executed !!!");

    }

    public void doQuestion3(String profName, TreeMap<String, ArrayList<Float>> queryAllTermWithMetrics) {

        // Temp values for calc simCos.
        float dotProduct;
        float sumPowsAndSqrtMember;
        float sumPowsAndSqrtQuery;
        float cosSim;

        // Store DEP member name with the cosSim.
        TreeMap<String, Float> simCosStore = new TreeMap<>();

        // Get member position.
        int memberPos = 0;
        for (int i = 0; i < allMembersDictionary.size() + 1; i++) {
            if (lookUpTableQuestion3[i][0].compareTo(profName) == 0) {
                memberPos = i;
                break;
            }
        }

        for (Map.Entry<String, TreeMap<String, ArrayList<Float>>> memberVSM : allMembersDictionary.entrySet()) {
            TreeMap<String, ArrayList<Float>> memberAllTermWithMetrics = memberVSM.getValue();

            // Get current memeber pos from table.
            int currentMemeberPos = 0;

            for (int j = 0; j < allMembersDictionary.size() + 1; j++) {
                if (lookUpTableQuestion3[0][j].compareTo(memberVSM.getKey()) == 0) {
                    currentMemeberPos = j;
                    break;
                }
            }

            // Initialize values (used for formula calculation).
            dotProduct = 0;
            sumPowsAndSqrtMember = 0;
            sumPowsAndSqrtQuery = 0;
            cosSim = 0;

            // CROSS-CASE
            if (memberPos == currentMemeberPos) {
                cosSim = 1;
                simCosStore.put(memberVSM.getKey(), cosSim);

                String cosSimString = Float.toString(cosSim);
                lookUpTableQuestion3[memberPos][currentMemeberPos] = cosSimString;

                // UPPER-TRIANGLE
            } else if (lookUpTableQuestion3[memberPos][currentMemeberPos].compareTo("WRITE") == 0) {

                for (Map.Entry<String, ArrayList<Float>> memberTermWithMetrics : memberAllTermWithMetrics.entrySet()) {

                    for (Map.Entry<String, ArrayList<Float>> queryTermWithMetrics : queryAllTermWithMetrics.entrySet()) {

                        // I want common terms ONLY.
                        if (queryTermWithMetrics.getKey().compareTo(memberTermWithMetrics.getKey()) == 0) {

                            // I care only for the existing terms of member.
                            if (queryTermWithMetrics.getValue().get(2) != 0) {
                                dotProduct += memberTermWithMetrics.getValue().get(2) * queryTermWithMetrics.getValue().get(2);  //a.b
                                sumPowsAndSqrtMember += Math.pow(memberTermWithMetrics.getValue().get(2), 2);  //(a^2)
                                sumPowsAndSqrtQuery += Math.pow(queryTermWithMetrics.getValue().get(2), 2); //(b^2)
                            }
                        }

                    }
                }
                sumPowsAndSqrtMember = (float) Math.sqrt(sumPowsAndSqrtMember);//sqrt(a^2)
                sumPowsAndSqrtQuery = (float) Math.sqrt(sumPowsAndSqrtQuery);//sqrt(b^2)

                if (sumPowsAndSqrtMember != 0.0 && sumPowsAndSqrtQuery != 0.0) {
                    cosSim = dotProduct / (sumPowsAndSqrtMember * sumPowsAndSqrtQuery);

                    // fix accurancy.
                    if (cosSim > 0.9999) {
                        cosSim = 1;
                    }

                    simCosStore.put(memberVSM.getKey(), cosSim);
                } else {
                    simCosStore.put(memberVSM.getKey(), (float) 0);
                }
                String cosSimString = Float.toString(cosSim);
                lookUpTableQuestion3[memberPos][currentMemeberPos] = cosSimString;

                // HAS CALCULATED.
            } else {

                cosSim = Float.valueOf(lookUpTableQuestion3[currentMemeberPos][memberPos]);
                simCosStore.put(memberVSM.getKey(), cosSim);
                lookUpTableQuestion3[memberPos][currentMemeberPos] = lookUpTableQuestion3[currentMemeberPos][memberPos];
            }
        }

        // Use list for better/easyiest use of TreeMap(TERM, SIMCOS).
        List<Map.Entry<String, Float>> sortedSimCos = new ArrayList<>(simCosStore.entrySet());
        // Comparator by value
        Collections.sort(sortedSimCos, new Comparator<Map.Entry<String, Float>>() {
            @Override
            public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2) {
                return -(o1.getValue().compareTo(o2.getValue())); // Invert for descending order(max to min).
            }
        });

        // TRY WITH RECOURSES.
        try (FileWriter fw = new FileWriter(fileNameQuestion3, true)) {

            fw.append(System.getProperty("line.separator"));
            String formatedProf = String.format("%13s", profName);
            fw.append(formatedProf + ":");

            // Only the first M elements I kick the first is always 1.
            for (int i = 1; i < M + 1; i++) {

                String DEPmember = sortedSimCos.get(i).getKey();
                Float simCos = sortedSimCos.get(i).getValue();
                fw.append(" (" + DEPmember + "," + simCos + ") ");
                fw.flush(); // Clean buffer.
            }

            fw.close();

        } catch (IOException ex) {
            System.err.println("Could Write to: " + fileNameQuestion3);
            ex.getMessage();
        }

    }

    public void saveArrayOfQuestion3() {

        String fileName = resultsFile+File.separator+"full-array-of-profs.txt";
        // Open file and write header.
        try (FileWriter fw = new FileWriter(fileName)) {
            for (int i = 0; i < allMembersDictionary.size() + 1; i++) {
                fw.append(System.getProperty("line.separator"));
                for (int j = 0; j < allMembersDictionary.size() + 1; j++) {
                    String formatedFields = String.format("%13s", lookUpTableQuestion3[i][j]);
                    fw.append(formatedFields + " | ");
                }
            }
            fw.append(System.getProperty("line.separator"));

        } catch (IOException ex) {
            System.err.println("Could Open to: " + fileName);
            ex.getMessage();
        }

    }

    // Question 4_3.
    public void doQuestion4_3() {

        // Create filename.
        fileNameQuestion4_3 = resultsFile + File.separator + "similar_profs-Question4.txt";

        // Open file and write header.
        try (FileWriter fw = new FileWriter(fileNameQuestion4_3)) {
            fw.append("   DEP_member: (otherProf,dotProductOfFreq) M-times from high(pos_1) to low(M).");
            fw.append(System.getProperty("line.separator"));
        } catch (IOException ex) {
            System.err.println("Could Open to: " + fileNameQuestion4_3);
            ex.getMessage();
        }

        // Calc one dep member with all other dep members.
        for (Map.Entry<String, TreeMap<String, ArrayList<Float>>> memberVSM : allMembersDictionary.entrySet()) {
            TreeMap<String, ArrayList<Float>> memberAllTermWithMetrics = memberVSM.getValue();
            String memberName = memberVSM.getKey();
            doQuestion4_3(memberName, memberAllTermWithMetrics);
        }
        System.out.println("Question 4_3 executed !!!");

    }

    public void doQuestion4_3(String profName, TreeMap<String, ArrayList<Float>> queryAllTermWithMetrics) {

        // Temp values for Dot_Product.
        float dotProduct;

        // Store DEP member name with the dotProduct.
        TreeMap<String, Float> dotProductStore = new TreeMap<>();

        // Get member position.
        int memberPos = 0;
        for (int i = 0; i < allMembersDictionary.size() + 1; i++) {
            if (lookUpTableQuestion4_3[i][0].compareTo(profName) == 0) {
                memberPos = i;
                break;
            }
        }

        for (Map.Entry<String, TreeMap<String, ArrayList<Float>>> memberVSM : allMembersDictionary.entrySet()) {
            TreeMap<String, ArrayList<Float>> memberAllTermWithMetrics = memberVSM.getValue();

            // Get current memeber pos from table.
            int currentMemeberPos = 0;

            for (int j = 0; j < allMembersDictionary.size() + 1; j++) {
                if (lookUpTableQuestion4_3[0][j].compareTo(memberVSM.getKey()) == 0) {
                    currentMemeberPos = j;
                    break;
                }
            }

            // Initialize values (used for formula calculation).
            dotProduct = 0;

            // CROSS-CASE & UPPER-TRIANGLE
            if (lookUpTableQuestion4_3[memberPos][currentMemeberPos].compareTo("WRITE") == 0) {

                for (Map.Entry<String, ArrayList<Float>> memberTermWithMetrics : memberAllTermWithMetrics.entrySet()) {

                    for (Map.Entry<String, ArrayList<Float>> queryTermWithMetrics : queryAllTermWithMetrics.entrySet()) {

                        // I want common terms ONLY.
                        if (queryTermWithMetrics.getKey().compareTo(memberTermWithMetrics.getKey()) == 0) {

                            // I care only for the existing terms of member.
                            if (queryTermWithMetrics.getValue().get(0) != 0) {
                                dotProduct += memberTermWithMetrics.getValue().get(0) * queryTermWithMetrics.getValue().get(0);  //a.b
                            }
                        }

                    }
                }

                String dotProductString = Float.toString(dotProduct);
                lookUpTableQuestion4_3[memberPos][currentMemeberPos] = dotProductString;
                dotProductStore.put(memberVSM.getKey(), dotProduct);

                // HAS CALCULATED.
            } else {

                dotProduct = Float.valueOf(lookUpTableQuestion4_3[currentMemeberPos][memberPos]);
                dotProductStore.put(memberVSM.getKey(), dotProduct);
                lookUpTableQuestion4_3[memberPos][currentMemeberPos] = lookUpTableQuestion4_3[currentMemeberPos][memberPos];
            }

        }

        // Use list for better/easyiest use of TreeMap(TERM, DOT_PRODUCT).
        List<Map.Entry<String, Float>> sortedDotProduct = new ArrayList<>(dotProductStore.entrySet());
        // Comparator by value
        Collections.sort(sortedDotProduct, new Comparator<Map.Entry<String, Float>>() {
            @Override
            public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2) {
                return -(o1.getValue().compareTo(o2.getValue())); // Invert for descending order(max to min).
            }
        });

        // TRY WITH RECOURSES.
        try (FileWriter fw = new FileWriter(fileNameQuestion4_3, true)) {

            fw.append(System.getProperty("line.separator"));
            String formatedProf = String.format("%13s", profName);
            fw.append(formatedProf + ":");

            // Only the first M elements I kick the first is always 1.
            for (int i = 0; i < M ; i++) {

                String DEPmember = sortedDotProduct.get(i).getKey();
                Float dotProd = sortedDotProduct.get(i).getValue();
                fw.append(" (" + DEPmember + "," + dotProd + ") ");
                fw.flush(); // Clean buffer.
            }

            fw.close();

        } catch (IOException ex) {
            System.err.println("Could Write to: " + fileNameQuestion4_3);
            ex.getMessage();
        }

    }

    public void saveArrayOfQuestion4_3() {

        String fileName = resultsFile+File.separator+"full-array-of-profs-Question4.txt";
        // Open file and write header.
        try (FileWriter fw = new FileWriter(fileName)) {
            for (int i = 0; i < allMembersDictionary.size() + 1; i++) {
                fw.append(System.getProperty("line.separator"));
                for (int j = 0; j < allMembersDictionary.size() + 1; j++) {
                    String formatedFields = String.format("%13s", lookUpTableQuestion4_3[i][j]);
                    fw.append(formatedFields + " | ");
                }
            }
            fw.append(System.getProperty("line.separator"));

        } catch (IOException ex) {
            System.err.println("Could Open to: " + fileName);
            ex.getMessage();
        }

    }
}
