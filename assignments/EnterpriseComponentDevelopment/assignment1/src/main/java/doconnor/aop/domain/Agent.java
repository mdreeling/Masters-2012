package doconnor.aop.domain;

public class Agent {
	private long id ;
	private String name ;
	
	public Agent() {
		super();
	}

	public Agent(String name) {
		super();
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
