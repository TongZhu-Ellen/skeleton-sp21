package gitlet;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static gitlet.BranchUtils.getHeadCommit;

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

    /**
     * The current working directory.
     */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /**
     * The .gitlet directory.
     */
    public static final File GITLET_DIR = join(CWD, ".gitlet");


    public static final File ADD_STAGE = join(GITLET_DIR, "stage_for_addition");
    public static final File DEL_SET = join(GITLET_DIR, "stage_for_deletion");

    public static final File COMMITS = join(GITLET_DIR, "commits");


    public static final File BRANCHES = join(GITLET_DIR, "branches");
    public static final File HEAD = join(GITLET_DIR, "head");



    public static final File BLOBS = join(GITLET_DIR, "blobs"); // this is a dir;


    // this is function that sets up files and dirs;

    static void setDirs() {
        try {
            GITLET_DIR.mkdir();
            COMMITS.mkdir();
            HEAD.createNewFile();
            BRANCHES.createNewFile();
            writeObject(BRANCHES, new HashMap<String, Commit>());
            BLOBS.mkdir();
            ADD_STAGE.mkdir();
            DEL_SET.createNewFile();
            writeObject(DEL_SET, new HashSet<String>());
        } catch (Exception ignore) {

        }
    }


    static void helpCheckoutSingleFileInGivenCommit(String name, Commit goalCommit) {
        Set<String> filesInGoalCommit = goalCommit.nameShaMap.keySet();
        if (!filesInGoalCommit.contains(name)) {
            System.out.println("File does not exist in that commit.");
            return;
        }
        byte[] content = goalCommit.getContent(name);
        DirUtils.writeGivenContentInGivenDirWithName(content, CWD, name);
    }

    static void helpCheckOutCommit(Commit newBranchHead) {
        Commit oldBranchHead = getHeadCommit();
        Set<String> filesInNewBranchHead = newBranchHead.nameShaMap.keySet();
        Set<String> filesInOldBranchHead = oldBranchHead.nameShaMap.keySet();
        Set<String> filesInCWD = DirUtils.helpFindRelPathSetInGivenDir(CWD);
        for (String name: filesInCWD) {
            if (!filesInOldBranchHead.contains(name) && newBranchHead.nameShaMap.containsKey(name) && !sha1(DirUtils.readGivenFileInGivenDir(name, CWD)).equals(sha1(newBranchHead.getContent(name)))) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                return;
            }
        }
        for (String name: filesInNewBranchHead) {
            byte[] content = newBranchHead.getContent(name);
            DirUtils.writeGivenContentInGivenDirWithName(content, CWD, name);
        }
        for (String name: filesInOldBranchHead) {
            if (!filesInNewBranchHead.contains(name)) {
                DirUtils.tryRemoveGivenFileFromGivenDir(name, CWD);
            }
        }
        DirUtils.clearDir(ADD_STAGE);
        DelSet.clear();
    }










}











