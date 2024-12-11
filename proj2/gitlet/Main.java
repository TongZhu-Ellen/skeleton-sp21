package gitlet;

import java.io.File;

import java.util.HashMap;
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

        if (args.length == 0) {
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
                if (sha1(curVerOfCont).equals(sha1(MyUtils.getHeadCommit().getFileContent(name)))) {
                    MyUtils.addStageTryRemove(name);
                } else {
                    MyUtils.addStageAddNameContent(name, curVerOfCont);
                }
                break;

            case "commit":
                if (MyUtils.addStageEmpty() && MyUtils.delListEmpty()) {
                    System.out.println("No changes added to the commit.");
                    System.exit(0);
                }
                validArgs(args,2);
                if (args[1].isEmpty()) {
                    System.out.println("Please enter a commit message.");
                    System.exit(0);
                }

                Commit oldHeadComm = MyUtils.getHeadCommit();
                Commit newHeadComm = new Commit(args[1], oldHeadComm);

                for (String name1: oldHeadComm.fileSet()) {
                    newHeadComm.putNameSha(name1, oldHeadComm.getSha(name1));
                }

                for (String name2: relPathSet(ADD_STAGE)) {
                    byte[] content = MyUtils.addStageGetContentFromName(name2);
                    MyUtils.blobDirAddCont(content);
                    newHeadComm.putNameSha(name2, sha1(content));
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

    static Set<String> relPathSet(File searchedDir) {
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