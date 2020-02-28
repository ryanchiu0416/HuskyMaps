import java.util.ArrayList;

// Checkstyle will report an error with this line:

public class SanityCheck {
    public static void main(String[] args) {
        // The following line of code should compile only if you correctly installed Java 11 or higher.
        System.out.println("Java 11 or above is correctly installed: " + " ".isBlank());

        // The following four lines should run, but checkstyle should complain about style errors in each lines.
        // We use these to mark where we expect you to write code, so remember to remove them before submitting.
        ArrayList<String> a = new ArrayList<>();
        a.add("test"); // Deliberately long line: .......................................... end
        System.out.println(!a.isEmpty());

        // The following line should produce an error when not commented.
        // List l = new ArrayList();

        System.out.println("Sanity check complete: everything seems to have been configured correctly!");
    }
}
