package com.Yash.repository;

import com.Yash.domain.WalletTransactionType;
import com.Yash.model.Wallet;
import com.Yash.model.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalletTransactionRepository extends JpaRepository<WalletTransaction,Long> {

    List<WalletTransaction> findByWalletOrderByDateDesc(Wallet wallet);

}
