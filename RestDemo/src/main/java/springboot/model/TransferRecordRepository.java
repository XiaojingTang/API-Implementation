package springboot.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransferRecordRepository extends JpaRepository<TransferRecord, Long> {
    TransferRecord findById(long id);

    List<TransferRecord> findAll();
}
