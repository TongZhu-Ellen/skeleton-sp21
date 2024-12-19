package gitlet;

import java.io.File;
import java.util.*;
import java.util.Objects;



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
    public static final File DEL_SET = join(GITLET_DIR, "del_set");

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
        byte[] cont = commit.tryGetContent(name);
        writeContents(join(CWD, name), cont);

    }



    static void checkOutCommit(Commit newHead) {
        Commit oldHead = MyUtils.getHeadCommit();
       Set<String> filesInNew = newHead.fileSet();
       Set<String> filesInOld = oldHead.fileSet();
        Set<String> filesInCWD = MyUtils.filesInDir(CWD);

        for (String file: filesInCWD) {
            if (!filesInOld.contains(file) && filesInNew.contains(file) && !sha1(MyUtils.readInFileNameCont(CWD, file)).equals(sha1(newHead.tryGetContent(file)))) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                System.exit(0);
            }
        }



       for (String file: filesInOld) {
           join(CWD, file).delete();
       }

       for (String file: filesInNew) {
           byte[] content = newHead.tryGetContent(file);
           MyUtils.saveInFileNameCont(CWD, file, content);
       }

       AddStage.clear();
       DelSet.clear();


    }


    static Commit splitPoint(Commit c1, Commit c2) {
        List<Commit> parent1 = c1.ancestersList();
        Set<Commit> parent2 = new HashSet<>(c2.ancestersList());
        for (Commit a1: parent1) {
            if (parent2.contains(a1)) {
                return a1;
            }
        }
        throw new GitletException("These two Commits have no common ancestors?!!");
    }


    static void printSetInOrder(Set<String> set) {
        List<String> list = new ArrayList<>(set); // 将 Set 转换为 List
        Collections.sort(list); // 按字典顺序排序
        // 输出排序后的 List
        for (String s : list) {
            System.out.println(s);
        }
    }


    static void printBranchInOrder() {
        String headBranch = MyUtils.getHeadBranchName();
        List<String> list = new ArrayList<>(MyUtils.getBranches()); // 将 Set 转换为 List
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




    static void helpMerge(Commit givenBranch, Commit curBranch, Commit comAn, String givenBranchName) {


        Set<String> useGiven = new HashSet<>();
        Set<String> conflict = new HashSet<>();

        for (String file: comAn.fileSet()) {
            if (comAn.tryGetSha(file).equals(curBranch.tryGetSha(file)) && !comAn.tryGetSha(file).equals(givenBranch.tryGetSha(file))) {
                useGiven.add(file);
            }
        } // this handles No.1 and 6;

        for (String file: givenBranch.fileSet()) {
            if (!comAn.contains(file) && !curBranch.contains(file)) {
                useGiven.add(file);
            }
        } // this handles No.5;

        Set<String> inEither = new HashSet<>(comAn.fileSet());
        inEither.addAll(curBranch.fileSet());
        inEither.addAll(givenBranch.fileSet());

        for (String file: inEither) {
            if (!Objects.equals(comAn.tryGetSha(file), curBranch.tryGetSha(file)) && !Objects.equals(comAn.tryGetSha(file), givenBranch.tryGetSha(file)) && !Objects.equals(curBranch.tryGetSha(file), givenBranch.tryGetSha(file))) {
                conflict.add(file);
            }
        } // this handles No.8;



        // check untracked;
        for (String file: useGiven) {
            if (join(CWD, file).exists() && !curBranch.contains(file)) { // then this is untracked;
                if (!sha1(readContents(join(CWD, file))).equals(givenBranch.tryGetSha(file))) {
                    System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                    System.exit(0);
                }
            }
        }

        for (String file: conflict) {
            if (join(CWD, file).exists() && !curBranch.contains(file)) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                System.exit(0);
            }
        }

        for (String file: useGiven) {
            if (givenBranch.contains(file)) {
                byte[] content = givenBranch.tryGetContent(file);
                writeContents(join(CWD, file), content);
                AddStage.putNameCont(file, content);
            } else {
                join(CWD, file).delete();
                DelSet.add(file);
            }
        }

        for (String file: conflict) {
            String str = "<<<<<<< HEAD\n" + curBranch.tryGetContentAsString(file) + "=======\n" + givenBranch.tryGetContentAsString(file) + ">>>>>>>\n";
            byte[] content = serialize(str);
            writeContents(join(CWD, file), content);
            AddStage.putNameCont(file, content);

        }












        Commit oldHeadComm = curBranch;
        Commit newHeadComm = new Commit("Merged " + givenBranchName + " into " + MyUtils.getHeadBranchName() + ".", oldHeadComm);
        newHeadComm.otherParent = givenBranch;


        for (String name1: oldHeadComm.fileSet()) {
            newHeadComm.putNameSha(name1, oldHeadComm.tryGetSha(name1));
        }

        for (String name2: AddStage.setOfFileNames()) {
            byte[] content = AddStage.getContent(name2);
            BlobDir.tryAddCont(content);
            newHeadComm.putNameSha(name2, sha1(content));
        }

        for (String name3: DelSet.setOfFileNames()) {
            newHeadComm.tryRemove(name3);
        }

        newHeadComm.save();
        MyUtils.updateBranchWithHead(MyUtils.getHeadBranchName(), newHeadComm);

        AddStage.clear();
        DelSet.clear();


        if (!conflict.isEmpty()) {
            System.out.println("Encountered a merge conflict.");
        }














    }










}











