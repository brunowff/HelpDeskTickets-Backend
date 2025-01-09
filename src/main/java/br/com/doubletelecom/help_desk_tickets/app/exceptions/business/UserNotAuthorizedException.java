package br.com.doubletelecom.help_desk_tickets.app.exceptions.business;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.com.doubletelecom.help_desk_tickets.app.exceptions.BaseRuntimeException;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UserNotAuthorizedException extends BaseRuntimeException{
    private static final String KEY = "user.not.authorized";

    public UserNotAuthorizedException() {
        super();
    }

    @Override
    public String getExceptionKey() {
        return KEY;
    }
}
