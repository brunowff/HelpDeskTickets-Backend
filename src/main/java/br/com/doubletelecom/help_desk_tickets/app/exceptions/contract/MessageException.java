/**
 * Interface representing a message exception.
 * Provides methods to retrieve the exception key and additional details.
 * 
 * @author 
 * @version
 */
package br.com.doubletelecom.help_desk_tickets.app.exceptions.contract;

import java.util.Map;

public interface MessageException {
    String getExceptionKey();
    Map<String, Object> getMapDetails();
}
