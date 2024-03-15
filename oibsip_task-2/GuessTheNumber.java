import java.util.Random;
import java.util.Scanner;

/**
 * GuessTheNumber
 */
public class GuessTheNumber {

    public static void main(String[] args) {
        game();
    }

    private static void game(){
        System.out.println("There are total 3 rounds to guess a number in which each round will have new random number to be guessed \nand in each round you are given 10 total attempts to guess that number correctly. \nNote: Score '-1' for any round means that round is not attempted.");
        int round=1,attempt=1;
        int score=0;
        int[] scoreboard=new int[3];
        for (int i = 0; i < scoreboard.length; i++) {
            scoreboard[i]=-1;
        }
        Random r=new Random();
        Scanner sc=new Scanner(System.in);
        while (round<=3) {
            int randomnum=r.nextInt(1, 1001);
            System.out.println("----------Round : "+round+"----------");
            int guessnum=0;
            while (attempt<=10) {
                System.out.println("Attempt : "+attempt);
                attempt++;
                System.out.print("Guess and enter a number between 1 to 1000 : ");
                guessnum=sc.nextInt();
                if (guessnum>1000 || guessnum<1) {
                    System.out.println("OOPS!!!!!enter the number in range of 1 to 1000");
                    attempt--;
                    continue;
                }
                if (guessnum==randomnum) {
                    score+=10;
                    System.out.println("Congratulatons,you have correctly gussed the number.");
                    break;
                }else if (guessnum<randomnum) {
                    System.out.println("Your guess number is smaller then the generated number");
                    if (randomnum-guessnum<=5) {
                        score+=2;
                    }else if (randomnum-guessnum<=10) {
                       score++; 
                    }
                }else if (guessnum>randomnum) {
                    System.out.println("Your guess number is larger then the generated number");
                    if (guessnum-randomnum<=5) {
                        score+=2;
                    }else if (guessnum-randomnum<=10) {
                       score++; 
                    }
                }
            }
            if(guessnum==randomnum){
                break;
            }
            System.out.println("All attempts are exausted.\nThe number was : "+randomnum);
            scoreboard[round-1]=score;
            score=0;
            attempt=1;
            round++;
        }
        sc.close();

        System.out.println("--------Scoreboard---------");
        System.out.println("Round-1 : "+scoreboard[0]);
        System.out.println("Round-2 : "+scoreboard[1]);
        System.out.println("Round-3 : "+scoreboard[2]);

    }
    
}