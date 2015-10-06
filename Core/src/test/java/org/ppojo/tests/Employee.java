/*
 * Copyright (c) 2015.  Gary Harpaz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ppojo.tests;




/**
 * Created by GARY on 10/3/2015.
 */
public class Employee  {
    public String name;
    public int age;
    public String id;

    public Employee() {

    }


    //staticCopyMethod
    //can be main copy method

    //copy static methodName
    public static Employee Copy(Employee source,Employee target) {
        target.age=source.age;
        target.id=source.id;
        target.name=source.name;
        return target;
    }



    //staticFactoryMethod
    public static Employee Copy(Employee source) {
        Employee target=new Employee();
        target.age=source.age;
        target.id=source.id;
        target.name=source.name;
        return target;
    }


    //memberFactoryMethod
    //copy instance method Name
    //public Employee NewCopy() {
    public Employee Copy() {
        Employee copy=new Employee();
        Copy(this,copy);
        return copy;
    }


    //can be main copy method
    //memberCopyToMethod
    public Employee CopyTo(Employee target) {
        Copy(this,target);
        return target;
    }


    //can be main copy method
    //memberCopyFromMethod
    public Employee CopyFrom(Employee source) {
        Copy(source,this);
        return this;
    }
    //copy constructor
    public Employee(Employee source) {
        Copy(source,this);
    }
}
