package gitlet;

import java.io.File;
import java.util.HashMap;
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

    public static final File COMMIT_DIR = join(GITLET_DIR, "commit_dir");


    public static final File ADD_STAGE = join(GITLET_DIR, "add_stage");
    public static final File DEL_SET = join(GITLET_DIR, "del_list");

    public static final File BLOB_DIR = join(GITLET_DIR, "blobs");

    public static final File HEAD_BRANCH = join(GITLET_DIR, "head_branch");
    public static final File BRANCH_MAP = join(GITLET_DIR, "branch_map");


    // this is function that sets up files and dirs;

    static void setDirs() {
        try {
            GITLET_DIR.mkdir();
            COMMIT_DIR.mkdir();
            ADD_STAGE.mkdir();
            DEL_SET.createNewFile();
            writeObject(DEL_SET, new HashSet<String>());
            BLOB_DIR.mkdir();
            HEAD_BRANCH.createNewFile();
            BRANCH_MAP.createNewFile();
            writeObject(BRANCH_MAP, new HashMap<String, Commit>());


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static void checkOutFile(String name, Commit commit) {

        if (!commit.fileSet().contains(name)) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
        byte[] cont = commit.getFileContent(name);
        writeContents(join(CWD, name), cont);

    }

    static void checkOutCommit(Commit newHead) {
        Commit oldHead = MyUtils.getHeadCommit();
       Set<String> filesInNew = newHead.fileSet();
       Set<String> filesInOld = oldHead.fileSet();
       Set<String> filesInCWD = MyUtils.filesInDir(CWD);

       for (String file: filesInCWD) {
           if (!filesInOld.contains(file) && filesInNew.contains(file) && !sha1(MyUtils.readInFileNameCont(CWD, file)).equals(sha1(newHead.getFileContent(file)))) {
               System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
               System.exit(0);
           }
       }

       for (String file: filesInOld) {
           join(CWD, file).delete();
       }

       for (String file: filesInNew) {
           byte[] content = newHead.getFileContent(file);
           MyUtils.saveInFileNameCont(CWD, file, content);
       }

       MyUtils.addStageClear();
       MyUtils.delSetClear();

       MyUtils.updateBranchWithHead(MyUtils.getHeadBranchName(), newHead);



    }










}











