package api.db.repository;

import api.db.model.Account;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Mehmet Aktas on 2020-12-10
 */

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

	@Modifying
	@Query("UPDATE Account SET balance = :balance WHERE id = :accountId ")
	void updateAccountBalance(@Param("accountId") Long accountId,
			@Param("balance") Long balance);
}
