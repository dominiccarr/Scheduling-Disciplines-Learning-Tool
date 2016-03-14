package scheduler;

import gui.graph.GraphComponent;
import gui.statistics.StatisticsPanel;

import java.awt.Color;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import processes.CPUProcess;
import processes.IOBoundProcess;

public class Statistics {
	
	// averages
	private double avgWaitingTime = 0;
	private double avgQueueSize = 0;
	private double avgTurnaroundTime = 0;
	private double avgNTurnaroundTime = 0;
	private double avgResponseTime = 0;
	
	private double avgWaitingTimeCPU = 0;
	private double avgTurnaroundTimeCPU = 0;
	private double avgNTurnaroundTimeCPU = 0;
	private double avgResponseTimeCPU = 0;
	
	private double avgWaitingTimeIO = 0;
	private double avgTurnaroundTimeIO = 0;
	private double avgNTurnaroundTimeIO = 0;
	private double avgResponseTimeIO = 0;
	
	private double avgQueueSizeIO = 0;
	private double avgQueueSizeCPU = 0;
	
	// maximums
	private int maxWaitingTime = 0;
	private int maxTurnaroundTime = 0;
	private int maxQueueSize = 0;
	private int maxNTurnaroundTime = 0;
	private int maxResponseTime = 0;
	
	// standard deviations
	private double stdDevWaiting = 0;
	private double stdDevTurnaround = 0;
	private double stdDevResponse = 0;
	private double stdDevNTuranround = 0;
	private double stdDevQueueSize = 0;
	
	private ArrayList<GraphComponent> turnaround = new ArrayList<GraphComponent>();
	private ArrayList<GraphComponent> waiting = new ArrayList<GraphComponent>();
	private ArrayList<GraphComponent> response = new ArrayList<GraphComponent>();
	private ArrayList<GraphComponent> nturnaround = new ArrayList<GraphComponent>();
	private ArrayList<GraphComponent> queues = new ArrayList<GraphComponent>();
	
	// minimums
	private int minTurnaroundTime = Integer.MAX_VALUE;
	private int minNTurnaroundTime = Integer.MAX_VALUE;
	
	// vector of queue sizes
	private ArrayList<Integer> queueSizes = new ArrayList<Integer>();
	
	private double cpuUtilization = 0;
	private double cpuPercentIdle = 0;
	private final StatisticsPanel statisticsPanel;
	private NumberFormat formatter = new DecimalFormat("0.00");
	private NumberFormat formatter2 = new DecimalFormat("0.00000");
	
	private double throughput = 0;
	private int startCount;
	private ArrayList<Double> queueSizesCPU = new ArrayList<Double>();
	private ArrayList<Double> queueSizesIO = new ArrayList<Double>();
	
	/**
	 * The constructor
	 * 
	 * @param statisticsPanel
	 *            - the gui
	 */
	public Statistics() {
		statisticsPanel = new StatisticsPanel(this);
	}
	
	/**
	 * calculates the performance statistics
	 */
	public void calculateStatistics(ArrayList<CPUProcess> finished,
			ArrayList<CPUProcess> all, ArrayList<CPUProcess> ready,
			double timeBusy) {
		calculateWaitTimeStats(finished);
		calculateTuranroundStats(finished);
		calculateAverageQueueSize(ready);
		calculateNTurnaroundStats(finished);
		calculateCPUUtilization(timeBusy);
		calculateResponseTimeStats(all);
		throughput = (((double) finished.size()) / Scheduler.simulationTime);
	}
	
	/**
	 * Updates the statistics GUI
	 */
	public void updateStatGUI() {
		statisticsPanel.updateStatistics();
		reset();
	}
	
	/**
	 * Reset Variables
	 */
	public void reset() {
		avgWaitingTimeCPU = 0;
		avgTurnaroundTimeCPU = 0;
		avgNTurnaroundTimeCPU = 0;
		avgResponseTimeCPU = 0;
		
		avgWaitingTimeIO = 0;
		avgTurnaroundTimeIO = 0;
		avgNTurnaroundTimeIO = 0;
		avgResponseTimeIO = 0;
	}
	
	/**
	 * calculates the average waiting time
	 */
	private void calculateWaitTimeStats(ArrayList<CPUProcess> finished) {
		int IOcounter = 0;
		int CPUcounter = 0;
		double total = 0;
		int sumSquared = 0;
		if (finished.size() > 0) {
			waiting = new ArrayList<GraphComponent>();
			for (CPUProcess a : finished) {
				int waitingTime = a.getWaitingTime();
				if (a instanceof IOBoundProcess) {
					avgWaitingTimeIO += waitingTime;
					IOcounter++;
				}
				else {
					avgWaitingTimeCPU += waitingTime;
					CPUcounter++;
				}
				waiting.add(new GraphComponent("" + a.getID(), waitingTime, a
						.getColor(), a.toString()));
				total += waitingTime;
				sumSquared += Math.pow(waitingTime, 2);
				if (waitingTime > maxWaitingTime) {
					maxWaitingTime = waitingTime;
				}
				
			}
			if (IOcounter > 0) {
				avgWaitingTimeIO /= IOcounter;
			}
			if (CPUcounter > 0) {
				avgWaitingTimeCPU /= CPUcounter;
			}
			if (finished.size() > 1) {
				avgWaitingTime = total / finished.size();
				stdDevWaiting = getStandardDeviation(sumSquared, total,
						finished.size());
			}
		}
	}
	
	/**
	 * calculates the average waiting time
	 */
	private void calculateResponseTimeStats(ArrayList<CPUProcess> all) {
		int IOcounter = 0;
		int CPUcounter = 0;
		double started = 0;
		double total = 0;
		int sumSquared = 0;
		response = new ArrayList<GraphComponent>();
		startCount = 0;
		for (CPUProcess a : all) {
			if (a.hasStarted()) {
				startCount++;
				int responseTime = a.getResponseTime();
				if (a instanceof IOBoundProcess) {
					avgResponseTimeIO += responseTime;
					IOcounter++;
				}
				else {
					avgResponseTimeCPU += responseTime;
					CPUcounter++;
				}
				response.add(new GraphComponent("" + a.getID(), responseTime, a
						.getColor(), a.toString()));
				started++;
				total += responseTime;
				sumSquared += Math.pow(responseTime, 2);
				if (responseTime > maxResponseTime) {
					maxResponseTime = responseTime;
				}
			}
		}
		if (started > 0) {
			if (IOcounter > 0) {
				avgResponseTimeIO /= IOcounter;
			}
			if (CPUcounter > 0) {
				avgResponseTimeCPU /= CPUcounter;
			}
			avgResponseTime = total / started;
		}
		stdDevResponse = getStandardDeviation(sumSquared, total, started);
	}
	
	/**
	 * calculates the average turn around
	 */
	private void calculateTuranroundStats(ArrayList<CPUProcess> finished) {
		int IOcounter = 0;
		int CPUcounter = 0;
		double total = 0;
		int sumSquared = 0;
		if (finished.size() > 0) {
			turnaround = new ArrayList<GraphComponent>();
			for (CPUProcess a : finished) {
				int totalTime = a.getTotalTime();
				if (a instanceof IOBoundProcess) {
					avgTurnaroundTimeIO += totalTime;
					IOcounter++;
				}
				else {
					avgTurnaroundTimeCPU += totalTime;
					CPUcounter++;
				}
				turnaround.add(new GraphComponent("" + a.getID(), totalTime, a
						.getColor(), a.toString()));
				total += totalTime;
				sumSquared += Math.pow(totalTime, 2);
				if (totalTime > maxTurnaroundTime) {
					maxTurnaroundTime = totalTime;
				}
				else if (totalTime < minTurnaroundTime) {
					minTurnaroundTime = totalTime;
				}
			}
			if (IOcounter > 0) {
				avgTurnaroundTimeIO /= IOcounter;
			}
			if (CPUcounter > 0) {
				avgTurnaroundTimeCPU /= CPUcounter;
			}
			avgTurnaroundTime = total / finished.size();
			stdDevTurnaround = getStandardDeviation(sumSquared, total, finished
					.size());
		}
	}
	
	/**
	 * calculates the average normalized turn around
	 */
	private void calculateNTurnaroundStats(ArrayList<CPUProcess> finished) {
		int IOcounter = 0;
		int CPUcounter = 0;
		double total = 0;
		int sumSquared = 0;
		if (finished.size() > 0) {
			nturnaround = new ArrayList<GraphComponent>();
			for (CPUProcess a : finished) {
				int nturnaroundTime = a.getTotalTime() / a.getExecutionTime();
				if (a instanceof IOBoundProcess) {
					avgNTurnaroundTimeIO += nturnaroundTime;
					IOcounter++;
				}
				else {
					avgNTurnaroundTimeCPU += nturnaroundTime;
					CPUcounter++;
				}
				nturnaround.add(new GraphComponent("" + a.getID(),
						nturnaroundTime, a.getColor(), a.toString()));
				total += nturnaroundTime;
				sumSquared += Math.pow(nturnaroundTime, 2);
				if (nturnaroundTime > maxNTurnaroundTime) {
					maxNTurnaroundTime = nturnaroundTime;
				}
				else if (nturnaroundTime < minNTurnaroundTime) {
					minNTurnaroundTime = nturnaroundTime;
				}
			}
			if (IOcounter > 0) {
				avgNTurnaroundTimeIO /= IOcounter;
			}
			if (CPUcounter > 0) {
				avgNTurnaroundTimeCPU /= CPUcounter;
			}
			avgNTurnaroundTime = total / finished.size();
			stdDevNTuranround = (getStandardDeviation(sumSquared, total,
					finished.size()));
		}
	}
	
	/**
	 * calculates the average queue size
	 */
	public void calculateAverageQueueSize(ArrayList<CPUProcess> ready) {
		double IOcounter = 0;
		double CPUcounter = 0;
		for (CPUProcess a : ready) {
			if (a instanceof IOBoundProcess) {
				IOcounter++;
			}
			else {
				CPUcounter++;
			}
		}
		
		queueSizesCPU.add(CPUcounter);
		queueSizesIO.add(IOcounter);
		
		int total = 0;
		for (Double a : queueSizesCPU) {
			total += a;
		}
		avgQueueSizeCPU = total / (double) queueSizesCPU.size();
		
		total = 0;
		for (Double a : queueSizesIO) {
			total += a;
		}
		avgQueueSizeIO = total / (double)queueSizesIO.size();
		
		queueSizes.add(ready.size());
		queues.add(new GraphComponent("" + Scheduler.simulationTime, ready
				.size(), Color.black, ""));
		total = 0;
		double sumSquared = 0;
		for (int a : queueSizes) {
			total += a;
			sumSquared += Math.pow(a, 2);
			if (a > maxQueueSize) {
				maxQueueSize = a;
			}
		}
		avgQueueSize = total / (double)queueSizes.size();
		
		if (queueSizes.size() > 1) {
			stdDevQueueSize = getStandardDeviation(sumSquared, total,
					queueSizes.size());
		}
	}
	
	/**
	 * calculates the standard deviation (unbiased)
	 * 
	 * @param sum
	 * @param total
	 * @param amount
	 * @return
	 */
	private static double getStandardDeviation(double sum, double total,
			double amount) {
		if (amount > 1) {
			double totalSquared = total * total;
			double stepOne = sum - totalSquared / amount;
			double s = stepOne / (amount - 1);
			return Math.sqrt(s);
		}
		else {
			return 0;
		}
	}
	
	/**
	 * calculates the percent of the simulation time the CPU was active
	 */
	private void calculateCPUUtilization(double timeBusy) {
		if (Scheduler.simulationTime > 0) {
			cpuUtilization = Math
					.floor((timeBusy / Scheduler.simulationTime) * 100);
			cpuPercentIdle = 100 - cpuUtilization;
		}
	}
	
	public String getAvgTurnaroundTime() {
		return formatter.format(avgTurnaroundTime);
	}
	
	public String getAvgResponseTime() {
		return formatter.format(avgResponseTime);
	}
	
	public String getStdDevWaiting() {
		return formatter.format(stdDevWaiting);
	}
	
	public String getStdDevTurnaround() {
		return formatter.format(stdDevTurnaround);
	}
	
	public int getMaxQueueSize() {
		return maxQueueSize;
	}
	
	public int getMaxWaitingTime() {
		return maxWaitingTime;
	}
	
	public int getMaxTurnaroundTime() {
		return maxTurnaroundTime;
	}
	
	public int getMaxResponseTime() {
		return maxResponseTime;
	}
	
	public int getMinTurnaroundTime() {
		if (minTurnaroundTime == Integer.MAX_VALUE) {
			return 0;
		}
		else {
			return minTurnaroundTime;
		}
	}
	
	public String getAvgWaitingTime() {
		return formatter.format(avgWaitingTime);
	}
	
	public String getAvgQueueSize() {
		return formatter.format(avgQueueSize);
	}
	
	public String getAvgTuraroundTime() {
		return formatter.format(avgTurnaroundTime);
	}
	
	public String getNormalizedTurnaround() {
		return formatter.format(avgNTurnaroundTime);
	}
	
	public String getCpuUtilization() {
		return formatter.format(cpuUtilization);
	}
	
	public StatisticsPanel getPanel() {
		return statisticsPanel;
	}
	
	public int getMaxNTurnaroundTime() {
		return maxNTurnaroundTime;
	}
	
	public int getMinNTurnaroundTime() {
		return (minNTurnaroundTime == Integer.MAX_VALUE ? 0
				: minNTurnaroundTime);
	}
	
	public String getStdDevQueueSize() {
		return formatter.format(stdDevQueueSize);
	}
	
	public String getStdDevNTuranround() {
		return formatter.format(stdDevNTuranround);
	}
	
	public String getStdDevResponse() {
		return formatter.format(stdDevResponse);
	}
	
	public String getCpuIdle() {
		return formatter.format(cpuPercentIdle);
	}
	
	public ArrayList<GraphComponent> getTurnaroundItems() {
		return turnaround;
	}
	
	public ArrayList<GraphComponent> getResponseItems() {
		return response;
	}
	
	public ArrayList<GraphComponent> getWaitingItems() {
		return waiting;
	}
	
	public ArrayList<GraphComponent> getNturnaroundItems() {
		return nturnaround;
	}
	
	public String getThroughput() {
		return formatter2.format(throughput);
	}
	
	public String getAvgWaitingTimeCPU() {
		return formatter.format(avgWaitingTimeCPU);
	}
	
	public String getAvgTurnaroundTimeCPU() {
		return formatter.format(avgTurnaroundTimeCPU);
	}
	
	public String getAvgNTurnaroundTimeCPU() {
		return formatter.format(avgNTurnaroundTimeCPU);
	}
	
	public String getAvgResponseTimeCPU() {
		return formatter.format(avgResponseTimeCPU);
	}
	
	public String getAvgWaitingTimeIO() {
		return formatter.format(avgWaitingTimeIO);
	}
	
	public String getAvgTurnaroundTimeIO() {
		return formatter.format(avgTurnaroundTimeIO);
	}
	
	public String getAvgNTurnaroundTimeIO() {
		return formatter.format(avgNTurnaroundTimeIO);
	}
	
	public String getAvgResponseIO() {
		return formatter.format(avgResponseTimeIO);
	}
	
	public String getAvgQueueSizeIO() {
		return formatter.format(avgQueueSizeIO);
	}
	
	public String getAvgQueueSizeCPU() {
		return formatter.format(avgQueueSizeCPU);
	}
	
	public ArrayList<GraphComponent> getQueues() {
		return queues;
	}
}