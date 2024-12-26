/**
 * UserRegisterRequestDTO is a Data Transfer Object used for user registration requests.
 * It contains user details such as username, password, entity number, personal information,
 * contact details, and roles.
 * 
 * Annotations:
 * - @Data: Generates getters, setters, toString, equals, and hashCode methods.
 * - @Getter: Generates getter methods for all fields.
 * - @Setter: Generates setter methods for all fields.
 * - @AllArgsConstructor: Generates a constructor with parameters for all fields.
 * - @NoArgsConstructor: Generates a no-argument constructor.
 * - @ToString: Generates a toString method.
 * 
 * Fields:
 * - username: The username of the user.
 * - password: The password of the user.
 * - entityNo: The entity number associated with the user.
 * - firstname: The first name of the user.
 * - lastname: The last name of the user.
 * - initial: The initial of the user.
 * - idNumber: The identification number of the user.
 * - startDate: The start date of the user's registration.
 * - endDate: The end date of the user's registration.
 * - email: The email address of the user.
 * - mobile: The mobile number of the user.
 * - roleList: The list of roles assigned to the user.
 */

package br.com.doubletelecom.help_desk_tickets.app.domain.dtos;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserRegisterRequestDTO {

	private String username;

	private String password;

	private String entityNo;
	
	private String firstname;

	private String lastname;

	private String initial;

	private String idNumber;

	private Date startDate;

	private Date endDate;
	
	private String email;
	
	private String mobile;

	private List<String> roleList = new ArrayList<>();
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the entityNo
	 */
	public String getEntityNo() {
		return entityNo;
	}

	/**
	 * @param entityNo the entityNo to set
	 */
	public void setEntityNo(String entityNo) {
		this.entityNo = entityNo;
	}

	/**
	 * @return the firstname
	 */
	public String getFirstname() {
		return firstname;
	}

	/**
	 * @param firstname the firstname to set
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	/**
	 * @return the lastname
	 */
	public String getLastname() {
		return lastname;
	}

	/**
	 * @param lastname the lastname to set
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	/**
	 * @return the initial
	 */
	public String getInitial() {
		return initial;
	}

	/**
	 * @param initial the initial to set
	 */
	public void setInitial(String initial) {
		this.initial = initial;
	}

	/**
	 * @return the idNumber
	 */
	public String getIdNumber() {
		return idNumber;
	}

	/**
	 * @param idNumber the idNumber to set
	 */
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * @param mobile the mobile to set
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public List<String> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<String> roleList) {
		this.roleList = roleList;
	}
	
	
}