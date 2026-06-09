package erp.ope.incentivos.ins.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import erp.ope.incentivos.ins.model.BinnacleIns;

@Repository
public class TravelRepository 
{
	private final JdbcTemplate jdbcTemplate;
	 
	public TravelRepository(JdbcTemplate jdbcTemplate) 
	{
		this.jdbcTemplate = jdbcTemplate;
	}
	
	private final RowMapper<BinnacleIns> binnacleInsRowMapper = (rs, rowNum) -> 
	{
		BinnacleIns vo = new BinnacleIns();
		vo.setDriverCode(rs.getString("driver_code"));
		vo.setKilometers(rs.getInt("kilometers"));
		
		return vo;
	};
	
	public List<BinnacleIns> consultaAcumuladoPorCuatrimestre(String regionId, String marcaId, String zonaId, LocalDate fechaIni, LocalDate fechaFin)
	{
		String sql = getQueryConsultaAcomuladosCuatrimestre();
		return jdbcTemplate.query(sql, binnacleInsRowMapper, regionId, marcaId, zonaId, fechaIni, fechaFin, regionId, marcaId, zonaId, fechaIni, fechaFin);
	}
	
	public String getQueryConsultaAcomuladosCuatrimestre()
	{
		return "SELECT  "
				+ "    a.driver_code, "
				+ "    (a.kilometros + b.kilometros ) as kilometers "
				+ " FROM  "
				+ "       (SELECT  "
				+ "               t.driver1_code as driver_code,  "
				+ "               sum(t.kms) as kilometros "
				+ "       FROM  "
				+ "               TRAVELS t, "
				+ "               drivers d "
				+ "       WHERE  "
				+ "            t.driver1_code = d.driver_code "
				+ "       and "
				+ "            d.REGION_CODE = ? "
				+ "       and "
				+ "            d.BRAND_CODE = ? "
				+ "       and "
				+ "            d.ZONE_CODE = ? "
				+ "       and "
				+ "            t.departure_time between ? and ? "
				+ "       group by  "
				+ "               t.driver1_code) A, "
				+ "      ( SELECT  "
				+ "               t.driver2_code as driver_code,  "
				+ "               sum(t.kms) as kilometros "
				+ "       FROM  "
				+ "               TRAVELS t, "
				+ "               drivers d "
				+ "       WHERE  "
				+ "            t.driver2_code = d.driver_code "
				+ "       and "
				+ "            d.REGION_CODE = ? "
				+ "       and "
				+ "            d.BRAND_CODE = ? "
				+ "       and "
				+ "            d.ZONE_CODE = ? "
				+ "       and "
				+ "            t.departure_time between ? and ? "
				+ "       group by  "
				+ "               t.driver2_code) B "
				+ " where "
				+ "       A.driver_code = b.driver_code ";
	}
	
	
}