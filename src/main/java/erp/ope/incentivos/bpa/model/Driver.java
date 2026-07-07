package erp.ope.incentivos.bpa.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "DRIVER")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Driver 
{
	@Id
	@Column(name = "driver_Code")
	String driverCode;
	
	@Column(name = "first_Name")
	String firstName;
	
	@Column(name = "paternal_lastname")
	String paternalLastName;
	
	@Column(name = "maternal_lastname")
	String maternalLastName;
	
	@Column(name = "hr_Status")
	Integer	hrStatus;
	
	@Column(name = "region_entrydate")
	@JsonFormat( shape = JsonFormat.Shape.STRING, pattern =  "yyyy-MM-dd", timezone = "America/Mexico_City")
	LocalDate regionEntryDate;

	@Column(name = "brand_entrydate")
	@JsonFormat( shape = JsonFormat.Shape.STRING, pattern =  "yyyy-MM-dd", timezone = "America/Mexico_City")
	LocalDate brandEntryDate;
	
	@Column(name = "region_Code")
	String regionCode;
	
	@Column(name = "region_Name")
	String regionName;
	
	@Column(name = "brand_Code")
	String brandCode;
	
	@Column(name = "brand_Name")
	String brandName;
	
	@Column(name = "zone_Code")
	String zoneCode;
	
	@Column(name = "zone_Name")
	String zoneName;
}
