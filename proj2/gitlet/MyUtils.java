package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static gitlet.Repository.*;
import static gitlet.Utils.*;

class MyUtils {


    static void saveInFileNameCont(File dir, String name, byte[] content) {
        writeContents(join(dir, name), content);
    }

    static byte[] readInFileNameCont(File file, String name) {
        return readContents(join(file, name));
    }

    static Object readInFileNameObj(File file, String name, Class type) {
        return readObject(join(file, name), type);
    }




    // ADD_STAGE


    static void addStageAddNameContent(String name, byte[] content) {
        saveInFileNameCont(ADD_STAGE, name, content);
    }

    static byte[] addStageGetContentFromName(String name) {
        return readInFileNameCont(ADD_STAGE, name);
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



    // DEL_LIST



    static void delListAddName(String name) {
        LinkedList<String> del_list = readObject(DEL_LIST, LinkedList.class);
        del_list.add(name);
        writeObject(DEL_LIST, del_list);
    }

    static List<String> delListGetList() {
        return readObject(DEL_LIST, LinkedList.class);
    }

    static boolean delListEmpty() {
        return delListGetList().size() == 0;
    }

    static void delListClear() {
        writeObject(DEL_LIST, new LinkedList<String>());
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













}
