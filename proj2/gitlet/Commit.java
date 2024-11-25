package gitlet;



import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static gitlet.Repository.COMMITS_DIR;
import static gitlet.Utils.*;


/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {
    /**
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    private Date curDate;
    private String message; // message of commit;
    Commit parent1;
    Commit parent2 = null;
    Map<String, Blob> files;

    Commit(Date inputDate, String inputMessage, Commit inputParent1) {
        this.curDate = inputDate;
        this.message = inputMessage;
        this.parent1 = inputParent1;
    }

    Blob tryFindFile(String fileName) {
        return this.files.getOrDefault(fileName, null);
    }

    void save() throws IOException {
        String sha1 = sha1(this);
        File curFile = join(COMMITS_DIR, sha1);
        curFile.createNewFile();
        writeObject(curFile, this);

    }









}
