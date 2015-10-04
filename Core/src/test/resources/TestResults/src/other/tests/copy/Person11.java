/* This file is a test result expectation. do not change the contents or location of this file tests depend on it */
package other.tests.copy;



public class Person11 {

    private String _firstName;
    private String _lastName;
    private int _Age;

    public String getFirstName() { return _firstName; }
    public void setFirstName(String firstName) { this._firstName = firstName; }
    public String getLastName() { return _lastName; }
    public void setLastName(String lastName) { this._lastName = lastName; }
    public int getAge() { return _Age; }
    public void setAge(int Age) { this._Age = Age; }

    public Person11() {
    }

    public Person11(Person11 source) {
        source.e(this);
    }

    public Person11 b() {
        Person11 target = new Person11();
        this.e(target);
        return target;
    }

    public static Person11 a(Person11 source) {
        Person11 target = new Person11();
        source.e(target);
        return target;
    }

    public Person11 e(Person11 target) {
        target._firstName = this.getFirstName();
        target._lastName = this.getLastName();
        target._Age = this.getAge();
        return target;
    }

    public Person11 d(Person11 source) {
        source.e(this);
        return this;
    }

    public static Person11 c(Person11 source,Person11 target) {
        source.e(target);
        return target;
    }

}
