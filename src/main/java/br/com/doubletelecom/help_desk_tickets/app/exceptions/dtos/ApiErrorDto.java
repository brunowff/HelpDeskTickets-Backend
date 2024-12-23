package br.com.doubletelecom.help_desk_tickets.app.exceptions.dtos;

import java.util.Date;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiErrorDto {
    private Date timestamp;
    private Integer status;
    private String code;
    private Set<ErrorDto> errors;
}
