package br.com.doubletelecom.help_desk_tickets.app.exceptions.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDto {
    private String key;
    private String message;
}
