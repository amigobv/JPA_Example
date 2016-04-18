package swt6.orm.domain.annotated;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
/*
 * Version 1
@DiscriminatorValue("P")
*/
public class PermanentEmployee extends Employee {
  private static final long serialVersionUID = 1L;
  private double salary;

  public PermanentEmployee() {
  }

  public PermanentEmployee(String firstName, String lastName, Date dateOfBirth) {
    super(firstName, lastName, dateOfBirth);
  }

  public double getSalary() {
    return salary;
  }

  public void setSalary(double salary) {
    this.salary = salary;
  }

  @Override
  public String toString() {
    return super.toString() + ", salary=" + salary;
  }
}
