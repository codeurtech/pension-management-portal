package com.pension.management.processpensionmicroservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pension.management.processpensionmicroservice.model.PensionAmount;
import com.pension.management.processpensionmicroservice.model.PensionerInput;

@Repository
public interface PensionerDetailRepository extends JpaRepository<PensionerInput, String> {

}
