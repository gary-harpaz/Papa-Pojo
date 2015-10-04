/* This file is a test result expectation. do not change the contents or location of this file tests depend on it */
package other.tests.copy;



public class Person5 {

    private String _firstName;
    private String _lastName;
    private int _Age;

    public String getFirstName() { return _firstName; }
    public void setFirstName(String firstName) { this._firstName = firstName; }
    public String getLastName() { return _lastName; }
    public void setLastName(String lastName) { this._lastName = lastName; }
    public int getAge() { return _Age; }
    public void setAge(int Age) { this._Age = Age; }

    public Person5() {
    }

    public Person5(Person5 source) {
        this._firstName = source.getFirstName();
        this._lastName = source.getLastName();
        this._Age = source.getAge();
    }

    public Person5 b() {
        return new Person5(this);
    }

    public static Person5 a(Person5 source) {
        return new Person5(source);
    }

}
