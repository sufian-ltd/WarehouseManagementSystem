package com.warehouse.Controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.warehouse.entity.Product;
import com.warehouse.entity.ProductRequest;
import com.warehouse.entity.Review;
import com.warehouse.entity.Storage;
import com.warehouse.entity.User;
import com.warehouse.model.StatisticDataModel;
import com.warehouse.service.OfferService;
import com.warehouse.service.ProductRequestService;
import com.warehouse.service.ProductService;
import com.warehouse.service.ReviewService;
import com.warehouse.service.StorageService;
import com.warehouse.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private StorageService storageService;

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductRequestService productRequestService;

	@Autowired
	private ReviewService reviewService;

	@Autowired
	private OfferService offerService;

	@GetMapping("/dashboard")
	public String dashboard() {
		return "user/user-dashboard";
	}

	@GetMapping("/findstorages")
	public String findStorages(Model model) {
		offerService.deleteExpiredOffer();
		model.addAttribute("storagesList", storageService.findByIsActive(1));
		return "user/user-find-storage";
	}

	@GetMapping("/pendingrequest")
	public String pendingRequest(Principal principal, Model model) {
		User user = userService.findByUsername(principal.getName());
		List<ProductRequest> productRequests = productRequestService.userPendingRequest(user);
		model.addAttribute("productRequestsList", productRequests);
		return "user/user-pending-request";
	}

	@GetMapping("/acceptedrequest")
	public String acceptedRequest(Principal principal, Model model) {
		User user = userService.findByUsername(principal.getName());
		List<ProductRequest> productRequests = productRequestService.userAcceptedRequest(user);
		model.addAttribute("productRequestsList", productRequests);
		return "user/user-accepted-request";
	}

	@GetMapping("/transaction")
	public String transaction(Principal principal, Model model) {
		User user = userService.findByUsername(principal.getName());
		List<ProductRequest> productRequests = productRequestService.userTransaction(user);
		model.addAttribute("productRequestsList", productRequests);
		return "user/user-transaction";
	}

	@GetMapping("/cancelrequest")
	public String cancelReqeust(@RequestParam("id") Integer id) {
		productRequestService.deleteById(id);
		return "redirect:/users/pendingrequest";
	}

	@GetMapping("/viewoffer")
	public String viewOffer(@RequestParam("id") Integer id, Model model) {
		Storage storage = storageService.findById(id);
		if (storage.getOffer() != null) {
			model.addAttribute("offer", storage.getOffer());
			return "user/view-offer";
		} else {
			return "redirect:/users/findstorages";
		}
	}

	@GetMapping("/checkdetails")
	public String checkDetails(@RequestParam("id") Integer id, Model model) {
		Storage storage = storageService.findById(id);
		model.addAttribute("storageid", id);
		model.addAttribute("productList", storage.getProducts());
		if (storage.getOffer() != null) {
			model.addAttribute("discount", "Storage offer : " + storage.getOffer().getDiscount() + "% Disocunt");
		}
		return "user/view-products";
	}

	@GetMapping("/sendrequest")
	public String sendRequest(@RequestParam("id") Integer id, Model model) {
		model.addAttribute(productService.findById(id));
		return "user/send-request";
	}

	@PostMapping("/sendrequest")
	public String sendReqest(Principal principal, HttpServletRequest request, Model model) {
		Integer weight = Integer.valueOf(request.getParameter("weight"));
		Integer productId = Integer.valueOf(request.getParameter("productId"));
		User user = userService.findByUsername(principal.getName());
		Product product = productService.findById(productId);
		ProductRequest productRequest = new ProductRequest();
		if (weight > product.getAvailableCapacity()) {
			model.addAttribute("msg", "Storage Capacity Limit Exceed...!!!Please Try Again.....!!!");
			model.addAttribute(product);
			return "user/send-request";
		} else {
			productRequest.setCapacity(weight);
			productRequest.setDuration(0);
			productRequest.setCost(0);
			productRequest.setDate(LocalDate.now());
			productRequest.setStatus("pending");
			productRequest.setProduct(product);
			productRequest.setUser(user);
			productRequestService.save(productRequest);
			return "redirect:/users/pendingrequest";
		}
	}

	@GetMapping("/addreview")
	public String addReview(@RequestParam("id") Integer id, Model model) {
		Storage storage = storageService.findById(id);
		model.addAttribute("storage", storage);
		model.addAttribute("review", new Review());
		return "user/add-review";
	}

	@PostMapping("/addreview")
	public String addReview(HttpServletRequest request, Model model, Principal principal) {
		Storage storage = storageService.findById(Integer.valueOf(request.getParameter("storageId")));
		User user = userService.findByUsername(principal.getName());
		String message = request.getParameter("message").trim();
		Review review = new Review();
		review.setMessage(message);
		review.setUser(user);
		review.setStorage(storage);
		review.setDate(LocalDate.now());
		reviewService.save(review);
		System.out.println("af-id = " + review.getId());
		model.addAttribute("msg", "Review added successfully....!!!!");
		model.addAttribute(storage);
		model.addAttribute("review", new Review());
		return "user/add-review";
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

	@GetMapping("/myreviews")
	public String myReviews(Principal principal, Model model) {
		User user = userService.findByUsername(principal.getName());
		List<Review> reviews = user.getReviews();
		model.addAttribute("reviews", reviews);
		return "user/my-reviews";
	}

	@GetMapping("/othersreviews")
	public String othersReviews(Principal principal, Model model, @RequestParam("id") Integer id) {
		User user = userService.findByUsername(principal.getName());
		List<Review> reviews = storageService.findById(id).getReviews();
		reviews.removeIf(e -> e.getUser().getId().equals(user.getId()));
		model.addAttribute("others-reviews", reviews);
		return "user/others-reviews";
	}
}
