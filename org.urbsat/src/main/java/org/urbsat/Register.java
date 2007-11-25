package org.urbsat;

import org.gdms.sql.customQuery.QueryManager;
import org.gdms.sql.function.FunctionManager;
import org.orbisgis.pluginManager.PluginActivator;
import org.urbsat.custom.AverageBuildHeight;
import org.urbsat.custom.BalancedBuildVolume;
import org.urbsat.custom.BuildArea;
import org.urbsat.custom.BuildLenght;
import org.urbsat.custom.BuildNumber;
import org.urbsat.custom.BuildVolume;
import org.urbsat.custom.FrontalDensity;
import org.urbsat.custom.LateralDensity;
import org.urbsat.custom.OldAverageBuildHeight;
import org.urbsat.custom.StandardDeviationBuildBalanced;
import org.urbsat.custom.StandardDeviationBuildHeight;
import org.urbsat.kmeans.KMeans;
import org.urbsat.landcoverIndicators.custom.Density;
import org.urbsat.landcoverIndicators.function.Compacity;
import org.urbsat.landcoverIndicators.function.MeanSpacingBetweenBuildingsInACell;
import org.urbsat.utilities.CreateGrid;
import org.urbsat.utilities.GetZDEM;

public class Register implements PluginActivator {
	public void start() throws Exception {
		QueryManager.registerQuery(new CreateGrid());
		QueryManager.registerQuery(new Density());
		QueryManager.registerQuery(new BuildNumber());
		QueryManager.registerQuery(new BuildLenght());
		QueryManager.registerQuery(new OldAverageBuildHeight());
		QueryManager.registerQuery(new AverageBuildHeight());
		QueryManager.registerQuery(new LateralDensity());
		QueryManager.registerQuery(new FrontalDensity());
		QueryManager.registerQuery(new BuildVolume());
		QueryManager.registerQuery(new BuildArea());
		QueryManager.registerQuery(new BalancedBuildVolume());
		QueryManager.registerQuery(new StandardDeviationBuildBalanced());
		QueryManager.registerQuery(new StandardDeviationBuildHeight());

		QueryManager.registerQuery(new GetZDEM());

		FunctionManager.addFunction(new MeanSpacingBetweenBuildingsInACell());
		FunctionManager.addFunction(new Compacity());
		
		QueryManager.registerQuery(new KMeans());
	}

	public void stop() throws Exception {
	}
}