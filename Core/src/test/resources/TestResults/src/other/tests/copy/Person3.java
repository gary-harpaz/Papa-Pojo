/* This file is a test result expectation. do not change the contents or location of this file tests depend on it */
package other.tests.copy;



public class Person3 {

    private String _firstName;
    private String _lastName;
    private int _Age;

    public String getFirstName() { return _firstName; }
    public void setFirstName(String firstName) { this._firstName = firstName; }
    public String getLastName() { return _lastName; }
    public void setLastName(String lastName) { this._lastName = lastName; }
    public int getAge() { return _Age; }
    public void setAge(int Age) { this._Age = Age; }

    public Person3 b() {
        Person3 target = new Person3();
        target._firstName = this.getFirstName();
        target._lastName = this.getLastName();
        target._Age = this.getAge();
        return target;
    }

    public static Person3 a(Person3 source) {
        return source.b();
    }

}
