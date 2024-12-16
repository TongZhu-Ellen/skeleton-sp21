package gitlet;

import java.io.File;

import java.util.*;

import static gitlet.Repository.*;
import static gitlet.Utils.*;

class MyUtils {


    static void saveInFileNameCont(File dir, String name, byte[] content) {
        writeContents(join(dir, name), content);
    }

    static byte[] readInFileNameCont(File file, String name) {
        return readContents(join(file, name));
    }







    // HEAD_BRANCH

    static void setHeadBranchWithName(String branchName) {
        writeContents(HEAD_BRANCH, branchName);
    }

    static String getHeadBranchName() {
        return readContentsAsString(HEAD_BRANCH);
    }

    static Commit getHeadCommit() {
        String headBranch = getHeadBranchName();
        return getBranchHead(headBranch);
    }





    // BRANCH_MAP

    static void makeBranchWithHead(String branchName, Commit commit) {
        HashMap<String, Commit> branchMap = readObject(BRANCH_MAP, HashMap.class);
        if (branchMap.containsKey(branchName)) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }
        branchMap.put(branchName, commit);
        writeObject(BRANCH_MAP, branchMap);
    }

    static void updateBranchWithHead(String branchName, Commit commit) {
        HashMap<String, Commit> branchMap = readObject(BRANCH_MAP, HashMap.class);
        branchMap.put(branchName, commit);
        writeObject(BRANCH_MAP, branchMap);
    }

    static void rmBranch(String branchName) {
        HashMap<String, Commit> branchMap = readObject(BRANCH_MAP, HashMap.class);
        branchMap.remove(branchName);
        writeObject(BRANCH_MAP, branchMap);
    }

    static Commit getBranchHead(String branchName) {
        HashMap<String, Commit> branchMap = readObject(BRANCH_MAP, HashMap.class);
        if (!branchMap.containsKey(branchName)) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
            return null;
        } else {
            return branchMap.get(branchName);
        }
    }


    static Set<String> getBranches() {
        HashMap<String, Commit> branchMap = readObject(BRANCH_MAP, HashMap.class);
        return branchMap.keySet();
    }







    // COMMIT_DIR

    static Set<String> getCommitIDs() {
        return filesInDir(COMMIT_DIR);
    }

    static Commit getCommitFromID(String ID) {
        return readObject(join(COMMIT_DIR, ID), Commit.class);
    }









    static Set<String> filesInDir(File searchedDir) {
        Set<String> relativePathsSet = new HashSet<>();
        for (File file : searchedDir.listFiles()) {
            if (file.getName().equals(".gitlet")) {
                continue;
            }
            String relativePath = searchedDir.toPath().relativize(file.toPath()).toString();
            relativePathsSet.add(relativePath);  // 添加到 set，自动去重
        }
        return relativePathsSet;
    }




}
