package com.warehouse.Controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.warehouse.entity.Offer;
import com.warehouse.entity.Product;
import com.warehouse.entity.Review;
import com.warehouse.entity.Storage;
import com.warehouse.entity.User;
import com.warehouse.model.StatisticDataModel;
import com.warehouse.service.OfferService;
import com.warehouse.service.ProductService;
import com.warehouse.service.ReviewService;
import com.warehouse.service.StorageService;
import com.warehouse.service.UserService;

@Controller
@RequestMapping("/owners")
public class OwnerController {

	@Autowired
	private UserService userService;

	@Autowired
	private StorageService storageService;

	@Autowired
	private OfferService offerService;

	@Autowired
	private ProductService productService;

	@Autowired
	private ReviewService reviewService;

	@GetMapping("/dashboard")
	public String dashboard() {
		return "owner/owner-dashboard";
	}

	@GetMapping("/storages")
	public String storages(Principal principal, Model model) {
		offerService.deleteExpiredOffer();
		User user = userService.findByUsername(principal.getName());
		List<Storage> storages = user.getStorages();
		storages.removeIf(e -> e.getIsActive().equals(0));
		model.addAttribute("storages", storages);
		return "owner/owner-storages";
	}

	@GetMapping("/addstorage")
	public String addStorageForm(Model model) {
		model.addAttribute("storage", new Storage());
		return "owner/add-storage";
	}

	@PostMapping("/addstorage")
	public String saveStorage(@Valid Storage storage, Principal principal, Model model) {
		User user = userService.findByUsername(principal.getName());
		storage.setAvailableCapacity(storage.getTotalCapacity());
		storage.setIsActive(1);
		storage.setUser(user);
		storageService.save(storage);
		return "redirect:/owners/storages";
	}

	@GetMapping("/edit")
	public String editStorageForm(@RequestParam("id") Integer id, Model model) {
		Storage storage = storageService.findById(id);
		model.addAttribute("storage", storage);
		return "owner/edit-storage";
	}

	@PostMapping("/edit")
	public String updateStorage(@ModelAttribute("storage") Storage storage, Model model) {
		Storage existingStorage = storageService.findById(storage.getId());
		Integer totalCapacityBefore = existingStorage.getTotalCapacity();
		Integer availableCapacityBefore = existingStorage.getAvailableCapacity();
		Integer total = totalCapacityBefore;
		Integer available = availableCapacityBefore;
		if (totalCapacityBefore < storage.getTotalCapacity()) {
			total = storage.getTotalCapacity();
			available = availableCapacityBefore + (storage.getTotalCapacity() - totalCapacityBefore);
			model.addAttribute("msg", "You increased your Storage capacity...!!!");
		} else if (storage.getTotalCapacity() < totalCapacityBefore) {
			if ((totalCapacityBefore - storage.getTotalCapacity()) <= availableCapacityBefore) {
				total = storage.getTotalCapacity();
				available = availableCapacityBefore - (totalCapacityBefore - storage.getTotalCapacity());
				model.addAttribute("msg", "You reduced your Storage capacity...!!!");
			} else {
				model.addAttribute("msg", "Your storage available capacity can not be decreased...!!!!!!!");
			}
		}
		existingStorage.setName(storage.getName());
		existingStorage.setLocation(storage.getLocation());
		existingStorage.setTotalCapacity(total);
		existingStorage.setAvailableCapacity(available);
		storageService.save(existingStorage);
		model.addAttribute("storage", existingStorage);
		return "owner/edit-storage";
	}

	@GetMapping("/addoffer")
	public String addOfferForm(@RequestParam("id") Integer id, Model model) {
		Storage storage = storageService.findById(id);
		Offer offer = storage.getOffer();
		if (offer == null) {
			model.addAttribute("msg", "No Offer Added In Your Storage....!!!!!!!!!!!!!");
			model.addAttribute("offer", new Offer());
		} else {
			model.addAttribute("offer", offer);
			model.addAttribute("msg", "Offer Added In Your Storage....!!!!!!!!!!!!!");
		}
		model.addAttribute("storageid", id);
		return "owner/storage-offer";
	}

	@PostMapping("/addoffer")
	public String saveOffer(Offer offer, Model model, HttpServletRequest request) {
		Integer storageId = Integer.valueOf(request.getParameter("storageid"));
		offer.setDate(LocalDate.now());
		offer.setStorage(storageService.findById(storageId));
		offerService.save(offer);
		return "redirect:/owners/addoffer?id=" + storageId;
	}

	@GetMapping("/deleteoffer")
	public String deleteOffer(@RequestParam("id") Integer id, Model model) {
		Offer offer = offerService.findById(id);
		Storage storage = offer.getStorage();
		storage.setOffer(null);
		offerService.deleteById(id);
		return "redirect:/owners/addoffer?id=" + storage.getId();
	}

	@GetMapping("/managespace")
	public String manageSpacePage(@RequestParam("id") Integer id, Model model) {
		Storage storage = storageService.findById(id);
		List<Product> products = storage.getProducts();
		model.addAttribute("productList", products);
		model.addAttribute("product", new Product());
		model.addAttribute("storageId", storage.getId());
		model.addAttribute("storageAvlCap", storage.getAvailableCapacity());
		return "owner/manage-space";
	}

	@PostMapping("editproduct")
	public String editProduct(HttpServletRequest request, Model model) {
		Product product = productService.findById(Integer.valueOf(request.getParameter("productId")));
		Integer storageId = Integer.valueOf(request.getParameter("storageId"));
		List<Product> products = storageService.findById(storageId).getProducts();
		model.addAttribute("productList", products);
		model.addAttribute("storageId", storageId);
		model.addAttribute("product", product);
		model.addAttribute("storageAvlCap", product.getStorage().getAvailableCapacity());
		return "owner/manage-space";
	}

	@PostMapping("/deleteproduct")
	public String deleteProduct(HttpServletRequest request) {
		Integer productId = Integer.valueOf(request.getParameter("productId"));
		Product product = productService.findById(productId);
		Integer storageId = Integer.valueOf(request.getParameter("storageId"));
		Storage storage = storageService.findById(storageId);
		storage.setAvailableCapacity(storage.getAvailableCapacity() + product.getTotalCapacity());
		productService.deleteById(productId);
		return "redirect:/owners/managespace?id=" + storageId;
	}

	@PostMapping("/saveproduct")
	public String saveproduct(@ModelAttribute("product") Product product, Model model, HttpServletRequest request) {
		Integer storageId = Integer.valueOf(request.getParameter("storageId"));
		Storage storage = storageService.findById(storageId);
		String msg = "";
		if (product.getId() == null) {
			if (storage.getAvailableCapacity() < product.getTotalCapacity())
				msg = "Storage Capacity Limit Exceed...!!!Please Try Again.....!!!";
			else {
				product.setAvailableCapacity(product.getTotalCapacity());
				storage.setAvailableCapacity(storage.getAvailableCapacity() - product.getTotalCapacity());
				storageService.save(storage);
				product.setStorage(storage);
				productService.save(product);
				msg = "Product added succesfully..!!";
			}
		} else {
			Integer capacitybefore = Integer.valueOf(request.getParameter("capacityBefore"));
			Integer availablecapacitybefore = product.getAvailableCapacity();
			if (capacitybefore < product.getTotalCapacity()) {
				if (storage.getAvailableCapacity() < (product.getTotalCapacity() - capacitybefore))
					msg = "Storage Capacity Limit Exceed...!!!Please Try Again.....!!!";
				else {
					product.setAvailableCapacity(
							availablecapacitybefore + (product.getTotalCapacity() - capacitybefore));
					storage.setAvailableCapacity(
							storage.getAvailableCapacity() - (product.getTotalCapacity() - capacitybefore));
					storageService.save(storage);
					product.setStorage(storage);
					productService.save(product);
					msg = "Update successful(product available capacity increased....!!!<br/>Update successful(storage available capacity decreased....!!!";
				}
			} else if (capacitybefore > product.getTotalCapacity()) {
				if ((capacitybefore - product.getTotalCapacity()) <= availablecapacitybefore) {
					product.setAvailableCapacity(
							availablecapacitybefore - (capacitybefore - product.getTotalCapacity()));
					storage.setAvailableCapacity(
							storage.getAvailableCapacity() + (capacitybefore - product.getTotalCapacity()));
					storageService.save(storage);
					product.setStorage(storage);
					productService.save(product);
					msg = "Update successful(product available capacity decreased....!!!<br/>Update successful(storage available capacity increased....!!!";
				}
			} else if (capacitybefore == product.getTotalCapacity()) {
				storageService.save(storage);
				product.setStorage(storage);
				productService.save(product);
				msg = "Update successful(No change in storage available capacity....!!!";
			}
		}
		if (!msg.equals("")) {
			model.addAttribute("msg", msg);
		}
		model.addAttribute("productList", storage.getProducts());
		model.addAttribute("storageId", storageId);
		model.addAttribute("product", new Product());
		model.addAttribute("storageAvlCap", storage.getAvailableCapacity());
		return "owner/manage-space";
	}

	@GetMapping("/makeinactive")
	public String makeInActive(@RequestParam("id") Integer id, Model model) {
		Storage storage = storageService.findById(id);
		storage.setIsActive(0);
		storageService.save(storage);
		return "redirect:/owners/storages";
	}

	@GetMapping("/makeactive")
	public String makeActive(@RequestParam("id") Integer id, Model model) {
		Storage storage = storageService.findById(id);
		storage.setIsActive(1);
		storageService.save(storage);
		return "redirect:/owners/inactivetorages";
	}

	@GetMapping("/inactivetorages")
	public String inactiveStorages(Principal principal, Model model) {
		User user = userService.findByUsername(principal.getName());
		List<Storage> storages = user.getStorages();
		storages.removeIf(e -> e.getIsActive().equals(1));
		model.addAttribute("storages", storages);
		return "owner/inactive-storages";
	}

	@PostMapping("/storagestatisticsdata")
	public @ResponseBody List<StatisticDataModel> storageStatisticsData(Principal principal, Model model) {
		User user = userService.findByUsername(principal.getName());
		List<Storage> storages = user.getStorages();
		List<StatisticDataModel> dataPoints = new ArrayList<>();
		for (Storage storage : storages) {
			Double yValue = (double) ((storage.getAvailableCapacity() * 100) / (storage.getTotalCapacity()));
			StatisticDataModel statisticDataModel = new StatisticDataModel(yValue, storage.getName());
			dataPoints.add(statisticDataModel);
		}
		return dataPoints;
	}

	@GetMapping("/storagestatisticsdata")
	public String storageStatisticsPage() {
		return "owner/storage-statistics";
	}

	@GetMapping("/productstatistics")
	public String productStatisticsPage(Principal principal, Model model) {
		model.addAttribute("storageList", userService.findByUsername(principal.getName()).getStorages());
		return "owner/product-statistics";
	}

	@PostMapping("/productstatisticsdata")
	public @ResponseBody Map<String, Object> productstatisticsData(Storage s) {
		Storage storage = storageService.findById(s.getId());
		Map<String, Object> map = new HashMap<String, Object>();
		List<StatisticDataModel> dataPoints = new ArrayList<>();
		for (Product product : storage.getProducts()) {
			Double yValue = (double) ((product.getAvailableCapacity() * 100) / (product.getTotalCapacity()));
			StatisticDataModel statisticDataModel = new StatisticDataModel(yValue, product.getName());
			dataPoints.add(statisticDataModel);
		}
		map.put("storageName", storage.getName());
		map.put("loc", storage.getLocation());
		map.put("data", dataPoints);
		return map;
	}

	@GetMapping("/reviews")
	public String reviews(Principal principal, Model model) {
		User user = userService.findByUsername(principal.getName());
		List<Review> reviews = reviewService.findAll();
		reviews.removeIf(e -> !e.getStorage().getUser().getId().equals(user.getId()));
		model.addAttribute("reviews", reviews);
		return "owner/reviews";
	}
}
