/* This file is a test result expectation. do not change the contents or location of this file tests depend on it */
package other.tests.copy;



public class Person10 {

    public String firstName;
    public String lastName;
    public int age;

    public Person10() {
    }

    public Person10(Person10 source) {
        d(source);
    }

    public Person10 b() {
        Person10 target = new Person10();
        target.d(this);
        return target;
    }

    public static Person10 a(Person10 source) {
        Person10 target = new Person10();
        target.d(source);
        return target;
    }

    public Person10 d(Person10 source) {
        this.firstName = source.firstName;
        this.lastName = source.lastName;
        this.age = source.age;
        return this;
    }

    public static Person10 c(Person10 source,Person10 target) {
        target.d(source);
        return target;
    }

    public Person10 e(Person10 target) {
        target.d(this);
        return target;
    }

}
