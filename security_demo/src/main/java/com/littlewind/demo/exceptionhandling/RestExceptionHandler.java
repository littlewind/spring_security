package com.littlewind.demo.exceptionhandling;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.littlewind.demo.customexception.ShopNotLinkedException;


@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice	
public class RestExceptionHandler extends ResponseEntityExceptionHandler{
	    
	@ExceptionHandler(ShopNotLinkedException.class)
	protected ResponseEntity<ApiError> handleCustomEntityNotFound(ShopNotLinkedException ex, WebRequest request) {
		ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, ex.getMessage(), ex);
	    return new ResponseEntity<>(apiError, apiError.getStatus());
	}
	
}
