package gitlet;

import java.io.File;

import java.util.Set;

import static gitlet.Repository.*;
import static gitlet.Utils.*;
import static gitlet.BranchUtils.*;


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
                    setDirs();
                    Commit initialCommit = new Commit("initial commit", null);
                    DirUtils.writeGivenObjInGivenDir(initialCommit, COMMITS);
                    makeBranch("master", initialCommit);
                    headThisBranch("master");
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
                        DelSet.remove(fileName);

                    } else {
                        DirUtils.writeGivenContentInGivenDirWithName(curContent, ADD_STAGE, fileName);
                    }
                }

                break;


            case "commit":
                if (ADD_STAGE.listFiles().length == 0) {
                    System.out.println("No changes added to the commit.");
                    break;
                }
                validArg(args, 2);
                String msg = args[1];
                if (msg.isEmpty()) {
                    System.out.println("Please enter a commit message.");
                    break;
                } else {
                    String message = msg;
                    Commit oldCommit = getHeadCommit();
                    Commit newCommit = new Commit(message, oldCommit.sha());
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
                    for (String name : DelSet.getSet()) {
                        newCommit.nameShaMap.remove(name);
                    }
                    DirUtils.writeGivenObjInGivenDir(newCommit, COMMITS);
                    updateBranch(getHeadBranch(), newCommit);
                    DirUtils.clearDir(ADD_STAGE);
                    DelSet.clear();

                }
                break;

            case "checkout":
                if (args.length == 3) {
                    String relPath = args[2];
                    checkOut(getHeadCommit(), relPath);
                } else if (args.length == 4) {
                    String shortenedID = args[1];
                    String commitSha = matchCommitId(DirUtils.helpFindRelPathSetInGivenDir(COMMITS), shortenedID);
                    Commit goalCommit = (Commit) DirUtils.readGivenFileInGivenDir(commitSha, COMMITS, Commit.class);
                    checkOut(goalCommit, args[3]);
                } else {
                    validArg(args, 2);
                    String branchName = args[1];
                    File givenBranchFullPath = join(BRANCHES, branchName);
                    if (!givenBranchFullPath.exists()) {
                        System.out.println("No such branch exists.");
                        break;
                    } else if (branchName.equals(getHeadBranch())) {
                        System.out.println("No need to checkout the current branch.");
                        break;
                    }
                    Commit commit = BranchUtils.findBranch(branchName);
                    Repository.helpCheckout(commit);




                    DirUtils.clearDir(ADD_STAGE);
                    DelSet.clear();

                }


                break;


            case "reset":
                validArg(args, 2);
                String shortenedCommitID = args[1];
                String commitId = matchCommitId(DirUtils.helpFindRelPathSetInGivenDir(COMMITS), shortenedCommitID);
                Commit commit = (Commit) DirUtils.readGivenFileInGivenDir(commitId, COMMITS, Commit.class);
                Repository.helpCheckout(commit);


                DirUtils.clearDir(ADD_STAGE);
                DelSet.clear();
                break;


            case "log":
                getHeadCommit().printLogFromThis();
                break;

            case "rm":
                validArg(args, 2);
                int actionCount = 0;
                String name = args[1];
                File nameInCWD = join(CWD, name);

                if (DirUtils.tryRemoveGivenFileFromGivenDir(name, ADD_STAGE)) {
                    actionCount += 1;
                }
                if (getHeadCommit().tryFindShaOfGivenName(name) != null) {
                    DelSet.add(name);
                    nameInCWD.delete();
                    actionCount += 1;
                }
                if (actionCount == 0) {
                    System.out.println("No reason to remove the file.");
                }
                break;

            case "branch":
                validArg(args, 2);
                makeBranch(args[1], getHeadCommit());

                break;

            case "rm-branch":
                validArg(args, 2);
                if (args[1].equals(getHeadBranch())) {
                    System.out.println("Cannot remove the current branch.");

                }
                if (!DirUtils.tryRemoveGivenFileFromGivenDir(args[1], BRANCHES)) {
                    System.out.println("A branch with that name does not exist.");
                }

                break;

            case "status":
                validArg(args, 1);
                System.out.println("=== Branches ===");
                printHeadBranch();
                printOtherBranch();
                System.out.println("");

                System.out.println("=== Staged Files ===");
                for (String stagedFile : DirUtils.helpFindRelPathSetInGivenDir(ADD_STAGE)) {
                    System.out.println(stagedFile);
                }
                System.out.println("");

                System.out.println("=== Removed Files ===");
                for (String toBeDelFile : DelSet.getSet()) {
                    System.out.println(toBeDelFile);
                }
                System.out.println("");

                System.out.println("=== Modifications Not Staged For Commit ===");
                System.out.println("");

                System.out.println("=== Untracked Files ===");
                System.out.println("");

                break;

            case "global-log":
                for (String commitRelPath : DirUtils.helpFindRelPathSetInGivenDir(COMMITS)) {
                    Commit curCommit = (Commit) DirUtils.readGivenFileInGivenDir(commitRelPath, COMMITS, Commit.class);
                    curCommit.printThisLog();
                }
                break;

            case "find":
                validArg(args, 2);
                String searchedMessage = args[1];
                int matchedCount = 0;
                for (String commitRelPath : DirUtils.helpFindRelPathSetInGivenDir(COMMITS)) {
                    Commit targetCommit = (Commit) DirUtils.readGivenFileInGivenDir(commitRelPath, COMMITS, Commit.class);
                    String commitMsg = targetCommit.getMessage();
                    if (commitMsg.contains(searchedMessage)) {
                        System.out.println(targetCommit.sha());
                        matchedCount += 1;
                    }
                }
                if (matchedCount == 0) {
                    System.out.println("Found no commit with that message.");
                }

                break;


        }
    }


        static void validArg (String[]args,int n){
            if (args.length != n) {
                throw new RuntimeException("Invalid number of arguments");
            }
        }

        static String matchCommitId (Set < String > set, String prefix){
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





