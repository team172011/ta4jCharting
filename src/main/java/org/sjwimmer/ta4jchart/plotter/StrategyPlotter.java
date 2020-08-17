package org.sjwimmer.ta4jchart.plotter;

import org.ta4j.core.Strategy;

public interface StrategyPlotter extends Plotter {

	void addStrategy(Strategy strategy);
}
