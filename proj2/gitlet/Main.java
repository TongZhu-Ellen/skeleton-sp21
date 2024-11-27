package gitlet;

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
    public static void main(String[] args) throws IOException {
        if (args == null) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                if (GITLET_DIR.exists()) {
                    throw new GitletException("A Gitlet version-control system already exists in the current directory.");
                } else {
                    Repository.setDirs();
                    Commit initialCommit = new Commit("initial commit", null);
                    initialCommit.save();
                    initialCommit.headIt();
                    initialCommit.masterIt();

                }
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                break;
            // TODO: FILL THE REST IN
        }
    }
}
