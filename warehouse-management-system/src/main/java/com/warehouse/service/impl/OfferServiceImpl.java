package com.warehouse.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.warehouse.entity.Offer;
import com.warehouse.repository.OfferRepository;
import com.warehouse.service.OfferService;
import com.warehouse.util.Utils;

@Service
public class OfferServiceImpl implements OfferService {

	@Autowired
	private OfferRepository offerRepository;

	@Override
	public List<Offer> findAll() {
		return offerRepository.findAll();
	}

	@Override
	public Offer findById(Integer id) {
		return offerRepository.getOne(id);
	}

	@Override
	public void save(Offer offer) {
		offerRepository.save(offer);
	}

	@Override
	public void deleteById(Integer id) {
		offerRepository.deleteById(id);
	}

	@Override
	public void deleteExpiredOffer() {
		findAll().forEach(e -> {
			if (e.getDuration() < Utils.getDaysBetweenDates(e.getDate(), LocalDate.now())) {
				e.getStorage().setOffer(null);
				offerRepository.delete(e);
			}
		});
	}
}
