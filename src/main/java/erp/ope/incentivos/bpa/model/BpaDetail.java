package erp.ope.incentivos.bpa.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Bpa_Detail")
@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BpaDetail 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "bpadetail_Id")
	Integer id;

	@Column(name = "driver_code")
	String driverCode;

	@Column(name = "driver_name")
	String driverName;

	@Column(name = "brand_code")
	String brandCode;

	@Column(name = "brand_name")
	String brandName;
	
	@Column(name = "incentives_Analyzed")
	Integer incentivesAnalyzed;

	@Column(name = "days_Incentives_Analyzed")
	Integer daysIncentivesAnalyzed;
	
	@Column(name = "calculatedat")
	@JsonFormat( shape = JsonFormat.Shape.STRING, pattern =  "yyyy-MM-dd", timezone = "America/Mexico_City")
	LocalDate calculatedAt;

	@Column(name = "year_Calculated")
	Integer yearCalculated;
	
	@Column(name = "region_Entry_Date")
	@JsonFormat( shape = JsonFormat.Shape.STRING, pattern =  "yyyy-MM-dd", timezone = "America/Mexico_City")
	LocalDate regionEntryDate;
	
	@Column(name = "brand_Entry_Date")
	@JsonFormat( shape = JsonFormat.Shape.STRING, pattern =  "yyyy-MM-dd", timezone = "America/Mexico_City")
	LocalDate brandEntryDate;
	
	@Column(name = "employe_Seniority")
	Integer employeSeniority;
	
	@Column(name = "user_Calculated")
	String userCalculated;
	
	@Column(name = "status")
	Integer status;
	
	@Column(name = "message")
	String message;
}
