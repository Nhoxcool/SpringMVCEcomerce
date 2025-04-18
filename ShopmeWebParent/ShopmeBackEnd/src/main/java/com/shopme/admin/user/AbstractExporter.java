package com.shopme.admin.user;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.shopme.common.entity.User;

import jakarta.servlet.http.HttpServletResponse;

public abstract class AbstractExporter {
	
	public void setReponseHeader(HttpServletResponse response, String contentType, String extension) throws IOException {
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String timeStap = dateFormatter.format(new Date()); 
		
		String fileName = "user_" + timeStap + extension;
		
		response.setContentType(contentType);
		
		String headerKey = "Content-Disposition";
		
		String headerValue =  "attachment; fileName=" + fileName;
		response.setHeader(headerKey, headerValue);
	}
}
