package com.pension.management.processpensionmicroservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pension.management.processpensionmicroservice.model.PensionAmount;

@Repository
public interface PensionAmountRepository extends JpaRepository<PensionAmount, String>{

}
