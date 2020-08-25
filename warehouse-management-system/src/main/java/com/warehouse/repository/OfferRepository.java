package com.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.warehouse.entity.Offer;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Integer> {

}
