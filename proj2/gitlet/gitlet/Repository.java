package gitlet;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

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
    // TODO: the 2 Files of XXX_STAGE is not implemented yet; I am not sure about what they should be;
    static void setDirs() {
        try {
            GITLET_DIR.mkdir();
            COMMITS.mkdir();
            HEAD.createNewFile();
            BRANCHES.mkdir();
            BLOBS.mkdir();
            ADD_STAGE.mkdir();
            DEL_SET.createNewFile();
            writeObject(DEL_SET, new HashSet<String>());
        } catch (Exception ignore) {

        }
    }

    static void checkOut(Commit goalCommit, String relPath) {
        File fileInCWD = join(CWD, relPath);
        String oldSha = goalCommit.tryFindShaOfGivenName(relPath);
        if (oldSha == null) {
            throw new GitletException("File does not exist in that commit.");
        }
        byte[] oldContent = DirUtils.readGivenFileInGivenDir(oldSha, BLOBS);
        writeContents(fileInCWD, oldContent);
    }



    static void helpCheckout(Commit commit) {
        Set<String> filesInCWD = DirUtils.helpFindRelPathSetInGivenDir(CWD);
        Set<String> filesInCommit = commit.nameShaMap.keySet();
        Set<String> untracked = new HashSet<>(filesInCWD);
        untracked.removeAll(filesInCommit);
        if (!untracked.isEmpty()) {
            throw new GitletException("There is an untracked file in the way; delete it, or add and commit it first.");
        }
        for (String name: filesInCommit) {
            byte[] content = commit.getContent(name);
            if (!filesInCWD.contains(name)) {
                try {
                    join(CWD, name).createNewFile();
                } catch (Exception ignore) {

                }
            }
            DirUtils.writeGivenContentInGivenDirWithName(content, CWD, name);
        }
        Set<String> filesInHeadButNotCommit = DirUtils.helpFindRelPathSetInGivenDir(HEAD);
        filesInHeadButNotCommit.removeAll(filesInCommit);
        Commit head = BranchUtils.getHeadCommit();
        for (String name: filesInHeadButNotCommit) {
            head.nameShaMap.remove(name);
        }

        BranchUtils.makeBranch(BranchUtils.getHeadBranch(), commit);
        DirUtils.clearDir(ADD_STAGE);
        DelSet.clear();

    }









}


