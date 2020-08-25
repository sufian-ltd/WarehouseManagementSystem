package com.warehouse.service;

import java.util.List;

import com.warehouse.entity.Offer;

public interface OfferService {

	public List<Offer> findAll();

	public Offer findById(Integer id);

	public void save(Offer offer);

	public void deleteById(Integer id);

	public void deleteExpiredOffer();
}
