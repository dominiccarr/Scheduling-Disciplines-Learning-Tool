package scheduler;

public class ExponentialRandom {
	
	private static final long serialVersionUID = 3093719855113098294L;
	public static double lambda = .16;
	
	/**
	 * Generates exponential random variables Adapted from Donald Knuth's
	 * Semi-numerical Algorithms. The Art of Computer Programming, Volume 2.
	 * Addison Wesley.
	 * 
	 * @return exponential variable
	 */
	public static int nextExponential() {
		double U = Math.random();
		return (int) Math.ceil((-(1/lambda) * Math.log(U)));
	}
	
	public static void main(String[] args) {
		int a = 0;
		int i = 0;
		for (i = 0; i <10000000; i++){
		a+=ExponentialRandom.nextExponential();
		}
		double avg =  a / (double)i;
		System.out.println(1/lambda + " " +avg);
	}

}
