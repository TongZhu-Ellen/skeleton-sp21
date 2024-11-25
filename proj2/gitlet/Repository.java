package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     *
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File HEAD = join(GITLET_DIR, ".head");



    public static final File COMMITS_DIR = join(GITLET_DIR, "commits");
    public static final File BLOBS_DIR = join(GITLET_DIR, "blobs");


    // this makes gitlet-dir file in CWD,
    // makes commits-file in the previous dir,
    public static void makeDirs() throws IOException {
        if (!GITLET_DIR.exists()) {
            GITLET_DIR.mkdir();
        } else {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }
        COMMITS_DIR.mkdir();
        Date UnixEpoch  = new Date(0);
        Commit initialCommit = new Commit(UnixEpoch, "initial commit", null);
        initialCommit.save();
        HEAD.createNewFile();
        writeObject(HEAD, initialCommit);

        BLOBS_DIR.mkdir();

    }




}
