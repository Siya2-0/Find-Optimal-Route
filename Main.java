import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        //TestHelperFunctions();
        Simulation();

    }
    public static void  SA()
    {
        SimulatedAnnealing one= new SimulatedAnnealing();
        //one.SA();
    }
    public static void Simulation()
    {
        IteratedLocalSearch generateRandomSolution= new IteratedLocalSearch();
        String RandomInitialPopulation[]=generateRandomSolution.GenerateRandomSolution();
        for(int i=0; i<RandomInitialPopulation.length; i++)
        {
            System.out.print(RandomInitialPopulation[i] +" -> ");
        }
        System.out.println("");
        System.out.println("Iterated Local Search");
        long startTime = System.nanoTime(); 
        IteratedLocalSearch two= new IteratedLocalSearch();
        two.ILS(RandomInitialPopulation);
        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println("Execution time in nanoseconds: " + totalTime);

        System.out.println("Simulated Annealing");
        startTime = System.nanoTime();  
        SimulatedAnnealing one= new SimulatedAnnealing();
        one.SA(RandomInitialPopulation);
        endTime = System.nanoTime();
        totalTime = endTime - startTime;
        System.out.println("Execution time in nanoseconds: " + totalTime);

    }
    public static void ILS()
    {
        IteratedLocalSearch two= new IteratedLocalSearch();
       //two.ILS();
    }
    public static void TestHelperFunctions()
    {
        IteratedLocalSearch one= new IteratedLocalSearch();
        System.out.println("Primaru Hash");
        for(int i=0; i<10;i++)
        {
            System.out.println(one.PrimaryHash());
        }

        System.out.println("Secondary Hash");
        for(int i=0; i<10;i++)
        {
            System.out.println(one.SecondaryHash());
        }

        String[] RandomSolution= one.GenerateRandomSolution();
        for(int i=0; i<RandomSolution.length; i++)
        {
            System.out.println(RandomSolution[i]);
        }

        System.out.println(one.IndexOfCampus(RandomSolution[0]));

        System.out.println(one.CalculateCost(RandomSolution));
        
        String[] perturb=one.perturb(RandomSolution);
        for(int i=0; i<RandomSolution.length; i++)
        {
            System.out.println(perturb[i]);
        }

       // String[] ILS=one.ILS();
        System.out.println("efrg ");
        // for(int i=0; i<RandomSolution.length; i++)
        // {
        //     System.out.println(ILS[i]);
        // }
        //System.out.println(one.CalculateCost(ILS));

    }
}