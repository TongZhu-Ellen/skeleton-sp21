package gitlet;


import java.io.File;

import static gitlet.Repository.*;
import static gitlet.Utils.*;

public class HeadOrMaster {

    static Commit getHeadCommit() {
        String relPath = readContentsAsString(HEAD);
        File fullPath = join(COMMITS, relPath);
        return readObject(fullPath, Commit.class);
    }

    static Commit getMasterCommit() {
        String relPath = readContentsAsString(MASTER);
        File fullPath = join(COMMITS, relPath);
        return readObject(fullPath, Commit.class);
    }

    static void headIt(Commit commit) {
        writeContents(HEAD, commit.sha());
    }

    static void masterIt(Commit commit) {
        writeContents(MASTER, commit.sha());
    }




}
