# Controlling Game Characters with a Neural Network and Fuzzy Logic
Colm Woodlock - G00341460

## Running the program
To run the program simply clone this repositiory and open the project in your IDE of choice.
Run GameRunner and this will launch the game.

## Design
The design of this project was important as per the requirements the player and enemy sprites had to be threaded, fuzzy logic must be incorporated, neural network integration and also a heuristic search. I will go talk about each class that is used in this project taht requires explanation for what I did and why I did it.

### GameRunner
The main things in GameRunner that I want to talk about are some of the methods I added to the original class. One of these was checkTile and I used this method to check the tile for a help marker tile and if there was one there it increases the level of the help. 

After this I have the method that I used to manage the fuzzy logic, fuzzyFight. In this method it reads in the file and has some error handling, then it accesses the function block "fight" and gets the spider object, it then sets the players variable accessing the player object to get their health and also generating a luck value, it then evaluates the data. Once the data is evaluated the damage outcome is established and is printed to te screen and decremented from the spiders health. The same thing happens for the spider in regards to their health, luck and generating how much damage is dealt to the player. Once the fight sequence is done it then prints out the playes health and also the spiders health. It then runs some checks on if the player still has health and also if the spider is dead. if the player dies the game ends and if the spider dies it is removed from the maze.

The final method that I will discuss in this class is the isValidMove methid. I have altered this from the original code and the player now "picks" up the various items that are littered around the maze. 

### GameView
In GameView there are two methods I want to talk about. The first being paintComponent, again I have changed this from the original implementation. The function for this method is to paint all the items into the maze in both the zoomed in and zoomed out versions of the mad. As such each item is assigned with the sprite array for the zoomed in version and the node is coloured a certain colour for the zoomed out version. This helps to distingush between all the various items on the maze.

The second method that I want to discuss is one I added myself and that is paintTile. In this method the sprites are drawn using the imageIndex's that were set in the previous method.

### Maze
In Maze for the moost part I kept it the same as the original implementation but I also added the method setGoalNode. This method generates a random node within the maze and it then sets that location to the goal node.

### Node
Node was important to this project as it replaced the char array that was being used in the original implementation. As such I had to adapt it from the code we got in labs to suit the project. To do this I added some variables that would be used in other classes and also changed some of the methods around to accomodate this. The main things I added were boolean checks that would be used throughout the project. For example hasSpider would be used to check if the current node contained a spider, this information would then be used to control the fuzzy logic.


### ControlledSprite
ControlledSprite was a class that did not change much from it's original implementation I added some methods to control the new variable that would represent it's healths such as incHealth and decHealth which would increment and decrement the health where appropriate and also a kill method where if the life total was 0 or below the player would be destroyed and the game would end.

### Help
Help was another class that I created for this project. The idea behind this class was that I wanted it to controll the help markers provided and when a user collected a help marker it would then spawn a heuristic path based on thelevel the help marker was, the higher the level the more effective the heuristic search would be. The two main methods in this are createBookTraversers and pathPainter. In createBookTraversers it's decided what level the book is at and it then does a traversal throught the maze up to a certain length. Once this is done pathPainter is called to paint the path to a certain length so the player can follow this towards the goal node.

### Spider
Spider was another class I made for this project used to control the spiders in the maze. My original plan with this was to try and implement a neural network to contol the spiders to hunt for the player. However from struggling to complete the other parts of the project I decided to try and implement a heuristic search for the spiders instead as I was running out of time. The main methods in this class are similar to that of help where a level is assigned to the spiders and depending on their level it applies a different seacrh type to the spider. I also used a TimerTask to try and get the spider moving. This was a late addition to try and meet the threading requirement.

### Spider Control
SpiderControl was a simple enough class used to create spiders and create random postions for them to spawn.

## Issues
I had many issue with this project. The project does compile and run, the movement is fine for the player howeber the player sprite does not get painted and when I try to paint it it switches back and forth with the spider image shown in the video below.

![Alt Text](https://raw.githubusercontent.com/cwoodlock/AI-Project/master/AI/resources/pics/Error.gif?token=AGURCVXR7VSFENN6DBIBD7K4XYOYU)

As well as this issue the fuzzy logic does not seem to be working for some reason that I have not been able to figure out. At oune point it was printing out the player damage dealt but taht was it. Finally the heuristic searches are also not implemented correctly. The issue here is I don't think I am getting the correct position to generate the path.

## Pictures of the game running

![Alt Text](https://raw.githubusercontent.com/cwoodlock/AI-Project/master/AI/resources/pics/GoalNode.png?token=AGURCVUOBPJPJBJAVLNNI4S4XYPGO)

[Alt Text](https://raw.githubusercontent.com/cwoodlock/AI-Project/master/AI/resources/pics/ZoomOut.png?token=AGURCVQE7GQETM752NEPWVK4XYPGW)
