package gitlet;

import java.io.File;


import java.util.HashSet;

import java.util.List;
import java.util.Set;

import static gitlet.Repository.*;
import static gitlet.Utils.*;


import static gitlet.Repository.GITLET_DIR;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ...
     */
    public static void main(String[] args) {

        if (args == null || args.length == 0) {
            System.out.println("Please enter a command.");
            return;
        }
        Set<String> set = new HashSet<>(List.of("add", "commit", "rm", "log", "global-log", "status", "find", "checkout", "branch", "rm-branch", "reset", "merge"));
        if (set.contains(args[0]) && (!GITLET_DIR.exists())) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }

        switch(args[0]) {

            case "init":
                validArgs(args, 1);
                if (GITLET_DIR.exists()) {
                    System.out.println("A Gitlet version-control system already exists in the current directory.");
                    System.exit(0);

                }
                setDirs();
                Commit initialCommit = new Commit("initial commit", null);
                initialCommit.save();
                MyUtils.makeBranchWithHead("master", initialCommit);
                MyUtils.setHeadBranchWithName("master");
                break;


            case "add":
                validArgs(args, 2);
                String name = args[1];
                File fileInCWD = join(CWD, name);

                if (!fileInCWD.exists()) {
                    System.out.println("File does not exist.");
                    System.exit(0);
                }

                byte[] curCont = readContents(fileInCWD);
                Commit headCommit = MyUtils.getHeadCommit();
                if (headCommit.fileSet().contains(name) && sha1(curCont).equals(sha1(headCommit.getFileContent(name)))) {
                    AddStage.tryRemove(name);
                } else {
                    AddStage.putNameCont(name, curCont);
                }

                DelSet.tryRemove(name);
                break;

            case "rm":
                validArgs(args, 2);
                int count = 0;

                if (AddStage.setOfFileNames().contains(args[1])) {
                    AddStage.tryRemove(args[1]);
                    count += 1;
                }
                if (MyUtils.getHeadCommit().fileSet().contains(args[1])) {
                    DelSet.add(args[1]);
                    join(CWD, args[1]).delete();
                    count += 1;
                }
                if (count == 0) {
                    System.out.println("No reason to remove the file.");
                }

                break;

            case "commit":
                if (AddStage.setOfFileNames().size() + DelSet.setOfFileNames().size() == 0) {
                    System.out.println("No changes added to the commit.");
                    System.exit(0);
                }
                validArgs(args,2);
                if (args[1].equals("")) {
                    System.out.println("Please enter a commit message.");
                    System.exit(0);
                }

                Commit oldHeadComm = MyUtils.getHeadCommit();
                Commit newHeadComm = new Commit(args[1], oldHeadComm);

                for (String name1: oldHeadComm.fileSet()) {
                    newHeadComm.putNameSha(name1, oldHeadComm.getSha(name1));
                }

                for (String name2: AddStage.setOfFileNames()) {
                    byte[] content = AddStage.getContent(name2);
                    MyUtils.blobDirTryAddCont(content);
                    newHeadComm.putNameSha(name2, sha1(content));
                }

                for (String name3: DelSet.setOfFileNames()) {
                    newHeadComm.tryRemove(name3);
                }

                newHeadComm.save();
                MyUtils.updateBranchWithHead(MyUtils.getHeadBranchName(), newHeadComm);

                AddStage.clear();
                DelSet.clear();
                break;

            case "checkout":
                if (args.length == 3 && args[1].equals("--")) {
                    checkOutFile(args[2], MyUtils.getHeadCommit());

                } else if (args.length == 4 && args[2].equals("--")) {
                    String matchedID = matchByPrefix(MyUtils.getCommitIDs(), args[1]);
                    checkOutFile(args[3], MyUtils.getCommitFromID(matchedID));

                } else if (args.length == 2) {
                    String branchName = args[1];
                    if (!MyUtils.getBranches().contains(branchName)) {
                        System.out.println("No such branch exists.");
                        System.exit(0);
                    }
                    if (MyUtils.getHeadBranchName().equals(branchName)) {
                        System.out.println("No need to checkout the current branch.");
                        System.exit(0);
                    }
                    checkOutCommit(MyUtils.getBranchHead(branchName));
                    MyUtils.setHeadBranchWithName(branchName);
                } else {
                    System.out.println("Incorrect operands.");
                }
                break;

            case "reset":
                validArgs(args, 2);
                String matchedID = matchByPrefix(MyUtils.getCommitIDs(), args[1]);
                Commit commit = MyUtils.getCommitFromID(matchedID);
                checkOutCommit(commit);
                MyUtils.updateBranchWithHead(MyUtils.getHeadBranchName(), commit);
                break;

            case "branch":
                validArgs(args, 2);
                MyUtils.makeBranchWithHead(args[1], MyUtils.getHeadCommit());
                break;

            case "rm-branch":
                validArgs(args, 2);
                if (!MyUtils.getBranches().contains(args[1])) {
                    System.out.println("A branch with that name does not exist.");
                    System.exit(0);
                }
                if (MyUtils.getHeadBranchName().equals(args[1])) {
                    System.out.println("Cannot remove the current branch.");
                    System.exit(0);
                }
                MyUtils.rmBranch(args[1]);
                break;


            case "log":
                validArgs(args, 1);
                MyUtils.getHeadCommit().printLogFromThis();
                break;

            case "global-log":
                validArgs(args, 1);
                for (String ID: MyUtils.filesInDir(COMMIT_DIR)) {
                    MyUtils.getCommitFromID(ID).printThisLog();
                }
                break;

            case "find":
                validArgs(args, 2);
                String searchedMessage = args[1];
                int matchedCount = 0;
                for (String ID: MyUtils.getCommitIDs()) {
                    Commit cm = MyUtils.getCommitFromID(ID);
                    if (cm.message.contains(searchedMessage)) {
                        System.out.println(ID);
                        matchedCount += 1;
                    }
                }
                if (matchedCount == 0) {
                    System.out.println("Found no commit with that message.");
                }

                break;

            case "status":
                validArgs(args, 1);

                System.out.println("=== Branches ===");
                printBranchInOrder();
                System.out.println("");



                System.out.println("=== Staged Files ===");
                printSetInOrder(AddStage.setOfFileNames());
                System.out.println("");




                System.out.println("=== Removed Files ===");
                printSetInOrder(DelSet.setOfFileNames());
                System.out.println("");




                System.out.println("=== Modifications Not Staged For Commit ===");
                System.out.println("");

                System.out.println("=== Untracked Files ===");
                System.out.println("");

                break;









            default:
                System.out.println("No command with that name exists.");
                System.exit(0);
        }

    }




    static void validArgs(String[] args, int n) {
        if (args.length != n) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
    }



    static String matchByPrefix (Set<String> set, String prefix) {
        Set<String> matchSet = new HashSet<>();
        for (String str : set) {
            if (str.startsWith(prefix)) {
                matchSet.add(str);
            }
        }
        if (matchSet.size() == 0) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        } else if (matchSet.size() >= 2) {
            System.out.println("Too many commits with that id exists.");
            System.exit(0);
        } else {
            String element = matchSet.iterator().next(); // 获取唯一的元素
            return element;
        }
        return null;
    }





}