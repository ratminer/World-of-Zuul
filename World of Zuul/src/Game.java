import java.util.*;

/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.07.31
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;
    private LinkedList<Room> previousRooms;
    
    private Map<String, Item> inventory;
    private int carryingCapacity;
    
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
    	inventory = new HashMap<>();
    	carryingCapacity = 25;
    	previousRooms = new LinkedList<Room>();
        createRooms();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room outside, theater, pub, lab, office, ground, sky;
        Item pencil, cloud, dirt, computer, beer, seat, sword;
        // create the rooms
        
        outside = new Room("the main entrance of the university");
        theater = new Room("a lecture theater");
        pub = new Room("the campus pub");
        lab = new Room("a computing lab");
        office = new Room("the computing admin office");
        ground = new Room("the dirt");
        sky = new Room("the clouds");
        
        pencil = new Item("pencil", "You can write with this", 1);
        cloud = new Item("cloud", "Looks fluffy", 0);
        dirt = new Item("dirt", "Brown dust under your feet", 3);
        computer = new Item("computer", "State of the art machinery", 20);
        beer = new Item("beer", "Mmmmmmmmmmmm an ice cold beer", 3);
        seat = new Item("seat", "You could probably sit in this", 20);
        sword = new Item("sword", "Sharp as fuck", 20);
        
        
        // initialise room exits
        outside.setExit("east", theater);
        outside.setExit("south", lab);
        outside.setExit("west", pub);
        outside.setExit("up", sky);
        outside.setExit("down", ground);
        theater.setExit("west", outside);
        pub.setExit("east", outside);
        lab.setExit("north", outside);
        lab.setExit("east", office);
        office.setExit("west", lab);
        sky.setExit("down", outside);
        ground.setExit("up", outside);
        
        // add items to rooms
        outside.putItem(sword);
        theater.putItem(seat);
        pub.putItem(beer);
        lab.putItem(computer);
        office.putItem(pencil);
        sky.putItem(cloud);
        ground.putItem(dirt);
        
        currentRoom = outside;  // start game outside
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        printExits();
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        else if (commandWord.equals("look")){
        	lookRoom(command);
        }
        else if(commandWord.equals("back")){
        	goBack();
        }else if(commandWord.equals("take")){
        	takeItem(command);
        }else if(commandWord.equals("drop")){
        	dropItem(command);
        }

        return wantToQuit;
    }
    
    private void takeItem(Command command){
    	if(!command.hasSecondWord()){
    		System.out.println("Take what?");
    		return;
    	}
    	String itemName = command.getSecondWord();
    	Item item = currentRoom.lookItem(itemName);
    	
    	if(item == null){
    		System.out.println("There are none of those here.");
    	}else{
    		int weight = 0;
    		for(Item i : inventory.values()){
    			weight += i.getWeight();
    		}
    		if(weight + item.getWeight() < carryingCapacity){
    			inventory.put(itemName, currentRoom.takeItem(itemName));
    			System.out.println("You found a " + itemName + "!");
    		}else{
    			System.out.println("tooooooo heeeeeaaavvvvyyyyy");
    		}
    	}
    }
    
    private void dropItem(Command command){
    	if(!command.hasSecondWord()){
    		System.out.println("Drop what?");
    		return;
    	}
    	String itemName = command.getSecondWord();
    	Item item = inventory.get(itemName);
    	
    	if(item == null){
    		System.out.println("You have none of those...");
    	}else{
    		currentRoom.putItem(item);
    		System.out.println("Goodbye " + itemName + ".");
    	}
    }
    
    private void goBack(){
    	if(previousRooms.isEmpty()){
    		System.out.println("you cant go back...");
    	}else{
    		currentRoom = previousRooms.getLast();
    		previousRooms.removeLast();
    		System.out.println("going back...");
    	}
    	printExits();
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        System.out.println(CommandWords.getWords());
    }

    /** 
     * Try to go in one direction. If there is an exit, enter
     * the new room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = null;
        nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            previousRooms.add(currentRoom);
            currentRoom = nextRoom;
            printExits();
        }
    }
    
    private void lookRoom(Command command){
    	if(!command.hasSecondWord()){
    		System.out.print("You are looking at ");
    		currentRoom.printDescription();
    	}else{
    		String direction = command.getSecondWord();
    		if(currentRoom.getExit(direction) != null){
    			Room lookingAt = currentRoom.getExit(direction);
    			System.out.print("You are looking at ");
    			lookingAt.printDescription();
    		}else{
    			System.out.println("Theres nothing there...");
    		}
    	}
    	printExits();
    }
    
    private void printExits(){
    	System.out.print("You are in ");
    	currentRoom.printDescription();
        System.out.print("Exits: ");
        System.out.print(currentRoom.getExitString());
        System.out.println();
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
    
    public static void main (String[] args) {
    	Game game = new Game();
    	game.play();
    }
    
}
