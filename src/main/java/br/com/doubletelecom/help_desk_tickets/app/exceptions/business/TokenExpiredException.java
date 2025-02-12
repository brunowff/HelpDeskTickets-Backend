package br.com.doubletelecom.help_desk_tickets.app.exceptions.business;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.com.doubletelecom.help_desk_tickets.app.exceptions.BaseRuntimeException;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class TokenExpiredException extends BaseRuntimeException{
    private static final String KEY = "token.expired";

    public TokenExpiredException() {
        super();
    }

    @Override
    public String getExceptionKey() {
        return KEY;
    }

}
