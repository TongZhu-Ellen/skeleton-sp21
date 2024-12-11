package gitlet;

import java.io.File;


import java.util.HashSet;

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
            System.exit(0);
        }

        switch(args[0]) {

            case "init":
                validArgs(args, 1);
                if (GITLET_DIR.exists()) {
                    System.out.println("A Gitlet version-control system already exists in the current directory.");
                    System.exit(0);

                }
                Repository.setDirs();
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
                byte[] curVerOfCont = readContents(fileInCWD);

                if (MyUtils.getHeadCommit().containsNameContent(name, curVerOfCont)) {
                    MyUtils.addStageTryRemove(name);
                } else {
                    MyUtils.addStageAddNameContent(name, curVerOfCont);
                }
                break;

            case "commit":
                if (MyUtils.getAddStage().size() + MyUtils.getDelSet().size() == 0) {
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

                for (String name2: MyUtils.getAddStage()) {
                    byte[] content = MyUtils.addStageGetContentFromName(name2);
                    MyUtils.blobDirAddCont(content);
                    newHeadComm.putNameSha(name2, sha1(content));
                }

                MyUtils.addStageClear();
                break;

            case "checkout":
                if ((args.length == 3) && (args[1].equals("--"))) {
                    Repository.checkOutFile(args[2], MyUtils.getHeadCommit());
                    return;
                }
                if ((args.length == 4) && (args[2].equals("--"))) {
                    String matchedID = matchByPrefix(MyUtils.getCommitIDs(), args[1]);
                    Repository.checkOutFile(args[3], MyUtils.getCommitFromID(matchedID));
                }

                break;

            case "log":
                validArgs(args, 1);
                MyUtils.getHeadCommit().printLogFromThis();







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