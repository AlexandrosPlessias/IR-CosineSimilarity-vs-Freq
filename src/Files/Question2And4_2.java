/**
 * @author Πλέσσιας Αλέξανδρος (ΑΜ.:2025201100068).
 */
package Files;

import IR_project1.CalcUsefullMetrics;
import IR_project1.CreateVSMs;
import PreProcessing.QueryModification;
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
 * Question2And4_2 the implementation of question2 and question4_2. Application
 * of similarity cosine type for each DEP member vector with QUERY vector. 
 * For question2 use as weight tfidf and simCos - for question4_2 use freq and DOT PRODUCT.
 */
public class Question2And4_2 {

    private final File resultsFile;
    private final String userQuery;
    private final TreeMap<String, ArrayList<String>> dictionary;
    private final TreeMap<String, Float> allTermsIDF;
    private final HashMap<String, TreeMap<String, ArrayList<Float>>> allMembersDictionary;

    public Question2And4_2(File resultsFile, String userQuery, TreeMap<String, ArrayList<String>> dictionary, TreeMap<String, Float> allTermsIDF, HashMap<String, TreeMap<String, ArrayList<Float>>> allMembersDictionary) {
        this.resultsFile = resultsFile;
        this.userQuery = userQuery;
        this.dictionary = dictionary;
        this.allTermsIDF = allTermsIDF;
        this.allMembersDictionary = allMembersDictionary;
    }

    public void doQuenstion2() {
        QueryModification queryModification = new QueryModification(userQuery, dictionary);
        TreeMap<String, ArrayList<Float>> queryTreeMap = queryModification.getQueryModification();

        HashMap<String, TreeMap<String, ArrayList<Float>>> queryHashMap = new HashMap<>();
        queryHashMap.put("Query", queryTreeMap);

        CreateVSMs queryVSM = new CreateVSMs(queryHashMap, dictionary);
        queryHashMap = queryVSM.getVSMs(); // Overwrite.

        //Calculate all tf and tf-idf for query vector. 
        CalcUsefullMetrics QueryUsefullMetrics = new CalcUsefullMetrics(queryHashMap, allTermsIDF);
        HashMap<String, TreeMap<String, ArrayList<Float>>> queryTfidfHashMap = QueryUsefullMetrics.getUsefullMetrics(); //Overwrite aqain.

        // Temp values for calc simCos.
        float dotProduct;
        float sumPowsAndSqrtMember;
        float sumPowsAndSqrtQuery;
        float cosSim;

        // Store DEP member name with the cosSim.
        TreeMap<String, Float> simCosStore = new TreeMap<>();

        for (Map.Entry<String, TreeMap<String, ArrayList<Float>>> memberVSM : allMembersDictionary.entrySet()) {
            TreeMap<String, ArrayList<Float>> memberAllTermWithMetrics = memberVSM.getValue();
            // Initialize values (used for formula calculation).
            dotProduct = 0;
            sumPowsAndSqrtMember = 0;
            sumPowsAndSqrtQuery = 0;
            cosSim = 0;
            for (Map.Entry<String, ArrayList<Float>> memberTermWithMetrics : memberAllTermWithMetrics.entrySet()) {

                for (Map.Entry<String, TreeMap<String, ArrayList<Float>>> queryVSMtemp : queryTfidfHashMap.entrySet()) {
                    TreeMap<String, ArrayList<Float>> queryAllTermWithMetrics = queryVSMtemp.getValue();
                    for (Map.Entry<String, ArrayList<Float>> queryTermWithMetrics : queryAllTermWithMetrics.entrySet()) {

                        // I want common terms ONLY.
                        if (queryTermWithMetrics.getKey().compareTo(memberTermWithMetrics.getKey()) == 0) {

                            // I care only for the existing terms of Query.
                            if (queryTermWithMetrics.getValue().get(2) != 0) {
                                dotProduct += memberTermWithMetrics.getValue().get(2) * queryTermWithMetrics.getValue().get(2);  //a.b
                                sumPowsAndSqrtMember += Math.pow(memberTermWithMetrics.getValue().get(2), 2);  //(a^2)
                                sumPowsAndSqrtQuery += Math.pow(queryTermWithMetrics.getValue().get(2), 2); //(b^2)
                            }
                        }
                    }
                }
            }
            sumPowsAndSqrtMember = (float) Math.sqrt(sumPowsAndSqrtMember);//sqrt(a^2)
            sumPowsAndSqrtQuery = (float) Math.sqrt(sumPowsAndSqrtQuery);//sqrt(b^2)

            // Check if can divide by zero.
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

        // Make filename elegant.
        String fileName = "result " + userQuery + ".txt";
        fileName = fileName.replaceAll("\\s+", "-");
        fileName = fileName.toLowerCase();

        // Create and fill result file.
        try (FileWriter fw = new FileWriter(resultsFile + File.separator + fileName)) {

            fw.append("(DEPname,simCos) : from high to low");
            fw.append(System.getProperty("line.separator"));
            fw.append(System.getProperty("line.separator"));

            for (int i = 0; i < sortedSimCos.size(); i++) {
                String DEPmember = sortedSimCos.get(i).getKey();
                Float simCos = sortedSimCos.get(i).getValue();
                String formatedDEPmember = String.format("%13s", DEPmember);
                fw.append("(" + formatedDEPmember + "," + simCos + ")");
                fw.append(System.getProperty("line.separator"));
                fw.flush(); // Clean buffer.
            }

            fw.close();
        } catch (IOException ex) {
            System.err.println("Could Write to: " + resultsFile + File.separator + "prof-description.txt");
            ex.getMessage();
        }

        System.out.println("Question 2 executed !!!");

    }

    public void doQuenstion4_2() {
        QueryModification queryModification = new QueryModification(userQuery, dictionary);
        TreeMap<String, ArrayList<Float>> queryTreeMap = queryModification.getQueryModification();

        HashMap<String, TreeMap<String, ArrayList<Float>>> queryHashMap = new HashMap<>();
        queryHashMap.put("Query", queryTreeMap);

        CreateVSMs queryVSM = new CreateVSMs(queryHashMap, dictionary);
        queryHashMap = queryVSM.getVSMs(); // Overwrite.

        // Temp values for Dot_Product.
        float dotProduct;
        
         // Store DEP member name with the dotProduct.
        TreeMap<String, Float> dotProductStore = new TreeMap<>();

        for (Map.Entry<String, TreeMap<String, ArrayList<Float>>> memberVSM : allMembersDictionary.entrySet()) {
            TreeMap<String, ArrayList<Float>> memberAllTermWithMetrics = memberVSM.getValue();
            // Initialize values (used for formula calculation).
            dotProduct = 0;
            for (Map.Entry<String, ArrayList<Float>> memberTermWithMetrics : memberAllTermWithMetrics.entrySet()) {

                for (Map.Entry<String, TreeMap<String, ArrayList<Float>>> queryVSMtemp : queryHashMap.entrySet()) {
                    TreeMap<String, ArrayList<Float>> queryAllTermWithMetrics = queryVSMtemp.getValue();
                    for (Map.Entry<String, ArrayList<Float>> queryTermWithMetrics : queryAllTermWithMetrics.entrySet()) {

                        // I want common terms ONLY.
                        if (queryTermWithMetrics.getKey().compareTo(memberTermWithMetrics.getKey()) == 0) {

                            // I care only for the existing terms of Query.
                            if (queryTermWithMetrics.getValue().get(0) != 0) {
                                dotProduct += memberTermWithMetrics.getValue().get(0) * queryTermWithMetrics.getValue().get(0);  //a.b
                            }
                        }
                    }
                }
            }
            dotProductStore.put(memberVSM.getKey(), dotProduct);

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

        // Elegant filename.
        String fileName = "result " + userQuery;
        fileName = fileName.replaceAll("\\s+", "-");
        fileName = fileName.toLowerCase();
        String finalFileName = fileName + "-Question4.txt";

        // Create and fill result file.
        try (FileWriter fw = new FileWriter(resultsFile + File.separator + finalFileName)) {

            fw.append("(DEPname,dotProductOfFreq) : from high to low");
            fw.append(System.getProperty("line.separator"));
            fw.append(System.getProperty("line.separator"));

            for (int i = 0; i < sortedDotProduct.size(); i++) {
                String DEPmember = sortedDotProduct.get(i).getKey();
                Float dotProd = sortedDotProduct.get(i).getValue();
                String formatedDEPmember = String.format("%13s", DEPmember);
                fw.append("(" + formatedDEPmember + "," + dotProd + ")");
                fw.append(System.getProperty("line.separator"));
                fw.flush(); // Clean buffer.
            }

            fw.close();
            System.out.println("Question 4.2 executed !!!");
        } catch (IOException ex) {
            System.err.println("Could Write to: " + resultsFile + File.separator + "prof-description.txt");
            ex.getMessage();
        }
    }
}
