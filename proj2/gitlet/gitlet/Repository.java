package gitlet;

import java.io.File;
import java.io.IOException;

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
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
        public static final File GITLET_DIR = join(CWD, ".gitlet");

            public static final File ADD_STAGE = join(GITLET_DIR, ".stage_for_addition");
            public static final File DEL_STAGE = join(GITLET_DIR, ".stage_for_deletion");

            public static final File COMMITS = join(GITLET_DIR, "commits"); // this is a file;
                public static final File HEAD = join(COMMITS, ".head");
                public static final File MASTER = join(COMMITS, ".master");

            public static final File BLOBS = join(GITLET_DIR, "blobs"); // this is a dir;


    // this is function that sets up files and dirs;
    // TODO: the 2 Files of XXX_STAGE is not implemented yet; I am not sure about what they should be;
    static void setDirs() throws IOException {
        GITLET_DIR.mkdir();
        COMMITS.createNewFile();
        HEAD.createNewFile();
        MASTER.createNewFile();
        BLOBS.mkdir();
    }
}