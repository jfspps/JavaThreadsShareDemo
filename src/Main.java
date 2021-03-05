
public class Main {

	public static void main(String[] args) {

		Countdown countdown = new Countdown();

		// t1 and t2 share the object countdown and can change countdown's instance variables
		// this means that both threads can see the changes brought about by the other thread
		CountdownThread t1 = new CountdownThread(countdown);
		t1.setName("Thread 1");
		CountdownThread t2 = new CountdownThread(countdown);
		t2.setName("Thread 2");
		
		// t1 cannot access the thread stack of t2 (each thread has its own thread stack)
		// the program main() has its own heap, which all threads can access
		
		//local variables are stored in the thread stack

		t1.start();
		t2.start();
	}	
}

class Countdown{

	// saved to the heap
	private int i;

	//each thread runs and suspends the other (including in between for-loop steps, and the println steps (there are a few intermediate 
	//steps, such as concatenation and screen printing etc.)) and vice versa dependent on the OS scheduling, hence the apparent randomness (overall, all numbers are printed
	//between the two threads, but each thread need not print all numbers by itself
	
	//this is referred to as "thread interference" or as a "race condition"
	
	//creating two countdowns with their own thread or by implementing synchronisation, eliminates the race condition
	
	public void doCountdown() {
		String colour;
		
		switch(Thread.currentThread().getName()) {
		case "Thread 1":
			colour = ThreadColour.ANSI_CYAN;
			break;
		case "Thread 2":
			colour = ThreadColour.ANSI_PURPLE;
			break;
		default:
			colour = ThreadColour.ANSI_GREEN;
		}
		
		//**changing int i to a instance variable (of type Countdown) yields seemingly unpredictable results
		for(i = 10; i >0; i--) {
			System.out.println(colour + Thread.currentThread().getName() + ": i = "+ i);

			// this is a point where other threads can be executed before this thread's for condition is checked
			// since there is only one instance of countdown, t1 and t2 can change i; that is, one thread can
			// decrement i for both threads; hence not all threads start at the value 10

			// this behaviour can be avoided by keeping i local to this for loop; in that case, other threads have no
			// knowledge of i and all start from 10
		}
	}
}

class CountdownThread extends Thread{
	//composition (allow CountdownThread objects to access Countdown methods)
	private Countdown threadCountdown;
	
	//on instantiation, CountdownThread object is tied to Thread, all calls to start() must operate on CountdownThread objects
	// not Countdown objects;
	//composition allows CountdownThread objects to access Countdown methods
	public CountdownThread(Countdown countdown) {
		this.threadCountdown = countdown;
		threadCountdown.doCountdown();
	}
	
	public void run() {
		threadCountdown.doCountdown();
	}
}