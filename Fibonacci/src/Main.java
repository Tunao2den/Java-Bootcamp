import java.util.Scanner;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
        public static void main(String[] args) {
            Scanner scan = new Scanner(System.in);
            int bound;
            System.out.println("Enter the how many iteration will execute: ");
            bound=scan.nextInt();
            for(int i=0;i<bound;i++){
            System.out.print(fib(i) + ", ");
        }
    }

    public static int fib(int i){
        if(i == 0){
            return 0;
        } else if (i==1 || i==2) {
            return 1;
        } else {
            return fib(i-2)+ fib(i-1);
        }
    }
}
