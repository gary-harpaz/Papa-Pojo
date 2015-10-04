/* This file is a test result expectation. do not change the contents or location of this file tests depend on it */
package other.tests.copy;



public class Person6 {

    public String firstName;
    public String lastName;
    public int age;

    public Person6() {
    }

    public Person6(Person6 source) {
        this.firstName = source.firstName;
        this.lastName = source.lastName;
        this.age = source.age;
    }

    public Person6 b() {
        return new Person6(this);
    }

    public static Person6 a(Person6 source) {
        return new Person6(source);
    }

}
