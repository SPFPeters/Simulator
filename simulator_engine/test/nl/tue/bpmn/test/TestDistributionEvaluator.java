package nl.tue.bpmn.test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import nl.tue.bpmn.parser.DistributionEvaluator;
import nl.tue.util.Util;

public class TestDistributionEvaluator {

	@Test
	public void testDistributionSyntax(){
		DistributionEvaluator de = new DistributionEvaluator();
		System.out.println(de.validate("N(10,4)"));
		System.out.println(de.validate("exp(5)"));
		System.out.println(de.validate("[{correct, 0%}, {incorrect, 100%}]"));
		System.out.println(de.validate("[{high, 10%}, {medium, 20%}, {low, 70%}]}]"));
		System.out.println(de.validate("[{high, 10%}, {medium, 20%}, {low, 70%}]}]"));
	}
	
	@Test
	public void testExponential() {
		DistributionEvaluator de = new DistributionEvaluator();
		double lambda = 0.25;
		double replications = 1000.0;
		double margin = 0.05;

		List<Double> sample = new ArrayList<Double>(); 
		for (int i = 0; i < replications; i++){
			sample.add(Double.parseDouble(de.evaluate("exp("+lambda+")")));
		}
		double mean = Util.mean(sample);
		double variance = Util.variance(sample);
		double theoreticalMean = 1.0/lambda; 
		double theoreticalVariance = Math.pow(lambda,-2.0); 
		assertTrue("Mean ("+mean+") of generated sample is not conform the theoretical mean ("+theoreticalMean+") (note: this can be a rondom occurrence, execute again to be sure)", mean < theoreticalMean*(1.0 + margin) && mean > theoreticalMean*(1.0 - margin));
		assertTrue("Variance ("+variance+") of generated sample is not conform the theoretical variance ("+theoreticalVariance+") (note: this can be a rondom occurrence, execute again to be sure)", variance < theoreticalVariance*(1.0 + margin) && variance > theoreticalVariance*(1.0 - margin));		
	}

	@Test
	public void testNormal() {
		DistributionEvaluator de = new DistributionEvaluator();
		double mu = 2.0;
		double sigma = 0.5;
		double replications = 1000.0;
		double margin = 0.05;

		List<Double> sample = new ArrayList<Double>(); 
		for (int i = 0; i < replications; i++){
			sample.add(Double.parseDouble(de.evaluate("N("+mu+","+sigma+")")));
		}
		double mean = Util.mean(sample);
		double variance = Util.sd(sample);
		double theoreticalMean = mu; 
		double theoreticalSD = sigma; 
		assertTrue("Mean ("+mean+") of generated sample is not conform the theoretical mean ("+theoreticalMean+") (note: this can be a rondom occurrence, execute again to be sure)", mean < theoreticalMean*(1.0 + margin) && mean > theoreticalMean*(1.0 - margin));
		assertTrue("Variance ("+variance+") of generated sample is not conform the theoretical variance ("+theoreticalSD+") (note: this can be a rondom occurrence, execute again to be sure)", variance < theoreticalSD*(1.0 + margin) && variance > theoreticalSD*(1.0 - margin));		
	}
}