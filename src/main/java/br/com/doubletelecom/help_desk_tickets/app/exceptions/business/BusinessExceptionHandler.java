package br.com.doubletelecom.help_desk_tickets.app.exceptions.business;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.com.doubletelecom.help_desk_tickets.app.exceptions.BaseRuntimeException;

@ResponseStatus
public class BusinessExceptionHandler extends BaseRuntimeException{
    private static String KEY = new String();

    
    public BusinessExceptionHandler() {
        super();
    }

    public HttpStatus LoginEmailOrPasswordException() {
        BusinessExceptionHandler.KEY = "login.email.or.password";
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getExceptionKey() {
        return KEY;
    }
}
