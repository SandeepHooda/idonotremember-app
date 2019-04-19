package request;

import java.util.Map;

public class QueryResult {
	private String queryText;
	private Intent intent;
	private Map<String, String> parameters;
	public String getQueryText() {
		return queryText;
	}
	public void setQueryText(String queryText) {
		this.queryText = queryText;
	}
	
	public Map<String, String> getParameters() {
		return parameters;
	}
	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}
	public Intent getIntent() {
		return intent;
	}
	public void setIntent(Intent intent) {
		this.intent = intent;
	}
	
	

}
