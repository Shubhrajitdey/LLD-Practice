package mixpattern;

import java.util.ArrayList;
import java.util.List;

interface OrgFlow{
    public int getTotalSalary();
}

class Employee implements OrgFlow{
    private final int salary;

    public Employee(int salary) {
        this.salary = salary;
    }
    @Override 
    public int getTotalSalary(){
        return salary;
    }
    
}

class Department implements OrgFlow{
    private final List<OrgFlow> employeeList = new ArrayList<>();
    private int totalsalary;

    public void addEmployee(OrgFlow employmentType){
        employeeList.add(employmentType);
    }

    @Override 
    public int getTotalSalary(){
        for(OrgFlow obj:employeeList){
            totalsalary += obj.getTotalSalary();
        }
        return totalsalary;
    }
}

public class OrgChartSalary {
    public static void main(String[] args) {
        Employee emp1 = new Employee(200);
        Employee emp2 = new Employee(400);

        Employee empIc = new Employee(1000);
        System.out.println("IC role Salary :" + empIc.getTotalSalary());

        Department parentDept = new Department();
        parentDept.addEmployee(emp1);
        parentDept.addEmployee(emp2);

        Department subDept = new Department();
        Employee empJunior = new Employee(100);
        subDept.addEmployee(empJunior);

        parentDept.addEmployee(subDept);

        System.out.println("Parent Dept Salary :" + parentDept.getTotalSalary());
    }
}
