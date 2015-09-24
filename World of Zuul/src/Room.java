import java.util.*;

/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  The exits are labelled north, 
 * east, south, west.  For each direction, the room stores a reference
 * to the neighboring room, or null if there is no exit in that direction.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.07.31
 */
public class Room 
{
    private String description;
    
    private Map<String, Room> exits;
    private Map<String, Item> items;
    
    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * @param description The room's description.
     */
    public Room(String description) 
    {
    	exits = new HashMap<>();
    	items = new HashMap<>();
    	this.description = description;
    }

    /**
     * Define the exits of this room.  Every direction either leads
     * to another room or is null (no exit there).
     * @param north The north exit.
     * @param east The east east.
     * @param south The south exit.
     * @param west The west exit.
     */
    public void setExit(String direction, Room room) 
    {
    	exits.put(direction, room);
    }
    
    public Room getExit(String exit){
    	return exits.get(exit);
    }
    
    public String getExitString(){
    	String exitString = "";
    	Set<String> keys = exits.keySet();
    	
    	for(String s : keys){
    		exitString += s + " ";
    	}
    	
    	return exitString;
    }
    
    public String getItems(){
    	String itemString = "";
    	Set<String> keys = items.keySet();
    	
    	for(String s: keys){
    		itemString += " " + s;
    	}
    	
    	if(keys.isEmpty()) itemString = " nothing";
    	
    	return "This room contains" + itemString;
    }
    
    public void putItem(Item item){
    	items.put(item.getName(), item);
    }
    
    public Item lookItem(String item){
    	return items.get(item);
    }
    
    public Item takeItem(String item){
    	Item i = items.get(item);
    	items.remove(item);
    	return i;
    }

    /**
     * @return The description of the room.
     */
    public void printDescription()
    {
        System.out.println(description);
        System.out.println(getItems());
    }

}
