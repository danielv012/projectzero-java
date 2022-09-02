/**
 * Daniel Vayman
 * dav210004
 */
import java.util.*;
import java.text.*;
import java.io.*;

public class Main {

    private static Scanner s = new Scanner(System.in);

    private static String[] names = new String[30];
    private static int[][] records = new int[30][10];
    private static ArrayList<ArrayList<Integer>> leaders = new ArrayList<ArrayList<Integer>>();
   
    private static double leaderBA, leaderOB;
    private static ArrayList<String> nameBA = new ArrayList<String>();
    private static ArrayList<String> nameOB = new ArrayList<String>();
    private static double[][] calculations = new double[30][2];

    private static DecimalFormat format = new DecimalFormat("#,###.###");

    public static void main(String[] args) throws FileNotFoundException
    {
        // asks user for file name, then opens filestream.
        System.out.println("What is the file name? ");
        String fileName = s.nextLine();
        File stats = new File(fileName);
        s = new Scanner(stats);
        s.useDelimiter(" ");

        // reads data from file, inputting first the name and then the record. the record is then sorted for the specific player.
        int index = 0;
        while(s.hasNextLine())
        {
            names[index] = s.next();
            String temp = s.nextLine();
            temp.replace(" ", "");
            sortRecord(index, temp);
            index++;
        }
        s.close();

        findLeaders();
        displayStats();
        displayLeaders();
    }

    // displays the statistics for each player
    private static void displayStats() 
    {
        int i = 0;
        // for each list of records (3 for 3 players)
        for(int[] x : records)
        {
            /*
            * the reason this null check is included is because it signifies the end of the array containing player names,
            * as objects havent been added to that index.
            */
            if(names[i] == null) break;
            System.out.println(names[i]);
            System.out.println("BA: " + format.format(calculations[i][0]));
            System.out.println("OB%: " + format.format(calculations[i][1]));
            System.out.println("H: " + x[0]);
            System.out.println("BB: " + x[3]);
            System.out.println("K: " + x[2]);
            System.out.println("HBP: " + x[4] + "\n");

            i++;
        }
    }

    private static void displayLeaders() 
    {
        // prints the leader values for BA and OB positions, which require decimals.
        System.out.println("LEAGUE LEADERS");
        System.out.println("BA: " + nameBA.toString() + " " + format.format(leaderBA));
        System.out.println("OB%: " + nameOB.toString() + " " + format.format(leaderOB));
        
        int i = 1;
        System.out.print("BA:");
        for(String x : nameBA)
        {
            System.out.print((i >= 2 ? ", " : " "));
            System.out.print(x);
            i++;
        }
        System.out.println(" " + format.format(leaderBA));
        i = 1;
        
        System.out.print("OB%:");
        for(String x : nameOB)
        {
            System.out.print((i >= 2 ? ", " : " "));
            System.out.print(x);
            i++;
        }
        System.out.println(" " + format.format(leaderOB));
        i = 1;

        /* 
        prints the leader for hits. a sublist is created from the arraylist. the reason why is explained in the findLeaders() method.
        the leader names are printed, and then finally the value for that leader position is printed.
        */
        System.out.print("H:");
        for(int x : leaders.get(0).subList(1, leaders.get(0).size()))
        {
            System.out.print((i >= 2 ? ", " : " "));
            System.out.print(names[x]);
            i++;
        }
        System.out.println(" " + leaders.get(0).get(0));
        i = 1;

        //prints the leader for walks
        System.out.print("BB:");
        for(int x : leaders.get(2).subList(1, leaders.get(2).size()))
        {
            System.out.print((i >= 2 ? ", " : " "));
            System.out.print(names[x]);
        }
        System.out.println(" " + leaders.get(2).get(0));
        i = 1;

        //prints the leader for strikeouts
        System.out.print("K:");
        for(int x : leaders.get(1).subList(1, leaders.get(1).size()))
        {
            System.out.print((i >= 2 ? ", " : " "));
            System.out.print(names[x]);
        }
        System.out.println(" " + leaders.get(1).get(0));
        i = 1;

        //prints the leader for hits by pitch
        System.out.print("HBP:");
        for(int x : leaders.get(3).subList(1, leaders.get(3).size()))
        {
            System.out.print((i >= 2 ? ", " : " "));
            System.out.print(names[x]);
        }
        System.out.println(" " + leaders.get(3).get(0));
        i = 1;
    }

    /*
     * index 0: Hit leaders
     * index 1: Strikeout leaders
     * index 2: Walk leaders
     * index 3: Hits by pitch leaders
     * index 4: BA leaders
     * index 5: OB leaders
     * 
     * first index of each ArrayList<int> is the leading value. The following indices
     * indicate the names of the players that hold that value. Thus, each ArrayList<Double>
     * in the larger ArrayList demonstrates the leading value for that statistic, as well
     * as the names of the players that hold that value. This is also why we create a sublist
     * when printing the leader values as the first element doesnt correspond to a player name.
     */
    private static void findLeaders() 
    {
        int i = 0;
        for(int j = 0; j<5; j++)
        {
            // assumes the first element is the leader value. I do this so I have a value to compare future elements to.
            ArrayList<Integer> temp = new ArrayList<>();
            int leader = records[i][j];
            temp.add(leader);
            temp.add(i);
            i++;
            while(names[i] != null)
            {
                //these elements, denoted by the 'j' index, aren't required for leader output, so there's no need to check for a leader value.
                if(j==1 || j==5 || j==6 || j==7)
                {
                    //skip
                }
                else if(j == 2)
                {
                    //this is a special case because for strikeouts, the better position is less. this else if statement is used to switch the conditions.
                    if(records[i][j] < leader)
                    {
                        leader = records[i][j];
                        //if a new leader positions is found, the arraylist is cleared. The leader value is added, followed by the leader name.
                        temp.clear();
                        temp.add(leader);
                        temp.add(i);
                    }
                    else if(records[i][j] == leader)
                    {
                        // if another players also holds the highest position, the name is added to the arraylist, not the value.
                        temp.add(i);
                    }
                }
                else
                {
                    if(records[i][j] > leader)
                    {
                        leader = records[i][j];
                        temp.clear();
                        temp.add(leader);
                        temp.add(i);
                    }
                    else if(records[i][j] == leader)
                    {
                        temp.add(i);
                    }
                }
                i++;
            }

            // this is needed to ensure that no temporary arraylist gets added to the official leader values list.
            if(j==1 || j==5 || j==6 || j==7)
            {
                //skip
            }
            else
            {
                leaders.add(temp);
            }
            i=0;
        }

        /* 
         * these blocks of code are only for the leader values that require decimal places. the logic is the same. Much of the values are global
         * variables, which is why it looks different. Also, the String names of leaders are added directly, instead of a parallel index number.
        */
        int k = 0;
        leaderBA = calculations[k][0];
        k++;
        while(names[k] != null)
        {
            if(calculations[k][0] > leaderBA)
            {
                leaderBA = calculations[k][0];
                nameBA.clear();
                nameBA.add(names[k]);
            }
            else if(calculations[k][0] == leaderBA)
            {
                nameBA.add(names[k]);
            }
            k++;
        }

        int l = 0;
        leaderOB = calculations[l][1];
        l++;
        while(names[l] != null)
        {
            if(calculations[l][1] > leaderOB)
            {
                leaderOB = calculations[l][1];
                nameOB.clear();
                nameOB.add(names[l]);
            }
            else if(calculations[l][1] == leaderOB)
            {
                nameOB.add(names[l]);
            }
            l++;
        }
        return;
    }
        
    /*
     * index 0: H (hits)
     * index 1: O (outs)
     * index 2: K (strikeouts)
     * index 3: W - BB (walks)
     * index 4: P - HBP (hits by pitch)
     * index 5: S (sacrifices)
     * index 6: PAs (plate appearances)
     * index 7: AB (at-bats [hits, outs, strikeouts])
     * 
     * 
     * index 8: BA (Batting Average - hits/ABs)
     * index 9: OB% (On-base percentage - [H + W + P]/PAs)
     */
    private static void sortRecord(int index, String tempRecord) 
    {
        //switches the player record to an array of chars so we can then use a switch statement to categorize each one.
        char[] record = tempRecord.toCharArray();
        //these booleans are used to denote if a character is 1.) valid and 2.) a valid atbat point, so we can later increment the correct count in the array.
        boolean invalid = false;
        boolean atbat = false;
        for (char x : record)
        {
            switch (x) {
                case 'H':
                //hits
                    records[index][0]++;
                    atbat = true;
                    break;
                case 'O':
                //outs
                    records[index][1]++;
                    atbat = true;
                    break;
                case 'K':
                //strikeouts
                    records[index][2]++;
                    atbat = true;
                    break;
                case 'W':
                //walks
                    records[index][3]++;
                    break;
                case 'P':
                //hits by pitch
                    records[index][4]++;
                    break;
                case 'S':
                //sacrifices
                    records[index][5]++;
                    break;
                default:
                    invalid = true;
                    break;
            }
            if(!invalid) records[index][6]++;
            if(atbat) records[index][7]++;
            //resets for next character check.
            atbat = false;
            invalid = false;
        }

        //Batting average H/ABs
        if(records[index][7] != 0) calculations[index][0] = (double)records[index][0] / (double)records[index][7];
        //On-base percentage [H + W + P]/PAs
        if(records[index][6] != 0) calculations[index][1] = ((records[index][0] + records[index][3] + records[index][4]) / (double)records[index][6]);

        return;
    }
}