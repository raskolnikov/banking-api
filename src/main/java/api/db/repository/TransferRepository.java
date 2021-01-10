package api.db.repository;

import api.db.model.Transfer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Mehmet Aktas on 2020-12-11
 */

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {


	@Query("Select tr from Transfer tr WHERE tr.accountId = :accountId OR tr.destinationAccountId = :accountId")
	List<Transfer> findInOutTransfersByAccountId(@Param("accountId") Long accountId, Pageable pageable);
}
