package gitlet;

import java.io.File;

import static gitlet.Repository.*;
import static gitlet.Utils.*;
import static gitlet.HeadOrMaster.*;


import static gitlet.Repository.GITLET_DIR;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ...
     */
    public static void main(String[] args) {
        if (args == null) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String firstArg = args[0];
        switch (firstArg) {
            case "init":
                validArg(args, 1);
                if (GITLET_DIR.exists()) {
                    System.out.println("A Gitlet version-control system already exists in the current directory.");
                } else {
                    Repository.setDirs();
                    Commit initialCommit = new Commit("initial commit", null);
                    DirUtils.writeGivenObjInGivenDir(initialCommit, COMMITS);
                    headIt(initialCommit);
                    MasterIt(initialCommit);
                }
                break;

            case "add":
                validArg(args, 2);
                String fileName = args[1];
                File curVersionFullPath = join(CWD, fileName);
                if (!curVersionFullPath.exists()) {
                    System.out.println("File does not exist.");
                } else {
                    byte[] curContent = readContents(curVersionFullPath);
                    String shaOfCurVersion = sha1(curContent);
                    String shaOfPreVersion = getHeadCommit().findShaOfName(fileName);
                    if (shaOfCurVersion.equals(shaOfPreVersion)) {
                        DirUtils.tryRemoveGivenFileFromGivenDir(fileName, ADD_STAGE);
                    } else {
                        DirUtils.writeGivenContentInGivenDirWithName(curContent, ADD_STAGE, fileName);
                    }

                }
                break;
            case "commit":
                validArg(args, 2);
                String message = args[1];


                break;

            case "checkout":


                break;

            case "log":


        }
    }

    public static void validArg(String[] args, int n) {
        if (args.length != n) {
            throw new RuntimeException("Invalid number of arguments");
        }
    }

}





