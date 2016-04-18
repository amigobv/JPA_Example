package swt6.orm.domain.annotated;

import java.text.DateFormat;
import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
/*
 * Version 1
@DiscriminatorValue("T")
*/
public class TemporaryEmployee extends Employee {
  private static final long serialVersionUID = 1L;
  private static final DateFormat fmt = DateFormat.getDateInstance();

  private String renter;
  private double hourlyRate;

  @Temporal(TemporalType.TIMESTAMP)
  private Date startDate;
  
  @Temporal(TemporalType.TIMESTAMP)
  private Date endDate;

  public TemporaryEmployee() {
  }

  public TemporaryEmployee(String firstName, String lastName, Date dateOfBirth) {
    super(firstName, lastName, dateOfBirth);
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public double getHourlyRate() {
    return hourlyRate;
  }

  public void setHourlyRate(double hourlyRate) {
    this.hourlyRate = hourlyRate;
  }

  public String getRenter() {
    return renter;
  }

  public void setRenter(String renter) {
    this.renter = renter;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer(super.toString());
    sb.append(", hourlyRate=" + hourlyRate);
    sb.append(", renter=" + hourlyRate);
    sb.append(", startDate=" + fmt.format(startDate));
    sb.append(", endDate=" + fmt.format(endDate));

    return sb.toString();
  }
}
