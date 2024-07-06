public class App {
    public static void main(String[] args) {
        try {
            while (true) {
                Menu.showAuthMenu();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}