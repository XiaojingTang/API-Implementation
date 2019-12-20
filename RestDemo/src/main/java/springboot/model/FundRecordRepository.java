package springboot.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FundRecordRepository extends JpaRepository<FundRecord, Long> {
    FundRecord findById(long id);

    List<FundRecord> findAll();
}
