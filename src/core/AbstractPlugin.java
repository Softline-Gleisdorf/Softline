package core;

public abstract class AbstractPlugin implements Plugin {

	@Override
	public void speak() {
		System.out.println(getClass().getSimpleName() + " speaking...");
	}

}
