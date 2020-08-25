package com.warehouse.Controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.warehouse.constant.WarehouseConstant;
import com.warehouse.entity.Offer;
import com.warehouse.entity.Product;
import com.warehouse.entity.ProductRequest;
import com.warehouse.entity.Storage;
import com.warehouse.entity.User;
import com.warehouse.service.OfferService;
import com.warehouse.service.ProductRequestService;
import com.warehouse.service.ProductService;
import com.warehouse.service.StorageService;
import com.warehouse.service.UserService;
import com.warehouse.util.PDFGenerator;
import com.warehouse.util.Utils;

@Controller
@RequestMapping("/owners/request")
public class ProductRequestController {

	@Autowired
	private UserService userService;

	@Autowired
	private StorageService storageService;

	@Autowired
	private ProductRequestService productRequestService;

	@Autowired
	private OfferService offerService;

	@Autowired
	private ProductService productService;

	@Autowired
	private PDFGenerator pdfGenerator;

	@GetMapping("/pending")
	public String pendingRequest(Principal principal, Model model) {
		User user = userService.findByUsername(principal.getName());
		List<ProductRequest> productRequests = productRequestService.ownerPendingRequest(user);
		model.addAttribute("productRequestsList", productRequests);
		return "owner/owner-pending-request";
	}

	@GetMapping("/storagecapacity")
	public String storageCapacity(@RequestParam("id") Integer id, Model model) {
		Storage storage = storageService.findById(id);
		model.addAttribute("storage", storage);
		return "owner/storage-product-capacity";
	}

	@GetMapping("/rejectrequest")
	public String rejectRequest(@RequestParam("id") Integer id, Model model) {
		productRequestService.deleteById(id);
		return "redirect:/owners/request/pending";
	}

	@GetMapping("/acceptrequest")
	public String acceptRequest(@RequestParam("id") Integer id, Model model, Principal principal) {
		offerService.deleteExpiredOffer();
		ProductRequest productRequest = productRequestService.findById(id);
		Product product = productRequest.getProduct();
		if (productRequest.getCapacity() <= product.getAvailableCapacity()) {
			productRequest.setStatus(WarehouseConstant.ACCPETED_STATUS);
			productRequest.setDate(LocalDate.now());
			product.setAvailableCapacity(product.getAvailableCapacity() - productRequest.getCapacity());
			productService.save(product);
			productRequestService.save(productRequest);
			model.addAttribute("msg", "Product Added in your storage successfully..!!!!");
		} else {
			model.addAttribute("msg", "Storage Capacity Limit Exceed...!!!Please Try Again.....!!!");
		}
		User user = userService.findByUsername(principal.getName());
		List<ProductRequest> productRequests = productRequestService.ownerPendingRequest(user);
		model.addAttribute("productRequestsList", productRequests);
		return "owner/owner-pending-request";
	}

	@PostMapping("/checkout")
	public String checkout(HttpServletRequest request, Model model) {
		Integer id = Integer.valueOf(request.getParameter("id"));
		ProductRequest productRequest = productRequestService.findById(id);
		productRequest.setStatus(WarehouseConstant.CHECKOUT_STATUS);
		LocalDate acceptedDate = productRequest.getDate();
		LocalDate checkoutDate = LocalDate.now();
		Integer duration = Utils.getDaysBetweenDates(acceptedDate, checkoutDate);
		productRequest.setDuration(duration);
		Integer totalCost = productRequest.getProduct().getPrice() * productRequest.getCapacity() * duration;
		productRequest.setCost(totalCost);
		Offer offer = productRequest.getProduct().getStorage().getOffer();
		if (offer != null) {
			Integer mainCost = totalCost - ((offer.getDiscount() * totalCost) / 100);
			productRequest.setCost(mainCost);
			model.addAttribute("maincost", mainCost);
		}
		productRequest.setDate(checkoutDate);
		Product product = productRequest.getProduct();
		product.setAvailableCapacity(product.getAvailableCapacity() + productRequest.getCapacity());
		productService.save(product);
		productRequestService.save(productRequest);
		model.addAttribute("totalcost", totalCost);
		model.addAttribute("request", productRequest);
		return "owner/checkout-summary";
	}

	@GetMapping("/accepted")
	public String accepetedRequest(Principal principal, Model model) {
		User user = userService.findByUsername(principal.getName());
		List<ProductRequest> productRequests = productRequestService.ownerAcceptedRequest(user);
		model.addAttribute("productRequestsList", productRequests);
		return "owner/owner-accepted-request";
	}

	@GetMapping("/transaction")
	public String transaction(Principal principal, Model model) {
		User user = userService.findByUsername(principal.getName());
		List<ProductRequest> productRequests = productRequestService.ownerTransaction(user);
		model.addAttribute("productRequestsList", productRequests);
		return "owner/owner-transaction";
	}

	@GetMapping("transactionreport")
	public String showTransactionReport(Principal principal) {
		User user = userService.findByUsername(principal.getName());
		List<ProductRequest> productRequests = productRequestService.ownerTransaction(user);
		List<String> columnNames = new ArrayList<>();
		columnNames.add("Owner Email");
		columnNames.add("Storage ID");
		columnNames.add("Storage Name");
		columnNames.add("Product ID");
		columnNames.add("Product Name");
		columnNames.add("Product Price");
		columnNames.add("Customer Email");
		columnNames.add("Product Weight");
		columnNames.add("Duration");
		columnNames.add("Date");
		pdfGenerator.setPdfData("Owner_Transaction_Report", 10, columnNames, WarehouseConstant.OWNER_TRANSACTION_REPORT,
				productRequests);
		pdfGenerator.generatePdfReport();
		return "redirect:/owners/request/transaction";
	}
}
