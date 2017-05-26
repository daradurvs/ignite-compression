package ru.daradurvs.ignite.compression.model;

public class Person implements Identifiable {
    private long id;
    private int age;
    private boolean isMale;
    private String fullName;
    private String phone;

    public Person(long id, int age, boolean isMale, String fullName, String phone) {
        this.id = id;
        this.age = age;
        this.isMale = isMale;
        this.fullName = fullName;
        this.phone = phone;
    }

    @Override public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean male) {
        isMale = male;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Person person = (Person)o;

        if (id != person.id)
            return false;
        if (age != person.age)
            return false;
        if (isMale != person.isMale)
            return false;
        if (fullName != null ? !fullName.equals(person.fullName) : person.fullName != null)
            return false;
        return phone != null ? phone.equals(person.phone) : person.phone == null;
    }

    @Override public int hashCode() {
        int result = (int)(id ^ (id >>> 32));
        result = 31 * result + age;
        result = 31 * result + (isMale ? 1 : 0);
        result = 31 * result + (fullName != null ? fullName.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        return result;
    }

    @Override public String toString() {
        return "Person{" +
            "id=" + id +
            ", age=" + age +
            ", isMale=" + isMale +
            ", fullName='" + fullName + '\'' +
            ", phone='" + phone + '\'' +
            '}';
    }
}

