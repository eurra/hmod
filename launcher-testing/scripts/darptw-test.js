var includes = new JavaImporter(
	Packages.hmod.solvers.common,
	Packages.hmod.domains.darptw,
	Packages.optefx.loader
);

with(includes) {
	var heuristic = new ModuleLoader().
		loadAll(
			IterativeHeuristic.class,
			DARPTWDomain.class,
			IterativeDARPTW.class
		).
		setParameter(DARPTWDomain.INSTANCE, '../launcher-testing/input/problems/darptw/pr01.txt').
		setParameter(DARPTWDomain.WEIGHT_TRANSIT_TIME, 8.0).
		setParameter(DARPTWDomain.WEIGHT_RIDE_TIME, 0.0).
		setParameter(DARPTWDomain.WEIGHT_EXCESS_RIDE_TIME, 3.0).
		setParameter(DARPTWDomain.WEIGHT_WAIT_TIME, 1.0).
		setParameter(DARPTWDomain.WEIGHT_SLACK_TIME, 0.0).
		setParameter(DARPTWDomain.WEIGHT_ROUTE_DURATION, 1.0).
		setParameter(DARPTWDomain.INITIALIZER, DARPTWDomain.DEFAULT_INIT).
		setParameter(IterativeHeuristic.MAX_ITERATIONS, 100).
		setParameter(IterativeHeuristic.MAX_SECONDS, 30.0).
		getInstance(Heuristic.class);

	console.reloadBundles();
	console.threading(false);
	console.setInterface('windowed');
	console.debug('1');
	console.randomSeed(1234);

	console.clearOutputs();
	//console.addOutput(DARPTWOutputIds.RESULT_DETAIL);
	//console.addOutput(HeuristicOutputIds.EXECUTION_INFO);

	console.echo("Starting test execution!");
	console.run(heuristic);
}