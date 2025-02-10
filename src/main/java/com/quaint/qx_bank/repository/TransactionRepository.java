package com.quaint.qx_bank.repository;

import com.quaint.qx_bank.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, String > {

}
