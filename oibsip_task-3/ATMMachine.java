import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ATMMachine {
    //method to read the account data
    private static TreeMap<String, String> readAccountData(String username) throws IOException {
        Map<String, String> data = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("accounts/" + username + ".txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                data.put(parts[0], parts[1]);
            }
        }

        return new TreeMap<>(data);
    }

    //method to write in the account data
    private static void writeAccountData(String username, Map<String, String> data) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter("accounts/" + username + ".txt"))) {
            for (Entry<String, String> entry : data.entrySet()) {
                writer.println(entry.getKey().toString() + "," + entry.getValue().toString());
            }
        }
    }

    public static class TransactionHistory {

        //method to get the whole transaction history of user
        public void getHistory(String username) {
            try {
                BufferedReader r = new BufferedReader(new FileReader("accounts//" + username + ".txt"));
                String line;
                while ((line = r.readLine()) != null) {
                    System.out.println(line);
                }
                r.close();
            } catch (Exception e) {
                e.getMessage();
            }
        }
    }

    public static class withdraw {

        //method withdraw money from user account
        public void doWithdraw(String username, long amount) throws InsufficientFundsException, IOException {
            Map<String, String> accountData = readAccountData(username);

            long balance = Long.parseLong(accountData.get("balance"));
            if (balance < amount) {
                throw new InsufficientFundsException("Balance is lower than requested amount.");
            }

            balance -= amount;
            accountData.put("balance", Long.toString(balance));
            accountData.put(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                    "Debited - " + amount);

            writeAccountData(username, accountData);
            //System.out.println("Withdrawal successful!");
        }

    }
    //Insufficient balance exception handler
    static class InsufficientFundsException extends Exception {
        public InsufficientFundsException(String message) {
            super(message);
        }
    }

    public static class Deposite {

        //method to deposit amount to the user account
        public void add(String username, Long amount) throws IOException {
            try {
                Map<String, String> accountData = readAccountData(username);
                long balance = Long.parseLong(accountData.get("balance"));

                balance += amount;
                accountData.put("balance", Long.toString(balance));
                accountData.put(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                        "Credited - " + amount);

                writeAccountData(username, accountData);
                //System.out.println("Deposited Succesfully!!!");
            } catch (Exception e) {
                e.getMessage();
            }
        }
    }

    public static class Transfer {

        //method to transer amount from uname1 to uname2
        public void trans(String uname1, String uname2, Long amount) throws IOException {
            try {
                Deposite d=new Deposite();
                d.add(uname2, amount);
                withdraw w=new withdraw();
                w.doWithdraw(uname1, amount);
            } catch (Exception e) {
                e.getMessage();
            }
        }
    }
    
    //Authenthicate the user with its username and password
    public static boolean checkLogin(String uname, String password) throws IOException {
        try {
            Map<String, String> map = new HashMap<>();
            BufferedReader reader = new BufferedReader(new FileReader("data.txt"));
            String line;
            while ((line = reader.readLine()) != null) {

                String[] parts = line.split(":");
                String key = parts[0];
                String value = parts[1];
                map.put(key, value);
            }
            reader.close();
            if (map.get(uname).equals(password)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.getMessage();
            return false;
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("---------Welcome to our ATM.----------");
        Scanner sc = new Scanner(System.in);
        start: {
            System.out.print("Enter your username : ");
            String username = sc.nextLine();
            System.out.print("Enter your password : ");
            String password = sc.nextLine();
            while (checkLogin(username, password)) {
                System.out.println(
                        "Enter the number correspounding to the operation that you want to perform:\n(1) Check Transaction History \n(2) Withdraw Money \n(3) Deposite Money \n(4) Transfer Money \n(5) Quit");
                int choice = sc.nextInt();
                if (choice == 5) {
                    break;
                }
                switch (choice) {
                    case 1:
                        TransactionHistory th = new TransactionHistory();
                        th.getHistory(username);
                        break;

                    case 2:
                        System.out.println("Enter the amount to be withdrawed : ");
                        long amount = sc.nextLong();
                        withdraw wd = new withdraw();
                        try {
                            wd.doWithdraw(username, amount);
                        } catch (ATMMachine.InsufficientFundsException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;

                    case 3:
                        System.out.println("Enter the money to be deposited : ");
                        long amt = sc.nextLong();
                        Deposite ds = new Deposite();
                        try {
                            ds.add(username, amt);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;

                    case 4:
                        System.out.print("Enter the username of recevier : ");
                        String uname = sc.next();
                        System.out.print("Enter the amount to be transfered : ");
                        long money = sc.nextLong();
                        Transfer tr = new Transfer();
                        try {
                            tr.trans(username, uname, money);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;

                    default:
                        System.out.println("Enter a valid input.");
                        break;
                }
            }
            if (!checkLogin(username, password)) {
                System.out.println("Enter a valid username and password.");
                break start;
            }
        }
        sc.close();
    }
}
