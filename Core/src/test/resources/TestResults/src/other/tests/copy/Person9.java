/* This file is a test result expectation. do not change the contents or location of this file tests depend on it */
package other.tests.copy;



public class Person9 {

    private String _firstName;
    private String _lastName;
    private int _Age;

    public String getFirstName() { return _firstName; }
    public void setFirstName(String firstName) { this._firstName = firstName; }
    public String getLastName() { return _lastName; }
    public void setLastName(String lastName) { this._lastName = lastName; }
    public int getAge() { return _Age; }
    public void setAge(int Age) { this._Age = Age; }

    public Person9() {
    }

    public Person9(Person9 source) {
        d(source);
    }

    public Person9 b() {
        Person9 target = new Person9();
        target.d(this);
        return target;
    }

    public static Person9 a(Person9 source) {
        Person9 target = new Person9();
        target.d(source);
        return target;
    }

    public Person9 d(Person9 source) {
        this._firstName = source.getFirstName();
        this._lastName = source.getLastName();
        this._Age = source.getAge();
        return this;
    }

    public static Person9 c(Person9 source,Person9 target) {
        target.d(source);
        return target;
    }

    public Person9 e(Person9 target) {
        target.d(this);
        return target;
    }

}
