/*
 * Slot.c
 * Program to simulate a simple slot machine
 *  Created on: 24 Jan 2012
 *      Author: Nick Badot
 */
#include <stdio.h>
#include <stdlib.h>
#define INITIAL_CR	10		//Defining a constant makes later changes easier
#define RANDOM	3

typedef struct {
					int slot1;
					int slot2;
					int slot3;
				} Slot;

void fruit_print (Slot S);
int status_check (Slot S);
int credit_calculator (int bet,int credit,int status);
void comment (int initial, int credit);



main()
{
	Slot S;
	int bet, credit=INITIAL_CR;
	int slot1,slot2,slot3,status;
	char answer[1];
	answer[0]='Y';
	printf("Welcome to our superfun magic slot machine, you have %d credits. How much would you like to bet?\n", INITIAL_CR);
	fflush(stdout);

	scanf("%d", &bet);
	//user inputs bet

	while(bet<2 || bet>credit)
	{
		fflush(stdout);
		printf("Invalid amount. Bet must be greater than 2 and less than your total amount of credit."
				"You entered <%d>, please re-enter:\n", bet);
		scanf("%d", &bet);
	}

	//error check above

	//loops while credit is sufficent
	while(answer[0]!='N')
	{
		fflush(stdout);
		S.slot1=0+rand()%RANDOM;
		S.slot2=0+rand()%RANDOM;
		S.slot3=0+rand()%RANDOM;

		 // using the rand() function to generate a number from 0-2 to assign to each fruit

		fruit_print(S);										//Displays slots
		status=status_check(S);								//Checks victory conditions
		credit=credit_calculator(bet,credit,status);		//calculates credit change

		printf("Your credit balance is %d\n", credit);
	    printf("Would you like to keep playing? Enter 'Y' for yes and 'N' for no.\n\n");
		scanf("%c", &answer[0]);



		if (answer[0]=='Y' || answer[0]=='y')
		{

			printf("Please enter amount of new bet:\n");
			scanf("%d", &bet);

				while(bet<2 || bet>credit)
				{	printf("Invalid amount. Bet must be greater than 2 and less than your total amount of credit."
					"You entered <%d>, please re-enter:\n", bet);
					scanf("%d", &bet);
				}

			continue;

		}
		else if (answer[0]=='N' || answer[0]=='n' || credit<=2)
		{
			printf("Goodbye user! Remember gambling ain't the problem, losing is!\n\n");
			comment(INITIAL_CR, credit);												//exits and comments if user quits
			return;
		}

	}



}


/**Function takes in the values of the slot machines then use switch statements on each slot to determine which fruit gets printed**/
void fruit_print (Slot S)
{
	switch (S.slot1)
	{
		case 0:
			printf("|ORANGE|");
			break;
		case 1:
			printf("|BANANA|");
			break;
		case 2:
			printf("|PEAR|");
			break;

		default:
			printf("error");
			break;
	}

	switch (S.slot2)
	{
			case 0:
				printf("|ORANGE|");
				break;
			case 1:
				printf("|BANANA|");
				break;
			case 2:
				printf("|PEAR|");
				break;
			default:
				printf("error");
				break;
	}

	switch (S.slot3)
	{
			case 0:
				printf("|ORANGE|");
				break;
			case 1:
				printf("|BANANA|");
				break;
			case 2:
				printf("|PEAR|");
				break;
			default:
				printf("error");
				break;
	}
}


/**This function uses a series of if statements and an input of the slot values to decide what the condition is after slot runs**/
int status_check (Slot S)
{
	int status;
	if(S.slot1==S.slot2 && S.slot2==S.slot3)
	{
		status=1;							//This equates to a full house
	}
	else if(S.slot1==S.slot2 || S.slot2==S.slot3 || S.slot1==S.slot3)
	{
		status=2;							//This equates to a half house
	}
	else
	{
		status=3;							//This equates to a loss
	}
	return (status);
}

/**this function uses the inputted status and bet to decide whether the credit should increase or decrease
	and by how much. It then returns the new amount of credit**/
int credit_calculator (int bet, int credit, int status)
{

	switch (status)
	{
	case 1:
		credit=credit+bet;
		printf("You have a full house!\n\n");
		break;
	case 2:
		credit=credit+bet/2;
		printf("You have a half-house\n\n");
		break;
	case 3:
		credit=credit-bet;
		printf("You have an empty house\n\n");
		break;
	}
	return(credit);
}


/**Function comments on win/loss of credits**/
void comment (int initial, int credit)
{
	if(credit<initial)
	{
		printf("You have lost %d credits\n\n", initial-credit);
	}
	else if(credit==initial)
	{
		printf("You broke even\n\n");
	}
	else
	{
		printf("You have won %d credits", credit-initial);
	}
}
