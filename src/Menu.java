import java.util.Scanner;
import java.io.IOException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

public class Menu {
    private static final String[] firstRunOptions = {"Bejelentkezés", "Regisztráció", "Kilépés"};
    private static final String[] loggedOptions = {"Profilom", "Bankszámlák", "Kijelentkezés", "Kilépés"};
    public static String[] bankOptions = new String[0];
    public static int selectedIndex = 0;
    public static boolean firstRun = true;
    private static Scanner scanner = new Scanner(System.in);
    private static Terminal terminal;
    private static java.io.Reader reader;

    static {
        try {
            terminal = TerminalBuilder.builder().jna(true).system(true).build();
            terminal.enterRawMode();
            reader = terminal.reader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showAuthMenu() {
        while (!Account.isUserLoggedIn) {
            ClearConsole.clear();
            if (firstRun) {
                System.out.println("Üdvözöllek a Wolimby Casinoban! W-S-el tudsz választani, Enterrel tudsz választani, Q-vel tudsz kilépni.");
            }
            System.out.println("[ Wolimby City - Üdv ]");
            printFirstRunMenu();
    
            try {
                int read = reader.read();
                if (read == 13) {
                    selectOption(firstRunOptions);
                } else if (read == 119 || read == 107) {
                    moveSelectionUp(firstRunOptions);
                } else if (read == 115 || read == 100) {
                    moveSelectionDown(firstRunOptions);
                } else if (read == 113) {
                    selectedIndex = 2;
                    selectOption(firstRunOptions);
                } else if (read == 101) {
                    selectOption(firstRunOptions);
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void showLoggedMenu() {
        while (Account.isUserLoggedIn) {
            ClearConsole.clear();
            System.out.println("[ Wolimby City Fiók - Menü ]");
            printLoggedMenu();
    
            try {
                int read = reader.read();
                if (read == 13) {
                    selectOption(loggedOptions);
                } else if (read == 119 || read == 107) {
                    moveSelectionUp(loggedOptions);
                } else if (read == 115 || read == 100) {
                    moveSelectionDown(loggedOptions);
                } else if (read == 113) {
                    selectedIndex = 2;
                    selectOption(loggedOptions);
                } else if (read == 101) {
                    selectOption(loggedOptions);
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void showBankAccountsMenu() {
        while (Account.isUserLoggedIn) {
            ClearConsole.clear();
            System.out.println("[ Wolimby City Bank - Bankszámlák (" + (bankOptions.length) + "/3 max) ]");
            printBankAccountsMenu();

            try {
                int read = reader.read();
                if (read == 13) {
                    if (selectedIndex == bankOptions.length && bankOptions.length < 3) {
                        BankAccounts.createNewBankAccount();
                    } else if (selectedIndex == bankOptions.length + 1) {
                        selectedIndex = 0;
                        showLoggedMenu();
                        return;
                    } else {
                        BankAccounts.printBankAccount(selectedIndex);
                    }
                } else if (read == 119 || read == 107) {
                    moveSelectionUpWithNewOption();
                } else if (read == 115 || read == 100) {
                    moveSelectionDownWithNewOption();
                } else if (read == 113) {
                    if (selectedIndex == bankOptions.length && bankOptions.length < 3) {
                        BankAccounts.createNewBankAccount();
                    } else if (selectedIndex == bankOptions.length + 1) {
                        selectedIndex = 0;
                        showLoggedMenu();
                        return;
                    } else {
                        BankAccounts.printBankAccount(selectedIndex);
                    }
                } else if (read == 101) {
                    if (selectedIndex == bankOptions.length && bankOptions.length < 3) {
                        BankAccounts.createNewBankAccount();
                    } else if (selectedIndex == bankOptions.length + 1) {
                        selectedIndex = 0;
                        showLoggedMenu();
                        return;
                    } else {
                        BankAccounts.printBankAccount(selectedIndex);
                    }
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void printFirstRunMenu() {
        for (int i = 0; i < firstRunOptions.length; i++) {
            if (i == selectedIndex) {
                System.out.print("> ");
            } else {
                System.out.print("  ");
            }
            System.out.println(firstRunOptions[i]);
        }
    }

    private static void printLoggedMenu() {
        for (int i = 0; i < loggedOptions.length; i++) {
            if (i == selectedIndex) {
                System.out.print("> ");
            } else {
                System.out.print("  ");
            }
            System.out.println(loggedOptions[i]);
        }
    }

    private static void printBankAccountsMenu() {
        for (int i = 0; i < bankOptions.length; i++) {
            if (i == selectedIndex) {
                System.out.print("> ");
            } else {
                System.out.print("  ");
            }
            System.out.println(bankOptions[i]);
        }

        if (bankOptions.length < 3) {
            if (selectedIndex == bankOptions.length) {
                System.out.print("> ");
            } else {
                System.out.print("  ");
            }
            System.out.println("Új bankszámla létrehozása");
        }

        if (bankOptions.length >= 1) {
            if (selectedIndex == bankOptions.length + 1) {
                System.out.print("> ");
            } else {
                System.out.print("  ");
            }
            System.out.println("Vissza");
        }
    }

    private static void moveSelectionUp(String[] options) {
        selectedIndex = (selectedIndex - 1 + options.length) % options.length;
    }

    private static void moveSelectionDown(String[] options) {
        selectedIndex = (selectedIndex + 1) % options.length;
    }

    private static void moveSelectionUpWithNewOption() {
        int maxIndex = bankOptions.length < 3 ? bankOptions.length + 1 : bankOptions.length + 1;
        selectedIndex = (selectedIndex - 1 + maxIndex + 1) % (maxIndex + 1);
    }

    private static void moveSelectionDownWithNewOption() {
        int maxIndex = bankOptions.length < 3 ? bankOptions.length + 1 : bankOptions.length + 1;
        selectedIndex = (selectedIndex + 1) % (maxIndex + 1);
    }

    private static void selectOption(String[] options) {
        firstRun = false;
        ClearConsole.clear();
        switch (selectedIndex) {
            case 0:
                if (!Account.isUserLoggedIn) {
                    Account.login();
                } else {
                    Account.printUser();
                }
                break;
            case 1:
                if (!Account.isUserLoggedIn) {
                    Account.register();
                } else {
                    BankAccounts.printBankList();
                }
                break;
            case 2:
                if (!Account.isUserLoggedIn) {
                    ClearConsole.clear();
                    System.out.println("Viszlát!");
                    System.exit(0);
                } else {
                    Account.logout();
                }
            case 3:
                if (Account.isUserLoggedIn) {
                    ClearConsole.clear();
                    System.out.println("Viszlát!");
                    System.exit(0);
                }
                break;
            default:
                break;
        }
    }

    public static void closeScanner() {
        scanner.close();
    }
}
