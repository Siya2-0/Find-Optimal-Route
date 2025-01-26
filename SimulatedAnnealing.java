import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class SimulatedAnnealing {

    String[] CampusList={"HATFIELD", "HILLCREST", "GROENKLOOF", "PRINSOF", "MAMELODI"};



    int[][] Distance={{0,15,20,22,30}, {15,0,10,12,25}, {20, 10, 0,8,22}, {22, 12, 8, 0, 18}, {30, 25, 22,18, 0}};

    public int PrimaryHash()
    {
        Random randomIndex = new Random();

        return randomIndex.nextInt((CampusList.length));
    }

    public int SecondaryHash()
    {
        Random randomIndex = new Random();
        int save=randomIndex.nextInt()%CampusList.length;
        if(save<0)
            save*=-1;
        while(save==0)
        {
            save=randomIndex.nextInt()%CampusList.length;
            if(save<0)
                save*=-1;
        }

        return save;
    }

    public String[] GenerateRandomSolution()
    {
        String RandomSolution[]=new String[CampusList.length];
        for(int i=0; i<CampusList.length; i++)
        {
            RandomSolution[i]="";
        }
        RandomSolution[0]=CampusList[0];
        int primaryIndex;
        int secondaryIndex;
        for(int step=1; step<CampusList.length; )
        {
            primaryIndex=PrimaryHash();
            if(RandomSolution[primaryIndex]=="")//empty
            {
                RandomSolution[primaryIndex]=CampusList[step];
                step++;
            }
            else{//clash
                secondaryIndex=(primaryIndex+SecondaryHash())%CampusList.length;
                if(RandomSolution[secondaryIndex]=="")
                {
                    RandomSolution[secondaryIndex]=CampusList[step];
                    step++;
                }
            }
        }


        return RandomSolution;
    }

    public int IndexOfCampus(String Campus)
    {
        for(int i=0; i<CampusList.length; i++)
        {
            if(Campus.toUpperCase()==CampusList[i])
            {
                return i;
            }
        }
        return -1;
    }
    public String[] LocalSearch(String[] Candidate)
    {
        String[]Copy=Copy(Candidate);
        Random randomIndex= new Random();
        int swap1=randomIndex.nextInt(Candidate.length);
        while( swap1==0)
        {
            swap1=randomIndex.nextInt(Candidate.length);
        }
        if(swap1==Candidate.length-1)
        {
            String temp=Copy[swap1];
            Copy[swap1]=Copy[swap1-1];
            Copy[swap1-1]=temp;
        }
        else{
            String temp=Copy[swap1];
            Copy[swap1]=Copy[swap1+1];
            Copy[swap1+1]=temp;
        }

        return Copy;
    }


    public int CalculateCost(String[]Candidate)
    {
        int cost=0;
      

        for(int i=0; i<Candidate.length; i++)
        {
            if(i==Candidate.length-1)
            {
                cost+=(Distance[IndexOfCampus(Candidate[i])][IndexOfCampus(Candidate[0])]);   
            }
            else{

                cost+=(Distance[IndexOfCampus(Candidate[i])][IndexOfCampus(Candidate[i+1])]);
            }
      
        }
        return cost;
    }
    public void Print(String[]solution)
    {
        for(int i=0; i<solution.length; i++)
        {
            System.out.print(solution[i]+"->");
        }
        System.out.println("");
    }

    public String[] Copy(String[] original)
    {
        String[]copy= new String[original.length];
        for(int i=0; i<original.length; i++)
        {
            copy[i]=original[i];
        }
        return copy;
    }
  
    public String[] perturb(String[] Candidate)
    {
        String[] copy=Copy(Candidate);
        Random randomIndex= new Random();
        int swap1=randomIndex.nextInt(Candidate.length);
        int swap2=randomIndex.nextInt(Candidate.length);
        while(swap1==swap2 || (swap1==0 || swap2==0))
        {
            swap1=randomIndex.nextInt(Candidate.length);
            swap2=randomIndex.nextInt(Candidate.length);
        }
        //System.out.println(swap1+ "  " +swap2);
        String temp=copy[swap1];
        copy[swap1]=copy[swap2];
        copy[swap2]=temp;
        return copy;
    }
    public void WriteToFile(int currentCost)
    {
        String filename="input.txt";
        try{
            FileWriter fileWriter= new FileWriter(filename, true);
            fileWriter.write("\n"+currentCost);

            fileWriter.close();
        }
        catch(IOException e)
        {
            System.out.println("An error occurred while appending the number to the file.");
            e.printStackTrace(); 
        }

    }
    public String[] BestSolution(String[] n1, String[]n2, String[]n3)
    {
        String[] best=n1;

        if(CalculateCost(best)>CalculateCost(n2))
        {
            best=n2;
        }

        if(CalculateCost(best)>CalculateCost(n3))
        {
            best=n3;
        }

        return best;
    }
    public String[] SA(String[] RandomSol)
    {
        // try {
        //     FileWriter writer = new FileWriter("input.txt", false);
        //     writer.write("");
        //     writer.close();
        //     writer= new FileWriter("Best.txt", false);
        //     writer.write("");
        //     writer.close();
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
        double Temperature=100.0;
        String[]Best=Copy(RandomSol);//generate  random
        WriteToFile2(CalculateCost(Best));
        String[] current=Copy(Best);
        WriteToFile(CalculateCost(current));
        int AvgObjective=CalculateCost(current);
        int MaxIteration=20;
        String[]neighbour1, neighbour2, neighbour3;
        for(int i=0; i<MaxIteration; i++)
        {
            neighbour1=LocalSearch(current);
            neighbour2=LocalSearch(current);
            neighbour3=LocalSearch(current);
            String[] next=Copy(BestSolution(neighbour1, neighbour2, neighbour3));
            Random r=new Random();
            int RandomPercentage=r.nextInt(101);
            if(CalculateCost(next)<CalculateCost(Best))
            {
                Best=Copy(next);
                current=Copy(next);
              
            }
            else if(RandomPercentage<Temperature)
            {
                current=Copy(next);
            }
            WriteToFile(CalculateCost(current));
            WriteToFile2(CalculateCost(Best));
            AvgObjective+=CalculateCost(current);
            Temperature=0.8*Temperature;
        }

        System.out.println("Printing Best Rout");
        Print(Best);
        System.out.println("Printing Objective Function Val ");
        System.out.println(CalculateCost(Best));
        float avg=AvgObjective/(MaxIteration+1);
        System.out.println("Printing Avg Objective Function Val");
        System.out.println(avg);
        AddEmptyLine();
        return Best;
    }
    public void WriteToFile2(int BestCost)
    {
        String filename="Best.txt";
        try{
            FileWriter fileWriter= new FileWriter(filename, true);
            fileWriter.write("\n"+BestCost);

            fileWriter.close();
        }
        catch(IOException e)
        {
            System.out.println("An error occurred while appending the number to the file.");
            e.printStackTrace(); 
        }

    }
    public void AddEmptyLine()
    {
        try {
            FileWriter writer = new FileWriter("input.txt", true);
            writer.write("\n");
            writer.close();
            writer= new FileWriter("Best.txt", true);
            writer.write("\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
