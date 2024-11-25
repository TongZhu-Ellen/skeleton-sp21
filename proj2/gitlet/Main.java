package gitlet;

import java.io.File;
import java.io.IOException;

import static gitlet.Repository.helpAdd;
import static gitlet.Utils.*;


/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) throws IOException {
        // TODO: what if args is empty?
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                gitlet.Repository.makeDirs();
            case "add":
                File searchedFile = join(System.getProperty("user.dir"), args[1]); // global address;
                if (!searchedFile.exists()) {
                    System.out.println("File does not exist.");
                    System.exit(0);
                }


        }
    }
}
