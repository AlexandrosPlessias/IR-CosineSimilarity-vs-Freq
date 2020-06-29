/**
 * @author Πλέσσιας Αλέξανδρος (ΑΜ.:2025201100068).
 */

package IR_project1;

import Files.FilesOfFolder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * AllMembersDictionary store all members dictionaries in a data structure 
 * of the form HashMap<MemberName, TreeMap<TermsOfMember, ArrayList<freq,tf,tfidf>>> 
 * for more details read report.
 * Open all files content and store it in String and create for each member a
 * dictionary (through class MemberDictionary ) and store them all together. 
 */
public class AllMembersDictionaries {

    private FilesOfFolder allFilenames;
    private HashMap<String, TreeMap<String, ArrayList<Float>>> allMembersDictionaries;

    public AllMembersDictionaries(String folderName) {
        
        // Initialize allMembersDictionaries & allFilenames folder.
        this.allFilenames = new FilesOfFolder(folderName);
        this.allMembersDictionaries = new HashMap<>();

        ArrayList<String> allDEPsFilenames = allFilenames.getFilesNames();

        String memberFileText;
        BufferedReader br = null;
        try {
            for (String DEPmemder : allDEPsFilenames) {
                memberFileText = "";
                br = new BufferedReader(new FileReader(folderName + File.separator + DEPmemder));
                String sCurrentLine;
                // Read all file line by line.
                while ((sCurrentLine = br.readLine()) != null) {
                    // Store all file text to memberFileText(String);
                    memberFileText = memberFileText + " \n" + sCurrentLine;
                }

                // Get each member dictionary and add all memebers in a HashMap. 
                MemberDictionary oneMemberFileTokenization = new MemberDictionary(DEPmemder, memberFileText);
                this.allMembersDictionaries.put(oneMemberFileTokenization.getMemberName(), oneMemberFileTokenization.getMemberDictionary());
            }

            br.close();

        } catch (IOException e) {
            e.getMessage();
        }
    }
    
    // Getter.
    public HashMap<String, TreeMap<String, ArrayList<Float>>> getAllDEPmembesrDictionary() {
        return allMembersDictionaries;
    }

}
