package com.mphasis.assignment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mphasis.assignment.entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	List<Transaction> findByAccountIdOrderByEventTimestampAsc(String accountId);

	boolean existsByEventId(String eventId);

}