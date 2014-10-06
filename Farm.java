/*
Population Game
Created by Sims Osborne 7/3/14

To-do list as of 7/6/14
--make output prettier.
--change line breaks so lines don't get cut off
--add battle sequence
--allow player actions to modify field fertility and ability to grow/harvest (plantEquip and harvestEquip variables)

*/

import java.util.Scanner;

public class Farm
{
	//May want to move these variable declarations elsewhere for clarity
	//Why don't these need to be static?  They work as is, but I don't see how.  Means I need to understand scoping better.
	String name; //I'm looking at only having one farm for now, but I might want to allow for multiple farms in a future version.
	Scanner keyboard=new Scanner(System.in);
	int plantWorkers=0; 
	int harvestWorkers=0;
	int planted=0;
	int growing=500;
	int harvest=0;  
	int plantEquip=4; //determines how many units each assigned person can plant; could eventually be upgradable.
	int harvestEquip=4; //analogous to plantEquip.
	float baseFertility=4; //used to calculate fertility
	float avgFertility=((baseFertility+1)/2); //expected average value of randomized fertility variable
	int fertility; //determines how much planted food increases at end of turn.
	float plantSuggest= (1/(avgFertility))*(harvestEquip/plantEquip); //used to suggest # of harvesters 
	float harvestSuggest=1-plantSuggest; //used to suggest # of harvesters

	public void showFarmStatus(int flag) //Flag equals one for the second time in each turn that farmstatus is displayed
	{
		System.out.println("");
		System.out.println("You have "+growing+" food that could be harvested.");
		if (flag==1)
		{
			if (growing!=0)
			{
				System.out.println("You have "+harvestWorkers+" workers assigned to harvest "+harvest+" food.");
			}
			System.out.println("You have "+plantWorkers+" workers assigned to plant "+planted+" food.");
			if (planted!=0)
			{
				System.out.println("On average, you should have "+(planted*avgFertility)+" food to harvest next month.");
			}	
		}
	}
	
	public void suggestHarvesters(int freePopulation) //input freePopulation from class Population
	{
			//Suggestion is for testing purposes--should not run in real game
			//Isn't working very well
			System.out.print("I suggest at least ");
			if ( growing <= (harvestSuggest*harvestEquip*freePopulation))
			{
				System.out.println((growing / harvestEquip)+" harvesters."); //this should be an int value, but trying to do so caused problems
			}
			else
			{
				System.out.println((harvestSuggest * freePopulation)+" harvesters."); //this should be an int value, but trying to do so caused problems
			}
			
			//end of harvest suggestion
	}
	
	
	public int assignHarvesters(int month, int freePopulation) //Input freePopulation and month from class Population; output updated freePopulation
	{	
		System.out.println("");
		if (growing!=0)
		{
			if (freePopulation==0)
			{
				System.out.println("You have no workers avaliable for harvest");
			}
			else
			{
				System.out.println("How many of your people would you like to put to work harvesting food?");
				System.out.print("You can assign up to ");	//Need to fix problem when avaliable harvest is between 0 and harvestEquip
				if ((0<growing)&&(growing<=harvestEquip))
				{
					System.out.println("You can assign 1 worker to havest "+growing+" food.");
				}
				else if (freePopulation<=(growing/harvestEquip))
				{
					System.out.println(freePopulation+" people to harvest up to "+(freePopulation*harvestEquip)+" food.");
				}
				else
				{
					System.out.println((growing/harvestEquip)+" people to harvest up to "+growing+" food.");
				}
				if (month==1) //one time instructions
				{
					provideHarvestInstructions();
				}
				harvestWorkers=keyboard.nextInt();
				harvest = (harvestEquip*harvestWorkers);
			
				//Check for bad input; need to check for decimal numbers and non-numbers as well
				while ((harvest > growing) || (harvestWorkers > freePopulation) || (harvestWorkers<0))
				{
					if (harvestWorkers<0)
					{
						System.out.println("Please enter a positive number.");
					}
					else
					{
						System.out.println("You can't have that many people harvesting!");
						System.out.println("Remember, you can't harvest more food than you have growing, and it takes one person to harvest " +harvestEquip+" food.");
					}
					System.out.println("How many people would you like to harvest food?");
				
					harvestWorkers=keyboard.nextInt();
					harvest = (harvestEquip*harvestWorkers);
				}

				freePopulation=(freePopulation-harvestWorkers);
				System.out.println("You've assigned " + harvestWorkers + " people to harvest " + harvest + " food.");
			}
		}
		return freePopulation;
	}

	public void provideHarvestInstructions()
	{
		System.out.println("It takes one person to harvest " + harvestEquip + " units of food, \nso you won't be able to harvest everything.");
		System.out.println("It takes one person to plant " + plantEquip + " units of food.");
		System.out.println("At the end of your turn, the food you harvest is added to your stored food \nand the food you plant is subtracted.");
		System.out.println("If the final amount of stored food you have is less than your population, \nyour people will starve!");
		System.out.println("Try to avoid that.");
		//System.out.println("I suggest you have at least " +(100*harvestSuggest)+"% of your people harvest, and the rest plant.");
		//Suggestion isn't working; I need to refine my formula
		System.out.println("");
		System.out.println("So how many people would you like to harvest food?");
	}

	public int assignPlanters(int freePopulation, int food, int population)  ////Input freePopulation and food from class Population; output updated freePopulation
	{
		System.out.println("");
		if (freePopulation==0)
		{
			System.out.println("You don't have any workers avaliable for planting.");
		}
		else if (food==0)
		{
			System.out.println("You don't have any food to plant.");
		}	
		else if ((0<food)&&(food<=plantEquip))
		{
			System.out.println("You can assign 1 worker to plant "+food+" food.");
		}
		else
		{
			System.out.print("You can assign up to ");
			if (freePopulation<=(food/plantEquip))
			{
				System.out.println(freePopulation+" people to plant up to "+(freePopulation*plantEquip)+" food.");
			}
			else
			{
				System.out.println((food/plantEquip)+" people to plant up to "+food+" food.");
			}
			if (population>harvest)
			{
				if (population<(food+harvest))
				{
					System.out.println("To avoid starvation, you should have no more than "+((food+harvest-population)/plantEquip)+" people ");
					System.out.println("plant no more than "+(food+harvest-population)+" food.");
				}
				else
				{
					System.out.println("If you can't harvest more food, your people are going to starve.");
				}
			}
			System.out.println("How many workers would you like to assign to plant food?");
			plantWorkers = keyboard.nextInt();
			if (food<(plantWorkers*plantEquip))
			{
				planted=food;
			}
			else
			{	
				planted = (plantWorkers*plantEquip);
			}
			//Check for bad input; still need to check for decimal numbers and non-numbers	
			while ((planted > food) || (plantWorkers > freePopulation) || (plantWorkers<0) || plantWorkers*plantEquip > planted)
			{
				if (plantWorkers<0)
				{
					System.out.println("Please enter a positive number.");
				}
				else
				{
					System.out.println("You can't plant that much food!");
					System.out.println("Remember, you can't plant more food than you have stored, and each person assigned will plant " + plantEquip + " food,");
					System.out.println("so you can't plant more food than you have people to plant it.");
					System.out.println("How many workers would you like to assign to plant food?");
					//this would be more interesting if you don't yet know the final harvest amount.
					plantWorkers = keyboard.nextInt();
					planted = (plantWorkers*plantEquip);
				}
			}
			freePopulation=freePopulation-plantWorkers;
			System.out.println("You've assigned " + plantWorkers + " people to plant " + planted + " food.");
		}
		return freePopulation;
	}

	public void starvationWarning(int population, int food) //assumes there's only one farm
	{	
		if (population>(food-planted+harvest))
			{		
				System.out.println("There isn't going to be enough food!");
				System.out.println("If you end your turn now, your people will starve.");
				System.out.println("Try harvesting more food, or planting less.");
			}
		else
			{
				System.out.println("You will have enough food to feed your people at the end of this turn.");
			}
	}
	
	public void resetFarm()
	{
	harvestWorkers = 0;
	plantWorkers = 0;
	planted = 0;
	harvest = 0;
	}

	public int endFarmTurn(int food) //input food from class Population; updates variables in class Farm for next turn and returns updated food values
	{
		food = food - planted;
		food = food + harvest;
		fertility = (int)(baseFertility*Math.random())+1; //produces a random value from 1 to 4.
		growing = (int) (planted * fertility);
		harvestWorkers = 0;
		plantWorkers = 0;
		planted = 0;
		harvest = 0;
		return food;
		
	}	
	

}	

Enter file contents here
