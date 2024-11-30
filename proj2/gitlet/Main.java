package gitlet;

import java.io.File;
import java.util.Set;

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
                    masterIt(initialCommit);
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
                    String shaOfPreVersion = getHeadCommit().tryFindShaOfGivenName(fileName);
                    if (shaOfCurVersion.equals(shaOfPreVersion)) {
                        DirUtils.tryRemoveGivenFileFromGivenDir(fileName, ADD_STAGE);
                    } else {
                        DirUtils.writeGivenContentInGivenDirWithName(curContent, ADD_STAGE, fileName);
                    }
                }
                break;


            case "commit":
                if (ADD_STAGE.listFiles().length == 0) {
                    System.out.println("No changes added to the commit.");
                } else if (args.length == 1) {
                    System.out.println("Please enter a commit message.");
                } else {
                    String message = args[1];
                    Commit oldCommit = getHeadCommit();
                    Commit newCommit = new Commit(message, oldCommit.getParentSha());
                    for (String name : oldCommit.nameShaMap.keySet()) {
                        String sha = oldCommit.nameShaMap.get(name);
                        newCommit.nameShaMap.put(name, sha);
                    }
                    for (String name : DirUtils.helpFindRelPathSetInGivenDir(ADD_STAGE)) {
                        byte[] content = DirUtils.readGivenFileInGivenDir(name, ADD_STAGE);
                        String sha = sha1(content);
                        DirUtils.writeGivenContentInGivenDirWithName(content, BLOBS, sha);
                        newCommit.nameShaMap.put(name, sha);
                    }
                    DirUtils.writeGivenObjInGivenDir(newCommit, COMMITS);
                    headIt(newCommit);
                    masterIt(newCommit);
                    DirUtils.clearDir(ADD_STAGE);

                }
                break;

            case "checkout":
                if (args.length == 3) {
                    String relPath = args[2];
                    Repository.checkOut(getHeadCommit(), relPath);
                } else if (args.length == 4) {
                    String shortenedID = args[1];
                    String commitSha = matchCommitId(DirUtils.helpFindRelPathSetInGivenDir(COMMITS), shortenedID);
                    Commit goalCommit = (Commit) DirUtils.readGivenFileInGivenDir(commitSha, COMMITS, Commit.class);
                    Repository.checkOut(goalCommit, args[3]);
                }

                break;


            case "log":
                getHeadCommit().printLogFromThis();
                break;

        }
    }


        public static void validArg (String[]args,int n){
            if (args.length != n) {
                throw new RuntimeException("Invalid number of arguments");
            }
        }

        static String matchCommitId (Set < String > set, String prefix) {
            String matched = null;
            for (String str : set) {
                if (str.startsWith(prefix)) {
                    if (matched != null) {
                        // 如果已经找到一个匹配项，再找到一个，返回null
                        return null;
                    }
                    matched = str;
                }
            }

            if (matched == null) {
                System.out.println("No commit with that id exists.");
                System.exit(0);
            }

            return matched;

        }
}


