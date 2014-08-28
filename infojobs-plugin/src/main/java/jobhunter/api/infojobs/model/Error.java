package jobhunter.api.infojobs.model;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "error_description", "status", "timestamp", "requestId" })
public class Error {
	
	@JsonProperty("error_description")
	private String errorDescription;
	@JsonProperty("status")
	private Integer status;
	@JsonProperty("timestamp")
	private String timestamp;
	@JsonProperty("requestId")
	private String requestId;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("error_description")
	public String getErrorDescription() {
		return errorDescription;
	}

	@JsonProperty("error_description")
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	@JsonProperty("status")
	public Integer getStatus() {
		return status;
	}

	@JsonProperty("status")
	public void setStatus(Integer status) {
		this.status = status;
	}

	@JsonProperty("timestamp")
	public String getTimestamp() {
		return timestamp;
	}

	@JsonProperty("timestamp")
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	@JsonProperty("requestId")
	public String getRequestId() {
		return requestId;
	}

	@JsonProperty("requestId")
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}
}
