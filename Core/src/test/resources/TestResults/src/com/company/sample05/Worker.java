/* This file is a test result expectation. do not change the contents or location of this file tests depend on it */
package com.company.sample05;

import com.company.sample01.Person1;


public class Worker extends Person1 implements IMyInterface, ISomeInterface {

private String _department;
private double _salary;
private int _employeeNumber;

public String getDepartment() { return _department; }
public void setDepartment(String department) { this._department = department; }
public double getSalary() { return _salary; }
public void setSalary(double salary) { this._salary = salary; }
public int getEmployeeNumber() { return _employeeNumber; }
public void setEmployeeNumber(int employeeNumber) { this._employeeNumber = employeeNumber; }

}
