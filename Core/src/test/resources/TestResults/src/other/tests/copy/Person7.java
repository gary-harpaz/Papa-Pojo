/* This file is a test result expectation. do not change the contents or location of this file tests depend on it */
package other.tests.copy;



public class Person7 {

    private String _firstName;
    private String _lastName;
    private int _Age;

    public String getFirstName() { return _firstName; }
    public void setFirstName(String firstName) { this._firstName = firstName; }
    public String getLastName() { return _lastName; }
    public void setLastName(String lastName) { this._lastName = lastName; }
    public int getAge() { return _Age; }
    public void setAge(int Age) { this._Age = Age; }

    public Person7() {
    }

    public Person7(Person7 source) {
        c(source,this);
    }

    public Person7 b() {
        Person7 target = new Person7();
        c(this,target);
        return target;
    }

    public static Person7 a(Person7 source) {
        Person7 target = new Person7();
        c(source,target);
        return target;
    }

    public static Person7 c(Person7 source,Person7 target) {
        target._firstName = source.getFirstName();
        target._lastName = source.getLastName();
        target._Age = source.getAge();
        return target;
    }

    public Person7 d(Person7 source) {
        c(source,this);
        return this;
    }

    public Person7 e(Person7 target) {
        c(this,target);
        return target;
    }

}
