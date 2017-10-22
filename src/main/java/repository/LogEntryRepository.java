/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import com.database.LogEntry;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author user
 */
@Repository
public interface LogEntryRepository extends JpaRepository<LogEntry, Long> {

    void save(List<LogEntry> entries);
//" WHERE date>=" + epochDate + " AND date<=" + endDate
    
    @Query("select ip from logs where arrival_date>=(:startDate) AND arrival_Date<=(:endDate) GROUP BY ip HAVING COUNT(*)>=(:threshold)")
    public List<String> findIpsByDate(@Param("startDate") long startDate,
            @Param("endDate") long endDate , @Param("threshold") int threshold);
}
