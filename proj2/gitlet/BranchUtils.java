package gitlet;


import java.io.File;

import static gitlet.Repository.*;
import static gitlet.Utils.*;

public class BranchUtils {

    static Commit getHeadCommit() {
        String branchName = readContentsAsString(HEAD);
        String shaOfBranch = (String) DirUtils.readGivenFileInGivenDir(branchName, BRANCHES, String.class);
        return (Commit) DirUtils.readGivenFileInGivenDir(shaOfBranch, COMMITS, Commit.class);
    }


    static void headThisBranch(String branchName) {
        writeContents(HEAD, branchName);
    }

    static void updateBranch(String branchName, Commit commit) {
        File branchWholeAddress = join(BRANCHES, branchName);
        if (branchWholeAddress.exists() && (!branchName.equals("master"))) {
            throw new GitletException("A branch with that name already exists.");
        } else {
            try {
                branchWholeAddress.createNewFile();
            } catch (Exception ignore) {

            }
        }
        writeContents(branchWholeAddress, commit.sha());
    }

    static Commit findBranch(String branchName) {
        String shaOfCommit = (String) DirUtils.readGivenFileInGivenDir(branchName, BRANCHES, String.class);
        return (Commit) DirUtils.readGivenFileInGivenDir(shaOfCommit, COMMITS, Commit.class);
    }




}
