/**
 * Daniel Vayman
 * dav210004
 */
import java.util.*;
import java.io.*;
import java.sql.Struct;

public class Main {

    public static Scanner s = new Scanner(System.in);

    public ArrayList<String> leadersBA;
    public ArrayList<String> leadersOB;
    public ArrayList<String> leadersH;
    public ArrayList<String> leadersBB;
    public ArrayList<String> leadersK;
    public ArrayList<String> leadersHBP;


    public static void main(String[] args) throws FileNotFoundException
    {
        String[] names = new String[30];
        double[][] records = new double[30][10];
        ArrayList<ArrayList<Double>> leaders = new ArrayList<ArrayList<Double>>();

        System.out.print("What is the file name? ");
        String fileName = s.nextLine();
        File stats = new File(fileName);
        s = new Scanner(stats);
        s.useDelimiter(" ");

        int index = 0;
        while(s.hasNextLine())
        {
            names[index] = s.next();
            records = sortRecord(index, records, s.nextLine());
            index++;
        }
        s.close();

        leaders = findLeaders(names, records, leaders);
        displayStats(names, records);
        displayLeaders(names, leaders);
    }

    

    private static void displayStats(String[] names, double[][] records) {
    }

    private static void displayLeaders(String[] names, ArrayList<ArrayList<Double>> leaders) {
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
     * indicate the names of the players that hold that value.
     */
    private static ArrayList<ArrayList<Double>> findLeaders(String[] names, double[][] records, ArrayList<ArrayList<Double>> leaders) 
    {
        int i = 0;
        for(int j = 0; j<10; j++)
        {
            ArrayList<Double> temp = new ArrayList<>();
            double leader = records[i][j];
            temp.add(leader);
            temp.add((double)i);
            i++;
            while(!names[i].isEmpty())
            {
                if(j==1 || j==5 || j==6 || j==7)
                {
                    //skip
                }
                else if(j == 2)
                {
                    if(records[i][j] < leader)
                    {
                        leader = records[i][j];
                        temp.clear();
                        temp.add(leader);
                        temp.add((double)i);
                    }
                    else if(records[i][j] == leader)
                    {
                        temp.add((double)i);
                    }
                }
                else
                {
                    if(records[i][j] > leader)
                    {
                        leader = records[i][j];
                        temp.clear();
                        temp.add(leader);
                        temp.add((double)i);
                    }
                    else if(records[i][j] == leader)
                    {
                        temp.add((double)i);
                    }
                }
            }
            leaders.add(temp);
            i=0;
        }
        return leaders;
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
     * index 8: BA (Batting Average - hits/ABs)
     * index 9: OB% (On-base percentage - [H + W + P]/PAs)
     */
    private static double[][] sortRecord(int index, double[][] records, String tempRecord) 
    {
        char[] record = tempRecord.toCharArray();
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
                case 'K':
                //strikeouts
                    records[index][2]++;
                    atbat = true;
                case 'W':
                //walks
                    records[index][3]++;
                case 'P':
                //hits by pitch
                    records[index][4]++;
                case 'S':
                //sacrifices
                    records[index][5]++;
                default:
                    invalid = true;
                    break;
            }
            if(!invalid) records[index][6]++;
            if(atbat) records[index][7]++;
        }
        //Batting average H/ABs
        records[index][8] = records[index][0] / records[index][7];
        //On-base percentage [H + W + P]/PAs
        records[index][9] = (records[index][0] + records[index][3] + records[index][4]) / records[index][6];

        return records;
    }
}