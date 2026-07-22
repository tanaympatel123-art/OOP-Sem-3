//importing scanner for input
import java.util.Scanner;

//enum for coin value
enum Coin{
    ONE,TWO,FIVE,TEN;
}

//class
public class VendingMachine
{
    public static void main(String[] args)
    {
        int price=15;
        int total=0;

        //declaring scanner and taking input from keyboard
        Scanner sc = new Scanner(System.in);

        //loop for taking coins
        while(total<price)
        {
            //convert every input to uppercase
            Coin coin = Coin.valueOf(sc.next().toUpperCase());
            int value=0;

            //for assigning value
            switch(coin){

            case ONE:
            value=1;
           
            break;

            case TWO:
            value=2;
            
            break;
            

            case FIVE:
            value=5;
           
            break;

            case TEN:
            value=10;
            
            break;

            }
            //add and print
             total+=value;
            System.out.println(total);
        }

        //final output
        System.out.println("Paid. Change: "+(total-price));
        
        sc.close();
    }
}