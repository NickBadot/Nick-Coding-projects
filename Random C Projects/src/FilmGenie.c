/**
Program to do read in film titles from a file and play a guessing game with them..
It works by seeding a random number and taking a film title based on this number. Then assigns it to a Dummy Array.
User can guess a character or a title. If character is correct the dummy is displays with the additional character.
If title is correct user wins.
Author Nick Badot
**/
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

#define AMOUNT 45

char Film[AMOUNT];
char Dummy[AMOUNT];										//This program will use two arrays, one for the real movie tilte another for screen display

void find_film (void);
void assign_dummy (int length);							//Prototypes
char answer (int guess);
int character_check (char c, int length);
void comment (int guess);
FILE *fp1;
int main()
{
	int length;
	fflush(stdout);
	srand(time(NULL));

	find_film();								//gets film
	length= strlen(Film);
	Film[length-1]='\0';						//eliminates newline
	assign_dummy(length);						//Creates dummy for display


	printf("********Welcome To FilmGenie***********");
	printf("In this game you play a command line hangman. You choose whether to guess a title or a character\n");
	printf("You have five guesses and will lose one for each incorrect character or title guess\n");
	printf("Your Film title to guess is:\n");

	int win=0,guess=5;
	char character[0], titleguess[AMOUNT], char_entered;
	while(win==0 && guess!=0)
	{
		printf("\n%s\n", Dummy);
		char_entered=answer(guess);
			//Answer function displays menu and prompts/returns user option choice
		if(char_entered=='c' || char_entered=='C')
		{
			printf("Enter the character you wish to enter:\n");
			fflush(stdin);
			character[0]=getchar();
			while(character[0]==' ')
				character[0]=getchar();

			// Function below returns 0 if character guess character exists
			if(character_check(character[0], length)==0)
				guess--;
			if(strcmp(Dummy,Film)==0) {
				win=1;
				break;
			}
		}
		else if(char_entered=='f' || char_entered=='F')
		{
			printf("Enter the film title you believe correct, remember to capitalise!\n");
			fflush(stdin);
			gets(titleguess);
			titleguess[length-1]='\0';
			if(strcmp(Film,titleguess)==0) {
				printf("Correct you are victorious!\n");
				win=1;
				break;
			}

			else{
				printf("No, you are wrong.\n");
				guess--;
			}

		}

		else {
			printf("\nIncorect character!\n");
			continue;
		}
	}

	if(win==1)
	{
		comment(guess);
	}
	else
	{
		printf("\nYOU HAVE FAILED");
	}

	return 0;
}




void find_film (void)
{
	int random,i;

	fp1=fopen( "filmtext.txt", "r");				//open film file for reading

	random=0+rand()%AMOUNT;							//Assign random  number

	for(i=0;i<=random; i++)
	{
		fgets(Film, 40, fp1);						//Use random number + for loop to choose a film
	}
	fclose(fp1);
}




void assign_dummy (int length)
{
	int i;
	for(i=0; i<length-1; i++)							//Loop creates a dummy string for the game
	{
		 if(Film[i]!=' ') {								//If character isn't a space print asteriks
			Dummy[i]='*'; }
		else{
			Dummy[i]=' ';}

	}
}


char answer (int guess)
{
		char answer;
	    printf("\n\nYou may choose to guess a character (enter 'c') or guess a title (enter 'f').\n");
		printf("You have <%d> guess(es) remaining\n", guess);
		printf("Choose whether to guess a character or a title:\n");
		fflush(stdin);
		scanf("%c", &answer);
		return answer;
}


//Function assigns characters to the Dummy array from the Film array if the user guesses them.
//Rerurns 0 if no matches occur.
int character_check (char c, int length)
{
	int i, correct=0;

	for(i=0; i<length; i++)
	{
		//The else if statements cause the character part of the game to be case sensitive
		if(c>='A' && c<= 'Z') {
			if(Film[i]==c || Film[i]==(c+32))
			{
				Dummy[i]=Film[i];
				correct=1;
			}
		}
		else if(c>='a' && c<='z')
		{
			if(Film[i]==c || Film[i]==(c-32))
			{
				Dummy[i]=Film[i];
				correct=1;
			}
		}
		else	{ printf("Don't be silly! use alphabet letters. You entered < %c >\n", c); }
	}
	if(correct==1)
		return 1;
	else
		return 0;
}

//Simple function to comment on amount of guesses needed
void comment (int guess)
{
	switch (guess) {
	case 5:
		printf("\nYou did it on your first guess! Reward yourself with cake\n");		//Everyone loves cake!
		break;
	case 4:
		printf("\nSecond time lucky! Maybe half a slice of cake for you\n");
		break;
	case 3:
		printf("\nI guess you're average. Enjoy your mediocrity.\n");
		break;
	case 2:
		printf("\nDamn you suck took you four guesses.  Hate to be you\n");
		break;
	case 1:
		printf("\nYou are a cabbage\n");
		break;
	}
}
