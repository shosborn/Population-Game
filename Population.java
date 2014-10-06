/*
Population Game
Created by Sims Osborne 7/3/14

To-do list as of 9/12/14
--come up with an actual interface or menu system
--make output prettier
--change line breaks so lines don't get cut off; is there a better way to do this than just throwing out new line characters everywhere?
--allow for game saving and high-score list (lets me practice file I/O)
--add battle sequence; include option to ignore/ take a set loss and rewards for victory (increased pop/food/fertility/widgets/equip)
--add merchant:  allow player to buy improved equipments (improve worker efficiency) and sell widgets
--add widget factory
--add option for workers to improve field fertility
--allow player actions to modify field fertility and ability to grow/harvest (plantEquip and harvestEquip variables)

*/

import java.util.Scanner;

public class Population
{


	public static void main (String args[])
	{	
	
	Scanner keyboard=new Scanner(System.in);
	int population =100;
	int food=100;
	int freePopulation=population;
	float popGrowth= (float) 1.05;	//setting this to 1.10 will result in starvation, usually before turn 8
	float popGrowthRecovery= (float) 1.10; //only applies when population < some number indicated in end-of-turn section; currently 100 as of 7/7/14
	//I may want to add another variable for slower pop Growth above a certain population number
	//int month=1;	variable definition moved to for loop below
	int gameLength; //should eventually allow this to be set by user at beginning of game
	// String gameMode=""; variable definition moved to point in intro where user input requested
	String answer; //I'd like to define this below rather than here, but I'll need to rewrite a few things to avoid scoping problems
	int starvation=0;	//Is there a good way to move this lower down?
	
	
		//Game Intro
		System.out.println("");
		System.out.println("In this game, you govern a small settlement.");
		System.out.println("Every month, you need to assign people to plant and harvest food.");
		System.out.println("The growing season is year-round, and planted food takes one month to mature.");
		System.out.println("If you make wise decisions, there will be plenty of food to eat and to spare.");
		System.out.println("If you don't, everyone starves.");
		System.out.println("Can you keep your people alive?  \nHow much food will you have left over in the end?");
		System.out.println("");
		System.out.println("Please only enter whole, positive numbers (zero is ok).  \nPlease enter all numbers as numerals, not words.");
		//System.out.println("I do plan to make the game more robust, but right now bad input would be bad.");
		System.out.println("If the game gives you a decimal number, \nplease interpret as the next lowest whole number.");
		//System.out.println("And I apologize for the bad line breaks.");
		//Think I've fixed most of those
		System.out.println("");
		System.out.println("How many months would you like your game to last?  I suggest at least 4.");
		gameLength=keyboard.nextInt(); //will reject zero and negative numbers; still need to deal with decimals and non-numbers
		while (gameLength <=0)
		{	
			System.out.println("Please enter a whole number greater than zero.");
			gameLength=keyboard.nextInt();
		}	
		System.out.println("");
		System.out.println("To play in normal mode, please type normal.  \nI'll have more options later, but that's it for now.");
		//Different modes will vary difficulty by changing the Equip, fertility, or starting food variables.
		//To play with suggestions on, type suggestions here instead of normal.  I want suggestions mode to be hidden, so it's not mentioned in the loop below
		String gameMode=keyboard.next();
		while (!(gameMode.equalsIgnoreCase("normal"))&&!(gameMode.equalsIgnoreCase("suggestions")))
		{
			System.out.println("Please enter a valid game mode.  Right now, that means normal.");
			gameMode=keyboard.next();
		}
		Farm farm1 = new Farm();  //I did this right!  Clas objectName = new className();
	
		//End of Intro

		for (int month=1; month <=gameLength; month=month+1) //most of game contained in this loop
		{
			System.out.println("");
			System.out.println("");
			System.out.println("This is month "+month+" of "+gameLength+".");
			
			do
			{
				freePopulation=population;
				System.out.println("");
				System.out.println("You have "+food+" food stored.");
				System.out.println("Your population is "+population+".");
				farm1.showFarmStatus(0);	//arguement 0 displays minimal info
				if (gameMode.equals("suggestions"))
				{
					farm1.suggestHarvesters(freePopulation);
				}
				freePopulation = farm1.assignHarvesters(month, freePopulation);
		       		freePopulation = farm1.assignPlanters(freePopulation, food, population);
				farm1.showFarmStatus(1);	//argument 1 displays more info
				System.out.println("You have "+freePopulation+" people unassigned.");
				System.out.println("");
				farm1.starvationWarning(population, food); //this is going to cause problems if I have more than one farm
				System.out.println("");
				if (month==1)
				{
					System.out.println("If you're not happy with your worker assignments,");
					System.out.println("you can change them by choosing to not end the month just yet.");
				}
				do
				{
					System.out.println("Would you like to end the month now?");
					System.out.println("Please answer yes or no.");
					answer=keyboard.next();
					if(answer.equalsIgnoreCase("no"))
					{
						farm1.resetFarm(); //resets farm w/o updating food amounts
					}
				}
				while (!(answer.equalsIgnoreCase("yes"))&&!(answer.equalsIgnoreCase("no")));
			}
			while(answer.equalsIgnoreCase("no"));
			
			food = farm1.endFarmTurn(food); //Subtracts planted and adds harvest to stored food; resets farm workers
						
			//Starvation check
			if (food < population)
			{
				population=food;
				if (population<=0)
				{
					System.out.println("Your people are dead. There's no one left. I hope you're proud of yourself.");
					System.exit(0);
						
				}
				if (population<25)
				{
					population=25;	//Mercy rule to allow population to recover a little bit and game to continue
				}
				food=25;	//Setting this to zero means the player almost certainly can't recover from starvation.
				System.out.println("");
				System.out.println("Your people are starving!  The population has fallen!");
				System.out.println("");
				System.out.println("You've found "+food+" food.  You don't want to know where it came from.  Or how it can be planted.");
				starvation=starvation+1; //Counts how many turns were spent in starvation
			}
			else //food is eaten and population grows
			{
				food=food-population;
				if (population<100)
				{
					population = (int) (population * popGrowthRecovery);
				}
				else
				{
					population = (int) (population * popGrowth);
				}	
			}

		} //end of for loop that contains most of the game
		
		//Game summary and player evaluation
		System.out.println("");
		System.out.println(gameLength+" months have passed.");
		System.out.println("Your final population is " + population + ".");
		System.out.println("Your final stored food is " + food + ".");
		if (starvation==0)
		{
			System.out.print("Your people always had enough food to eat");
			if (food>=(2*population))
			{
				System.out.println(", and will have plenty for the forseeable future.");
				System.out.println("You're awesome!");
			}
			else if (food>=population)
			{
				System.out.println(".  You're a good ruler.");
			}
			else
			{
				System.out.println(", but that might not last. \nStill, you're a pretty good ruler.");
			}
		}
		else if (starvation<=(gameLength/4))
		{
			System.out.println("Your people spent "+starvation+ " months starving, \nbut they usually had enough food. You're an ok ruler.");
		}	
		else if (starvation<=(gameLength/2))
		{
			System.out.println("Your people spent "+starvation+ " months starving. \nYou're a bad ruler.");
		}
		else if (starvation<(gameLength))
		{
			System.out.println("Your people spent "+starvation+ " months starving! \nYou're a terrible ruler! What's wrong with you?");
		}
		else
		{
			System.out.println("Your people have starved as long as you've been in power!  \nHow is that even possible? Shouldn't they all be dead by now?");
		}	
		System.out.println("");
		System.out.println("How'd you like my game?");
		

	}
}
Enter file contents here
