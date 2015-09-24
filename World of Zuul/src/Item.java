
public class Item {
	
	private String description;
	private String name;
	private int weight;

	public Item(String name, String description, int weight){
		this.description = description;
		this.name = name;
		this.weight = weight;
	}
	
	public String getDescription() {
		return description;
	}

	public String getName(){
		return name;
	}
	
	public int getWeight() {
		return weight;
	}
}
