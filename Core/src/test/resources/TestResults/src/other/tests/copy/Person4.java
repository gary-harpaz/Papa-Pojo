/* This file is a test result expectation. do not change the contents or location of this file tests depend on it */
package other.tests.copy;



public class Person4 {

    public String firstName;
    public String lastName;
    public int age;

    public Person4 b() {
        Person4 target = new Person4();
        target.firstName = this.firstName;
        target.lastName = this.lastName;
        target.age = this.age;
        return target;
    }

    public static Person4 a(Person4 source) {
        return source.b();
    }

}
