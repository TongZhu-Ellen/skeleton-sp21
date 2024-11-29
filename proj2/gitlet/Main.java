package gitlet;

import java.io.File;
import java.io.IOException;
import gitlet.Utils.*;

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
                    initialCommit.save();
                    initialCommit.headIt();
                    initialCommit.masterIt();
                }
                break;
            case "add":
                validArg(args, 2);
                File targetFile = join(CWD, args[1]);
                if (!targetFile.exists()) {
                    System.out.println("File does not exist.");
                } else {
                    byte[] content = readContents(targetFile);
                    String sha = sha1(content);
                    if (Repository.fileChanged(args[1], sha)) {
                        File fileInAddStage = join(ADD_STAGE, args[1]);
                        if (!fileInAddStage.exists()) {
                            try {
                                fileInAddStage.createNewFile();
                            } catch (Exception ignore) {
                            }
                            writeContents(fileInAddStage, content);
                        }
                    }
                }
                break;
            case "commit":
                if (ADD_STAGE.listFiles().length == 0) {
                    System.out.println("No changes added to the commit.");
                    System.exit(0);
                }
                if (args.length == 1) {
                    System.out.println("Please enter a commit message.");
                    System.exit(0);
                }
                validArg(args, 2);
                Repository.commit(args[1]);
                break;

            case "checkout":
                if (args.length == 3) {
                    Repository.checkout(getHeadCommit(), args[2]);
                } else if (args.length == 4) {
                    String completeSha = Repository.findUniqueMatch(shasInCommitsDir(), args[1]);
                    Repository.checkout(Repository.getCommitFromSha(completeSha), args[3]);
                }

                break;


        }
    }

    public static void validArg(String[] args, int n) {
        if (args.length != n) {
            throw new RuntimeException("Invalid number of arguments");
        }
    }

}





