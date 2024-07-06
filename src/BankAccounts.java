import java.util.List;
import java.util.Scanner;

public class BankAccounts {
    private static Scanner scanner = new Scanner(System.in);

    public static String[] bankAccountsOfUser(int userid) {
        List<String[]> results = Database.executeQuery("select * from bankaccounts where userid = " + userid);

        if (results.size() == 0) {
            return new String[0];
        }

        String[] bankAccounts = new String[results.size()];
        int i = 0;
        for (String[] result : results) {
            bankAccounts[i] = result[0] + " | " + "$" + result[2];
            i++;
        }
        System.out.println(bankAccounts[0]);
        return bankAccounts;
    }

    public static void createBankAccount(int userid, int balance) {
        Database.executeUpdate("insert into bankaccounts (userid, balance) values (" + userid + ", " + balance + ")");
    }

    public static String[] getBankInformation(String bankAccount) {
        List<String[]> results = Database.executeQuery("select * from bankaccounts where id = '" + bankAccount + "'");
        if (results.size() == 0) {
            return new String[0];
        }
        return results.get(0);
    }

    public static void createNewBankAccount() {
        ClearConsole.clear();
        System.out.println("Új bankszámla létrehozása...");
        BankAccounts.createBankAccount(Account.user.id, 1000);
        Menu.bankOptions = BankAccounts.bankAccountsOfUser(Account.user.id);
        Menu.selectedIndex = 0;
        Menu.showBankAccountsMenu();
    }

    public static void printBankList() {
        System.out.println("Bankszámlák betöltése...");
        Menu.bankOptions = bankAccountsOfUser(Account.user.id);
        if (Menu.bankOptions.length == 0) {
            ClearConsole.clear();
            System.out.println("Nincs elérhető bankszámla. Létrehozás folyamatban...");
            createBankAccount(Account.user.id, 1000);
            Menu.bankOptions = bankAccountsOfUser(Account.user.id);

            ClearConsole.clear();
            printBankAccount(0);
        } else {
            Menu.selectedIndex = 0;
            Menu.showBankAccountsMenu();
        }
        ClearConsole.clear();
    }

    public static void printBankAccount(int index) {
        if (index == Menu.bankOptions.length + 1) {
            Menu.selectedIndex = 0;
            Menu.showLoggedMenu();
            return;
        }

        if (index >= 0 && index < Menu.bankOptions.length) {
            ClearConsole.clear();
            System.out.println("Betöltés...");
            String[] bankAccount = getBankInformation(Menu.bankOptions[index].split(" \\| ")[0]);
            ClearConsole.clear();
            System.out.println("[ Wolimby City Bank - Bankszámla ]");
            System.out.println("- Tulajdonos neve: " + Account.user.username);
            System.out.println("- Számlaszám: " + bankAccount[0]);
            System.out.println("- Egyenleg: $" + bankAccount[2]);
            System.out.println("\nNyomd meg az Entert a folytatáshoz...");
            scanner.nextLine();
            Menu.selectedIndex = 0;
            Menu.showLoggedMenu();
        }
    }

    public static void main(String[] args) {
        bankAccountsOfUser(0);
    }
}