package com.littlewind.demo.exceptionhandling;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.http.HttpStatus;

public class ApiError {
	private HttpStatus status;
	private String timestamp;
	private String message;
    private String debugMessage;
    private List<ApiSubError> subErrors;
    
    public ApiError() {
    	super();
    	TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        String nowAsISO = df.format(new Date());
        
        this.timestamp = nowAsISO;
	}

	public ApiError(HttpStatus status) {
		this();
		this.status = status;
	}

	public ApiError(HttpStatus status, Throwable exeption) {
		this(status);
		this.message = "Unexpected error";
		this.debugMessage = exeption.getLocalizedMessage();
	}

	public ApiError(HttpStatus status, String message, Throwable exeption) {
		this(status);
		this.message = message;
		this.debugMessage = exeption.getLocalizedMessage();
	}
	
	
	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDebugMessage() {
		return debugMessage;
	}

	public void setDebugMessage(String debugMessage) {
		this.debugMessage = debugMessage;
	}

	public List<ApiSubError> getSubErrors() {
		return subErrors;
	}

	public void setSubErrors(List<ApiSubError> subErrors) {
		this.subErrors = subErrors;
	}

	//
	void addSubError(ApiSubError subError)  {
		if (this.subErrors == null) {
            this.subErrors = new ArrayList<>();
        }
        this.subErrors.add(subError);
	}
	


	abstract class ApiSubError {

    }

    class ApiValidationError extends ApiSubError {
        private String object;
        private String field;
        private Object rejectedValue;
        private String message;

        public ApiValidationError(String object, String message) {
            this.object = object;
            this.message = message;
        }

		public ApiValidationError(String object2, String field2, Object rejectedValue2, String message2) {
			this.object = object2;
			this.field = field2;
			this.rejectedValue = rejectedValue2;
			this.message = message2;
		}
    }
}
