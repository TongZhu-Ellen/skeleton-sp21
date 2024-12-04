package gitlet;


import java.io.File;
import java.util.Set;

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
            System.out.println("A branch with that name already exists.");
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

    static void printHeadBranch() {
        System.out.println("*" + getHeadBranch());
    }

    static void printOtherBranch() {
        Set<String> branchNames = DirUtils.helpFindRelPathSetInGivenDir(BRANCHES);
        branchNames.remove(getHeadBranch());
        for (String name: branchNames) {
            System.out.println(name);
        }
    }




}
