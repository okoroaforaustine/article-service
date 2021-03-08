/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.clane.articleservice.util;

import com.clane.articleservice.dto.Response;
import com.clane.articleservice.util.ConstantsUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import com.clane.articleservice.dto.Error;

/**
 *
 * @author austine.okoroafor
 */
@Slf4j
@Component
public class AppUtils {
     public boolean isEmptyString(String str) {
        return null == str || "".equalsIgnoreCase(str.trim()) || "null".equalsIgnoreCase(str);
    }

    public ResponseEntity<Response> returnPostValidationErrors(Errors errors) {
        log.debug("Returning post validation errors...");
        List<FieldError> fields = errors.getFieldErrors();
        List<Error> errorList = new ArrayList<Error>();
        Iterator<FieldError> eIt = fields.iterator();
        while (eIt.hasNext()) {
            FieldError fe = eIt.next();
            errorList.add(getFieldRequiredError(fe.getField()));
        }
        return returnErrorResponse(errorList, HttpStatus.BAD_REQUEST);
    }

    public Error getFieldRequiredError(String field) {
        return new Error(ConstantsUtil.FIELD_REQUIRED_MESSAGE.replace("{}", field), 4, field);
    }

    public ResponseEntity<Response> returnErrorResponse(List<Error> errors, HttpStatus code) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        Response response = new Response();
        response.setStatus(ConstantsUtil.ERROR);
        response.setErrors(errors);
        return new ResponseEntity<Response>(response, httpHeaders, code);
    }

   public ResponseEntity<Response> returnSuccessResponse(Object responseObj, String message)
	{
		final HttpHeaders httpHeaders= new HttpHeaders();
	    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
	    Response response = new Response();
	    response.setStatus(ConstantsUtil.SUCCESS);
	    response.setCode(ConstantsUtil.SUCCESS_CODE);
	    response.setMessage(message);
	    response.setResponse(responseObj);
	    return new ResponseEntity<Response>(response, httpHeaders, HttpStatus.OK);
	}
	
   public ResponseEntity<Response> returnErrorResponse(String error, HttpStatus code)
	{
		List<Error> errors = new ArrayList<Error>();
		errors.add(new Error(error, 4));
		final HttpHeaders httpHeaders= new HttpHeaders();
	    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
	    Response response = new Response();
	    response.setStatus(ConstantsUtil.ERROR);
	    response.setErrors(errors);
	    return new ResponseEntity<Response>(response, httpHeaders, code);
	}
   public ResponseEntity<Response> returnStatusResponse(String message)
	{
		final HttpHeaders httpHeaders= new HttpHeaders();
	    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
	    Response response = new Response();
	    response.setStatus(ConstantsUtil.SUCCESS);
	    response.setCode(ConstantsUtil.SUCCESS_CODE);
            response.setMessage(message);
	    return new ResponseEntity<Response>(response, httpHeaders, HttpStatus.OK);
	}
    
}
