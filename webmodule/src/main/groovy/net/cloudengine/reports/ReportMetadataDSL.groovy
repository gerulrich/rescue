package net.cloudengine.reports

class ReportMetadataDSL {
	
	ReportMetadata parseDSL(File dsl) {
		
		ReportMetadata reportMetadata = new ReportMetadata()
		Script dslScript = new GroovyShell().parse(dsl.text, dsl.getName())		
				
		dslScript.metaClass = createEMC(dslScript.class, {
			ExpandoMetaClass emc ->
			
			emc.report = {
				Closure cl ->
				
				cl.delegate = new ReportDelegate(reportMetadata)
				cl.resolveStrategy = Closure.DELEGATE_FIRST
				
				cl()
			}
		})
		dslScript.run()
		
		return reportMetadata;
	}
	
	static ExpandoMetaClass createEMC(Class clazz, Closure cl) {
		ExpandoMetaClass emc = new ExpandoMetaClass(clazz, false)
		cl(emc)
		emc.initialize()
		return emc
	}
}

class ReportDelegate {
	
	private ReportMetadata reportMetadata;
	
	public ReportDelegate(ReportMetadata reportMetadata) {
		this.reportMetadata = reportMetadata;
	}

	void name(String name) {
		reportMetadata.setName(name);
	}
	
	void parameters(Closure cl) {
		cl.delegate = new ParamsDelegate(reportMetadata)
		cl.resolveStrategy = Closure.DELEGATE_FIRST
		cl()
	}
}

class ParamsDelegate {
	
	private ReportMetadata reportMetadata;
	
	public ParamsDelegate(ReportMetadata reportMetadata) {
		super();
		this.reportMetadata = reportMetadata;
	}

	def methodMissing(String name, Object args) {
		if (args.length == 1) {
			if (args[0] instanceof Closure) {
				ParameterMetadata pm = new ParameterMetadata();
				pm.setName(name);
				args[0].delegate = new ParamDelegate(pm)
				args[0].resolveStrategy = Closure.DELEGATE_FIRST
				
				args[0]()
				
				this.reportMetadata.addParameter(pm);

			} else {
				throw new MissingMethodException(name, this.class, args as Object[])
			}
		} else {
			throw new MissingMethodException(name, this.class, args as Object[])
		}
	}
}

class ParamDelegate {
	
	private ParameterMetadata pm;
	
	ParamDelegate(ParameterMetadata pm) {
		this.pm = pm
	}
	
	void required(boolean required) {
		pm.setRequired(required);
	}
	
	void type(Class clazz) {
		pm.setClazz(clazz);
	}
	
	void label(String label) {
		pm.setLabel(label);
	}
	
	void multiple(boolean multiple) {
		pm.setMultiple(multiple);
	}
	
	void sql(String sql) {
		pm.setQuery(sql);
	}
	
	void methodMissing(String name, Object args) {
		throw new MissingMethodException(name, this.class, args as Object[])
	}	
}