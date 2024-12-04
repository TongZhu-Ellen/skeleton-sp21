package gitlet;


import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

    static void printBranchInOrder() {
        String headBranch = getHeadBranch();
        List<String> list = new ArrayList<>(DirUtils.helpFindRelPathSetInGivenDir(BRANCHES)); // 将 Set 转换为 List
        Collections.sort(list); // 按字典顺序排序
        // 输出排序后的 List
        for (String s : list) {
            if (s.equals(headBranch)) {
                System.out.println("*" + s);
            } else {
                System.out.println(s);
            }
        }
    }




}
