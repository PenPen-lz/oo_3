package src;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        //while (scanner.hasNextLine()) {
        try {
            String originExpression = scanner.nextLine();

            //String originExpression = args[0];
            StringRecord strRecord = new StringRecord(originExpression);
            Expression expression = Factory.createExpression(strRecord);
            //System.out.println(expression.toString());
            System.out.println(expression.diff().toString());
        } catch (RFormatException e) {
            System.out.println("WRONG FORMAT!");
        }
    }
}
// }
