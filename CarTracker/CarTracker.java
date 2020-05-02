// Primary driver program for project 3
import java.util.*;
import java.io.*;
public class CarTracker
{
    public static void main(String args[]) throws IOException
    {
        // Declaring variables used for input from a file
        Scanner input = new Scanner(System.in);
        String fileName = new String("cars.txt");
        File inputFile = new File(fileName);
        Scanner fromFile = new Scanner(inputFile);
        
        // Declaring main data structure variables
        CarPQ prices = new CarPQ(15, 'p');  // initially set to 15, but a CarPQ will dynamically
        CarPQ mileages = new CarPQ(15, 'm');
        CarDLB priceVIN = new CarDLB();
        CarDLB mileageVIN = new CarDLB();

        CarDLB priceMM = new CarDLB();
        CarDLB mileageMM = new CarDLB();

        // Read in data from the file
        while(fromFile.hasNext())
        {
            String word = fromFile.nextLine();

            if(word.contains("#"))
            {
                word = fromFile.nextLine();
            }
            
            String vin = word.substring(0, 17);
            word = word.substring(18);

            String make = word.substring(0, word.indexOf(":"));
            word = word.substring(word.indexOf(":") + 1);

            String model = word.substring(0, word.indexOf(":"));
            word = word.substring(word.indexOf(":") + 1);

            double price = Double.parseDouble(word.substring(0, word.indexOf(":")));
            word = word.substring(word.indexOf(":") + 1);

            double mileage = Double.parseDouble(word.substring(0, word.indexOf(":")));
            word = word.substring(word.indexOf(":") + 1);

            String color = word;
            
            Car newCar = new Car(vin, make, model, price, mileage, color);
            prices.insert(prices.size(), newCar);
            mileages.insert(mileages.size(), newCar);
            priceVIN.insert(vin, priceVIN.size());
            mileageVIN.insert(vin, mileageVIN.size());
            
            // MakeModel inserts into a new DLB for indirection
            String key = make + model;
            priceMM.insertMM(key, newCar, 'p');
            mileageMM.insertMM(key, newCar, 'm');
        }

        //prices.printPQ();
        //mileages.printPQ();
        
        System.out.println("Hello! Welcome to Teddy's Totally Legit Car Dealership!");
        System.out.println("How can I help you?");
        System.out.println("\nOptions (! to quit):");
        System.out.println("\t1) Add a car");
        System.out.println("\t2) Update a car");
        System.out.println("\t3) Remove a car");
        System.out.println("\t4) Show lowest priced car");
        System.out.println("\t5) Show lowest mileage car");
        System.out.println("\t6) Show lowest priced car by make and model");
        System.out.println("\t7) Show lowest mileage car by make and model");

        String response = input.nextLine();

        while(!response.equals("!"))
        {
            if(response.equals("1"))    // Add a car
            {
                System.out.println(" - Adding a car - ");
                System.out.print("\tEnter the VIN number: ");
                String vin = input.nextLine();

                System.out.print("\tEnter the make: ");
                String make = input.nextLine();

                System.out.print("\tEnter the model: ");
                String model = input.nextLine();

                System.out.print("\tEnter the price: ");
                double price = Double.parseDouble(input.nextLine());

                System.out.print("\tEnter the mileage: ");
                double mileage = Double.parseDouble(input.nextLine());

                System.out.print("\tEnter the color: ");
                String color = input.nextLine();

                Car newCar = new Car(vin, make, model, price, mileage, color);
                prices.insert(prices.size(), newCar);
                mileages.insert(mileages.size(), newCar);
                priceVIN.insert(vin, priceVIN.size());
                mileageVIN.insert(vin, mileageVIN.size());

                // MakeModel inserts into a new DLB for indirection
                String key = make + model;
                priceMM.insertMM(key, newCar, 'p');
                mileageMM.insertMM(key, newCar, 'm');

                //prices.printPQ();
                //mileages.printPQ();
            }
            else if(response.equals("2"))   // Update a car
            {
                System.out.println(" - Updating a car - ");
                System.out.print("\tEnter the VIN number: ");
                String vin = input.nextLine();
                System.out.println("\tOptions: ");
                System.out.println("\t\t1) Update price");
                System.out.println("\t\t2) Update mileage");
                System.out.println("\t\t3) Update color");
                response = input.nextLine();

                if(response.equals("1"))    // Update price
                {
                    int currIndex = priceVIN.search(vin); 
                    Car currCar = prices.carAt(currIndex); // O(1), just indexes an array

                    System.out.println(" - Updating a car price - ");
                    System.out.print("\tEnter the new price: ");
                    double price = Double.parseDouble(input.nextLine());

                    Car newCar = new Car(vin, currCar.getMake(), currCar.getModel(), price, currCar.getMileage(), currCar.getColor());
                    prices.update(currIndex, newCar);
                    mileages.update(currIndex, newCar);

                    String mm = newCar.getMake() + newCar.getModel();
                    priceMM.insertMM(mm, newCar, 'p');
                    mileageMM.insertMM(mm, newCar, 'm');

                    //prices.printPQ();
                    //mileages.printPQ();
                }
                else if(response.equals("2"))   // Update mileage
                {
                    int currIndex = mileageVIN.search(vin);
                    Car currCar = mileages.carAt(currIndex);

                    System.out.println(" - Updating a car mileage - ");
                    System.out.print("\tEnter the new mileage: ");
                    double mileage = Double.parseDouble(input.nextLine());

                    Car newCar = new Car(vin, currCar.getMake(), currCar.getModel(), currCar.getPrice(), mileage, currCar.getColor());
                    prices.update(currIndex, newCar);
                    mileages.update(currIndex, newCar);

                    String mm = newCar.getMake() + newCar.getModel();
                    priceMM.insertMM(mm, newCar, 'p');
                    mileageMM.insertMM(mm, newCar, 'm');

                    //prices.printPQ();
                    //mileages.printPQ();
                }
                else if(response.equals("3"))   // Update color
                {
                    int currIndex = priceVIN.search(vin);
                    Car currCar = prices.carAt(currIndex);

                    System.out.println(" - Updating a car color - ");
                    System.out.print("\tEnter the new color: ");
                    String color = input.nextLine();

                    Car newCar = new Car(vin, currCar.getMake(), currCar.getModel(), currCar.getPrice(), currCar.getMileage(), color);
                    prices.update(currIndex, newCar);
                    mileages.update(currIndex, newCar);

                    String mm = newCar.getMake() + newCar.getModel();
                    priceMM.insertMM(mm, newCar, 'p');
                    mileageMM.insertMM(mm, newCar, 'm');

                    //prices.printPQ();
                    //mileages.printPQ();
                }
            }
            else if(response.equals("3"))   // Remove a specific car from consideration
            {
                System.out.println(" - Removing a car - ");
                System.out.print("\tEnter the VIN number: ");
                String vin = input.nextLine();

                int currIndexP = priceVIN.search(vin);
                int currIndexM = mileageVIN.search(vin);

                prices.delete(currIndexP);
                mileages.delete(currIndexM);

                //prices.printPQ();
                //mileages.printPQ();
            }
            else if(response.equals("4"))   // Retrieve the lowest price car
            {
                System.out.println(" - Showing lowest priced car - ");
                System.out.println(prices.minCar());
            }
            else if(response.equals("5"))   // Retrieve the lowest mileage car
            {
                System.out.println(" - Showing lowest mileage car - ");
                System.out.println(mileages.minCar());
            }  
            else if(response.equals("6"))   // Retrieve the lowest price car by make and model
            {
                System.out.println(" - Showing lowest dumb priced car by make and model - ");
                System.out.print("\tEnter the make: ");
                String make = input.nextLine();
                System.out.print("\tEnter the model: ");
                String model = input.nextLine();

                String key = make + model;
                CarPQ newPricePQ = priceMM.searchMM(key);
                Car lowest = newPricePQ.minCar();

                int currIndex = priceVIN.search(lowest.getVIN());

                // If the car still exists and if the price is still the same
                if(prices.contains(currIndex) && prices.carAt(currIndex).getPrice() == lowest.getPrice()) // contains(), carAt(), and getMileage() are constant time theta(1) operations
                {
                    System.out.println("\nHere's the car you selected: " + lowest);
                }          
                else
                {
                    newPricePQ.delMin();    // log(n)
                    lowest = newPricePQ.minCar();
                    System.out.println("\nHere's the car you selected: " + lowest);
                }      
            }
            else if(response.equals("7"))   // Retrieve the lowest mileage car by make and model
            {
                System.out.println(" - Showing lowest mileage car by make and model - ");
                System.out.print("\tEnter the make: ");
                String make = input.nextLine();
                System.out.print("\tEnter the model: ");
                String model = input.nextLine();

                String key = make + model;

                CarPQ newMileagePQ = mileageMM.searchMM(key);
                Car lowest = newMileagePQ.minCar();

                int currIndex = mileageVIN.search(lowest.getVIN());

                // If the car still exists and if the mileage is still the same
                if(mileages.contains(currIndex) && mileages.carAt(currIndex).getMileage() == lowest.getMileage()) // contains(), carAt(), and getMileage() are constant time theta(1) operations
                {
                    System.out.println("\nHere's the car you selected: " + lowest);
                }          
                else
                {
                    newMileagePQ.delMin();  // log(n)
                    lowest = newMileagePQ.minCar();
                    System.out.println("\nHere's the car you selected: " + lowest);
                }   
            }


            System.out.println("\n\nOptions (! to quit):");
            System.out.println("\t1) Add a car");
            System.out.println("\t2) Update a car");
            System.out.println("\t3) Remove a car");
            System.out.println("\t4) Show lowest priced car");
            System.out.println("\t5) Show lowest mileage car");
            System.out.println("\t6) Show lowest priced car by make and model");
            System.out.println("\t7) Show lowest mileage car by make and model");
            response = input.nextLine();
        }

        input.close();
        fromFile.close();
    }
}