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






    // ADD_STAGE


    static void addStageAddNameContent(String name, byte[] content) {
        saveInFileNameCont(ADD_STAGE, name, content);
    }

    static byte[] addStageGetContentFromName(String name) {
        return readInFileNameCont(ADD_STAGE, name);
    }

    static Set<String> getAddStage() {
        return filesInDir(ADD_STAGE);
    }


    static void addStageTryRemove(String name) {
        join(ADD_STAGE, name).delete();
    }

    static boolean addStageEmpty() {
        File[] files = ADD_STAGE.listFiles();
        return files.length == 0;
    }

    static void addStageClear() {
        File[] files = ADD_STAGE.listFiles();
        for (File file: files) {
            file.delete();
        }
    }



    // DEL_Set



    static void delSetAddName(String name) {
        HashSet<String> del_set = readObject(DEL_SET, HashSet.class);
        del_set.add(name);
        writeObject(DEL_SET, del_set);
    }

    static Set<String> getDelSet() {
        return readObject(DEL_SET, HashSet.class);
    }


    static void delListClear() {
        writeObject(DEL_SET, new HashSet<String>());
    }


    // BLOB_DIR


    static void blobDirAddCont(byte[] content) {
        saveInFileNameCont(BLOB_DIR, sha1(content), content);
    }

    static byte[] blobDirGetCont(String sha) {
        return readInFileNameCont(BLOB_DIR, sha);
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

    static Commit getBranchHead(String branchName) {
        HashMap<String, Commit> branchMap = readObject(BRANCH_MAP, HashMap.class);
        return branchMap.get(branchName);
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
