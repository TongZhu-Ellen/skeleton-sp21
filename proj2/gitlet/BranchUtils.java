package gitlet;


import java.io.File;
import java.util.*;

import static gitlet.Repository.*;
import static gitlet.Utils.*;

public class BranchUtils {

    static Commit getHeadCommit() {
        String headBranch = getHeadBranch();
        HashMap<String, Commit> branchMap = readObject(BRANCHES, HashMap.class);
        return branchMap.get(headBranch);

    }

    static String getHeadBranch() {
        return readContentsAsString(HEAD);
    }


    static void headThisBranch(String branchName) {
        writeContents(HEAD, branchName);
    }

    static void makeBranch(String branchName, Commit commit) {
        HashMap<String, Commit> branchMap = readObject(BRANCHES, HashMap.class);
        if (branchMap.containsKey(branchName)) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        } else {
            branchMap.put(branchName, commit);
        }
        writeObject(BRANCHES, branchMap);
    }

    static void updateBranch(String branchName, Commit commit) {
        HashMap<String, Commit> branchMap = readObject(BRANCHES, HashMap.class);
        branchMap.put(branchName, commit);
        writeObject(BRANCHES, branchMap);
    }

    static Commit findBranch(String branchName) {
        HashMap<String, Commit> branchMap = readObject(BRANCHES, HashMap.class);
        return branchMap.get(branchName);
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
