package gitlet;

// TODO: any imports you need here


import java.io.Serializable;
import java.text.SimpleDateFormat;

import java.util.Date; // TODO: You'll likely use this in this class
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static gitlet.Repository.BLOBS;
import static gitlet.Repository.COMMITS;
import static gitlet.Utils.*;


/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {
    /**
     *
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
    Map<String, String> nameShaMap;
    ; // maps from name to sha;

    Commit(String messageInput, String parentInput) {
        this.message = messageInput;
        this.parentSha = parentInput;
        if (this.parentSha == null) {
            this.timeStamp = new Date(0);
        } else {
            this.timeStamp = new Date();
        }
        this.nameShaMap = new HashMap<>();
    }

    byte[] getContent(String name) {
        String sha = this.nameShaMap.get(name);
        return DirUtils.readGivenFileInGivenDir(sha, BLOBS);
    }

    String getMessage() {
        return this.message;
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
            Commit parentCommit = (Commit) DirUtils.readGivenFileInGivenDir(this.parentSha, COMMITS, Commit.class);
            parentCommit.printLogFromThis();
        }
    }

    public String sha() {
        return sha1(serialize(this));
    }
}





