package com.example.attendance;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalTime;

@Entity
@Table(name = "employee")
//@timeCheck
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    @NotBlank(message = "enter a valid name")
    @Size(min = 3, message = "enter a valid name")
    private String name;

    @Column(name = "email")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "must be a valid email address")
    private String email;

    @Column(name = "time_in")
    private LocalTime timeIn;

    @Column(name = "time_out")
    private LocalTime timeOut;

    @Column(name = "is_late")
    private boolean isLate;

    @Column(name = "duration")
    private String duration;

    public Employee() {}

    public Employee(String name, String email, LocalTime timeIn, LocalTime timeOut, boolean isLate, String duration) {
        this.name = name;
        this.email = email;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.isLate = isLate;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalTime getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(LocalTime timeIn) {
        this.timeIn = timeIn;
    }

    public LocalTime getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(LocalTime timeOut) {
        this.timeOut = timeOut;
    }

    public boolean getIsLate() {
        return isLate;
    }

    public void setIsLate(boolean late) {
        this.isLate = late;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", timeIn=" + timeIn +
                ", timeOut=" + timeOut +
                ", isLate=" + isLate +
                ", duration=" + duration +
                '}';
    }


}
