import java.io.File;

public class JavaDiffMain {

    public static void main(String [] args) {

        if (args.length != 2) {
            File f = new File("");
            System.out.println(f.getAbsolutePath());
            System.out.println("You need two arguments, the path to folder 1 and folder 2.");
            System.exit(0);
        }

        File folder1 = new File(args[0]);
        File folder2 = new File(args[1]);

        if (!(folder1.exists() && folder1.isDirectory())) {
            System.out.println("Folder " + args[0] + " does not exist or is not a folder");
        }

        if (!(folder2.exists() && folder2.isDirectory())) {
            System.out.println("Folder " + args[1] + " does not exist or is not a folder");
        }
    }
}
