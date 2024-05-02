package org.oplearn.project.controller.advice;

import lombok.RequiredArgsConstructor;
import org.oplearn.project.dto.response.AddressResponse;
import org.oplearn.project.entity.Addresses;
import org.oplearn.project.service.AddressService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/addresses")
public class AddressController {
  private final AddressService addressService;

  @PostMapping("/create")
  public Addresses create() {
    return addressService.create();
  }

  @PostMapping
  public AddressResponse create(
        @RequestBody Addresses addresses
  ) {
    return addressService.create(addresses);
  }

  @GetMapping("/{id}")
  public AddressResponse get(@PathVariable Integer id) {
    return addressService.get(id);
  }

  @GetMapping
  private List<AddressResponse> getAll(){
    return addressService.getAll();
  }

  @PutMapping("/{id}")
  public String delete(@PathVariable Integer id) {
    addressService.delete(id);
    return "deleted";
  }
}
