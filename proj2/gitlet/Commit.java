package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date; // TODO: You'll likely use this in this class
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

    /** The message of this Commit. */
    private String message;
    private Date timeStamp;
    private String parentSha;
    Map<String, String> files; // what's in this commit? this is the map from name to blobSha;

    public Commit(String messageInput, String parentInput) {
        this.message = messageInput;
        this.parentSha = parentInput;
        if (this.parentSha == null) {
            this.timeStamp = new Date(0);
        }
    }

    public String getMessage() {
        return this.message;
    }

    public  Date getTimeStamp() {
        return this.timeStamp;
    }

    public String getParent() {
        return this.parentSha;
    }

    // saves this particular Commit Obj in COMMITS, using its sha1 as index to find;
    public void save() throws IOException {
        File fileName = join(COMMITS, sha1(this));
        fileName.createNewFile();
        writeObject(fileName, this);
    }

    public void headIt() {
        writeContents(HEAD, sha1(this));
    }

    public void masterIt() {
        writeObject(MASTER, sha1(this));
    }






}