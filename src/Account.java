import java.util.List;
import java.util.Scanner;

class User {
    public int id;
    public String username;
    public String email;
    public String password;
}

public class Account {
    public static User user = new User();
    public static boolean isUserLoggedIn = false;
    private static Scanner scanner = new Scanner(System.in);

    public static void login() {
        if (isUserLoggedIn) {
            System.out.println("Már be vagy jelentkezve!");
            Menu.showLoggedMenu();
            return;
        }

        String username, password;
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("--- Casino - Bejelentkezés ---");
            System.out.print("Kérem, adja meg a felhasználónevet: ");
            username = scanner.nextLine();

            System.out.print("Kérem, adja meg a jelszót: ");
            password = scanner.nextLine();

            System.out.println("Bejelentkezés folyamatban...");

            List<String[]> results = Database.executeQuery("select id, username, email, password from users where username = '" + username + "' and password = '" + password + "'");
            if (results.size() == 1) {
                isUserLoggedIn = true;
                System.out.println("Bejelentkezve, mint " + username);
                user.id = Integer.parseInt(results.get(0)[0]);
                user.username = results.get(0)[1];
                user.email = results.get(0)[2];
                user.password = results.get(0)[3];
                Menu.showLoggedMenu();
            } else {
                System.out.println("Sikertelen bejelentkezés! Megpróbálod újra? (Alapértelmezett: IGEN, kilépéshez írd be: NEM)");
                String answer = scanner.nextLine();
                if (answer.toLowerCase().equals("igen") || answer.equals("")) {
                    login();
                } else {
                    Menu.showAuthMenu();
                }
            }
        }
    }

    public static void register() {
        String username, password;
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("--- Casino - Regisztráció ---");
            System.out.print("Kérem, adja meg a felhasználónevet: ");
            username = scanner.nextLine();

            System.out.print("Kérem, adja meg a jelszót: ");
            password = scanner.nextLine();

            System.out.println("Regisztráció folyamatban...");

            List<String[]> results = Database.executeQuery("select username from users where username = '" + username + "'");
            if (results.size() == 1) {
                System.out.println("A felhasználónév már foglalt! Megpróbálod újra? (Alapértelmezett: IGEN, kilépéshez írd be: NEM)");
                String answer = scanner.nextLine();
                if (answer.toLowerCase().equals("igen") || answer.equals("")) {
                    register();
                } else {
                    Menu.showAuthMenu();
                }
            } else {
                Database.executeUpdate("insert into users (username, password) values ('" + username + "', '" + password + "')");
                ClearConsole.clear();
                Menu.selectedIndex = 0;
                System.out.println("Sikeres regisztráció! Most kérlek jelentkezz be.");
                login();
            }
        }
    }

    public static void printUser() {
        System.out.println("[ Wolimby City - Profil ]");
        System.out.println("ID: " + user.id);
        System.out.println("Felhasználónév: " + user.username);
        System.out.println("Email: " + user.email);
        System.out.println("Jelszó: " + user.password);
        System.out.println("\nNyomd meg az Entert a folytatáshoz...");
        scanner.nextLine();
    }

    public static void logout() {
        System.out.println("Sikeres kijelentkezés!");
        isUserLoggedIn = false;
        Menu.firstRun = true;
        Menu.selectedIndex = 0;
        user = new User();
        Menu.showAuthMenu();
    }
}