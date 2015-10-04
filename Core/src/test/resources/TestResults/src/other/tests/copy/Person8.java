/* This file is a test result expectation. do not change the contents or location of this file tests depend on it */
package other.tests.copy;



public class Person8 {

    public String firstName;
    public String lastName;
    public int age;

    public Person8() {
    }

    public Person8(Person8 source) {
        c(source,this);
    }

    public Person8 b() {
        Person8 target = new Person8();
        c(this,target);
        return target;
    }

    public static Person8 a(Person8 source) {
        Person8 target = new Person8();
        c(source,target);
        return target;
    }

    public static Person8 c(Person8 source,Person8 target) {
        target.firstName = source.firstName;
        target.lastName = source.lastName;
        target.age = source.age;
        return target;
    }

    public Person8 d(Person8 source) {
        c(source,this);
        return this;
    }

    public Person8 e(Person8 target) {
        c(this,target);
        return target;
    }

}
