package com.example.attendance;

import jakarta.validation.Valid;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/")
public class EmployeeController {

    private EmployeeService employeeService;

    public EmployeeController(EmployeeService theEmployeeService) {
        employeeService = theEmployeeService;
    }

//    @InitBinder
//    public void initBinder(WebDataBinder dataBinder) {
//        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
//        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
//    }

    @RequestMapping("/")
    public String redirect() {
        return "redirect:/list";
    }

    @GetMapping("/list")
    public String displayAllEmployees(Model theModel) {

        List<Employee> employeeList = employeeService.findAll();

        for (Employee tempE : employeeList) {
            if (tempE.getTimeOut() == null) {
                LocalTime now = LocalTime.now();
                Duration diff = Duration.between(tempE.getTimeIn(), now);

                if ((diff.toHoursPart() == 1) && (diff.toMinutesPart() == 1)) {
                    tempE.setDuration(String.format("%02d hour and %02d minute so far", diff.toHoursPart(), diff.toMinutesPart()));
                }
                else if (diff.toMinutesPart() == 1) {
                    tempE.setDuration(String.format("%02d hours and %02d minute so far", diff.toHoursPart(), diff.toMinutesPart()));
                }
                else if (diff.toHoursPart() == 1) {
                    tempE.setDuration(String.format("%02d hour and %02d minutes so far", diff.toHoursPart(), diff.toMinutesPart()));
                }
                else if (diff.toHoursPart() == 0) {
                    tempE.setDuration(String.format("%02d minutes so far", diff.toMinutesPart()));
                }
                else if (diff.toMinutesPart() == 0) {
                    tempE.setDuration(String.format("%02d hours so far", diff.toHoursPart()));
                }
                else {
                    tempE.setDuration(String.format("%02d hours and %02d minutes so far", diff.toHoursPart(), diff.toMinutesPart()));
                }
            }
            else {
                Duration duration = Duration.between(tempE.getTimeIn(), tempE.getTimeOut());

                if ((duration.toHoursPart() == 1) && (duration.toMinutesPart() == 1)) {
                    tempE.setDuration(String.format("%02d hour and %02d minute today", duration.toHoursPart(), duration.toMinutesPart()));
                }
                else if (duration.toMinutesPart() == 1) {
                    tempE.setDuration(String.format("%02d hours and %02d minute today", duration.toHoursPart(), duration.toMinutesPart()));
                }
                else if (duration.toHoursPart() == 1) {
                    tempE.setDuration(String.format("%02d hour and %02d minutes today", duration.toHoursPart(), duration.toMinutesPart()));
                }
                else if (duration.toHoursPart() == 0) {
                    tempE.setDuration(String.format("%02d minutes today", duration.toMinutesPart()));
                }
                else if (duration.toMinutesPart() == 0) {
                    tempE.setDuration(String.format("%02d hours today", duration.toHoursPart()));
                }
                else {
                    tempE.setDuration(String.format("%02d hours and %02d minutes today", duration.toHoursPart(), duration.toMinutesPart()));
                }
            }
        }
        theModel.addAttribute("employees", employeeList);

        return "display-employees";

    }

    @GetMapping("/addEmployee")
    public String displayAddEmployee(Model theModel) {

        Employee theEmployee = new Employee();
        LocalTime now = LocalTime.parse(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        theEmployee.setTimeIn(now);
        theModel.addAttribute("employee", theEmployee);

        return "add-employee";
    }

    @PostMapping("/save")
    public String saveEmployee(@Valid @ModelAttribute("employee") Employee theEmployee,
                               BindingResult theBindingResult) {

        if (theBindingResult.hasErrors()) {
            System.out.println("\n");
            System.out.println("Error found..");
            System.out.println("Binding results: " + theBindingResult.toString());
            System.out.println("\n");
//            theModel.addAttribute("timeError", timeError);
            return "add-employee";
        }

        LocalTime late = LocalTime.parse("09:00");

        int value = theEmployee.getTimeIn().compareTo(late);
        if (value >= 0) {
            theEmployee.setIsLate(true);
        }
//
//        if (theEmployee.getTimeOut() == null) {
//            LocalTime now = LocalTime.now();
//            Duration diff = Duration.between(theEmployee.getTimeIn(), now);
//            theEmployee.setDuration(String.format("%02d hours and %02d minutes so far", diff.toHoursPart(), diff.toMinutesPart()));
//        }
//        else {
//            Duration duration = Duration.between(theEmployee.getTimeIn(), theEmployee.getTimeOut());
//            if (duration.toMinutes() <= 0) {
//                return "add-employee";
//            } else {
//                theEmployee.setDuration(String.format("%02d hours and %02d minutes", duration.toHoursPart(), duration.toMinutesPart()));
//            }
//        }

        employeeService.save(theEmployee);

        return "redirect:/list";

    }

    @GetMapping("/addTimeOut")
    public String addTimeOut(@RequestParam("employeeId") int theId, Model theModel) {
        Employee theEmployee = employeeService.findById(theId);
        LocalTime now = LocalTime.parse(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        theEmployee.setTimeOut(now);
        theModel.addAttribute("employee", theEmployee);

        return "clock-out";
    }

    @PostMapping("/saveTimeOut")
    public String saveTimeOut(@Valid @ModelAttribute("employee") Employee theEmployee) {

        Duration duration = Duration.between(theEmployee.getTimeIn(), theEmployee.getTimeOut());
        if (duration.toMinutes() <= 0) {
            return "clock-out";
        } else {
            theEmployee.setDuration(String.format("%02d hours and %02d minutes", duration.toHoursPart(), duration.toMinutesPart()));
        }
        employeeService.save(theEmployee);

        return "redirect:/list";

    }

    @GetMapping("/editEmployee")
    public String editEmployee(@RequestParam("employeeId") int theId, Model theModel) {

        Employee theEmployee = employeeService.findById(theId);
        boolean error = false;
        theModel.addAttribute("employee", theEmployee);
        theModel.addAttribute("error", error);
        return "edit-employee";
    }

    @PostMapping("/saveEdit")
    public String saveEdit(@Valid @ModelAttribute("employee") Employee theEmployee,
                           BindingResult theBindingResult) {

        if (theBindingResult.hasErrors()) {
            System.out.println("\n");
            System.out.println("Error found..");
            System.out.println("Binding results: " + theBindingResult.toString());
            System.out.println("\n");

            return "edit-employee";
        }



        LocalTime late = LocalTime.parse("09:00");
        int value = theEmployee.getTimeIn().compareTo(late);
        if (value >= 0) {
            theEmployee.setIsLate(true);
        }


        if (theEmployee.getTimeOut() == null) {

            LocalTime now = LocalTime.now();
            Duration diff = Duration.between(theEmployee.getTimeIn(), now);
            theEmployee.setDuration(String.format("%02d hours and %02d minutes so far", diff.toHoursPart(), diff.toMinutesPart()));
        } else {
            Duration duration = Duration.between(theEmployee.getTimeIn(), theEmployee.getTimeOut());
            theEmployee.setDuration(String.format("%02d hours and %02d minutes", duration.toHoursPart(), duration.toMinutesPart()));
        }

        employeeService.save(theEmployee);
        return "redirect:/list";
    }

    @GetMapping("/deleteEmployee")
    public String deleteEmployee(@RequestParam("employeeId") int theId) {

        employeeService.deleteEmployeeById(theId);
        return "redirect:/list";

    }

    @GetMapping("/report")
    public String getReport(Model theModel) {

        List<Employee> employeeList = employeeService.findAll();
        int latecomers = 0;

        for (Employee employee : employeeList) {
            if (employee.getIsLate()) {
                latecomers += 1;
            }
        }

        theModel.addAttribute("employees", employeeList);
        theModel.addAttribute("latecomers", latecomers);

        return "end-of-day-report";
    }

    @GetMapping("/list/{employeeId}")
    public String getEmployee(@PathVariable int employeeId, Model theModel) {

        List<Employee> employeeList = employeeService.findAll();

        for ( Employee tempE : employeeList) {

            if (employeeId == tempE.getId()) {
                Employee theEmployee = employeeService.findById(employeeId);
                theModel.addAttribute("employee", theEmployee);

                return "employee-page";
            }
        }
        return "404-error";

    }



    @PostMapping("/list")
    public String addEmployee(@RequestBody Employee theEmployee, BindingResult theBindingResult) {

        theEmployee.setId(0);
        saveEmployee(theEmployee, theBindingResult);
        return "redirect:/list";

    }

    @PutMapping("/list")
    public String updateEmployee(@RequestBody Employee theEmployee, BindingResult theBindingResult) {

        saveEdit(theEmployee, theBindingResult);
        return "redirect:/list";

    }

    @DeleteMapping("/list/{employeeId}")
    public String deleteAnEmployee(@PathVariable int employeeId) {

        Employee tempEmployee = employeeService.findById(employeeId);

        if (tempEmployee == null) {
            throw new RuntimeException("Employee id not found - " + employeeId);
        }

        employeeService.deleteEmployeeById(employeeId);

        return "redirect:/list";
    }





}





















