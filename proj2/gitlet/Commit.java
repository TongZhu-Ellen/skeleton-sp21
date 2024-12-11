package gitlet;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

import static gitlet.Repository.*;
import static gitlet.Utils.*;

class Commit implements Serializable {

    String message;
    Date time;
    Map<String, String> nameShaMap;
    Commit parent;

    Commit(String inputMsg, Commit inputParent) {
        this.message = inputMsg;

        if (inputParent == null) {
            this.time = new Date(0);
        } else {
            this.time = new Date();
        }

        this.nameShaMap = new HashMap<>();

        this.parent = inputParent;
    }

    Set<String> fileSet() {
        return this.nameShaMap.keySet();
    }

    void putNameSha(String name, String sha) {
        this.nameShaMap.put(name, sha);
    }


    String getSha(String name) {
        return this.nameShaMap.get(name);
    }


    byte[] getFileContent(String name) {
        String sha = this.getSha(name);
        return MyUtils.blobDirGetCont(sha);
    }

    void save() {
        writeObject(join(COMMIT_DIR, sha1(this)), this);
    }




    void printThisLog() {
        System.out.println("===");
        System.out.println("commit " + sha1(serialize(this)));
        // Adjust the format to remove timezone offset, and match the required format
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.ENGLISH);
        System.out.println("Date: " + dateFormat.format(this.time));  // Prints without timezone offset
        System.out.println(this.message);
        System.out.println("");
    }

    void printLogFromThis() {
        this.printThisLog();
        if (this.parent != null) {
            this.parent.printLogFromThis();
        }
    }




}
