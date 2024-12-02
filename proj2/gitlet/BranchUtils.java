package gitlet;


import java.io.File;

import static gitlet.Repository.*;
import static gitlet.Utils.*;

public class BranchUtils {

    static Commit getHeadCommit() {

        String shaOfBranch = readContentsAsString(join(BRANCHES, getHeadBranch()));
        return (Commit) DirUtils.readGivenFileInGivenDir(shaOfBranch, COMMITS, Commit.class);
    }

    static String getHeadBranch() {
        return readContentsAsString(HEAD);
    }


    static void headThisBranch(String branchName) {
        writeContents(HEAD, branchName);
    }

    static void makeBranch(String branchName, Commit commit) {
        File branchWholeAddress = join(BRANCHES, branchName);
        if (branchWholeAddress.exists()) {
            throw new GitletException("A branch with that name already exists.");
        } else {
            try {
                branchWholeAddress.createNewFile();
            } catch (Exception ignore) {

            }
            writeContents(branchWholeAddress, commit.sha());
        }
    }

    static void updateBranch(String branchName, Commit commit) {
        File branchWholeAddress = join(BRANCHES, branchName);
        writeContents(branchWholeAddress, commit.sha());

    }

    static Commit findBranch(String branchName) {
        String shaOfCommit = readContentsAsString(join(BRANCHES, branchName));
        return (Commit) DirUtils.readGivenFileInGivenDir(shaOfCommit, COMMITS, Commit.class);
    }




}
