package api.exception.component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mehmet Aktas on 2020-12-12
 */

public class ExceptionResponse {

	private String errorMessage;
	private String requestedURI;
	private Map<String, String> fieldErrors;

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getRequestedURI() {
		return requestedURI;
	}

	public void setRequestedURI(String requestedURI) {
		this.requestedURI = requestedURI;
	}

	public Map<String, String> getFieldErrors() {
		return fieldErrors;
	}

	public void setFieldErrors(Map<String, String> fieldErrors) {
		this.fieldErrors = fieldErrors;
	}
}
