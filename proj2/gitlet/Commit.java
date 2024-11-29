package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date; // TODO: You'll likely use this in this class
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static gitlet.Repository.*;
import static gitlet.Utils.*;
import static gitlet.Repository.COMMITS;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /**
     * The message of this Commit.
     */
    private String message;
    private Date timeStamp;
    private String parentSha;
    Map<String, String> files;
    ; // maps from name to sha;

    Commit(String messageInput, String parentInput) {
        this.message = messageInput;
        this.parentSha = parentInput;
        this.timeStamp = new Date();
        if (this.parentSha == null) {
            this.timeStamp = new Date(0);
        }
        this.files = new HashMap<>();
    }

    String getMessage() {
        return this.message;
    }

    Date getTimeStamp() {
        return this.timeStamp;
    }

    String getParent() {
        return this.parentSha;
    }

    // saves this particular Commit Obj in COMMITS, using its sha1 as index to find;
    void save() {
        try {
            File fileName = join(COMMITS, sha1(serialize(this)));
            fileName.createNewFile();
            writeObject(fileName, this);
        } catch (Exception ignore) {

        }
    }

    void headIt() {
        writeContents(HEAD, sha1(serialize(this)));
    }

    void masterIt() {
        writeObject(MASTER, sha1(serialize(this)));
    }

    void printThisLog() {
        System.out.println("===");
        System.out.println("commit " + sha1(serialize(this)));
        // Adjust the format to remove timezone offset, and match the required format
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.ENGLISH);
        System.out.println("Date: " + dateFormat.format(this.timeStamp));  // Prints without timezone offset
        System.out.println(this.message);
        System.out.println("");
    }

    void printLogFromThis() {
        this.printThisLog();
        if (this.parentSha != null) {
            Commit parCommit = getCommitFromSha(this.parentSha);
            parCommit.printLogFromThis();
        }
    }


}





