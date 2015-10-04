/* This file is a test result expectation. do not change the contents or location of this file tests depend on it */
package other.tests.copy;



public class Person1 {

    private String _firstName;
    private String _lastName;
    private int _Age;

    public String getFirstName() { return _firstName; }
    public void setFirstName(String firstName) { this._firstName = firstName; }
    public String getLastName() { return _lastName; }
    public void setLastName(String lastName) { this._lastName = lastName; }
    public int getAge() { return _Age; }
    public void setAge(int Age) { this._Age = Age; }

    public static Person1 a(Person1 source) {
        Person1 target = new Person1();
        target._firstName = source.getFirstName();
        target._lastName = source.getLastName();
        target._Age = source.getAge();
        return target;
    }

    public Person1 b() {
        return a(this);
    }

}
