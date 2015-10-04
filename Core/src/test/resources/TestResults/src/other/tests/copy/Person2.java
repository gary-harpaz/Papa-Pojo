/* This file is a test result expectation. do not change the contents or location of this file tests depend on it */
package other.tests.copy;



public class Person2 {

    public String firstName;
    public String lastName;
    public int age;

    public static Person2 a(Person2 source) {
        Person2 target = new Person2();
        target.firstName = source.firstName;
        target.lastName = source.lastName;
        target.age = source.age;
        return target;
    }

    public Person2 b() {
        return a(this);
    }

}
