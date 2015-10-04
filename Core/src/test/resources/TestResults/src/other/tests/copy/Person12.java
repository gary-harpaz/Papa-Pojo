/* This file is a test result expectation. do not change the contents or location of this file tests depend on it */
package other.tests.copy;



public class Person12 {

    public String firstName;
    public String lastName;
    public int age;

    public Person12() {
    }

    public Person12(Person12 source) {
        source.e(this);
    }

    public Person12 b() {
        Person12 target = new Person12();
        this.e(target);
        return target;
    }

    public static Person12 a(Person12 source) {
        Person12 target = new Person12();
        source.e(target);
        return target;
    }

    public Person12 e(Person12 target) {
        target.firstName = this.firstName;
        target.lastName = this.lastName;
        target.age = this.age;
        return target;
    }

    public Person12 d(Person12 source) {
        source.e(this);
        return this;
    }

    public static Person12 c(Person12 source,Person12 target) {
        source.e(target);
        return target;
    }

}
