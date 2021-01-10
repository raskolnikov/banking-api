package api.db.repository;

import api.db.model.BalanceTransaction;
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
public interface BalanceTransactionRepository extends JpaRepository<BalanceTransaction, Long> {

	@Query("Select bt from BalanceTransaction bt WHERE bt.accountId=:accountId")
	List<BalanceTransaction> findByAccountId(@Param("accountId") Long accountId, Pageable pageable);
}
