package request;

import java.util.List;
import java.util.Map;

public class QueryResult {
	private String queryText;
	private Intent intent;
	private List<OutputContexts> outputContexts;
	private Map<String, Object> parameters;
	public String getQueryText() {
		return queryText;
	}
	public void setQueryText(String queryText) {
		this.queryText = queryText;
	}
	
	public Map<String, Object> getParameters() {
		return parameters;
	}
	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}
	public Intent getIntent() {
		return intent;
	}
	public void setIntent(Intent intent) {
		this.intent = intent;
	}
	public List<OutputContexts> getOutputContexts() {
		return outputContexts;
	}
	public void setOutputContexts(List<OutputContexts> outputContexts) {
		this.outputContexts = outputContexts;
	}
	
	

}
