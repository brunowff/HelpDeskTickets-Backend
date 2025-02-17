package br.com.doubletelecom.help_desk_tickets.app.services;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.LoginRequest;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.LoginResponse;

public interface AuthenticationService {
    public LoginResponse login(LoginRequest loginReq);
    public LoginResponse refresh(String refreshToken);
    public void logout(String refreshToken);
}
