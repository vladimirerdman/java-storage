package examples;

import java.util.Date;

public class Person {

    private String name, surname, fatherName;
    private int age;
    private long uid;
    private Date birthDate;
    private String address, email, phoneNumber;

    private Person(PersonBuilder builder) {
        this.name = builder.name;
        this.surname = builder.surname;
        this.fatherName = builder.fatherName;
        this.age = builder.age;
        this.uid = builder.uid;
        this.birthDate = builder.birthDate;
        this.address = builder.address;
        this.email = builder.email;
        this.phoneNumber = builder.phoneNumber;
    }

    public static PersonBuilder builder() {
        return new PersonBuilder();
    }

    public static class PersonBuilder {

        private String name, surname, fatherName;
        private int age;
        private long uid;
        private Date birthDate;
        private String address, email, phoneNumber;

        public Person build() {
            return new Person(this);
        }

        public PersonBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public PersonBuilder setSurname(String surname) {
            this.surname = surname;
            return this;
        }

        public PersonBuilder setFatherName(String fatherName) {
            this.fatherName = fatherName;
            return this;
        }

        public PersonBuilder setAge(int age) {
            this.age = age;
            return this;
        }

        public PersonBuilder setUid(long uid) {
            this.uid = uid;
            return this;
        }

        public PersonBuilder setBirthDate(Date birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public PersonBuilder setAddress(String address) {
            this.address = address;
            return this;
        }

        public PersonBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public PersonBuilder setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }
    }
}
