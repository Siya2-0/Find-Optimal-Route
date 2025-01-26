import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;


public class IteratedLocalSearch
{
    String[] CampusList={"HATFIELD", "HILLCREST", "GROENKLOOF", "PRINSOF", "MAMELODI"};



    int[][] Distance={{0,15,20,22,30}, {15,0,10,12,25}, {20, 10, 0,8,22}, {22, 12, 8, 0, 18}, {30, 25, 22,18, 0}};

    // public String[]  RandomSolution()
    // {
    //     while()
    //     {
            
    //     }
    //     return new String [CampusList.length];
    // }
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
    public int CalculateCost(String[]Candidate)
    {
        int cost=0;
        //int row=IndexOfCampus(Candidate[0]);

        for(int i=0; i<Candidate.length; i++)
        {
            if(i==Candidate.length-1)
            {
                cost+=(Distance[IndexOfCampus(Candidate[i])][IndexOfCampus(Candidate[0])]);   
            }
            else{
               // System.out.println(IndexOfCampus(Candidate[i]) +" i "+ Candidate[i]);
                //System.out.println(IndexOfCampus(Candidate[i+1]) +" i+1 "+ Candidate[i+1]);
                cost+=(Distance[IndexOfCampus(Candidate[i])][IndexOfCampus(Candidate[i+1])]);
            }
          //  row=i;
        }
        return cost;
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
    // public String[] perturb(String [] Candidate)
    // {
    //     for(int i=Candidate.length-1; i>1; i--)
    //     {
    //         if(Distance[IndexOfCampus(Candidate[i-1])][i] <  Distance[IndexOfCampus(Candidate[i-2])][i-1])
    //         {
    //             String temp=Candidate[i];
    //             Candidate[i]=Candidate[i-1];
    //             Candidate[i-1]=temp;
    //             return Candidate;
    //         }
    //     }
    //     return Candidate;
    // }
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
    public String[] ILS(String[] RandomSol)
    {
        try {
            FileWriter writer = new FileWriter("input.txt", false);
            writer.write("");
            writer.close();
            writer= new FileWriter("Best.txt", false);
            writer.write("");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String BestSolution[]=Copy(RandomSol);//Generate RandomSolution
        WriteToFile2(CalculateCost(BestSolution));
        String CurrentSolution[]= Copy(BestSolution);
        int AvgObjective=0;
        int MaxIteration=20;
        AvgObjective+=CalculateCost(CurrentSolution);
        WriteToFile(CalculateCost(CurrentSolution));
        for(int i=0; i<MaxIteration; i++)
        {
            CurrentSolution=LocalSearch(BestSolution);
            CurrentSolution=perturb(CurrentSolution);
            WriteToFile(CalculateCost(CurrentSolution));
            AvgObjective+=CalculateCost(CurrentSolution);
            if(CalculateCost(CurrentSolution)<CalculateCost(BestSolution))
            {
               // Print(CurrentSolution);
                //System.out.println("best "  + CalculateCost(CurrentSolution)  + "  "+CalculateCost(BestSolution));
                BestSolution=Copy(CurrentSolution);
                //Fitness=CalculateCost(BestSolution);
            }
            WriteToFile2(CalculateCost(BestSolution));
            //compare fitness
        }
        System.out.println("Printing Best Rout");
        Print(BestSolution);
        System.out.println("Printing Objective Function Val ");
        System.out.println(CalculateCost(BestSolution));
        float avg=AvgObjective/(MaxIteration+1);
        System.out.println("Print Avg Objective Function Val");
        System.out.println(avg);
        AddEmptyLine();
        return BestSolution;
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
    public void Print(String[]solution)
    {
        for(int i=0; i<solution.length; i++)
        {
            System.out.print(solution[i]+"->");
        }
        System.out.println("");
    }
}